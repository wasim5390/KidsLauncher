package com.uiu.kids.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User{

	@SerializedName("user_type")
	private int userType;

	@SerializedName("image_link")
	private String imageLink;

	@SerializedName("fcm_key")
	private String fcmKey;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("id")
	private String id;

	@SerializedName("first_name")
	private String firstName;

	@SerializedName("email")
	private String email;

	@SerializedName("username")
	private String username;

	@SerializedName("invitations")
	private List<Invitation> invitations;

	private User primaryHelper;



	public User getPrimaryHelper() {
		for(Invitation entity:getHelpers())
		{
			if(entity.isPrimary())
				primaryHelper = entity.getSender();
		}
		return primaryHelper;
	}

	public void setPrimaryHelper(User primaryHelper) {
		this.primaryHelper = primaryHelper;
	}


	public List<Invitation> getHelpers() {
		return  invitations==null?new ArrayList<>():invitations;
	}

	public void setUserType(int userType){
		this.userType = userType;
	}

	public int getUserType(){
		return userType;
	}

	public void setImageLink(String imageLink){
		this.imageLink = imageLink;
	}

	public String getImageLink(){
		return imageLink;
	}

	public void setFcmKey(String fcmKey){
		this.fcmKey = fcmKey;
	}

	public String getFcmKey(){
		return fcmKey;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"user_type = '" + userType + '\'' + 
			",image_link = '" + imageLink + '\'' + 
			",fcm_key = '" + fcmKey + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",id = '" + id + '\'' + 
			",first_name = '" + firstName + '\'' + 
			",email = '" + email + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}