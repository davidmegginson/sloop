package com.megginson.sloop.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.megginson.sloop.model.DataCollection;

/**
 * Pager adapter for a data collection.
 */
public class DataCollectionPagerAdapter extends FragmentStatePagerAdapter {

	private DataCollection mDataCollection = new DataCollection();

	public DataCollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public DataCollection getDataCollection() {
		return mDataCollection;
	}

	public void setDataCollection(DataCollection dataCollection) {
		mDataCollection = dataCollection;
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		DataRecordFragment fragment = new DataRecordFragment();
		Bundle args = new Bundle();
		args.putParcelable("dataRecord", mDataCollection.get(position));
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return mDataCollection.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Record #" + (position + 1);
	}

}
