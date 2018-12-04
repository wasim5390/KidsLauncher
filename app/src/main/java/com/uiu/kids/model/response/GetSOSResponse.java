package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class GetSOSResponse extends BaseResponse {

    @SerializedName( "soses" )
    private ContactEntity contactEntity;



    @SerializedName( "sos" )
    private List<ContactEntity> contactEntityList;

    public ContactEntity getContactEntity() {
        return contactEntity;
    }

    public List<ContactEntity> getContactEntityList() {
        return contactEntityList;
    }

    @Override
    public String toString(){
        return
                "GetFavContactResponse{" +
                        "contact = '" + contactEntity + '\'' +
                        "}";
    }
}
