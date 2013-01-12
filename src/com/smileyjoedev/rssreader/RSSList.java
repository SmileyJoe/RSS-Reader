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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class RSSList extends SherlockActivity {

	private ListView lvRssFeed;
	private ArrayList<RSS> rssFeed;
	private RSSListAdapter rssListAdapter;
	private Views views;
	private ImageView refreshView;
	private MenuItem refreshItem;
	private boolean downloading;
	private Animation rotateClockwise;
	private LayoutInflater inflater;
	
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
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.populateView();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.rss_list, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
	        case R.id.menu_refresh:
	        	this.downloading = true;
	            // Apply the animation to our View
	            this.refreshView.startAnimation(this.rotateClockwise);

	            // Apply the View to our MenuItem
	            this.refreshItem = item;
	            this.refreshItem.setActionView(this.refreshView);
	            
	            ArrayList<RSS> newFeeds = this.getRSS("http://iol.co.za/cmlink/1.738");
	            
	            for(int i = 0; i < newFeeds.size(); i++){
	            	this.rssFeed.add(newFeeds.get(i));
	            	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	            
	            this.rssListAdapter.setRssFeed(this.rssFeed);
	        	this.rssListAdapter.notifyDataSetChanged();
	    		this.lvRssFeed.refreshDrawableState();
	            
	    		
	    		this.downloading = false;
//	        	Notify.toast(this, R.string.toast_updating);
//	        	this.numSmsSent = 3;
//	        	Send.sms(this, "31050", "MM");
//	        	Send.sms(this, "31050", "SB");
//	        	Send.sms(this, "31050", "AM");
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    public void initialize(){
    	this.lvRssFeed = (ListView) findViewById(R.id.lv_rss_feed);
    	
    	this.views = new Views(this);
    	
    	this.inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    	this.refreshView = (ImageView) this.inflater.inflate(R.xml.refresh_actionview, null);
    	this.rotateClockwise = AnimationUtils.loadAnimation(this, R.anim.rotate);
        this.rotateClockwise.setAnimationListener(this.rotateListener);
        this.downloading = false;
    }
    
    public void populateView(){
    	this.rssFeed = this.getRSS("http://iol.co.za/cmlink/1.738");
        	
    	this.rssListAdapter = this.views.rssList(this.rssFeed, (ListView) findViewById(R.id.lv_rss_feed));
    }
    
    public ArrayList<RSS> getRSS(String url){
        ArrayList<RSS> rssFeed = new ArrayList<RSS>();
        RSS rss = new RSS();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(getInputStream(url), "UTF_8");
            
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
                        	rss.setCategory(parser.nextText());
                        }
                    } else if(parser.getName().equalsIgnoreCase("pubDate")){
                    	if(isItem){
                    		rss.setUt(parser.nextText());
                    	}
                    }
                }else if(eventType == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")){
                	rssFeed.add(rss);
                	isItem = false;
                }
                eventType = parser.next(); //move to next element
                
            }
            
        } catch (MalformedURLException e) {
        	Log.d("SmileyJoeDev", "Malformed");
            e.printStackTrace();
        } catch (XmlPullParserException e) {
        	Log.d("SmileyJoeDev", "XML parse exception");
            e.printStackTrace();
        } catch (IOException e) {
        	Log.d("SmileyJoeDev", "IOException");
            e.printStackTrace();
        }
        
        return rssFeed;

    }
    
    public InputStream getInputStream(String url) {
    	InputStream content = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			content = response.getEntity().getContent();
		} catch (Exception e) {
			Log.v("SmileyJoeDev", e.toString());
		}
		return content;
     }	
}
