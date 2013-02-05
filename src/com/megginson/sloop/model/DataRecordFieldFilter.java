package com.megginson.sloop.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Filter to test a single field in a data record.
 * 
 * @author David Megginson
 */
public class DataRecordFieldFilter implements ListItemFilter<DataRecord>, Parcelable {

	public final static int OPERATOR_EQUALS = 1;
	public final static int OPERATOR_STARTSWITH = 2;

	private String mName;

	private String mPattern;

	private int mOperator;

	public DataRecordFieldFilter(String name, String pattern) {
		this(name, pattern, OPERATOR_EQUALS);
	}
	
	public DataRecordFieldFilter(DataRecordFieldFilter filter) {
		mName = filter.getName();
		mPattern = filter.getPattern();
		mOperator = filter.getOperator();
	}

	public DataRecordFieldFilter(String name, String pattern, int operator) {
		mName = name;
		mPattern = pattern;
		mOperator = operator;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getPattern() {
		return mPattern;
	}
	
	public int getOperator() {
		return mOperator;
	}

	@Override
	public boolean isMatch(DataRecord record) {
		String value = record.get(mName);

		switch (mOperator) {
		case OPERATOR_EQUALS:
			return getPattern().equals(value);
		case OPERATOR_STARTSWITH:
			return getPattern().startsWith(value);
		default:
			return false;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getName());
		dest.writeString(getPattern());
		dest.writeInt(getOperator());
	}

	public final static Parcelable.Creator<DataRecordFieldFilter> CREATOR = new Parcelable.Creator<DataRecordFieldFilter>() {

		@Override
		public DataRecordFieldFilter createFromParcel(Parcel source) {
			String name = source.readString();
			String pattern = source.readString();
			int operator = source.readInt();
			return new DataRecordFieldFilter(name, pattern, operator);
		}

		@Override
		public DataRecordFieldFilter[] newArray(int size) {
			return new DataRecordFieldFilter[size];
		}
	};

}
