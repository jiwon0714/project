package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.auth.JwtInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class set_retrofit {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://192.168.0.41:8080/demo/";
    private static Context context;
    private static SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    private static String jwtToken = prefs.getString("token", null);

    public static Retrofit getClient() {
        if(retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("HTTP Log", message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 로그 수준 설정
            httpClient.addInterceptor(loggingInterceptor);
            httpClient.addInterceptor(new JwtInterceptor(jwtToken));

            // Retrofit 설정
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build()) // OkHttpClient 설정
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
