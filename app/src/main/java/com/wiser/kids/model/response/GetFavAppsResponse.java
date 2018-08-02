package com.wiser.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.ui.home.apps.AppsEntity;

import java.util.List;

public class GetFavAppsResponse extends BaseResponse{

	@SerializedName( "application" )
	private AppsEntity appsEntity;


	@SerializedName("applications")
	private List<AppsEntity> favAppsList;

	public void setEntity(AppsEntity application){
		this.appsEntity = application;
	}

	public AppsEntity getAppsEntity(){
		return appsEntity;
	}

	public List<AppsEntity> getFavAppsList() {
		return favAppsList;
	}

	public void setFavAppsList(List<AppsEntity> favAppsList) {
		this.favAppsList = favAppsList;
	}

	@Override
	public String toString(){
		return
				"GetFavAppsResponse{" +
						"application = '" + appsEntity + '\'' +
						"}";
	}
}