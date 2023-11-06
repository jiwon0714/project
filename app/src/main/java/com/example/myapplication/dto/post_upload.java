package com.example.myapplication.dto;

import com.google.gson.annotations.SerializedName;

public class post_upload {
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

    public post_upload() {}
}
