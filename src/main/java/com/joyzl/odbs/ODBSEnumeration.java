/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

import java.lang.reflect.Array;

import com.joyzl.EnumCode;
import com.joyzl.EnumText;

/**
 * 枚举描述
 * 
 * @author ZhangXi
 * @date 2020年6月6日
 */
public final class ODBSEnumeration {

	private final int type;
	private final String name;
	private final boolean coded;
	private final Class<Enum<?>> CLASS;
	private final Enum<?>[] VALUES;

	public ODBSEnumeration(Class<Enum<?>> clazz, int idx) {
		CLASS = clazz;
		VALUES = clazz.getEnumConstants();
		name = clazz.getCanonicalName();

		type = idx;
		coded = ODBSReflect.isImplemented(clazz, EnumCode.class);
	}

	public final int getIndex() {
		return type;
	}

	public final String getName() {
		return name;
	}

	public final String getClassName() {
		return CLASS.getName();
	}

	public final Class<?> getSourceClass() {
		return CLASS;
	}

	/**
	 * 通过枚举值获取枚举常量
	 * 
	 * @param value
	 * @return
	 */
	public final Object getConstant(int value) {
		if (coded) {
			for (int index = 0; index < VALUES.length; index++) {
				if (((EnumCode) VALUES[index]).code() == value) {
					return VALUES[index];
				}
			}
			return null;
		} else {
			return VALUES[value];
		}
	}

	/**
	 * 通过枚举常量获取枚举代码值
	 * 
	 * @param value
	 * @return
	 */
	public final int getValue(Object value) {
		if (coded) {
			return ((EnumCode) value).code();
		} else {
			return ((Enum<?>) value).ordinal();
		}
	}

	/**
	 * 通过枚举常量获取枚举名称
	 * 
	 * @param value
	 * @return
	 */
	public final String getName(Object value) {
		return ((Enum<?>) value).name();
	}

	/**
	 * 通过枚举常量获取枚举文本
	 * 
	 * @param value
	 * @return
	 */
	public final String getText(Object value) {
		if (value instanceof EnumText) {
			return ((EnumText) value).text();
		} else {
			return null;
		}
	}

	/**
	 * 获取枚举定义的常量数量
	 * 
	 * @return 0~n
	 */
	public final int getValueCount() {
		return VALUES.length;
	}

	/**
	 * 枚举是否实现了自定义代码
	 * 
	 * @return true 实现了{@link EnumCode}接口 / false
	 */
	public final boolean isCoded() {
		return coded;
	}

	/**
	 * 创建对象数组实例
	 * 
	 * @return length 数组元素数量（数组长度）
	 */
	public final Object[] newArray(int length) {
		return (Object[]) Array.newInstance(CLASS, length);
	}

	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(type);
		builder.append('\t');
		builder.append(CLASS.getName());
		for (Enum<?> item : VALUES) {
			builder.append(',');
			builder.append(item);
		}
		return builder.toString();
	}
}
