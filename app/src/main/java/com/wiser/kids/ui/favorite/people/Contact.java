package com.wiser.kids.ui.favorite.people;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wiser.kids.Constant;

import java.io.Serializable;
import java.util.List;

public class Contact implements Serializable,Constant {

    @SerializedName("id")
    @Expose(serialize = false)
    private String Id;

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
    private List<String> phoneNumber;
    @SerializedName("photo_uri")
    private int photoUrl;
    @SerializedName("contact_icon")
    private String contactIcon;
    @Expose(serialize = false)
    private boolean hasAccess;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String  id) {
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

    public List<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumbers(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(int photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getContactIcon() {
        return contactIcon;
    }

    public void setContactIcon(String contactIcon) {
        this.contactIcon = contactIcon;
    }
    public boolean hasAccess() {
        return hasAccess=requestStatus==ACCEPTED?true:false;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
