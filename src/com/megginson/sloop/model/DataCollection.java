package com.megginson.sloop.model;

import java.util.List;

public interface DataCollection extends List<DataRecord> {

	/**
	 * Indicate whether the collection is currently filtered.
	 * 
	 * @return true if the collection is filtered; false if it is unfiltered.
	 * @see #setFiltered(boolean)
	 * @see #putColumnFilter(String, ValueFilter)
	 */
	public abstract boolean isFiltered();

	/**
	 * Toggle whether the collection is currently filtered.
	 * 
	 * Simply adding a filter with {@link #putColumnFilter(String, ValueFilter)} does
	 * not toggle filtering; it's necessary to call this method explicitly to
	 * turn filtering on and off.
	 * 
	 * @param isFiltered
	 *            true to enable filtering; false otherwise.
	 * @see #isFiltered()
	 * @see #putColumnFilter(String, ValueFilter)
	 */
	public abstract void setFiltered(boolean isFiltered);

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
	 * the {@link #setFiltered(boolean)} method explicitly to turn filtering on
	 * and off.
	 * 
	 * @param name
	 *            the column name to filter.
	 * @param filter
	 *            the filter object.
	 * @see #getColumnFilter(String)
	 * @see #isFiltered()
	 */
	public abstract void putColumnFilter(String name, ValueFilter filter);

	/**
	 * Get the current filter for a column.
	 * 
	 * @param name
	 *            the column name.
	 * @return the filter if it exists, or null otherwise.
	 * @see #putColumnFilter(String, ValueFilter)
	 */
	public abstract ValueFilter getColumnFilter(String name);

	/**
	 * Very simple search function.
	 * 
	 * @param pattern
	 *            A substring to search for (case-sensitive for now).
	 * @param startingPosition
	 *            The record for starting the search.
	 * @return The number of the first record containing the pattern, or -1 if
	 *         none was found.
	 */
	public abstract int search(String pattern, int startingPosition);

	/**
	 * Get the unfiltered size of the collection.
	 * 
	 * This method will always return the total number of records in the
	 * collection, even if filtering is in force.
	 * 
	 * @return the total number of items in the (unfiltered) collection.
	 * @see #size()
	 */
	public abstract int sizeUnfiltered();

}