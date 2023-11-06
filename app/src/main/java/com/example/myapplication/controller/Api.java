package com.example.myapplication.controller;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.example.myapplication.dto.post;
import com.example.myapplication.dto.post_upload;

public interface Api {
    @GET("sample_post")
    Call<post> getData();

    @POST("upload_post")
    Call <post_upload> postData(@Body post_upload post);
    
}

