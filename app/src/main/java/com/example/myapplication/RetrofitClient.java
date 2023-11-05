
package com.example.myapplication;
import android.util.Log;

import com.example.myapplication.controller.Api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private static Api userRetrofitInterface;
    private static String baseUrl = "http://192.168.56.1/demo/";

    public RetrofitClient() {
        // OkHttpClient에 로깅 인터셉터 추가
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("HTTP Log", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 로그 수준 설정
        httpClient.addInterceptor(loggingInterceptor);

        // Retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient.build()) // OkHttpClient 설정
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userRetrofitInterface = retrofit.create(Api.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
    public static Api getUserRetrofitInterface() {
        return userRetrofitInterface;
    }
}