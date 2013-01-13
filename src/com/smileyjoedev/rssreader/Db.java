package com.smileyjoedev.rssreader;

import android.database.sqlite.SQLiteDatabase;

public class Db {
	// Strings to create the DB titles //
	private static final String CREATE_RSS_ITEM = "CREATE TABLE rss_item (_id integer primary key autoincrement, rss_item_title String not null, rss_item_description String not null, rss_item_ut long not null, rss_item_link String not null, rss_item_content String not null, rss_item_comments_link String not null, rss_item_author String not null);";
	private static final String CREATE_CATEGORY = "CREATE TABLE category (_id integer primary key autoincrement, category_title String not null, category_status integer not null)";
	private static final String CREATE_RSS_ITEM_REL_CATEGORY = "CREATE TABLE rss_item_rel_category (_id integer primary key autoincrement, rss_item_id integer not null, category_id integer not null)";
	
 	public static void onCreate(SQLiteDatabase database) {
 		// Create all the tables the first time the app is run //
		database.execSQL(CREATE_RSS_ITEM);
		database.execSQL(CREATE_CATEGORY);
		database.execSQL(CREATE_RSS_ITEM_REL_CATEGORY);
		
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		
	}
	
}
