package com.uiu.kids.ui.home.helper;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HelperEntity implements Serializable{

    @SerializedName("id")
    public String id;

    @SerializedName("email")
    public String email;

    @SerializedName("first_name")
    public String first_name;

    @SerializedName("last_name")
    public String last_name;

    @SerializedName("username")
    public String username;

    @SerializedName("user_type")
    public String user_type;

    @SerializedName("phone_number")
    public String phoneNumber;

    @SerializedName("image_link")
    public String image_link;

    @SerializedName("is_primary")
    public boolean isPrimary;

    public boolean isHelperSelected;

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public boolean isHelperSelected() {
        return isHelperSelected;
    }

    public void setHelperSelected(boolean helperSelected) {
        isHelperSelected = helperSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getImage_link() {
        return image_link==null?"":image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
