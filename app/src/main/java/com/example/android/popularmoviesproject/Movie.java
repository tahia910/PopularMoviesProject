package com.example.android.popularmoviesproject;

public class Movie {

    private final String id;
    private final String title;
    private final String posterPath;
    private final String plotSynopsis;
    private final String userRating;
    private final String releaseDate;

    public Movie(String id, String title, String posterPath, String plotSynopsis, String
            userRating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.plotSynopsis = plotSynopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

}
