/*
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * 基础类型值实体
 * 
 * @author ZhangXi
 * @date 2023年7月27日
 */
public class EntityBase {

	// 值类型 8

	private boolean booleanValue;
	private byte byteValue;
	private char charValue;
	private short shortValue;
	private int intValue;
	private long longValue;
	private float floatValue;
	private double doubleValue;

	// 基础类型 14

	private Boolean booleanObject;
	private Byte byteObject;
	private Character characterObject;
	private Short shortObject;
	private Integer integerObject;
	private Long longObject;
	private Float floatObject;
	private Double doubleObject;

	private Date dateObject;
	private String stringObject;
	private LocalDate localDateObject;
	private LocalTime localTimeObject;
	private LocalDateTime localDateTimeObject;
	private BigDecimal decimalObject;

	// 枚举类型 4

	private EnumCodes enumCodes;
	private EnumTexts enumTexts;
	private EnumNative enumNative;
	private EnumCodeTexts enumCodeTexts;

	static EntityBase createNullValue() {
		final EntityBase entity = new EntityBase();
		// entity.setBooleanValue(null);
		// entity.setByteValue(null);
		// entity.setCharValue(null);
		// entity.setShortValue(null);
		// entity.setIntValue(null);
		// entity.setLongValue(null);
		// entity.setFloatValue(null);
		// entity.setDoubleValue(null);
		entity.setBooleanObject(null);
		entity.setCharacterObject(null);
		entity.setByteObject(null);
		entity.setShortObject(null);
		entity.setIntegerObject(null);
		entity.setLongObject(null);
		entity.setFloatObject(null);
		entity.setDoubleObject(null);
		entity.setStringObject(null);
		entity.setDateObject(null);
		entity.setLocalDateObject(null);
		entity.setLocalTimeObject(null);
		entity.setLocalDateTimeObject(null);
		entity.setDecimalObject(null);
		entity.setEnumCodes(null);
		entity.setEnumTexts(null);
		entity.setEnumNative(null);
		entity.setEnumCodeTexts(null);
		return entity;
	}

	static EntityBase createMinValue() {
		final EntityBase entity = new EntityBase();
		entity.setBooleanValue(Boolean.FALSE.booleanValue());
		entity.setByteValue(Byte.MIN_VALUE);
		entity.setCharValue(Character.MIN_VALUE);
		entity.setShortValue(Short.MIN_VALUE);
		entity.setIntValue(Integer.MIN_VALUE);
		entity.setLongValue(Long.MIN_VALUE);
		entity.setFloatValue(Float.MIN_VALUE);
		entity.setDoubleValue(Double.MIN_VALUE);

		entity.setBooleanObject(Boolean.FALSE);
		entity.setCharacterObject(Character.valueOf(Character.MIN_VALUE));
		entity.setByteObject(Byte.valueOf(Byte.MIN_VALUE));
		entity.setShortObject(Short.valueOf(Short.MIN_VALUE));
		entity.setIntegerObject(Integer.valueOf(Integer.MIN_VALUE));
		entity.setLongObject(Long.valueOf(Long.MIN_VALUE));
		entity.setFloatObject(Float.valueOf(Float.MIN_VALUE));
		entity.setDoubleObject(Double.valueOf(Double.MIN_VALUE));
		entity.setStringObject("");
		entity.setDateObject(new Date(0));
		entity.setLocalDateObject(LocalDate.MIN);
		entity.setLocalTimeObject(LocalTime.MIN);
		entity.setLocalDateTimeObject(LocalDateTime.MIN);
		entity.setDecimalObject(BigDecimal.ZERO);

		entity.setEnumCodes(EnumCodes.RECORD);
		entity.setEnumTexts(EnumTexts.RECORD);
		entity.setEnumNative(EnumNative.RECORD);
		entity.setEnumCodeTexts(EnumCodeTexts.RECORD);
		return entity;
	}

