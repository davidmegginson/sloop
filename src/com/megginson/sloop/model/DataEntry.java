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

	public DataEntry(String key, String value) {
		mKey = key;
		setValue(value);
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
		mValue = value;
		return value;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mKey);
		dest.writeString(mValue);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new DataEntry(getKey(), getValue());
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
			return new DataEntry(key, value);
		}

		@Override
		public DataEntry[] newArray(int size) {
			return new DataEntry[size];
		}
	};

}
