package com.example.myapplication.chat;

public class ChatMessage {
    private String messageContent;
    private String timestamp;
    private boolean isRead;
    private boolean isSent;
    private int viewType;

    public ChatMessage(String messageContent, String timestamp, boolean isSent, int viewType) {
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.isRead = false;
        this.isSent = isSent;
        this.viewType = viewType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isSent() {
        return isSent;
    }

    public int getViewType() {
        return viewType;
    }
}
