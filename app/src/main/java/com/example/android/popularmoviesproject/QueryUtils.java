package com.example.android.popularmoviesproject;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Helper methods related to requesting and receiving data to/from the themoviedb.org API.
 */
public final class QueryUtils extends AppCompatActivity {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Movie> fetchMovieData(URL url) {
        String jsonResponse = null;
        try {
            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Movie> movieList = extractFeatureFromJson(jsonResponse);
        return movieList;
    }

    /**
     * Make an HTTP request with the given URL, use Scanner to retrieve the Json response from
     * the stream, then close the connection.
     */
    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Parse the Json response to get a list of {@link Movie} objects,
     * then return them as a list of Movies to the MainActivity.
     **/
    private static List<Movie> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<Movie> movieList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONArray newsArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentMovieItem = newsArray.getJSONObject(i);
                String id = currentMovieItem.getString("id");
                String title = currentMovieItem.getString("title");
                String posterPath = currentMovieItem.getString("poster_path");
                String plotSynopsis = currentMovieItem.getString("overview");
                String userRating = currentMovieItem.getString("vote_average");
                String releaseDate = currentMovieItem.getString("release_date");

                Movie movieItem = new Movie(id, title, posterPath, plotSynopsis, userRating,
                        releaseDate);
                movieList.add(movieItem);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        return movieList;
    }
}
