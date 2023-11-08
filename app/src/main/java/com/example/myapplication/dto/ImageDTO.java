package com.example.myapplication.dto;

import com.google.gson.annotations.SerializedName;

public class ImageDTO {
    /*
    @Expose
    @SerializedName("user_id")
    private String id;
    */
    @SerializedName("img")
    private String img;
    @SerializedName("txt")
    private String txt;
    public void setImg(String img) { this.img = img; }
    public void setTxt(String txt) {
        this.txt = txt;
    }

    public ImageDTO() {}

    public ImageDTO(String img, String txt) {
        this.img = img;
        this.txt = txt;
    }
}

