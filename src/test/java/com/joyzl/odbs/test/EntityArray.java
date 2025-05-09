/*
 * www.joyzl.net
 * 重庆骄智科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 具有数组的实体
 * 
 * @author ZhangXi
 * @date 2023年7月27日
 */
public class EntityArray {

	static final boolean[] BOOLEAN_VALUES = new boolean[] { true, false };
	static final byte[] BYTE_VALUES = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, Byte.MIN_VALUE, Byte.MAX_VALUE };
	static final char[] CHAR_VALUES = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	static final short[] SHORT_VALUES = new short[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, Short.MIN_VALUE, Short.MAX_VALUE };
	static final int[] INT_VALUES = new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, Integer.MIN_VALUE, Integer.MAX_VALUE };
	static final long[] LONG_VALUES = new long[] { 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, Long.MIN_VALUE, Long.MAX_VALUE };
	static final float[] FLOAT_VALUES = new float[] { 100.002F, Float.MIN_VALUE, Float.MAX_VALUE };
	static final double[] DOUBLE_VALUES = new double[] { 100.002D, Double.MIN_VALUE, Double.MAX_VALUE };

	static final Boolean[] BOOLEAN_OBJECTS = new Boolean[] { Boolean.FALSE, Boolean.TRUE };
	static final Character[] CHARACTER_OBJECTS = new Character[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', Character.MAX_VALUE };
	static final Byte[] BYTE_OBJECTS = new Byte[] { 0x04, Byte.MIN_VALUE, Byte.MAX_VALUE };
	static final Short[] SHORT_OBJECTS = new Short[] { -120, Short.MIN_VALUE, Short.MAX_VALUE };
	static final Integer[] INTEGER_OBJECTS = new Integer[] { 100, Integer.MIN_VALUE, Integer.MAX_VALUE };
	static final Long[] LONG_OBJECTS = new Long[] { 1000L, Long.MIN_VALUE, Long.MAX_VALUE };
	static final Float[] FLOAT_OBJECTS = new Float[] { 100.002F, Float.MIN_VALUE, Float.MAX_VALUE };
	static final Double[] DOUBLE_OBJECTS = new Double[] { 100.002D, Double.MIN_VALUE, Double.MAX_VALUE };

	static final String[] STRING_OBJECTS = new String[] { "", "中华人民共和国" ,"123,[{ABC]}:"};
	static final Date[] DATE_OBJECTS = new Date[] { new Date(), new Date(), new Date() };
	static final LocalDate[] LOCALDATE_OBJECTS = new LocalDate[] { LocalDate.now(), LocalDate.MIN, LocalDate.MAX };
	static final LocalTime[] LOCALTIME_OBJECTS = new LocalTime[] { LocalTime.now(), LocalTime.MIN, LocalTime.MAX };
	static final LocalDateTime[] LOCALDATETIME_OBJECTS = new LocalDateTime[] { LocalDateTime.now(), LocalDateTime.MIN, LocalDateTime.MAX };
	static final BigDecimal[] BIGDECIMAL_OBJECTS = new BigDecimal[] { BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN };

	// 值类型数组

	private boolean[] booleanValues;
	private byte[] byteValues;
	private char[] charValues;
	private short[] shortValues;
	private int[] intValues;
	private long[] longValues;
	private float[] floatValues;
	private double[] doubleValues;

	// 基础类型数组

	private Boolean[] booleanObjects;
	private Byte[] byteObjects;
	private Character[] characterObjects;
	private Short[] shortObjects;
	private Integer[] integerObjects;
	private Long[] longObjects;
	private Float[] floatObjects;
	private Double[] doubleObjects;

	private Date[] dateObjects;
	private String[] stringObjects;
	private LocalDate[] localDateObjects;
	private LocalTime[] localTimeObjects;
	private LocalDateTime[] localDateTimeObjects;
	private BigDecimal[] decimalObjects;

	// 枚举数组

	private EnumCodes[] enumCodes;
	private EnumTexts[] enumTexts;
	private EnumNative[] enumNatives;
	private EnumCodeTexts[] enumCodeTexts;

	// 对象数组

	private EntityBase[] entityBases;
	private EntityEmpty[] entityEmpties;

	static EntityArray createNullValue() {
		final EntityArray entity = new EntityArray();
		entity.setBooleanValues(null);
		entity.setByteValues(null);
		entity.setCharValues(null);
		entity.setShortValues(null);
		entity.setIntValues(null);
		entity.setLongValues(null);
		entity.setFloatValues(null);
		entity.setDoubleValues(null);

		entity.setBooleanObjects(null);
		entity.setCharacterObjects(null);
		entity.setByteObjects(null);
		entity.setShortObjects(null);
		entity.setIntegerObjects(null);
		entity.setLongObjects(null);
		entity.setFloatObjects(null);
		entity.setDoubleObjects(null);

		entity.setStringObjects(null);
		entity.setDateObjects(null);
		entity.setLocalDateObjects(null);
		entity.setLocalTimeObjects(null);
		entity.setLocalDateTimeObjects(null);
		entity.setDecimalObjects(null);

		entity.setEnumCodes(null);
		entity.setEnumTexts(null);
		entity.setEnumNatives(null);
		entity.setEnumCodeTexts(null);

		entity.setEntityBases(null);
		entity.setEntityEmpties(null);
		return entity;
	}

	static EntityArray createEmptyValue() {
		final EntityArray entity = new EntityArray();
		entity.setBooleanValues(new boolean[0]);
		entity.setByteValues(new byte[0]);
		entity.setCharValues(new char[0]);
		entity.setShortValues(new short[0]);
		entity.setIntValues(new int[0]);
		entity.setLongValues(new long[0]);
		entity.setFloatValues(new float[0]);
		entity.setDoubleValues(new double[0]);

		entity.setBooleanObjects(new Boolean[0]);
		entity.setCharacterObjects(new Character[0]);
		entity.setByteObjects(new Byte[0]);
		entity.setShortObjects(new Short[0]);
		entity.setIntegerObjects(new Integer[0]);
		entity.setLongObjects(new Long[0]);
		entity.setFloatObjects(new Float[0]);
		entity.setDoubleObjects(new Double[0]);

		entity.setStringObjects(new String[0]);
		entity.setDateObjects(new Date[0]);
		entity.setLocalDateObjects(new LocalDate[0]);
		entity.setLocalTimeObjects(new LocalTime[0]);
		entity.setLocalDateTimeObjects(new LocalDateTime[0]);
		entity.setDecimalObjects(new BigDecimal[0]);

		entity.setEnumCodes(new EnumCodes[0]);
		entity.setEnumTexts(new EnumTexts[0]);
		entity.setEnumNatives(new EnumNative[0]);
		entity.setEnumCodeTexts(new EnumCodeTexts[0]);

		entity.setEntityBases(new EntityBase[0]);
		entity.setEntityEmpties(new EntityEmpty[0]);
		return entity;
	}

	static EntityArray createNormalValue() {
		final EntityArray entity = new EntityArray();
		entity.setBooleanValues(BOOLEAN_VALUES);
		entity.setByteValues(BYTE_VALUES);
		entity.setCharValues(CHAR_VALUES);
		entity.setShortValues(SHORT_VALUES);
		entity.setIntValues(INT_VALUES);
		entity.setLongValues(LONG_VALUES);
		entity.setFloatValues(FLOAT_VALUES);
		entity.setDoubleValues(DOUBLE_VALUES);

		entity.setBooleanObjects(BOOLEAN_OBJECTS);
		entity.setCharacterObjects(CHARACTER_OBJECTS);
		entity.setByteObjects(BYTE_OBJECTS);
		entity.setShortObjects(SHORT_OBJECTS);
		entity.setIntegerObjects(INTEGER_OBJECTS);
		entity.setLongObjects(LONG_OBJECTS);
		entity.setFloatObjects(FLOAT_OBJECTS);
		entity.setDoubleObjects(DOUBLE_OBJECTS);

		entity.setStringObjects(STRING_OBJECTS);
		entity.setDateObjects(DATE_OBJECTS);
		entity.setLocalDateObjects(LOCALDATE_OBJECTS);
		entity.setLocalTimeObjects(LOCALTIME_OBJECTS);
		entity.setLocalDateTimeObjects(LOCALDATETIME_OBJECTS);
		entity.setDecimalObjects(BIGDECIMAL_OBJECTS);

		entity.setEnumCodes(EnumCodes.values());
		entity.setEnumTexts(EnumTexts.values());
		entity.setEnumNatives(EnumNative.values());
		entity.setEnumCodeTexts(EnumCodeTexts.values());

		entity.setEntityBases(new EntityBase[] { EntityBase.createNullValue(), EntityBase.createMinValue(), EntityBase.createMaxValue() });
		entity.setEntityEmpties(new EntityEmpty[] { new EntityEmpty(), new EntityEmpty() });
		return entity;
	}
	
	static void assertEntity(EntityArray a, EntityArray b) {
		assertArrayEquals(a.isBooleanValues(), b.isBooleanValues());
		assertArrayEquals(a.getByteValues(), b.getByteValues());
		assertArrayEquals(a.getCharValues(), b.getCharValues());
		assertArrayEquals(a.getShortValues(), b.getShortValues());
		assertArrayEquals(a.getIntValues(), b.getIntValues());
		assertArrayEquals(a.getLongValues(), b.getLongValues());
		assertArrayEquals(a.getFloatValues(), b.getFloatValues());
		assertArrayEquals(a.getDoubleValues(), b.getDoubleValues());

		assertArrayEquals(a.getBooleanObjects(), b.getBooleanObjects());
		assertArrayEquals(a.getByteObjects(), b.getByteObjects());
		assertArrayEquals(a.getCharacterObjects(), b.getCharacterObjects());
		assertArrayEquals(a.getShortObjects(), b.getShortObjects());
		assertArrayEquals(a.getIntegerObjects(), b.getIntegerObjects());
		assertArrayEquals(a.getLongObjects(), b.getLongObjects());
		assertArrayEquals(a.getFloatObjects(), b.getFloatObjects());
		assertArrayEquals(a.getDoubleObjects(), b.getDoubleObjects());

		assertArrayEquals(a.getStringObjects(), b.getStringObjects());
		// assertArrayEquals(a.getDateObjects(), b.getDateObjects());
		assertArrayEquals(a.getLocalDateObjects(), b.getLocalDateObjects());
		// assertArrayEquals(a.getLocalTimeObjects(), b.getLocalTimeObjects());
		// assertArrayEquals(a.getLocalDateTimeObjects(),
		// b.getLocalDateTimeObjects());
		assertArrayEquals(a.getDecimalObjects(), b.getDecimalObjects());

		assertArrayEquals(a.getEnumCodes(), b.getEnumCodes());
		assertArrayEquals(a.getEnumTexts(), b.getEnumTexts());
		assertArrayEquals(a.getEnumNatives(), b.getEnumNatives());
		assertArrayEquals(a.getEnumCodeTexts(), b.getEnumCodeTexts());

		if (a.getEntityBases() != null && b.getEntityBases() != null) {
			assertEquals(a.getEntityBases().length, b.getEntityBases().length);
			for (int index = 0; index < a.getEntityBases().length; index++) {
				EntityBase.assertEntity(a.getEntityBases()[index], b.getEntityBases()[index]);
			}
		}

		if (a.getEntityEmpties() != null && b.getEntityEmpties() != null) {
			assertEquals(a.getEntityEmpties().length, b.getEntityEmpties().length);
		}
	}

	public boolean[] isBooleanValues() {
		return booleanValues;
	}

	public void setBooleanValues(boolean[] values) {
		booleanValues = values;
	}

	public byte[] getByteValues() {
		return byteValues;
	}

	public void setByteValues(byte[] values) {
		byteValues = values;
	}

	public char[] getCharValues() {
		return charValues;
	}

	public void setCharValues(char[] values) {
		charValues = values;
	}

	public short[] getShortValues() {
		return shortValues;
	}

	public void setShortValues(short[] values) {
		shortValues = values;
	}

	public int[] getIntValues() {
		return intValues;
	}

	public void setIntValues(int[] values) {
		intValues = values;
	}

	public long[] getLongValues() {
		return longValues;
	}

	public void setLongValues(long[] values) {
		longValues = values;
	}

	public float[] getFloatValues() {
		return floatValues;
	}

	public void setFloatValues(float[] values) {
		floatValues = values;
	}

	public double[] getDoubleValues() {
		return doubleValues;
	}

	public void setDoubleValues(double[] values) {
		doubleValues = values;
	}

	public Boolean[] getBooleanObjects() {
		return booleanObjects;
	}

	public void setBooleanObjects(Boolean[] values) {
		booleanObjects = values;
	}

	public Byte[] getByteObjects() {
		return byteObjects;
	}

	public void setByteObjects(Byte[] values) {
		byteObjects = values;
	}

	public Character[] getCharacterObjects() {
		return characterObjects;
	}

	public void setCharacterObjects(Character[] values) {
		characterObjects = values;
	}

	public Short[] getShortObjects() {
		return shortObjects;
	}

	public void setShortObjects(Short[] values) {
		shortObjects = values;
	}

	public Integer[] getIntegerObjects() {
		return integerObjects;
	}

	public void setIntegerObjects(Integer[] values) {
		integerObjects = values;
	}

	public Long[] getLongObjects() {
		return longObjects;
	}

	public void setLongObjects(Long[] values) {
		longObjects = values;
	}

	public Float[] getFloatObjects() {
		return floatObjects;
	}

	public void setFloatObjects(Float[] values) {
		floatObjects = values;
	}

	public Double[] getDoubleObjects() {
		return doubleObjects;
	}

	public void setDoubleObjects(Double[] values) {
		doubleObjects = values;
	}

	public String[] getStringObjects() {
		return stringObjects;
	}

	public void setStringObjects(String[] values) {
		stringObjects = values;
	}

	public Date[] getDateObjects() {
		return dateObjects;
	}

	public void setDateObjects(Date[] values) {
		dateObjects = values;
	}

	public LocalDate[] getLocalDateObjects() {
		return localDateObjects;
	}

	public void setLocalDateObjects(LocalDate[] values) {
		localDateObjects = values;
	}

	public LocalTime[] getLocalTimeObjects() {
		return localTimeObjects;
	}

	public void setLocalTimeObjects(LocalTime[] values) {
		localTimeObjects = values;
	}

	public LocalDateTime[] getLocalDateTimeObjects() {
		return localDateTimeObjects;
	}

	public void setLocalDateTimeObjects(LocalDateTime[] values) {
		localDateTimeObjects = values;
	}

	public BigDecimal[] getDecimalObjects() {
		return decimalObjects;
	}

	public void setDecimalObjects(BigDecimal[] values) {
		decimalObjects = values;
	}

	public EnumCodes[] getEnumCodes() {
		return enumCodes;
	}

	public void setEnumCodes(EnumCodes[] values) {
		enumCodes = values;
	}

	public EnumTexts[] getEnumTexts() {
		return enumTexts;
	}

	public void setEnumTexts(EnumTexts[] values) {
		enumTexts = values;
	}

	public EnumNative[] getEnumNatives() {
		return enumNatives;
	}

	public void setEnumNatives(EnumNative[] values) {
		enumNatives = values;
	}

	public EnumCodeTexts[] getEnumCodeTexts() {
		return enumCodeTexts;
	}

	public void setEnumCodeTexts(EnumCodeTexts[] values) {
		enumCodeTexts = values;
	}

	public EntityBase[] getEntityBases() {
		return entityBases;
	}

	public void setEntityBases(EntityBase[] values) {
		entityBases = values;
	}

	public EntityEmpty[] getEntityEmpties() {
		return entityEmpties;
	}

	public void setEntityEmpties(EntityEmpty[] values) {
		entityEmpties = values;
	}
}