	static EntityBase createMaxValue() {
		final EntityBase entity = new EntityBase();
		entity.setBooleanValue(Boolean.TRUE);
		entity.setByteValue(Byte.MAX_VALUE);
		entity.setCharValue(Character.MAX_VALUE);
		entity.setShortValue(Short.MAX_VALUE);
		entity.setIntValue(Integer.MAX_VALUE);
		entity.setLongValue(Long.MAX_VALUE);
		entity.setFloatValue(Float.MAX_VALUE);
		entity.setDoubleValue(Double.MAX_VALUE);

		entity.setBooleanObject(Boolean.FALSE);
		entity.setCharacterObject(Character.valueOf(Character.MAX_VALUE));
		entity.setByteObject(Byte.valueOf(Byte.MAX_VALUE));
		entity.setShortObject(Short.valueOf(Short.MAX_VALUE));
		entity.setIntegerObject(Integer.valueOf(Integer.MAX_VALUE));
		entity.setLongObject(Long.valueOf(Long.MAX_VALUE));
		entity.setFloatObject(Float.valueOf(Float.MAX_VALUE));
		entity.setDoubleObject(Double.valueOf(Double.MAX_VALUE));
		entity.setStringObject("1234567890[{ABCDEFG]}:");
		entity.setDateObject(new Date());
		entity.setLocalDateObject(LocalDate.MAX);
		entity.setLocalTimeObject(LocalTime.MAX);
		entity.setLocalDateTimeObject(LocalDateTime.MAX);
		entity.setDecimalObject(BigDecimal.TEN);

		entity.setEnumCodes(EnumCodes.DANGER);
		entity.setEnumTexts(EnumTexts.DANGER);
		entity.setEnumNative(EnumNative.DANGER);
		entity.setEnumCodeTexts(EnumCodeTexts.DANGER);
		return entity;
	}

	static void assertEntity(EntityBase a, EntityBase b) {
		assertEquals(a.isBooleanValue(), b.isBooleanValue());
		assertEquals(a.getByteValue(), b.getByteValue());
		assertEquals(a.getCharValue(), b.getCharValue());
		assertEquals(a.getShortValue(), b.getShortValue());
		assertEquals(a.getIntValue(), b.getIntValue());
		assertEquals(a.getLongValue(), b.getLongValue());
		assertEquals(a.getFloatValue(), b.getFloatValue());
		assertEquals(a.getDoubleValue(), b.getDoubleValue());

		assertEquals(a.getBooleanObject(), b.getBooleanObject());
		assertEquals(a.getByteObject(), b.getByteObject());
		assertEquals(a.getCharacterObject(), b.getCharacterObject());
		assertEquals(a.getShortObject(), b.getShortObject());
		assertEquals(a.getIntegerObject(), b.getIntegerObject());
		assertEquals(a.getLongObject(), b.getLongObject());
		assertEquals(a.getFloatObject(), b.getFloatObject());
		assertEquals(a.getDoubleObject(), b.getDoubleObject());

		assertEquals(a.getStringObject(), b.getStringObject());
		if (a.getDateObject() != null && b.getDateObject() != null) {
			final Calendar ca = Calendar.getInstance();
			final Calendar cb = Calendar.getInstance();
			ca.setTime(a.getDateObject());
			cb.setTime(b.getDateObject());
			assertEquals(ca.get(Calendar.YEAR), cb.get(Calendar.YEAR));
			assertEquals(ca.get(Calendar.MONTH), cb.get(Calendar.MONTH));
			assertEquals(ca.get(Calendar.DATE), cb.get(Calendar.DATE));
			assertEquals(ca.get(Calendar.HOUR), cb.get(Calendar.HOUR));
			assertEquals(ca.get(Calendar.MINUTE), cb.get(Calendar.MINUTE));
			assertEquals(ca.get(Calendar.SECOND), cb.get(Calendar.SECOND));
			// JSON 序列化丢失毫秒
		} else {
			assertEquals(a.getDateObject(), b.getDateObject());
		}
		assertEquals(a.getLocalDateObject(), b.getLocalDateObject());
		if (a.getDateObject() != null && b.getDateObject() != null) {
			assertEquals(a.getLocalTimeObject().getHour(), b.getLocalTimeObject().getHour());
			assertEquals(a.getLocalTimeObject().getMinute(), b.getLocalTimeObject().getMinute());
			assertEquals(a.getLocalTimeObject().getSecond(), b.getLocalTimeObject().getSecond());
			// JSON 序列化丢失毫秒
		} else {
			assertEquals(a.getLocalTimeObject(), b.getLocalTimeObject());
		}
		if (a.getLocalDateTimeObject() != null && b.getLocalDateTimeObject() != null) {
			assertEquals(a.getLocalDateTimeObject().getYear(), b.getLocalDateTimeObject().getYear());
			assertEquals(a.getLocalDateTimeObject().getMonth(), b.getLocalDateTimeObject().getMonth());
			assertEquals(a.getLocalDateTimeObject().getDayOfMonth(), b.getLocalDateTimeObject().getDayOfMonth());

			assertEquals(a.getLocalDateTimeObject().getHour(), b.getLocalDateTimeObject().getHour());
			assertEquals(a.getLocalDateTimeObject().getMinute(), b.getLocalDateTimeObject().getMinute());
			assertEquals(a.getLocalDateTimeObject().getSecond(), b.getLocalDateTimeObject().getSecond());
			// JSON 序列化丢失毫秒
		} else {
			assertEquals(a.getLocalDateTimeObject(), b.getLocalDateTimeObject());
		}
		assertEquals(a.getDecimalObject(), b.getDecimalObject());

		assertEquals(a.getEnumCodes(), b.getEnumCodes());
		assertEquals(a.getEnumTexts(), b.getEnumTexts());
		assertEquals(a.getEnumNative(), b.getEnumNative());
		assertEquals(a.getEnumCodeTexts(), b.getEnumCodeTexts());
	}

