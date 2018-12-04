package com.uiu.kids.model;

import com.google.gson.annotations.SerializedName;

public class NotificationsItem{

	@SerializedName("notification_type")
	private int notificationType;

	@SerializedName("file_url")
	private String fileUrl;

	@SerializedName("file_type")
	private String fileType;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("message")
	private String message;

	@SerializedName("notificationInnerObject")
	private NotificationInnerObject notificationInnerObject;

	public void setNotificationType(int notificationType){
		this.notificationType = notificationType;
	}

	public int getNotificationType(){
		return notificationType;
	}

	public void setFileUrl(String fileUrl){
		this.fileUrl = fileUrl;
	}

	public String getFileUrl(){
		return fileUrl;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getFileType(){
		return fileType;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setNotificationInnerObject(NotificationInnerObject notificationInnerObject){
		this.notificationInnerObject = notificationInnerObject;
	}

	public NotificationInnerObject getNotificationInnerObject(){
		return notificationInnerObject;
	}

	@Override
 	public String toString(){
		return 
			"NotificationsItem{" + 
			"notification_type = '" + notificationType + '\'' + 
			",file_url = '" + fileUrl + '\'' + 
			",file_type = '" + fileType + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",message = '" + message + '\'' + 
			",notificationInnerObject = '" + notificationInnerObject + '\'' +
			"}";
		}
}