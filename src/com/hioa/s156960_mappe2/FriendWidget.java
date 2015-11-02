package com.hioa.s156960_mappe2;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;


import android.widget.RemoteViews;

public class FriendWidget extends AppWidgetProvider{
	MyApplication myApp;
	ArrayList<Friend> allFriends;
	Calendar calendar;
	StringBuilder birthdayNames;


	@Override
	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds){	
		RemoteViews updateViews	= new RemoteViews(context.getApplicationContext().getPackageName(),R.layout.widget_layout);	
		calendar = Calendar.getInstance();
		myApp = (MyApplication) context.getApplicationContext();
		
		allFriends = myApp.friendsDB.getAllFriends();

		// get todays date in proper format, according to the phone's settings
		

		String todaysDate = DateUtils.formatDateTime(context, calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE);
		
		birthdayNames = new StringBuilder();
		birthdayNames.append(todaysDate + "\n");
		
		for (Friend friend : allFriends) {
			String friendsBirthday = myApp.removeYearFromBirthday(friend.getBirthday());
		
			if (friendsBirthday.equals(todaysDate)) {
				
				if (friend.getFirstName() != null && !friend.getFirstName().equals("null")) {
					birthdayNames.append(friend.getFirstName() + " ");
				}

				if (friend.getLastName() != null && !friend.getLastName().equals("null")) {
					birthdayNames.append(friend.getLastName() + " ");
				}
				
				birthdayNames.append("\n");
			}
		}
		
		updateViews.setTextViewText(R.id.widget_text, birthdayNames.toString());

		Intent clickIntent	= new Intent(context, FriendWidget.class);	
		clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

		PendingIntent pendingIntent	= PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);			
		updateViews.setOnClickPendingIntent(R.id.widget_text, pendingIntent);	
		appWidgetManager.updateAppWidget(appWidgetIds,updateViews);


	}

}

