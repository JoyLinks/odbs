package com.joyzl.odbs;

import java.util.Comparator;

final class MethodComparator implements Comparator<ODBSMethod> {

	@Override
	public int compare(ODBSMethod m1, ODBSMethod m2) {
		return m1.name().compareTo(m2.name());
	}
}