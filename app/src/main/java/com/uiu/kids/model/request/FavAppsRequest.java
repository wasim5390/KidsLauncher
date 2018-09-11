package com.uiu.kids.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.uiu.kids.ui.home.apps.AppsEntity;

public class FavAppsRequest {
    public void setApp(AppsEntity entity) {
        this.entity = entity;
    }
    @Expose
    @SerializedName("application")
    AppsEntity entity;
}
