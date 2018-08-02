package com.wiser.kids.event;

import com.wiser.kids.Constant;

import org.json.JSONObject;

public class NotificationReceiveEvent implements Constant{
    JSONObject object;
    int notificationType;
    public NotificationReceiveEvent(JSONObject object,int notificationType) {
        this.notificationType = notificationType;
        this.object = object;
    }

    public JSONObject getNotificationResponse(){
      return object;
    }
    public int getNotificationForSlideType() {
        return notificationType;
    }
}
