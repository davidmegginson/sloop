package com.megginson.sloop.model.impl;

import android.annotation.SuppressLint;
import com.megginson.sloop.model.ValueFilter;

/**
 * Value filter to test case-insensitive string equality.
 * 
 * @author David Megginson
 */
@SuppressLint("DefaultLocale")
public class EqualsStringFilter implements ValueFilter {
	private String mValue;

	public EqualsStringFilter(String value) {
		mValue = value.toLowerCase();
	}

	@Override
	public boolean isMatch(String value) {
		return mValue.equals(value.toLowerCase());
	}
	
	public String toString() {
		return "=" + mValue;
	}

}
