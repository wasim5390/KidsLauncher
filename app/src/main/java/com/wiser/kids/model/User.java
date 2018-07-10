package com.wiser.kids.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by sidhu on 4/11/2018.
 */

public class User{

    @SerializedName("post_delivery_contractor_id")
    private int userId;

    @SerializedName("email_address")
    private String userEmail;
    @SerializedName("first_name")
    private String userFirstName;
    @SerializedName("last_name")
    private String userLastName;
    @SerializedName("mobile")
    private String userPhone;
    @SerializedName("post_code")
    private String postCode;
    @SerializedName("country")
    private String country;
    @SerializedName("activated")
    private int activated;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getFullName() {
        if (userFirstName!=null&&!userFirstName.trim().isEmpty() && userLastName!=null&&!userLastName.trim().isEmpty()) {
            return userFirstName + " " + userLastName;
        } else if (userFirstName!=null&&!userFirstName.trim().isEmpty()) {
            return userFirstName;
        } else if (userLastName!=null&&!userLastName.trim().isEmpty()) {
            return userLastName;
        } else {
            return "Unknown";
        }

    }


    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }
}
