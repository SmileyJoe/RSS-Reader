package com.smileyjoedev.rssreader;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class CategoryList extends SherlockActivity implements OnItemClickListener {

	private DbCategoryAdapter categoryAdapter;
	private ListView lvCategories;
	private CategoryListAdapter categoryListAdapter;
	private Views views;
	private ArrayList<Category> categories;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_list);
		this.initialize();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		this.populateView();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.category_list, menu);
		
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case android.R.id.home:
				finish();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
    
    public void initialize(){
    	this.lvCategories = (ListView) findViewById(R.id.lv_categories);
    	this.lvCategories.setOnItemClickListener(this);
    	
    	this.categoryAdapter = new DbCategoryAdapter(this);
    	
    	this.views = new Views(this);
    	
    	this.categories = this.categoryAdapter.get();
    }
    
    public void populateView(){
    	this.categoryListAdapter = this.views.categoryList(this.categories, this.lvCategories, (TextView) findViewById(R.id.tv_categories_empty));
    }
    
    public void updateView(){
    	this.categories = this.categoryAdapter.get();
		this.categoryListAdapter.setCategories(this.categories);
		this.categoryListAdapter.notifyDataSetChanged();
		this.lvCategories.refreshDrawableState();
    }

	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		switch(view.getId()){
			case R.id.lv_categories:
				Intent resultIntent = new Intent();
				resultIntent.putExtra("category_id", this.categories.get(position).getId());
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
		}
		
	}
	
}
