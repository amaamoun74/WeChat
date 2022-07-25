package com.example.wechat.model;

public class NotificationData {
    String sender,body,title,massage_sent;

    public NotificationData(String sender, String body, String title, String massage_sent) {
        this.sender = sender;
        this.body = body;
        this.title = title;
        this.massage_sent = massage_sent;
    }

    public NotificationData() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMassage_sent() {
        return massage_sent;
    }

    public void setMassage_sent(String massage_sent) {
        this.massage_sent = massage_sent;
    }
}
