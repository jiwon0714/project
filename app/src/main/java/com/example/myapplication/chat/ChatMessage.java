package com.example.myapplication.chat;

public class ChatMessage {
    private Integer idx;
    private String messageContent;
    private String timestamp;
    private boolean isRead; // 읽음 상태를 나타내는 필드 추가
    private Integer writer;
    private Integer reader;

    public ChatMessage(String messageContent, String timestamp, Integer writer, Integer reader) {
        this.messageContent = messageContent;
        this.timestamp = timestamp;
        this.isRead = false; // 초기값은 읽지 않음
        this.writer = writer;
        this.reader = reader;
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
    public Integer getWriter() { return writer;}
    public Integer getIdx() { return idx; }
}
