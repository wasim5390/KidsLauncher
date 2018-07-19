package com.wiser.kids.ui.home.apps;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppsEntity  {

//    @SerializedName("pkgName")
    private String pkgName;
//    @SerializedName("name")
    private String name;
//    @SerializedName("isEmptylist")
    private boolean isEmptylist;
//    @SerializedName("icon")
    private Drawable icon;

    public AppsEntity(String name, Drawable icon,String pkgName) {
        this.name = name;
        this.icon = icon;
        this.pkgName=pkgName;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getPkgName()
    {
        return pkgName;
    }

    public void setFlagEmptylist(boolean isemptylist) {
        isEmptylist = isemptylist;
    }
    public boolean getFlagEmptyList()
    {
        return isEmptylist;
    }
}
