package com.megginson.sloop.activities;

import com.megginson.sloop.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity fragment for the text search filter.
 * 
 * This fragment encapsulates part of the layout for MainActivity, including the
 * search field at the top (which starts hidden).
 * 
 * @author David Megginson
 */
public class TextFilterFragment extends Fragment {

	private MainActivity mActivity;
	private ViewGroup mLayout;
	private EditText mTextField;
	private Button mCancelButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		mLayout = (ViewGroup) inflater.inflate(R.layout.fragment_text_filter,
				container);
		setupTextField();
		setupCancelButton();
		setShown(false);
		return mLayout;
	}

	/**
	 * Clear the text in the field.
	 */
	public void clear() {
		mTextField.setText(null);
	}

	/**
	 * Set the visibility of the text filter.
	 * 
	 * @param isShown
	 *            true to show the filter; false otherwise.
	 */
	public void setShown(boolean isShown) {
		if (isShown) {
			mLayout.setVisibility(View.VISIBLE);
		} else {
			mLayout.setVisibility(View.GONE);
		}
	}

	/**
	 * Indicate whether the text filter is visible.
	 * 
	 * @return true if the filter is visible.
	 */
	public boolean isShown() {
		return (mLayout.getVisibility() == View.VISIBLE);
	}

	/**
	 * Set up the edit field for the filter.
	 *
	 */
	private void setupTextField() {
		mTextField = (EditText) mLayout.findViewById(R.id.field_filter);
		
		// the enter button submits the search text
		mTextField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						mActivity.doSetTextFilter(v.getText().toString());
						ActivitiesUtil.doHideKeyboard(mActivity, mTextField);
						return true;
					}
				});
	}

	/**
	 * Set up the cancel button in the text field.
	 */
	private void setupCancelButton() {
		mCancelButton = (Button) mLayout.findViewById(R.id.button_filter_clear);
		
		// first click clears; second click closes
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// clear the filter either way
				mActivity.doSetTextFilter(null);
				if (mTextField.getText().length() == 0) {
					setShown(false);
				} else {
					clear();
				}
			}
		});
	}

}
