package com.megginson.sloop.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of data records (i.e. a file).
 * 
 * TODO This is a naive implementation that simply wraps an ArrayList. It will
 * evolve into something more efficient for large data files, using arrays and
 * indices.
 * 
 * @author David Megginson
 * @see DataRecord
 * @see DataEntry
 */
public class DataCollection extends AbstractList<DataRecord> {

	private List<DataRecord> dataRecords = new ArrayList<DataRecord>();

	@Override
	public DataRecord get(int location) {
		return dataRecords.get(location);
	}

	@Override
	public int size() {
		return dataRecords.size();
	}

	@Override
	public DataRecord set(int location, DataRecord dataRecord) {
		return dataRecords.set(location, dataRecord);
	}

	@Override
	public void add(int location, DataRecord dataRecord) {
		dataRecords.add(location, dataRecord);
	}

	@Override
	public DataRecord remove(int location) {
		return dataRecords.remove(location);
	}

}
