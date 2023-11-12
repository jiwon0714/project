package com.example.myapplication.auth;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {

    private String jwtToken;

    public JwtInterceptor(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request newRequest = ((Request) originalRequest).newBuilder()
                .header("Authorization", "Bearer " + jwtToken)
                .build();

        return chain.proceed(newRequest);
    }
}
