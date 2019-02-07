package com.uiu.kids.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationSender implements Serializable{

    @SerializedName("name")
    String senderName;
    @SerializedName("image")
    String senderImage;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage==null||senderImage.isEmpty()?"www.empty":senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }
}
