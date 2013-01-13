package com.megginson.sloop.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class DataRecordFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public DataRecordFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);
		for (int x = 1; x <= 40; x++) {
			addText(layout, "Section " + getArguments().getInt(ARG_SECTION_NUMBER) + ", Line #" + x);
		}
		
		ScrollView scrollView = new ScrollView(getActivity());
		scrollView.addView(layout);
		
		return scrollView;
	}
	
	private TextView addText (ViewGroup container, String text) {
		TextView textView = new TextView(getActivity());
		textView.setText(text);
		container.addView(textView);
		return textView;
	}
}

