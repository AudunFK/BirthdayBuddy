package com.hioa.s156960_mappe2;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DatePickerFragment extends DialogFragment  {

	DatePickerDialog.OnDateSetListener onDateSet;
	private boolean isModal = false;

	public static DatePickerFragment newInstance()
	{
		DatePickerFragment frag = new DatePickerFragment();
		frag.isModal = true; // when fragment is called as a dialog, set flag 
		return frag;
	}

	public DatePickerFragment(){}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it

		return new DatePickerDialog(getActivity(), onDateSet, year, month, day);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(isModal) {
			return super.onCreateView(inflater, container, savedInstanceState);
		}
		else {
			View rootView = inflater.inflate(R.layout.add_or_edit_friend, container, false);
			return rootView;
		}
	}

	public void setCallBack(DatePickerDialog.OnDateSetListener onDate) {
		onDateSet = onDate;
	}

}