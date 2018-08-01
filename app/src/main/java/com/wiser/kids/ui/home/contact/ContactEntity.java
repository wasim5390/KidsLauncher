package com.wiser.kids.ui.home.contact;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.Injection;

import java.io.Serializable;

import static com.wiser.kids.Constant.ACCEPTED;

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
    private Integer id;

    @SerializedName("user_id")
    private String userId;


    @SerializedName("phone_number")
    private String mPhoneNumber;
    @SerializedName("home_number")
    private String mHomeNumber;

    private int contactType;
    @SerializedName("request_status")
    private int requestStatus=1;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer contactId) {
        this.id = contactId;
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

    public String[] getAllNumbers(){
        String numbers[] = new String[2];
        if(mPhoneNumber!=null && !mPhoneNumber.isEmpty())
            numbers[0]=mPhoneNumber;
        if(mHomeNumber!=null && !mHomeNumber.isEmpty())
            numbers[1]=mHomeNumber;
        return numbers;
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
    public int getContactType() {
        return contactType;
    }

    public void setContactType(int contactType) {
        this.contactType = contactType;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    public boolean hasAccess() {
        return hasAccess=requestStatus==ACCEPTED?true:false;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

}
