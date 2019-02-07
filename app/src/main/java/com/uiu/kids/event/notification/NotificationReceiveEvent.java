package com.uiu.kids.event.notification;

import com.uiu.kids.Constant;

import org.json.JSONObject;

public class NotificationReceiveEvent implements Constant {
    JSONObject object;
    JSONObject sender;
    int notificationType=-1;
    int status=3;
    private String title;
    private String message;
    private boolean slideUpdate=false;


    public NotificationReceiveEvent(String title,String message,JSONObject sender,JSONObject object,int notificationType,int status) {
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.object = object;
        this.sender = sender;
        this.status=status;
    }



    public NotificationReceiveEvent(int notificationType, boolean slideUpdate) {
        this.notificationType = notificationType;
        this.slideUpdate = slideUpdate;
    }

    public JSONObject getSender() {
        return sender;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject getNotificationResponse(){
        return object;
    }
    public int getNotificationForSlideType() {
        return notificationType;
    }
    public int getStatus() {
        return status;
    }
    public boolean isSlideUpdate() {
        return slideUpdate;
    }

}
