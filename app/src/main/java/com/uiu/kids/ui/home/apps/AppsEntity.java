
package com.uiu.kids.ui.home.apps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uiu.kids.Constant;

import java.io.Serializable;

public class AppsEntity  implements Serializable,Constant {


    @SerializedName("id")
   // @Expose(serialize = false)
    private String id;

    @SerializedName("user_id")
    private String userId;
    @SerializedName("app_icon" )
    private String appIcon;

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

    public int getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    @SerializedName("request_status")
    private int requestStatus=1;


    public AppsEntity(String name,String pkgName) {
        this.label = name;
        this.packageName=pkgName;
    }

    public String getId() {
        return id;
    }

    public void setAppIcon(String base64String){
        this.appIcon = base64String;
    }

    public String getAppIcon(){
        return appIcon==null?"":appIcon;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public boolean hasAccess() {
        return hasAccess=requestStatus==ACCEPTED?true:false;
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
                return icon;
               // e.printStackTrace();
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
                        "app_icon = '" + appIcon + '\'' +
                        ",package_name = '" + packageName + '\'' +
                        ",label = '" + label + '\'' +
                        ",slide_id = '" + slideId + '\'' +
                        "}";
    }

}
