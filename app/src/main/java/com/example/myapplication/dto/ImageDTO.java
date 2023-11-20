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
    @SerializedName("name")
    private String name;
    @SerializedName("date")
    private String date;

    public void setImg(String img) { this.img = img; }
    public void setTxt(String txt) {
        this.txt = txt;
    }
    public void setDate(String date) { this.date = date; }
    public void setName(String name){ this.name = name; }
    public ImageDTO() {}

}

