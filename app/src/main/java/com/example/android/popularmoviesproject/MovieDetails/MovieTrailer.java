package com.example.android.popularmoviesproject.MovieDetails;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieTrailer implements Parcelable {

    private final String youtubeKey;
    private final String title;
    private final String type;

    public MovieTrailer(String youtubeKey, String title, String type) {
        this.youtubeKey = youtubeKey;
        this.title = title;
        this.type = type;;
    }

    private MovieTrailer(Parcel in) {
        youtubeKey = in.readString();
        title = in.readString();
        type = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel in) {
            return new MovieTrailer(in);
        }

        @Override
        public MovieTrailer[] newArray(int size) {
            return new MovieTrailer[size];
        }
    };

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(youtubeKey);
        dest.writeString(title);
        dest.writeString(type);
    }
}
