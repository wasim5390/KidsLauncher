package com.uiu.kids.event.notification;

import com.uiu.kids.model.LinksEntity;
import com.uiu.kids.model.LocalNotificationModel;
import com.uiu.kids.model.NotificationSender;
import com.uiu.kids.ui.home.apps.AppsEntity;

public class LinkNotificationEvent {
    LinksEntity linksEntity;
    LocalNotificationModel localNotificationModel;

    String message;
    int status;

    String image;

    public LinkNotificationEvent(LinksEntity linksEntity,  LocalNotificationModel notificationModel) {
        this.linksEntity = linksEntity;
        this.localNotificationModel = notificationModel;
        this.message = notificationModel.getMessage();
        this.status =notificationModel.getStatus();
        this.image = notificationModel.getImage();
    }

    public LinksEntity getLinkEntity() {
        return linksEntity;
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
