package com.smileyjoedev.rssreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class RssItemView extends SherlockActivity {
	
	private TextView tvTitle;
	private TextView tvDate;
	private TextView tvAuthor;
	private WebView wvRssItemDescription;
	private RSS rss;
	private DbRSSAdapter rssAdapter;
	private long itemId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss_item_view);
		this.initialize();
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
		
        if(extras.containsKey("rss_item_id")){
        	this.itemId = extras.getLong("rss_item_id");
        	Log.d("SmileyJoeDev", "OnCreate ItemId: " + this.itemId);
        } else {
        	this.itemId = 0L;
        }
		
		this.populateView();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.rss_item_view, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
	        case R.id.menu_share:
	        	Intent shareIntent = new Intent(Intent.ACTION_SEND);

	        	shareIntent.setType("text/plain");
	        	shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this article: " + this.rss.getLink());
	        	startActivity(Intent.createChooser(shareIntent, "Share using"));
	        	return true;
	        case R.id.menu_open_browser:
	        	Intent browserIntent = new Intent(Intent.ACTION_VIEW);
	        	browserIntent.setData(Uri.parse(this.rss.getLink()));
	        	startActivity(browserIntent);
	        	return true;
	        case R.id.menu_comment:
	        	Intent commentIntent = new Intent(Intent.ACTION_VIEW);
	        	commentIntent.setData(Uri.parse(this.rss.getCommentsLink()));
	        	startActivity(commentIntent);
	        	return true;
	        case android.R.id.home:
				finish();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    public void initialize(){
    	this.tvDate = (TextView) findViewById(R.id.tv_rss_date);
    	this.tvTitle = (TextView) findViewById(R.id.tv_rss_title);
    	this.tvAuthor = (TextView) findViewById(R.id.tv_rss_author);
    	this.rssAdapter = new DbRSSAdapter(this);
    	this.rss = new RSS();
    	this.wvRssItemDescription = (WebView) findViewById(R.id.wv_rss_item_description);
    	this.wvRssItemDescription.setHorizontalScrollBarEnabled(true);
    }
    
    public void populateView(){
    	this.rss = this.rssAdapter.getItemDetails(this.itemId);
    	this.tvTitle.setText(this.rss.getTitle());
    	this.tvDate.setText(this.rss.getDate(TimeStamp.LONG_DATE_TIME));
    	this.tvAuthor.setText(this.rss.getAuthor());
//    	this.wvRssItemDescription.loadData("<html><body>" + this.rss.getContent() + "</body></html>", "text/html", "UTF-8");
    	this.wvRssItemDescription.loadDataWithBaseURL(this.rss.getLink(), this.rss.getContent(), "text/html", "UTF-8", null);
    }
}
