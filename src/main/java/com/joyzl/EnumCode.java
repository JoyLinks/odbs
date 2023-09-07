/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 枚举自定义代码
 * 
 * @author ZhangXi 2020年6月6日
 */
public interface EnumCode {

	int code();

	/**
	 * 根据code值获取枚举实例
	 * 
	 * @param <T> 实现{@link EnumCode}接口的枚举实例
	 * @param clazz
	 * @param code
	 * @return 实例 / null
	 */
	static <T extends EnumCode> T valueOf(Class<T> clazz, int code) {
		final T[] items = clazz.getEnumConstants();
		for (int index = 0; index < items.length; index++) {
			if (items[index].code() == code) {
				return items[index];
			}
		}
		return null;
	}

	/**
	 * 根据name获取枚举实例
	 * 
	 * @param <T> 实现{@link EnumCode}接口的枚举实例
	 * @param clazz
	 * @param code
	 * @return 实例 / null
	 */
	static <T extends Enum<T>> T valueOf(Class<T> clazz, String name) {
		final T[] items = clazz.getEnumConstants();
		for (int index = 0; index < items.length; index++) {
			if (items[index].name().equalsIgnoreCase(name)) {
				return items[index];
			}
		}
		return null;
	}

	/**
	 * 根据聚合值获取枚举实例
	 * 
	 * @param <T> 实现{@link EnumCode}接口的枚举实例
	 * @param clazz
	 * @param value
	 * @return 实例 / null
	 */
	static <T extends EnumCode> T[] valuesOf(Class<T> clazz, int value) {
		final T[] items = clazz.getEnumConstants();
		@SuppressWarnings("unchecked")
		T[] values = (T[]) Array.newInstance(clazz, items.length);
		int size = 0;
		for (int index = 0; index < items.length; index++) {
			if ((items[index].code() & value) != 0) {
				values[size++] = items[index];
			}
		}
		return Arrays.copyOf(values, size);
	}
}