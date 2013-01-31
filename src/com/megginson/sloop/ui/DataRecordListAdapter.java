package com.megginson.sloop.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.DataRecord;

/**
 * Adapt a single {@link DataRecord} for display as a list of entries.
 * 
 * The {@link DataRecordFragment} class uses this adapter to display the data
 * record in a {@link ListView}.
 * 
 * Each list entry is a linear layout containing two text views, one for the
 * entry's label, and one for its value.
 * 
 * @author David Megginson
 */
public class DataRecordListAdapter extends BaseAdapter {

	private final static int LABEL_POSITION = 0;

	private final static int VALUE_POSITION = 1;

	private Context mContext;

	private DataRecord mDataRecord;

	public DataRecordListAdapter(Context context, DataRecord dataRecord) {
		mContext = context;
		mDataRecord = dataRecord;
	}

	@Override
	public int getCount() {
		return mDataRecord.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataRecord.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layout;
		TextView labelView;
		TextView valueView;

		// Reuse existing components if we can
		if (convertView != null) {
			layout = (LinearLayout) convertView;
			labelView = (TextView) ((ViewGroup) convertView)
					.getChildAt(LABEL_POSITION);
			valueView = (TextView) ((ViewGroup) convertView)
					.getChildAt(VALUE_POSITION);
		} else {
			layout = makeLayout();
			labelView = makeLabel();
			valueView = makeValue();
			layout.addView(labelView, LABEL_POSITION);
			layout.addView(valueView, VALUE_POSITION);
		}

		// Set the appropriate text for this entry
		DataEntry entry = mDataRecord.get(position);
		labelView.setText(entry.getKey());
		valueView.setText(entry.getValue());

		return layout;
	}

	/**
	 * Construct the layout that holds the entry.
	 * 
	 * @return a fully set-up linear layout.
	 */
	private LinearLayout makeLayout() {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(0, 20, 0, 20);
		return layout;
	}

	/**
	 * Construct the text view that holds the label.
	 * 
	 * @return a fully set-up text view.
	 */
	private TextView makeLabel() {
		TextView textView = new TextView(mContext);
		textView.setTextIsSelectable(true);
		textView.setTextColor(Color.BLUE);
		textView.setTextSize(15);
		return textView;
	}

	/**
	 * Construct the text view that holds the value.
	 * 
	 * @return a fully set-up text view.
	 */
	private TextView makeValue() {
		TextView textView = new TextView(mContext);
		textView.setTextIsSelectable(true);
		textView.setTextSize(20);
		return textView;
	}

}
