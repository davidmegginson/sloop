package com.megginson.sloop.app;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.megginson.sloop.model.DataCollection;

/**
 * Pager adapter for a data collection.
 */
public class DataCollectionPagerAdapter extends FragmentStatePagerAdapter {
	
	private DataCollection dataCollection;

	public DataCollectionPagerAdapter(FragmentManager fm, DataCollection dataCollection) {
		super(fm);
		this.dataCollection = dataCollection;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new DataRecordFragment(dataCollection.get(position));
		return fragment;
	}

	@Override
	public int getCount() {
		return dataCollection.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Record #" + (position + 1);
	}
	
}

