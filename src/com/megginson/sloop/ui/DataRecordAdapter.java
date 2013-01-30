package com.megginson.sloop.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.DataRecord;

public class DataRecordAdapter extends BaseAdapter {

	private Context mContext;

	private DataRecord mDataRecord;

	public DataRecordAdapter(Context context, DataRecord dataRecord) {
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
		DataEntry entry = mDataRecord.get(position);

		// TODO reuse convertView
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);

		addLabel(layout, entry.getKey());
		addValue(layout, entry.getValue());

		return layout;
	}

	private TextView addLabel(ViewGroup container, String text) {
		TextView textView = new TextView(mContext);
		textView.setText(text);
		textView.setTextIsSelectable(true);
		textView.setTypeface(null, Typeface.BOLD);
		container.addView(textView);
		return textView;
	}

	private TextView addValue(ViewGroup container, String text) {
		TextView textView = new TextView(mContext);
		textView.setText(text);
		textView.setTextIsSelectable(true);
		container.addView(textView);
		return textView;
	}

}
