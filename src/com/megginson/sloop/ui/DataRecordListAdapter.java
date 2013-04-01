package com.megginson.sloop.ui;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.megginson.sloop.R;
import com.megginson.sloop.activities.MainActivity;
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
		System.err.println("Got a data record with "
				+ dataRecord.getEntries().size() + " entries");
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
	public View getView(int position, View convertView, final ViewGroup parent) {
		View layout;
		ImageView checkView;
		TextView labelView;
		TextView valueView;

		// Reuse existing components if we can
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			layout = inflater.inflate(R.layout.component_data_entry, null);
		} else {
			layout = convertView;
		}

		// Set the appropriate text for this entry
		DataEntry entry = mDataRecord.getEntries().get(position);

		labelView = (TextView) layout.findViewById(R.id.field_name);
		labelView.setText(entry.getKey());

		final String value = entry.getValue();
		valueView = (TextView) layout.findViewById(R.id.field_value);
		valueView.setText(value);

		// FIXME temporary kludge to make links clickable
		if (Util.isUrl(value)) {
			valueView.setTextColor(Color.BLUE);
			valueView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (value.toLowerCase(Locale.US).endsWith(".csv")) {
						Intent intent = new Intent(parent.getContext(),
								MainActivity.class);
						intent.setAction(Intent.ACTION_MAIN);
						intent.putExtra(MainActivity.PARAM_URL, value);
						parent.getContext().startActivity(intent);
					} else {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(value));
						parent.getContext().startActivity(intent);
					}
				}
			});
		}

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
