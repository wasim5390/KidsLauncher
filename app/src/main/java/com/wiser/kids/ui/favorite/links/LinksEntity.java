package com.wiser.kids.ui.favorite.links;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class LinksEntity implements Serializable {

   public String linkName;
   public String imgLink;
   public boolean flagEmptylist;
    @Expose(serialize = false)
    private boolean hasAccess;


    public LinksEntity(String name,String link)
    {
        this.imgLink=link;
        this.linkName=name;
    }

    public String getLinkName() {
        return linkName;
    }

    public String getImgLink() {
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
