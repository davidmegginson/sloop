package com.megginson.sloop.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.megginson.sloop.model.DataCollection;

/**
 * Pager adapter for a data collection.
 * 
 * The page includes a {@link DataRecordFilter} that can limit the number of
 * terms visible. The {@link #getCount()} method returns the number of items
 * that satisfy the filter, while the {@link #getUnfilteredCount()} method
 * returns the total size of the collection.
 * 
 * @author David Megginson
 */
public class DataCollectionPagerAdapter extends FragmentStatePagerAdapter {

	private DataCollection mDataCollection;

	public DataCollectionPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * Get the data collection to be displayed.
	 * 
	 * @return the data collection, or null if application has not yet set one.
	 */
	public DataCollection getDataCollection() {
		return mDataCollection;
	}

	/**
	 * Set the underlying data collection.
	 * 
	 * @param dataCollection
	 *            the data collection (should not be null).
	 */
	public void setDataCollection(DataCollection dataCollection) {
		if (dataCollection != null) {
			mDataCollection = dataCollection;
		}
		notifyDataSetChanged();
	}

	@Override
	public Fragment getItem(int position) {
		DataRecordFragment fragment = new DataRecordFragment();
		Bundle args = new Bundle();
		args.putParcelable("dataRecord", mDataCollection.getFilteredRecords().get(position));
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		if (mDataCollection == null) {
			return 0;
		} else {
			return mDataCollection.getFilteredRecords().size();
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Record #" + (position + 1);
	}

	@Override
	public int getItemPosition(Object object) {
		return mDataCollection.getFilteredRecords().indexOf(object);
	}

}
