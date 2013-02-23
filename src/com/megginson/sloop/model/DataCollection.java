package com.megginson.sloop.model;

import java.util.List;

import com.megginson.sloop.model.impl.DataCollectionIO;

/**
 * A read-only table of data.
 * 
 * This interface represents tabular data, where every derived
 * {@link DataRecord} has the same entries in the same order. The headers are
 * available through {@link #getHeaders()}, and the rows are available through
 * {@link #getRecords()}.
 * 
 * A data collection has built-in support for filtering, base on the
 * {@link ValueFilter} interface. See {@link #setFilteringEnabled(boolean)},
 * {@link #putColumnFilter(String, ValueFilter)}, and {@link #getRecords()}.
 * Filtering would be more elegant if it happened outside the class, but it
 * would be highly inefficient, since it wouldn't be able to take advantage of
 * the internal data-storage optimisations, and would result in a lot of object
 * creation.
 * 
 * The {@link DataCollectionIO} class provides a loader for creating a
 * collection from a CSV file.
 * 
 * @author David Megginson
 */
public interface DataCollection {

	/**
	 * Get the collection headers.
	 * 
	 * @return a list of column headers.
	 */
	public abstract List<String> getHeaders();

	/**
	 * Get the collection rows (data records) without filters applied.
	 * 
	 * @return a list of all records in the collection.
	 */
	public abstract List<DataRecord> getRecords();

	/**
	 * Get the collection rows (data records) with filters applied.
	 * 
	 * @return a possibly-empty list of records, after applying any filters.
	 */
	public abstract List<DataRecord> getFilteredRecords();

	/**
	 * Indicate whether the collection is currently filtered.
	 * 
	 * @return true if the collection is filtered; false if it is unfiltered.
	 * @see #setFilteringEnabled(boolean)
	 * @see #putColumnFilter(String, ValueFilter)
	 */
	public abstract boolean isFilteringEnabled();

	/**
	 * Toggle whether the collection is currently filtered.
	 * 
	 * Simply adding a filter with {@link #putColumnFilter(String, ValueFilter)}
	 * does not toggle filtering; it's necessary to call this method explicitly
	 * to turn filtering on and off. That way, we can keep filters around while
	 * easily toggling between a filtered and unfiltered view.
	 * 
	 * @param isFiltered
	 *            true to enable filtering; false otherwise.
	 * @see #isFilteringEnabled()
	 * @see #putColumnFilter(String, ValueFilter)
	 */
	public abstract void setFilteringEnabled(boolean isFiltered);

	/**
	 * Indicate whether any filters are currently assigned.
	 * 
	 * @return true if there is at least one filter assigned.
	 */
	public abstract boolean hasFilters();

	/**
	 * Add a filter for one of the columns.
	 * 
	 * This method does not actually enable filtering; it's necessary to invoke
	 * the {@link #setFilteringEnabled(boolean)} method explicitly to turn
	 * filtering on and off.
	 * 
	 * @param name
	 *            the column name to filter.
	 * @param filter
	 *            the filter object.
	 * @see #getColumnFilter(String)
	 * @see #setTextFilter(ValueFilter)
	 * @see #isFilteringEnabled()
	 * @see #getFilteredRecords()
	 */
	public abstract void putColumnFilter(String name, ValueFilter filter);

	/**
	 * Get the current filter for a column.
	 * 
	 * @param name
	 *            the column name.
	 * @return the filter if it exists, or null otherwise.
	 * @see #putColumnFilter(String, ValueFilter)
	 * @see #getTextFilter()
	 */
	public abstract ValueFilter getColumnFilter(String name);

	/**
	 * Set a text search filter for all columns.
	 * 
	 * Unlike the column filters, this filter will be applied to all values in
	 * turn, and if any of them passes, then the record passes.
	 * 
	 * The collection will apply the filter only if filtering is enabled via
	 * {@link #setFilteringEnabled(boolean)}.
	 * 
	 * @param filter
	 *            the text filter object.
	 * @see #putColumnFilter(String, ValueFilter)
	 * @see #getTextFilter()
	 * @see #getFilteredRecords()
	 */
	public abstract void setTextFilter(ValueFilter filter);

	/**
	 * Get the current text filter for this collection.
	 * 
	 * @return the filter object, or null if none exists.
	 * @see #setTextFilter(ValueFilter)
	 * @see #getColumnFilter(String)
	 */
	public abstract ValueFilter getTextFilter();

}