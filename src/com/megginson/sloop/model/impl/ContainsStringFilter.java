package com.megginson.sloop.model.impl;

import android.annotation.SuppressLint;
import com.megginson.sloop.model.ValueFilter;

/**
 * Value filter to test for a case-insensitive substring.
 * 
 * @author David Megginson
 */
@SuppressLint("DefaultLocale")
public class ContainsStringFilter implements ValueFilter {

	private String mValueFragment;

	public ContainsStringFilter(String valueFragment) {
		mValueFragment = valueFragment.toLowerCase();
	}

	@Override
	public boolean isMatch(String value) {
		return value.toLowerCase().contains(mValueFragment);
	}
	
	public String toString() {
		return "~" + mValueFragment;
	}
	
}

