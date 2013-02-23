package com.megginson.sloop.model;

import java.util.Map;

import android.os.Parcelable;

public interface DataEntry extends Map.Entry<String, String>, Parcelable {

	/**
	 * Indicate whether there is a filter associated with this entry.
	 * 
	 * FIXME should query the DataRecord, not the DataEntry
	 * 
	 * @return true if there is an associated filter; false otherwise.
	 */
	public abstract boolean hasFilter();

}