package com.hioa.s156960_mappe2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * Sends a birthday greeting sms to friends that have birthday on todays date. 
 * Send a notification when this has been done.
 * This service is used as an pending intent, managed by the SetRepeatService class.
 */
public class NotificationAlarmService extends Service {

	String TAG = "IN NOTIFICATION ALAMR SERVICE";
	MyApplication myApp;
	 // we will append names to this string
	ArrayList<Friend> allFriends;
	String smsText;
	Calendar calendar;
	 StringBuilder nameOfBirthdayFriends;
	String todaysDate;
	int numberOfBirthdayFriends;
	java.text.DateFormat localeDateFormat;
	boolean success;

	public int onStartCommand(Intent intent, int flags, int startID) {
		Context context = getApplicationContext();
		myApp = (MyApplication) getApplication();
		
		calendar = Calendar.getInstance();
		nameOfBirthdayFriends = new StringBuilder();
		numberOfBirthdayFriends = 0;
	
		// get todays date in proper format, according to the phone's settings
		todaysDate = DateUtils.formatDateTime(this, calendar.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE);
		
		sendSmsToFriendsWhoHaveBirthday();

		if (numberOfBirthdayFriends > 0) {
			Log.d(TAG, "Number of birthday buddies: " + numberOfBirthdayFriends);
			sendNotificationToUser();
		}

		return startID;

	}

	/**
	 * Finds friends that have birthday today and invokes a method to send them a birthday greeting 
	 */
	private void sendSmsToFriendsWhoHaveBirthday() {
		allFriends = myApp.friendsDB.getAllFriends();		

		for (Friend f : allFriends) {
			String friendsBirthday = myApp.removeYearFromBirthday(f.getBirthday());
			
			if (friendsBirthday.equals(todaysDate)) {
				Log.d(TAG, "MATCH:" + f.getFirstName() + " on " + f.getBirthday());
				
				numberOfBirthdayFriends++;
				sendSmsToFriend(f);
			}
		}
	}


	/**
	 * Send a birthday greeting sms to the friends that were born on this day.
	 * Appends the name(s) of the person to the stringBuilder, so we can use it later to notify the user
	 * about to whom we sent birthday greetings.
	 * @param friend
	 */
	private void sendSmsToFriend(Friend friend) {
		
		// get the sms message saved in shared preferences
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		smsText = settings.getString("pref_key_sms_greeting", "");

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(friend.getPhoneNumber(), null, smsText, null, null);
			
			nameOfBirthdayFriends.append("\n");
			if (friend.getFirstName() != null && !friend.getFirstName().equals("null")) {
				nameOfBirthdayFriends.append(friend.getFirstName() + " ");
			}

			if (friend.getLastName() != null && !friend.getLastName().equals("null")) {
				nameOfBirthdayFriends.append(friend.getLastName() + " ");
			}

		} catch (Exception e) {
			String error = getString(R.string.sms_error_text) + " " + friend.getPhoneNumber();
			Log.d(TAG, "Sms failed to: " + friend.getFirstName());

			Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
			success = false;
			e.printStackTrace();
		}
	}


	/**
	 * Sends a notification to the users, notifying that a sms was sent to this friend and that friend.
	 */
	private void sendNotificationToUser() {
		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(context, MainActivity.class); // starts activity if notification is selected. IE returns you to the listview;

		PendingIntent contentIntent = PendingIntent.getActivity(context, 1, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		NotificationManager nm = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

		Notification.Builder builder = new Notification.Builder(context);
		builder.setContentIntent(contentIntent)
		.setSmallIcon(R.drawable.ic_launcher)
		.setWhen(System.currentTimeMillis())
		.setAutoCancel(true)
		.setContentTitle(getString(R.string.notification_title)) 
		.setContentText(getString(R.string.notification_text))
		.setStyle(new Notification.BigTextStyle()
		.bigText(getString(R.string.notification_text) + "\n" + nameOfBirthdayFriends.toString()));

		Notification notification = builder.build();
		notification.flags	|=	Notification.FLAG_AUTO_CANCEL;
		nm.notify(0, notification);	
		Log.d(TAG, "Notifcation will be sent to " + nameOfBirthdayFriends.toString());
	}


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
