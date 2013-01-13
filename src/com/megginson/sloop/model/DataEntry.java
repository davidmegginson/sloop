package com.megginson.sloop.model;

import java.util.Map;

/**
 * A single name=value pair in a data record.
 * 
 * TODO This is initially a naive implementation. It will evolve into something
 * more efficient, probably involving indexes into arrays.
 * 
 * @author David Megginson
 * @see DataCollection
 * @see DataRecord
 */
public class DataEntry implements Map.Entry<String, String> {

	private String key;
	private String value;

	public DataEntry(String key, String value) {
		this.key = key;
		setValue(value);
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String setValue(String value) {
		this.value = value;
		return value;
	}

}
