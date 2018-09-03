package com.example.android.popularmoviesproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Movie> movieList;
    private RecyclerView recyclerView;
    private TextView emptyStateTextView;
    private ProgressBar spinner;
    private MoviePosterAdapter adapter;

    private static final String THEMOVIEDB_REQUEST_URL = "https://api.themoviedb" +
            ".org/3/discover/movie";

    /** Please put your API key here. **/
    private static final String API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (ProgressBar) findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);

        String name = getString(R.string.image_url_base);
        //Check if the device is connected to the internet.
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected == false) {
            emptyStateTextView.setText(R.string.no_internet_connection);
        } else {
            recyclerView = (RecyclerView) findViewById(R.id.rv_movies);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            new FetchMovieTask().execute();
        }
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
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences
                    (MainActivity.this);

            String sortBy = sharedPrefs.getString(getString(R.string.settings_sort_by_key),
                    getString(R.string.settings_sort_by_default));

            Uri buildUri = Uri.parse(THEMOVIEDB_REQUEST_URL).buildUpon().appendQueryParameter
                    ("sort_by", sortBy).appendQueryParameter("api_key", API_KEY).build();

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
         * movie to the MovieDetails activity and display the information.
         **/
        @Override
        protected void onPostExecute(final List<Movie> movieList) {
            if (movieList != null && !movieList.isEmpty()) {
                adapter = new MoviePosterAdapter(MainActivity.this, movieList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new MoviePosterAdapter.ClickListener() {

                    @Override
                    public void onItemClick(int position, View v) {
                        Class destinationClass = MovieDetails.class;
                        Movie currentMovie = movieList.get(position);
                        String id = currentMovie.getId();
                        String title = currentMovie.getTitle();
                        String posterPath = currentMovie.getPosterPath();
                        String synopsis = currentMovie.getPlotSynopsis();
                        String userRating = currentMovie.getUserRating();
                        String releaseDate = currentMovie.getReleaseDate();

                        Intent intentToStartDetailActivity = new Intent(MainActivity.this,
                                destinationClass);
                        intentToStartDetailActivity.putExtra("movieItem", new Movie(id, title,
                                posterPath, synopsis, userRating, releaseDate));
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

