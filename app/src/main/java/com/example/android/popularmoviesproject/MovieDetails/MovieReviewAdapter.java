package com.example.android.popularmoviesproject.MovieDetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesproject.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter
        .MovieReviewAdapterViewHolder> {

    @BindView(R.id.review_author)
    TextView reviewAuthor;
    @BindView(R.id.review_content)
    TextView reviewContent;

    private Context context;
    private final List<MovieReview> movieReviewList;
    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MovieReviewAdapter.clickListener = clickListener;
    }

    public MovieReviewAdapter(Context context, List<MovieReview> movieReviewList) {
        this.context = context;
        this.movieReviewList = movieReviewList;
    }


    @Override
    public MovieReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        context = parent.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        ButterKnife.bind(this, view);
        MovieReviewAdapterViewHolder viewHolder = new MovieReviewAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieReviewAdapterViewHolder viewHolder, int i) {
        final MovieReview currentMovieReview = movieReviewList.get(i);
        reviewAuthor.setText(currentMovieReview.getAuthor());
        reviewContent.setText(currentMovieReview.getContent());
    }

    @Override
    public int getItemCount() {
        if (null == movieReviewList) return 0;
        return movieReviewList.size();
    }


    public class MovieReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        private MovieReviewAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
