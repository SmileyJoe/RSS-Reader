package com.smileyjoedev.rssreader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class TimeStamp {
	
	public static final String SHORT_DATE = "dd MMM yyyy";
	public static final String LONG_DATE = "dd MMMM yyyy HH:mm:ss";
	public static final String LONG_DATE_TIME = "dd MMM yyyy HH:mm:ss";
	public static final String DAY_MONTH_YEAR_CONCAT = "ddMMyyyy";

	public static long getUt(String date){
		long ut = 0;
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
			Date d = format.parse(date);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			
			ut = cal.getTimeInMillis() / 1000L;
			
		} catch (ParseException e) {
			Log.d("SmileyJoeDev", "Date format incorrect");
		}
		
		return ut;
	}
	
	public static String getDate(long ut, String format){
		String date = "";
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		date = formatter.format(new Date(ut * 1000L));
		
		return date;
	}
	
}
