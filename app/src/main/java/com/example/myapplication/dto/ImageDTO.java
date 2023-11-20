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
    @SerializedName("date")
    private String date;
    @SerializedName("post_owner")
    private String owner;

    public void setImg(String img) { this.img = img; }
    public void setTxt(String txt) {
        this.txt = txt;
    }
    public void setDate(String date) { this.date = date; }
    public String getImg() { return this.img; }
    public String getTxt() { return this.txt; }
    public String getOwner() { return this.owner; }

    public ImageDTO() {}

}

