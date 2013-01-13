package com.smileyjoedev.rssreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class Intents {

	public static Intent rssList(Context context){
		Intent intent = new Intent(context, RSSList.class);
		
		return intent;
	}
	
	public static Intent rssItemView(Context context, long itemId){
		Intent intent = new Intent(context, RssItemView.class);
		Log.d("SmileyJoeDev", "Intent ItemId: " + itemId);
		Bundle extras = new Bundle();
		extras.putLong("rss_item_id", itemId);
		intent.putExtras(extras);	
		
		return intent;
	}
	
	public static Intent noConnection(Context context){
		Intent intent = new Intent(context, NoConnection.class);
		
		return intent;
	}
	
}
