package com.example.myapplication.chat;

public class List_FriendItem {
    String name;
    String message;
    int resourceId;
    int identifier;
    int opp_identifier;

    public List_FriendItem(int resourceId, String name, String message, int id, int opp_id) {
        this.name = name;
        this.message= message;
        this.resourceId = resourceId;
        this.identifier = id;
        this.opp_identifier = opp_id;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
    public String getRoomId() {
        if(identifier > opp_identifier) {
            return opp_identifier + "/" + identifier;
        }
        return identifier + "/" + opp_identifier;
    }
}