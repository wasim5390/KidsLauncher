package com.wiser.kids.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.http.Body;

public class SlideItem implements Serializable{

	@SerializedName("serial")
	private int serial;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@SerializedName("user_id")
	private String user_id;

	@SerializedName("type")
	private String type;

	public void setSerial(int serial){
		this.serial = serial;
	}

	public int getSerial(){
		return serial;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public String getUserId() {
		return user_id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"slide{" +
			"serial = '" + serial + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' +
					",user_id = '" + user_id + '\'' +
			"}";
		}
}