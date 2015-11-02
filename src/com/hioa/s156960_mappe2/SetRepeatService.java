package com.hioa.s156960_mappe2;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;


/**
 * Sets the time for the birthday messages
 */
public class SetRepeatService extends Service {
	public static final String TAG = "IN SETREPEAT";
	Context context;
	boolean smsEnabled;
	int hours;
	int minutes;

	@Override
	public int onStartCommand(Intent intent, int flags,	int startId)	{	

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		// get information about the toggle state of the check box in sharedPreferences 
		smsEnabled = settings.getBoolean("pref_key_sms_service", false);

		// if the sms service is enabled, a birthday sms will be sent on the time specified in sharedPreferences
		if (smsEnabled) {
			Calendar calendar = Calendar.getInstance();	

			// get time from sharedPreferences
			Date date = new Date(settings.getLong("pref_key_set_time", 0));
			
			// replace with hour and minutes from shared prefrences
			calendar.set(Calendar.HOUR_OF_DAY, date.getHours()); 
			calendar.set(Calendar.MINUTE, date.getMinutes()); 
			
			// create pending intent
			Intent notifcationAlarmService = new Intent(this,	NotificationAlarmService.class);	
			PendingIntent pendingIntent = PendingIntent.getService(this, 0,	notifcationAlarmService, 0);
			
			// set the pending intent to react in accordance with the alarmManager
			AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);	
			alarm.setRepeating(AlarmManager.RTC_WAKEUP,	calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,	pendingIntent);		
		} else {
			// Stop the service
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			PendingIntent pSmsIntent = PendingIntent.getService(this, 0, new Intent(this, NotificationAlarmService.class), 0);
			alarmManager.cancel(pSmsIntent);
		}

		return super.onStartCommand(intent,	flags, startId);	
	}	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
