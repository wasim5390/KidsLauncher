package com.uiu.kids.event;

import com.uiu.kids.model.NotificationSender;

public class ShareEvent {

    String fileUrl;


    String thumbnailUrl;
    String title;
    boolean isShared=false;
    int mediaType;
    String createdAt;
    NotificationSender sender;

    public ShareEvent(boolean isShared, int mediaType) {
        this.isShared = isShared;
        this.mediaType = mediaType;
    }

    public ShareEvent(int mediaType, String fileUrl, String title,String createdAt) {
        this.mediaType = mediaType;
        this.fileUrl = fileUrl;
        this.title = title;
        this.createdAt = createdAt;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl==null?"www.gg":thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    public boolean isShared() {
        return isShared;
    }


    public NotificationSender getSender() {
        return sender;
    }

    public void setSender(NotificationSender sender) {
        this.sender = sender;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String fileName) {
        this.title = fileName;
    }
    public String getCreatedAt() {
        return createdAt;
    }
}
