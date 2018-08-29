package com.wiser.kids.ui.home.contact;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.Injection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.wiser.kids.Constant.ACCEPTED;

public class ContactEntity implements Serializable{



    @SerializedName("slide_id")
    public String slide_id;

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
    private String id;

    @SerializedName("user_id")
    private String userId;


    @SerializedName("phone_number")
    private String mPhoneNumber;

    @SerializedName("home_number")
    private String mHomeNumber;

    @SerializedName("phone_numbers")
    private List<String> phoneNumber;

    private int contactType;
    @SerializedName("request_status")
    private int requestStatus=1;

    private boolean hasAccess;

    public boolean isSelectedForSharing;

    public transient boolean isSelected;

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
        return photoUri!=null && !photoUri.isEmpty()?photoUri:"www.empty";
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

    public String getId() {
        return id;
    }

    public void setId(String contactId) {
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

    public List getAllNumbers(){
        List numbers = new ArrayList();
        if(mPhoneNumber!=null && !mPhoneNumber.isEmpty())
            numbers.add(mPhoneNumber);
        if(mHomeNumber!=null && !mHomeNumber.isEmpty())
            numbers.add(mHomeNumber);
        return numbers;
    }
    public List<String> getPhoneNumber() {
        return phoneNumber;
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

    public String getSlide_id() {
        return slide_id;
    }

    public void setSlide_id(String slide_id) {
        this.slide_id = slide_id;
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

    public boolean isSelectedForSharing() {
        return isSelectedForSharing;
    }

    public void setSelectedForSharing(boolean selectedForSharing) {
        isSelectedForSharing = selectedForSharing;
    }
}
