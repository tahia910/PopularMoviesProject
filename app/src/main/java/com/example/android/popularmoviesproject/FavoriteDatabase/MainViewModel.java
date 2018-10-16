package com.example.android.popularmoviesproject.FavoriteDatabase;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.popularmoviesproject.FavoriteDatabase.FavoriteMovieDatabase;
import com.example.android.popularmoviesproject.FavoriteDatabase.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> movieList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        FavoriteMovieDatabase favoriteMovieDatabase = FavoriteMovieDatabase.getInstance(this.getApplication());
        movieList = favoriteMovieDatabase.movieDao().loadAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getMovies(){
        return movieList;
    }

}
