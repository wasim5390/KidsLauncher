package com.wiser.kids.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Created by sidhu on 4/11/2018.
 */

public class ContactPerson implements Serializable {

    @SerializedName("id")
    private int Id;

    @SerializedName("contact_id")
    private String contactId;
    @SerializedName("slide_id")
    private String slideId;
    @SerializedName("name")
    private String contactName;
    @SerializedName("email")
    private String contactEmail;
    @SerializedName("request_status")
    private String requestStatus;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("photo_uri")
    private int photoUrl;
    @SerializedName("contact_icon")
    private int contactIcon;
}
