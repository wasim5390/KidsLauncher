package com.uiu.kids.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Slide implements Serializable{

	@SerializedName("serial")
	private Integer serial;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("user_id")
	private String user_id;

	@SerializedName("type")
	private Integer type;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	private int count;

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}



	public void setSerial(Integer serial){
		this.serial = serial;
	}

	public Integer getSerial(){
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

	public void setType(int type){
		this.type = type;
	}

	public Integer getType(){
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

	@Override
	public boolean equals(Object obj) {
		return this.id == ((Slide)obj).id;
	}
	@Override
	public int hashCode()
	{
		return type==null?20:type.hashCode();
	}
}