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
 * TODO This is initially just a naive implementation wrapping an ArrayList. It
 * will evolve into something more memory-efficient later.
 * 
 * Implements the {@link Parcelable} interface so that it can be saved in an
 * Android {@link Bundle}.
 * 
 * @author David Megginson
 * @see DataCollection
 * @see DataEntry
 */
public class DataRecord extends AbstractList<DataEntry> implements Parcelable {

	private List<DataEntry> dataEntries = new ArrayList<DataEntry>();

	public DataRecord() {
		super();
	}

	public void addEntry(String name, String value) {
		add(new DataEntry(name, value));
	}

	@Override
	public DataEntry get(int location) {
		return dataEntries.get(location);
	}

	@Override
	public int size() {
		return dataEntries.size();
	}

	@Override
	public DataEntry set(int location, DataEntry entry) {
		return dataEntries.set(location, entry);
	}

	@Override
	public void add(int location, DataEntry entry) {
		dataEntries.add(location, entry);
	}

	@Override
	public DataEntry remove(int location) {
		return dataEntries.remove(location);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(dataEntries.size());
		for (DataEntry dataEntry : dataEntries) {
			dest.writeParcelable(dataEntry, 0);
		}
	}

	public final static Parcelable.Creator<DataRecord> CREATOR = new Parcelable.Creator<DataRecord>() {

		@Override
		public DataRecord createFromParcel(Parcel source) {
			DataRecord dataRecord = new DataRecord();
			int size = source.readInt();
			for (int i = 0; i < size; i++) {
				dataRecord.add((DataEntry) source.readParcelable(null));
			}
			return dataRecord;
		}

		@Override
		public DataRecord[] newArray(int size) {
			return new DataRecord[size];
		}
	};

}
