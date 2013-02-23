package com.megginson.sloop.model.impl;

import java.util.AbstractList;

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
class DataRecordImpl extends AbstractList<DataEntry> implements DataRecord {
	
	private String mHeaders[];
	
	private String mValues[];
	
	private boolean mColumnFilterFlags[];

	/**
	 * Convenience constructor to build the record from arrays.
	 * 
	 * The record will be the same length as the headers array.
	 * 
	 * @param headers
	 *            An array of strings representing the headers.
	 * @param values
	 *            An array of strings representing the values.
	 */
	protected DataRecordImpl(String headers[], String values[], boolean columnFilterFlags[]) {
		mHeaders = headers;
		mValues = values;
		mColumnFilterFlags = columnFilterFlags;
	}

	@Override
	public DataEntry get(int location) {
		return new DataEntryImpl(mHeaders[location], mValues[location], mColumnFilterFlags[location]);
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataRecord#get(java.lang.String)
	 */
	@Override
	public DataEntry get(String name) {
		return get(name, 0);
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataRecord#get(java.lang.String, int)
	 */
	@Override
	public DataEntry get(String name, int index) {
		for (int i = 0; i < mHeaders.length; i++) {
			if (name.equals(mHeaders[i])) {
				if (index == 0) {
					return get(i);
				} else {
					index--;
				}
			}
		}
		return null;
	}

	@Override
	public int size() {
		return mHeaders.length;
	}

	/* (non-Javadoc)
	 * @see com.megginson.sloop.model.DataRecord#size(java.lang.String)
	 */
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
		// TODO Auto-generated method stub
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

}
