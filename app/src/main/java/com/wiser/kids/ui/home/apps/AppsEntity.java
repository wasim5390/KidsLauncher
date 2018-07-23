package com.wiser.kids.ui.home.apps;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


import java.io.Serializable;

public class AppsEntity  implements Serializable{

    private String pkgName;
    private String name;
    private boolean isEmptylist;

    public boolean hasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    private boolean hasAccess;


    public AppsEntity(String name,String pkgName) {
        this.name = name;
        this.pkgName=pkgName;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon(Context context) {
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(pkgName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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
