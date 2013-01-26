package com.megginson.sloop.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of data records (i.e. internal representation of a CSV file).
 * 
 * @author David Megginson
 * @see DataRecord
 * @see DataEntry
 */
public class DataCollection extends AbstractList<DataRecord> {

	private List<DataRecord> mDataRecords = new ArrayList<DataRecord>();

	/**
	 * Default constructor.
	 * 
	 * Creates an empty collection.
	 */
	public DataCollection() {
		super();
	}

	/**
	 * Copy constructor.
	 * 
	 * Makes a deep copy.
	 * 
	 * @param dataCollection
	 *            The data collection to copy.
	 */
	public DataCollection(DataCollection dataCollection) {
		super();
		for (DataRecord dataRecord : dataCollection) {
			add(new DataRecord(dataRecord));
		}
	}

	@Override
	public DataRecord get(int location) {
		return mDataRecords.get(location);
	}

	@Override
	public int size() {
		return mDataRecords.size();
	}

	@Override
	public DataRecord set(int location, DataRecord dataRecord) {
		return mDataRecords.set(location, dataRecord);
	}

	@Override
	public void add(int location, DataRecord dataRecord) {
		mDataRecords.add(location, dataRecord);
	}

	@Override
	public DataRecord remove(int location) {
		return mDataRecords.remove(location);
	}

}
