package com.example.myapplication.dto;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
    private Integer  identifier;
    private String id;
    private String name;
    private String email;
    private String password;

    private String date;

    public UserDTO(String id, String name, String email, String password, String date){
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.date = date;
    }

    public Integer getIdentifier() {
        return identifier;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }
}