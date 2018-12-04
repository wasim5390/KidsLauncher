package com.uiu.kids.model;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.response.BaseResponse;

public class NotificationsListResponse extends BaseResponse{

	@SerializedName("data")
	private Data data;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"NotificationsListResponse{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}