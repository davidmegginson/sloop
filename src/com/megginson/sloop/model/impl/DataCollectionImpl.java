package com.megginson.sloop.model.impl;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.megginson.sloop.model.DataCollection;
import com.megginson.sloop.model.DataRecord;
import com.megginson.sloop.model.ValueFilter;

/**
 * A collection of data records (i.e. internal representation of a CSV file).
 * 
 * This represents tabular data: a list of lists, where the child lists are all
 * the same length. From a Java Collections point of view, this is a read-only
 * {@link List} of {@link DataRecordImpl} objects. The list of headers is set at
 * creation time, and can't be changed afterwards. To add a row of new values,
 * use the {@link addRecord} objects.
 * 
 * The collection can filter itself to show only some of its members (or none at
 * all) using a {@link ValueFilter}. For example, to show only records where the
 * <code>foo</code> column has the value <code>bar</code>, you could use the
 * following code:
 * 
 * <pre>
 * dataCollection.putFilter("foo", new ValueFilter() {
 *   public boolean isMatch (value) { return "bar".equals(value); }
 * });
 * 
 * dataCollection.setFiltered(true);
 * </pre>
 * 
 * @author David Megginson
 * @see DataRecordImpl
 */
class DataCollectionImpl implements DataCollection {

	private String mHeaders[];

	private List<String[]> mRecords = new ArrayList<String[]>();

	private ValueFilter mColumnFilters[];

	private ValueFilter mTextFilter;

	private boolean mColumnFilterFlags[];

	private List<Integer> mFilteredIndices = new ArrayList<Integer>();

	private boolean mIsFiltered = false;

	private boolean mIsCacheDirty = true;

	private FilteredRecordList mFilteredRecordList;

	private UnfilteredRecordList mUnfilteredRecordList;

	protected DataCollectionImpl(String headers[]) {
		super();
		mHeaders = headers;
		mColumnFilters = new ValueFilter[headers.length];
		mColumnFilterFlags = new boolean[headers.length];
	}

	@Override
	public List<String> getHeaders() {
		return Arrays.asList(mHeaders);
	}

	@Override
	public List<DataRecord> getFilteredRecords() {
		// create only if needed
		if (mFilteredRecordList == null) {
			mFilteredRecordList = new FilteredRecordList();
		}
		return mFilteredRecordList;
	}

	@Override
	public List<DataRecord> getRecords() {
		// create only if needed
		if (mUnfilteredRecordList == null) {
			mUnfilteredRecordList = new UnfilteredRecordList();
		}
		return mUnfilteredRecordList;
	}

	@Override
	public boolean isFilteringEnabled() {
		return mIsFiltered;
	}

	public void setFilteringEnabled(boolean isFiltered) {
		mIsFiltered = isFiltered;
	}

	@Override
	public boolean hasFilters() {
		for (ValueFilter filter : mColumnFilters) {
			if (filter != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void putColumnFilter(String name, ValueFilter filter) {
		int index = indexOf(name);
		if (index > -1) {
			mColumnFilters[index] = filter;
			mColumnFilterFlags[index] = (filter != null);
			mIsCacheDirty = true;
		}
	}

	@Override
	public ValueFilter getColumnFilter(String name) {
		return mColumnFilters[indexOf(name)];
	}

	@Override
	public void setTextFilter(ValueFilter filter) {
		mTextFilter = filter;
		mIsCacheDirty = true;
	}

	@Override
	public ValueFilter getTextFilter() {
		return mTextFilter;
	}

	/**
	 * Add a row to the collection as a string array.
	 * 
	 * The class will convert this to a {@link DataRecordImpl} for display as
	 * required.
	 * 
	 * @param record
	 *            an array of strings representing a row.
	 */
	protected void addRecord(String record[]) {
		mRecords.add(record.clone());
		mIsCacheDirty = true;
	}

	/**
	 * Get the index of a header (first occurrence only).
	 * 
	 * @param header
	 *            the header string.
	 * @return the zero-based index, or -1 if not found.
	 */
	private int indexOf(String header) {
		for (int i = 0; i < mHeaders.length; i++) {
			if (header.equals(mHeaders[i])) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Make a cached list of indices for a filtered view.
	 */
	private void rebuildCache() {
		mFilteredIndices.clear();
		for (int i = 0; i < mRecords.size(); i++) {
			if (doFilterRecord(mRecords.get(i))) {
				mFilteredIndices.add(i);
			}
		}
		mIsCacheDirty = false;
	}

	/**
	 * Run all appropriate filters against a single record.
	 * 
	 * @param record
	 *            the record to filter (in its String array form).
	 * @return true if the record matches the filters.
	 */
	private boolean doFilterRecord(String record[]) {
		boolean foundTextMatch = false;

		for (int i = 0; i < mHeaders.length; i++) {

			// No filters to apply, so keep going ...
			if (mTextFilter == null && mColumnFilters[i] == null) {
				continue;
			}

			// If there's a column filter, it has to pass
			if (mColumnFilters[i] != null) {
				if (i >= record.length || !mColumnFilters[i].isMatch(record[i])) {
					return false;
				}
			}

			// If there's a text filter, it has to pass once for whole record
			if (!foundTextMatch && mTextFilter != null) {
				if (i < record.length && mTextFilter.isMatch(record[i])) {
					foundTextMatch = true;
				}
			}
		}

		// Success if there's no text filter, or a match for the text filter.
		return (mTextFilter == null || foundTextMatch);
	}

	/**
	 * List wrapper showing only filtered records.
	 * 
	 * If filtering is off (see
	 * {@link DataCollection#setFilteringEnabled(boolean)}), then this will
	 * behave identically to an {@link UnfilteredRecordList}.
	 * 
	 * @author David Megginson
	 * @see DataCollection#getFilteredRecords()
	 * @see DataCollection#isFilteringEnabled()
	 */
	private class FilteredRecordList extends AbstractList<DataRecord> {

		@Override
		public int size() {
			if (mIsFiltered) {
				if (mIsCacheDirty) {
					rebuildCache();
				}
				return mFilteredIndices.size();
			} else {
				return mRecords.size();
			}
		}

		@Override
		public DataRecord get(int location) {
			int rawLocation;
			if (mIsFiltered) {
				if (mIsCacheDirty) {
					rebuildCache();
				}
				rawLocation = mFilteredIndices.get(location);
			} else {
				rawLocation = location;
			}
			return new DataRecordImpl(mHeaders, mRecords.get(rawLocation),
					mColumnFilterFlags);
		}

	}

	/**
	 * List wrapper showing all records.
	 * 
	 * This list wrapper will include all records, even when filtering is turned
	 * on.
	 * 
	 * @author David Megginson
	 * @see DataCollection#getRecords()
	 * @see DataCollection#isFilteringEnabled()
	 */
	private class UnfilteredRecordList extends AbstractList<DataRecord> {

		@Override
		public int size() {
			return mRecords.size();
		}

		@Override
		public DataRecord get(int location) {
			return new DataRecordImpl(mHeaders, mRecords.get(location),
					mColumnFilterFlags);
		}
	}

}
