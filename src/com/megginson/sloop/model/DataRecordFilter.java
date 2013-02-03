package com.megginson.sloop.model;

/**
 * Interface for a data record filter.
 * 
 * The {@link FilteredDataCollection} class uses objects that implement this
 * interface to selectively filter data collections (e.g. show only records
 * where the "country" column equals "CA").
 * 
 * Note that this interface allows testing an entire record, not just individual
 * fields; that way, tests can look at more than one field together.
 * 
 * @author David Megginson
 */
public interface DataRecordFilter {

	/**
	 * Test whether a record should pass the filter.
	 * 
	 * @param dataRecord
	 *            the record to test.
	 * @return true if the record should appear in the filtered results; false
	 *         otherwise.
	 */
	public boolean isMatch(DataRecord dataRecord);

}
