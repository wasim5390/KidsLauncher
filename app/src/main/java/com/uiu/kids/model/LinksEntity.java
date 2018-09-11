package com.uiu.kids.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static com.uiu.kids.Constant.ACCEPTED;


public class LinksEntity implements Serializable {

   @SerializedName("complete_url")
   public String link;

   public Uri imgLink;

   public boolean flagEmptylist;

    @Expose(serialize = false)
    private boolean hasAccess;

    @SerializedName("slide_id")
    public String slide_id;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("id")
    public String id;

    @SerializedName("icon_url")
    public String icon_url;

    @SerializedName("short_url")
    public String short_url;

    @SerializedName("request_status")
    public int requestStatus;

    public String getSlide_id() {
        return slide_id;
    }

    public void setSlide_id(String slide_id) {
        this.slide_id = slide_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int request_status) {
        this.requestStatus = request_status;
    }

    public LinksEntity(String link, Uri iconLink)
    {
        this.imgLink=iconLink;
        this.link=link;
    }

    public String getLinkName() {
        return link;
    }

    public Uri getImgLink() {
        return imgLink;
    }

    public boolean getFlagEmptylist() {
        return flagEmptylist;
    }

    public void setFlagEmptylist(boolean flagEmptylist) {
        this.flagEmptylist = flagEmptylist;
    }

    public boolean hasAccess() {
         return hasAccess=requestStatus==ACCEPTED?true:false;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
