package com.uiu.kids.ui.home.contact;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.response.BaseResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.uiu.kids.Constant.ACCEPTED;


public class ContactEntity extends BaseResponse implements Serializable{

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
    @SerializedName(value = "photo_uri" ,alternate= "image_link")
    private String photoUri;

    @SerializedName("contact_icon")
    private String base64ProfilePic;
    @SerializedName("lookupId")
    private String lookupId;
    @SerializedName("androidId")
    private String androidId;

    @SerializedName("id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("registered_id")
    private String registeredId;

    @SerializedName("user_type")
    private Integer userType;

    @SerializedName("mobile_number")
    private String mMobileNumber;

    @SerializedName("home_number")
    private String mHomeNumber;

    public void setPhoneNumber(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @SerializedName("phone_numbers")
    private List<String> phoneNumber;

    private int contactType;
    @SerializedName("request_status")
    private int requestStatus=1;

    private boolean hasAccess;

    public boolean isSelectedForSharing;

    public transient boolean isSelected;
    public void setBase64ProfilePic(String base64Profile) {
        this.base64ProfilePic = base64Profile;
    }
    public String getName() {
        return (name!=null && !name.isEmpty())?name:getFirstName()+" "+getLastName() ;
    }
    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName!=null?firstName:"";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName!=null?lastName:"";
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

    public String getProfilePic() {
        return base64ProfilePic!=null && !base64ProfilePic.isEmpty()?base64ProfilePic:"www.emptyphoto";
    }
    public String getPhotoUri() {
        return photoUri!=null && !photoUri.isEmpty()?photoUri:"www.empty";
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getLookupId() {
        return lookupId==null?"":lookupId;
    }

    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }

    public String getAndroidId() {
        return androidId==null?"":androidId;
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

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public List<String> getAllNumbers(){
        List<String> numbers = new ArrayList();
        if(mMobileNumber !=null && !mMobileNumber.isEmpty())
            numbers.add(mMobileNumber);
        if(mHomeNumber!=null && !mHomeNumber.isEmpty())
            numbers.add(mHomeNumber);
        return numbers;
    }
    public List<String> getPhoneNumber() {
        return phoneNumber;
    }
    public void setMobileNumber(String mPhoneNumber) {
        this.mMobileNumber = mPhoneNumber;
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

    public boolean isRegistered(){
        return registeredId!=null;
    }
}
