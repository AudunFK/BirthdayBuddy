package com.hioa.s156960_mappe2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class manages the data and fragments related to adding new 
 * contacts and displaying and editing information about them.
 *
 */
public class FriendDetailActivity extends MainActivity {
	private static final int RESULT_OK = 1;

	MyApplication myApp;	
	String TAG = "FriendDetailActivity";

	// The fragments used by this activity
	AddFriendFragment addFriendFragment;
	EditFriendFragment editFriendFragment;
	ShowFriendDetailsFragment friendDetailsFragment;
	
	// Friend variables
	Friend friend;
	String firstNameText;
	String lastNameText;
	String phoneNumberText;
	String dateOfBirthText; // should be replaced by datepicker

	EditText firstNameTextField;
	EditText lastNameTextField;
	EditText phoneNumberField;
	TextView birthdayField;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_layout_fragment_container);
		myApp = (MyApplication) getApplication();
		friend = new Friend(this);

		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			// Get the message from the intent
			Intent intent = getIntent();
			String intentStartedFrom = intent.getStringExtra(MainActivity.EXTRA_ORIGIN_MESSAGE);

			// Show different fragments based on from where the intent was started
			if (intentStartedFrom.equals("showDetailsAboutClickedFriend")) {
				long idOfClickedFriend = intent.getLongExtra(MainActivity.EXTRA_FRIEND_ID, 0);
				friend = myApp.friendsDB.getFriendObjectById(idOfClickedFriend);
				changeFragmentToFriendDetailFragment();

			} else if (intentStartedFrom.equals("addFriendButton")) {
				changeFragmentToAddFriendFragment();
			}
		}
	}

	/**
	 * Helps passing the friend object from this activity to the fragments
	 * @return
	 */
	public Friend getFriend() {
		return friend;
	}

	/**
	 * Replaces the current fragment with the AddFriendFragment
	 */
	public void changeFragmentToAddFriendFragment() {
		addFriendFragment = new AddFriendFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, addFriendFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * Invoked when the save button is clicked in the addFriendFragment
	 */
	public void saveNewFriend() {
		if (allValidationTestsPassed()) {
			friend.addFriendToDB();
			changeFragmentToFriendDetailFragment();
		}
	}

	/**
	 * Invoked when you click the save icon in EditFriendFragment
	 * The method invokes content provider's update operation
	 */
	public void updateFriend() {
		if (allValidationTestsPassed()) {
			friend.updateFriendDataInDB();
			changeFragmentToFriendDetailFragment();
		}
	}

	/**
	 * Invoked when a new friend has been successfully added to the DB. 
	 * Replaces the AddFriendFragment in order to display the new friend.
	 */
	private void changeFragmentToFriendDetailFragment() {
		friendDetailsFragment = new ShowFriendDetailsFragment();
		friendDetailsFragment.setFriendObject(friend);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, friendDetailsFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * Invoked when the edit button is clicked in the ShowFriendDetailsFragment.
	 * Replaces the current fragment with the EditFriendFragment.
	 */
	public void changeFragmentToEditFriendFragment() {
		editFriendFragment = new EditFriendFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, editFriendFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	/**
	 * Gets the information from the fragment's EditText fields. Invokes the
	 * input validation methods of the Friend object. Returns false if there are
	 * any error messages.
	 */
	public boolean allValidationTestsPassed() {

		firstNameTextField = (EditText) findViewById(R.id.firstName);
		firstNameText = firstNameTextField.getText().toString();

		lastNameTextField = (EditText) findViewById(R.id.lastName);
		lastNameText = lastNameTextField.getText().toString();

		phoneNumberField = (EditText) findViewById(R.id.phoneNumber);
		phoneNumberText = phoneNumberField.getText().toString();

		birthdayField = (TextView) findViewById(R.id.date);
		dateOfBirthText = birthdayField.getText().toString();

		Log.d(TAG, "DATE OF BIRTH: " + dateOfBirthText);
		
		// We need this, as we're using this method for validation when friends are edited
		// If we didnt test for null, we would lose our friend object when editing friends right after adding them (and hence the mContactId)
		if (friend == null) {
			friend = new Friend(this);
		}

		String errorMessagesFromInputValidationTests = friend
				.getErrorMessagesFromInputValidationTests(firstNameText, lastNameText,
						phoneNumberText, dateOfBirthText);

		if (errorMessagesFromInputValidationTests.length() > 0) {
			Log.d(TAG, "SOME OF THE INPUT TESTS FAILED.");

			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(R.string.invalid_input_title);
			alertDialog.setMessage(errorMessagesFromInputValidationTests.toString());
			
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			alertDialog.show();
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		ImageView view = (ImageView) findViewById(android.R.id.home);
		view.setPadding(0, 0, 20, 0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}


	public void deleteFriend() {
		friend.deleteFriendFromDB();
		// finish this activity and return back to main (the listView)
		NavUtils.navigateUpFromSameTask(this);
		finish();	
	}
}