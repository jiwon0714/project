package com.example.myapplication.dto;

public class AuthResponse {
    private String status;
    private String message;
    private String authToken;
    private UserDTO user;

    public String get_status() { return status; }
    public String get_message() { return message; }
    public String get_toket() {
        return authToken;
    }
}
