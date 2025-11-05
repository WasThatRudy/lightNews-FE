package com.example.ainews.data.api;

import com.example.ainews.data.dto.FeedResponse;
import com.example.ainews.data.dto.PreferencesResponse;
import com.example.ainews.data.dto.UpdateFiltersRequest;
import com.example.ainews.data.dto.AuthRequest;
import com.example.ainews.data.dto.AuthResponse;
import com.example.ainews.data.dto.InitRequest;
import com.example.ainews.data.dto.InitResponse;
import com.example.ainews.data.dto.SwipeRequest;
import com.example.ainews.data.dto.SwipeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsApiService {
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body AuthRequest body);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthRequest body);
    @POST("api/init")
    Call<InitResponse> init(@Body InitRequest body);

    @POST("api/swipe")
    Call<SwipeResponse> swipe(@Body SwipeRequest body);

    @GET("api/feed")
    Call<FeedResponse> feed(
            @Query("userId") String userId,
            @Query("category") String category,
            @Query("pageSize") Integer pageSize
    );

    @GET("api/categories")
    Call<FeedResponse.CategoriesResponse> categories();

    @GET("api/user/{id}/preferences")
    Call<PreferencesResponse> preferences(@Path("id") String userId);

    @retrofit2.http.PATCH("api/user/{id}/filters")
    Call<PreferencesResponse> updateFilters(@Path("id") String userId, @Body UpdateFiltersRequest body);
}


