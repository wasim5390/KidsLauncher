package com.wiser.kids.ui.home.apps;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;

public class AppsEntity  implements Serializable{


    @SerializedName("id")
    @Expose(serialize = false)
    private String id;
    @SerializedName("app_icon")
    @Expose(serialize = false)
    private String app_icon;

    @SerializedName("appIcon")
    private File appIcon;

    @SerializedName("package_name")
    private String packageName;

    @SerializedName("label")
    private String label;

    @SerializedName("slide_id")
    private String slideId;
    @Expose(serialize = false)
    private boolean isEmptylist;
    @Expose(serialize = false)
    private boolean hasAccess;


    public AppsEntity(String name,String pkgName, File appIcon) {
        this.label = name;
        this.packageName=pkgName;
        this.appIcon=appIcon;
    }

    public String getId() {
        return id;
    }

    public String getApp_icon() {
        return app_icon;
    }
    public void setAppIcon(File appIcon){
        this.appIcon = appIcon;
    }

    public File getAppIcon(){
        return appIcon;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    public String getPackageName(){
        return packageName;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public void setSlideId(String slideId){
        this.slideId = slideId;
    }

    public String getSlideId(){
        return slideId;
    }





    public boolean hasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }


    public String getName() {
        return label;
    }

    public Drawable getIcon(Context context) {
        Drawable icon = null;

        if (packageName!=null) {
            try {
                icon = context.getPackageManager().getApplicationIcon(packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return icon;

    }

    public String getPkgName()
    {
        return packageName;
    }

    public void setFlagEmptylist(boolean isemptylist) {
        isEmptylist = isemptylist;
    }
    public boolean getFlagEmptyList()
    {
        return isEmptylist;
    }

    @Override
    public String toString(){
        return
                "Application{" +
                        "appIcon = '" + appIcon + '\'' +
                        ",package_name = '" + packageName + '\'' +
                        ",label = '" + label + '\'' +
                        ",slide_id = '" + slideId + '\'' +
                        "}";
    }

}
