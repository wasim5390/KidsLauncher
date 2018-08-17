package com.wiser.kids.event;

import com.wiser.kids.Constant;

import org.json.JSONObject;

public class NotificationReceiveEvent implements Constant{
    JSONObject object;
    int notificationType;

    private String title;
    private String message;


    public NotificationReceiveEvent(String title,String message,JSONObject object,int notificationType) {
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.object = object;
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
}
