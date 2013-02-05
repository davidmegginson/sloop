package com.megginson.sloop.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class DataRecordFilter implements ListItemFilter<DataRecord>, Parcelable {

	public final static int OPERATOR_AND = 1;

	public final static int OPERATOR_OR = 2;

	private List<DataRecordFieldFilter> mFilters = new ArrayList<DataRecordFieldFilter>();

	private int mOperator;

	public DataRecordFilter() {
		this(OPERATOR_AND);
	}

	public DataRecordFilter(int operator) {
		mOperator = operator;
	}
	
	public int getOperator() {
		return mOperator;
	}
	
	public void addFilter (DataRecordFieldFilter filter) {
		mFilters.add(filter);
	}

	@Override
	public boolean isMatch(DataRecord item) {
		for (DataRecordFieldFilter filter : mFilters) {
			boolean result = filter.isMatch(item);
			switch (mOperator) {
			case OPERATOR_AND:
				// any negative result means failure
				if (!result) {
					return false;
				}
				break;
			case OPERATOR_OR:
				// any positive result means success
				if (result) {
					return true;
				}
				break;
			default:
				return false;
			}
		}

		// if we get to here, it's success for AND and failure for OR
		return (mOperator == OPERATOR_AND);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mOperator);
		dest.writeInt(mFilters.size());
		for (DataRecordFieldFilter filter : mFilters) {
			dest.writeParcelable(filter, 0);
		}
	}
	
	public final static Parcelable.Creator<DataRecordFilter> CREATOR = new Parcelable.Creator<DataRecordFilter>() {

		@Override
		public DataRecordFilter createFromParcel(Parcel source) {
			DataRecordFilter filter = new DataRecordFilter(source.readInt());
			int size = source.readInt();
			for (int i = 0; i < size; i++) {
				filter.addFilter((DataRecordFieldFilter)source
						.readParcelable(DataRecordFieldFilter.class.getClassLoader()));
			}
			return filter;
		}

		@Override
		public DataRecordFilter[] newArray(int size) {
			return new DataRecordFilter[size];
		}
	};


}
