/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.odbs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 具有集合的实体
 * 
 * @author ZhangXi
 * @date 2023年7月27日
 */
public class EntityList {

	// 基础类型集合

	private List<Boolean> booleanObjects;
	private List<Byte> byteObjects;
	private List<Character> characterObjects;
	private List<Short> shortObjects;
	private List<Integer> integerObjects;
	private List<Long> longObjects;
	private List<Float> floatObjects;
	private List<Double> doubleObjects;

	private List<Date> dateObjects;
	private List<String> stringObjects;
	private List<LocalDate> localDateObjects;
	private List<LocalTime> localTimeObjects;
	private List<LocalDateTime> localDateTimeObjects;
	private List<BigDecimal> decimalObjects;

	// 枚举集合

	private List<EnumCodes> enumCodes;
	private List<EnumTexts> enumTexts;
	private List<EnumNative> enumNatives;
	private List<EnumCodeTexts> enumCodeTexts;

	// 对象集合

	private List<EntityBase> entityBases;
	private List<EntityEmpty> entityEmpties;

	static EntityList createNullValue() {
		final EntityList entity = new EntityList();

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

	static EntityList createEmptyValue() {
		final EntityList entity = new EntityList();

		entity.setBooleanObjects(new ArrayList<>());
		entity.setCharacterObjects(new ArrayList<>());
		entity.setByteObjects(new ArrayList<>());
		entity.setShortObjects(new ArrayList<>());
		entity.setIntegerObjects(new ArrayList<>());
		entity.setLongObjects(new ArrayList<>());
		entity.setFloatObjects(new ArrayList<>());
		entity.setDoubleObjects(new ArrayList<>());

		entity.setStringObjects(new ArrayList<>());
		entity.setDateObjects(new ArrayList<>());
		entity.setLocalDateObjects(new ArrayList<>());
		entity.setLocalTimeObjects(new ArrayList<>());
		entity.setLocalDateTimeObjects(new ArrayList<>());
		entity.setDecimalObjects(new ArrayList<>());

		entity.setEnumCodes(new ArrayList<>());
		entity.setEnumTexts(new ArrayList<>());
		entity.setEnumNatives(new ArrayList<>());
		entity.setEnumCodeTexts(new ArrayList<>());

		entity.setEntityBases(new ArrayList<>());
		entity.setEntityEmpties(new ArrayList<>());
		return entity;
	}

	static EntityList createNormalValue() {
		final EntityList entity = new EntityList();

		entity.setBooleanObjects(Arrays.asList(EntityArray.BOOLEAN_OBJECTS));
		entity.setCharacterObjects(Arrays.asList(EntityArray.CHARACTER_OBJECTS));
		entity.setByteObjects(Arrays.asList(EntityArray.BYTE_OBJECTS));
		entity.setShortObjects(Arrays.asList(EntityArray.SHORT_OBJECTS));
		entity.setIntegerObjects(Arrays.asList(EntityArray.INTEGER_OBJECTS));
		entity.setLongObjects(Arrays.asList(EntityArray.LONG_OBJECTS));
		entity.setFloatObjects(Arrays.asList(EntityArray.FLOAT_OBJECTS));
		entity.setDoubleObjects(Arrays.asList(EntityArray.DOUBLE_OBJECTS));

		entity.setStringObjects(Arrays.asList(EntityArray.STRING_OBJECTS));
		entity.setDateObjects(Arrays.asList(EntityArray.DATE_OBJECTS));
		entity.setLocalDateObjects(Arrays.asList(EntityArray.LOCALDATE_OBJECTS));
		entity.setLocalTimeObjects(Arrays.asList(EntityArray.LOCALTIME_OBJECTS));
		entity.setLocalDateTimeObjects(Arrays.asList(EntityArray.LOCALDATETIME_OBJECTS));
		entity.setDecimalObjects(Arrays.asList(EntityArray.BIGDECIMAL_OBJECTS));

		entity.setEnumCodes(Arrays.asList(EnumCodes.values()));
		entity.setEnumTexts(Arrays.asList(EnumTexts.values()));
		entity.setEnumNatives(Arrays.asList(EnumNative.values()));
		entity.setEnumCodeTexts(Arrays.asList(EnumCodeTexts.values()));

		entity.setEntityBases(Arrays.asList(EntityBase.createNullValue(), EntityBase.createMinValue(), EntityBase.createMaxValue()));
		entity.setEntityEmpties(Arrays.asList(new EntityEmpty(), new EntityEmpty(), new EntityEmpty()));
		return entity;
	}

	static void assertEntity(EntityList a, EntityList b) {
		assertEquals(a.getBooleanObjects(), b.getBooleanObjects());
		assertEquals(a.getByteObjects(), b.getByteObjects());
		assertEquals(a.getCharacterObjects(), b.getCharacterObjects());
		assertEquals(a.getShortObjects(), b.getShortObjects());
		assertEquals(a.getIntegerObjects(), b.getIntegerObjects());
		assertEquals(a.getLongObjects(), b.getLongObjects());
		assertEquals(a.getFloatObjects(), b.getFloatObjects());
		assertEquals(a.getDoubleObjects(), b.getDoubleObjects());

		assertEquals(a.getStringObjects(), b.getStringObjects());
		// assertEquals(a.getDateObjects(), b.getDateObjects());
		assertEquals(a.getLocalDateObjects(), b.getLocalDateObjects());
		// assertEquals(a.getLocalTimeObjects(), b.getLocalTimeObjects());
		// assertEquals(a.getLocalDateTimeObjects(),
		// b.getLocalDateTimeObjects());
		assertEquals(a.getDecimalObjects(), b.getDecimalObjects());

		assertEquals(a.getEnumCodes(), b.getEnumCodes());
		assertEquals(a.getEnumTexts(), b.getEnumTexts());
		assertEquals(a.getEnumNatives(), b.getEnumNatives());
		assertEquals(a.getEnumCodeTexts(), b.getEnumCodeTexts());

		if (a.getEntityBases() != null && b.getEntityBases() != null) {
			assertEquals(a.getEntityBases().size(), b.getEntityBases().size());
		} else {
			assertEquals(a.getEntityBases(), b.getEntityBases());
		}
		if (a.getEntityEmpties() != null && b.getEntityEmpties() != null) {
			assertEquals(a.getEntityEmpties().size(), b.getEntityEmpties().size());
		} else {
			assertEquals(a.getEntityEmpties(), b.getEntityEmpties());
		}
	}

	public List<Boolean> getBooleanObjects() {
		return booleanObjects;
	}

	public void setBooleanObjects(List<Boolean> values) {
		booleanObjects = values;
	}

	public List<Byte> getByteObjects() {
		return byteObjects;
	}

	public void setByteObjects(List<Byte> values) {
		byteObjects = values;
	}

	public List<Character> getCharacterObjects() {
		return characterObjects;
	}

	public void setCharacterObjects(List<Character> values) {
		characterObjects = values;
	}

	public List<Short> getShortObjects() {
		return shortObjects;
	}

	public void setShortObjects(List<Short> values) {
		shortObjects = values;
	}

	public List<Integer> getIntegerObjects() {
		return integerObjects;
	}

	public void setIntegerObjects(List<Integer> values) {
		integerObjects = values;
	}

	public List<Long> getLongObjects() {
		return longObjects;
	}

	public void setLongObjects(List<Long> values) {
		longObjects = values;
	}

	public List<Float> getFloatObjects() {
		return floatObjects;
	}

	public void setFloatObjects(List<Float> values) {
		floatObjects = values;
	}

	public List<Double> getDoubleObjects() {
		return doubleObjects;
	}

	public void setDoubleObjects(List<Double> values) {
		doubleObjects = values;
	}

	public List<String> getStringObjects() {
		return stringObjects;
	}

	public void setStringObjects(List<String> values) {
		stringObjects = values;
	}

	public List<Date> getDateObjects() {
		return dateObjects;
	}

	public void setDateObjects(List<Date> values) {
		dateObjects = values;
	}

	public List<LocalDate> getLocalDateObjects() {
		return localDateObjects;
	}

	public void setLocalDateObjects(List<LocalDate> values) {
		localDateObjects = values;
	}

	public List<LocalTime> getLocalTimeObjects() {
		return localTimeObjects;
	}

	public void setLocalTimeObjects(List<LocalTime> values) {
		localTimeObjects = values;
	}

	public List<LocalDateTime> getLocalDateTimeObjects() {
		return localDateTimeObjects;
	}

	public void setLocalDateTimeObjects(List<LocalDateTime> values) {
		localDateTimeObjects = values;
	}

	public List<BigDecimal> getDecimalObjects() {
		return decimalObjects;
	}

	public void setDecimalObjects(List<BigDecimal> values) {
		decimalObjects = values;
	}

	public List<EnumCodes> getEnumCodes() {
		return enumCodes;
	}

	public void setEnumCodes(List<EnumCodes> values) {
		enumCodes = values;
	}

	public List<EnumTexts> getEnumTexts() {
		return enumTexts;
	}

	public void setEnumTexts(List<EnumTexts> values) {
		enumTexts = values;
	}

	public List<EnumNative> getEnumNatives() {
		return enumNatives;
	}

	public void setEnumNatives(List<EnumNative> values) {
		enumNatives = values;
	}

	public List<EnumCodeTexts> getEnumCodeTexts() {
		return enumCodeTexts;
	}

	public void setEnumCodeTexts(List<EnumCodeTexts> values) {
		enumCodeTexts = values;
	}

	public List<EntityBase> getEntityBases() {
		return entityBases;
	}

	public void setEntityBases(List<EntityBase> values) {
		entityBases = values;
	}

	public List<EntityEmpty> getEntityEmpties() {
		return entityEmpties;
	}

	public void setEntityEmpties(List<EntityEmpty> values) {
		entityEmpties = values;
	}
}
