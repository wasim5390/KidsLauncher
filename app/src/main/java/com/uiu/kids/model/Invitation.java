package com.uiu.kids.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Invitation implements Serializable {

    @SerializedName("id")
    String inviteId;
    @SerializedName("request_status")
    int status;
    @SerializedName("primary")
    boolean isPrimary;
    @SerializedName("user")
    User sender;

    public String getInviteId() {
        return inviteId;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
    
}
