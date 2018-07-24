package com.wiser.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.ui.home.apps.AppsEntity;

public class GetFavAppsResponse extends BaseResponse{

	@SerializedName("application")
	private AppsEntity appsEntity;

	public void setEntity(AppsEntity application){
		this.appsEntity = application;
	}

	public AppsEntity getAppsEntity(){
		return appsEntity;
	}

	@Override
 	public String toString(){
		return 
			"GetFavAppsResponse{" + 
			"application = '" + appsEntity + '\'' +
			"}";
		}
}