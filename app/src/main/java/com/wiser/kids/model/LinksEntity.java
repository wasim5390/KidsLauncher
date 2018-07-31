package com.wiser.kids.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class LinksEntity implements Serializable {

   public String link;

   public Uri imgLink;

   public boolean flagEmptylist;

    @Expose(serialize = false)
    private boolean hasAccess;


    public LinksEntity(String link,Uri iconLink)
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
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
