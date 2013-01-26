package com.megginson.sloop.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A data record, consisting of name/value pairs.
 * 
 * Implements the {@link Parcelable} interface so that it can be saved in an
 * Android {@link Bundle}.
 * 
 * @author David Megginson
 * @see DataCollection
 * @see DataEntry
 */
public class DataRecord extends AbstractList<DataEntry> implements Parcelable {

	private List<DataEntry> mDataEntries = new ArrayList<DataEntry>();

	
	/**
	 * Default constructor.
	 * 
	 * Creates an empty record.
	 */
	public DataRecord() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * Performs a deep copy of the record provided.
	 * 
	 * @param dataRecord
	 *            The data record to copy.
	 */
	public DataRecord(DataRecord dataRecord) {
		for (DataEntry dataEntry : dataRecord) {
			mDataEntries.add(new DataEntry(dataEntry));
		}
	}

	/**
	 * Convenience function to create and add a new entry.
	 * 
	 * Equivalent to add(new DataEntry(name, value)).
	 * 
	 * @param name
	 *            The name/label of the {@link DataEntry}.
	 * @param value
	 *            The string value of the {@link DataEntry}.
	 */
	public void addEntry(String name, String value) {
		add(new DataEntry(name, value));
	}

	@Override
	public DataEntry get(int location) {
		return mDataEntries.get(location);
	}

	@Override
	public int size() {
		return mDataEntries.size();
	}

	@Override
	public DataEntry set(int location, DataEntry entry) {
		return mDataEntries.set(location, entry);
	}

	@Override
	public void add(int location, DataEntry entry) {
		mDataEntries.add(location, entry);
	}

	@Override
	public DataEntry remove(int location) {
		return mDataEntries.remove(location);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mDataEntries.size());
		for (DataEntry dataEntry : mDataEntries) {
			dest.writeParcelable(dataEntry, 0);
		}
	}

	public final static Parcelable.Creator<DataRecord> CREATOR = new Parcelable.Creator<DataRecord>() {

		@Override
		public DataRecord createFromParcel(Parcel source) {
			DataRecord dataRecord = new DataRecord();
			int size = source.readInt();
			for (int i = 0; i < size; i++) {
				dataRecord.add((DataEntry) source.readParcelable(DataEntry.class.getClassLoader()));
			}
			return dataRecord;
		}

		@Override
		public DataRecord[] newArray(int size) {
			return new DataRecord[size];
		}
	};

}
