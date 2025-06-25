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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 具有集合的实体
 * 
 * @author ZhangXi
 * @date 2023年7月27日
 */
public class EntityMap {

	private Map<Boolean, Boolean> booleanBoolean;
	private Map<Boolean, Byte> booleanByte;
	private Map<Boolean, Character> booleanCharacter;
	private Map<Boolean, Short> booleanShort;
	private Map<Boolean, Integer> booleanInteger;
	private Map<Boolean, Long> booleanLong;
	private Map<Boolean, Float> booleanFloat;
	private Map<Boolean, Double> booleanDouble;
	private Map<Boolean, Date> booleanDate;
	private Map<Boolean, String> booleanString;
	private Map<Boolean, LocalDate> booleanLocalDate;
	private Map<Boolean, LocalTime> booleanLocalTime;
	private Map<Boolean, LocalDateTime> booleanLocalDateTime;
	private Map<Boolean, BigDecimal> booleanBigDecimal;
	private Map<Boolean, EnumCodes> booleanEnumCodes;
	private Map<Boolean, EnumTexts> booleanEnumTexts;
	private Map<Boolean, EnumNative> booleanEnumNative;
	private Map<Boolean, EnumCodeTexts> booleanEnumCodeTexts;
	private Map<Boolean, EntityBase> booleanEntityBase;
	private Map<Boolean, EntityEmpty> booleanEntityEmpty;

	private Map<Byte, Boolean> byteBoolean;
	private Map<Byte, Byte> byteByte;
	private Map<Byte, Character> byteCharacter;
	private Map<Byte, Short> byteShort;
	private Map<Byte, Integer> byteInteger;
	private Map<Byte, Long> byteLong;
	private Map<Byte, Float> byteFloat;
	private Map<Byte, Double> byteDouble;
	private Map<Byte, Date> byteDate;
	private Map<Byte, String> byteString;
	private Map<Byte, LocalDate> byteLocalDate;
	private Map<Byte, LocalTime> byteLocalTime;
	private Map<Byte, LocalDateTime> byteLocalDateTime;
	private Map<Byte, BigDecimal> byteBigDecimal;
	private Map<Byte, EnumCodes> byteEnumCodes;
	private Map<Byte, EnumTexts> byteEnumTexts;
	private Map<Byte, EnumNative> byteEnumNative;
	private Map<Byte, EnumCodeTexts> byteEnumCodeTexts;
	private Map<Byte, EntityBase> byteEntityBase;
	private Map<Byte, EntityEmpty> byteEntityEmpty;

	private Map<Character, Boolean> characterBoolean;
	private Map<Character, Byte> characterByte;
	private Map<Character, Character> characterCharacter;
	private Map<Character, Short> characterShort;
	private Map<Character, Integer> characterInteger;
	private Map<Character, Long> characterLong;
	private Map<Character, Float> characterFloat;
	private Map<Character, Double> characterDouble;
	private Map<Character, Date> characterDate;
	private Map<Character, String> characterString;
	private Map<Character, LocalDate> characterLocalDate;
	private Map<Character, LocalTime> characterLocalTime;
	private Map<Character, LocalDateTime> characterLocalDateTime;
	private Map<Character, BigDecimal> characterBigDecimal;
	private Map<Character, EnumCodes> characterEnumCodes;
	private Map<Character, EnumTexts> characterEnumTexts;
	private Map<Character, EnumNative> characterEnumNative;
	private Map<Character, EnumCodeTexts> characterEnumCodeTexts;
	private Map<Character, EntityBase> characterEntityBase;
	private Map<Character, EntityEmpty> characterEntityEmpty;

	private Map<Short, Boolean> shortBoolean;
	private Map<Short, Byte> shortByte;
	private Map<Short, Character> shortCharacter;
	private Map<Short, Short> shortShort;
	private Map<Short, Integer> shortInteger;
	private Map<Short, Long> shortLong;
	private Map<Short, Float> shortFloat;
	private Map<Short, Double> shortDouble;
	private Map<Short, Date> shortDate;
	private Map<Short, String> shortString;
	private Map<Short, LocalDate> shortLocalDate;
	private Map<Short, LocalTime> shortLocalTime;
	private Map<Short, LocalDateTime> shortLocalDateTime;
	private Map<Short, BigDecimal> shortBigDecimal;
	private Map<Short, EnumCodes> shortEnumCodes;
	private Map<Short, EnumTexts> shortEnumTexts;
	private Map<Short, EnumNative> shortEnumNative;
	private Map<Short, EnumCodeTexts> shortEnumCodeTexts;
	private Map<Short, EntityBase> shortEntityBase;
	private Map<Short, EntityEmpty> shortEntityEmpty;

	private Map<Integer, Boolean> integerBoolean;
	private Map<Integer, Byte> integerByte;
	private Map<Integer, Character> integerCharacter;
	private Map<Integer, Short> integerShort;
	private Map<Integer, Integer> integerInteger;
	private Map<Integer, Long> integerLong;
	private Map<Integer, Float> integerFloat;
	private Map<Integer, Double> integerDouble;
	private Map<Integer, Date> integerDate;
	private Map<Integer, String> integerString;
	private Map<Integer, LocalDate> integerLocalDate;
	private Map<Integer, LocalTime> integerLocalTime;
	private Map<Integer, LocalDateTime> integerLocalDateTime;
	private Map<Integer, BigDecimal> integerBigDecimal;
	private Map<Integer, EnumCodes> integerEnumCodes;
	private Map<Integer, EnumTexts> integerEnumTexts;
	private Map<Integer, EnumNative> integerEnumNative;
	private Map<Integer, EnumCodeTexts> integerEnumCodeTexts;
	private Map<Integer, EntityBase> integerEntityBase;
	private Map<Integer, EntityEmpty> integerEntityEmpty;

	private Map<Long, Boolean> longBoolean;
	private Map<Long, Byte> longByte;
	private Map<Long, Character> longCharacter;
	private Map<Long, Short> longShort;
	private Map<Long, Integer> longInteger;
	private Map<Long, Long> longLong;
	private Map<Long, Float> longFloat;
	private Map<Long, Double> longDouble;
	private Map<Long, Date> longDate;
	private Map<Long, String> longString;
	private Map<Long, LocalDate> longLocalDate;
	private Map<Long, LocalTime> longLocalTime;
	private Map<Long, LocalDateTime> longLocalDateTime;
	private Map<Long, BigDecimal> longBigDecimal;
	private Map<Long, EnumCodes> longEnumCodes;
	private Map<Long, EnumTexts> longEnumTexts;
	private Map<Long, EnumNative> longEnumNative;
	private Map<Long, EnumCodeTexts> longEnumCodeTexts;
	private Map<Long, EntityBase> longEntityBase;
	private Map<Long, EntityEmpty> longEntityEmpty;

	private Map<Float, Boolean> floatBoolean;
	private Map<Float, Byte> floatByte;
	private Map<Float, Character> floatCharacter;
	private Map<Float, Short> floatShort;
	private Map<Float, Integer> floatInteger;
	private Map<Float, Long> floatLong;
	private Map<Float, Float> floatFloat;
	private Map<Float, Double> floatDouble;
	private Map<Float, Date> floatDate;
	private Map<Float, String> floatString;
	private Map<Float, LocalDate> floatLocalDate;
	private Map<Float, LocalTime> floatLocalTime;
	private Map<Float, LocalDateTime> floatLocalDateTime;
	private Map<Float, BigDecimal> floatBigDecimal;
	private Map<Float, EnumCodes> floatEnumCodes;
	private Map<Float, EnumTexts> floatEnumTexts;
	private Map<Float, EnumNative> floatEnumNative;
	private Map<Float, EnumCodeTexts> floatEnumCodeTexts;
	private Map<Float, EntityBase> floatEntityBase;
	private Map<Float, EntityEmpty> floatEntityEmpty;

	private Map<Double, Boolean> doubleBoolean;
	private Map<Double, Byte> doubleByte;
	private Map<Double, Character> doubleCharacter;
	private Map<Double, Short> doubleShort;
	private Map<Double, Integer> doubleInteger;
	private Map<Double, Long> doubleLong;
	private Map<Double, Float> doubleFloat;
	private Map<Double, Double> doubleDouble;
	private Map<Double, Date> doubleDate;
	private Map<Double, String> doubleString;
	private Map<Double, LocalDate> doubleLocalDate;
	private Map<Double, LocalTime> doubleLocalTime;
	private Map<Double, LocalDateTime> doubleLocalDateTime;
	private Map<Double, BigDecimal> doubleBigDecimal;
	private Map<Double, EnumCodes> doubleEnumCodes;
	private Map<Double, EnumTexts> doubleEnumTexts;
	private Map<Double, EnumNative> doubleEnumNative;
	private Map<Double, EnumCodeTexts> doubleEnumCodeTexts;
	private Map<Double, EntityBase> doubleEntityBase;
	private Map<Double, EntityEmpty> doubleEntityEmpty;

	private Map<Date, Boolean> dateBoolean;
	private Map<Date, Byte> dateByte;
	private Map<Date, Character> dateCharacter;
	private Map<Date, Short> dateShort;
	private Map<Date, Integer> dateInteger;
	private Map<Date, Long> dateLong;
	private Map<Date, Float> dateFloat;
	private Map<Date, Double> dateDouble;
	private Map<Date, Date> dateDate;
	private Map<Date, String> dateString;
	private Map<Date, LocalDate> dateLocalDate;
	private Map<Date, LocalTime> dateLocalTime;
	private Map<Date, LocalDateTime> dateLocalDateTime;
	private Map<Date, BigDecimal> dateBigDecimal;
	private Map<Date, EnumCodes> dateEnumCodes;
	private Map<Date, EnumTexts> dateEnumTexts;
	private Map<Date, EnumNative> dateEnumNative;
	private Map<Date, EnumCodeTexts> dateEnumCodeTexts;
	private Map<Date, EntityBase> dateEntityBase;
	private Map<Date, EntityEmpty> dateEntityEmpty;

	private Map<String, Boolean> stringBoolean;
	private Map<String, Byte> stringByte;
	private Map<String, Character> stringCharacter;
	private Map<String, Short> stringShort;
	private Map<String, Integer> stringInteger;
	private Map<String, Long> stringLong;
	private Map<String, Float> stringFloat;
	private Map<String, Double> stringDouble;
	private Map<String, Date> stringDate;
	private Map<String, String> stringString;
	private Map<String, LocalDate> stringLocalDate;
	private Map<String, LocalTime> stringLocalTime;
	private Map<String, LocalDateTime> stringLocalDateTime;
	private Map<String, BigDecimal> stringBigDecimal;
	private Map<String, EnumCodes> stringEnumCodes;
	private Map<String, EnumTexts> stringEnumTexts;
	private Map<String, EnumNative> stringEnumNative;
	private Map<String, EnumCodeTexts> stringEnumCodeTexts;
	private Map<String, EntityBase> stringEntityBase;
	private Map<String, EntityEmpty> stringEntityEmpty;

	private Map<LocalDate, Boolean> localdateBoolean;
	private Map<LocalDate, Byte> localdateByte;
	private Map<LocalDate, Character> localdateCharacter;
	private Map<LocalDate, Short> localdateShort;
	private Map<LocalDate, Integer> localdateInteger;
	private Map<LocalDate, Long> localdateLong;
	private Map<LocalDate, Float> localdateFloat;
	private Map<LocalDate, Double> localdateDouble;
	private Map<LocalDate, Date> localdateDate;
	private Map<LocalDate, String> localdateString;
	private Map<LocalDate, LocalDate> localdateLocalDate;
	private Map<LocalDate, LocalTime> localdateLocalTime;
	private Map<LocalDate, LocalDateTime> localdateLocalDateTime;
	private Map<LocalDate, BigDecimal> localdateBigDecimal;
	private Map<LocalDate, EnumCodes> localdateEnumCodes;
	private Map<LocalDate, EnumTexts> localdateEnumTexts;
	private Map<LocalDate, EnumNative> localdateEnumNative;
	private Map<LocalDate, EnumCodeTexts> localdateEnumCodeTexts;
	private Map<LocalDate, EntityBase> localdateEntityBase;
	private Map<LocalDate, EntityEmpty> localdateEntityEmpty;

	private Map<LocalTime, Boolean> localtimeBoolean;
	private Map<LocalTime, Byte> localtimeByte;
	private Map<LocalTime, Character> localtimeCharacter;
	private Map<LocalTime, Short> localtimeShort;
	private Map<LocalTime, Integer> localtimeInteger;
	private Map<LocalTime, Long> localtimeLong;
	private Map<LocalTime, Float> localtimeFloat;
	private Map<LocalTime, Double> localtimeDouble;
	private Map<LocalTime, Date> localtimeDate;
	private Map<LocalTime, String> localtimeString;
	private Map<LocalTime, LocalDate> localtimeLocalDate;
	private Map<LocalTime, LocalTime> localtimeLocalTime;
	private Map<LocalTime, LocalDateTime> localtimeLocalDateTime;
	private Map<LocalTime, BigDecimal> localtimeBigDecimal;
	private Map<LocalTime, EnumCodes> localtimeEnumCodes;
	private Map<LocalTime, EnumTexts> localtimeEnumTexts;
	private Map<LocalTime, EnumNative> localtimeEnumNative;
	private Map<LocalTime, EnumCodeTexts> localtimeEnumCodeTexts;
	private Map<LocalTime, EntityBase> localtimeEntityBase;
	private Map<LocalTime, EntityEmpty> localtimeEntityEmpty;

	private Map<LocalDateTime, Boolean> localdatetimeBoolean;
	private Map<LocalDateTime, Byte> localdatetimeByte;
	private Map<LocalDateTime, Character> localdatetimeCharacter;
	private Map<LocalDateTime, Short> localdatetimeShort;
	private Map<LocalDateTime, Integer> localdatetimeInteger;
	private Map<LocalDateTime, Long> localdatetimeLong;
	private Map<LocalDateTime, Float> localdatetimeFloat;
	private Map<LocalDateTime, Double> localdatetimeDouble;
	private Map<LocalDateTime, Date> localdatetimeDate;
	private Map<LocalDateTime, String> localdatetimeString;
	private Map<LocalDateTime, LocalDate> localdatetimeLocalDate;
	private Map<LocalDateTime, LocalTime> localdatetimeLocalTime;
	private Map<LocalDateTime, LocalDateTime> localdatetimeLocalDateTime;
	private Map<LocalDateTime, BigDecimal> localdatetimeBigDecimal;
	private Map<LocalDateTime, EnumCodes> localdatetimeEnumCodes;
	private Map<LocalDateTime, EnumTexts> localdatetimeEnumTexts;
	private Map<LocalDateTime, EnumNative> localdatetimeEnumNative;
	private Map<LocalDateTime, EnumCodeTexts> localdatetimeEnumCodeTexts;
	private Map<LocalDateTime, EntityBase> localdatetimeEntityBase;
	private Map<LocalDateTime, EntityEmpty> localdatetimeEntityEmpty;

	private Map<BigDecimal, Boolean> bigdecimalBoolean;
	private Map<BigDecimal, Byte> bigdecimalByte;
	private Map<BigDecimal, Character> bigdecimalCharacter;
	private Map<BigDecimal, Short> bigdecimalShort;
	private Map<BigDecimal, Integer> bigdecimalInteger;
	private Map<BigDecimal, Long> bigdecimalLong;
	private Map<BigDecimal, Float> bigdecimalFloat;
	private Map<BigDecimal, Double> bigdecimalDouble;
	private Map<BigDecimal, Date> bigdecimalDate;
	private Map<BigDecimal, String> bigdecimalString;
	private Map<BigDecimal, LocalDate> bigdecimalLocalDate;
	private Map<BigDecimal, LocalTime> bigdecimalLocalTime;
	private Map<BigDecimal, LocalDateTime> bigdecimalLocalDateTime;
	private Map<BigDecimal, BigDecimal> bigdecimalBigDecimal;
	private Map<BigDecimal, EnumCodes> bigdecimalEnumCodes;
	private Map<BigDecimal, EnumTexts> bigdecimalEnumTexts;
	private Map<BigDecimal, EnumNative> bigdecimalEnumNative;
	private Map<BigDecimal, EnumCodeTexts> bigdecimalEnumCodeTexts;
	private Map<BigDecimal, EntityBase> bigdecimalEntityBase;
	private Map<BigDecimal, EntityEmpty> bigdecimalEntityEmpty;

