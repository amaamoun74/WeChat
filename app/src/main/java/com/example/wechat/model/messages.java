package com.example.wechat.model;

public class messages {
    private  String message,receiver,sender;
    private  boolean messageStatus;


    public messages() {
    }

    public messages(String message, String receiver, String sender, boolean messageStatus) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.messageStatus = messageStatus;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(boolean messageStatus) {
        this.messageStatus = messageStatus;
    }
}
