package com.example.myapplication.sns;

import android.graphics.Bitmap;

public class Comment_Item {

//    private Bitmap profileImage;
//    private String name;
    private String commentContext;
    private String commentTime;
    private boolean isHeart;
    private int heartCount;

    public Comment_Item( String commentContext, String commentTime, boolean isHeart, int heartCount) {
//        this.profileImage = profileImage;
//        this.name = name;
        this.commentContext = commentContext;
        this.commentTime = commentTime;
        this.isHeart = isHeart;
        this.heartCount = heartCount;
    }


    //    public Bitmap getProfileImage(){return profileImage;}
//    public String getname() {
//        return name;
//    }
    public String getcommentContext() {
        return commentContext;
    }
    public String getcommentTime() {
        return commentTime;
    }
    public boolean isHeart() {return isHeart;}
    public int getHeartCount(){return heartCount;}

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public void setHeart(boolean heart) {
        isHeart = heart;
    }
}