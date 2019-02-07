package com.uiu.kids.model;

import org.json.JSONObject;

public class LocalNotificationModel {
    private NotificationSender sender;
    private JSONObject jsonObject;
    private String message;
    private String image;
    private int status;
    private String file;
    private String type;

    public LocalNotificationModel(NotificationSender sender, JSONObject jsonObject, String message, String image, int status) {
        this.sender = sender;
        this.jsonObject = jsonObject;
        this.message = message;
        this.image = image;
        this.status = status;
    }



    public NotificationSender getSender() {
        return sender;
    }

    public void setSender(NotificationSender sender) {
        this.sender = sender;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



}
