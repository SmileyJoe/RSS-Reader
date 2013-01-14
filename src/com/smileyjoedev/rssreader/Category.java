package com.smileyjoedev.rssreader;
import org.apache.commons.lang3.StringUtils;

public class Category {

	private long id;
	private String title;
	private int status;
	
	public static final int STATUS_FAVOURITE = 1;
	public static final int STATUS_NORMAL = 0;
	
	public Category() {
		this.id = 0L;
		this.title = "";
		this.status = 0;
	}

	/**********************************************
	 * SETTERS
	 *********************************************/
	
	public void setId(long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title.toLowerCase();
	}
	
	public void setStatus(int status){
		this.status = status;
	}

	/**********************************************
	 * GETTERS
	 *********************************************/
	
	public long getId() {
		return id;
	}

	public String getTitle() {
		return StringUtils.capitalize(this.title);
	}
	
	public int getStatus(){
		return this.status;
	}

	public boolean isFavourite(){
		if(this.getStatus() == this.STATUS_FAVOURITE){
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "Category [getId()=" + getId() + ", getTitle()=" + getTitle()
				+ ", getStatus()=" + getStatus() + "]";
	}
	
	
	
}
