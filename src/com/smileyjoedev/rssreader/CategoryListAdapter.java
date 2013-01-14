package com.smileyjoedev.rssreader;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {

	private ArrayList<Category> categories;
	private Context context;
	private int numFav;
	
	public CategoryListAdapter(Context context, ArrayList<Category> categories){
		this.categories = categories;
		this.context = context;
		this.numFav = 0;
		
		for(int i = 0; i < this.categories.size(); i++){
			if(this.categories.get(i).isFavourite()){
				this.numFav++;
			}
		}
	}
	
	public void setCategories(ArrayList<Category> categories){
		this.categories = categories;
	}
	
	@Override
	public int getCount() {
		return this.categories.size();
	}

	@Override
	public Object getItem(int position) {
		return this.categories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Category cat = this.categories.get(position);
		final int pos = position;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.xml.category_row, null);
		
		TextView catTitle = (TextView) convertView.findViewById(R.id.tv_category_title);
		ImageView favStar = (ImageView) convertView.findViewById(R.id.iv_favourite);
		
		catTitle.setText(cat.getTitle());
		
		if(cat.isFavourite()){
			favStar.setImageResource(R.drawable.star_full);
		}
		
		favStar.setOnClickListener(new OnClickListener(){

			private boolean isFav;
			private boolean favSet = false;
			private int posi = pos;
			private DbCategoryAdapter categoryAdapter = new DbCategoryAdapter(CategoryListAdapter.this.context);
			
			@Override
			public void onClick(View v) {
				
				if(!this.favSet){
					this.isFav = CategoryListAdapter.this.categories.get(this.posi).isFavourite();
					this.favSet = true;
				}
				
				
				if(this.isFav){
					ImageView image = (ImageView) v;
					image.setImageResource(R.drawable.star);
					this.isFav = false;
					CategoryListAdapter.this.numFav--;
					CategoryListAdapter.this.categories.get(this.posi).setStatus(Category.STATUS_NORMAL);
					this.categoryAdapter.updateStatus(CategoryListAdapter.this.categories.get(this.posi).getId(), Category.STATUS_NORMAL);
				} else {
					if(CategoryListAdapter.this.numFav < 4){
						ImageView image = (ImageView) v;
						image.setImageResource(R.drawable.star_full);
						this.isFav = true;
						CategoryListAdapter.this.numFav++;
						CategoryListAdapter.this.categories.get(this.posi).setStatus(Category.STATUS_FAVOURITE);
						this.categoryAdapter.updateStatus(CategoryListAdapter.this.categories.get(this.posi).getId(), Category.STATUS_FAVOURITE);
					} else {
						Gen.toast(CategoryListAdapter.this.context, CategoryListAdapter.this.context.getString(R.string.toast_to_many_fav), 1);
					}
				}
				
				
				
				
				
			}
			
		});
		
		return convertView;
	}
	
}
