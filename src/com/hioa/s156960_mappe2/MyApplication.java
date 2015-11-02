package com.hioa.s156960_mappe2;

import java.util.ArrayList;
import java.util.Locale;


import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyApplication extends Application {

	private Locale locale = null;
	ArrayList<Friend> friendList;
	FriendsDB friendsDB;
	public static final String KEY_PREF_LANGUAGE = "pref_key_language";


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (locale != null)
		{
			newConfig.locale = locale;
			Locale.setDefault(locale);
			getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		friendList = new ArrayList<Friend>();
		friendsDB = new FriendsDB(this);

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		Configuration config = getBaseContext().getResources().getConfiguration();

		// Gets the value of the language preference key (en_GB if english, nb_NO if norwegian)
		String lang = settings.getString(KEY_PREF_LANGUAGE, " ");
		if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
		{
			locale = new Locale(lang);
			Locale.setDefault(locale);
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		}



	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public void setLocale(String lang) { 

		Configuration config = getBaseContext().getResources().getConfiguration();
		if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {

			Log.d("MY APPLICATION", " set lang "  + lang);
			locale = new Locale(lang);
			Locale.setDefault(locale);
			config.locale = locale;
			getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		}
	}

	/**
	 * Used by NotificationAlarmService and the widget to remove the year from a birthday 
	 * in order to be able to compare it to today's date (not containing year information).
	 * This is quite complex as the date format could differ.
	 * @param str date
	 * @return
	 */
	public String removeYearFromBirthday(String date) {
		Log.d("APPLICATION", "input: " + date);

		if  (date.matches("\\d{2}[-/]\\d{2}[-/]\\d{4}")) {
			// date is on format: dd/mm/yyyy or mm/dd/yyyy or dd-mm-yyyy or mm-dd-yyyy 
			date =  date.substring(0, date.length()-5);
		} else if (date.matches("\\d{4}[-/]\\d{2}[-/]\\d{2}")) {
			// date is on format: yyyy/dd/mm or yyyy/mm/dd or yyyy-dd-mm or yyyy-mm-dd 
			date =  date.substring(5, date.length());

		} else if (date.matches("\\d{1}[-/]\\d{2}[-/]\\d{4}")) {
			// date is on format d/mm/yyyy or m/dd/yyyy 
			date =  date.substring(0, date.length()-5);
		} else if (date.matches("\\d{2}[-/]\\d{1}[-/]\\d{4}")) {
			// date is on format dd/m/yyyy or mm/d/yyyy 
			date =  date.substring(0, date.length()-5);
		} else if (date.matches("\\d{2}[.]\\d{1}[.]\\d{4}")) {
			// date is on format dd.m.yyyy or mm.d.yyyy
			date =  date.substring(0, date.length()-4);
		} else if (date.matches("\\d{2}[.]\\d{2}[.]\\d{4}")) {
			// date is on format dd.mm.yyyy or mm.dd.yyyy
			date =  date.substring(0, date.length()-4);
		} else if (date.matches("\\d{1}[.]\\d{2}[.]\\d{4}")) {
			date =  date.substring(0, date.length()-4);
		} else if (date.matches("\\d{1}[.]\\d{1}[.]\\d{4}")) {
			date =  date.substring(0, date.length()-4);
		}
			Log.d("APPLICATION", "output: " + date);
			return date;
		}
	}
