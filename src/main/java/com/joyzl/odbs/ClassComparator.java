package com.joyzl.odbs;

import java.util.Comparator;

final class ClassComparator implements Comparator<Class<?>> {

	@Override
	public int compare(Class<?> c1, Class<?> c2) {
		// 必须指定排序以确保服务端和客户端获得相同的排列方式
		return c1.getName().compareTo(c2.getName());
	}
}