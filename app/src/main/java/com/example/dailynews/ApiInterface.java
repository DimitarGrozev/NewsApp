package com.example.dailynews;


import com.example.dailynews.Model.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("top-headlines")
    Call<Headlines> getHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey,
            @Query("pageSize") int pageSize
    );

    @GET("top-headlines")
    Call<Headlines> getCategory(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey
    );
    @GET("top-headlines")
    Call<Headlines> getNot(
            @Query("q") String query,
            @Query("country") String country,
            @Query("category") String category,
            @Query("apiKey") String apiKey

    );

    @GET("top-headlines")
    Call<Headlines> getSpecificData(
            @Query("q") String query,
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );



}
