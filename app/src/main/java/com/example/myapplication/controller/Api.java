package com.example.myapplication.controller;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.example.myapplication.dto.AuthResponse;
import com.example.myapplication.dto.ImageDTO;
import com.example.myapplication.dto.LoginRequest;
import com.example.myapplication.dto.post;

public interface Api {
    @GET("sample_post")
    Call<post> getData();

    @POST("upload")
    Call <ResponseBody> postImage(@Body ImageDTO imageDTO);

    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);
}

