package com.smileyjoedev.rssreader;

import android.util.Log;

public class RSS {

	private String title;
	private String description;
	private String link;
	private long id;
	private String category;
	private Long ut;
	
	public RSS() {
		this.title = "";
		this.description = "";
		this.link = "";
		this.id = 0;
		this.category = "";
	}
	
	/***********************************************
	 * SETTERS
	 **********************************************/
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setCategory(String category){
		this.category = category;
	}
	
	public void setUt(String date){
		this.ut = TimeStamp.getUt(date);
	}
	
	public void setUt(Long ut){
		this.ut = ut;
	}

	/***********************************************
	 * GETTERS
	 **********************************************/
	
	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getLink() {
		return link;
	}

	public long getId() {
		return id;
	}
	
	public String getCategory(){
		return category;
	}
	
	public long getUt(){
		return this.ut;
	}
	
	public String getDate(String format){
		String date = TimeStamp.getDate(this.getUt(), format);
		
		return date;
	}
	
	public String getShortDescription(){
		String shortDescription = this.description.replaceAll("\\<.*?\\>", "");
		
		int end = this.description.indexOf(" ", 150);
		Log.d("SmileyJoeDev", "End:" + end);
		Log.d("SmileyJoeDev", "Length:" + shortDescription.length());
		if(end > 0 && end < shortDescription.length()){
			shortDescription = shortDescription.substring(0, end) + "...";
		}
		
		return shortDescription;
	}
	
	/*****************************************************
	 * TOSTRING
	 ****************************************************/
	
	@Override
	public String toString() {
		return "RSS [getTitle()=" + getTitle() + ", getDescription()="
				+ getDescription() + ", getLink()=" + getLink() + ", getId()="
				+ getId() + "]";
	}
	
	
	
}
