package com.example.myapplication.chat;

public class ChatMessage {
    private String messageContent;
    private String timestamp;
    private boolean isRead; // 읽음 상태를 나타내는 필드 추가
    private boolean isSent; // 발신 여부를 나타내는 필드 추가

    private int viewType;

    public ChatMessage(String messageContent, String timestamp) {
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.isRead = false; // 초기값은 읽지 않음
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
    public boolean isSent() {return isSent;}

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getViewType() {
        return viewType;
    }
}
