package com.example.myapplication.dto;

public class post_upload {
    /*
    @Expose
    @SerializedName("user_id")
    private String id;
    */

    private String img;
    private String txt;
    public void setImg(String img) {
        this.img = img;
    }
    public void setTxt(String txt) {
        this.txt = txt;
    }
}
