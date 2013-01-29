package com.megginson.sloop.model;

public class ModelUtil {
	
	public static int hashCode(Object o) {
		if (o == null) {
			return 0;
		} else {
			return o.hashCode();
		}
	}
	
	public static boolean equals(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		} else if (o1 == null || o2 == null) {
			return false;
		} else {
			return o1.equals(o2);
		}
	}

}
