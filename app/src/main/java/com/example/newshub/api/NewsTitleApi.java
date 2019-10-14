package com.example.newshub.api;

import com.example.newshub.model.RequestInformation;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsTitleApi {

    @GET("top-headlines")
    Call<RequestInformation> getNewsTitles(@Query("category") String category, @Query("apiKey") String apiKey);
}
