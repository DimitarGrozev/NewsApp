package com.example.dailynews;

import com.example.dailynews.Model.Suggestion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AutocompleteInterface {

    @GET("/sug")
    Call<List<Suggestion>> getSuggestions(
            @Query("s") String suggestion
    );
}
