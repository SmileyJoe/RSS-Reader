package com.smileyjoedev.rssreader;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbRSSAdapter {
	
	/*****************************************
	 * VARIABLES
	 ****************************************/
	
	private Context context;
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private Cursor cursor;
	private DbCategoryAdapter dbCatAdapter;
	private int idCol;
	private int titleCol;
	private int descriptionCol;
	private int utCol;
	private int linkCol;
	private int contentCol;
	private int commentsLinkCol;
	private int authorCol;
	
	/*****************************************
	 * CONSTRUCTOR
	 ****************************************/
	
	public DbRSSAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
		this.db = dbHelper.getWritableDatabase();
		this.dbCatAdapter = new DbCategoryAdapter(this.context);
	}
	
	/******************************************
	 * GET
	 *****************************************/
	
	public RSS getItemDetails(long itemId){
		this.setCursorItem("WHERE _id = '" + itemId + "' ");
		return this.sortCursorItem();
	}
	
	public ArrayList<RSS> getItems(){
		this.setCursorItem("");
		return this.sortCursorArrayListItem();
	}
	
	/******************************************
	 * SAVE
	 *****************************************/
	
	public long saveItem(RSS rss) {
		Log.d("SmileyJoeDev", "RssAdapter Save");
		long dbId = 0;
		
		ContentValues values = createContentValuesItem(rss);
		dbId = this.db.insert("rss_item", null, values);
		
		for(int i = 0; i < rss.getCategories().size(); i++){
			long catId = this.dbCatAdapter.checkCatExists(rss.getCategories().get(i).getTitle());
			
			if(catId == 0){
				catId = this.dbCatAdapter.saveItem(rss.getCategory(i));
			}
			
			rss.getCategory(i).setId(catId);
			
			this.dbCatAdapter.saveRelCat(dbId, catId);
		}
		
		return dbId;
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void setCursorItem(String where){
		this.cursor = this.db.rawQuery(
				"SELECT _id, rss_item_title, rss_item_description, rss_item_ut, rss_item_link, rss_item_content, rss_item_comments_link, rss_item_author "
				+ "FROM rss_item " 
				+ " " + where + " "
				+ "ORDER BY rss_item_ut DESC", null);
	}
	
	private void setColoumnsItem(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.titleCol = this.cursor.getColumnIndex("rss_item_title");
		this.descriptionCol = this.cursor.getColumnIndex("rss_item_description");
		this.utCol = this.cursor.getColumnIndex("rss_item_ut");
		this.linkCol = this.cursor.getColumnIndex("rss_item_link");
		this.contentCol = this.cursor.getColumnIndex("rss_item_content");
		this.commentsLinkCol = this.cursor.getColumnIndex("rss_item_comments_link");
		this.authorCol = this.cursor.getColumnIndex("rss_item_author");
	}
	
	private RSS getDataItem(){
		RSS rss = new RSS();
		
		rss.setId(this.cursor.getLong(this.idCol));
		rss.setTitle(this.cursor.getString(this.titleCol));
		rss.setDescription(this.cursor.getString(this.descriptionCol));
		rss.setUt(this.cursor.getLong(this.utCol));
		rss.setLink(this.cursor.getString(this.linkCol));
		rss.setContent(this.cursor.getString(this.contentCol));
		rss.setCommentsLink(this.cursor.getString(this.commentsLinkCol));
		rss.setAuthor(this.cursor.getString(this.authorCol));
		rss.setCategories(this.dbCatAdapter.getByRssItem(rss.getId()));
		
		return rss;
	}
	
	private ArrayList<RSS> sortCursorArrayListItem(){
		ArrayList<RSS> rssFeed = new ArrayList<RSS>();
		this.setColoumnsItem();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				
				int i = 0;
				do{
					rssFeed.add(this.getDataItem());
				}while(this.cursor.moveToNext());
			}
		}
		this.cursor.close();
		return rssFeed;
	}
	
	private RSS sortCursorItem(){
		RSS rss = new RSS();
		
		this.setColoumnsItem();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				rss = this.getDataItem();
			}
		}
		this.cursor.close();
		return rss;
	}
	
	private ContentValues createContentValuesItem(RSS rss) {
		ContentValues values = new ContentValues();
		
		values.put("rss_item_title", rss.getTitle());
		values.put("rss_item_description", rss.getDescription());
		values.put("rss_item_ut", rss.getUt());
		values.put("rss_item_link", rss.getLink());
		values.put("rss_item_content", rss.getContent());
		values.put("rss_item_comments_link", rss.getCommentsLink());
		values.put("rss_item_author", rss.getAuthor());
		
		return values;
	}
	
	public void close(){
		this.db.close();
	}
	
}
