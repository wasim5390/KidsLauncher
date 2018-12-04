package com.uiu.kids.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("page_count")
	private int pageCount;

	@SerializedName("current_page")
	private int currentPage;

	@SerializedName("notifications")
	private List<NotificationsItem> notifications;

	public void setPageCount(int pageCount){
		this.pageCount = pageCount;
	}

	public int getPageCount(){
		return pageCount;
	}

	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}

	public int getCurrentPage(){
		return currentPage;
	}

	public void setNotifications(List<NotificationsItem> notifications){
		this.notifications = notifications;
	}

	public List<NotificationsItem> getNotifications(){
		return notifications;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"page_count = '" + pageCount + '\'' + 
			",current_page = '" + currentPage + '\'' + 
			",notifications = '" + notifications + '\'' + 
			"}";
		}
}