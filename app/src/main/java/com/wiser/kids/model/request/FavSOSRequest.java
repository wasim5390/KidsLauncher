package com.wiser.kids.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wiser.kids.model.LinksEntity;
import com.wiser.kids.ui.home.contact.ContactEntity;

public class FavSOSRequest {

    public void setSOS(ContactEntity entity) {
        this.entity = entity;
    }
    @Expose
    @SerializedName("sos")
    ContactEntity entity;
}
