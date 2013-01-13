package com.smileyjoedev.rssreader;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
