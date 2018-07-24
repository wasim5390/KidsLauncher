package com.wiser.kids.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wiser.kids.ui.home.apps.AppsEntity;

import java.util.List;

public class FavAppsRequest {
    public void setApp(AppsEntity entity) {
        this.entity = entity;
    }
    @Expose
    @SerializedName("application")
    AppsEntity entity;
}
