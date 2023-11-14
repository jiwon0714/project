package com.example.myapplication.dto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDTO {
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("password")
    private String password;

    @Expose
    @SerializedName("email")
    private String email;

    public UserDTO(){}

    public UserDTO(String name, String id, String password, String email){
        this.name = name;
        this.id = id;
        this.password = password;
        this.email = email;
    }
// 생년월일 확인까지
//    public UserDTO(String id,String name, String email, String password){
//        this.name = name;
//        this.email = email;
//    }

}