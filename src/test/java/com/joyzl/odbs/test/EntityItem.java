/*
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs.test;

public class EntityItem {

	// 值类型

	private boolean booleanValue;
	private int intValue;

	// 基础类型

	private String stringObject;

	// 枚举类型

	private EnumCodeTexts enumObject;

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean value) {
		booleanValue = value;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int value) {
		intValue = value;
	}

	public EnumCodeTexts getEnumObject() {
		return enumObject;
	}

	public void setEnumObject(EnumCodeTexts value) {
		enumObject = value;
	}

	public String getStringObject() {
		return stringObject;
	}

	public void setStringObject(String value) {
		stringObject = value;
	}
}
