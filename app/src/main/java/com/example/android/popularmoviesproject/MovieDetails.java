package com.example.android.popularmoviesproject;

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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Movie currentMovie = (Movie) bundle.getParcelable("movieItem");
            String title = currentMovie.getTitle();
            String posterPath = currentMovie.getPosterPath();
            String synopsis = currentMovie.getPlotSynopsis();
            String userRating = currentMovie.getUserRating();
            String releaseDate = currentMovie.getReleaseDate();

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
