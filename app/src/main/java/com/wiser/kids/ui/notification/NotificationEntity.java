package com.wiser.kids.ui.notification;

import android.net.Uri;

public class NotificationEntity {

    public String title;
    public String message;
    public String notificationType;
    public String senderName;
    public Uri senderImage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Uri getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(Uri senderImage) {
        this.senderImage = senderImage;
    }
}
