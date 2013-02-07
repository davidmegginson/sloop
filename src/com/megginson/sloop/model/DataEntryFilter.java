package com.megginson.sloop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.megginson.sloop.util.FilteredList;
import com.megginson.sloop.util.ListItemFilter;

/**
 * Test a data entry for a {@link FilteredList}
 * 
 * Currently, this filter tests only equality.
 * 
 * @author David Megginson
 */
public class DataEntryFilter implements ListItemFilter<DataEntry>, Parcelable {

	private String mName;

	private String mPattern;

	public DataEntryFilter(String name, String pattern) {
		setName(name);
		setValue(pattern);
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

	@Override
	public boolean isMatch(DataEntry item) {
		return (mName.equals(item.getKey()) && mPattern.equals(item.getValue()));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(mPattern);
	}

	public final static Parcelable.Creator<DataEntryFilter> CREATOR = new Parcelable.Creator<DataEntryFilter>() {

		@Override
		public DataEntryFilter createFromParcel(Parcel source) {
			String name = source.readString();
			String pattern = source.readString();
			return new DataEntryFilter(name, pattern);
		}

		@Override
		public DataEntryFilter[] newArray(int size) {
			return new DataEntryFilter[size];
		}
	};
}
