package com.smileyjoedev.rssreader;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Views {

	private Context context;
	
	public Views(Context context) {
		this.context = context;
	}
	
	public RSSListAdapter rssList(ArrayList<RSS> rssFeed, ListView list) {
//		TextView emptyView = (TextView) findViewById(R.id.tv_rss_feed_empty);
		
		int first = list.getFirstVisiblePosition();
		View topChild = list.getChildAt(0);
		int top;
		
		if(topChild == null){
			top = 0;
		}else{
			top = topChild.getTop();
		}
		
		RSSListAdapter adapter = new RSSListAdapter(this.context, rssFeed);
		list.setAdapter(adapter);
		list.setSelectionFromTop(first, top);
//		list.setEmptyView(emptyView);
		
		return adapter;
	}
	
}
