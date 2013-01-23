package com.megginson.sloop.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.DataRecord;

/**
 * A view fragment wrapping a data record
 * 
 * @author David Megginson
 * @see DataRecord
 * @see DataCollectionPagerAdapter
 */
public class DataRecordFragment extends Fragment {
	
	private DataRecord mDataRecord = null;

	public DataRecordFragment() {
		super();
	}
	
	public void setDataRecord (DataRecord dataRecord) {
		mDataRecord = dataRecord;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);
		
		for (DataEntry entry : mDataRecord) {
			addLabel(layout, entry.getKey());
			addValue(layout, entry.getValue());	
		}
		
		ScrollView scrollView = new ScrollView(getActivity());
		scrollView.addView(layout);
		
		return scrollView;
	}
	
	private TextView addLabel (ViewGroup container, String text) {
		TextView textView = new TextView(getActivity());
		textView.setText(text);
		textView.setTextIsSelectable(true);
		textView.setTypeface(null, Typeface.BOLD);
		container.addView(textView);
		return textView;
	}

	private TextView addValue (ViewGroup container, String text) {
		TextView textView = new TextView(getActivity());
		textView.setText(text);
		textView.setTextIsSelectable(true);
		container.addView(textView);
		return textView;
	}
}

