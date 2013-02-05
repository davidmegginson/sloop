package com.megginson.sloop.model;

import android.os.Parcelable;

/**
 * A test for a list item.
 * 
 * The {@link FilteredList} class uses an object implementing this interface to
 * decide which items to include from the main list. This simple example
 * includes only items that don't contain the string value "exclude me":
 * 
 * <pre>
 * ListItemFilter&lt;String> filter = new ListItemFilter&lt;String>() {
 *   public boolean isMatch(String item) {
 *     return !("exclude me".equals(item));
 *   }
 * });
 * 
 * FilteredList&lt;String> filteredList = 
 *   new FilteredList&lt;String>(filter, list);
 * </pre>
 * 
 * Note that it's possible to build filters that combine other filters in
 * complex ways; just make certain that there's one top-level
 * {@link ListItemFilter} to fire off all the other ones.
 * 
 * @author David Megginson
 */
public interface ListItemFilter<T> {

	/**
	 * Test whether a list item should pass the filter.
	 * 
	 * @param item
	 *            the list item to test.
	 * @return true if the list item should appear in the filtered results;
	 *         false otherwise.
	 */
	public boolean isMatch(T item);

}
