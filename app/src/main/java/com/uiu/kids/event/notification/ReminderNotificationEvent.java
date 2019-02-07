package com.uiu.kids.event.notification;


import com.uiu.kids.model.LocalNotificationModel;
import com.uiu.kids.model.NotificationSender;
import com.uiu.kids.ui.slides.reminder.ReminderEntity;

public class ReminderNotificationEvent {

    ReminderEntity reminderEntity;
    LocalNotificationModel localNotificationModel;

    String message;
    int status;

    String image;

    public ReminderNotificationEvent(ReminderEntity reminderEntity, LocalNotificationModel notificationModel) {
        this.reminderEntity = reminderEntity;
        this.localNotificationModel = notificationModel;
        this.message = notificationModel.getMessage();
        this.status =notificationModel.getStatus();
        this.image = notificationModel.getImage();
    }

    public ReminderEntity getReminderEntity() {
        return reminderEntity;
    }

    public NotificationSender getSender() {
        return localNotificationModel.getSender();
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image==null?"www.g":image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }
}
