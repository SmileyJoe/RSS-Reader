package com.smileyjoedev.rssreader;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbCategoryAdapter {
	
	/*****************************************
	 * VARIABLES
	 ****************************************/
	
	private Context context;
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private Cursor cursor;
	private int idCol;
	private int titleCol;
	private int statusCol;
	
	/*****************************************
	 * CONSTRUCTOR
	 ****************************************/
	
	public DbCategoryAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
		this.db = dbHelper.getWritableDatabase();
	}
	
	/******************************************
	 * GET
	 *****************************************/
	
	public Category getDetails(long catId){
		this.setCursor("WHERE _id = '" + catId + "' ");
		return this.sortCursor();
	}
	
	public ArrayList<Category> get(){
		this.setCursor("");
		return this.sortCursorArrayList();
	}
	
	public ArrayList<Category> getByRssItem(long rssItemId){
		this.setCursorRelItem("WHERE relCat.rss_item_id = " + rssItemId + "");
		return this.sortCursorArrayList();
	}
	
	public ArrayList<Category> getFavourites(){
		this.setCursor("WHERE category_status = '" + Category.STATUS_FAVOURITE + "'");
		return this.sortCursorArrayList();
	}
	
	/******************************************
	 * SAVE
	 *****************************************/
	
	public long saveItem(Category cat) {
		long dbId = 0;
		
		ContentValues values = createContentValues(cat);
		dbId = this.db.insert("category", null, values);
		
		return dbId;
	}
	
	public long saveRelCat(long rssItemId, long catId){
		long dbId = 0;
		
		ContentValues values = createContentValuesRelItem(rssItemId, catId);
		dbId = this.db.insert("rss_item_rel_category", null, values);
		
		return dbId;
	}
	
	/*******************************************
	 * UPDATE
	 ******************************************/
	
	public void updateStatus(long catId, int statusId){
		ContentValues values = new ContentValues();
		
		values.put("category_status", statusId);
		
		db.update("category", values, " _id = '" + catId + "' ", null);
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void setCursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT _id, category_title, category_status "
				+ "FROM category " 
				+ " " + where + " "
				+ "ORDER BY category_status DESC, category_title ASC", null);
	}
	
	private void setCursorRelItem(String where){
		this.cursor = this.db.rawQuery(
				"SELECT cat._id, cat.category_title, cat.category_status "
				+ "FROM category cat "
				+ "JOIN rss_item_rel_category relCat ON cat._id = relCat._id"
				+ " " + where + " "
				+ "ORDER BY category_title ASC", null);
	}
	
	private void setColoumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.titleCol = this.cursor.getColumnIndex("category_title");
		this.statusCol = this.cursor.getColumnIndex("category_status");
	}
	
	private Category getData(){
		Category cat = new Category();
		
		cat.setId(this.cursor.getInt(this.idCol));
		cat.setTitle(this.cursor.getString(this.titleCol));
		cat.setStatus(this.cursor.getInt(this.statusCol));
		
		return cat;
	}
	
	private ArrayList<Category> sortCursorArrayList(){
		ArrayList<Category> cats = new ArrayList<Category>();
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				
				int i = 0;
				do{
					cats.add(this.getData());
				}while(this.cursor.moveToNext());
			}
		}
		this.cursor.close();
		return cats;
	}
	
	private Category sortCursor(){
		Category cat = new Category();
		
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				cat = this.getData();
			}
		}
		this.cursor.close();
		return cat;
	}
	
	private ContentValues createContentValues(Category cat) {
		ContentValues values = new ContentValues();
		
		values.put("category_title", cat.getTitle());
		values.put("category_status", cat.getStatus());
		
		return values;
	}
	
	private ContentValues createContentValuesRelItem(long itemId, long catId) {
		ContentValues values = new ContentValues();
		
		values.put("category_id", catId);
		values.put("rss_item_id", itemId);
		
		return values;
	}
	
	public void close(){
		this.db.close();
	}
	
	/**************************************
	 * CHECK
	 *************************************/
	
	public long checkCatExists(String title){
		long catId = 0L;
		
		Cursor cursor = this.db.rawQuery("SELECT _id FROM category WHERE category_title = '" + title + "'", null);
		
		if(cursor != null){
			cursor.moveToFirst();
			if(cursor.getCount() > 0){
				catId = cursor.getLong(cursor.getColumnIndex("_id"));
			}
		}
		
		return catId;
	}
	
}
