package com.example.android.popularmoviesproject;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesproject.FavoriteDatabase.FavoriteMovieDatabase;
import com.example.android.popularmoviesproject.FavoriteDatabase.MainViewModel;
import com.example.android.popularmoviesproject.FavoriteDatabase.Movie;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BuildConfig;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private TextView emptyStateTextView;
    private ProgressBar spinner;
    private MoviePosterAdapter adapter;

    public static final String THEMOVIEDB_REQUEST_URL = "http://api.themoviedb.org/3/movie";
    private static final String API_KEY = BuildConfig.THE_GUARDIAN_API_KEY;
    public static final String KEY_API_QUERY = "api_key";
    public static final String KEY_MOVIE_ITEM = "movieItem";

    private FavoriteMovieDatabase favoriteMovieDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.rv_movies);

        favoriteMovieDatabase = FavoriteMovieDatabase.getInstance(getApplicationContext());

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        LiveData<List<Movie>> favMovies = viewModel.getMovies();

        spinner = (ProgressBar) findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);

        //Check if the device is connected to the internet.
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            emptyStateTextView.setText(R.string.no_internet_connection);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            decideWhichMovieOptionToDisplay();
        }
    }

    private void decideWhichMovieOptionToDisplay() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String sortBy = sharedPreferences.getString(getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default));

        if (sortBy.equals(getString(R.string.settings_sort_by_most_popular_value))) {
            getSupportActionBar().setTitle(R.string.settings_sort_by_most_popular_label);
            new FetchMovieTask().execute();
        } else if (sortBy.equals(getString(R.string.settings_sort_by_highest_rate_value))) {
            getSupportActionBar().setTitle(R.string.settings_sort_by_highest_rate_label);
            new FetchMovieTask().execute();
        } else if (sortBy.equals(getString(R.string.settings_sort_by_favorite_value))) {
            getSupportActionBar().setTitle(R.string.settings_sort_by_favorite_label);
            retrieveFavoriteMovies();
        }
    }

    private void retrieveFavoriteMovies() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        LiveData<List<Movie>> favMovies = viewModel.getMovies();
        favMovies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> favMovies) {
                adapter = new MoviePosterAdapter(MainActivity.this, favMovies);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new MoviePosterAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Class destinationClass = MovieDetailsActivity.class;
                        Movie currentMovie = favMovies.get(position);
                        int movieId = currentMovie.getMovieId();
                        String title = currentMovie.getTitle();
                        String synopsis = currentMovie.getPlotSynopsis();
                        String userRating = currentMovie.getUserRating();
                        String releaseDate = currentMovie.getReleaseDate();
                        String posterPath = currentMovie.getPosterPath();

                        Intent intentToStartDetailActivity = new Intent(MainActivity
                                .this, destinationClass);
                        intentToStartDetailActivity.putExtra(KEY_MOVIE_ITEM, new Movie(movieId,
                                title, synopsis, userRating, releaseDate, posterPath));
                        startActivity(intentToStartDetailActivity);
                    }
                });
            }
        });
    }


    /**
     * Fetch the movie data on another thread with AsyncTask.
     **/
    public class FetchMovieTask extends AsyncTask<URL, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(URL... url) {
            URL requestUrl = buildUrl();
            List<Movie> result = QueryUtils.fetchMovieData(requestUrl);
            return result;
        }

        /**
         * Check the preferences and build the query url.
         **/
        private URL buildUrl() {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String sortBy = sharedPreferences.getString(getString(R.string.settings_sort_by_key),
                    getString(R.string.settings_sort_by_default));

            Uri buildUri = Uri.parse(THEMOVIEDB_REQUEST_URL).buildUpon().appendPath(sortBy)
                    .appendQueryParameter(KEY_API_QUERY, API_KEY).build();
            URL url = null;
            try {
                url = new URL(buildUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }

        /**
         * After getting the movie data, set the adapter and the click listener.
         * When the user clicks on a movie poster, an intent will send the information about the
         * movie to the MovieDetailsActivity activity and display the information.
         **/
        @Override
        protected void onPostExecute(final List<Movie> movieList) {
            if (movieList != null && !movieList.isEmpty()) {
                MoviePosterAdapter adapter = new MoviePosterAdapter(MainActivity.this, movieList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new MoviePosterAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Class destinationClass = MovieDetailsActivity.class;
                        Movie currentMovie = movieList.get(position);
                        int movieId = currentMovie.getMovieId();
                        String title = currentMovie.getTitle();
                        String synopsis = currentMovie.getPlotSynopsis();
                        String userRating = currentMovie.getUserRating();
                        String releaseDate = currentMovie.getReleaseDate();
                        String posterPath = currentMovie.getPosterPath();

                        Intent intentToStartDetailActivity = new Intent(MainActivity.this,
                                destinationClass);
                        intentToStartDetailActivity.putExtra(KEY_MOVIE_ITEM, new Movie(movieId,
                                title, synopsis, userRating, releaseDate, posterPath));
                        startActivity(intentToStartDetailActivity);
                    }
                });
            } else {
                emptyStateTextView.setText(R.string.no_movies);
            }
            spinner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        decideWhichMovieOptionToDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

