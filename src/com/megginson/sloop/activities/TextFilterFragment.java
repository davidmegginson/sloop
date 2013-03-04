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

public class TextFilterFragment extends Fragment {

	private MainActivity mActivity;
	
	private ViewGroup mLayout;

	private EditText mTextField;

	private Button mCancelButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity)getActivity();
		mLayout = (ViewGroup) inflater.inflate(R.layout.fragment_text_filter,
				container);
		setupTextField();
		setupButton();
		return mLayout;
	}
	
	public void clear(){
		mTextField.setText(null);
	}

	private void setupTextField() {
		mTextField = (EditText) mLayout.findViewById(R.id.field_filter);
		mTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				mActivity.doSetTextFilter(v.getText().toString());
				mActivity.doHideKeyboard();
				return true;
			}
		});
	}

	private void setupButton() {
		mCancelButton = (Button) mLayout.findViewById(R.id.button_filter_clear);
		mCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.doSetTextFilter(null);
				if (mTextField.getText().length() == 0) {
					mLayout.setVisibility(View.GONE);
					mActivity.doHideKeyboard();
				} else {
					mTextField.setText(null);
				}
			}
		});
	}

}
