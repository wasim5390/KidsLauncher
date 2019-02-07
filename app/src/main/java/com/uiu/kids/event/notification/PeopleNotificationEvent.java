package com.uiu.kids.event.notification;

import com.uiu.kids.model.LocalNotificationModel;
import com.uiu.kids.model.NotificationSender;
import com.uiu.kids.ui.home.contact.ContactEntity;

public class PeopleNotificationEvent {
    ContactEntity contactEntity;
    LocalNotificationModel localNotificationModel;

    String message;
    int status;

    String image;

    public PeopleNotificationEvent(ContactEntity contactEntity, LocalNotificationModel notificationModel) {
        this.contactEntity = contactEntity;
        this.localNotificationModel = notificationModel;
        this.message = notificationModel.getMessage();
        this.status =notificationModel.getStatus();
        this.image = notificationModel.getImage();
    }

    public ContactEntity getContactEntity() {
        return contactEntity;
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
