package com.smileyjoedev.rssreader;

import java.util.ArrayList;

import android.util.Log;

public class RSS {

	private String title;
	private String description;
	private String link;
	private long id;
	private ArrayList<Category> categories;
	private Long ut;
	private String content;
	private String commentsLink;
	private String author;
	private boolean read;
	
	public RSS() {
		this.title = "";
		this.description = "";
		this.link = "";
		this.id = 0;
		this.categories = new ArrayList<Category>();
		this.ut = 0L;
		this.content = "";
		this.commentsLink = "";
		this.author = "";
		this.read = false;
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
	
	public void addCategory(String title){
		this.addCategory(title, 0L);
	}
	
	public void addCategory(String title, long id){
		Category cat = new Category();
		
		cat.setTitle(title);
		cat.setId(id);
		
		this.categories.add(cat);
	}
	
	public void setCategories(ArrayList<Category> categories){
		this.categories = categories;
	}
	
	public void setUt(String date){
		this.ut = TimeStamp.getUt(date);
	}
	
	public void setUt(Long ut){
		this.ut = ut;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public void setCommentsLink(String commentsLink){
		this.commentsLink = commentsLink;
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public void setRead(boolean read){
		this.read = read;
	}
	
	public void setRead(int read){
		if(read == 1){
			this.setRead(true);
		} else {
			this.setRead(false);
		}
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
	
	public ArrayList<Category> getCategories(){
		return this.categories;
	}
	
	public Category getCategory(int id){
		return this.categories.get(id);
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
		if(end > 0 && end < shortDescription.length()){
			shortDescription = shortDescription.substring(0, end) + "...";
		}
		
		return shortDescription;
	}
	
	public String getContent(){
		return this.content;
	}
	
	public String getCommentsLink(){
		return this.commentsLink;
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public boolean getRead(){
		return this.read;
	}
	
	public int getReadInt(){
		if(this.getRead()){
			return 1;
		} else {
			return 0;
		}
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
