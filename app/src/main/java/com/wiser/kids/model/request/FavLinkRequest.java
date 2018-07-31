package com.wiser.kids.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wiser.kids.model.LinksEntity;


public class FavLinkRequest {
    public void setLink(LinksEntity entity) {
        this.entity = entity;
    }
    @Expose
    @SerializedName("link")
    LinksEntity entity;
}
