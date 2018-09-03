package com.example.android.popularmoviesproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    private TextView titleTextView;
    private TextView synopsisTextView;
    private TextView userRatingTextView;
    private TextView releaseDateTextView;
    private ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        titleTextView = (TextView) findViewById(R.id.tv_title);
        posterImageView = (ImageView) findViewById(R.id.iv_poster);
        synopsisTextView = (TextView) findViewById(R.id.tv_synopsis);
        userRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        releaseDateTextView = (TextView) findViewById(R.id.tv_release_date);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getExtras().getString("title");
            String posterPath = intent.getExtras().getString("posterPath");
            String synopsis = intent.getExtras().getString("synopsis");
            String userRating = intent.getExtras().getString("userRating");
            String releaseDate = intent.getExtras().getString("releaseDate");

            titleTextView.setText(title);
            synopsisTextView.setText(synopsis);
            userRatingTextView.setText(userRating);
            releaseDateTextView.setText(releaseDate);

            String base = getString(R.string.image_url_base);
            String size = getString(R.string.image_url_size);
            Uri imagePath = Uri.parse(base + size + posterPath);
            Picasso.with(this).load(imagePath).into(posterImageView);
        }
    }
}
