package com.example.android.popularmoviesproject.MovieDetails;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesproject.MoviePosterAdapter;
import com.example.android.popularmoviesproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter
        .MovieTrailerAdapterViewHolder> {

    @BindView(R.id.trailer_type)
    TextView trailerType;
    @BindView(R.id.trailer_title)
    TextView trailerTitle;

    private Context context;
    private final List<MovieTrailer> movieTrailerList;
    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MovieTrailerAdapter.clickListener = clickListener;
    }

    public MovieTrailerAdapter(Context context, List<MovieTrailer> movieTrailerList) {
        this.context = context;
        this.movieTrailerList = movieTrailerList;
    }


    @Override
    public MovieTrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        ButterKnife.bind(this, view);
        MovieTrailerAdapterViewHolder viewHolder = new MovieTrailerAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapterViewHolder viewHolder, int i) {
        final MovieTrailer currentMovieTrailer = movieTrailerList.get(i);
        trailerType.setText(currentMovieTrailer.getType());
        trailerTitle.setText(currentMovieTrailer.getTitle());
    }

    @Override
    public int getItemCount() {
        if (null == movieTrailerList) return 0;
        return movieTrailerList.size();
    }


    public class MovieTrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        private MovieTrailerAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

}
