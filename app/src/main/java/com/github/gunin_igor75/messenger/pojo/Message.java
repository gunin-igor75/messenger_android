package com.github.gunin_igor75.messenger.pojo;

public class Message {

    private String content;
    private String senderId;
    private String recipientId;

    public Message() {
    }

    public Message(String content, String senderId, String recipientId) {
        this.content = content;
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public String getContent() {
        return content;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }
}
