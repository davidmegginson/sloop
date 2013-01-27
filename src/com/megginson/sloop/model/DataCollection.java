package com.megginson.sloop.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of data records (i.e. internal representation of a CSV file).
 * 
 * This represents tabular data: a list of lists, where the child lists are all
 * the same length. From a Java Collections point of view, this is a read-only
 * {@link List} of {@link DataRecord} objects. The list of headers is set at
 * creation time, and can't be changed afterwards. To add a row of new values,
 * use the {@link addRecord} objects.
 * 
 * @author David Megginson
 * @see DataRecord
 */
public class DataCollection extends AbstractList<DataRecord> {

	private String mHeader[];

	private List<String[]> mRecords = new ArrayList<String[]>();

	/**
	 * Default constructor.
	 * 
	 * Creates an empty collection.
	 */
	public DataCollection(String headers[]) {
		super();
		mHeader = headers;
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
		mHeader = dataCollection.mHeader.clone();
		for (String record[] : dataCollection.mRecords) {
			addRecord(record);
		}
	}

	public String[] getHeader() {
		return mHeader;
	}

	public void addRecord(String record[]) {
		mRecords.add(record.clone());
	}

	public String[] getRecord(int position) {
		return mRecords.get(position);
	}

	@Override
	public DataRecord get(int location) {
		return new DataRecord(mHeader, mRecords.get(location));
	}

	@Override
	public int size() {
		return mRecords.size();
	}

}
