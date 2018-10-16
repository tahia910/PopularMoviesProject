package com.example.android.popularmoviesproject;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesproject.FavoriteDatabase.FavoriteMovieDatabase;
import com.example.android.popularmoviesproject.FavoriteDatabase.MainViewModel;
import com.example.android.popularmoviesproject.FavoriteDatabase.Movie;
import com.example.android.popularmoviesproject.MovieDetails.MovieReview;
import com.example.android.popularmoviesproject.MovieDetails.MovieReviewAdapter;
import com.example.android.popularmoviesproject.MovieDetails.MovieReviewQueryUtils;
import com.example.android.popularmoviesproject.MovieDetails.MovieTrailer;
import com.example.android.popularmoviesproject.MovieDetails.MovieTrailerAdapter;
import com.example.android.popularmoviesproject.MovieDetails.MovieTrailerQueryUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    public static final String KEY_MOVIE_ITEM = MainActivity.KEY_MOVIE_ITEM;

    private static final int TRAILER_QUERY_LOADER = 32;
    private static final int REVIEW_QUERY_LOADER = 116;

    private static final String CURRENT_MOVIE_ID = "movieId";

    private FavoriteMovieDatabase favoriteMovieDatabase;

    @BindView(R.id.tv_title)
    TextView titleTextView;
    @BindView(R.id.tv_synopsis)
    TextView synopsisTextView;
    @BindView(R.id.tv_user_rating)
    TextView userRatingTextView;
    @BindView(R.id.tv_release_date)
    TextView releaseDateTextView;
    @BindView(R.id.iv_poster)
    ImageView posterImageView;

    @BindView(R.id.trailer_empty_view)
    TextView trailerEmptyView;
    @BindView(R.id.review_empty_view)
    TextView reviewEmptyView;

    @BindView(R.id.favorite_button)
    ImageButton favoriteButton;

    @BindView(R.id.trailer_list)
    RecyclerView trailerListRecyclerView;
    @BindView(R.id.review_list)
    RecyclerView reviewListRecyclerView;

    public boolean alreadyFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        favoriteMovieDatabase = FavoriteMovieDatabase.getInstance(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Movie currentMovie = (Movie) bundle.getParcelable(KEY_MOVIE_ITEM);
            populateUi(currentMovie);
        }
    }

    public void populateUi(final Movie currentMovie) {
        final int movieId = currentMovie.getMovieId();
        final String title = currentMovie.getTitle();
        final String plotSynopsis = currentMovie.getPlotSynopsis();
        final String userRating = currentMovie.getUserRating();
        final String releaseDate = currentMovie.getReleaseDate();
        final String posterPath = currentMovie.getPosterPath();

        titleTextView.setText(title);
        if (plotSynopsis != null && !plotSynopsis.isEmpty()) {
            synopsisTextView.setText(plotSynopsis);
        } else {
            synopsisTextView.setText(R.string.no_synopsis);
        }
        if (userRating != null && !userRating.isEmpty()) {
            String max = getString(R.string.user_rating_maximum);
            String userRatingWithMax = userRating + max;
            userRatingTextView.setText(userRatingWithMax);
        } else {
            userRatingTextView.setText(R.string.no_user_rating);
        }
        if (releaseDate != null && !releaseDate.isEmpty()) {
            releaseDateTextView.setText(releaseDate);
        } else {
            releaseDateTextView.setText(R.string.no_release_date);
        }

        alreadyFavorite = setFavoriteButton(title);
        final MainViewModel viewModel = ViewModelProviders.of(MovieDetailsActivity.this).get
                (MainViewModel.class);
        viewModel.getMovies().observe(MovieDetailsActivity.this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> favMovies) {
                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (alreadyFavorite) {
                            alreadyFavorite = false;
                            favoriteButton.setImageResource(R.drawable.ic_star_grey);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    favoriteMovieDatabase.movieDao().deleteFavoriteMovie
                                            (currentMovie);
                                }
                            });
                        } else {
                            alreadyFavorite = true;
                            favoriteButton.setImageResource(R.drawable.ic_star);
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    favoriteMovieDatabase.movieDao().insertFavoriteMovie
                                            (currentMovie);
                                }
                            });
                        }
                    }
                });
            }
        });

        String base = getString(R.string.image_url_base);
        String size = getString(R.string.image_url_size);
        Uri imagePath = Uri.parse(base + size + posterPath);
        Picasso.with(this).load(imagePath).into(posterImageView);

        Bundle currentMovieIdBundle = new Bundle();
        currentMovieIdBundle.putString(CURRENT_MOVIE_ID, String.valueOf(movieId));

        trailerListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().initLoader(TRAILER_QUERY_LOADER, currentMovieIdBundle,
                trailerLoaderCallback);

        reviewListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportLoaderManager().initLoader(REVIEW_QUERY_LOADER, currentMovieIdBundle,
                reviewLoaderCallback);
    }

    private boolean setFavoriteButton(final String title) {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(MovieDetailsActivity.this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> favMovies) {
                ArrayList<String> array = new ArrayList<>();
                int dbSize = favMovies.size();
                for (int i = 0; i < dbSize; i++) {
                    final Movie movieTest = favMovies.get(i);
                    String currentMovieTitle = movieTest.getTitle();
                    array.add(currentMovieTitle);
                }
                boolean containsMovieId = array.contains(title);
                if (!containsMovieId) {
                    alreadyFavorite = false;
                    favoriteButton.setImageResource(R.drawable.ic_star_grey);
                } else {
                    alreadyFavorite = true;
                    favoriteButton.setImageResource(R.drawable.ic_star);
                }
            }
        });
        return alreadyFavorite;
    }

    public LoaderManager.LoaderCallbacks<List<MovieTrailer>> trailerLoaderCallback = new
            LoaderManager.LoaderCallbacks<List<MovieTrailer>>() {

        @SuppressLint("StaticFieldLeak")
        @NonNull
        @Override
        public Loader<List<MovieTrailer>> onCreateLoader(int id, @Nullable final Bundle
                currentMovieIdBundle) {
            return new AsyncTaskLoader<List<MovieTrailer>>(getApplicationContext()) {
                @Override
                protected void onStartLoading() {
                    if (currentMovieIdBundle == null) {
                        trailerListRecyclerView.setVisibility(View.GONE);
                        trailerEmptyView.setVisibility(View.VISIBLE);
                        trailerEmptyView.setText(R.string.no_trailer);
                        return;
                    }
                    forceLoad();
                }

                @Override
                public List<MovieTrailer> loadInBackground() {
                    String currentMovieId = currentMovieIdBundle.getString(CURRENT_MOVIE_ID);
                    URL requestQueryUrl = MovieTrailerQueryUtils.buildTrailerUrl(currentMovieId);
                    List<MovieTrailer> trailerList = MovieTrailerQueryUtils.fetchMovieTrailer
                            (requestQueryUrl);
                    return trailerList;
                }
            };
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<MovieTrailer>> loader, final
        List<MovieTrailer> trailerList) {
            if (trailerList != null && !trailerList.isEmpty()) {
                trailerEmptyView.setVisibility(View.GONE);
                MovieTrailerAdapter trailerAdapter = new MovieTrailerAdapter(MovieDetailsActivity
                        .this, trailerList);
                trailerListRecyclerView.setAdapter(trailerAdapter);

                trailerAdapter.setOnItemClickListener(new MovieTrailerAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        MovieTrailer currentMovieTrailer = trailerList.get(position);
                        String youtubeKey = currentMovieTrailer.getYoutubeKey();
                        Uri trailerUrl = Uri.parse(YOUTUBE_BASE_URL + youtubeKey);
                        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, trailerUrl);
                        startActivity(youtubeIntent);
                    }
                });
            } else {
                trailerListRecyclerView.setVisibility(View.GONE);
                trailerEmptyView.setVisibility(View.VISIBLE);
                trailerEmptyView.setText(R.string.no_trailer);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<MovieTrailer>> loader) {
        }
    };

    private LoaderManager.LoaderCallbacks<List<MovieReview>> reviewLoaderCallback = new
            LoaderManager.LoaderCallbacks<List<MovieReview>>() {
        @SuppressLint("StaticFieldLeak")
        @NonNull
        @Override
        public Loader<List<MovieReview>> onCreateLoader(int id, @Nullable final Bundle
                currentMovieIdBundle) {
            return new AsyncTaskLoader<List<MovieReview>>(getApplicationContext()) {
                @Override
                protected void onStartLoading() {
                    if (currentMovieIdBundle == null) {
                        reviewListRecyclerView.setVisibility(View.GONE);
                        reviewEmptyView.setVisibility(View.VISIBLE);
                        reviewEmptyView.setText(R.string.no_review);
                        return;
                    }
                    forceLoad();
                }

                @Override
                public List<MovieReview> loadInBackground() {
                    String currentMovieId = currentMovieIdBundle.getString(CURRENT_MOVIE_ID);
                    URL requestQueryUrl = MovieReviewQueryUtils.buildReviewUrl(currentMovieId);
                    List<MovieReview> reviewList = MovieReviewQueryUtils.fetchMovieReview
                            (requestQueryUrl);
                    return reviewList;
                }
            };
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<MovieReview>> loader, final
        List<MovieReview> reviewList) {
            if (reviewList != null && !reviewList.isEmpty()) {
                reviewEmptyView.setVisibility(View.GONE);
                MovieReviewAdapter reviewAdapter = new MovieReviewAdapter(MovieDetailsActivity
                        .this, reviewList);
                reviewListRecyclerView.setAdapter(reviewAdapter);

            } else {
                reviewListRecyclerView.setVisibility(View.GONE);
                reviewEmptyView.setVisibility(View.VISIBLE);
                reviewEmptyView.setText(R.string.no_review);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<MovieReview>> loader) {
        }
    };

}
