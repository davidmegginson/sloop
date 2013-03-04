package com.megginson.sloop.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.megginson.sloop.R;

/**
 * Activity fragment for the main browser display area.
 * 
 * This fragment encapsulates part of the layout for MainActivity, including the
 * main display area for data records.
 * 
 * @author David Megginson
 */
public class MainDisplayFragment extends Fragment {

	private MainActivity mActivity;
	private ViewGroup mLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		mLayout = (ViewGroup) inflater.inflate(R.layout.fragment_main_display,
				container);
		return mLayout;
	}

}
