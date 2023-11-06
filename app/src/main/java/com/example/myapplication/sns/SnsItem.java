package com.example.myapplication.sns;

public class SnsItem {

    String name;
    String text;
    String comment;
    String comment_name;
    int resourceId_profile;
    int resourceId_main_image;

    public SnsItem(int resourceId_profile,String name,int resourceId_main_image,String text,String comment_name, String comment ) {
        this.name = name;
        this.text= text;
        this.resourceId_profile = resourceId_profile;
        this.resourceId_main_image = resourceId_main_image;
        this.comment = comment;
        this.comment_name = comment_name;
    }

    public int getResourceId_profile() {
        return resourceId_profile;
    }

    public int getResourceId_main_image() {
        return resourceId_main_image;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getComment_name() {
        return comment_name;
    }



    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceId_profile(int resourceId_profile) {this.resourceId_profile = resourceId_profile;}

    public void setResourceId_main_image(int resourceId_main_image) {this.resourceId_main_image = resourceId_main_image;}

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setComment_name(String comment_name) {
        this.comment_name = comment_name;
    }

}
