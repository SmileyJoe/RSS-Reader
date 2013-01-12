package com.smileyjoedev.rssreader;

import android.content.Context;
import android.content.Intent;

public class Intents {

	public static Intent rssList(Context context){
		Intent intent = new Intent(context, RSSList.class);
		
		return intent;
	}
	
}
