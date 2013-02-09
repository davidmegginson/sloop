package com.megginson.sloop.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.megginson.sloop.util.FilteredList;
import com.megginson.sloop.util.ListItemFilter;

/**
 * Filter for a data record.
 * 
 * Takes a collection of {@link DataEntryFilter} objects, one for each field in
 * the record, then applies them all. If they all pass, so does the record.
 * 
 * This class, together with {@link FilteredList}, provides the main machinery
 * for filtering a {@link DataCollection} for display in the Sloop browser.
 * 
 * This filter is parcelable, so that it can be passed around easily in an
 * {@link Intent}.
 * 
 * @author David Megginson
 */
public class DataRecordFilter implements ListItemFilter<DataRecord>, Parcelable {

	/**
	 * The column filters;
	 */
	private Map<String, DataEntryFilter> mFilters = new HashMap<String, DataEntryFilter>();

	/**
	 * Create a new, empty filter.
	 * 
	 * Use {@link #putFilter(DataEntryFilter)} to add column-specific subfilters
	 * as needed.
	 */
	public DataRecordFilter() {
		super();
	}

	/**
	 * Add a new filter.
	 * 
	 * Will replace any existing filter for the specified column name.
	 * 
	 * @param filter
	 *            the (new) filter for the column.
	 * @see #getFilter(String)
	 * @see #getFilters()
	 */
	public void putFilter(DataEntryFilter filter) {
		mFilters.put(filter.getName(), filter);
	}
	
	/**
	 * Clear the filter for a column.
	 * 
	 * @param name the column name.
	 */
	public void clearFilter(String name) {
		mFilters.remove(name);
	}

	/**
	 * Get the filter for a column.
	 * 
	 * @param name
	 *            the column name.
	 * @return the existing filter, or null if none exists.
	 * @see #putFilter(DataEntryFilter)
	 * @see #getFilters()
	 */
	public DataEntryFilter getFilter(String name) {
		return mFilters.get(name);
	}

	/**
	 * Get a collection of all column filters.
	 * 
	 * @return all column filters as a (possibly empty) collection.
	 * @see #putFilter(DataEntryFilter)
	 * @see #getFilter(String)
	 */
	public Collection<DataEntryFilter> getFilters() {
		return mFilters.values();
	}
	
	/**
	 * Clear all column filters.
	 */
	public void clear() {
		mFilters.clear();
	}

	@Override
	public boolean isMatch(DataRecord dataRecord) {
		for (DataEntryFilter filter : getFilters()) {
			boolean result = filter.isMatch(dataRecord.get(filter.getName()));
			if (!result) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelableArray(
				mFilters.values().toArray(new DataEntryFilter[0]), 0);
	}

	/**
	 * Recreate a {@link DataRecordFilter} from a {@link Parcel}
	 */
	public final static Parcelable.Creator<DataRecordFilter> CREATOR = new Parcelable.Creator<DataRecordFilter>() {

		@Override
		public DataRecordFilter createFromParcel(Parcel source) {
			DataRecordFilter recordFilter = new DataRecordFilter();
			Parcelable filters[] = source
					.readParcelableArray(DataEntryFilter.class.getClassLoader());
			for (Parcelable filter : filters) {
				recordFilter.putFilter((DataEntryFilter) filter);
			}
			return recordFilter;
		}

		@Override
		public DataRecordFilter[] newArray(int size) {
			return new DataRecordFilter[size];
		}
	};

}
