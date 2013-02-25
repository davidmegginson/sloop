package com.megginson.sloop.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Filters applied to a data collection.
 * 
 * @author David Megginson
 * @see DataCollection
 * @see ValueFilter
 */
public class CollectionFilter {

	private ValueFilter mTextFilter;

	private Map<String, ValueFilter> mColumnFilters = new HashMap<String, ValueFilter>();

	/**
	 * Set the fulltext filter.
	 * 
	 * This is a filter that must match any entry in a {@link DataRecord} for
	 * the record to pass.
	 * 
	 * @param textFilter
	 *            the filter to apply.
	 * @see #getTextFilter()
	 * @see #getColumnFilters()
	 */
	public void setTextFilter(ValueFilter textFilter) {
		mTextFilter = textFilter;
	}

	/**
	 * Get the current fulltext filter.
	 * 
	 * @return the current text filter, or null if none exists.
	 * @see #setTextFilter(ValueFilter)
	 * @see #getColumnFilters()
	 */
	public ValueFilter getTextFilter() {
		return mTextFilter;
	}

	/**
	 * Get the current column filters.
	 * 
	 * This method returns a {@link Map} of {@link ValueFilter} objects that
	 * Sloop will apply to specific named entries (columns). The application may
	 * use the map to assign new filters.
	 * 
	 * @return a map of column filters (possibly empty).
	 * @see #setTextFilter(ValueFilter)
	 */
	public Map<String, ValueFilter> getColumnFilters() {
		return mColumnFilters;
	}
	
	public String toString() {
		String s = "";
		if (mTextFilter != null) {
			s += "*" + mTextFilter.toString();
		}
		for (Map.Entry<String, ValueFilter> entry : getColumnFilters().entrySet()) {
			if (s.length() > 0) {
				s += "&";
			}
			s += entry.getKey() + entry.getValue();
		}
		return s;
	}

}
