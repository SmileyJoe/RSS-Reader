package com.smileyjoedev.rssreader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Gen {
	
	public static boolean isInternet(Context context){
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		
		if(activeNetworkInfo != null){
			return true;
		} else {
			return false;
		}
	}
	
	public static void toast(Context context, String msg, int length) {
		switch(length){
			case 1:
				length = Toast.LENGTH_SHORT;
				break;
			case 2:
				length = Toast.LENGTH_LONG;
				break;
			default:
				length = Toast.LENGTH_SHORT;
				break;
		}
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
}
