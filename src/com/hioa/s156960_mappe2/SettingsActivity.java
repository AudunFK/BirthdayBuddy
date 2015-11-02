package com.hioa.s156960_mappe2;

import java.util.Locale;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	public static final String KEY_PREF_SMS_GREETING = "pref_key_sms_greeting";
	public static final String KEY_PREF_LANGUAGE = "pref_key_language";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
		.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
		.unregisterOnSharedPreferenceChangeListener(this);
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		if (key.equals(KEY_PREF_SMS_GREETING )) {
			Preference connectionPref = findPreference(key);
			// Set summary to be the user-description for the selected value
			connectionPref.setSummary(sharedPreferences.getString(key, ""));
		}

		if (key.equals(KEY_PREF_LANGUAGE)) {

			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			String language = settings.getString(KEY_PREF_LANGUAGE, " ");

			((MyApplication) getApplication()).setLocale(language);
			restartActivity();
		}

	}

	private void restartActivity() {

		Intent intent = getIntent();
		startActivity(intent);
		finish();
	}
}