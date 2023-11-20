package com.example.myapplication.dto;

public class LoginRequest {
    private String id;
    private String password;

    public LoginRequest(String id, String pw) {
        this.id = id;
        this.password = pw;
    }
}
