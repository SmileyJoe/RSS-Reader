package com.smileyjoedev.rssreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class RSSList extends SherlockActivity implements OnItemClickListener, OnClickListener {

	private ListView lvRssFeed;
	private ArrayList<RSS> rssFeed;
	private RSSListAdapter rssListAdapter;
	private Views views;
	private ImageView refreshView;
	private MenuItem refreshItem;
	private boolean downloading;
	private Animation rotateClockwise;
	private LayoutInflater inflater;
	private DbRSSAdapter rssAdapter;
	private TextView tvRssFeedEmpty;
	private SharedPreferences prefs;
	private Menu menu;
	private long catId;
	private Category category;
	private DbCategoryAdapter categoryAdapter;
	private ArrayList<Category> favourites;
	private TextView tvFavOne;
	private TextView tvFavTwo;
	private TextView tvFavThree;
	private TextView tvFavFour;
	private ActionBar actionBar;
	
	private AnimationListener rotateListener = new AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {}
		
		@Override
		public void onAnimationRepeat(Animation animation) {}
		
		/*
		 * A hacked way to start/stop the animation after it has complete
		 * it's current cycle.
		 */
		@Override
		public void onAnimationEnd(Animation animation) {
			if(downloading) { // Download complete? Stop.
				refreshView.startAnimation(rotateClockwise);
			} else { // Still downloading? Start again.
				refreshView.clearAnimation();
				refreshItem.setActionView(null);				
			}
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss_list);
		this.initialize();
		
		this.populateView();
		this.populateFavourites();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.rss_list, menu);
        this.menu = menu;
        
		if(!this.prefs.contains("rss_feed_last_update_ut") || (this.prefs.getLong("rss_feed_last_update_ut", 0) == 0)){
			this.updateRssFeed(menu.getItem(0));
		}
        return super.onCreateOptionsMenu(menu);
    }
	
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
	        case R.id.menu_refresh:
	        	this.updateRssFeed(item);
	            return true;
	        case R.id.menu_categories:
	        	startActivityForResult(Intents.categoryList(this), 0);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    public void initialize(){
    	this.rssFeed = new ArrayList<RSS>();
    	this.lvRssFeed = (ListView) findViewById(R.id.lv_rss_feed);
    	this.lvRssFeed.setOnItemClickListener(this);
    	
    	this.views = new Views(this);
    	
    	this.inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    	this.refreshView = (ImageView) this.inflater.inflate(R.xml.refresh_actionview, null);
    	this.rotateClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate);
        this.rotateClockwise.setAnimationListener(this.rotateListener);
        this.downloading = false;
        
        this.rssAdapter = new DbRSSAdapter(this);
        
        this.tvRssFeedEmpty = (TextView) findViewById(R.id.tv_rss_feed_empty);
        
        this.prefs = this.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        
        this.catId = 0;
        this.category = new Category();
        
        this.categoryAdapter = new DbCategoryAdapter(this);
        
        this.favourites = new ArrayList<Category>();
        
        this.tvFavOne = (TextView) findViewById(R.id.tv_favourite_one);
        this.tvFavTwo = (TextView) findViewById(R.id.tv_favourite_two);
        this.tvFavThree = (TextView) findViewById(R.id.tv_favourite_three);
        this.tvFavFour = (TextView) findViewById(R.id.tv_favourite_four);
        
        this.actionBar = getSupportActionBar();
    }
    
    public void populateFavourites(){
    	this.tvFavOne.setOnClickListener(this);
    	this.tvFavTwo.setOnClickListener(this);
    	this.tvFavThree.setOnClickListener(this);
    	this.tvFavFour.setOnClickListener(this);
    	
    	this.favourites = this.categoryAdapter.getFavourites();
    	
    	for(int i = 0; i < this.favourites.size(); i++){
    		String title = this.favourites.get(i).getTitle();
    		switch(i){
	    		case 0:
	    			this.tvFavOne.setText(title);
	    			this.tvFavOne.setId(0);
	    			break;
	    		case 1:
	    			this.tvFavTwo.setText(title);
	    			this.tvFavTwo.setId(1);
	    			break;
	    		case 2:
	    			this.tvFavThree.setText(title);
	    			this.tvFavThree.setId(2);
	    			break;
	    		case 3:
	    			this.tvFavFour.setText(title);
	    			this.tvFavFour.setId(3);
	    			break;
    		}
    	}
    	
    	switch(this.favourites.size()){
	    	case 0:
	    		this.tvFavOne.setVisibility(View.GONE);
	    		this.tvFavTwo.setVisibility(View.GONE);
	    		this.tvFavThree.setVisibility(View.GONE);
	    		this.tvFavFour.setVisibility(View.GONE);
	    		break;
	    	case 1:
	    		this.tvFavOne.setVisibility(View.VISIBLE);
	    		this.tvFavTwo.setVisibility(View.GONE);
	    		this.tvFavThree.setVisibility(View.GONE);
	    		this.tvFavFour.setVisibility(View.GONE);
	    		break;
	    	case 2:
	    		this.tvFavOne.setVisibility(View.VISIBLE);
	    		this.tvFavTwo.setVisibility(View.VISIBLE);
	    		this.tvFavThree.setVisibility(View.GONE);
	    		this.tvFavFour.setVisibility(View.GONE);
	    		break;
	    	case 3:
	    		this.tvFavOne.setVisibility(View.VISIBLE);
	    		this.tvFavTwo.setVisibility(View.VISIBLE);
	    		this.tvFavThree.setVisibility(View.VISIBLE);
	    		this.tvFavFour.setVisibility(View.GONE);
	    		break;
	    	case 4:
	    		this.tvFavOne.setVisibility(View.VISIBLE);
	    		this.tvFavTwo.setVisibility(View.VISIBLE);
	    		this.tvFavThree.setVisibility(View.VISIBLE);
	    		this.tvFavFour.setVisibility(View.VISIBLE);
	    		break;
    	}
    	
    	
    }
    
    public void populateView(){
    	this.rssFeed = this.rssAdapter.getItems();
    	this.changeBarTitle("All");
    	this.rssListAdapter = this.views.rssList(this.rssFeed, this.lvRssFeed, (TextView) findViewById(R.id.tv_rss_feed_empty));
    }
    

    public void updateRssFeed(MenuItem item){
    	if(Gen.isInternet(this)){
    		Gen.toast(this, this.getString(R.string.update), 1);
	    	this.downloading = true;
	        // Apply the animation to our View
	        this.refreshView.startAnimation(this.rotateClockwise);
	
	        // Apply the View to our MenuItem
	        this.refreshItem = item;
	        this.refreshItem.setActionView(this.refreshView);
	        
	        GetRSSFeedTask task = new GetRSSFeedTask(this, "http://feeds.feedburner.com/9to5Google");
			task.execute(0);
    	} else {
    		Gen.toast(this, this.getString(R.string.update_no_internet), 1);
    	}
		
    }
    
    public void changeCategory(long catId){
    	this.catId = catId;
    	if(catId == 1){
    		this.updateView();
    	} else if(catId != 0){
        	this.category = this.categoryAdapter.getDetails(this.catId);
        	this.changeBarTitle(this.category.getTitle());
        	this.rssFeed = this.rssAdapter.getByCategory(this.catId);
        	this.refreshView();
    	}
    	
    }
    
    public void updateView(){
    	if(this.catId > 1){
    		this.changeBarTitle(this.category.getTitle());
    		this.rssFeed = this.rssAdapter.getByCategory(this.catId);
    	} else {
    		this.changeBarTitle("All");
    		this.rssFeed = this.rssAdapter.getItems();
    	}
    	
    	 this.refreshView();
    }
    
    public void changeBarTitle(String title){
    	this.actionBar.setTitle(this.getString(R.string.bar_title_rss_list) + ": " + title);
    }
    
    public void refreshView(){
    	this.rssListAdapter.setRssFeed(this.rssFeed);
	     this.rssListAdapter.notifyDataSetChanged();
	     this.lvRssFeed.refreshDrawableState();
    }
    
    public InputStream getInputStream(String url) {
    	InputStream content = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			content = response.getEntity().getContent();
		} catch (Exception e) {
		}
		return content;
    }	
    
	private class GetRSSFeedTask extends AsyncTask{
		String url;
		SharedPreferences prefs;
		DbRSSAdapter rssAdapter;
		
		public GetRSSFeedTask(Context context, String url){
			this.url = url;
			this.prefs = context.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
			this.rssAdapter = new DbRSSAdapter(context);
		}
		 
		protected void onPreExecute(){
		}
		    
		@Override
		protected void onPostExecute(Object updated)   {
			RSSList.this.downloading = false;
		}
		    
		@Override
		protected void onProgressUpdate(Object... params){
		}
		
		@Override
		protected Object doInBackground(Object... params) {
			final ArrayList<RSS> newFeeds = this.getRSS(this.url);
			
			for(int i = 0; i < newFeeds.size(); i++){
				this.rssAdapter.saveItem(newFeeds.get(i));
			}
			
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(newFeeds.size() == 0){
						Gen.toast(RSSList.this, RSSList.this.getString(R.string.update_no_new), 1);
					} else {
						Gen.toast(RSSList.this, RSSList.this.getString(R.string.update_new), 1);
					}
					RSSList.this.updateView();
				}
			});
			   
			return null;
		}
		
		public ArrayList<RSS> getRSS(String url){
			ArrayList<RSS> rssFeed = new ArrayList<RSS>();
			ArrayList<RSS> tempRss = new ArrayList<RSS>();
			RSS rss = new RSS();
			long currUt = 0;
			
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(false);
				XmlPullParser parser = factory.newPullParser();
				
				parser.setInput(getInputStream(url), "UTF_8");
				
				currUt = TimeStamp.getCurrentUt();
				
				boolean isItem = false;
				
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						if (parser.getName().equalsIgnoreCase("item")) {
							rss = new RSS();
							isItem = true;
						} else if (parser.getName().equalsIgnoreCase("title")) {
							if (isItem){
								rss.setTitle(parser.nextText());
							}
						} else if(parser.getName().equalsIgnoreCase("link")){
							if (isItem){
								rss.setLink(parser.nextText());
							}
						} else if(parser.getName().equalsIgnoreCase("description")){
							if (isItem){
								rss.setDescription(parser.nextText());
							}
						} else if(parser.getName().equalsIgnoreCase("category")){
							if (isItem){
								rss.addCategory(parser.nextText());
							}
						} else if(parser.getName().equalsIgnoreCase("pubDate")){
							if(isItem){
								rss.setUt(parser.nextText());
							}
						} else if(parser.getName().equalsIgnoreCase("content:encoded")){
							if(isItem){
								rss.setContent(parser.nextText());
							}
						} else if(parser.getName().equalsIgnoreCase("comments")){
							if(isItem){
								rss.setCommentsLink(parser.nextText());
							}
						} else if(parser.getName().equalsIgnoreCase("dc:creator")){
							if(isItem){
								rss.setAuthor(parser.nextText());
							}
						}
					}else if(eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")){
						tempRss.add(rss);
						isItem = false;
					}
					eventType = parser.next(); //move to next element
				        
				}
			    
			} catch (MalformedURLException e) {
//				Gen.toast(RSSList.this, RSSList.this.getString(R.string.update_failed), 1);
				e.printStackTrace();
			} catch (XmlPullParserException e) {
//				Gen.toast(RSSList.this, RSSList.this.getString(R.string.update_failed), 1);
				e.printStackTrace();
			} catch (IOException e) {
//				Gen.toast(RSSList.this, RSSList.this.getString(R.string.update_failed), 1);
				e.printStackTrace();
			}
			
			
			
			for(int i = 0; i < tempRss.size(); i++){
				if(tempRss.get(i).getUt() > this.prefs.getLong("rss_feed_last_update_ut", 0)){
					rssFeed.add(tempRss.get(i));
				}
			}
			
			SharedPreferences.Editor prefsEditor = this.prefs.edit();
			prefsEditor.putLong("rss_feed_last_update_ut", currUt);
			prefsEditor.commit();
			    
			return rssFeed;
		
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		switch(view.getId()){
			case R.id.lv_rss_feed:
				startActivityForResult(Intents.rssItemView(this, this.rssFeed.get(position).getId()), 1);
				break;
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case 0:
				if(resultCode == Activity.RESULT_OK){
					if(data.hasExtra("category_id")){
						this.changeCategory(data.getLongExtra("category_id", 0));
					}
				}
				
				this.populateFavourites();
				break;
			case 1:
				this.updateView();
				break;
		}
	}

	@Override
	public void onClick(View v) {
		this.changeCategory(this.favourites.get(v.getId()).getId());
		
	}
}
