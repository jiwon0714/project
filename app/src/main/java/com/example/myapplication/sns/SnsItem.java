package com.example.myapplication.sns;

import android.graphics.Bitmap;

public class SnsItem {

    String name;
    String text;

    Bitmap resourceId_profile;
    Bitmap resourceId_main_image;

    String uploadDate;

    public SnsItem(Bitmap resourceId_profile, String name, Bitmap resourceId_main_image, String text, String uploadDate ) {
        this.name = name;
        this.text= text;
        this.resourceId_profile = resourceId_profile;
        this.resourceId_main_image = resourceId_main_image;
        this.uploadDate = uploadDate;

    }

    public Bitmap getResourceId_profile() {
        return resourceId_profile;
    }

    public Bitmap getResourceId_main_image() {
        return resourceId_main_image;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {this.uploadDate = uploadDate;}

    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceId_profile(Bitmap resourceId_profile) {this.resourceId_profile = resourceId_profile;}

    public void setResourceId_main_image(Bitmap resourceId_main_image) {this.resourceId_main_image = resourceId_main_image;}



}
