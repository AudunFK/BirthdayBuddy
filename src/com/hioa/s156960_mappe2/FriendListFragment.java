package com.hioa.s156960_mappe2;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Fragment to display the list of friends.
 */
public class FriendListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Friend>> {

	private CustomArrayAdapter mAdapter;
	Friend clickedFriend;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Initially there is no data
		setEmptyText(getString(R.string.no_friends));

		// Create an empty adapter we will use to display the loaded data.
		mAdapter = new CustomArrayAdapter(getActivity());
		
		setListAdapter(mAdapter);
		setListShown(false);
		getListView().setFastScrollEnabled(true);
		getLoaderManager().initLoader(0, null, this);


		OnItemLongClickListener listener = new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {

				clickedFriend = (Friend) getListAdapter().getItem(position);
				((MainActivity) getActivity()).requestDeletionOfClickedFriend(clickedFriend);


				return true;
			}
		};
		getListView().setOnItemLongClickListener(listener);
		

	}


	/**
	 * Invoked when you click an item in the listView. Fetches the friend object
	 * based on the item's position and passes the friend's mContactId to the MainActivity.
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Friend clickedFriend = (Friend) getListAdapter().getItem(position);
		((MainActivity) getActivity()).showDetailsAboutClickedFriend(clickedFriend.getmContactId());
	}


	@Override
	public Loader<List<Friend>> onCreateLoader(int arg0, Bundle arg1) {
		System.out.println("DataListFragment.onCreateLoader");
		return new FriendListLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Friend>> arg0, List<Friend> data) {
		mAdapter.setData(data);
		System.out.println("DataListFragment.onLoadFinished");
		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Friend>> arg0) {
		mAdapter.setData(null);
	}
}