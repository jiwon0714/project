package com.example.myapplication.dto;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String id, String pw) {
        this.username = id;
        this.password = pw;
    }
}
