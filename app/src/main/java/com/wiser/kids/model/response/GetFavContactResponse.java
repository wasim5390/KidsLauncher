package com.wiser.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.ui.home.contact.ContactEntity;

public class GetFavContactResponse extends BaseResponse{

    @SerializedName( "contact" )
    private ContactEntity contactEntity;



    public void setFavContact(ContactEntity contactEntity){
        this.contactEntity = contactEntity;
    }

    public ContactEntity getFavoriteContact(){
        return contactEntity;
    }


    @Override
    public String toString(){
        return
                "GetFavContactResponse{" +
                        "contact = '" + contactEntity + '\'' +
                        "}";
    }
}