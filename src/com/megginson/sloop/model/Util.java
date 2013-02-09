package com.megginson.sloop.model;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Android-independent static utility methods.
 */
public class Util {

	/**
	 * Null-safe hash code calculation.
	 * 
	 * This is a convenience method to calculate the hash code of an object. If
	 * the parameter is null, the method will return a hash code of 0 rather
	 * than throwing a {@link NullPointerException}.
	 * 
	 * This method is useful for implementing the {@link Object#hashCode()}
	 * method in a class across a series of embedded objects that might have
	 * null values.
	 * 
	 * @param o
	 *            the object from which to obtain a hash code (may be null).
	 * @return the object's hash code, or 0 if the argument is null.
	 */
	public static int hashCode(Object o) {
		if (o == null) {
			return 0;
		} else {
			return o.hashCode();
		}
	}

	/**
	 * Null-safe equality test.
	 * 
	 * This is a convenience method to test whether two objects are equal.
	 * Either may be null, without triggering a {@link NullPointerException}; if
	 * both are null, this method will consider them equal.
	 * 
	 * @param o1
	 *            the first object to test for equality (may be null).
	 * @param o2
	 *            the second object to test for equality (may be null).
	 * @return true if the two objects are equal, or if both are null.
	 */
	public static boolean equals(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		} else if (o1 == null || o2 == null) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}
	
	/**
	 * Test whether a string is a properly-formed URL.
	 * 
	 * @param s the string to test.
	 * @return true if the string is a URL.
	 */
	public static boolean isUrl (String s) {
		try {
			// let Java do the work
			new URL(s);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}

}
