package com.megginson.sloop.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.megginson.sloop.R;
import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.DataRecord;
import com.megginson.sloop.model.Util;

/**
 * Adapt a single {@link DataRecordImpl} for display as a list of entries.
 * 
 * The {@link DataRecordFragment} class uses this adapter to display the data
 * record in a {@link ListView}.
 * 
 * @author David Megginson
 */
public class DataRecordListAdapter extends BaseAdapter {

	private Context mContext;

	private DataRecord mDataRecord;

	public DataRecordListAdapter(Context context, DataRecord dataRecord) {
		mContext = context;
		mDataRecord = dataRecord;
		System.err.println("Got a data record with " + dataRecord.getEntries().size() + " entries");
	}

	@Override
	public int getCount() {
		return mDataRecord.getEntries().size();
	}

	@Override
	public Object getItem(int position) {
		return mDataRecord.getEntries().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout;
		ImageView checkView;
		TextView labelView;
		TextView valueView;

		// Reuse existing components if we can
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			layout = inflater.inflate(
					R.layout.component_data_entry, null);
		} else {
			layout = convertView;
		}

		// Set the appropriate text for this entry
		DataEntry entry = mDataRecord.getEntries().get(position);

		labelView = (TextView) layout.findViewById(R.id.field_name);
		labelView.setText(entry.getKey());

		valueView = (TextView) layout.findViewById(R.id.field_value);
		valueView.setText(entry.getValue());

		checkView = (ImageView) layout.findViewById(R.id.image_checkbox);

		if (entry.hasFilter()) {
			checkView.setVisibility(View.VISIBLE);
		} else {
			checkView.setVisibility(View.INVISIBLE);			
		}
		
		if (position % 2 == 1) {
			layout.setBackgroundColor(Color.rgb(0xee, 0xee, 0xee));
		} else {
			layout.setBackgroundColor(Color.TRANSPARENT);
		}

		return layout;
	}

}
