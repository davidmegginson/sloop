package com.megginson.sloop.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Filtering wrapper around a {@link DataCollection}.
 * 
 * @author David Megginson
 */
public class FilteredDataCollection extends AbstractList<DataRecord> {
	
	private DataCollection mDataCollection;
	
	private DataRecordFilter mFilter;
	
	private List<Integer> mMatches;
	
	public FilteredDataCollection(DataCollection dataCollection, DataRecordFilter filter) {
		mDataCollection = dataCollection;
		mFilter = filter;
	}

	@Override
	public DataRecord get(int location) {
		checkFilter();
		return mDataCollection.get(mMatches.get(location));
	}

	@Override
	public int size() {
		checkFilter();
		return mMatches.size();
	}
	
	/**
	 * Generate the filter list if necessary.
	 */
	private void checkFilter() {
		if (mMatches == null) {
			mMatches = new ArrayList<Integer>();
			for (int i = 0; i < mDataCollection.size(); i++) {
				if (mFilter.isMatch(mDataCollection.get(i))) {
					mMatches.add(i);
				}
			}
		}
	}

}
