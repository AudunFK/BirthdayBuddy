package com.hioa.s156960_mappe2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FriendsBroadcastReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, SetRepeatService.class); 
		context.startService(i);

		Log.d("BROADCASTRECEIVER", "Intent was started");
	}
}
