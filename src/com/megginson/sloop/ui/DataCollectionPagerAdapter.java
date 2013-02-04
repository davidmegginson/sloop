package com.megginson.sloop.ui;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.model.DataRecord;
import com.megginson.sloop.model.FilteredList;
import com.megginson.sloop.model.ListItemFilter;

/**
 * Pager adapter for a data collection.
 * 
 * The adapter can optionally take a {@link ListItemFilter} that will control
 * which items are visible.
 */
public class DataCollectionPagerAdapter extends FragmentStatePagerAdapter {

	private DataCollection mDataCollection;
	private ListItemFilter<DataRecord> mFilter;
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
	public ListItemFilter<DataRecord> getFilter() {
		return mFilter;
	}

	/**
	 * Set the filter to apply to the data collection.
	 * 
	 * @param filter
	 *            the filter to apply, or null to leave the list unfiltered.
	 */
	public void setFilter(ListItemFilter<DataRecord> filter) {
		mFilter = filter;
		updateFilter();
		notifyDataSetChanged();
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
	private void updateFilter() {
		mFilteredList = null;
		if (mDataCollection != null && mFilter != null) {
			mFilteredList = new FilteredList<DataRecord>(mFilter,
					mDataCollection);
			System.err.println("Filter updated");
		}
	}

}
