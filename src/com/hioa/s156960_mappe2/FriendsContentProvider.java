package com.hioa.s156960_mappe2;

import java.sql.SQLException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/** A custom Content Provider to do the database operations */
public class FriendsContentProvider extends ContentProvider {

	public static final String PROVIDER_NAME = "com.hioa.s156960_mappe2.contacts";

	/**
	 * A uri to do operations on contacts table. A content provider is
	 * identified by its uri
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/contacts");

	/** Constants to identify the requested operation */
	private static final int CONTACTS = 1;
	private static final int CONTACT_ID = 2;

	Context context;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "contacts", CONTACTS);
		uriMatcher.addURI(PROVIDER_NAME, "contacts/#", CONTACT_ID);
	}

	/** This content provider does the database operations by this object */
	FriendsDB mContactsDB;

	/**
	 * A callback method which is invoked when the content provider is starting
	 * up
	 */
	@Override
	public boolean onCreate() {
		mContactsDB = new FriendsDB(getContext());
		return true;
	}

	/**
	 * A callback method which is invoked when delete operation is requested on
	 * this content provider
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;

		if (uriMatcher.match(uri) == CONTACT_ID) {
			String contactID = uri.getPathSegments().get(1);
			count = mContactsDB.del(contactID);
		}
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	/**
	 * A callback method which is invoked when insert operation is requested on
	 * this content provider
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = mContactsDB.insert(values);
		Uri _uri = null;

		if (rowID > 0) {
			_uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
		} else {
			try {
				throw new SQLException("Failed to insert : " + uri);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return _uri;

	}

	/** A callback method which is by the default content uri */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		if (uriMatcher.match(uri) == CONTACTS) {
			return mContactsDB.getAllContacts();
		} else {
			String contactID = uri.getPathSegments().get(1);
			return mContactsDB.getContactByID(contactID);
		}
	}

	/**
	 * A callback method which is invoked when update operation is requested on
	 * this content provider
	 */
	@Override
	public int update(Uri uri, ContentValues contentValues, String selection,
			String[] selectionArgs) {
		int count = 0;
		if (uriMatcher.match(uri) == CONTACT_ID) {
			String contactID = uri.getPathSegments().get(1);
			count = mContactsDB.update(contentValues, contactID);
		}
		return count;
	}
}