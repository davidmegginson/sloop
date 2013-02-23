package com.megginson.sloop.model.impl;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.megginson.sloop.model.DataEntry;

/**
 * A single name=value pair in a data record.
 * 
 * This implements the {@link Parcelable} interface, so that it can be passed
 * around in an Android {@link Bundle}.
 * 
 * @author David Megginson
 * @see DataCollectionImpl
 * @see DataRecordImpl
 */
class DataEntryImpl implements DataEntry {

	private String mKey;
	private String mValue;
	private boolean mHasFilter = false;
	
	protected DataEntryImpl (String key, String value) {
		this(key, value, false);
	}

	protected DataEntryImpl(String key, String value, boolean hasFilter) {
		mKey = key;
		setValue(value);
		mHasFilter = hasFilter;
	}
	
	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataEntry#hasFilter()
	 */
	@Override
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
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof DataEntryImpl) {
			return getKey().equals(((DataEntryImpl) o).getKey())
					&& getValue().equals(((DataEntryImpl) o).getValue());
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

	public final static Parcelable.Creator<DataEntryImpl> CREATOR = new Parcelable.Creator<DataEntryImpl>() {

		@Override
		public DataEntryImpl createFromParcel(Parcel source) {
			String key = source.readString();
			String value = source.readString();
			boolean hasFilter = (Boolean)source.readValue(Boolean.class.getClassLoader());
			return new DataEntryImpl(key, value, hasFilter);
		}

		@Override
		public DataEntryImpl[] newArray(int size) {
			return new DataEntryImpl[size];
		}
	};

}
