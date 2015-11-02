package com.hioa.s156960_mappe2;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Our plain, old Java object representation of a friend.
 */
public class Friend {
	String TAG = "CLASS: FRIEND";
	private long mContactId = 0;
	private String phoneNumber;
	private String birthday;
	private String firstName;
	private String lastName;
	StringBuilder errorMessages = new StringBuilder();
	Context context;
	ContentValues contentValues;


	Friend(Context c) {
		errorMessages = new StringBuilder();
		this.context = c;
	}

	public long getmContactId() {
		return mContactId;
	}

	public void setmContactId(long id) {
		this.mContactId = id;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Invokes the content provider's add operation,
	 * which adds the friend object data to the DB. 
	 */
	public void addFriendToDB() {
		contentValues = new ContentValues();
		putAllFriendVariablesThatArentNullIntoContentValues();
		Uri uri = context.getContentResolver().insert(FriendsContentProvider.CONTENT_URI, contentValues);

		// update the friend object with the id from the DB, in case the user wants to edit the same friend right away
		this.setmContactId(Long.parseLong(uri.getLastPathSegment())); 
		Log.d(TAG, "FRIEND CONTACT ID I ADD: " + this.getmContactId() + "");
	}

	/**
	 * Invokes the content provider's update operation,
	 * which updates the friend object data to the DB. 
	 */
	public void updateFriendDataInDB() {
		contentValues = new ContentValues();
		putAllFriendVariablesThatArentNullIntoContentValues();

		Uri uri = FriendsContentProvider.CONTENT_URI;
		String pathSegment = this.getmContactId() + "";
		uri = Uri.withAppendedPath(uri, pathSegment);
		context.getContentResolver().update(uri,contentValues, null, null);
	}
	
	public void deleteFriendFromDB() {
		Uri uri = FriendsContentProvider.CONTENT_URI;
		String pathSegment = this.getmContactId() + "";
		uri = Uri.withAppendedPath(uri, pathSegment);
		context.getContentResolver().delete(uri, null, null);
	}

	/**
	 * Helper method for addFriendToDB and updateFriendDataInDB.
	 * Puts all variables that are not null into the contentValues that
	 * we later will pass to the contentResolver.
	 */
	private void putAllFriendVariablesThatArentNullIntoContentValues() {
		if (this.getFirstName() != null) {
			contentValues.put(FriendsDB.KEY_NAME, firstName);
		}

		if (this.getLastName() != null) {
			 contentValues.put(FriendsDB.KEY_LASTNAME, lastName);
		}

		if (this.getPhoneNumber() != null) {
			contentValues.put(FriendsDB.KEY_PHONE, phoneNumber);
		}

		if (this.getBirthday() != null) {
			 contentValues.put(FriendsDB.KEY_BIRTHDAY, birthday);
		}
	}

	/**
	 * Invoked when you try to add a new friend to the database.
	 * Invokes in turn the other validation methods, adding an error
	 * message to a String builder for every validation test that fails. 
	 * @param firstName
	 * @param lastName
	 * @param phoneNumber
	 * @param dateOfBirth
	 * @return
	 */
	public String getErrorMessagesFromInputValidationTests(String firstName,
			String lastName, String phoneNumber, String dateOfBirth) {

		if (firstName.equals("") && lastName.equals("")) {
			errorMessages.append(context.getString(R.string.enter_first_or_last_name) + "\n"+ "\n");
		}

		if (!firstName.equals("")) {
			validateFirstName(firstName);
		}

		if (!lastName.equals("")) {
			validateLastName(lastName);
		}

		if (!phoneNumber.equals("")) {
			validatePhoneNumer(phoneNumber);
		} else {
			errorMessages.append(context.getString(R.string.enter_phone_number) + "\n");
		}

		// Sets birthday directly as the date picker takes care of the validation
		this.setBirthday(dateOfBirth);
		
		return errorMessages.toString();
	}

	/**
	 * Validation test for names. If name contains any other character than
	 * letters, spaces or -, the test fails and an error message is added to the
	 * list of error messages. If validation passes, the data is passed to the
	 * appropriate set method.
	 * 
	 * @param name
	 * @return true if name is ok
	 */
	public boolean validateFirstName(String name) {
		if (!name.matches("[a-zæøåA-ZÆØÅ]+([ -][a-zæøåA-ZÆØÅ]+)*")) {
			errorMessages.append(context.getString(R.string.validate_first_name) + "\n"+ "\n");
			return false;
		}
		setFirstName(name);
		return true;
	}

	/**
	 * Validation test for names. If name contains any other character than
	 * letters, spaces or -, the test fails and an error message is added to the
	 * list of error messages. If validation passes, the data is passed to the
	 * appropriate set method.
	 * 
	 * @param name
	 * @return true if name is ok
	 */
	private boolean validateLastName(String name) {
		if (!name.matches("[a-zæøåA-ZÆØÅ ]+([ -][a-zæøåA-ZÆØÅ]+)*")) {
			errorMessages.append(context.getString(R.string.validate_last_name) + "\n"+ "\n");
			return false;
		}
		setLastName(name);
		return true;

	}

	/**
	 * Validation test for names. If input contains any other character than
	 * digits or +, the test fails and an error message is added to the list of
	 * error messages. As the length of phone numbers differs, the validation
	 * method is flexible. If validation passes, the data is passed to the
	 * appropriate set method.
	 * 
	 * @param phoneNumber
	 * @return true if phone number is ok
	 */
	private boolean validatePhoneNumer(String phoneNumber) {
		if (!phoneNumber.matches("[+0-9]+")) {
			errorMessages.append(context.getString(R.string.validate_phone_number) + "\n"+ "\n");
			return false;
		}
		setPhoneNumber(phoneNumber);
		return true;
	}
}
