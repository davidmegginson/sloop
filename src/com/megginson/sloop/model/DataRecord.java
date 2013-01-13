package com.megginson.sloop.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A data record, consisting of name/value pairs.
 * 
 * TODO This is initially just a naive implementation wrapping an ArrayList. It
 * will evolve into something more memory-efficient later.
 * 
 * TODO should this be a {@link Map}?
 * 
 * @author David Megginson
 * @see DataCollection
 * @see DataEntry
 */
public class DataRecord extends AbstractList<DataEntry> {

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

}
