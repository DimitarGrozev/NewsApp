package com.example.dailynews;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AutocompleteClient {
    private static final String BASE_URL = "https://api.datamuse.com";
    private static AutocompleteClient apiClient;
    private static Retrofit retrofit;

    private AutocompleteClient(){
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized AutocompleteClient getInstance(){
        if (apiClient == null){
            apiClient = new AutocompleteClient();
        }
        return apiClient;
    }


    public AutocompleteInterface getApi(){
        return retrofit.create(AutocompleteInterface.class);
    }
}
