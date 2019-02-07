package com.uiu.kids.event.notification;

import com.uiu.kids.model.LocalNotificationModel;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.NotificationSender;


public class SafePlacesNotificationEvent {
    Location appsEntity;
    String message;
    int status;
    String image;
    LocalNotificationModel localNotificationModel;
    public SafePlacesNotificationEvent(Location appsEntity, LocalNotificationModel notificationModel) {
        this.appsEntity = appsEntity;
        this.localNotificationModel = notificationModel;
        this.message = notificationModel.getMessage();
        this.status =notificationModel.getStatus();
        this.image = notificationModel.getImage();
    }

    public Location getLocationEntity() {
        return appsEntity;
    }

    public NotificationSender getSender() {
        return localNotificationModel.getSender();
    }
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
    public String getImage() {
        return image==null?"www.g":image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
