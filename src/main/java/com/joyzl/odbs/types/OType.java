package com.joyzl.odbs.types;

public class OType {

	private final Class<?> CLASS;
	private final int index;

	public OType(Class<?> c, int i) {
		CLASS = c;
		index = i;
	}

	public Class<?> source() {
		return CLASS;
	}

	public int index() {
		return index;
	}
}