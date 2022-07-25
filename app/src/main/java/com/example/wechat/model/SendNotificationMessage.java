package com.example.wechat.model;

public class SendNotificationMessage {
    NotificationData notificationData;
    String sendTO;

    public SendNotificationMessage(NotificationData notificationData, String sendTO) {
        this.notificationData = notificationData;
        this.sendTO = sendTO;
    }
}
