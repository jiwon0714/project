package com.example.myapplication.dto;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
    private Integer identifier;
    private String id;
    private String name;
    private String email;
    private String password;

    public UserDTO(String id, String name, String email, String password){
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }
    public Integer getIdentifier() { return identifier; }
}