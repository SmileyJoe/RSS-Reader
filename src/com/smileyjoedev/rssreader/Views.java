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
	
	public RSSListAdapter rssList(ArrayList<RSS> rssFeed, ListView list, TextView emptyView) {
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
		list.setEmptyView(emptyView);
		
		return adapter;
	}
	
	public CategoryListAdapter categoryList(ArrayList<Category> categories, ListView list, TextView emptyView) {
		int first = list.getFirstVisiblePosition();
		View topChild = list.getChildAt(0);
		int top;
		
		if(topChild == null){
			top = 0;
		}else{
			top = topChild.getTop();
		}
		
		CategoryListAdapter adapter = new CategoryListAdapter(this.context, categories);
		list.setAdapter(adapter);
		list.setSelectionFromTop(first, top);
		list.setEmptyView(emptyView);
		
		return adapter;
	}
	
}
