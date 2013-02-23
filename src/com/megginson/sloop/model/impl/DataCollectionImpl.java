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

	private ValueFilter mFilters[];
	
	private boolean mColumnFilterFlags[];
	
	private FilteredRecordList mFilteredRecordList;

	private List<String[]> mRecords = new ArrayList<String[]>();

	private List<Integer> mFilteredIndices = new ArrayList<Integer>();

	private boolean mIsFiltered = false;

	private boolean mIsCacheDirty = true;

	/**
	 * Default constructor.
	 * 
	 * Creates an empty collection with no filters.
	 */
	public DataCollectionImpl(String headers[]) {
		super();
		mHeaders = headers;
		mFilters = new ValueFilter[headers.length];
		mColumnFilterFlags = new boolean[headers.length];
	}
	
	public List<String> getHeaders(){
		return Arrays.asList(mHeaders);
	}
	
	public List<DataRecord> getFilteredRecords(){
		// create only if needed
		if (mFilteredRecordList == null) {
			mFilteredRecordList = new FilteredRecordList();
		}
		return mFilteredRecordList;
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataCollection#isFiltered()
	 */
	@Override
	public boolean isFiltered() {
		return mIsFiltered;
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataCollection#setFiltered(boolean)
	 */
	@Override
	public void setFiltered(boolean isFiltered) {
		mIsFiltered = isFiltered;
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataCollection#hasFilters()
	 */
	@Override
	public boolean hasFilters() {
		for (ValueFilter filter : mFilters) {
			if (filter != null) {
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataCollection#putFilter(java.lang.String, com.megginson.sloop.model.ValueFilter)
	 */
	@Override
	public void putColumnFilter(String name, ValueFilter filter) {
		int index = indexOf(name);
		mFilters[index] = filter;
		mColumnFilterFlags[index] = (filter != null);
		mIsCacheDirty = true;
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataCollection#getFilter(java.lang.String)
	 */
	@Override
	public ValueFilter getColumnFilter(String name) {
		return mFilters[indexOf(name)];
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
	public void addRecord(String record[]) {
		mRecords.add(record.clone());
		mIsCacheDirty = true;
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataCollection#search(java.lang.String, int)
	 */
	@Override
	public int search(String pattern, int startingPosition) {
		for (int i = startingPosition; i < mRecords.size(); i++) {
			String record[] = mRecords.get(i);
			for (String value : record) {
				if (value.contains(pattern)) {
					return i;
				}
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataCollection#sizeUnfiltered()
	 */
	@Override
	public int sizeUnfiltered() {
		return mRecords.size();
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
	 * @return true if the record matches the filter.
	 */
	private boolean doFilterRecord(String record[]) {
		for (int i = 0; i < mHeaders.length; i++) {
			if (i < mFilters.length && mFilters[i] != null) {
				if (i >= record.length || !mFilters[i].isMatch(record[i])) {
					return false;
				}
			}
		}
		return true;
	}
	
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
		public DataRecordImpl get(int location) {
			int rawLocation;
			if (mIsFiltered) {
				if (mIsCacheDirty) {
					rebuildCache();
				}
				rawLocation = mFilteredIndices.get(location);
			} else {
				rawLocation = location;
			}
			return new DataRecordImpl(mHeaders, mRecords.get(rawLocation), mColumnFilterFlags);
		}
		
	}

}
