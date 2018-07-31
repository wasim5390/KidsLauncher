package com.wiser.kids.ui.favorite.people;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wiser.kids.Constant;

import java.io.Serializable;

public class Contact implements Serializable,Constant {

    @SerializedName("id")
    private int Id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("slide_id")
    private String slideId;
    @SerializedName("name")
    private String contactName;
    @SerializedName("email")
    private String contactEmail;
    @SerializedName("request_status")
    private int requestStatus=1;
    @SerializedName("phone_numbers")
    private String[] phoneNumber;
    @SerializedName("photo_uri")
    private int photoUrl;
    @SerializedName("contact_icon")
    private int contactIcon;
    @Expose(serialize = false)
    private boolean hasAccess;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    public String getSlideId() {
        return slideId;
    }

    public void setSlideId(String slideId) {
        this.slideId = slideId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String[] getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumbers(String[] phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(int photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getContactIcon() {
        return contactIcon;
    }

    public void setContactIcon(int contactIcon) {
        this.contactIcon = contactIcon;
    }
    public boolean hasAccess() {
        return hasAccess=requestStatus==ACCEPTED?true:false;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
