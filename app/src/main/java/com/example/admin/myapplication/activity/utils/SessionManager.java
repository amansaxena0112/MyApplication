package com.example.admin.myapplication.activity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.admin.myapplication.activity.model.MovieModel;
import com.example.admin.myapplication.activity.model.MovieResultModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SessionManager {

    private static final String PREF_NAME = "movieInfoPref";
    private static final String SAVED_MOVIES = "movies";
    private static final String SAVED_FAVOURITES = "favourites";

    private final SharedPreferences pref;
    private final Editor editor;
    private final Gson gson = new Gson();

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    /**
     * Get stored movie
     */
    public MovieModel getSavedMovies() {
        String str = pref.getString(SAVED_MOVIES, null);
        return gson.fromJson(str, MovieModel.class);
    }

    /**
     * Create saved movie
     */
    public void setSaveMovies(MovieModel movieModel) {
        editor.putString(SAVED_MOVIES, gson.toJson(movieModel));
        editor.commit();
    }

    /**
     * Get stored movie
     */
    public List<MovieResultModel> getSavedFavourites() {
        String str = pref.getString(SAVED_FAVOURITES, null);
        Type listType = new TypeToken<List<MovieResultModel>>() {
        }.getType();
        List<MovieResultModel> favList = new Gson().fromJson(str, listType);
        return favList;
    }

    /**
     * Create saved movie
     */
    public void setSavedFavourites(List<MovieResultModel> movieModel) {
        editor.putString(SAVED_FAVOURITES, gson.toJson(movieModel));
        editor.commit();
    }
}
