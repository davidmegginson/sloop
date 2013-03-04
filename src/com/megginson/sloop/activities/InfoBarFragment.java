package com.megginson.sloop.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.megginson.sloop.R;

/**
 * Activity fragment for the browser info bar.
 * 
 * This fragment encapsulates part of the layout for MainActivity, including the
 * infobar (at the bottom) and the cancel button that appears when filtering is
 * active.
 * 
 * @author David Megginson
 */
public class InfoBarFragment extends Fragment {

	private MainActivity mActivity;
	private ViewGroup mLayout;
	private TextView mText;
	private Button mClearButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		mLayout = (ViewGroup) inflater.inflate(R.layout.fragment_info_bar,
				container);
		setupText();
		setupClearButton();
		return mLayout;
	}

	/**
	 * Display the position in the current data collection.
	 * 
	 * @param recordNumber the current record (zero-based).
	 * @param filteredTotal the number of filtered records.
	 * @param unfilteredTotal the total number of records.
	 */
	public void displayRecordCount(int recordNumber, int filteredTotal,
			int unfilteredTotal) {
		if (unfilteredTotal == 0) {
			mText.setText(getString(R.string.msg_no_data));
			mLayout.setBackgroundColor(Color.GRAY);
			mClearButton.setVisibility(View.GONE);
		}
		if (filteredTotal == 0) {
			mText.setText(String.format(getString(R.string.info_records_none),
					unfilteredTotal));
			mLayout.setBackgroundColor(Color.argb(64, 255, 0, 0));
			mClearButton.setVisibility(View.VISIBLE);
		} else if (filteredTotal < unfilteredTotal) {
			mText.setText(String.format(
					getString(R.string.info_records_filtered),
					recordNumber + 1, filteredTotal, unfilteredTotal));
			mLayout.setBackgroundColor(Color.argb(64, 255, 255, 0));
			mClearButton.setVisibility(View.VISIBLE);
		} else {
			mText.setText(String.format(
					getString(R.string.info_records_unfiltered),
					recordNumber + 1, filteredTotal));
			mLayout.setBackgroundColor(Color.argb(64, 0, 255, 0));
			mClearButton.setVisibility(View.GONE);
		}

	}

	/**
	 * Set up the text field containing the current status.
	 */
	private void setupText() {
		mText = (TextView) mLayout.findViewById(R.id.info_bar);
	}

	/**
	 * Set up the button for clearing any filters.
	 * 
	 * The button starts hidden.
	 */
	private void setupClearButton() {
		mClearButton = (Button) mLayout.findViewById(R.id.button_filters_clear);

		// filter clear button is invisible until there's a filter
		mClearButton.setVisibility(View.GONE);

		// clears all filters on click
		mClearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.doClearFilters();
			}
		});
	}

}
