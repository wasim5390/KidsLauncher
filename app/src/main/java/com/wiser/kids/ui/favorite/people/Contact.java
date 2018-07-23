package com.wiser.kids.ui.favorite.people;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contact implements Serializable {

    @SerializedName("id")
    private int Id;
    @SerializedName("user_id")
    private String contactId;
    @SerializedName("slide_id")
    private String slideId;
    @SerializedName("name")
    private String contactName;
    @SerializedName("email")
    private String contactEmail;
    @SerializedName("request_status")
    private String requestStatus;
    @SerializedName("phone_numbers")
    private String phoneNumber;
    @SerializedName("photo_uri")
    private int photoUrl;
    @SerializedName("contact_icon")
    private int contactIcon;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
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

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
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
}