	static EntityMap createNullValue() {
		final EntityMap entity = new EntityMap();
		entity.setBooleanBoolean(null);
		entity.setBooleanByte(null);
		entity.setBooleanCharacter(null);
		entity.setBooleanShort(null);
		entity.setBooleanInteger(null);
		entity.setBooleanLong(null);
		entity.setBooleanFloat(null);
		entity.setBooleanDouble(null);
		entity.setBooleanDate(null);
		entity.setBooleanString(null);
		entity.setBooleanLocalDate(null);
		entity.setBooleanLocalTime(null);
		entity.setBooleanLocalDateTime(null);
		entity.setBooleanBigDecimal(null);
		entity.setBooleanEnumCodes(null);
		entity.setBooleanEnumTexts(null);
		entity.setBooleanEnumNative(null);
		entity.setBooleanEnumCodeTexts(null);
		entity.setBooleanEntityBase(null);
		entity.setBooleanEntityEmpty(null);
		entity.setByteBoolean(null);
		entity.setByteByte(null);
		entity.setByteCharacter(null);
		entity.setByteShort(null);
		entity.setByteInteger(null);
		entity.setByteLong(null);
		entity.setByteFloat(null);
		entity.setByteDouble(null);
		entity.setByteDate(null);
		entity.setByteString(null);
		entity.setByteLocalDate(null);
		entity.setByteLocalTime(null);
		entity.setByteLocalDateTime(null);
		entity.setByteBigDecimal(null);
		entity.setByteEnumCodes(null);
		entity.setByteEnumTexts(null);
		entity.setByteEnumNative(null);
		entity.setByteEnumCodeTexts(null);
		entity.setByteEntityBase(null);
		entity.setByteEntityEmpty(null);
		entity.setCharacterBoolean(null);
		entity.setCharacterByte(null);
		entity.setCharacterCharacter(null);
		entity.setCharacterShort(null);
		entity.setCharacterInteger(null);
		entity.setCharacterLong(null);
		entity.setCharacterFloat(null);
		entity.setCharacterDouble(null);
		entity.setCharacterDate(null);
		entity.setCharacterString(null);
		entity.setCharacterLocalDate(null);
		entity.setCharacterLocalTime(null);
		entity.setCharacterLocalDateTime(null);
		entity.setCharacterBigDecimal(null);
		entity.setCharacterEnumCodes(null);
		entity.setCharacterEnumTexts(null);
		entity.setCharacterEnumNative(null);
		entity.setCharacterEnumCodeTexts(null);
		entity.setCharacterEntityBase(null);
		entity.setCharacterEntityEmpty(null);
		entity.setShortBoolean(null);
		entity.setShortByte(null);
		entity.setShortCharacter(null);
		entity.setShortShort(null);
		entity.setShortInteger(null);
		entity.setShortLong(null);
		entity.setShortFloat(null);
		entity.setShortDouble(null);
		entity.setShortDate(null);
		entity.setShortString(null);
		entity.setShortLocalDate(null);
		entity.setShortLocalTime(null);
		entity.setShortLocalDateTime(null);
		entity.setShortBigDecimal(null);
		entity.setShortEnumCodes(null);
		entity.setShortEnumTexts(null);
		entity.setShortEnumNative(null);
		entity.setShortEnumCodeTexts(null);
		entity.setShortEntityBase(null);
		entity.setShortEntityEmpty(null);
		entity.setIntegerBoolean(null);
		entity.setIntegerByte(null);
		entity.setIntegerCharacter(null);
		entity.setIntegerShort(null);
		entity.setIntegerInteger(null);
		entity.setIntegerLong(null);
		entity.setIntegerFloat(null);
		entity.setIntegerDouble(null);
		entity.setIntegerDate(null);
		entity.setIntegerString(null);
		entity.setIntegerLocalDate(null);
		entity.setIntegerLocalTime(null);
		entity.setIntegerLocalDateTime(null);
		entity.setIntegerBigDecimal(null);
		entity.setIntegerEnumCodes(null);
		entity.setIntegerEnumTexts(null);
		entity.setIntegerEnumNative(null);
		entity.setIntegerEnumCodeTexts(null);
		entity.setIntegerEntityBase(null);
		entity.setIntegerEntityEmpty(null);
		entity.setLongBoolean(null);
		entity.setLongByte(null);
		entity.setLongCharacter(null);
		entity.setLongShort(null);
		entity.setLongInteger(null);
		entity.setLongLong(null);
		entity.setLongFloat(null);
		entity.setLongDouble(null);
		entity.setLongDate(null);
		entity.setLongString(null);
		entity.setLongLocalDate(null);
		entity.setLongLocalTime(null);
		entity.setLongLocalDateTime(null);
		entity.setLongBigDecimal(null);
		entity.setLongEnumCodes(null);
		entity.setLongEnumTexts(null);
		entity.setLongEnumNative(null);
		entity.setLongEnumCodeTexts(null);
		entity.setLongEntityBase(null);
		entity.setLongEntityEmpty(null);
		entity.setFloatBoolean(null);
		entity.setFloatByte(null);
		entity.setFloatCharacter(null);
		entity.setFloatShort(null);
		entity.setFloatInteger(null);
		entity.setFloatLong(null);
		entity.setFloatFloat(null);
		entity.setFloatDouble(null);
		entity.setFloatDate(null);
		entity.setFloatString(null);
		entity.setFloatLocalDate(null);
		entity.setFloatLocalTime(null);
		entity.setFloatLocalDateTime(null);
		entity.setFloatBigDecimal(null);
		entity.setFloatEnumCodes(null);
		entity.setFloatEnumTexts(null);
		entity.setFloatEnumNative(null);
		entity.setFloatEnumCodeTexts(null);
		entity.setFloatEntityBase(null);
		entity.setFloatEntityEmpty(null);
		entity.setDoubleBoolean(null);
		entity.setDoubleByte(null);
		entity.setDoubleCharacter(null);
		entity.setDoubleShort(null);
		entity.setDoubleInteger(null);
		entity.setDoubleLong(null);
		entity.setDoubleFloat(null);
		entity.setDoubleDouble(null);
		entity.setDoubleDate(null);
		entity.setDoubleString(null);
		entity.setDoubleLocalDate(null);
		entity.setDoubleLocalTime(null);
		entity.setDoubleLocalDateTime(null);
		entity.setDoubleBigDecimal(null);
		entity.setDoubleEnumCodes(null);
		entity.setDoubleEnumTexts(null);
		entity.setDoubleEnumNative(null);
		entity.setDoubleEnumCodeTexts(null);
		entity.setDoubleEntityBase(null);
		entity.setDoubleEntityEmpty(null);
		entity.setDateBoolean(null);
		entity.setDateByte(null);
		entity.setDateCharacter(null);
		entity.setDateShort(null);
		entity.setDateInteger(null);
		entity.setDateLong(null);
		entity.setDateFloat(null);
		entity.setDateDouble(null);
		entity.setDateDate(null);
		entity.setDateString(null);
		entity.setDateLocalDate(null);
		entity.setDateLocalTime(null);
		entity.setDateLocalDateTime(null);
		entity.setDateBigDecimal(null);
		entity.setDateEnumCodes(null);
		entity.setDateEnumTexts(null);
		entity.setDateEnumNative(null);
		entity.setDateEnumCodeTexts(null);
		entity.setDateEntityBase(null);
		entity.setDateEntityEmpty(null);
		entity.setStringBoolean(null);
		entity.setStringByte(null);
		entity.setStringCharacter(null);
		entity.setStringShort(null);
		entity.setStringInteger(null);
		entity.setStringLong(null);
		entity.setStringFloat(null);
		entity.setStringDouble(null);
		entity.setStringDate(null);
		entity.setStringString(null);
		entity.setStringLocalDate(null);
		entity.setStringLocalTime(null);
		entity.setStringLocalDateTime(null);
		entity.setStringBigDecimal(null);
		entity.setStringEnumCodes(null);
		entity.setStringEnumTexts(null);
		entity.setStringEnumNative(null);
		entity.setStringEnumCodeTexts(null);
		entity.setStringEntityBase(null);
		entity.setStringEntityEmpty(null);
		entity.setLocalDateBoolean(null);
		entity.setLocalDateByte(null);
		entity.setLocalDateCharacter(null);
		entity.setLocalDateShort(null);
		entity.setLocalDateInteger(null);
		entity.setLocalDateLong(null);
		entity.setLocalDateFloat(null);
		entity.setLocalDateDouble(null);
		entity.setLocalDateDate(null);
		entity.setLocalDateString(null);
		entity.setLocalDateLocalDate(null);
		entity.setLocalDateLocalTime(null);
		entity.setLocalDateLocalDateTime(null);
		entity.setLocalDateBigDecimal(null);
		entity.setLocalDateEnumCodes(null);
		entity.setLocalDateEnumTexts(null);
		entity.setLocalDateEnumNative(null);
		entity.setLocalDateEnumCodeTexts(null);
		entity.setLocalDateEntityBase(null);
		entity.setLocalDateEntityEmpty(null);
		entity.setLocalTimeBoolean(null);
		entity.setLocalTimeByte(null);
		entity.setLocalTimeCharacter(null);
		entity.setLocalTimeShort(null);
		entity.setLocalTimeInteger(null);
		entity.setLocalTimeLong(null);
		entity.setLocalTimeFloat(null);
		entity.setLocalTimeDouble(null);
		entity.setLocalTimeDate(null);
		entity.setLocalTimeString(null);
		entity.setLocalTimeLocalDate(null);
		entity.setLocalTimeLocalTime(null);
		entity.setLocalTimeLocalDateTime(null);
		entity.setLocalTimeBigDecimal(null);
		entity.setLocalTimeEnumCodes(null);
		entity.setLocalTimeEnumTexts(null);
		entity.setLocalTimeEnumNative(null);
		entity.setLocalTimeEnumCodeTexts(null);
		entity.setLocalTimeEntityBase(null);
		entity.setLocalTimeEntityEmpty(null);
		entity.setLocalDateTimeBoolean(null);
		entity.setLocalDateTimeByte(null);
		entity.setLocalDateTimeCharacter(null);
		entity.setLocalDateTimeShort(null);
		entity.setLocalDateTimeInteger(null);
		entity.setLocalDateTimeLong(null);
		entity.setLocalDateTimeFloat(null);
		entity.setLocalDateTimeDouble(null);
		entity.setLocalDateTimeDate(null);
		entity.setLocalDateTimeString(null);
		entity.setLocalDateTimeLocalDate(null);
		entity.setLocalDateTimeLocalTime(null);
		entity.setLocalDateTimeLocalDateTime(null);
		entity.setLocalDateTimeBigDecimal(null);
		entity.setLocalDateTimeEnumCodes(null);
		entity.setLocalDateTimeEnumTexts(null);
		entity.setLocalDateTimeEnumNative(null);
		entity.setLocalDateTimeEnumCodeTexts(null);
		entity.setLocalDateTimeEntityBase(null);
		entity.setLocalDateTimeEntityEmpty(null);
		entity.setBigDecimalBoolean(null);
		entity.setBigDecimalByte(null);
		entity.setBigDecimalCharacter(null);
		entity.setBigDecimalShort(null);
		entity.setBigDecimalInteger(null);
		entity.setBigDecimalLong(null);
		entity.setBigDecimalFloat(null);
		entity.setBigDecimalDouble(null);
		entity.setBigDecimalDate(null);
		entity.setBigDecimalString(null);
		entity.setBigDecimalLocalDate(null);
		entity.setBigDecimalLocalTime(null);
		entity.setBigDecimalLocalDateTime(null);
		entity.setBigDecimalBigDecimal(null);
		entity.setBigDecimalEnumCodes(null);
		entity.setBigDecimalEnumTexts(null);
		entity.setBigDecimalEnumNative(null);
		entity.setBigDecimalEnumCodeTexts(null);
		entity.setBigDecimalEntityBase(null);
		entity.setBigDecimalEntityEmpty(null);
		return entity;
	}

	static EntityMap createEmptyValue() {
		final EntityMap entity = new EntityMap();
		entity.setBooleanBoolean(new HashMap<>());
		entity.setBooleanByte(new HashMap<>());
		entity.setBooleanCharacter(new HashMap<>());
		entity.setBooleanShort(new HashMap<>());
		entity.setBooleanInteger(new HashMap<>());
		entity.setBooleanLong(new HashMap<>());
		entity.setBooleanFloat(new HashMap<>());
		entity.setBooleanDouble(new HashMap<>());
		entity.setBooleanDate(new HashMap<>());
		entity.setBooleanString(new HashMap<>());
		entity.setBooleanLocalDate(new HashMap<>());
		entity.setBooleanLocalTime(new HashMap<>());
		entity.setBooleanLocalDateTime(new HashMap<>());
		entity.setBooleanBigDecimal(new HashMap<>());
		entity.setBooleanEnumCodes(new HashMap<>());
		entity.setBooleanEnumTexts(new HashMap<>());
		entity.setBooleanEnumNative(new HashMap<>());
		entity.setBooleanEnumCodeTexts(new HashMap<>());
		entity.setBooleanEntityBase(new HashMap<>());
		entity.setBooleanEntityEmpty(new HashMap<>());
		entity.setByteBoolean(new HashMap<>());
		entity.setByteByte(new HashMap<>());
		entity.setByteCharacter(new HashMap<>());
		entity.setByteShort(new HashMap<>());
		entity.setByteInteger(new HashMap<>());
		entity.setByteLong(new HashMap<>());
		entity.setByteFloat(new HashMap<>());
		entity.setByteDouble(new HashMap<>());
		entity.setByteDate(new HashMap<>());
		entity.setByteString(new HashMap<>());
		entity.setByteLocalDate(new HashMap<>());
		entity.setByteLocalTime(new HashMap<>());
		entity.setByteLocalDateTime(new HashMap<>());
		entity.setByteBigDecimal(new HashMap<>());
		entity.setByteEnumCodes(new HashMap<>());
		entity.setByteEnumTexts(new HashMap<>());
		entity.setByteEnumNative(new HashMap<>());
		entity.setByteEnumCodeTexts(new HashMap<>());
		entity.setByteEntityBase(new HashMap<>());
		entity.setByteEntityEmpty(new HashMap<>());
		entity.setCharacterBoolean(new HashMap<>());
		entity.setCharacterByte(new HashMap<>());
		entity.setCharacterCharacter(new HashMap<>());
		entity.setCharacterShort(new HashMap<>());
		entity.setCharacterInteger(new HashMap<>());
		entity.setCharacterLong(new HashMap<>());
		entity.setCharacterFloat(new HashMap<>());
		entity.setCharacterDouble(new HashMap<>());
		entity.setCharacterDate(new HashMap<>());
		entity.setCharacterString(new HashMap<>());
		entity.setCharacterLocalDate(new HashMap<>());
		entity.setCharacterLocalTime(new HashMap<>());
		entity.setCharacterLocalDateTime(new HashMap<>());
		entity.setCharacterBigDecimal(new HashMap<>());
		entity.setCharacterEnumCodes(new HashMap<>());
		entity.setCharacterEnumTexts(new HashMap<>());
		entity.setCharacterEnumNative(new HashMap<>());
		entity.setCharacterEnumCodeTexts(new HashMap<>());
		entity.setCharacterEntityBase(new HashMap<>());
		entity.setCharacterEntityEmpty(new HashMap<>());
		entity.setShortBoolean(new HashMap<>());
		entity.setShortByte(new HashMap<>());
		entity.setShortCharacter(new HashMap<>());
		entity.setShortShort(new HashMap<>());
		entity.setShortInteger(new HashMap<>());
		entity.setShortLong(new HashMap<>());
		entity.setShortFloat(new HashMap<>());
		entity.setShortDouble(new HashMap<>());
		entity.setShortDate(new HashMap<>());
		entity.setShortString(new HashMap<>());
		entity.setShortLocalDate(new HashMap<>());
		entity.setShortLocalTime(new HashMap<>());
		entity.setShortLocalDateTime(new HashMap<>());
		entity.setShortBigDecimal(new HashMap<>());
		entity.setShortEnumCodes(new HashMap<>());
		entity.setShortEnumTexts(new HashMap<>());
		entity.setShortEnumNative(new HashMap<>());
		entity.setShortEnumCodeTexts(new HashMap<>());
		entity.setShortEntityBase(new HashMap<>());
		entity.setShortEntityEmpty(new HashMap<>());
		entity.setIntegerBoolean(new HashMap<>());
		entity.setIntegerByte(new HashMap<>());
		entity.setIntegerCharacter(new HashMap<>());
		entity.setIntegerShort(new HashMap<>());
		entity.setIntegerInteger(new HashMap<>());
		entity.setIntegerLong(new HashMap<>());
		entity.setIntegerFloat(new HashMap<>());
		entity.setIntegerDouble(new HashMap<>());
		entity.setIntegerDate(new HashMap<>());
		entity.setIntegerString(new HashMap<>());
		entity.setIntegerLocalDate(new HashMap<>());
		entity.setIntegerLocalTime(new HashMap<>());
		entity.setIntegerLocalDateTime(new HashMap<>());
		entity.setIntegerBigDecimal(new HashMap<>());
		entity.setIntegerEnumCodes(new HashMap<>());
		entity.setIntegerEnumTexts(new HashMap<>());
		entity.setIntegerEnumNative(new HashMap<>());
		entity.setIntegerEnumCodeTexts(new HashMap<>());
		entity.setIntegerEntityBase(new HashMap<>());
		entity.setIntegerEntityEmpty(new HashMap<>());
		entity.setLongBoolean(new HashMap<>());
		entity.setLongByte(new HashMap<>());
		entity.setLongCharacter(new HashMap<>());
		entity.setLongShort(new HashMap<>());
		entity.setLongInteger(new HashMap<>());
		entity.setLongLong(new HashMap<>());
		entity.setLongFloat(new HashMap<>());
		entity.setLongDouble(new HashMap<>());
		entity.setLongDate(new HashMap<>());
		entity.setLongString(new HashMap<>());
		entity.setLongLocalDate(new HashMap<>());
		entity.setLongLocalTime(new HashMap<>());
		entity.setLongLocalDateTime(new HashMap<>());
		entity.setLongBigDecimal(new HashMap<>());
		entity.setLongEnumCodes(new HashMap<>());
		entity.setLongEnumTexts(new HashMap<>());
		entity.setLongEnumNative(new HashMap<>());
		entity.setLongEnumCodeTexts(new HashMap<>());
		entity.setLongEntityBase(new HashMap<>());
		entity.setLongEntityEmpty(new HashMap<>());
		entity.setFloatBoolean(new HashMap<>());
		entity.setFloatByte(new HashMap<>());
		entity.setFloatCharacter(new HashMap<>());
		entity.setFloatShort(new HashMap<>());
		entity.setFloatInteger(new HashMap<>());
		entity.setFloatLong(new HashMap<>());
		entity.setFloatFloat(new HashMap<>());
		entity.setFloatDouble(new HashMap<>());
		entity.setFloatDate(new HashMap<>());
		entity.setFloatString(new HashMap<>());
		entity.setFloatLocalDate(new HashMap<>());
		entity.setFloatLocalTime(new HashMap<>());
		entity.setFloatLocalDateTime(new HashMap<>());
		entity.setFloatBigDecimal(new HashMap<>());
		entity.setFloatEnumCodes(new HashMap<>());
		entity.setFloatEnumTexts(new HashMap<>());
		entity.setFloatEnumNative(new HashMap<>());
		entity.setFloatEnumCodeTexts(new HashMap<>());
		entity.setFloatEntityBase(new HashMap<>());
		entity.setFloatEntityEmpty(new HashMap<>());
		entity.setDoubleBoolean(new HashMap<>());
		entity.setDoubleByte(new HashMap<>());
		entity.setDoubleCharacter(new HashMap<>());
		entity.setDoubleShort(new HashMap<>());
		entity.setDoubleInteger(new HashMap<>());
		entity.setDoubleLong(new HashMap<>());
		entity.setDoubleFloat(new HashMap<>());
		entity.setDoubleDouble(new HashMap<>());
		entity.setDoubleDate(new HashMap<>());
		entity.setDoubleString(new HashMap<>());
		entity.setDoubleLocalDate(new HashMap<>());
		entity.setDoubleLocalTime(new HashMap<>());
		entity.setDoubleLocalDateTime(new HashMap<>());
		entity.setDoubleBigDecimal(new HashMap<>());
		entity.setDoubleEnumCodes(new HashMap<>());
		entity.setDoubleEnumTexts(new HashMap<>());
		entity.setDoubleEnumNative(new HashMap<>());
		entity.setDoubleEnumCodeTexts(new HashMap<>());
		entity.setDoubleEntityBase(new HashMap<>());
		entity.setDoubleEntityEmpty(new HashMap<>());
		entity.setDateBoolean(new HashMap<>());
		entity.setDateByte(new HashMap<>());
		entity.setDateCharacter(new HashMap<>());
		entity.setDateShort(new HashMap<>());
		entity.setDateInteger(new HashMap<>());
		entity.setDateLong(new HashMap<>());
		entity.setDateFloat(new HashMap<>());
		entity.setDateDouble(new HashMap<>());
		entity.setDateDate(new HashMap<>());
		entity.setDateString(new HashMap<>());
		entity.setDateLocalDate(new HashMap<>());
		entity.setDateLocalTime(new HashMap<>());
		entity.setDateLocalDateTime(new HashMap<>());
		entity.setDateBigDecimal(new HashMap<>());
		entity.setDateEnumCodes(new HashMap<>());
		entity.setDateEnumTexts(new HashMap<>());
		entity.setDateEnumNative(new HashMap<>());
		entity.setDateEnumCodeTexts(new HashMap<>());
		entity.setDateEntityBase(new HashMap<>());
		entity.setDateEntityEmpty(new HashMap<>());
		entity.setStringBoolean(new HashMap<>());
		entity.setStringByte(new HashMap<>());
		entity.setStringCharacter(new HashMap<>());
		entity.setStringShort(new HashMap<>());
		entity.setStringInteger(new HashMap<>());
		entity.setStringLong(new HashMap<>());
		entity.setStringFloat(new HashMap<>());
		entity.setStringDouble(new HashMap<>());
		entity.setStringDate(new HashMap<>());
		entity.setStringString(new HashMap<>());
		entity.setStringLocalDate(new HashMap<>());
		entity.setStringLocalTime(new HashMap<>());
		entity.setStringLocalDateTime(new HashMap<>());
		entity.setStringBigDecimal(new HashMap<>());
		entity.setStringEnumCodes(new HashMap<>());
		entity.setStringEnumTexts(new HashMap<>());
		entity.setStringEnumNative(new HashMap<>());
		entity.setStringEnumCodeTexts(new HashMap<>());
		entity.setStringEntityBase(new HashMap<>());
		entity.setStringEntityEmpty(new HashMap<>());
		entity.setLocalDateBoolean(new HashMap<>());
		entity.setLocalDateByte(new HashMap<>());
		entity.setLocalDateCharacter(new HashMap<>());
		entity.setLocalDateShort(new HashMap<>());
		entity.setLocalDateInteger(new HashMap<>());
		entity.setLocalDateLong(new HashMap<>());
		entity.setLocalDateFloat(new HashMap<>());
		entity.setLocalDateDouble(new HashMap<>());
		entity.setLocalDateDate(new HashMap<>());
		entity.setLocalDateString(new HashMap<>());
		entity.setLocalDateLocalDate(new HashMap<>());
		entity.setLocalDateLocalTime(new HashMap<>());
		entity.setLocalDateLocalDateTime(new HashMap<>());
		entity.setLocalDateBigDecimal(new HashMap<>());
		entity.setLocalDateEnumCodes(new HashMap<>());
		entity.setLocalDateEnumTexts(new HashMap<>());
		entity.setLocalDateEnumNative(new HashMap<>());
		entity.setLocalDateEnumCodeTexts(new HashMap<>());
		entity.setLocalDateEntityBase(new HashMap<>());
		entity.setLocalDateEntityEmpty(new HashMap<>());
		entity.setLocalTimeBoolean(new HashMap<>());
		entity.setLocalTimeByte(new HashMap<>());
		entity.setLocalTimeCharacter(new HashMap<>());
		entity.setLocalTimeShort(new HashMap<>());
		entity.setLocalTimeInteger(new HashMap<>());
		entity.setLocalTimeLong(new HashMap<>());
		entity.setLocalTimeFloat(new HashMap<>());
		entity.setLocalTimeDouble(new HashMap<>());
		entity.setLocalTimeDate(new HashMap<>());
		entity.setLocalTimeString(new HashMap<>());
		entity.setLocalTimeLocalDate(new HashMap<>());
		entity.setLocalTimeLocalTime(new HashMap<>());
		entity.setLocalTimeLocalDateTime(new HashMap<>());
		entity.setLocalTimeBigDecimal(new HashMap<>());
		entity.setLocalTimeEnumCodes(new HashMap<>());
		entity.setLocalTimeEnumTexts(new HashMap<>());
		entity.setLocalTimeEnumNative(new HashMap<>());
		entity.setLocalTimeEnumCodeTexts(new HashMap<>());
		entity.setLocalTimeEntityBase(new HashMap<>());
		entity.setLocalTimeEntityEmpty(new HashMap<>());
		entity.setLocalDateTimeBoolean(new HashMap<>());
		entity.setLocalDateTimeByte(new HashMap<>());
		entity.setLocalDateTimeCharacter(new HashMap<>());
		entity.setLocalDateTimeShort(new HashMap<>());
		entity.setLocalDateTimeInteger(new HashMap<>());
		entity.setLocalDateTimeLong(new HashMap<>());
		entity.setLocalDateTimeFloat(new HashMap<>());
		entity.setLocalDateTimeDouble(new HashMap<>());
		entity.setLocalDateTimeDate(new HashMap<>());
		entity.setLocalDateTimeString(new HashMap<>());
		entity.setLocalDateTimeLocalDate(new HashMap<>());
		entity.setLocalDateTimeLocalTime(new HashMap<>());
		entity.setLocalDateTimeLocalDateTime(new HashMap<>());
		entity.setLocalDateTimeBigDecimal(new HashMap<>());
		entity.setLocalDateTimeEnumCodes(new HashMap<>());
		entity.setLocalDateTimeEnumTexts(new HashMap<>());
		entity.setLocalDateTimeEnumNative(new HashMap<>());
		entity.setLocalDateTimeEnumCodeTexts(new HashMap<>());
		entity.setLocalDateTimeEntityBase(new HashMap<>());
		entity.setLocalDateTimeEntityEmpty(new HashMap<>());
		entity.setBigDecimalBoolean(new HashMap<>());
		entity.setBigDecimalByte(new HashMap<>());
		entity.setBigDecimalCharacter(new HashMap<>());
		entity.setBigDecimalShort(new HashMap<>());
		entity.setBigDecimalInteger(new HashMap<>());
		entity.setBigDecimalLong(new HashMap<>());
		entity.setBigDecimalFloat(new HashMap<>());
		entity.setBigDecimalDouble(new HashMap<>());
		entity.setBigDecimalDate(new HashMap<>());
		entity.setBigDecimalString(new HashMap<>());
		entity.setBigDecimalLocalDate(new HashMap<>());
		entity.setBigDecimalLocalTime(new HashMap<>());
		entity.setBigDecimalLocalDateTime(new HashMap<>());
		entity.setBigDecimalBigDecimal(new HashMap<>());
		entity.setBigDecimalEnumCodes(new HashMap<>());
		entity.setBigDecimalEnumTexts(new HashMap<>());
		entity.setBigDecimalEnumNative(new HashMap<>());
		entity.setBigDecimalEnumCodeTexts(new HashMap<>());
		entity.setBigDecimalEntityBase(new HashMap<>());
		entity.setBigDecimalEntityEmpty(new HashMap<>());
		return entity;
	}

