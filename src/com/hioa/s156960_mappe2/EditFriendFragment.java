package com.hioa.s156960_mappe2;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Fragment that displays a layout for editing an existing friend.
 */
public class EditFriendFragment extends Fragment {
	private static final int RESULT_OK = 1;
	String TAG = "*** EDITFRIEND FRAGMENT";
	Friend friend;

	EditText firstNameTextField;
	EditText lastNameTextField;
	EditText phoneNumberField;
	TextView birthdayField;

	public EditFriendFragment() {
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		((FriendDetailActivity) getActivity()).setTitle("Edit friend");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragment_edit_friend, menu);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		firstNameTextField = (EditText)  getView().findViewById(R.id.firstName);
		lastNameTextField = (EditText)  getView().findViewById(R.id.lastName);
		phoneNumberField = (EditText)  getView().findViewById(R.id.phoneNumber);
		birthdayField = (TextView)  getView().findViewById(R.id.date);

		friend = ((FriendDetailActivity) getActivity()).getFriend();
		setTextFieldData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.add_or_edit_friend, container, false);

		Button button = (Button) rootView.findViewById(R.id.setDateButton);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerFragment dpf = new DatePickerFragment().newInstance();
				dpf.setCallBack(onDate);
				dpf.show(getFragmentManager().beginTransaction(), "DatePickerFragment");
			}
		});

		return rootView;
	}


	DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			
			calendar.set(Calendar.DATE, dayOfMonth);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.YEAR, year);
			
			Date date = calendar.getTime();
			
			String birthday = DateFormat.getDateFormat(getActivity()).format(date);			


			birthdayField = (TextView) getActivity().findViewById(R.id.date);
			birthdayField.setText(birthday);
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_contact_fragment:
			Log.d(TAG, "Save ble trykket");
			((FriendDetailActivity) getActivity()).updateFriend();
			return true;
		case R.id.action_delete_contact:
			((FriendDetailActivity) getActivity()).deleteFriend();
			return true;
		case R.id.action_exit:
			((FriendDetailActivity) getActivity()).setResult(RESULT_OK, null);
			((FriendDetailActivity) getActivity()).finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
