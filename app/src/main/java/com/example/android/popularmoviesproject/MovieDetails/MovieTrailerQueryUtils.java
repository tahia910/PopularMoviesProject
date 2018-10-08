package com.example.android.popularmoviesproject.MovieDetails;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesproject.BuildConfig;
import com.example.android.popularmoviesproject.MainActivity;
import com.example.android.popularmoviesproject.QueryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieTrailerQueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String THEMOVIEDB_REQUEST_URL = MainActivity.THEMOVIEDB_REQUEST_URL;
    private static final String API_KEY = BuildConfig.THE_GUARDIAN_API_KEY;
    private static final String KEY_API_QUERY = MainActivity.KEY_API_QUERY;
    private static final String KEY_VIDEO_QUERY = "videos";

    private MovieTrailerQueryUtils() {
    }

    public static URL buildTrailerUrl(String movieId) {
        Uri buildUri = Uri.parse(THEMOVIEDB_REQUEST_URL).buildUpon().appendPath
                (movieId).appendPath(KEY_VIDEO_QUERY).appendQueryParameter
                (KEY_API_QUERY, API_KEY).build();
        URL trailerQueryUrl = null;
        try {
            trailerQueryUrl = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return trailerQueryUrl;
    }

    public static List<MovieTrailer> fetchMovieTrailer(URL url) {
        String jsonResponse = null;
        try {
            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<MovieTrailer> trailerList = extractFeatureFromJson(jsonResponse);
        return trailerList;
    }

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

    private static List<MovieTrailer> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<MovieTrailer> trailerList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONArray newsArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentMovieItem = newsArray.getJSONObject(i);
                String youtubeKey = currentMovieItem.getString("key");
                String title = currentMovieItem.getString("name");
                String type = currentMovieItem.getString("type");

                MovieTrailer trailerItem = new MovieTrailer(youtubeKey, title, type);
                trailerList.add(trailerItem);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return trailerList;
    }

}
