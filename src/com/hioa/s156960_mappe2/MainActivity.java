package com.hioa.s156960_mappe2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


public class MainActivity extends FragmentActivity {
	public final static String EXTRA_ORIGIN_MESSAGE = "com.hioa.s156960_mappe2.INTENT_ORIGIN";
	public final static String EXTRA_FRIEND_ID = "com.hioa.s156960_mappe2.FRIEND_ID";
	private static final int REQUEST_EXIT = 0;
	String TAG = "MAIN";

	MyApplication myApp;
	FriendListFragment friendListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_layout_fragment_container);

		myApp = (MyApplication) getApplication();

		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			friendListFragment = new FriendListFragment();
			friendListFragment.setArguments(getIntent().getExtras());

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.fragment_container, friendListFragment);
			transaction.addToBackStack(null);
			transaction.commit();			
		}
	}

	/**
	 * Invoked when you click a friend in the list view
	 * @param friendId
	 */
	public void showDetailsAboutClickedFriend(long friendId) {
		Intent intent = new Intent(this, FriendDetailActivity.class);
		String intentStartedFrom = "showDetailsAboutClickedFriend";
		intent.putExtra(EXTRA_ORIGIN_MESSAGE, intentStartedFrom);
		intent.putExtra(EXTRA_FRIEND_ID, friendId);

		startActivityForResult(intent, REQUEST_EXIT);
	}


	/** Invoked when you "long click" an element in the listView
	 * @param getmContactId
	 */
	public void requestDeletionOfClickedFriend(final Friend clickedFriend) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(R.string.dialog_delete_friend_title);
		dialog.setMessage(getString(R.string.dialog_delete_friend_text));

		dialog.setPositiveButton(R.string.dialog_delete_button,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {	
				
				clickedFriend.deleteFriendFromDB();
				restartActivity();


			}
		});

		dialog.setNegativeButton(R.string.dialog_cancel_button,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				return;
			}
		});
		dialog.show();

	}


	private void restartActivity() {
		finish();
		startActivity(getIntent());
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_EXIT) {
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ImageView view = (ImageView) findViewById(android.R.id.home);
		view.setPadding(0, 0, 20, 0);
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.add_friend:
			Intent intent = new Intent(this, FriendDetailActivity.class);
			String intentStartedFrom = "addFriendButton";
			intent.putExtra(EXTRA_ORIGIN_MESSAGE, intentStartedFrom);
			startActivityForResult(intent, REQUEST_EXIT);
			return true;
		case R.id.action_sms_settings:
			Intent preferencesIntent = new Intent(this, SettingsActivity.class);
			this.startActivity(preferencesIntent);
			return true;
		case R.id.action_exit: 
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}












