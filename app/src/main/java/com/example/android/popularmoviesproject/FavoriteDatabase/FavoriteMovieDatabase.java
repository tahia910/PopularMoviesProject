package com.example.android.popularmoviesproject.FavoriteDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoriteMovieDatabase extends RoomDatabase{

    private static final String LOG_TAG = FavoriteMovieDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favoritemoviedatabase";
    private static FavoriteMovieDatabase instance;

    public static FavoriteMovieDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                instance = Room.databaseBuilder(context.getApplicationContext(),
                        FavoriteMovieDatabase.class, FavoriteMovieDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return instance;
    }

    public abstract MovieDao movieDao();

}
