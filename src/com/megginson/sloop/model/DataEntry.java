package com.megginson.sloop.model;

import java.util.Map;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A single name=value pair in a data record.
 * 
 * This implements the {@link Parcelable} interface, so that it can be passed
 * around in an Android {@link Bundle}.
 * 
 * @author David Megginson
 * @see DataCollection
 * @see DataRecord
 */
public class DataEntry implements Map.Entry<String, String>, Parcelable {

	private String mKey;
	private String mValue;
	private boolean mHasFilter = false;
	
	public DataEntry(DataEntry dataEntry) {
		mKey = dataEntry.getKey();
		setValue(dataEntry.getValue());
	}
	
	public DataEntry (String key, String value) {
		this(key, value, false);
	}

	public DataEntry(String key, String value, boolean hasFilter) {
		mKey = key;
		setValue(value);
		mHasFilter = hasFilter;
	}
	
	/**
	 * Indicate whether there is a filter associated with this entry.
	 * 
	 * @return true if there is an associated filter; false otherwise.
	 */
	public boolean hasFilter () {
		return mHasFilter;
	}

	@Override
	public String getKey() {
		return mKey;
	}

	@Override
	public String getValue() {
		return mValue;
	}

	@Override
	public String setValue(String value) {
		String oldValue = mValue;
		mValue = value;
		return oldValue;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mKey);
		dest.writeString(mValue);
		dest.writeValue(mHasFilter);
	}

	@Override
	protected Object clone() {
		return new DataEntry(this);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof DataEntry) {
			return getKey().equals(((DataEntry) o).getKey())
					&& getValue().equals(((DataEntry) o).getValue());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getKey().hashCode() + getValue().hashCode();
	}

	@Override
	public String toString() {
		return "{" + getKey() + "=" + getValue() + "}";
	}

	public final static Parcelable.Creator<DataEntry> CREATOR = new Parcelable.Creator<DataEntry>() {

		@Override
		public DataEntry createFromParcel(Parcel source) {
			String key = source.readString();
			String value = source.readString();
			boolean hasFilter = (Boolean)source.readValue(Boolean.class.getClassLoader());
			return new DataEntry(key, value, hasFilter);
		}

		@Override
		public DataEntry[] newArray(int size) {
			return new DataEntry[size];
		}
	};

}
