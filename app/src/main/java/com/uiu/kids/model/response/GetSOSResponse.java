package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class GetSOSResponse extends BaseResponse {

    @SerializedName( "soses" )
    private ContactEntity sosEntity;



    @SerializedName( "sos" )
    private List<ContactEntity> sosEntityList;

    @SerializedName( "contacts" )
    private List<ContactEntity> contactEntityList;

    public ContactEntity getSOSEntity() {
        return sosEntity;
    }

    public List<ContactEntity> getSOSList() {
        return sosEntityList;
    }


    public List<ContactEntity> getAllFavPeopleList() {
        return contactEntityList;
    }
    @Override
    public String toString(){
        return
                "GetFavContactResponse{" +
                        "contact = '" + sosEntity + '\'' +
                        "}";
    }
}
