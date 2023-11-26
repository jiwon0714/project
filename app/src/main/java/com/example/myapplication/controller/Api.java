package com.example.myapplication.controller;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.example.myapplication.chat.ChatMessage;
import com.example.myapplication.dto.AuthResponse;
import com.example.myapplication.dto.ChatCount;
import com.example.myapplication.dto.ImageDTO;
import com.example.myapplication.dto.LoginRequest;
import com.example.myapplication.dto.UserDTO;
import com.example.myapplication.dto.post;

import java.util.List;

public interface Api {
    @GET("sample_post")
    Call<post> getData();

    @GET("download")
    Call<List<ImageDTO>> getImage();

    @POST("upload")
    Call <ResponseBody> postImage(@Body ImageDTO imageDTO);

    @POST("add")
    Call <ResponseBody> addNewUser(@Body UserDTO userDTO);

    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @GET("chat_room")
    Call<ChatCount> getChatCount();

    @POST("add_chat/{opp_id}")
    Call<Integer>add_chat(@Path("opp_id") String opp_id);

    @POST("postchat/{room_id}/{opp_id}")
    Call<ChatMessage>postChat(@Path("room_id") String room_id, @Path("opp_id") Integer opp_identifier, @Body ChatMessage chatMessage);

    @GET("getchat/{room_id}/{chat_cnt}")
    Call<List<ChatMessage>>getChat(@Path("room_id") String room_id, @Path("chat_cnt") Integer chat_cnt);
}

