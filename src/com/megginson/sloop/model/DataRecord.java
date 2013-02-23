package com.megginson.sloop.model;

import java.util.List;

import android.os.Parcelable;

public interface DataRecord extends List<DataEntry>, Parcelable {

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