package com.example.myapplication.controller;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.example.myapplication.dto.ImageDTO;
import com.example.myapplication.dto.post;

public interface Api {
    @GET("sample_post")
    Call<post> getData();

    @POST("upload")
    Call <ResponseBody> postImage(@Body ImageDTO imageDTO);
}

