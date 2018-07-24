package com.wiser.kids.ui.home.contact;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.Injection;

import java.io.Serializable;

public class ContactEntity implements Serializable{
    @SerializedName("name")
    private String name;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("photo_uri")
    private String photoUri;
    @SerializedName("lookupId")
    private String lookupId;
    @SerializedName("androidId")
    private String androidId;
    @SerializedName("id")
    private String userId;
    @SerializedName("phone_number")
    private String mPhoneNumber;
    @SerializedName("home_number")
    private String mHomeNumber;
    @SerializedName("request_status")
    private int requestStatus;
    @SerializedName("slide_id")
    private String slideId;

    private boolean hasAccess;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getSlideId() {
        return slideId;
    }

    public void setSlideId(String slideId) {
        this.slideId = slideId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUri() {
        return photoUri!=null?photoUri:"www.empty";
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getLookupId() {
        return lookupId;
    }

    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmHomeNumber() {
        return mHomeNumber;
    }

    public void setmHomeNumber(String mHomeNumber) {
        this.mHomeNumber = mHomeNumber;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

}
