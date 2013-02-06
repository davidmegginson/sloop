package com.megginson.sloop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.megginson.sloop.util.FilteredList;
import com.megginson.sloop.util.ListItemFilter;

/**
 * Test a data entry for a {@link FilteredList}
 * 
 * @author David Megginson
 */
public class DataEntryFilter implements ListItemFilter<DataEntry>, Parcelable {

	public final static int OPERATOR_EQUALS = 1;
	public final static int OPERATOR_NOTEQUALS = 2;
	public final static int OPERATOR_STARTSWITH = 3;

	private String mName;

	private String mPattern;

	private int mOperator;

	public DataEntryFilter(String name, String pattern, int operator) {
		setName(name);
		setValue(pattern);
		setOperator(operator);
	}

	public String getName() {
		return mName;
	}

	protected void setName(String name) {
		mName = name;
	}

	public String getPattern() {
		return mPattern;
	}

	public void setValue(String pattern) {
		mPattern = pattern;
	}

	public int getOperator() {
		return mOperator;
	}

	public void setOperator(int operator) {
		mOperator = operator;
	}

	@Override
	public boolean isMatch(DataEntry item) {
		if (mName.equals(item.getKey())) {
			switch (mOperator) {
			case OPERATOR_EQUALS:
				return item.getValue().equals(mPattern);
			case OPERATOR_NOTEQUALS:
				return item.getValue().equals(mPattern);
			case OPERATOR_STARTSWITH:
				return item.getValue().startsWith(mPattern);
			}
		}
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mOperator);
		dest.writeString(mName);
		dest.writeString(mPattern);
	}

	public final static Parcelable.Creator<DataEntryFilter> CREATOR = new Parcelable.Creator<DataEntryFilter>() {

		@Override
		public DataEntryFilter createFromParcel(Parcel source) {
			int operator = source.readInt();
			String name = source.readString();
			String pattern = source.readString();
			return new DataEntryFilter(name, pattern, operator);
		}

		@Override
		public DataEntryFilter[] newArray(int size) {
			return new DataEntryFilter[size];
		}
	};
}
