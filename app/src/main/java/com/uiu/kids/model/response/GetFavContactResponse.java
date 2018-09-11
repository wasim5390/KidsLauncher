package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class GetFavContactResponse extends BaseResponse {

    @SerializedName( "contact" )
    private ContactEntity contactEntity;


    public List<ContactEntity> getContactEntityList() {
        return contactEntityList;
    }

    @SerializedName( "contacts" )
    private List<ContactEntity> contactEntityList;

    @SerializedName( "users" )
    private List<ContactEntity> regdContactEntityList;

    public void setFavContact(ContactEntity contactEntity){
        this.contactEntity = contactEntity;
    }

    public ContactEntity getFavoriteContact(){
        return contactEntity;
    }


    public List<ContactEntity> getRegdContactEntityList() {
        return regdContactEntityList;
    }

    @Override
    public String toString(){
        return
                "GetFavContactResponse{" +
                        "contact = '" + contactEntity + '\'' +
                        "}";
    }
}