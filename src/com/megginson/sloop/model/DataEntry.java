package com.megginson.sloop.model;

import java.util.Map;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A single name=value pair in a data record.
 * 
 * TODO This is initially a naive implementation. It will evolve into something
 * more efficient, probably involving indexes into arrays.
 * 
 * This implements the {@link Parcelable} interface, so that it can be passed
 * around in an Android {@link Bundle}.
 * 
 * @author David Megginson
 * @see DataCollection
 * @see DataRecord
 */
public class DataEntry implements Map.Entry<String, String>, Parcelable {

	private String key;
	private String value;

	public DataEntry(String key, String value) {
		this.key = key;
		setValue(value);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String setValue(String value) {
		this.value = value;
		return value;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(key);
		dest.writeString(value);
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
