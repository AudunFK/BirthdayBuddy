package com.hioa.s156960_mappe2;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Thanks to this CustomArrayAdapter our listView can use an arrayList filled with Friend objects! 
 * @author martineeklund
 *
 */
public class CustomArrayAdapter extends ArrayAdapter<Friend> {
	private final LayoutInflater mInflater;

	public CustomArrayAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(List<Friend> data) {
		clear();
		if (data != null) {
			for (Friend appEntry : data) {
				add(appEntry);
			}
		}
	}

	/**
	 * Populate new items in the list. 
	 * Choose which variables to display in the textViews of our listView. 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.single_list_item, parent, false);
		} else {
			view = convertView;
		}

		Friend friend = getItem(position);
		((TextView) view.findViewById(R.id.listview_item_friend)).setText(friend.getFirstName());
		
		return view;
	}
}