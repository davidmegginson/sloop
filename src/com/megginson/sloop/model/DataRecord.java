package com.megginson.sloop.model;

import java.util.List;
import java.util.Map;

import android.os.Parcelable;

/**
 * A read-only row of data.
 * 
 * This interface represents a single row of data in a {@link DataCollection}. Use the
 * {@link #getEntries()} method to get at the actual entry list. There are also
 * convenience methods for looking up entries by name rather than index (a bit
 * like a {@link Map}).
 * 
 * @author David Megginson
 */
public interface DataRecord extends Parcelable {

	/**
	 * Return a read-only list of entries in this record.
	 * 
	 * @return a possibly-empty list of entries.
	 */
	public abstract List<DataEntry> getEntries();

	/**
	 * Return the first column with the specified name.
	 * 
	 * @param name
	 *            the column name.
	 * @return the data entry, or null if it does not exist.
	 */
	public abstract DataEntry get(String name);

	/**
	 * Return the nth column with the specified name.
	 * 
	 * @param name
	 *            the column name.
	 * @param index
	 *            the column index, zero-based (counting only columns with the
	 *            same name)
	 * @return the matching entry, or null if it does not exist.
	 */
	public abstract DataEntry get(String name, int index);

	/**
	 * Return the number of entries with the specified name.
	 * 
	 * @param name
	 *            the column name.
	 * @return the number of entries.
	 */
	public abstract int size(String name);

}