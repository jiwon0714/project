package com.example.myapplication.sns;

import android.graphics.Bitmap;

public class SnsItem {
    private Bitmap base64ImageProfile;
    private String name;
    private Bitmap base64ImageMain;
    private String text;
    private String commentName;
    private String comment;

    public SnsItem(Bitmap base64ImageProfile, String name, Bitmap base64ImageMain, String text, String commentName, String comment) {
        this.base64ImageProfile = base64ImageProfile;
        this.name = name;
        this.base64ImageMain = base64ImageMain;
        this.text = text;
        this.commentName = commentName;
        this.comment = comment;
    }

    public Bitmap getBase64ImageProfile() {
        return base64ImageProfile;
    }

    public Bitmap getBase64ImageMain() {
        return base64ImageMain;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentName() {
        return commentName;
    }
}