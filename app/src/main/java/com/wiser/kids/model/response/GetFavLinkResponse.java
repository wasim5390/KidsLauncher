package com.wiser.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.model.LinksEntity;

import java.util.List;

public class GetFavLinkResponse extends BaseResponse {

    @SerializedName( "link" )
    private LinksEntity linkEntity;


    @SerializedName("links")
    private List<LinksEntity> favlinkList;

    public LinksEntity getLinkEntity() {
        return linkEntity;
    }

    public void setLinkEntity(LinksEntity linkEntity) {
        this.linkEntity = linkEntity;
    }

    public List<LinksEntity> getFavLinkList() {
        return favlinkList;
    }

    public void setFavAppsList(List<LinksEntity> favAppsList) {
        this.favlinkList = favAppsList;
    }

    @Override
    public String toString(){
        return
                "GetFavAppsResponse{" +
                        "application = '" + linkEntity + '\'' +
                        "}";
    }
}
