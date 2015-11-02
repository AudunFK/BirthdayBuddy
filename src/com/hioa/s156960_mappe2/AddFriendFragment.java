package com.hioa.s156960_mappe2;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Fragment that displays a layout for adding and saving a new friend.
 */
public class AddFriendFragment extends Fragment {
	String TAG = "*** ADDFRIEND FRAGMENT";
	private static final int RESULT_OK = 1;
	
	public AddFriendFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		((FriendDetailActivity) getActivity()).setTitle("Add friend");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_activity_add_friend, menu);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
			
			// Use the local date settings when formatting the date
			String birthday = DateFormat.getDateFormat(getActivity()).format(date);

			TextView textViewDate = (TextView) getActivity().findViewById(R.id.date);
			textViewDate.setText(birthday);
		}
	};


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_contact_fragment:
			Log.d(TAG, "save ble trykket - saveNewFriend kalles");
			((FriendDetailActivity) getActivity()).saveNewFriend();
			return true;
		case R.id.action_exit:
			((FriendDetailActivity) getActivity()).setResult(RESULT_OK, null);
			((FriendDetailActivity) getActivity()).finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
