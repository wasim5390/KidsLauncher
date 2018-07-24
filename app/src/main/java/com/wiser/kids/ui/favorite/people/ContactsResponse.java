package com.wiser.kids.ui.favorite.people;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContactsResponse implements Serializable,Parcelable{
    @SerializedName("success")
    private String success;
    @SerializedName("contacts")
    private ArrayList<ContactEntity> contacts;

    protected ContactsResponse(Parcel in) {
        success = in.readString();
    }

    public static final Creator<ContactsResponse> CREATOR = new Creator<ContactsResponse>() {
        @Override
        public ContactsResponse createFromParcel(Parcel in) {
            return new ContactsResponse(in);
        }

        @Override
        public ContactsResponse[] newArray(int size) {
            return new ContactsResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(success);
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<ContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ContactEntity> contacts) {
        this.contacts = contacts;
    }
}
