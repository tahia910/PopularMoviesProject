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

public class MovieReviewQueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String THEMOVIEDB_REQUEST_URL = MainActivity.THEMOVIEDB_REQUEST_URL;
    private static final String API_KEY = BuildConfig.THE_GUARDIAN_API_KEY;
    private static final String KEY_API_QUERY = MainActivity.KEY_API_QUERY;
    private static final String KEY_REVIEW_QUERY = "reviews";

    private MovieReviewQueryUtils() {
    }

    public static URL buildReviewUrl(String movieId) {
        Uri buildUri = Uri.parse(THEMOVIEDB_REQUEST_URL).buildUpon().appendPath
                (movieId).appendPath(KEY_REVIEW_QUERY).appendQueryParameter
                (KEY_API_QUERY, API_KEY).build();
        URL reviewQueryUrl = null;
        try {
            reviewQueryUrl = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return reviewQueryUrl;
    }

    public static List<MovieReview> fetchMovieReview(URL url) {
        String jsonResponse = null;
        try {
            jsonResponse = getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<MovieReview> reviewList = extractFeatureFromJson(jsonResponse);
        return reviewList;
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

    private static List<MovieReview> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<MovieReview> reviewList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONArray newsArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentMovieItem = newsArray.getJSONObject(i);
                String reviewAuthor = currentMovieItem.getString("author");
                String reviewContent = currentMovieItem.getString("content");
                String reviewId = currentMovieItem.getString("id");

                MovieReview reviewItem = new MovieReview(reviewAuthor, reviewContent, reviewId);
                reviewList.add(reviewItem);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }
        return reviewList;
    }
}
