package com.smileyjoedev.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NoConnection extends Activity implements OnClickListener {
	
	private Button btOk;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.no_connection);
		this.initialize();
		
	}
	
    private void initialize(){
    	this.btOk = (Button) findViewById(R.id.bt_ok);
    	this.btOk.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_ok:
				finish();
				break;
		}
		
	}
}
