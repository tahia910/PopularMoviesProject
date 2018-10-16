package com.example.android.popularmoviesproject;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesproject.FavoriteDatabase.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.MovieAdapterViewHolder> {

    private Context context;
    private final List<Movie> movieList;

    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener){
        MoviePosterAdapter.clickListener = clickListener;
    }

    public MoviePosterAdapter(Context context, List<Movie> movieList){
        this.context = context;
        this.movieList = movieList;
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.item_details;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        MovieAdapterViewHolder viewHolder = new MovieAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder viewHolder, int i) {
        final Movie movieItem = movieList.get(i);
        String base = context.getString(R.string.image_url_base);
        String size = context.getString(R.string.image_url_size);
        Uri imagePath = Uri.parse(base + size + movieItem.getPosterPath());
        Picasso.with(context).load(imagePath).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        if (null == movieList) return 0;
        return movieList.size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;

        private MovieAdapterViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.item_details_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
