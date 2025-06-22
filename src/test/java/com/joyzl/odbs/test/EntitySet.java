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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 具有集合的实体
 * 
 * @author ZhangXi
 * @date 2023年7月27日
 */
public class EntitySet {

	// 基础类型集合

	private Set<Boolean> booleanObjects;
	private Set<Byte> byteObjects;
	private Set<Character> characterObjects;
	private Set<Short> shortObjects;
	private Set<Integer> integerObjects;
	private Set<Long> longObjects;
	private Set<Float> floatObjects;
	private Set<Double> doubleObjects;

	private Set<Date> dateObjects;
	private Set<String> stringObjects;
	private Set<LocalDate> localDateObjects;
	private Set<LocalTime> localTimeObjects;
	private Set<LocalDateTime> localDateTimeObjects;
	private Set<BigDecimal> decimalObjects;

	// 枚举集合

	private Set<EnumCodes> enumCodes;
	private Set<EnumTexts> enumTexts;
	private Set<EnumNative> enumNatives;
	private Set<EnumCodeTexts> enumCodeTexts;

	// 对象集合

	private Set<EntityBase> entityBases;
	private Set<EntityEmpty> entityEmpties;

	static EntitySet createNullValue() {
		final EntitySet entity = new EntitySet();

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

	static EntitySet createEmptyValue() {
		final EntitySet entity = new EntitySet();

		entity.setBooleanObjects(new HashSet<>());
		entity.setCharacterObjects(new HashSet<>());
		entity.setByteObjects(new HashSet<>());
		entity.setShortObjects(new HashSet<>());
		entity.setIntegerObjects(new HashSet<>());
		entity.setLongObjects(new HashSet<>());
		entity.setFloatObjects(new HashSet<>());
		entity.setDoubleObjects(new HashSet<>());

		entity.setStringObjects(new HashSet<>());
		entity.setDateObjects(new HashSet<>());
		entity.setLocalDateObjects(new HashSet<>());
		entity.setLocalTimeObjects(new HashSet<>());
		entity.setLocalDateTimeObjects(new HashSet<>());
		entity.setDecimalObjects(new HashSet<>());

		entity.setEnumCodes(new HashSet<>());
		entity.setEnumTexts(new HashSet<>());
		entity.setEnumNatives(new HashSet<>());
		entity.setEnumCodeTexts(new HashSet<>());

		entity.setEntityBases(new HashSet<>());
		entity.setEntityEmpties(new HashSet<>());
		return entity;
	}

	static EntitySet createNormalValue() {
		final EntitySet entity = new EntitySet();

		entity.setBooleanObjects(new HashSet<>(Arrays.asList(EntityArray.BOOLEAN_OBJECTS)));
		entity.setCharacterObjects(new HashSet<>(Arrays.asList(EntityArray.CHARACTER_OBJECTS)));
		entity.setByteObjects(new HashSet<>(Arrays.asList(EntityArray.BYTE_OBJECTS)));
		entity.setShortObjects(new HashSet<>(Arrays.asList(EntityArray.SHORT_OBJECTS)));
		entity.setIntegerObjects(new HashSet<>(Arrays.asList(EntityArray.INTEGER_OBJECTS)));
		entity.setLongObjects(new HashSet<>(Arrays.asList(EntityArray.LONG_OBJECTS)));
		entity.setFloatObjects(new HashSet<>(Arrays.asList(EntityArray.FLOAT_OBJECTS)));
		entity.setDoubleObjects(new HashSet<>(Arrays.asList(EntityArray.DOUBLE_OBJECTS)));

		entity.setStringObjects(new HashSet<>(Arrays.asList(EntityArray.STRING_OBJECTS)));
		entity.setDateObjects(new HashSet<>(Arrays.asList(EntityArray.DATE_OBJECTS)));
		entity.setLocalDateObjects(new HashSet<>(Arrays.asList(EntityArray.LOCALDATE_OBJECTS)));
		entity.setLocalTimeObjects(new HashSet<>(Arrays.asList(EntityArray.LOCALTIME_OBJECTS)));
		entity.setLocalDateTimeObjects(new HashSet<>(Arrays.asList(EntityArray.LOCALDATETIME_OBJECTS)));
		entity.setDecimalObjects(new HashSet<>(Arrays.asList(EntityArray.BIGDECIMAL_OBJECTS)));

		entity.setEnumCodes(new HashSet<>(Arrays.asList(EnumCodes.values())));
		entity.setEnumTexts(new HashSet<>(Arrays.asList(EnumTexts.values())));
		entity.setEnumNatives(new HashSet<>(Arrays.asList(EnumNative.values())));
		entity.setEnumCodeTexts(new HashSet<>(Arrays.asList(EnumCodeTexts.values())));

		entity.setEntityBases(new HashSet<>(Arrays.asList(EntityBase.createNullValue(), EntityBase.createMinValue(), EntityBase.createMaxValue())));
		entity.setEntityEmpties(new HashSet<>(Arrays.asList(new EntityEmpty(), new EntityEmpty(), new EntityEmpty())));
		return entity;
	}

	static void assertEntity(EntitySet a, EntitySet b) {
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

	public Set<Boolean> getBooleanObjects() {
		return booleanObjects;
	}

	public void setBooleanObjects(Set<Boolean> values) {
		booleanObjects = values;
	}

	public Set<Byte> getByteObjects() {
		return byteObjects;
	}

	public void setByteObjects(Set<Byte> values) {
		byteObjects = values;
	}

	public Set<Character> getCharacterObjects() {
		return characterObjects;
	}

	public void setCharacterObjects(Set<Character> values) {
		characterObjects = values;
	}

	public Set<Short> getShortObjects() {
		return shortObjects;
	}

	public void setShortObjects(Set<Short> values) {
		shortObjects = values;
	}

	public Set<Integer> getIntegerObjects() {
		return integerObjects;
	}

	public void setIntegerObjects(Set<Integer> values) {
		integerObjects = values;
	}

	public Set<Long> getLongObjects() {
		return longObjects;
	}

	public void setLongObjects(Set<Long> values) {
		longObjects = values;
	}

	public Set<Float> getFloatObjects() {
		return floatObjects;
	}

	public void setFloatObjects(Set<Float> values) {
		floatObjects = values;
	}

	public Set<Double> getDoubleObjects() {
		return doubleObjects;
	}

	public void setDoubleObjects(Set<Double> values) {
		doubleObjects = values;
	}

	public Set<String> getStringObjects() {
		return stringObjects;
	}

	public void setStringObjects(Set<String> values) {
		stringObjects = values;
	}

	public Set<Date> getDateObjects() {
		return dateObjects;
	}

	public void setDateObjects(Set<Date> values) {
		dateObjects = values;
	}

	public Set<LocalDate> getLocalDateObjects() {
		return localDateObjects;
	}

	public void setLocalDateObjects(Set<LocalDate> values) {
		localDateObjects = values;
	}

	public Set<LocalTime> getLocalTimeObjects() {
		return localTimeObjects;
	}

	public void setLocalTimeObjects(Set<LocalTime> values) {
		localTimeObjects = values;
	}

	public Set<LocalDateTime> getLocalDateTimeObjects() {
		return localDateTimeObjects;
	}

	public void setLocalDateTimeObjects(Set<LocalDateTime> values) {
		localDateTimeObjects = values;
	}

	public Set<BigDecimal> getDecimalObjects() {
		return decimalObjects;
	}

	public void setDecimalObjects(Set<BigDecimal> values) {
		decimalObjects = values;
	}

	public Set<EnumCodes> getEnumCodes() {
		return enumCodes;
	}

	public void setEnumCodes(Set<EnumCodes> values) {
		enumCodes = values;
	}

	public Set<EnumTexts> getEnumTexts() {
		return enumTexts;
	}

	public void setEnumTexts(Set<EnumTexts> values) {
		enumTexts = values;
	}

	public Set<EnumNative> getEnumNatives() {
		return enumNatives;
	}

	public void setEnumNatives(Set<EnumNative> values) {
		enumNatives = values;
	}

	public Set<EnumCodeTexts> getEnumCodeTexts() {
		return enumCodeTexts;
	}

	public void setEnumCodeTexts(Set<EnumCodeTexts> values) {
		enumCodeTexts = values;
	}

	public Set<EntityBase> getEntityBases() {
		return entityBases;
	}

	public void setEntityBases(Set<EntityBase> values) {
		entityBases = values;
	}

	public Set<EntityEmpty> getEntityEmpties() {
		return entityEmpties;
	}

	public void setEntityEmpties(Set<EntityEmpty> values) {
		entityEmpties = values;
	}
}
