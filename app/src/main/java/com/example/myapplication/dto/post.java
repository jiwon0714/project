package com.example.myapplication.dto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class post {

    @Expose
    @SerializedName("post_id")
    private int id;

    @Expose
    @SerializedName("user_id")
    private String post_oner;

    @Expose
    @SerializedName("post_image")
    private String img;

    @Expose
    @SerializedName("post_text")
    private String text;

    @Expose
    @SerializedName("like")
    private int like;

    public int get_id() { return id; }
    public String get_user() { return post_oner; }
    public String get_img() { return img; }
    public String get_txt() { return text; }
    public int get_like() { return like; }
}
