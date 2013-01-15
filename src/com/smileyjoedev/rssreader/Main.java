package com.smileyjoedev.rssreader;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

public class Main extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getSupportActionBar().hide();
		new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
            	Main.this.checkInternet();
            }
        }, 3000);
	}
	
	public void checkInternet(){
		if(Gen.isInternet(this)){
			startActivityForResult(Intents.rssList(this), 0);
		} else {
			startActivityForResult(Intents.noConnection(this), 1);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case 0:
				finish();
				break;
			case 1:
				startActivityForResult(Intents.rssList(this), 0);
				break;
		}
	}

}
