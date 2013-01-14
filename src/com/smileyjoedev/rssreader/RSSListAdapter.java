package com.smileyjoedev.rssreader;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RSSListAdapter extends BaseAdapter {

	private ArrayList<RSS> rssFeed;
	private Context context;
	private int lastPosition;
	
	public RSSListAdapter(Context context, ArrayList<RSS> rssFeed){
		this.rssFeed = rssFeed;
		this.context = context;
		this.lastPosition = 0;
	}
	
	public void setRssFeed(ArrayList<RSS> rssFeed){
		this.rssFeed = rssFeed;
	}
	
	@Override
	public int getCount() {
		return this.rssFeed.size();
	}

	@Override
	public Object getItem(int position) {
		return this.rssFeed.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RSS rss = this.rssFeed.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.xml.rss_row, null);
		int dayMonthYear = Integer.parseInt(rss.getDate(TimeStamp.DAY_MONTH_YEAR_CONCAT));
		
		TextView rssTitle = (TextView) convertView.findViewById(R.id.tv_rss_title);
		TextView rssDescription = (TextView) convertView.findViewById(R.id.tv_rss_description);
		TextView rssDate = (TextView) convertView.findViewById(R.id.tv_rss_date);
		TextView rssSectionTitle = (TextView) convertView.findViewById(R.id.tv_rss_section_title);
		LinearLayout llRSSDetailsWrapper = (LinearLayout) convertView.findViewById(R.id.ll_rss_details_wrapper);
		
		if(!this.showHeader(position)){
			rssSectionTitle.setVisibility(View.GONE);
		} else {
			rssSectionTitle.setText(rss.getDate(TimeStamp.SHORT_DATE));
		}
		
		rssTitle.setText(rss.getTitle());
		rssDescription.setText(rss.getShortDescription());
		rssDate.setText(rss.getDate(TimeStamp.LONG_DATE_TIME));
		
		if(rss.getRead()){
			llRSSDetailsWrapper.setBackgroundColor(this.context.getResources().getColor(R.color.grey_light));
		}
		
		return convertView;
	}
	
	private boolean showHeader(int position){
		boolean showHeader = false;
		boolean isForward = true;
		
		if(position == 0){
			showHeader = true;
		} else {
			int currDayMonthYear = Integer.parseInt(this.rssFeed.get(position).getDate(TimeStamp.DAY_MONTH_YEAR_CONCAT));
			int prevDayMonthYear = Integer.parseInt(this.rssFeed.get(position - 1).getDate(TimeStamp.DAY_MONTH_YEAR_CONCAT));
		
			if(currDayMonthYear != prevDayMonthYear){
				showHeader = true;
			}
		}
		
		this.lastPosition = position;
		
		return showHeader;
		
	}
	
}
