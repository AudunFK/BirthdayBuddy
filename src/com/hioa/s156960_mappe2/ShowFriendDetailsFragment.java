package com.hioa.s156960_mappe2;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class ShowFriendDetailsFragment extends Fragment {
	String TAG = "*** FriendDetailsFragment FRAGMENT";
	private static final int RESULT_OK = 1;
	Friend friend; // the friend to display information about

	TextView firstNameTextField;
	TextView lastNameTextField;
	TextView phoneNumberField;
	TextView birthdayField;

	public ShowFriendDetailsFragment() {
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "3: FriendDetailsFragment onActivityCreated()");
		super.onCreate(savedInstanceState);

		firstNameTextField = (TextView)  getView().findViewById(R.id.firstName);
		lastNameTextField = (TextView)  getView().findViewById(R.id.lastName);
		phoneNumberField = (TextView)  getView().findViewById(R.id.phoneNumber);
		birthdayField = (TextView)  getView().findViewById(R.id.date);

		friend = ((FriendDetailActivity) getActivity()).getFriend();
		setTextFieldData();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "2: FriendDetailsFragment onCreateView()");
		return inflater.inflate(R.layout.friend_details_fragment, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "1: FriendDetailsFragment ON CREATE()");
		setHasOptionsMenu(true);
		((FriendDetailActivity) getActivity()).setTitle("");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragment_show_friend_details, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit_friend:
			((FriendDetailActivity) getActivity()).changeFragmentToEditFriendFragment();
			return true;
		case R.id.action_exit:
			((FriendDetailActivity) getActivity()).setResult(RESULT_OK, null);
			((FriendDetailActivity) getActivity()).finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setFriendObject(Friend friend) {
		this.friend = friend;		
	}

	public void setTextFieldData() {
		if (friend.getFirstName() != null) {
			firstNameTextField.setText(friend.getFirstName());
		}

		if (friend.getLastName() != null) {
			lastNameTextField.setText(friend.getLastName());
		}

		if (friend.getPhoneNumber() != null) {
			phoneNumberField.setText(friend.getPhoneNumber());
		}

		if (friend.getBirthday() != null) {
			birthdayField.setText(friend.getBirthday());
		}

	}
}
