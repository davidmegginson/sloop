package com.megginson.sloop.ui;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.model.DataRecord;
import com.megginson.sloop.model.DataRecordFilter;
import com.megginson.sloop.util.FilteredList;

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
	private DataRecordFilter mFilter = new DataRecordFilter();
	private FilteredList<DataRecord> mFilteredList;

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
		updateFilter();
		notifyDataSetChanged();
	}

	/**
	 * Get the filter currently applied to the data collection.
	 * 
	 * @return the filter, or null if the collection is unfiltered.
	 */
	public DataRecordFilter getFilter() {
		return mFilter;
	}

	@Override
	public Fragment getItem(int position) {
		DataRecordFragment fragment = new DataRecordFragment();
		Bundle args = new Bundle();
		// getList() will choose the filtered list if necessary
		args.putParcelable("dataRecord", getList().get(position));
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		// getList() will choose the filtered list if necessary
		return getList().size();
	}

	public int getUnfilteredCount() {
		if (mDataCollection != null) {
			return mDataCollection.size();
		} else {
			return 0;
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "Record #" + (position + 1);
	}

	@Override
	public int getItemPosition(Object object) {
		// force a reset after notifyChanged
		return POSITION_NONE;
	}

	/**
	 * Select the filtered or unfiltered list, as appropriate.
	 * 
	 * @return the list to use (or a default empty list, if neither exists).
	 */
	private List<DataRecord> getList() {
		if (mFilteredList != null) {
			return mFilteredList;
		} else if (mDataCollection != null) {
			return mDataCollection;
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Update the filtered list.
	 */
	public void updateFilter() {
		mFilteredList = null;
		if (mDataCollection != null && mFilter != null
				&& mFilter.getFilters().size() > 0) {
			mFilteredList = new FilteredList<DataRecord>(mFilter,
					mDataCollection);
		}
	}

}
