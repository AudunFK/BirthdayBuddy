package com.hioa.s156960_mappe2;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Loads data asynchronously for our listView
 */
public class FriendListLoader extends AsyncTaskLoader<List<Friend>> {
	String TAG = "IN DATA_LIST_LOADER";
	List<Friend> allFriends;
	MyApplication myApp;
	FriendsDB friendsDB;

	public FriendListLoader(Context context) {
		super(context);
	}

	/**
	 * Does the heavy task of getting data from our FriendDB
	 * As the arrayList 
	 */
	@Override
	public List<Friend> loadInBackground() {
		Log.d(TAG, "DataListLoader.loadInBackground");
		FriendsDB friendsDB = new FriendsDB(getContext());
		allFriends = friendsDB.getAllFriends();
		return allFriends;
	}

	/**
	 * Called when there is new data to deliver to the client. The super class
	 * will take care of delivering it; the implementation here just adds a
	 * little more logic.
	 */
	@Override
	public void deliverResult(List<Friend> listOfData) {
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (listOfData != null) {
				onReleaseResources(listOfData);
			}
		}
		List<Friend> oldApps = listOfData;
		allFriends = listOfData;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(listOfData);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override
	protected void onStartLoading() {
		if (allFriends != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(allFriends);
		}

		if (takeContentChanged() || allFriends == null) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}

	/**
	 * Handles a request to stop the Loader.
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override
	public void onCanceled(List<Friend> apps) {
		super.onCanceled(apps);

		// At this point we can release the resources associated with 'apps'
		// if needed.
		onReleaseResources(apps);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with 'apps'
		// if needed.
		if (allFriends != null) {
			onReleaseResources(allFriends);
			allFriends = null;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated with an
	 * actively loaded data set.
	 */
	protected void onReleaseResources(List<Friend> apps) {
	}

}
