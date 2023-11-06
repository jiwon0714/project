package com.example.myapplication.Diary;

public class DiaryItem {
    String title;
    String message;
    int resourceId;

    public DiaryItem(int resourceId, String title, String message) {
        this.title = title;
        this.message= message;
        this.resourceId = resourceId;
    }

    public int getResourceId() {return resourceId;}

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String name) {
        this.title = title;
    }

    public void setResourceId(int resourceId) {this.resourceId = resourceId;
    }
}