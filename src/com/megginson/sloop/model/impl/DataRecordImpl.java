package com.megginson.sloop.model.impl;

import java.util.AbstractList;
import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.megginson.sloop.model.DataEntry;
import com.megginson.sloop.model.DataRecord;

/**
 * A read-only data record, consisting of name/value pairs.
 * 
 * Implements the {@link Parcelable} interface so that it can be saved in an
 * Android {@link Bundle}.
 * 
 * @author David Megginson
 * @see DataCollectionImpl
 * @see DataEntryImpl
 */
class DataRecordImpl implements DataRecord {
	
	private String mHeaders[];
	
	private String mValues[];
	
	private boolean mColumnFilterFlags[];
	
	private EntryList mEntryList;

	protected DataRecordImpl(String headers[], String values[], boolean columnFilterFlags[]) {
		mHeaders = headers;
		mValues = values;
		mColumnFilterFlags = columnFilterFlags;
	}
	
	@Override
	public List<DataEntry> getEntries() {
		if (mEntryList == null) {
			mEntryList = new EntryList();
		}
		return mEntryList;
	}

	@Override
	public DataEntry get(String name) {
		return get(name, 0);
	}

	@Override
	public DataEntry get(String name, int index) {
		for (int i = 0; i < mHeaders.length; i++) {
			if (name.equals(mHeaders[i])) {
				if (index == 0) {
					return makeEntry(i);
				} else {
					index--;
				}
			}
		}
		return null;
	}

	@Override
	public int size(String name) {
		int count = 0;
		for (String header : mHeaders) {
			if (name.equals(header)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Serialize the record to an Android parcel.
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(mHeaders);
		dest.writeStringArray(mValues);
		dest.writeBooleanArray(mColumnFilterFlags);
	}
	
	private DataEntry makeEntry(int index) {
		return new DataEntryImpl(mHeaders[index], mValues[index], mColumnFilterFlags[index]);
	}

	public final static Parcelable.Creator<DataRecordImpl> CREATOR = new Parcelable.Creator<DataRecordImpl>() {

		/**
		 * Deserialize a record from an Android parcel.
		 */
		@Override
		public DataRecordImpl createFromParcel(Parcel source) {
			String headers[] = source.createStringArray();
			String values[] = source.createStringArray();
			boolean columnFilterFlags[] = source.createBooleanArray();
			return new DataRecordImpl(headers, values, columnFilterFlags);
		}

		@Override
		public DataRecordImpl[] newArray(int size) {
			return new DataRecordImpl[size];
		}
	};
	
	/**
	 * List wrapper for the record's entries.
	 * 
	 * @author David Megginson
	 * @see DataRecord#getEntries()
	 */
	private class EntryList extends AbstractList<DataEntry> {
		
		@Override
		public DataEntry get(int location) {
			return makeEntry(location);
		}

		@Override
		public int size() {
			return mHeaders.length;
		}

	}

}
