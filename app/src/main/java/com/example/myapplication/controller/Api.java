package com.example.myapplication.controller;
import retrofit2.Call;
import retrofit2.http.GET;
import com.example.myapplication.dto.post;
public interface Api {
    @GET("sample_post")
    Call<post> getData();
}