	@Override
	public boolean equals(Object o) {
		return o != null;
	}

	public boolean isBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(boolean value) {
		booleanValue = value;
	}

	public byte getByteValue() {
		return byteValue;
	}

	public void setByteValue(byte value) {
		byteValue = value;
	}

	public char getCharValue() {
		return charValue;
	}

	public void setCharValue(char value) {
		charValue = value;
	}

	public short getShortValue() {
		return shortValue;
	}

	public void setShortValue(short value) {
		shortValue = value;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int value) {
		intValue = value;
	}

	public long getLongValue() {
		return longValue;
	}

	public void setLongValue(long value) {
		longValue = value;
	}

	public float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(float value) {
		floatValue = value;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double value) {
		doubleValue = value;
	}

	public Boolean getBooleanObject() {
		return booleanObject;
	}

	public void setBooleanObject(Boolean value) {
		booleanObject = value;
	}

	public Byte getByteObject() {
		return byteObject;
	}

	public void setByteObject(Byte value) {
		byteObject = value;
	}

	public Character getCharacterObject() {
		return characterObject;
	}

	public void setCharacterObject(Character value) {
		characterObject = value;
	}

	public Short getShortObject() {
		return shortObject;
	}

	public void setShortObject(Short value) {
		shortObject = value;
	}

	public Integer getIntegerObject() {
		return integerObject;
	}

	public void setIntegerObject(Integer value) {
		integerObject = value;
	}

	public Long getLongObject() {
		return longObject;
	}

	public void setLongObject(Long value) {
		longObject = value;
	}

	public Float getFloatObject() {
		return floatObject;
	}

	public void setFloatObject(Float value) {
		floatObject = value;
	}

	public Double getDoubleObject() {
		return doubleObject;
	}

	public void setDoubleObject(Double value) {
		doubleObject = value;
	}

	public String getStringObject() {
		return stringObject;
	}

	public void setStringObject(String value) {
		stringObject = value;
	}

	public Date getDateObject() {
		return dateObject;
	}

	public void setDateObject(Date value) {
		dateObject = value;
	}

	public LocalDate getLocalDateObject() {
		return localDateObject;
	}

	public void setLocalDateObject(LocalDate value) {
		localDateObject = value;
	}

	public LocalTime getLocalTimeObject() {
		return localTimeObject;
	}

	public void setLocalTimeObject(LocalTime value) {
		localTimeObject = value;
	}

	public LocalDateTime getLocalDateTimeObject() {
		return localDateTimeObject;
	}

	public void setLocalDateTimeObject(LocalDateTime value) {
		localDateTimeObject = value;
	}

	public BigDecimal getDecimalObject() {
		return decimalObject;
	}

	public void setDecimalObject(BigDecimal value) {
		decimalObject = value;
	}

	public EnumCodes getEnumCodes() {
		return enumCodes;
	}

	public void setEnumCodes(EnumCodes value) {
		enumCodes = value;
	}

	public EnumTexts getEnumTexts() {
		return enumTexts;
	}

	public void setEnumTexts(EnumTexts value) {
		enumTexts = value;
	}

	public EnumNative getEnumNative() {
		return enumNative;
	}

	public void setEnumNative(EnumNative value) {
		enumNative = value;
	}

	public EnumCodeTexts getEnumCodeTexts() {
		return enumCodeTexts;
	}

	public void setEnumCodeTexts(EnumCodeTexts value) {
		enumCodeTexts = value;
	}
}