	static EntityMap createNormalValue() {
		final EntityBase[] bases = new EntityBase[] { EntityBase.createNullValue(), EntityBase.createMinValue(), EntityBase.createMaxValue() };
		final EntityEmpty[] empties = new EntityEmpty[] { new EntityEmpty(), new EntityEmpty(), new EntityEmpty() };
		final EntityMap entity = createEmptyValue();
		fill(entity.getBooleanBoolean(), EntityArray.BOOLEAN_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getBooleanByte(), EntityArray.BOOLEAN_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getBooleanCharacter(), EntityArray.BOOLEAN_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getBooleanShort(), EntityArray.BOOLEAN_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getBooleanInteger(), EntityArray.BOOLEAN_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getBooleanLong(), EntityArray.BOOLEAN_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getBooleanFloat(), EntityArray.BOOLEAN_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getBooleanDouble(), EntityArray.BOOLEAN_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getBooleanDate(), EntityArray.BOOLEAN_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getBooleanString(), EntityArray.BOOLEAN_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getBooleanLocalDate(), EntityArray.BOOLEAN_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getBooleanLocalTime(), EntityArray.BOOLEAN_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getBooleanLocalDateTime(), EntityArray.BOOLEAN_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getBooleanBigDecimal(), EntityArray.BOOLEAN_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getBooleanEnumCodes(), EntityArray.BOOLEAN_OBJECTS, EnumCodes.values());
		fill(entity.getBooleanEnumTexts(), EntityArray.BOOLEAN_OBJECTS, EnumTexts.values());
		fill(entity.getBooleanEnumNative(), EntityArray.BOOLEAN_OBJECTS, EnumNative.values());
		fill(entity.getBooleanEnumCodeTexts(), EntityArray.BOOLEAN_OBJECTS, EnumCodeTexts.values());
		fill(entity.getBooleanEntityBase(), EntityArray.BOOLEAN_OBJECTS, bases);
		fill(entity.getBooleanEntityEmpty(), EntityArray.BOOLEAN_OBJECTS, empties);
		fill(entity.getByteBoolean(), EntityArray.BYTE_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getByteByte(), EntityArray.BYTE_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getByteCharacter(), EntityArray.BYTE_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getByteShort(), EntityArray.BYTE_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getByteInteger(), EntityArray.BYTE_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getByteLong(), EntityArray.BYTE_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getByteFloat(), EntityArray.BYTE_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getByteDouble(), EntityArray.BYTE_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getByteDate(), EntityArray.BYTE_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getByteString(), EntityArray.BYTE_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getByteLocalDate(), EntityArray.BYTE_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getByteLocalTime(), EntityArray.BYTE_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getByteLocalDateTime(), EntityArray.BYTE_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getByteBigDecimal(), EntityArray.BYTE_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getByteEnumCodes(), EntityArray.BYTE_OBJECTS, EnumCodes.values());
		fill(entity.getByteEnumTexts(), EntityArray.BYTE_OBJECTS, EnumTexts.values());
		fill(entity.getByteEnumNative(), EntityArray.BYTE_OBJECTS, EnumNative.values());
		fill(entity.getByteEnumCodeTexts(), EntityArray.BYTE_OBJECTS, EnumCodeTexts.values());
		fill(entity.getByteEntityBase(), EntityArray.BYTE_OBJECTS, bases);
		fill(entity.getByteEntityEmpty(), EntityArray.BYTE_OBJECTS, empties);
		fill(entity.getCharacterBoolean(), EntityArray.CHARACTER_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getCharacterByte(), EntityArray.CHARACTER_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getCharacterCharacter(), EntityArray.CHARACTER_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getCharacterShort(), EntityArray.CHARACTER_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getCharacterInteger(), EntityArray.CHARACTER_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getCharacterLong(), EntityArray.CHARACTER_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getCharacterFloat(), EntityArray.CHARACTER_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getCharacterDouble(), EntityArray.CHARACTER_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getCharacterDate(), EntityArray.CHARACTER_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getCharacterString(), EntityArray.CHARACTER_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getCharacterLocalDate(), EntityArray.CHARACTER_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getCharacterLocalTime(), EntityArray.CHARACTER_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getCharacterLocalDateTime(), EntityArray.CHARACTER_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getCharacterBigDecimal(), EntityArray.CHARACTER_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getCharacterEnumCodes(), EntityArray.CHARACTER_OBJECTS, EnumCodes.values());
		fill(entity.getCharacterEnumTexts(), EntityArray.CHARACTER_OBJECTS, EnumTexts.values());
		fill(entity.getCharacterEnumNative(), EntityArray.CHARACTER_OBJECTS, EnumNative.values());
		fill(entity.getCharacterEnumCodeTexts(), EntityArray.CHARACTER_OBJECTS, EnumCodeTexts.values());
		fill(entity.getCharacterEntityBase(), EntityArray.CHARACTER_OBJECTS, bases);
		fill(entity.getCharacterEntityEmpty(), EntityArray.CHARACTER_OBJECTS, empties);
		fill(entity.getShortBoolean(), EntityArray.SHORT_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getShortByte(), EntityArray.SHORT_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getShortCharacter(), EntityArray.SHORT_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getShortShort(), EntityArray.SHORT_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getShortInteger(), EntityArray.SHORT_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getShortLong(), EntityArray.SHORT_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getShortFloat(), EntityArray.SHORT_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getShortDouble(), EntityArray.SHORT_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getShortDate(), EntityArray.SHORT_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getShortString(), EntityArray.SHORT_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getShortLocalDate(), EntityArray.SHORT_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getShortLocalTime(), EntityArray.SHORT_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getShortLocalDateTime(), EntityArray.SHORT_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getShortBigDecimal(), EntityArray.SHORT_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getShortEnumCodes(), EntityArray.SHORT_OBJECTS, EnumCodes.values());
		fill(entity.getShortEnumTexts(), EntityArray.SHORT_OBJECTS, EnumTexts.values());
		fill(entity.getShortEnumNative(), EntityArray.SHORT_OBJECTS, EnumNative.values());
		fill(entity.getShortEnumCodeTexts(), EntityArray.SHORT_OBJECTS, EnumCodeTexts.values());
		fill(entity.getShortEntityBase(), EntityArray.SHORT_OBJECTS, bases);
		fill(entity.getShortEntityEmpty(), EntityArray.SHORT_OBJECTS, empties);
		fill(entity.getIntegerBoolean(), EntityArray.INTEGER_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getIntegerByte(), EntityArray.INTEGER_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getIntegerCharacter(), EntityArray.INTEGER_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getIntegerShort(), EntityArray.INTEGER_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getIntegerInteger(), EntityArray.INTEGER_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getIntegerLong(), EntityArray.INTEGER_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getIntegerFloat(), EntityArray.INTEGER_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getIntegerDouble(), EntityArray.INTEGER_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getIntegerDate(), EntityArray.INTEGER_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getIntegerString(), EntityArray.INTEGER_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getIntegerLocalDate(), EntityArray.INTEGER_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getIntegerLocalTime(), EntityArray.INTEGER_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getIntegerLocalDateTime(), EntityArray.INTEGER_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getIntegerBigDecimal(), EntityArray.INTEGER_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getIntegerEnumCodes(), EntityArray.INTEGER_OBJECTS, EnumCodes.values());
		fill(entity.getIntegerEnumTexts(), EntityArray.INTEGER_OBJECTS, EnumTexts.values());
		fill(entity.getIntegerEnumNative(), EntityArray.INTEGER_OBJECTS, EnumNative.values());
		fill(entity.getIntegerEnumCodeTexts(), EntityArray.INTEGER_OBJECTS, EnumCodeTexts.values());
		fill(entity.getIntegerEntityBase(), EntityArray.INTEGER_OBJECTS, bases);
		fill(entity.getIntegerEntityEmpty(), EntityArray.INTEGER_OBJECTS, empties);
		fill(entity.getLongBoolean(), EntityArray.LONG_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getLongByte(), EntityArray.LONG_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getLongCharacter(), EntityArray.LONG_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getLongShort(), EntityArray.LONG_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getLongInteger(), EntityArray.LONG_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getLongLong(), EntityArray.LONG_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getLongFloat(), EntityArray.LONG_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getLongDouble(), EntityArray.LONG_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getLongDate(), EntityArray.LONG_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getLongString(), EntityArray.LONG_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getLongLocalDate(), EntityArray.LONG_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getLongLocalTime(), EntityArray.LONG_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getLongLocalDateTime(), EntityArray.LONG_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getLongBigDecimal(), EntityArray.LONG_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getLongEnumCodes(), EntityArray.LONG_OBJECTS, EnumCodes.values());
		fill(entity.getLongEnumTexts(), EntityArray.LONG_OBJECTS, EnumTexts.values());
		fill(entity.getLongEnumNative(), EntityArray.LONG_OBJECTS, EnumNative.values());
		fill(entity.getLongEnumCodeTexts(), EntityArray.LONG_OBJECTS, EnumCodeTexts.values());
		fill(entity.getLongEntityBase(), EntityArray.LONG_OBJECTS, bases);
		fill(entity.getLongEntityEmpty(), EntityArray.LONG_OBJECTS, empties);
		fill(entity.getFloatBoolean(), EntityArray.FLOAT_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getFloatByte(), EntityArray.FLOAT_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getFloatCharacter(), EntityArray.FLOAT_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getFloatShort(), EntityArray.FLOAT_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getFloatInteger(), EntityArray.FLOAT_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getFloatLong(), EntityArray.FLOAT_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getFloatFloat(), EntityArray.FLOAT_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getFloatDouble(), EntityArray.FLOAT_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getFloatDate(), EntityArray.FLOAT_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getFloatString(), EntityArray.FLOAT_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getFloatLocalDate(), EntityArray.FLOAT_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getFloatLocalTime(), EntityArray.FLOAT_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getFloatLocalDateTime(), EntityArray.FLOAT_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getFloatBigDecimal(), EntityArray.FLOAT_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getFloatEnumCodes(), EntityArray.FLOAT_OBJECTS, EnumCodes.values());
		fill(entity.getFloatEnumTexts(), EntityArray.FLOAT_OBJECTS, EnumTexts.values());
		fill(entity.getFloatEnumNative(), EntityArray.FLOAT_OBJECTS, EnumNative.values());
		fill(entity.getFloatEnumCodeTexts(), EntityArray.FLOAT_OBJECTS, EnumCodeTexts.values());
		fill(entity.getFloatEntityBase(), EntityArray.FLOAT_OBJECTS, bases);
		fill(entity.getFloatEntityEmpty(), EntityArray.FLOAT_OBJECTS, empties);
		fill(entity.getDoubleBoolean(), EntityArray.DOUBLE_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getDoubleByte(), EntityArray.DOUBLE_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getDoubleCharacter(), EntityArray.DOUBLE_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getDoubleShort(), EntityArray.DOUBLE_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getDoubleInteger(), EntityArray.DOUBLE_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getDoubleLong(), EntityArray.DOUBLE_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getDoubleFloat(), EntityArray.DOUBLE_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getDoubleDouble(), EntityArray.DOUBLE_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getDoubleDate(), EntityArray.DOUBLE_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getDoubleString(), EntityArray.DOUBLE_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getDoubleLocalDate(), EntityArray.DOUBLE_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getDoubleLocalTime(), EntityArray.DOUBLE_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getDoubleLocalDateTime(), EntityArray.DOUBLE_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getDoubleBigDecimal(), EntityArray.DOUBLE_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getDoubleEnumCodes(), EntityArray.DOUBLE_OBJECTS, EnumCodes.values());
		fill(entity.getDoubleEnumTexts(), EntityArray.DOUBLE_OBJECTS, EnumTexts.values());
		fill(entity.getDoubleEnumNative(), EntityArray.DOUBLE_OBJECTS, EnumNative.values());
		fill(entity.getDoubleEnumCodeTexts(), EntityArray.DOUBLE_OBJECTS, EnumCodeTexts.values());
		fill(entity.getDoubleEntityBase(), EntityArray.DOUBLE_OBJECTS, bases);
		fill(entity.getDoubleEntityEmpty(), EntityArray.DOUBLE_OBJECTS, empties);
		fill(entity.getDateBoolean(), EntityArray.DATE_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getDateByte(), EntityArray.DATE_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getDateCharacter(), EntityArray.DATE_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getDateShort(), EntityArray.DATE_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getDateInteger(), EntityArray.DATE_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getDateLong(), EntityArray.DATE_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getDateFloat(), EntityArray.DATE_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getDateDouble(), EntityArray.DATE_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getDateDate(), EntityArray.DATE_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getDateString(), EntityArray.DATE_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getDateLocalDate(), EntityArray.DATE_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getDateLocalTime(), EntityArray.DATE_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getDateLocalDateTime(), EntityArray.DATE_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getDateBigDecimal(), EntityArray.DATE_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getDateEnumCodes(), EntityArray.DATE_OBJECTS, EnumCodes.values());
		fill(entity.getDateEnumTexts(), EntityArray.DATE_OBJECTS, EnumTexts.values());
		fill(entity.getDateEnumNative(), EntityArray.DATE_OBJECTS, EnumNative.values());
		fill(entity.getDateEnumCodeTexts(), EntityArray.DATE_OBJECTS, EnumCodeTexts.values());
		fill(entity.getDateEntityBase(), EntityArray.DATE_OBJECTS, bases);
		fill(entity.getDateEntityEmpty(), EntityArray.DATE_OBJECTS, empties);
		fill(entity.getStringBoolean(), EntityArray.STRING_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getStringByte(), EntityArray.STRING_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getStringCharacter(), EntityArray.STRING_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getStringShort(), EntityArray.STRING_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getStringInteger(), EntityArray.STRING_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getStringLong(), EntityArray.STRING_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getStringFloat(), EntityArray.STRING_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getStringDouble(), EntityArray.STRING_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getStringDate(), EntityArray.STRING_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getStringString(), EntityArray.STRING_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getStringLocalDate(), EntityArray.STRING_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getStringLocalTime(), EntityArray.STRING_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getStringLocalDateTime(), EntityArray.STRING_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getStringBigDecimal(), EntityArray.STRING_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getStringEnumCodes(), EntityArray.STRING_OBJECTS, EnumCodes.values());
		fill(entity.getStringEnumTexts(), EntityArray.STRING_OBJECTS, EnumTexts.values());
		fill(entity.getStringEnumNative(), EntityArray.STRING_OBJECTS, EnumNative.values());
		fill(entity.getStringEnumCodeTexts(), EntityArray.STRING_OBJECTS, EnumCodeTexts.values());
		fill(entity.getStringEntityBase(), EntityArray.STRING_OBJECTS, bases);
		fill(entity.getStringEntityEmpty(), EntityArray.STRING_OBJECTS, empties);
		fill(entity.getLocalDateBoolean(), EntityArray.LOCALDATE_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getLocalDateByte(), EntityArray.LOCALDATE_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getLocalDateCharacter(), EntityArray.LOCALDATE_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getLocalDateShort(), EntityArray.LOCALDATE_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getLocalDateInteger(), EntityArray.LOCALDATE_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getLocalDateLong(), EntityArray.LOCALDATE_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getLocalDateFloat(), EntityArray.LOCALDATE_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getLocalDateDouble(), EntityArray.LOCALDATE_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getLocalDateDate(), EntityArray.LOCALDATE_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getLocalDateString(), EntityArray.LOCALDATE_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getLocalDateLocalDate(), EntityArray.LOCALDATE_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getLocalDateLocalTime(), EntityArray.LOCALDATE_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getLocalDateLocalDateTime(), EntityArray.LOCALDATE_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getLocalDateBigDecimal(), EntityArray.LOCALDATE_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getLocalDateEnumCodes(), EntityArray.LOCALDATE_OBJECTS, EnumCodes.values());
		fill(entity.getLocalDateEnumTexts(), EntityArray.LOCALDATE_OBJECTS, EnumTexts.values());
		fill(entity.getLocalDateEnumNative(), EntityArray.LOCALDATE_OBJECTS, EnumNative.values());
		fill(entity.getLocalDateEnumCodeTexts(), EntityArray.LOCALDATE_OBJECTS, EnumCodeTexts.values());
		fill(entity.getLocalDateEntityBase(), EntityArray.LOCALDATE_OBJECTS, bases);
		fill(entity.getLocalDateEntityEmpty(), EntityArray.LOCALDATE_OBJECTS, empties);
		fill(entity.getLocalTimeBoolean(), EntityArray.LOCALTIME_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getLocalTimeByte(), EntityArray.LOCALTIME_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getLocalTimeCharacter(), EntityArray.LOCALTIME_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getLocalTimeShort(), EntityArray.LOCALTIME_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getLocalTimeInteger(), EntityArray.LOCALTIME_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getLocalTimeLong(), EntityArray.LOCALTIME_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getLocalTimeFloat(), EntityArray.LOCALTIME_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getLocalTimeDouble(), EntityArray.LOCALTIME_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getLocalTimeDate(), EntityArray.LOCALTIME_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getLocalTimeString(), EntityArray.LOCALTIME_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getLocalTimeLocalDate(), EntityArray.LOCALTIME_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getLocalTimeLocalTime(), EntityArray.LOCALTIME_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getLocalTimeLocalDateTime(), EntityArray.LOCALTIME_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getLocalTimeBigDecimal(), EntityArray.LOCALTIME_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getLocalTimeEnumCodes(), EntityArray.LOCALTIME_OBJECTS, EnumCodes.values());
		fill(entity.getLocalTimeEnumTexts(), EntityArray.LOCALTIME_OBJECTS, EnumTexts.values());
		fill(entity.getLocalTimeEnumNative(), EntityArray.LOCALTIME_OBJECTS, EnumNative.values());
		fill(entity.getLocalTimeEnumCodeTexts(), EntityArray.LOCALTIME_OBJECTS, EnumCodeTexts.values());
		fill(entity.getLocalTimeEntityBase(), EntityArray.LOCALTIME_OBJECTS, bases);
		fill(entity.getLocalTimeEntityEmpty(), EntityArray.LOCALTIME_OBJECTS, empties);
		fill(entity.getLocalDateTimeBoolean(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getLocalDateTimeByte(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getLocalDateTimeCharacter(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getLocalDateTimeShort(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getLocalDateTimeInteger(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getLocalDateTimeLong(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getLocalDateTimeFloat(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getLocalDateTimeDouble(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getLocalDateTimeDate(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getLocalDateTimeString(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getLocalDateTimeLocalDate(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getLocalDateTimeLocalTime(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getLocalDateTimeLocalDateTime(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getLocalDateTimeBigDecimal(), EntityArray.LOCALDATETIME_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getLocalDateTimeEnumCodes(), EntityArray.LOCALDATETIME_OBJECTS, EnumCodes.values());
		fill(entity.getLocalDateTimeEnumTexts(), EntityArray.LOCALDATETIME_OBJECTS, EnumTexts.values());
		fill(entity.getLocalDateTimeEnumNative(), EntityArray.LOCALDATETIME_OBJECTS, EnumNative.values());
		fill(entity.getLocalDateTimeEnumCodeTexts(), EntityArray.LOCALDATETIME_OBJECTS, EnumCodeTexts.values());
		fill(entity.getLocalDateTimeEntityBase(), EntityArray.LOCALDATETIME_OBJECTS, bases);
		fill(entity.getLocalDateTimeEntityEmpty(), EntityArray.LOCALDATETIME_OBJECTS, empties);
		fill(entity.getBigDecimalBoolean(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.BOOLEAN_OBJECTS);
		fill(entity.getBigDecimalByte(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.BYTE_OBJECTS);
		fill(entity.getBigDecimalCharacter(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.CHARACTER_OBJECTS);
		fill(entity.getBigDecimalShort(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.SHORT_OBJECTS);
		fill(entity.getBigDecimalInteger(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.INTEGER_OBJECTS);
		fill(entity.getBigDecimalLong(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.LONG_OBJECTS);
		fill(entity.getBigDecimalFloat(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.FLOAT_OBJECTS);
		fill(entity.getBigDecimalDouble(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.DOUBLE_OBJECTS);
		fill(entity.getBigDecimalDate(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.DATE_OBJECTS);
		fill(entity.getBigDecimalString(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.STRING_OBJECTS);
		fill(entity.getBigDecimalLocalDate(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.LOCALDATE_OBJECTS);
		fill(entity.getBigDecimalLocalTime(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.LOCALTIME_OBJECTS);
		fill(entity.getBigDecimalLocalDateTime(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.LOCALDATETIME_OBJECTS);
		fill(entity.getBigDecimalBigDecimal(), EntityArray.BIGDECIMAL_OBJECTS, EntityArray.BIGDECIMAL_OBJECTS);
		fill(entity.getBigDecimalEnumCodes(), EntityArray.BIGDECIMAL_OBJECTS, EnumCodes.values());
		fill(entity.getBigDecimalEnumTexts(), EntityArray.BIGDECIMAL_OBJECTS, EnumTexts.values());
		fill(entity.getBigDecimalEnumNative(), EntityArray.BIGDECIMAL_OBJECTS, EnumNative.values());
		fill(entity.getBigDecimalEnumCodeTexts(), EntityArray.BIGDECIMAL_OBJECTS, EnumCodeTexts.values());
		fill(entity.getBigDecimalEntityBase(), EntityArray.BIGDECIMAL_OBJECTS, bases);
		fill(entity.getBigDecimalEntityEmpty(), EntityArray.BIGDECIMAL_OBJECTS, empties);
		return entity;
	}

	static <K, V> void fill(Map<K, V> map, K[] keys, V[] values) {
		int v = 0;
		for (int k = 0; k < keys.length; k++) {
			if (keys[k] == null) {
				continue;
			}
			map.put(keys[k], values[v++]);
			v = v < values.length ? v : 0;
		}
	}

	static void assertEntity(EntityMap a, EntityMap b) {
		assertEquals(a.getBooleanBoolean(), b.getBooleanBoolean());
		assertEquals(a.getBooleanByte(), b.getBooleanByte());
		assertEquals(a.getBooleanCharacter(), b.getBooleanCharacter());
		assertEquals(a.getBooleanShort(), b.getBooleanShort());
		assertEquals(a.getBooleanInteger(), b.getBooleanInteger());
		assertEquals(a.getBooleanLong(), b.getBooleanLong());
		assertEquals(a.getBooleanFloat(), b.getBooleanFloat());
		assertEquals(a.getBooleanDouble(), b.getBooleanDouble());
		// assertEquals(a.getBooleanDate(), b.getBooleanDate());
		assertEquals(a.getBooleanString(), b.getBooleanString());
		assertEquals(a.getBooleanLocalDate(), b.getBooleanLocalDate());
		// assertEquals(a.getBooleanLocalTime(), b.getBooleanLocalTime());
		// assertEquals(a.getBooleanLocalDateTime(),
		// b.getBooleanLocalDateTime());
		assertEquals(a.getBooleanBigDecimal(), b.getBooleanBigDecimal());
		assertEquals(a.getBooleanEnumCodes(), b.getBooleanEnumCodes());
		assertEquals(a.getBooleanEnumTexts(), b.getBooleanEnumTexts());
		assertEquals(a.getBooleanEnumNative(), b.getBooleanEnumNative());
		assertEquals(a.getBooleanEnumCodeTexts(), b.getBooleanEnumCodeTexts());
		assertEquals(a.getBooleanEntityBase(), b.getBooleanEntityBase());
		assertEquals(a.getBooleanEntityEmpty(), b.getBooleanEntityEmpty());
		assertEquals(a.getByteBoolean(), b.getByteBoolean());
		assertEquals(a.getByteByte(), b.getByteByte());
		assertEquals(a.getByteCharacter(), b.getByteCharacter());
		assertEquals(a.getByteShort(), b.getByteShort());
		assertEquals(a.getByteInteger(), b.getByteInteger());
		assertEquals(a.getByteLong(), b.getByteLong());
		assertEquals(a.getByteFloat(), b.getByteFloat());
		assertEquals(a.getByteDouble(), b.getByteDouble());
		// assertEquals(a.getByteDate(), b.getByteDate());
		assertEquals(a.getByteString(), b.getByteString());
		assertEquals(a.getByteLocalDate(), b.getByteLocalDate());
		// assertEquals(a.getByteLocalTime(), b.getByteLocalTime());
		// assertEquals(a.getByteLocalDateTime(), b.getByteLocalDateTime());
		assertEquals(a.getByteBigDecimal(), b.getByteBigDecimal());
		assertEquals(a.getByteEnumCodes(), b.getByteEnumCodes());
		assertEquals(a.getByteEnumTexts(), b.getByteEnumTexts());
		assertEquals(a.getByteEnumNative(), b.getByteEnumNative());
		assertEquals(a.getByteEnumCodeTexts(), b.getByteEnumCodeTexts());
		assertEquals(a.getByteEntityBase(), b.getByteEntityBase());
		assertEquals(a.getByteEntityEmpty(), b.getByteEntityEmpty());
		assertEquals(a.getCharacterBoolean(), b.getCharacterBoolean());
		assertEquals(a.getCharacterByte(), b.getCharacterByte());
		assertEquals(a.getCharacterCharacter(), b.getCharacterCharacter());
		assertEquals(a.getCharacterShort(), b.getCharacterShort());
		assertEquals(a.getCharacterInteger(), b.getCharacterInteger());
		assertEquals(a.getCharacterLong(), b.getCharacterLong());
		assertEquals(a.getCharacterFloat(), b.getCharacterFloat());
		assertEquals(a.getCharacterDouble(), b.getCharacterDouble());
		// assertEquals(a.getCharacterDate(), b.getCharacterDate());
		assertEquals(a.getCharacterString(), b.getCharacterString());
		assertEquals(a.getCharacterLocalDate(), b.getCharacterLocalDate());
		// assertEquals(a.getCharacterLocalTime(), b.getCharacterLocalTime());
		// assertEquals(a.getCharacterLocalDateTime(),
		// b.getCharacterLocalDateTime());
		assertEquals(a.getCharacterBigDecimal(), b.getCharacterBigDecimal());
		assertEquals(a.getCharacterEnumCodes(), b.getCharacterEnumCodes());
		assertEquals(a.getCharacterEnumTexts(), b.getCharacterEnumTexts());
		assertEquals(a.getCharacterEnumNative(), b.getCharacterEnumNative());
		assertEquals(a.getCharacterEnumCodeTexts(), b.getCharacterEnumCodeTexts());
		assertEquals(a.getCharacterEntityBase(), b.getCharacterEntityBase());
		assertEquals(a.getCharacterEntityEmpty(), b.getCharacterEntityEmpty());
		assertEquals(a.getShortBoolean(), b.getShortBoolean());
		assertEquals(a.getShortByte(), b.getShortByte());
		assertEquals(a.getShortCharacter(), b.getShortCharacter());
		assertEquals(a.getShortShort(), b.getShortShort());
		assertEquals(a.getShortInteger(), b.getShortInteger());
		assertEquals(a.getShortLong(), b.getShortLong());
		assertEquals(a.getShortFloat(), b.getShortFloat());
		assertEquals(a.getShortDouble(), b.getShortDouble());
		// assertEquals(a.getShortDate(), b.getShortDate());
		assertEquals(a.getShortString(), b.getShortString());
		assertEquals(a.getShortLocalDate(), b.getShortLocalDate());
		// assertEquals(a.getShortLocalTime(), b.getShortLocalTime());
		// assertEquals(a.getShortLocalDateTime(), b.getShortLocalDateTime());
		assertEquals(a.getShortBigDecimal(), b.getShortBigDecimal());
		assertEquals(a.getShortEnumCodes(), b.getShortEnumCodes());
		assertEquals(a.getShortEnumTexts(), b.getShortEnumTexts());
		assertEquals(a.getShortEnumNative(), b.getShortEnumNative());
		assertEquals(a.getShortEnumCodeTexts(), b.getShortEnumCodeTexts());
		assertEquals(a.getShortEntityBase(), b.getShortEntityBase());
		assertEquals(a.getShortEntityEmpty(), b.getShortEntityEmpty());
		assertEquals(a.getIntegerBoolean(), b.getIntegerBoolean());
		assertEquals(a.getIntegerByte(), b.getIntegerByte());
		assertEquals(a.getIntegerCharacter(), b.getIntegerCharacter());
		assertEquals(a.getIntegerShort(), b.getIntegerShort());
		assertEquals(a.getIntegerInteger(), b.getIntegerInteger());
		assertEquals(a.getIntegerLong(), b.getIntegerLong());
		assertEquals(a.getIntegerFloat(), b.getIntegerFloat());
		assertEquals(a.getIntegerDouble(), b.getIntegerDouble());
		// assertEquals(a.getIntegerDate(), b.getIntegerDate());
		assertEquals(a.getIntegerString(), b.getIntegerString());
		assertEquals(a.getIntegerLocalDate(), b.getIntegerLocalDate());
		// assertEquals(a.getIntegerLocalTime(), b.getIntegerLocalTime());
		// assertEquals(a.getIntegerLocalDateTime(),
		// b.getIntegerLocalDateTime());
		assertEquals(a.getIntegerBigDecimal(), b.getIntegerBigDecimal());
		assertEquals(a.getIntegerEnumCodes(), b.getIntegerEnumCodes());
		assertEquals(a.getIntegerEnumTexts(), b.getIntegerEnumTexts());
		assertEquals(a.getIntegerEnumNative(), b.getIntegerEnumNative());
		assertEquals(a.getIntegerEnumCodeTexts(), b.getIntegerEnumCodeTexts());
		assertEquals(a.getIntegerEntityBase(), b.getIntegerEntityBase());
		assertEquals(a.getIntegerEntityEmpty(), b.getIntegerEntityEmpty());
		assertEquals(a.getLongBoolean(), b.getLongBoolean());
		assertEquals(a.getLongByte(), b.getLongByte());
		assertEquals(a.getLongCharacter(), b.getLongCharacter());
		assertEquals(a.getLongShort(), b.getLongShort());
		assertEquals(a.getLongInteger(), b.getLongInteger());
		assertEquals(a.getLongLong(), b.getLongLong());
		assertEquals(a.getLongFloat(), b.getLongFloat());
		assertEquals(a.getLongDouble(), b.getLongDouble());
		// assertEquals(a.getLongDate(), b.getLongDate());
		assertEquals(a.getLongString(), b.getLongString());
		assertEquals(a.getLongLocalDate(), b.getLongLocalDate());
		// assertEquals(a.getLongLocalTime(), b.getLongLocalTime());
		// assertEquals(a.getLongLocalDateTime(), b.getLongLocalDateTime());
		assertEquals(a.getLongBigDecimal(), b.getLongBigDecimal());
		assertEquals(a.getLongEnumCodes(), b.getLongEnumCodes());
		assertEquals(a.getLongEnumTexts(), b.getLongEnumTexts());
		assertEquals(a.getLongEnumNative(), b.getLongEnumNative());
		assertEquals(a.getLongEnumCodeTexts(), b.getLongEnumCodeTexts());
		assertEquals(a.getLongEntityBase(), b.getLongEntityBase());
		assertEquals(a.getLongEntityEmpty(), b.getLongEntityEmpty());
		assertEquals(a.getFloatBoolean(), b.getFloatBoolean());
		assertEquals(a.getFloatByte(), b.getFloatByte());
		assertEquals(a.getFloatCharacter(), b.getFloatCharacter());
		assertEquals(a.getFloatShort(), b.getFloatShort());
		assertEquals(a.getFloatInteger(), b.getFloatInteger());
		assertEquals(a.getFloatLong(), b.getFloatLong());
		assertEquals(a.getFloatFloat(), b.getFloatFloat());
		assertEquals(a.getFloatDouble(), b.getFloatDouble());
		// assertEquals(a.getFloatDate(), b.getFloatDate());
		assertEquals(a.getFloatString(), b.getFloatString());
		assertEquals(a.getFloatLocalDate(), b.getFloatLocalDate());
		// assertEquals(a.getFloatLocalTime(), b.getFloatLocalTime());
		// assertEquals(a.getFloatLocalDateTime(), b.getFloatLocalDateTime());
		assertEquals(a.getFloatBigDecimal(), b.getFloatBigDecimal());
		assertEquals(a.getFloatEnumCodes(), b.getFloatEnumCodes());
		assertEquals(a.getFloatEnumTexts(), b.getFloatEnumTexts());
		assertEquals(a.getFloatEnumNative(), b.getFloatEnumNative());
		assertEquals(a.getFloatEnumCodeTexts(), b.getFloatEnumCodeTexts());
		assertEquals(a.getFloatEntityBase(), b.getFloatEntityBase());
		assertEquals(a.getFloatEntityEmpty(), b.getFloatEntityEmpty());
		assertEquals(a.getDoubleBoolean(), b.getDoubleBoolean());
		assertEquals(a.getDoubleByte(), b.getDoubleByte());
		assertEquals(a.getDoubleCharacter(), b.getDoubleCharacter());
		assertEquals(a.getDoubleShort(), b.getDoubleShort());
		assertEquals(a.getDoubleInteger(), b.getDoubleInteger());
		assertEquals(a.getDoubleLong(), b.getDoubleLong());
		assertEquals(a.getDoubleFloat(), b.getDoubleFloat());
		assertEquals(a.getDoubleDouble(), b.getDoubleDouble());
		// assertEquals(a.getDoubleDate(), b.getDoubleDate());
		assertEquals(a.getDoubleString(), b.getDoubleString());
		assertEquals(a.getDoubleLocalDate(), b.getDoubleLocalDate());
		// assertEquals(a.getDoubleLocalTime(), b.getDoubleLocalTime());
		// assertEquals(a.getDoubleLocalDateTime(), b.getDoubleLocalDateTime());
		assertEquals(a.getDoubleBigDecimal(), b.getDoubleBigDecimal());
		assertEquals(a.getDoubleEnumCodes(), b.getDoubleEnumCodes());
		assertEquals(a.getDoubleEnumTexts(), b.getDoubleEnumTexts());
		assertEquals(a.getDoubleEnumNative(), b.getDoubleEnumNative());
		assertEquals(a.getDoubleEnumCodeTexts(), b.getDoubleEnumCodeTexts());
		assertEquals(a.getDoubleEntityBase(), b.getDoubleEntityBase());
		assertEquals(a.getDoubleEntityEmpty(), b.getDoubleEntityEmpty());
		// assertEquals(a.getDateBoolean(), b.getDateBoolean());
		// assertEquals(a.getDateByte(), b.getDateByte());
		// assertEquals(a.getDateCharacter(), b.getDateCharacter());
		// assertEquals(a.getDateShort(), b.getDateShort());
		// assertEquals(a.getDateInteger(), b.getDateInteger());
		// assertEquals(a.getDateLong(), b.getDateLong());
		// assertEquals(a.getDateFloat(), b.getDateFloat());
		// assertEquals(a.getDateDouble(), b.getDateDouble());
		// assertEquals(a.getDateDate(), b.getDateDate());
		// assertEquals(a.getDateString(), b.getDateString());
		// assertEquals(a.getDateLocalDate(), b.getDateLocalDate());
		// assertEquals(a.getDateLocalTime(), b.getDateLocalTime());
		// assertEquals(a.getDateLocalDateTime(), b.getDateLocalDateTime());
		// assertEquals(a.getDateBigDecimal(), b.getDateBigDecimal());
		// assertEquals(a.getDateEnumCodes(), b.getDateEnumCodes());
		// assertEquals(a.getDateEnumTexts(), b.getDateEnumTexts());
		// assertEquals(a.getDateEnumNative(), b.getDateEnumNative());
		// assertEquals(a.getDateEnumCodeTexts(), b.getDateEnumCodeTexts());
		// assertEquals(a.getDateEntityBase(), b.getDateEntityBase());
		// assertEquals(a.getDateEntityEmpty(), b.getDateEntityEmpty());
		assertEquals(a.getStringBoolean(), b.getStringBoolean());
		assertEquals(a.getStringByte(), b.getStringByte());
		assertEquals(a.getStringCharacter(), b.getStringCharacter());
		assertEquals(a.getStringShort(), b.getStringShort());
		assertEquals(a.getStringInteger(), b.getStringInteger());
		assertEquals(a.getStringLong(), b.getStringLong());
		assertEquals(a.getStringFloat(), b.getStringFloat());
		assertEquals(a.getStringDouble(), b.getStringDouble());
		// assertEquals(a.getStringDate(), b.getStringDate());
		assertEquals(a.getStringString(), b.getStringString());
		assertEquals(a.getStringLocalDate(), b.getStringLocalDate());
		// assertEquals(a.getStringLocalTime(), b.getStringLocalTime());
		// assertEquals(a.getStringLocalDateTime(), b.getStringLocalDateTime());
		assertEquals(a.getStringBigDecimal(), b.getStringBigDecimal());
		assertEquals(a.getStringEnumCodes(), b.getStringEnumCodes());
		assertEquals(a.getStringEnumTexts(), b.getStringEnumTexts());
		assertEquals(a.getStringEnumNative(), b.getStringEnumNative());
		assertEquals(a.getStringEnumCodeTexts(), b.getStringEnumCodeTexts());
		assertEquals(a.getStringEntityBase(), b.getStringEntityBase());
		assertEquals(a.getStringEntityEmpty(), b.getStringEntityEmpty());
		assertEquals(a.getLocalDateBoolean(), b.getLocalDateBoolean());
		assertEquals(a.getLocalDateByte(), b.getLocalDateByte());
		assertEquals(a.getLocalDateCharacter(), b.getLocalDateCharacter());
		assertEquals(a.getLocalDateShort(), b.getLocalDateShort());
		assertEquals(a.getLocalDateInteger(), b.getLocalDateInteger());
		assertEquals(a.getLocalDateLong(), b.getLocalDateLong());
		assertEquals(a.getLocalDateFloat(), b.getLocalDateFloat());
		assertEquals(a.getLocalDateDouble(), b.getLocalDateDouble());
		// assertEquals(a.getLocalDateDate(), b.getLocalDateDate());
		assertEquals(a.getLocalDateString(), b.getLocalDateString());
		assertEquals(a.getLocalDateLocalDate(), b.getLocalDateLocalDate());
		// assertEquals(a.getLocalDateLocalTime(), b.getLocalDateLocalTime());
		// assertEquals(a.getLocalDateLocalDateTime(),
		// b.getLocalDateLocalDateTime());
		assertEquals(a.getLocalDateBigDecimal(), b.getLocalDateBigDecimal());
		assertEquals(a.getLocalDateEnumCodes(), b.getLocalDateEnumCodes());
		assertEquals(a.getLocalDateEnumTexts(), b.getLocalDateEnumTexts());
		assertEquals(a.getLocalDateEnumNative(), b.getLocalDateEnumNative());
		assertEquals(a.getLocalDateEnumCodeTexts(), b.getLocalDateEnumCodeTexts());
		assertEquals(a.getLocalDateEntityBase(), b.getLocalDateEntityBase());
		assertEquals(a.getLocalDateEntityEmpty(), b.getLocalDateEntityEmpty());
		// assertEquals(a.getLocalTimeBoolean(), b.getLocalTimeBoolean());
		// assertEquals(a.getLocalTimeByte(), b.getLocalTimeByte());
		// assertEquals(a.getLocalTimeCharacter(), b.getLocalTimeCharacter());
		// assertEquals(a.getLocalTimeShort(), b.getLocalTimeShort());
		// assertEquals(a.getLocalTimeInteger(), b.getLocalTimeInteger());
		// assertEquals(a.getLocalTimeLong(), b.getLocalTimeLong());
		// assertEquals(a.getLocalTimeFloat(), b.getLocalTimeFloat());
		// assertEquals(a.getLocalTimeDouble(), b.getLocalTimeDouble());
		// assertEquals(a.getLocalTimeDate(), b.getLocalTimeDate());
		// assertEquals(a.getLocalTimeString(), b.getLocalTimeString());
		// assertEquals(a.getLocalTimeLocalDate(), b.getLocalTimeLocalDate());
		// assertEquals(a.getLocalTimeLocalTime(), b.getLocalTimeLocalTime());
		// assertEquals(a.getLocalTimeLocalDateTime(),
		// b.getLocalTimeLocalDateTime());
		// assertEquals(a.getLocalTimeBigDecimal(), b.getLocalTimeBigDecimal());
		// assertEquals(a.getLocalTimeEnumCodes(), b.getLocalTimeEnumCodes());
		// assertEquals(a.getLocalTimeEnumTexts(), b.getLocalTimeEnumTexts());
		// assertEquals(a.getLocalTimeEnumNative(), b.getLocalTimeEnumNative());
		// assertEquals(a.getLocalTimeEnumCodeTexts(),
		// b.getLocalTimeEnumCodeTexts());
		// assertEquals(a.getLocalTimeEntityBase(), b.getLocalTimeEntityBase());
		// assertEquals(a.getLocalTimeEntityEmpty(),
		// b.getLocalTimeEntityEmpty());
		// assertEquals(a.getLocalDateTimeBoolean(),
		// b.getLocalDateTimeBoolean());
		// assertEquals(a.getLocalDateTimeByte(), b.getLocalDateTimeByte());
		// assertEquals(a.getLocalDateTimeCharacter(),
		// b.getLocalDateTimeCharacter());
		// assertEquals(a.getLocalDateTimeShort(), b.getLocalDateTimeShort());
		// assertEquals(a.getLocalDateTimeInteger(),
		// b.getLocalDateTimeInteger());
		// assertEquals(a.getLocalDateTimeLong(), b.getLocalDateTimeLong());
		// assertEquals(a.getLocalDateTimeFloat(), b.getLocalDateTimeFloat());
		// assertEquals(a.getLocalDateTimeDouble(), b.getLocalDateTimeDouble());
		// assertEquals(a.getLocalDateTimeDate(), b.getLocalDateTimeDate());
		// assertEquals(a.getLocalDateTimeString(), b.getLocalDateTimeString());
		// assertEquals(a.getLocalDateTimeLocalDate(),
		// b.getLocalDateTimeLocalDate());
		// assertEquals(a.getLocalDateTimeLocalTime(),
		// b.getLocalDateTimeLocalTime());
		// assertEquals(a.getLocalDateTimeLocalDateTime(),
		// b.getLocalDateTimeLocalDateTime());
		// assertEquals(a.getLocalDateTimeBigDecimal(),
		// b.getLocalDateTimeBigDecimal());
		// assertEquals(a.getLocalDateTimeEnumCodes(),
		// b.getLocalDateTimeEnumCodes());
		// assertEquals(a.getLocalDateTimeEnumTexts(),
		// b.getLocalDateTimeEnumTexts());
		// assertEquals(a.getLocalDateTimeEnumNative(),
		// b.getLocalDateTimeEnumNative());
		// assertEquals(a.getLocalDateTimeEnumCodeTexts(),
		// b.getLocalDateTimeEnumCodeTexts());
		// assertEquals(a.getLocalDateTimeEntityBase(),
		// b.getLocalDateTimeEntityBase());
		// assertEquals(a.getLocalDateTimeEntityEmpty(),
		// b.getLocalDateTimeEntityEmpty());
		assertEquals(a.getBigDecimalBoolean(), b.getBigDecimalBoolean());
		assertEquals(a.getBigDecimalByte(), b.getBigDecimalByte());
		assertEquals(a.getBigDecimalCharacter(), b.getBigDecimalCharacter());
		assertEquals(a.getBigDecimalShort(), b.getBigDecimalShort());
		assertEquals(a.getBigDecimalInteger(), b.getBigDecimalInteger());
		assertEquals(a.getBigDecimalLong(), b.getBigDecimalLong());
		assertEquals(a.getBigDecimalFloat(), b.getBigDecimalFloat());
		assertEquals(a.getBigDecimalDouble(), b.getBigDecimalDouble());
		// assertEquals(a.getBigDecimalDate(), b.getBigDecimalDate());
		assertEquals(a.getBigDecimalString(), b.getBigDecimalString());
		assertEquals(a.getBigDecimalLocalDate(), b.getBigDecimalLocalDate());
		// assertEquals(a.getBigDecimalLocalTime(), b.getBigDecimalLocalTime());
		// assertEquals(a.getBigDecimalLocalDateTime(),
		// b.getBigDecimalLocalDateTime());
		assertEquals(a.getBigDecimalBigDecimal(), b.getBigDecimalBigDecimal());
		assertEquals(a.getBigDecimalEnumCodes(), b.getBigDecimalEnumCodes());
		assertEquals(a.getBigDecimalEnumTexts(), b.getBigDecimalEnumTexts());
		assertEquals(a.getBigDecimalEnumNative(), b.getBigDecimalEnumNative());
		assertEquals(a.getBigDecimalEnumCodeTexts(), b.getBigDecimalEnumCodeTexts());
		assertEquals(a.getBigDecimalEntityBase(), b.getBigDecimalEntityBase());
		assertEquals(a.getBigDecimalEntityEmpty(), b.getBigDecimalEntityEmpty());
	}

	public Map<Boolean, Boolean> getBooleanBoolean() {
		return booleanBoolean;
	}

	public void setBooleanBoolean(Map<Boolean, Boolean> value) {
		booleanBoolean = value;
	}

	public Map<Boolean, Byte> getBooleanByte() {
		return booleanByte;
	}

	public void setBooleanByte(Map<Boolean, Byte> value) {
		booleanByte = value;
	}

	public Map<Boolean, Character> getBooleanCharacter() {
		return booleanCharacter;
	}

	public void setBooleanCharacter(Map<Boolean, Character> value) {
		booleanCharacter = value;
	}

	public Map<Boolean, Short> getBooleanShort() {
		return booleanShort;
	}

	public void setBooleanShort(Map<Boolean, Short> value) {
		booleanShort = value;
	}

	public Map<Boolean, Integer> getBooleanInteger() {
		return booleanInteger;
	}

	public void setBooleanInteger(Map<Boolean, Integer> value) {
		booleanInteger = value;
	}

	public Map<Boolean, Long> getBooleanLong() {
		return booleanLong;
	}

	public void setBooleanLong(Map<Boolean, Long> value) {
		booleanLong = value;
	}

	public Map<Boolean, Float> getBooleanFloat() {
		return booleanFloat;
	}

	public void setBooleanFloat(Map<Boolean, Float> value) {
		booleanFloat = value;
	}

	public Map<Boolean, Double> getBooleanDouble() {
		return booleanDouble;
	}

	public void setBooleanDouble(Map<Boolean, Double> value) {
		booleanDouble = value;
	}

	public Map<Boolean, Date> getBooleanDate() {
		return booleanDate;
	}

	public void setBooleanDate(Map<Boolean, Date> value) {
		booleanDate = value;
	}

	public Map<Boolean, String> getBooleanString() {
		return booleanString;
	}

	public void setBooleanString(Map<Boolean, String> value) {
		booleanString = value;
	}

	public Map<Boolean, LocalDate> getBooleanLocalDate() {
		return booleanLocalDate;
	}

	public void setBooleanLocalDate(Map<Boolean, LocalDate> value) {
		booleanLocalDate = value;
	}

	public Map<Boolean, LocalTime> getBooleanLocalTime() {
		return booleanLocalTime;
	}

	public void setBooleanLocalTime(Map<Boolean, LocalTime> value) {
		booleanLocalTime = value;
	}

	public Map<Boolean, LocalDateTime> getBooleanLocalDateTime() {
		return booleanLocalDateTime;
	}

	public void setBooleanLocalDateTime(Map<Boolean, LocalDateTime> value) {
		booleanLocalDateTime = value;
	}

	public Map<Boolean, BigDecimal> getBooleanBigDecimal() {
		return booleanBigDecimal;
	}

	public void setBooleanBigDecimal(Map<Boolean, BigDecimal> value) {
		booleanBigDecimal = value;
	}

	public Map<Boolean, EnumCodes> getBooleanEnumCodes() {
		return booleanEnumCodes;
	}

	public void setBooleanEnumCodes(Map<Boolean, EnumCodes> value) {
		booleanEnumCodes = value;
	}

	public Map<Boolean, EnumTexts> getBooleanEnumTexts() {
		return booleanEnumTexts;
	}

	public void setBooleanEnumTexts(Map<Boolean, EnumTexts> value) {
		booleanEnumTexts = value;
	}

	public Map<Boolean, EnumNative> getBooleanEnumNative() {
		return booleanEnumNative;
	}

	public void setBooleanEnumNative(Map<Boolean, EnumNative> value) {
		booleanEnumNative = value;
	}

	public Map<Boolean, EnumCodeTexts> getBooleanEnumCodeTexts() {
		return booleanEnumCodeTexts;
	}

	public void setBooleanEnumCodeTexts(Map<Boolean, EnumCodeTexts> value) {
		booleanEnumCodeTexts = value;
	}

	public Map<Boolean, EntityBase> getBooleanEntityBase() {
		return booleanEntityBase;
	}

	public void setBooleanEntityBase(Map<Boolean, EntityBase> value) {
		booleanEntityBase = value;
	}

	public Map<Boolean, EntityEmpty> getBooleanEntityEmpty() {
		return booleanEntityEmpty;
	}

	public void setBooleanEntityEmpty(Map<Boolean, EntityEmpty> value) {
		booleanEntityEmpty = value;
	}

	public Map<Byte, Boolean> getByteBoolean() {
		return byteBoolean;
	}

	public void setByteBoolean(Map<Byte, Boolean> value) {
		byteBoolean = value;
	}

	public Map<Byte, Byte> getByteByte() {
		return byteByte;
	}

	public void setByteByte(Map<Byte, Byte> value) {
		byteByte = value;
	}

	public Map<Byte, Character> getByteCharacter() {
		return byteCharacter;
	}

	public void setByteCharacter(Map<Byte, Character> value) {
		byteCharacter = value;
	}

	public Map<Byte, Short> getByteShort() {
		return byteShort;
	}

	public void setByteShort(Map<Byte, Short> value) {
		byteShort = value;
	}

	public Map<Byte, Integer> getByteInteger() {
		return byteInteger;
	}

	public void setByteInteger(Map<Byte, Integer> value) {
		byteInteger = value;
	}

	public Map<Byte, Long> getByteLong() {
		return byteLong;
	}

	public void setByteLong(Map<Byte, Long> value) {
		byteLong = value;
	}

	public Map<Byte, Float> getByteFloat() {
		return byteFloat;
	}

	public void setByteFloat(Map<Byte, Float> value) {
		byteFloat = value;
	}

	public Map<Byte, Double> getByteDouble() {
		return byteDouble;
	}

	public void setByteDouble(Map<Byte, Double> value) {
		byteDouble = value;
	}

	public Map<Byte, Date> getByteDate() {
		return byteDate;
	}

	public void setByteDate(Map<Byte, Date> value) {
		byteDate = value;
	}

	public Map<Byte, String> getByteString() {
		return byteString;
	}

	public void setByteString(Map<Byte, String> value) {
		byteString = value;
	}

	public Map<Byte, LocalDate> getByteLocalDate() {
		return byteLocalDate;
	}

	public void setByteLocalDate(Map<Byte, LocalDate> value) {
		byteLocalDate = value;
	}

	public Map<Byte, LocalTime> getByteLocalTime() {
		return byteLocalTime;
	}

	public void setByteLocalTime(Map<Byte, LocalTime> value) {
		byteLocalTime = value;
	}

	public Map<Byte, LocalDateTime> getByteLocalDateTime() {
		return byteLocalDateTime;
	}

	public void setByteLocalDateTime(Map<Byte, LocalDateTime> value) {
		byteLocalDateTime = value;
	}

	public Map<Byte, BigDecimal> getByteBigDecimal() {
		return byteBigDecimal;
	}

	public void setByteBigDecimal(Map<Byte, BigDecimal> value) {
		byteBigDecimal = value;
	}

	public Map<Byte, EnumCodes> getByteEnumCodes() {
		return byteEnumCodes;
	}

	public void setByteEnumCodes(Map<Byte, EnumCodes> value) {
		byteEnumCodes = value;
	}

	public Map<Byte, EnumTexts> getByteEnumTexts() {
		return byteEnumTexts;
	}

	public void setByteEnumTexts(Map<Byte, EnumTexts> value) {
		byteEnumTexts = value;
	}

	public Map<Byte, EnumNative> getByteEnumNative() {
		return byteEnumNative;
	}

	public void setByteEnumNative(Map<Byte, EnumNative> value) {
		byteEnumNative = value;
	}

	public Map<Byte, EnumCodeTexts> getByteEnumCodeTexts() {
		return byteEnumCodeTexts;
	}

	public void setByteEnumCodeTexts(Map<Byte, EnumCodeTexts> value) {
		byteEnumCodeTexts = value;
	}

	public Map<Byte, EntityBase> getByteEntityBase() {
		return byteEntityBase;
	}

	public void setByteEntityBase(Map<Byte, EntityBase> value) {
		byteEntityBase = value;
	}

	public Map<Byte, EntityEmpty> getByteEntityEmpty() {
		return byteEntityEmpty;
	}

	public void setByteEntityEmpty(Map<Byte, EntityEmpty> value) {
		byteEntityEmpty = value;
	}

	public Map<Character, Boolean> getCharacterBoolean() {
		return characterBoolean;
	}

	public void setCharacterBoolean(Map<Character, Boolean> value) {
		characterBoolean = value;
	}

	public Map<Character, Byte> getCharacterByte() {
		return characterByte;
	}

	public void setCharacterByte(Map<Character, Byte> value) {
		characterByte = value;
	}

	public Map<Character, Character> getCharacterCharacter() {
		return characterCharacter;
	}

	public void setCharacterCharacter(Map<Character, Character> value) {
		characterCharacter = value;
	}

	public Map<Character, Short> getCharacterShort() {
		return characterShort;
	}

	public void setCharacterShort(Map<Character, Short> value) {
		characterShort = value;
	}

	public Map<Character, Integer> getCharacterInteger() {
		return characterInteger;
	}

	public void setCharacterInteger(Map<Character, Integer> value) {
		characterInteger = value;
	}

	public Map<Character, Long> getCharacterLong() {
		return characterLong;
	}

	public void setCharacterLong(Map<Character, Long> value) {
		characterLong = value;
	}

	public Map<Character, Float> getCharacterFloat() {
		return characterFloat;
	}

	public void setCharacterFloat(Map<Character, Float> value) {
		characterFloat = value;
	}

	public Map<Character, Double> getCharacterDouble() {
		return characterDouble;
	}

	public void setCharacterDouble(Map<Character, Double> value) {
		characterDouble = value;
	}

	public Map<Character, Date> getCharacterDate() {
		return characterDate;
	}

	public void setCharacterDate(Map<Character, Date> value) {
		characterDate = value;
	}

	public Map<Character, String> getCharacterString() {
		return characterString;
	}

	public void setCharacterString(Map<Character, String> value) {
		characterString = value;
	}

	public Map<Character, LocalDate> getCharacterLocalDate() {
		return characterLocalDate;
	}

	public void setCharacterLocalDate(Map<Character, LocalDate> value) {
		characterLocalDate = value;
	}

	public Map<Character, LocalTime> getCharacterLocalTime() {
		return characterLocalTime;
	}

	public void setCharacterLocalTime(Map<Character, LocalTime> value) {
		characterLocalTime = value;
	}

	public Map<Character, LocalDateTime> getCharacterLocalDateTime() {
		return characterLocalDateTime;
	}

	public void setCharacterLocalDateTime(Map<Character, LocalDateTime> value) {
		characterLocalDateTime = value;
	}

	public Map<Character, BigDecimal> getCharacterBigDecimal() {
		return characterBigDecimal;
	}

	public void setCharacterBigDecimal(Map<Character, BigDecimal> value) {
		characterBigDecimal = value;
	}

	public Map<Character, EnumCodes> getCharacterEnumCodes() {
		return characterEnumCodes;
	}

	public void setCharacterEnumCodes(Map<Character, EnumCodes> value) {
		characterEnumCodes = value;
	}

	public Map<Character, EnumTexts> getCharacterEnumTexts() {
		return characterEnumTexts;
	}

	public void setCharacterEnumTexts(Map<Character, EnumTexts> value) {
		characterEnumTexts = value;
	}

	public Map<Character, EnumNative> getCharacterEnumNative() {
		return characterEnumNative;
	}

	public void setCharacterEnumNative(Map<Character, EnumNative> value) {
		characterEnumNative = value;
	}

	public Map<Character, EnumCodeTexts> getCharacterEnumCodeTexts() {
		return characterEnumCodeTexts;
	}

	public void setCharacterEnumCodeTexts(Map<Character, EnumCodeTexts> value) {
		characterEnumCodeTexts = value;
	}

	public Map<Character, EntityBase> getCharacterEntityBase() {
		return characterEntityBase;
	}

	public void setCharacterEntityBase(Map<Character, EntityBase> value) {
		characterEntityBase = value;
	}

	public Map<Character, EntityEmpty> getCharacterEntityEmpty() {
		return characterEntityEmpty;
	}

	public void setCharacterEntityEmpty(Map<Character, EntityEmpty> value) {
		characterEntityEmpty = value;
	}

	public Map<Short, Boolean> getShortBoolean() {
		return shortBoolean;
	}

	public void setShortBoolean(Map<Short, Boolean> value) {
		shortBoolean = value;
	}

	public Map<Short, Byte> getShortByte() {
		return shortByte;
	}

	public void setShortByte(Map<Short, Byte> value) {
		shortByte = value;
	}

	public Map<Short, Character> getShortCharacter() {
		return shortCharacter;
	}

	public void setShortCharacter(Map<Short, Character> value) {
		shortCharacter = value;
	}

	public Map<Short, Short> getShortShort() {
		return shortShort;
	}

	public void setShortShort(Map<Short, Short> value) {
		shortShort = value;
	}

	public Map<Short, Integer> getShortInteger() {
		return shortInteger;
	}

	public void setShortInteger(Map<Short, Integer> value) {
		shortInteger = value;
	}

	public Map<Short, Long> getShortLong() {
		return shortLong;
	}

	public void setShortLong(Map<Short, Long> value) {
		shortLong = value;
	}

	public Map<Short, Float> getShortFloat() {
		return shortFloat;
	}

	public void setShortFloat(Map<Short, Float> value) {
		shortFloat = value;
	}

	public Map<Short, Double> getShortDouble() {
		return shortDouble;
	}

	public void setShortDouble(Map<Short, Double> value) {
		shortDouble = value;
	}

	public Map<Short, Date> getShortDate() {
		return shortDate;
	}

	public void setShortDate(Map<Short, Date> value) {
		shortDate = value;
	}

	public Map<Short, String> getShortString() {
		return shortString;
	}

	public void setShortString(Map<Short, String> value) {
		shortString = value;
	}

	public Map<Short, LocalDate> getShortLocalDate() {
		return shortLocalDate;
	}

	public void setShortLocalDate(Map<Short, LocalDate> value) {
		shortLocalDate = value;
	}

	public Map<Short, LocalTime> getShortLocalTime() {
		return shortLocalTime;
	}

	public void setShortLocalTime(Map<Short, LocalTime> value) {
		shortLocalTime = value;
	}

	public Map<Short, LocalDateTime> getShortLocalDateTime() {
		return shortLocalDateTime;
	}

	public void setShortLocalDateTime(Map<Short, LocalDateTime> value) {
		shortLocalDateTime = value;
	}

	public Map<Short, BigDecimal> getShortBigDecimal() {
		return shortBigDecimal;
	}

	public void setShortBigDecimal(Map<Short, BigDecimal> value) {
		shortBigDecimal = value;
	}

	public Map<Short, EnumCodes> getShortEnumCodes() {
		return shortEnumCodes;
	}

	public void setShortEnumCodes(Map<Short, EnumCodes> value) {
		shortEnumCodes = value;
	}

	public Map<Short, EnumTexts> getShortEnumTexts() {
		return shortEnumTexts;
	}

	public void setShortEnumTexts(Map<Short, EnumTexts> value) {
		shortEnumTexts = value;
	}

	public Map<Short, EnumNative> getShortEnumNative() {
		return shortEnumNative;
	}

	public void setShortEnumNative(Map<Short, EnumNative> value) {
		shortEnumNative = value;
	}

	public Map<Short, EnumCodeTexts> getShortEnumCodeTexts() {
		return shortEnumCodeTexts;
	}

	public void setShortEnumCodeTexts(Map<Short, EnumCodeTexts> value) {
		shortEnumCodeTexts = value;
	}

	public Map<Short, EntityBase> getShortEntityBase() {
		return shortEntityBase;
	}

	public void setShortEntityBase(Map<Short, EntityBase> value) {
		shortEntityBase = value;
	}

	public Map<Short, EntityEmpty> getShortEntityEmpty() {
		return shortEntityEmpty;
	}

	public void setShortEntityEmpty(Map<Short, EntityEmpty> value) {
		shortEntityEmpty = value;
	}

	public Map<Integer, Boolean> getIntegerBoolean() {
		return integerBoolean;
	}

	public void setIntegerBoolean(Map<Integer, Boolean> value) {
		integerBoolean = value;
	}

	public Map<Integer, Byte> getIntegerByte() {
		return integerByte;
	}

	public void setIntegerByte(Map<Integer, Byte> value) {
		integerByte = value;
	}

	public Map<Integer, Character> getIntegerCharacter() {
		return integerCharacter;
	}

	public void setIntegerCharacter(Map<Integer, Character> value) {
		integerCharacter = value;
	}

	public Map<Integer, Short> getIntegerShort() {
		return integerShort;
	}

	public void setIntegerShort(Map<Integer, Short> value) {
		integerShort = value;
	}

	public Map<Integer, Integer> getIntegerInteger() {
		return integerInteger;
	}

	public void setIntegerInteger(Map<Integer, Integer> value) {
		integerInteger = value;
	}

	public Map<Integer, Long> getIntegerLong() {
		return integerLong;
	}

	public void setIntegerLong(Map<Integer, Long> value) {
		integerLong = value;
	}

	public Map<Integer, Float> getIntegerFloat() {
		return integerFloat;
	}

	public void setIntegerFloat(Map<Integer, Float> value) {
		integerFloat = value;
	}

	public Map<Integer, Double> getIntegerDouble() {
		return integerDouble;
	}

	public void setIntegerDouble(Map<Integer, Double> value) {
		integerDouble = value;
	}

	public Map<Integer, Date> getIntegerDate() {
		return integerDate;
	}

	public void setIntegerDate(Map<Integer, Date> value) {
		integerDate = value;
	}

	public Map<Integer, String> getIntegerString() {
		return integerString;
	}

	public void setIntegerString(Map<Integer, String> value) {
		integerString = value;
	}

	public Map<Integer, LocalDate> getIntegerLocalDate() {
		return integerLocalDate;
	}

	public void setIntegerLocalDate(Map<Integer, LocalDate> value) {
		integerLocalDate = value;
	}

	public Map<Integer, LocalTime> getIntegerLocalTime() {
		return integerLocalTime;
	}

	public void setIntegerLocalTime(Map<Integer, LocalTime> value) {
		integerLocalTime = value;
	}

	public Map<Integer, LocalDateTime> getIntegerLocalDateTime() {
		return integerLocalDateTime;
	}

	public void setIntegerLocalDateTime(Map<Integer, LocalDateTime> value) {
		integerLocalDateTime = value;
	}

	public Map<Integer, BigDecimal> getIntegerBigDecimal() {
		return integerBigDecimal;
	}

	public void setIntegerBigDecimal(Map<Integer, BigDecimal> value) {
		integerBigDecimal = value;
	}

	public Map<Integer, EnumCodes> getIntegerEnumCodes() {
		return integerEnumCodes;
	}

	public void setIntegerEnumCodes(Map<Integer, EnumCodes> value) {
		integerEnumCodes = value;
	}

	public Map<Integer, EnumTexts> getIntegerEnumTexts() {
		return integerEnumTexts;
	}

	public void setIntegerEnumTexts(Map<Integer, EnumTexts> value) {
		integerEnumTexts = value;
	}

	public Map<Integer, EnumNative> getIntegerEnumNative() {
		return integerEnumNative;
	}

	public void setIntegerEnumNative(Map<Integer, EnumNative> value) {
		integerEnumNative = value;
	}

	public Map<Integer, EnumCodeTexts> getIntegerEnumCodeTexts() {
		return integerEnumCodeTexts;
	}

	public void setIntegerEnumCodeTexts(Map<Integer, EnumCodeTexts> value) {
		integerEnumCodeTexts = value;
	}

	public Map<Integer, EntityBase> getIntegerEntityBase() {
		return integerEntityBase;
	}

	public void setIntegerEntityBase(Map<Integer, EntityBase> value) {
		integerEntityBase = value;
	}

	public Map<Integer, EntityEmpty> getIntegerEntityEmpty() {
		return integerEntityEmpty;
	}

	public void setIntegerEntityEmpty(Map<Integer, EntityEmpty> value) {
		integerEntityEmpty = value;
	}

	public Map<Long, Boolean> getLongBoolean() {
		return longBoolean;
	}

	public void setLongBoolean(Map<Long, Boolean> value) {
		longBoolean = value;
	}

	public Map<Long, Byte> getLongByte() {
		return longByte;
	}

	public void setLongByte(Map<Long, Byte> value) {
		longByte = value;
	}

	public Map<Long, Character> getLongCharacter() {
		return longCharacter;
	}

	public void setLongCharacter(Map<Long, Character> value) {
		longCharacter = value;
	}

	public Map<Long, Short> getLongShort() {
		return longShort;
	}

	public void setLongShort(Map<Long, Short> value) {
		longShort = value;
	}

	public Map<Long, Integer> getLongInteger() {
		return longInteger;
	}

	public void setLongInteger(Map<Long, Integer> value) {
		longInteger = value;
	}

	public Map<Long, Long> getLongLong() {
		return longLong;
	}

	public void setLongLong(Map<Long, Long> value) {
		longLong = value;
	}

	public Map<Long, Float> getLongFloat() {
		return longFloat;
	}

	public void setLongFloat(Map<Long, Float> value) {
		longFloat = value;
	}

	public Map<Long, Double> getLongDouble() {
		return longDouble;
	}

	public void setLongDouble(Map<Long, Double> value) {
		longDouble = value;
	}

	public Map<Long, Date> getLongDate() {
		return longDate;
	}

	public void setLongDate(Map<Long, Date> value) {
		longDate = value;
	}

	public Map<Long, String> getLongString() {
		return longString;
	}

	public void setLongString(Map<Long, String> value) {
		longString = value;
	}

	public Map<Long, LocalDate> getLongLocalDate() {
		return longLocalDate;
	}

	public void setLongLocalDate(Map<Long, LocalDate> value) {
		longLocalDate = value;
	}

	public Map<Long, LocalTime> getLongLocalTime() {
		return longLocalTime;
	}

	public void setLongLocalTime(Map<Long, LocalTime> value) {
		longLocalTime = value;
	}

	public Map<Long, LocalDateTime> getLongLocalDateTime() {
		return longLocalDateTime;
	}

	public void setLongLocalDateTime(Map<Long, LocalDateTime> value) {
		longLocalDateTime = value;
	}

	public Map<Long, BigDecimal> getLongBigDecimal() {
		return longBigDecimal;
	}

	public void setLongBigDecimal(Map<Long, BigDecimal> value) {
		longBigDecimal = value;
	}

	public Map<Long, EnumCodes> getLongEnumCodes() {
		return longEnumCodes;
	}

	public void setLongEnumCodes(Map<Long, EnumCodes> value) {
		longEnumCodes = value;
	}

	public Map<Long, EnumTexts> getLongEnumTexts() {
		return longEnumTexts;
	}

	public void setLongEnumTexts(Map<Long, EnumTexts> value) {
		longEnumTexts = value;
	}

	public Map<Long, EnumNative> getLongEnumNative() {
		return longEnumNative;
	}

	public void setLongEnumNative(Map<Long, EnumNative> value) {
		longEnumNative = value;
	}

	public Map<Long, EnumCodeTexts> getLongEnumCodeTexts() {
		return longEnumCodeTexts;
	}

	public void setLongEnumCodeTexts(Map<Long, EnumCodeTexts> value) {
		longEnumCodeTexts = value;
	}

	public Map<Long, EntityBase> getLongEntityBase() {
		return longEntityBase;
	}

	public void setLongEntityBase(Map<Long, EntityBase> value) {
		longEntityBase = value;
	}

	public Map<Long, EntityEmpty> getLongEntityEmpty() {
		return longEntityEmpty;
	}

	public void setLongEntityEmpty(Map<Long, EntityEmpty> value) {
		longEntityEmpty = value;
	}

	public Map<Float, Boolean> getFloatBoolean() {
		return floatBoolean;
	}

	public void setFloatBoolean(Map<Float, Boolean> value) {
		floatBoolean = value;
	}

	public Map<Float, Byte> getFloatByte() {
		return floatByte;
	}

	public void setFloatByte(Map<Float, Byte> value) {
		floatByte = value;
	}

	public Map<Float, Character> getFloatCharacter() {
		return floatCharacter;
	}

	public void setFloatCharacter(Map<Float, Character> value) {
		floatCharacter = value;
	}

	public Map<Float, Short> getFloatShort() {
		return floatShort;
	}

	public void setFloatShort(Map<Float, Short> value) {
		floatShort = value;
	}

	public Map<Float, Integer> getFloatInteger() {
		return floatInteger;
	}

	public void setFloatInteger(Map<Float, Integer> value) {
		floatInteger = value;
	}

	public Map<Float, Long> getFloatLong() {
		return floatLong;
	}

	public void setFloatLong(Map<Float, Long> value) {
		floatLong = value;
	}

	public Map<Float, Float> getFloatFloat() {
		return floatFloat;
	}

	public void setFloatFloat(Map<Float, Float> value) {
		floatFloat = value;
	}

	public Map<Float, Double> getFloatDouble() {
		return floatDouble;
	}

	public void setFloatDouble(Map<Float, Double> value) {
		floatDouble = value;
	}

	public Map<Float, Date> getFloatDate() {
		return floatDate;
	}

	public void setFloatDate(Map<Float, Date> value) {
		floatDate = value;
	}

	public Map<Float, String> getFloatString() {
		return floatString;
	}

	public void setFloatString(Map<Float, String> value) {
		floatString = value;
	}

	public Map<Float, LocalDate> getFloatLocalDate() {
		return floatLocalDate;
	}

	public void setFloatLocalDate(Map<Float, LocalDate> value) {
		floatLocalDate = value;
	}

	public Map<Float, LocalTime> getFloatLocalTime() {
		return floatLocalTime;
	}

	public void setFloatLocalTime(Map<Float, LocalTime> value) {
		floatLocalTime = value;
	}

	public Map<Float, LocalDateTime> getFloatLocalDateTime() {
		return floatLocalDateTime;
	}

	public void setFloatLocalDateTime(Map<Float, LocalDateTime> value) {
		floatLocalDateTime = value;
	}

	public Map<Float, BigDecimal> getFloatBigDecimal() {
		return floatBigDecimal;
	}

	public void setFloatBigDecimal(Map<Float, BigDecimal> value) {
		floatBigDecimal = value;
	}

	public Map<Float, EnumCodes> getFloatEnumCodes() {
		return floatEnumCodes;
	}

	public void setFloatEnumCodes(Map<Float, EnumCodes> value) {
		floatEnumCodes = value;
	}

	public Map<Float, EnumTexts> getFloatEnumTexts() {
		return floatEnumTexts;
	}

	public void setFloatEnumTexts(Map<Float, EnumTexts> value) {
		floatEnumTexts = value;
	}

	public Map<Float, EnumNative> getFloatEnumNative() {
		return floatEnumNative;
	}

	public void setFloatEnumNative(Map<Float, EnumNative> value) {
		floatEnumNative = value;
	}

	public Map<Float, EnumCodeTexts> getFloatEnumCodeTexts() {
		return floatEnumCodeTexts;
	}

	public void setFloatEnumCodeTexts(Map<Float, EnumCodeTexts> value) {
		floatEnumCodeTexts = value;
	}

	public Map<Float, EntityBase> getFloatEntityBase() {
		return floatEntityBase;
	}

	public void setFloatEntityBase(Map<Float, EntityBase> value) {
		floatEntityBase = value;
	}

	public Map<Float, EntityEmpty> getFloatEntityEmpty() {
		return floatEntityEmpty;
	}

	public void setFloatEntityEmpty(Map<Float, EntityEmpty> value) {
		floatEntityEmpty = value;
	}

	public Map<Double, Boolean> getDoubleBoolean() {
		return doubleBoolean;
	}

	public void setDoubleBoolean(Map<Double, Boolean> value) {
		doubleBoolean = value;
	}

	public Map<Double, Byte> getDoubleByte() {
		return doubleByte;
	}

	public void setDoubleByte(Map<Double, Byte> value) {
		doubleByte = value;
	}

	public Map<Double, Character> getDoubleCharacter() {
		return doubleCharacter;
	}

	public void setDoubleCharacter(Map<Double, Character> value) {
		doubleCharacter = value;
	}

	public Map<Double, Short> getDoubleShort() {
		return doubleShort;
	}

	public void setDoubleShort(Map<Double, Short> value) {
		doubleShort = value;
	}

	public Map<Double, Integer> getDoubleInteger() {
		return doubleInteger;
	}

	public void setDoubleInteger(Map<Double, Integer> value) {
		doubleInteger = value;
	}

	public Map<Double, Long> getDoubleLong() {
		return doubleLong;
	}

	public void setDoubleLong(Map<Double, Long> value) {
		doubleLong = value;
	}

	public Map<Double, Float> getDoubleFloat() {
		return doubleFloat;
	}

	public void setDoubleFloat(Map<Double, Float> value) {
		doubleFloat = value;
	}

	public Map<Double, Double> getDoubleDouble() {
		return doubleDouble;
	}

	public void setDoubleDouble(Map<Double, Double> value) {
		doubleDouble = value;
	}

	public Map<Double, Date> getDoubleDate() {
		return doubleDate;
	}

	public void setDoubleDate(Map<Double, Date> value) {
		doubleDate = value;
	}

	public Map<Double, String> getDoubleString() {
		return doubleString;
	}

	public void setDoubleString(Map<Double, String> value) {
		doubleString = value;
	}

	public Map<Double, LocalDate> getDoubleLocalDate() {
		return doubleLocalDate;
	}

	public void setDoubleLocalDate(Map<Double, LocalDate> value) {
		doubleLocalDate = value;
	}

	public Map<Double, LocalTime> getDoubleLocalTime() {
		return doubleLocalTime;
	}

	public void setDoubleLocalTime(Map<Double, LocalTime> value) {
		doubleLocalTime = value;
	}

	public Map<Double, LocalDateTime> getDoubleLocalDateTime() {
		return doubleLocalDateTime;
	}

	public void setDoubleLocalDateTime(Map<Double, LocalDateTime> value) {
		doubleLocalDateTime = value;
	}

	public Map<Double, BigDecimal> getDoubleBigDecimal() {
		return doubleBigDecimal;
	}

	public void setDoubleBigDecimal(Map<Double, BigDecimal> value) {
		doubleBigDecimal = value;
	}

	public Map<Double, EnumCodes> getDoubleEnumCodes() {
		return doubleEnumCodes;
	}

	public void setDoubleEnumCodes(Map<Double, EnumCodes> value) {
		doubleEnumCodes = value;
	}

	public Map<Double, EnumTexts> getDoubleEnumTexts() {
		return doubleEnumTexts;
	}

	public void setDoubleEnumTexts(Map<Double, EnumTexts> value) {
		doubleEnumTexts = value;
	}

	public Map<Double, EnumNative> getDoubleEnumNative() {
		return doubleEnumNative;
	}

	public void setDoubleEnumNative(Map<Double, EnumNative> value) {
		doubleEnumNative = value;
	}

	public Map<Double, EnumCodeTexts> getDoubleEnumCodeTexts() {
		return doubleEnumCodeTexts;
	}

	public void setDoubleEnumCodeTexts(Map<Double, EnumCodeTexts> value) {
		doubleEnumCodeTexts = value;
	}

	public Map<Double, EntityBase> getDoubleEntityBase() {
		return doubleEntityBase;
	}

	public void setDoubleEntityBase(Map<Double, EntityBase> value) {
		doubleEntityBase = value;
	}

	public Map<Double, EntityEmpty> getDoubleEntityEmpty() {
		return doubleEntityEmpty;
	}

	public void setDoubleEntityEmpty(Map<Double, EntityEmpty> value) {
		doubleEntityEmpty = value;
	}

	public Map<Date, Boolean> getDateBoolean() {
		return dateBoolean;
	}

	public void setDateBoolean(Map<Date, Boolean> value) {
		dateBoolean = value;
	}

	public Map<Date, Byte> getDateByte() {
		return dateByte;
	}

	public void setDateByte(Map<Date, Byte> value) {
		dateByte = value;
	}

	public Map<Date, Character> getDateCharacter() {
		return dateCharacter;
	}

	public void setDateCharacter(Map<Date, Character> value) {
		dateCharacter = value;
	}

	public Map<Date, Short> getDateShort() {
		return dateShort;
	}

	public void setDateShort(Map<Date, Short> value) {
		dateShort = value;
	}

	public Map<Date, Integer> getDateInteger() {
		return dateInteger;
	}

	public void setDateInteger(Map<Date, Integer> value) {
		dateInteger = value;
	}

	public Map<Date, Long> getDateLong() {
		return dateLong;
	}

	public void setDateLong(Map<Date, Long> value) {
		dateLong = value;
	}

	public Map<Date, Float> getDateFloat() {
		return dateFloat;
	}

	public void setDateFloat(Map<Date, Float> value) {
		dateFloat = value;
	}

	public Map<Date, Double> getDateDouble() {
		return dateDouble;
	}

	public void setDateDouble(Map<Date, Double> value) {
		dateDouble = value;
	}

	public Map<Date, Date> getDateDate() {
		return dateDate;
	}

	public void setDateDate(Map<Date, Date> value) {
		dateDate = value;
	}

	public Map<Date, String> getDateString() {
		return dateString;
	}

	public void setDateString(Map<Date, String> value) {
		dateString = value;
	}

	public Map<Date, LocalDate> getDateLocalDate() {
		return dateLocalDate;
	}

	public void setDateLocalDate(Map<Date, LocalDate> value) {
		dateLocalDate = value;
	}

	public Map<Date, LocalTime> getDateLocalTime() {
		return dateLocalTime;
	}

	public void setDateLocalTime(Map<Date, LocalTime> value) {
		dateLocalTime = value;
	}

	public Map<Date, LocalDateTime> getDateLocalDateTime() {
		return dateLocalDateTime;
	}

	public void setDateLocalDateTime(Map<Date, LocalDateTime> value) {
		dateLocalDateTime = value;
	}

	public Map<Date, BigDecimal> getDateBigDecimal() {
		return dateBigDecimal;
	}

	public void setDateBigDecimal(Map<Date, BigDecimal> value) {
		dateBigDecimal = value;
	}

	public Map<Date, EnumCodes> getDateEnumCodes() {
		return dateEnumCodes;
	}

	public void setDateEnumCodes(Map<Date, EnumCodes> value) {
		dateEnumCodes = value;
	}

	public Map<Date, EnumTexts> getDateEnumTexts() {
		return dateEnumTexts;
	}

	public void setDateEnumTexts(Map<Date, EnumTexts> value) {
		dateEnumTexts = value;
	}

	public Map<Date, EnumNative> getDateEnumNative() {
		return dateEnumNative;
	}

	public void setDateEnumNative(Map<Date, EnumNative> value) {
		dateEnumNative = value;
	}

	public Map<Date, EnumCodeTexts> getDateEnumCodeTexts() {
		return dateEnumCodeTexts;
	}

	public void setDateEnumCodeTexts(Map<Date, EnumCodeTexts> value) {
		dateEnumCodeTexts = value;
	}

	public Map<Date, EntityBase> getDateEntityBase() {
		return dateEntityBase;
	}

	public void setDateEntityBase(Map<Date, EntityBase> value) {
		dateEntityBase = value;
	}

	public Map<Date, EntityEmpty> getDateEntityEmpty() {
		return dateEntityEmpty;
	}

	public void setDateEntityEmpty(Map<Date, EntityEmpty> value) {
		dateEntityEmpty = value;
	}

	public Map<String, Boolean> getStringBoolean() {
		return stringBoolean;
	}

	public void setStringBoolean(Map<String, Boolean> value) {
		stringBoolean = value;
	}

	public Map<String, Byte> getStringByte() {
		return stringByte;
	}

	public void setStringByte(Map<String, Byte> value) {
		stringByte = value;
	}

	public Map<String, Character> getStringCharacter() {
		return stringCharacter;
	}

	public void setStringCharacter(Map<String, Character> value) {
		stringCharacter = value;
	}

	public Map<String, Short> getStringShort() {
		return stringShort;
	}

	public void setStringShort(Map<String, Short> value) {
		stringShort = value;
	}

	public Map<String, Integer> getStringInteger() {
		return stringInteger;
	}

	public void setStringInteger(Map<String, Integer> value) {
		stringInteger = value;
	}

	public Map<String, Long> getStringLong() {
		return stringLong;
	}

	public void setStringLong(Map<String, Long> value) {
		stringLong = value;
	}

	public Map<String, Float> getStringFloat() {
		return stringFloat;
	}

	public void setStringFloat(Map<String, Float> value) {
		stringFloat = value;
	}

	public Map<String, Double> getStringDouble() {
		return stringDouble;
	}

	public void setStringDouble(Map<String, Double> value) {
		stringDouble = value;
	}

	public Map<String, Date> getStringDate() {
		return stringDate;
	}

	public void setStringDate(Map<String, Date> value) {
		stringDate = value;
	}

	public Map<String, String> getStringString() {
		return stringString;
	}

	public void setStringString(Map<String, String> value) {
		stringString = value;
	}

	public Map<String, LocalDate> getStringLocalDate() {
		return stringLocalDate;
	}

	public void setStringLocalDate(Map<String, LocalDate> value) {
		stringLocalDate = value;
	}

	public Map<String, LocalTime> getStringLocalTime() {
		return stringLocalTime;
	}

	public void setStringLocalTime(Map<String, LocalTime> value) {
		stringLocalTime = value;
	}

	public Map<String, LocalDateTime> getStringLocalDateTime() {
		return stringLocalDateTime;
	}

	public void setStringLocalDateTime(Map<String, LocalDateTime> value) {
		stringLocalDateTime = value;
	}

	public Map<String, BigDecimal> getStringBigDecimal() {
		return stringBigDecimal;
	}

	public void setStringBigDecimal(Map<String, BigDecimal> value) {
		stringBigDecimal = value;
	}

	public Map<String, EnumCodes> getStringEnumCodes() {
		return stringEnumCodes;
	}

	public void setStringEnumCodes(Map<String, EnumCodes> value) {
		stringEnumCodes = value;
	}

	public Map<String, EnumTexts> getStringEnumTexts() {
		return stringEnumTexts;
	}

	public void setStringEnumTexts(Map<String, EnumTexts> value) {
		stringEnumTexts = value;
	}

	public Map<String, EnumNative> getStringEnumNative() {
		return stringEnumNative;
	}

	public void setStringEnumNative(Map<String, EnumNative> value) {
		stringEnumNative = value;
	}

	public Map<String, EnumCodeTexts> getStringEnumCodeTexts() {
		return stringEnumCodeTexts;
	}

	public void setStringEnumCodeTexts(Map<String, EnumCodeTexts> value) {
		stringEnumCodeTexts = value;
	}

	public Map<String, EntityBase> getStringEntityBase() {
		return stringEntityBase;
	}

	public void setStringEntityBase(Map<String, EntityBase> value) {
		stringEntityBase = value;
	}

	public Map<String, EntityEmpty> getStringEntityEmpty() {
		return stringEntityEmpty;
	}

	public void setStringEntityEmpty(Map<String, EntityEmpty> value) {
		stringEntityEmpty = value;
	}

	public Map<LocalDate, Boolean> getLocalDateBoolean() {
		return localdateBoolean;
	}

	public void setLocalDateBoolean(Map<LocalDate, Boolean> value) {
		localdateBoolean = value;
	}

	public Map<LocalDate, Byte> getLocalDateByte() {
		return localdateByte;
	}

	public void setLocalDateByte(Map<LocalDate, Byte> value) {
		localdateByte = value;
	}

	public Map<LocalDate, Character> getLocalDateCharacter() {
		return localdateCharacter;
	}

	public void setLocalDateCharacter(Map<LocalDate, Character> value) {
		localdateCharacter = value;
	}

	public Map<LocalDate, Short> getLocalDateShort() {
		return localdateShort;
	}

	public void setLocalDateShort(Map<LocalDate, Short> value) {
		localdateShort = value;
	}

	public Map<LocalDate, Integer> getLocalDateInteger() {
		return localdateInteger;
	}

	public void setLocalDateInteger(Map<LocalDate, Integer> value) {
		localdateInteger = value;
	}

	public Map<LocalDate, Long> getLocalDateLong() {
		return localdateLong;
	}

	public void setLocalDateLong(Map<LocalDate, Long> value) {
		localdateLong = value;
	}

	public Map<LocalDate, Float> getLocalDateFloat() {
		return localdateFloat;
	}

	public void setLocalDateFloat(Map<LocalDate, Float> value) {
		localdateFloat = value;
	}

	public Map<LocalDate, Double> getLocalDateDouble() {
		return localdateDouble;
	}

	public void setLocalDateDouble(Map<LocalDate, Double> value) {
		localdateDouble = value;
	}

	public Map<LocalDate, Date> getLocalDateDate() {
		return localdateDate;
	}

	public void setLocalDateDate(Map<LocalDate, Date> value) {
		localdateDate = value;
	}

	public Map<LocalDate, String> getLocalDateString() {
		return localdateString;
	}

	public void setLocalDateString(Map<LocalDate, String> value) {
		localdateString = value;
	}

	public Map<LocalDate, LocalDate> getLocalDateLocalDate() {
		return localdateLocalDate;
	}

	public void setLocalDateLocalDate(Map<LocalDate, LocalDate> value) {
		localdateLocalDate = value;
	}

	public Map<LocalDate, LocalTime> getLocalDateLocalTime() {
		return localdateLocalTime;
	}

	public void setLocalDateLocalTime(Map<LocalDate, LocalTime> value) {
		localdateLocalTime = value;
	}

	public Map<LocalDate, LocalDateTime> getLocalDateLocalDateTime() {
		return localdateLocalDateTime;
	}

	public void setLocalDateLocalDateTime(Map<LocalDate, LocalDateTime> value) {
		localdateLocalDateTime = value;
	}

	public Map<LocalDate, BigDecimal> getLocalDateBigDecimal() {
		return localdateBigDecimal;
	}

	public void setLocalDateBigDecimal(Map<LocalDate, BigDecimal> value) {
		localdateBigDecimal = value;
	}

	public Map<LocalDate, EnumCodes> getLocalDateEnumCodes() {
		return localdateEnumCodes;
	}

	public void setLocalDateEnumCodes(Map<LocalDate, EnumCodes> value) {
		localdateEnumCodes = value;
	}

	public Map<LocalDate, EnumTexts> getLocalDateEnumTexts() {
		return localdateEnumTexts;
	}

	public void setLocalDateEnumTexts(Map<LocalDate, EnumTexts> value) {
		localdateEnumTexts = value;
	}

	public Map<LocalDate, EnumNative> getLocalDateEnumNative() {
		return localdateEnumNative;
	}

	public void setLocalDateEnumNative(Map<LocalDate, EnumNative> value) {
		localdateEnumNative = value;
	}

	public Map<LocalDate, EnumCodeTexts> getLocalDateEnumCodeTexts() {
		return localdateEnumCodeTexts;
	}

	public void setLocalDateEnumCodeTexts(Map<LocalDate, EnumCodeTexts> value) {
		localdateEnumCodeTexts = value;
	}

	public Map<LocalDate, EntityBase> getLocalDateEntityBase() {
		return localdateEntityBase;
	}

	public void setLocalDateEntityBase(Map<LocalDate, EntityBase> value) {
		localdateEntityBase = value;
	}

	public Map<LocalDate, EntityEmpty> getLocalDateEntityEmpty() {
		return localdateEntityEmpty;
	}

	public void setLocalDateEntityEmpty(Map<LocalDate, EntityEmpty> value) {
		localdateEntityEmpty = value;
	}

	public Map<LocalTime, Boolean> getLocalTimeBoolean() {
		return localtimeBoolean;
	}

	public void setLocalTimeBoolean(Map<LocalTime, Boolean> value) {
		localtimeBoolean = value;
	}

	public Map<LocalTime, Byte> getLocalTimeByte() {
		return localtimeByte;
	}

	public void setLocalTimeByte(Map<LocalTime, Byte> value) {
		localtimeByte = value;
	}

	public Map<LocalTime, Character> getLocalTimeCharacter() {
		return localtimeCharacter;
	}

	public void setLocalTimeCharacter(Map<LocalTime, Character> value) {
		localtimeCharacter = value;
	}

	public Map<LocalTime, Short> getLocalTimeShort() {
		return localtimeShort;
	}

	public void setLocalTimeShort(Map<LocalTime, Short> value) {
		localtimeShort = value;
	}

	public Map<LocalTime, Integer> getLocalTimeInteger() {
		return localtimeInteger;
	}

	public void setLocalTimeInteger(Map<LocalTime, Integer> value) {
		localtimeInteger = value;
	}

	public Map<LocalTime, Long> getLocalTimeLong() {
		return localtimeLong;
	}

	public void setLocalTimeLong(Map<LocalTime, Long> value) {
		localtimeLong = value;
	}

	public Map<LocalTime, Float> getLocalTimeFloat() {
		return localtimeFloat;
	}

	public void setLocalTimeFloat(Map<LocalTime, Float> value) {
		localtimeFloat = value;
	}

	public Map<LocalTime, Double> getLocalTimeDouble() {
		return localtimeDouble;
	}

	public void setLocalTimeDouble(Map<LocalTime, Double> value) {
		localtimeDouble = value;
	}

	public Map<LocalTime, Date> getLocalTimeDate() {
		return localtimeDate;
	}

	public void setLocalTimeDate(Map<LocalTime, Date> value) {
		localtimeDate = value;
	}

	public Map<LocalTime, String> getLocalTimeString() {
		return localtimeString;
	}

	public void setLocalTimeString(Map<LocalTime, String> value) {
		localtimeString = value;
	}

	public Map<LocalTime, LocalDate> getLocalTimeLocalDate() {
		return localtimeLocalDate;
	}

	public void setLocalTimeLocalDate(Map<LocalTime, LocalDate> value) {
		localtimeLocalDate = value;
	}

	public Map<LocalTime, LocalTime> getLocalTimeLocalTime() {
		return localtimeLocalTime;
	}

	public void setLocalTimeLocalTime(Map<LocalTime, LocalTime> value) {
		localtimeLocalTime = value;
	}

	public Map<LocalTime, LocalDateTime> getLocalTimeLocalDateTime() {
		return localtimeLocalDateTime;
	}

	public void setLocalTimeLocalDateTime(Map<LocalTime, LocalDateTime> value) {
		localtimeLocalDateTime = value;
	}

	public Map<LocalTime, BigDecimal> getLocalTimeBigDecimal() {
		return localtimeBigDecimal;
	}

	public void setLocalTimeBigDecimal(Map<LocalTime, BigDecimal> value) {
		localtimeBigDecimal = value;
	}

	public Map<LocalTime, EnumCodes> getLocalTimeEnumCodes() {
		return localtimeEnumCodes;
	}

	public void setLocalTimeEnumCodes(Map<LocalTime, EnumCodes> value) {
		localtimeEnumCodes = value;
	}

	public Map<LocalTime, EnumTexts> getLocalTimeEnumTexts() {
		return localtimeEnumTexts;
	}

	public void setLocalTimeEnumTexts(Map<LocalTime, EnumTexts> value) {
		localtimeEnumTexts = value;
	}

	public Map<LocalTime, EnumNative> getLocalTimeEnumNative() {
		return localtimeEnumNative;
	}

	public void setLocalTimeEnumNative(Map<LocalTime, EnumNative> value) {
		localtimeEnumNative = value;
	}

	public Map<LocalTime, EnumCodeTexts> getLocalTimeEnumCodeTexts() {
		return localtimeEnumCodeTexts;
	}

	public void setLocalTimeEnumCodeTexts(Map<LocalTime, EnumCodeTexts> value) {
		localtimeEnumCodeTexts = value;
	}

	public Map<LocalTime, EntityBase> getLocalTimeEntityBase() {
		return localtimeEntityBase;
	}

	public void setLocalTimeEntityBase(Map<LocalTime, EntityBase> value) {
		localtimeEntityBase = value;
	}

	public Map<LocalTime, EntityEmpty> getLocalTimeEntityEmpty() {
		return localtimeEntityEmpty;
	}

	public void setLocalTimeEntityEmpty(Map<LocalTime, EntityEmpty> value) {
		localtimeEntityEmpty = value;
	}

	public Map<LocalDateTime, Boolean> getLocalDateTimeBoolean() {
		return localdatetimeBoolean;
	}

	public void setLocalDateTimeBoolean(Map<LocalDateTime, Boolean> value) {
		localdatetimeBoolean = value;
	}

	public Map<LocalDateTime, Byte> getLocalDateTimeByte() {
		return localdatetimeByte;
	}

	public void setLocalDateTimeByte(Map<LocalDateTime, Byte> value) {
		localdatetimeByte = value;
	}

	public Map<LocalDateTime, Character> getLocalDateTimeCharacter() {
		return localdatetimeCharacter;
	}

	public void setLocalDateTimeCharacter(Map<LocalDateTime, Character> value) {
		localdatetimeCharacter = value;
	}

	public Map<LocalDateTime, Short> getLocalDateTimeShort() {
		return localdatetimeShort;
	}

	public void setLocalDateTimeShort(Map<LocalDateTime, Short> value) {
		localdatetimeShort = value;
	}

	public Map<LocalDateTime, Integer> getLocalDateTimeInteger() {
		return localdatetimeInteger;
	}

	public void setLocalDateTimeInteger(Map<LocalDateTime, Integer> value) {
		localdatetimeInteger = value;
	}

	public Map<LocalDateTime, Long> getLocalDateTimeLong() {
		return localdatetimeLong;
	}

	public void setLocalDateTimeLong(Map<LocalDateTime, Long> value) {
		localdatetimeLong = value;
	}

	public Map<LocalDateTime, Float> getLocalDateTimeFloat() {
		return localdatetimeFloat;
	}

	public void setLocalDateTimeFloat(Map<LocalDateTime, Float> value) {
		localdatetimeFloat = value;
	}

	public Map<LocalDateTime, Double> getLocalDateTimeDouble() {
		return localdatetimeDouble;
	}

	public void setLocalDateTimeDouble(Map<LocalDateTime, Double> value) {
		localdatetimeDouble = value;
	}

	public Map<LocalDateTime, Date> getLocalDateTimeDate() {
		return localdatetimeDate;
	}

	public void setLocalDateTimeDate(Map<LocalDateTime, Date> value) {
		localdatetimeDate = value;
	}

	public Map<LocalDateTime, String> getLocalDateTimeString() {
		return localdatetimeString;
	}

	public void setLocalDateTimeString(Map<LocalDateTime, String> value) {
		localdatetimeString = value;
	}

	public Map<LocalDateTime, LocalDate> getLocalDateTimeLocalDate() {
		return localdatetimeLocalDate;
	}

	public void setLocalDateTimeLocalDate(Map<LocalDateTime, LocalDate> value) {
		localdatetimeLocalDate = value;
	}

	public Map<LocalDateTime, LocalTime> getLocalDateTimeLocalTime() {
		return localdatetimeLocalTime;
	}

	public void setLocalDateTimeLocalTime(Map<LocalDateTime, LocalTime> value) {
		localdatetimeLocalTime = value;
	}

	public Map<LocalDateTime, LocalDateTime> getLocalDateTimeLocalDateTime() {
		return localdatetimeLocalDateTime;
	}

	public void setLocalDateTimeLocalDateTime(Map<LocalDateTime, LocalDateTime> value) {
		localdatetimeLocalDateTime = value;
	}

	public Map<LocalDateTime, BigDecimal> getLocalDateTimeBigDecimal() {
		return localdatetimeBigDecimal;
	}

	public void setLocalDateTimeBigDecimal(Map<LocalDateTime, BigDecimal> value) {
		localdatetimeBigDecimal = value;
	}

	public Map<LocalDateTime, EnumCodes> getLocalDateTimeEnumCodes() {
		return localdatetimeEnumCodes;
	}

	public void setLocalDateTimeEnumCodes(Map<LocalDateTime, EnumCodes> value) {
		localdatetimeEnumCodes = value;
	}

	public Map<LocalDateTime, EnumTexts> getLocalDateTimeEnumTexts() {
		return localdatetimeEnumTexts;
	}

	public void setLocalDateTimeEnumTexts(Map<LocalDateTime, EnumTexts> value) {
		localdatetimeEnumTexts = value;
	}

	public Map<LocalDateTime, EnumNative> getLocalDateTimeEnumNative() {
		return localdatetimeEnumNative;
	}

	public void setLocalDateTimeEnumNative(Map<LocalDateTime, EnumNative> value) {
		localdatetimeEnumNative = value;
	}

	public Map<LocalDateTime, EnumCodeTexts> getLocalDateTimeEnumCodeTexts() {
		return localdatetimeEnumCodeTexts;
	}

	public void setLocalDateTimeEnumCodeTexts(Map<LocalDateTime, EnumCodeTexts> value) {
		localdatetimeEnumCodeTexts = value;
	}

	public Map<LocalDateTime, EntityBase> getLocalDateTimeEntityBase() {
		return localdatetimeEntityBase;
	}

	public void setLocalDateTimeEntityBase(Map<LocalDateTime, EntityBase> value) {
		localdatetimeEntityBase = value;
	}

	public Map<LocalDateTime, EntityEmpty> getLocalDateTimeEntityEmpty() {
		return localdatetimeEntityEmpty;
	}

	public void setLocalDateTimeEntityEmpty(Map<LocalDateTime, EntityEmpty> value) {
		localdatetimeEntityEmpty = value;
	}

	public Map<BigDecimal, Boolean> getBigDecimalBoolean() {
		return bigdecimalBoolean;
	}

	public void setBigDecimalBoolean(Map<BigDecimal, Boolean> value) {
		bigdecimalBoolean = value;
	}

	public Map<BigDecimal, Byte> getBigDecimalByte() {
		return bigdecimalByte;
	}

	public void setBigDecimalByte(Map<BigDecimal, Byte> value) {
		bigdecimalByte = value;
	}

	public Map<BigDecimal, Character> getBigDecimalCharacter() {
		return bigdecimalCharacter;
	}

	public void setBigDecimalCharacter(Map<BigDecimal, Character> value) {
		bigdecimalCharacter = value;
	}

	public Map<BigDecimal, Short> getBigDecimalShort() {
		return bigdecimalShort;
	}

	public void setBigDecimalShort(Map<BigDecimal, Short> value) {
		bigdecimalShort = value;
	}

	public Map<BigDecimal, Integer> getBigDecimalInteger() {
		return bigdecimalInteger;
	}

	public void setBigDecimalInteger(Map<BigDecimal, Integer> value) {
		bigdecimalInteger = value;
	}

	public Map<BigDecimal, Long> getBigDecimalLong() {
		return bigdecimalLong;
	}

	public void setBigDecimalLong(Map<BigDecimal, Long> value) {
		bigdecimalLong = value;
	}

	public Map<BigDecimal, Float> getBigDecimalFloat() {
		return bigdecimalFloat;
	}

	public void setBigDecimalFloat(Map<BigDecimal, Float> value) {
		bigdecimalFloat = value;
	}

	public Map<BigDecimal, Double> getBigDecimalDouble() {
		return bigdecimalDouble;
	}

	public void setBigDecimalDouble(Map<BigDecimal, Double> value) {
		bigdecimalDouble = value;
	}

	public Map<BigDecimal, Date> getBigDecimalDate() {
		return bigdecimalDate;
	}

	public void setBigDecimalDate(Map<BigDecimal, Date> value) {
		bigdecimalDate = value;
	}

	public Map<BigDecimal, String> getBigDecimalString() {
		return bigdecimalString;
	}

	public void setBigDecimalString(Map<BigDecimal, String> value) {
		bigdecimalString = value;
	}

	public Map<BigDecimal, LocalDate> getBigDecimalLocalDate() {
		return bigdecimalLocalDate;
	}

	public void setBigDecimalLocalDate(Map<BigDecimal, LocalDate> value) {
		bigdecimalLocalDate = value;
	}

	public Map<BigDecimal, LocalTime> getBigDecimalLocalTime() {
		return bigdecimalLocalTime;
	}

	public void setBigDecimalLocalTime(Map<BigDecimal, LocalTime> value) {
		bigdecimalLocalTime = value;
	}

	public Map<BigDecimal, LocalDateTime> getBigDecimalLocalDateTime() {
		return bigdecimalLocalDateTime;
	}

	public void setBigDecimalLocalDateTime(Map<BigDecimal, LocalDateTime> value) {
		bigdecimalLocalDateTime = value;
	}

	public Map<BigDecimal, BigDecimal> getBigDecimalBigDecimal() {
		return bigdecimalBigDecimal;
	}

	public void setBigDecimalBigDecimal(Map<BigDecimal, BigDecimal> value) {
		bigdecimalBigDecimal = value;
	}

	public Map<BigDecimal, EnumCodes> getBigDecimalEnumCodes() {
		return bigdecimalEnumCodes;
	}

	public void setBigDecimalEnumCodes(Map<BigDecimal, EnumCodes> value) {
		bigdecimalEnumCodes = value;
	}

	public Map<BigDecimal, EnumTexts> getBigDecimalEnumTexts() {
		return bigdecimalEnumTexts;
	}

	public void setBigDecimalEnumTexts(Map<BigDecimal, EnumTexts> value) {
		bigdecimalEnumTexts = value;
	}

	public Map<BigDecimal, EnumNative> getBigDecimalEnumNative() {
		return bigdecimalEnumNative;
	}

	public void setBigDecimalEnumNative(Map<BigDecimal, EnumNative> value) {
		bigdecimalEnumNative = value;
	}

	public Map<BigDecimal, EnumCodeTexts> getBigDecimalEnumCodeTexts() {
		return bigdecimalEnumCodeTexts;
	}

	public void setBigDecimalEnumCodeTexts(Map<BigDecimal, EnumCodeTexts> value) {
		bigdecimalEnumCodeTexts = value;
	}

	public Map<BigDecimal, EntityBase> getBigDecimalEntityBase() {
		return bigdecimalEntityBase;
	}

	public void setBigDecimalEntityBase(Map<BigDecimal, EntityBase> value) {
		bigdecimalEntityBase = value;
	}

	public Map<BigDecimal, EntityEmpty> getBigDecimalEntityEmpty() {
		return bigdecimalEntityEmpty;
	}

	public void setBigDecimalEntityEmpty(Map<BigDecimal, EntityEmpty> value) {
		bigdecimalEntityEmpty = value;
	}
}
