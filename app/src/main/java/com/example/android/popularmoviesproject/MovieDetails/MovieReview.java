package com.example.android.popularmoviesproject.MovieDetails;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieReview implements Parcelable {

    private final String author;
    private final String content;
    private final String reviewId;

    public MovieReview(String author, String content, String reviewId) {
        this.author = author;
        this.content = content;
        this.reviewId = reviewId;;
    }

    private MovieReview(Parcel in) {
        author = in.readString();
        content = in.readString();
        reviewId = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getReviewId() {
        return reviewId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(reviewId);
    }
}
