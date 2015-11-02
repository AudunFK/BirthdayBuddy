package com.hioa.s156960_mappe2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FriendsDB extends SQLiteOpenHelper {

	/** Database name */
	private static String DBNAME = "birthdaybuddy_db";

	/** Version number of the database */
	private static int VERSION = 2;

	/** Field 1 of the table contacts, which is the primary key */
	public static final String KEY_ROW_ID = "_id";

	/** Field 2 of the table contacts, stores the contact name */
	public static final String KEY_NAME = "name";
	
	/** Field 3 of the table contacts, stores the contact's last name */
	public static final String KEY_LASTNAME = "last_name";

	/** Field 4 of the table contacts, stores the phone number of the contact */
	public static final String KEY_PHONE = "phone";
	
	/** Field 5 of the table contacts, stores the contact's date of birth */
	public static final String KEY_BIRTHDAY = "birthday";

	/** A constant, stores the the table name */
	private static final String DATABASE_TABLE = "contacts";

	/** An instance variable for SQLiteDatabase */
	private SQLiteDatabase mDB;

	private String TAG;

	Context context;

	/** Constructor */
	public FriendsDB(Context context) {
		super(context, DBNAME, null, VERSION);
		this.mDB = getWritableDatabase();
		this.context = context;
	}

	/**
	 * This is a callback method, invoked when the method getReadableDatabase()
	 * / getWritableDatabase() is called provided the database does not exists
	 * */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT , "
				+ " name TEXT, last_name TEXT, phone TEXT NOT NULL, birthday TEXT) ";
		db.execSQL(sql);
	}

	/** Inserts a new contact to the table contacts */
	public long insert(ContentValues contentValues) {
		long rowID = mDB.insert(DATABASE_TABLE, null, contentValues);
		return rowID;
	}

	/** Updates a contact */
	public int update(ContentValues contentValues, String contactID) {
		int cnt = mDB.update(DATABASE_TABLE, contentValues, "_id=" + contactID,
				null);
		return cnt;
	}

	/** Deletes a contact from the table */
	public int del(String contactID) {
		int count = mDB.delete(DATABASE_TABLE, "_id=" + contactID, null);
		return count;
	}

	/** Returns a contact by passing its id */
	public Cursor getContactByID(String contactID) {
		return mDB.query(DATABASE_TABLE, new String[] { KEY_ROW_ID, KEY_NAME, KEY_LASTNAME, KEY_PHONE, KEY_BIRTHDAY }, "_ID=" + contactID, null, null, null, KEY_NAME + " asc ");
	}

	/** Returns all the contacts in the table */
	public Cursor getAllContacts() {
		return mDB.query(DATABASE_TABLE, new String[] { KEY_ROW_ID, KEY_NAME, KEY_LASTNAME, KEY_PHONE, KEY_BIRTHDAY }, null, null, null, null, KEY_NAME + " asc ");
	}
	
	/**
	 * Returns a friend object
	 * @param friendId the id of the friend 
	 * @return
	 */
	public Friend getFriendObjectById(long friendId) {
		Friend friend = new Friend(context);
		String id = friendId + "";

		Cursor cursor = getContactByID(id);

		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				friend.setmContactId(cursor.getInt(cursor.getColumnIndex("_id")));
				friend.setFirstName(cursor.getString(cursor.getColumnIndex("name")));
				friend.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
				friend.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone")));
				friend.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));

				cursor.moveToNext();
			}
		}
		cursor.close();
		return friend;
	}


	/**
	 * Returns an ArrayList of all friends in the DB THIS SHOULD PROBABLY BE
	 * DONE THROUGH THE PROVIDER, NOT HERE.
	 * 
	 * */
	public ArrayList<Friend> getAllFriends() {
		ArrayList<Friend> friendList = new ArrayList<Friend>(); 

		String selectAllQuery = "SELECT * FROM contacts";
		Cursor cursor = mDB.rawQuery(selectAllQuery, null);

		if (cursor != null && cursor.moveToFirst()) {
			while (cursor.isAfterLast() == false) {
				Friend friend = new Friend(context);

				friend.setmContactId(cursor.getInt(cursor.getColumnIndex("_id")));
				friend.setFirstName(cursor.getString(cursor.getColumnIndex("name")));
				friend.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
				friend.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phone")));
				friend.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));

				friendList.add(friend);
				cursor.moveToNext();
			}
		}
		cursor.close();

		// Sorts the list alphabetically by first name - we could of course make if statements
		// and give the user other sort options 
		Collections.sort(friendList, new CustomComparator());
		return friendList;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE); 
		Log.d(TAG,"updated from version " + oldVersion + " to version " + newVersion);
		onCreate(db);
	}


	/**
	 * Sorts our arrayList of Friends alphabetically by first name
	 */
	private class CustomComparator implements Comparator<Friend> {
		@Override
		public int compare(Friend f1, Friend f2) {
			String f1Name = f1.getFirstName();
			String f2Name = f2.getFirstName();
			return f1Name.compareTo(f2Name);
		}
	}
}