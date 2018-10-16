package com.example.android.popularmoviesproject.FavoriteDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcelable;
import android.os.Parcel;

@Entity(tableName = "favorite_movie")
public class Movie implements Parcelable {

    @PrimaryKey
    private int movieId;
    private String title;
    @ColumnInfo(name = "plot_synopsis")
    private String plotSynopsis;
    @ColumnInfo(name = "user_rating")
    private String userRating;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "poster_path")
    private String posterPath;

    public Movie(int movieId, String title, String plotSynopsis, String
            userRating, String releaseDate, String posterPath) {
        this.movieId = movieId;
        this.title = title;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    @Ignore
    private Movie(Parcel in) {
        movieId = in.readInt();
        title = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getMovieId() {
        return movieId;
    }
    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }
    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }
    public void setUserRating(String userRating){this.userRating = userRating;}

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate){this.releaseDate = releaseDate;}

    public String getPosterPath() {
        return posterPath;
    }
    public void setPosterPath(String posterPath){this.posterPath = posterPath;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(plotSynopsis);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
    }
}
