package com.wiser.kids.ui.home.apps;

import android.graphics.drawable.Drawable;

public class AppsEntity {

    private String pkgName;
    private String name;
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

}
