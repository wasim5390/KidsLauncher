package com.uiu.kids.model;


import com.google.gson.annotations.SerializedName;
import com.uiu.kids.Constant;

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

	@SerializedName("mobile_number")
	private String mobileNumber;

	@SerializedName("invitations")
	private List<Invitation> invitations;

	boolean isPrimaryConnected=false;

	private User primaryHelper;

	public boolean isPrimaryConnected() {
		return isPrimaryConnected;
	}

	public void setPrimaryConnected(boolean primaryConnected) {
		isPrimaryConnected = primaryConnected;
	}

	public void setInvitations(List<Invitation> invitations) {
		this.invitations = invitations;
	}

	public User getPrimaryHelper() {
		User primaryHelper=null;
		for(Invitation entity: getInvitations())
		{
			if(entity.isPrimary()) {
				primaryHelper = entity.getSender();
				primaryHelper.setPrimaryConnected(entity.getStatus()== Constant.INVITE.CONNECTED);
			}
		}
		return primaryHelper;
	}

	public void setPrimaryHelper(User primaryHelper) {
		for(Invitation entity: getInvitations())
		{
			if(entity.isPrimary())
				primaryHelper = entity.getSender();
		}
	}


	public List<Invitation> getInvitations() {
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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