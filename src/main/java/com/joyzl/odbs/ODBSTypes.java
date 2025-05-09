/*-
 * www.joyzl.net
 * 重庆骄智科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象序列化支持的基础类型
 * 
 * @author ZhangXi
 * @date 2020年6月3日
 */
public class ODBSTypes {

	/** unknow */
	public final static int UNKNOW = 0;

	/** boolean */
	public final static int _boolean = 1;
	/** byte */
	public final static int _byte = 2;
	/** char */
	public final static int _char = 3;
	/** short */
	public final static int _short = 4;
	/** int */
	public final static int _int = 5;
	/** long */
	public final static int _long = 6;
	/** float */
	public final static int _float = 7;
	/** double */
	public final static int _double = 8;

	/** Boolean */
	public final static int BOOLEAN = 10;
	/** Byte */
	public final static int BYTE = 11;
	/** Character */
	public final static int CHARACTER = 12;
	/** Short */
	public final static int SHORT = 13;
	/** Integer */
	public final static int INTEGER = 14;
	/** Long */
	public final static int LONG = 15;
	/** Float */
	public final static int FLOAT = 16;
	/** Double */
	public final static int DOUBLE = 17;
	/** BigDecimal */
	public final static int BIG_DECIMAL = 18;
	/** Date */
	public final static int DATE = 19;
	/** LocalTime */
	public final static int LOCAL_TIME = 20;
	/** LocalDate */
	public final static int LOCAL_DATE = 21;
	/** LocalDateTime */
	public final static int LOCAL_DATE_TIME = 22;
	/** String */
	public final static int STRING = 23;

	/** Array */
	public final static int ARRAY = 50;
	/** List */
	public final static int LIST = 51;
	/** Set */
	public final static int SET = 52;
	/** Map */
	public final static int MAP = 53;

	/** ENUM 100 ~ 999 */
	public final static int ENUM = 100;
	/** Entity 1000~32767 */
	public final static int ENTITY = 1000;
	/** Object 32767 */
	public final static int ANY = 32767;

	// 类型区间划分: [0][1~9][10~30][50~59][100~999][1000~32767]
	// [0] 未知类型
	// [1~9] 值类型
	// [10~30] 基础类型
	// [50~59] 集合类型
	// [100~999] 枚举类型
	// [1000~32766] 自定义类型
	// [32767] Object
	// 0~32767是Varint两个字节可表示的范围128*256=32768

	/**
	 * 判断指定类型是否为值类型
	 * 
	 * @param type
	 * @return
	 */
	public final static boolean isValue(int type) {
		return type >= _boolean && type <= _double;
	}

	/**
	 * 判断指定类型是否为基础类型
	 * 
	 * @param type
	 * @return
	 */
	public final static boolean isBase(int type) {
		return type >= BOOLEAN && type <= STRING;
	}

	/**
	 * 判断指定类型是否为枚举类型
	 * 
	 * @param type
	 * @return
	 */
	public final static boolean isEnum(int type) {
		return type >= ENUM && type < ENTITY;
	}

	/**
	 * 判断指定类型是否为数组
	 * 
	 * @param type
	 * @return
	 */
	public final static boolean isArray(int type) {
		return type == ARRAY;
	}

	public final static boolean isList(int type) {
		return type == LIST;
	}

	public final static boolean isSet(int type) {
		return type == SET;
	}

	public final static boolean isMap(int type) {
		return type == MAP;
	}

	public final static boolean isEntity(int type) {
		return type >= ENTITY && type < ANY;
	}

	public final static boolean isAny(int type) {
		return type == ANY;
	}

	public final static String getName(int type) {
		switch (type) {
			case _boolean:
				return "boolean";
			case _byte:
				return "byte";
			case _char:
				return "char";
			case _short:
				return "short";
			case _int:
				return "int";
			case _long:
				return "long";
			case _float:
				return "float";
			case _double:
				return "double";
			case BOOLEAN:
				return "Boolean";
			case BYTE:
				return "Byte";
			case CHARACTER:
				return "Character";
			case SHORT:
				return "Short";
			case INTEGER:
				return "Integer";
			case LONG:
				return "Long";
			case FLOAT:
				return "Float";
			case DOUBLE:
				return "Double";
			case BIG_DECIMAL:
				return "BigDecimal";
			case DATE:
				return "Date";
			case LOCAL_TIME:
				return "LocalTime";
			case LOCAL_DATE:
				return "LocalDate";
			case LOCAL_DATE_TIME:
				return "LocalDateTime";
			case STRING:
				return "String";
			case ARRAY:
				return "Array";
			case LIST:
				return "List";
			case SET:
				return "Set";
			case MAP:
				return "Map";
			case ANY:
				return "Object";
			default:
				if (isEnum(type)) {
					return "Enum";
				} else if (isEntity(type)) {
					return "Entity";
				}
				return null;
		}
	}

	/**
	 * 获取数据类型标识
	 * 
	 * @param clazz
	 * @return 0 未知
	 */
	public final static int getType(Class<?> clazz) {
		if (clazz == boolean.class)
			return _boolean;
		else if (clazz == byte.class)
			return _byte;
		else if (clazz == char.class)
			return _char;
		else if (clazz == short.class)
			return _short;
		else if (clazz == int.class)
			return _int;
		else if (clazz == long.class)
			return _long;
		else if (clazz == float.class)
			return _float;
		else if (clazz == double.class)
			return _double;
		else if (clazz == Boolean.class)
			return BOOLEAN;
		else if (clazz == Byte.class)
			return BYTE;
		else if (clazz == Character.class)
			return CHARACTER;
		else if (clazz == Short.class)
			return SHORT;
		else if (clazz == Integer.class)
			return INTEGER;
		else if (clazz == Long.class)
			return LONG;
		else if (clazz == Float.class)
			return FLOAT;
		else if (clazz == Double.class)
			return DOUBLE;
		else if (clazz == BigDecimal.class)
			return BIG_DECIMAL;
		else if (clazz == Date.class)
			return DATE;
		else if (clazz == LocalTime.class)
			return LOCAL_TIME;
		else if (clazz == LocalDate.class)
			return LOCAL_DATE;
		else if (clazz == LocalDateTime.class)
			return LOCAL_DATE_TIME;
		else if (clazz == String.class)
			return STRING;
		else if (clazz.isArray())
			return ARRAY;
		else if (ODBSReflect.isImplemented(clazz, List.class))
			return LIST;
		else if (ODBSReflect.isImplemented(clazz, Set.class))
			return SET;
		else if (ODBSReflect.isImplemented(clazz, Map.class))
			return MAP;
		else if (clazz.isEnum())
			return ENUM;
		else if (clazz == Object.class)
			return ANY;
		else
			return ENTITY;
	}

	/**
	 * 根据值获取基础数据类型标识
	 * 
	 * @param value
	 * @return 0 未知
	 */
	public final static int getType(Object value) {
		if (value == null)
			return UNKNOW;
		else if (value.getClass() == Boolean.class)
			return BOOLEAN;
		else if (value instanceof Number) {
			if (value.getClass() == Byte.class)
				return BYTE;
			else if (value.getClass() == Short.class)
				return SHORT;
			else if (value.getClass() == Integer.class)
				return INTEGER;
			else if (value.getClass() == Float.class)
				return FLOAT;
			else if (value.getClass() == Double.class)
				return DOUBLE;
			else if (value.getClass() == Long.class)
				return LONG;
			else if (value.getClass() == BigDecimal.class)
				return BIG_DECIMAL;
			else
				return UNKNOW;
			// BigInteger
			// DoubleAccumulator
			// DoubleAdder
			// LongAccumulator
			// LongAdder
		} else if (value.getClass() == Character.class)
			return CHARACTER;
		else if (value.getClass() == String.class)
			return STRING;
		else if (value.getClass() == Date.class)
			return DATE;
		else if (value.getClass() == LocalTime.class)
			return LOCAL_TIME;
		else if (value.getClass() == LocalDate.class)
			return LOCAL_DATE;
		else if (value.getClass() == LocalDateTime.class)
			return LOCAL_DATE_TIME;
		else
			return UNKNOW;
	}

	/**
	 * 获取基础类型(含值类型)的类型实例
	 * 
	 * @param type
	 * @return null 不是基础类型和值类型
	 */
	public final static Class<?> getBaseClass(int type) {
		switch (type) {
			case _boolean:
				return boolean.class;
			case _byte:
				return byte.class;
			case _char:
				return char.class;
			case _short:
				return short.class;
			case _int:
				return int.class;
			case _long:
				return long.class;
			case _float:
				return float.class;
			case _double:
				return double.class;
			case BOOLEAN:
				return Boolean.class;
			case BYTE:
				return Byte.class;
			case CHARACTER:
				return Character.class;
			case SHORT:
				return Short.class;
			case INTEGER:
				return Integer.class;
			case LONG:
				return Long.class;
			case FLOAT:
				return Float.class;
			case DOUBLE:
				return Double.class;
			case BIG_DECIMAL:
				return BigDecimal.class;
			case DATE:
				return Date.class;
			case LOCAL_TIME:
				return LocalTime.class;
			case LOCAL_DATE:
				return LocalDate.class;
			case LOCAL_DATE_TIME:
				return LocalDateTime.class;
			case STRING:
				return String.class;
			default:
				return null;
		}
	}

	public final static Object getDefaultValue(Class<?> clazz) {
		if (clazz == boolean.class)
			return false;
		else if (clazz == byte.class)
			return (byte) 0;
		else if (clazz == char.class)
			return (char) 0;
		else if (clazz == short.class)
			return (short) 0;
		else if (clazz == int.class)
			return (int) 0;
		else if (clazz == long.class)
			return 0L;
		else if (clazz == float.class)
			return 0F;
		else if (clazz == double.class)
			return 0D;
		else if (clazz == Boolean.class)
			return null;
		else if (clazz == Byte.class)
			return null;
		else if (clazz == Character.class)
			return null;
		else if (clazz == Short.class)
			return null;
		else if (clazz == Integer.class)
			return null;
		else if (clazz == Long.class)
			return null;
		else if (clazz == Float.class)
			return null;
		else if (clazz == Double.class)
			return null;
		else if (clazz == BigDecimal.class)
			return null;
		else if (clazz == Date.class)
			return null;
		else if (clazz == LocalTime.class)
			return null;
		else if (clazz == LocalDate.class)
			return null;
		else if (clazz == LocalDateTime.class)
			return null;
		else if (clazz == String.class)
			return null;
		else
			return null;
	}

	public final static Byte DEFAULT_BYTE = Byte.valueOf((byte) 0);
	public final static Character DEFAULT_CHAR = Character.valueOf((char) 0);
	public final static Short DEFAULT_SHORT = Short.valueOf((short) 0);
	public final static Integer DEFAULT_INTEGER = Integer.valueOf(0);
	public final static Long DEFAULT_LONG = Long.valueOf(0);
	public final static Float DEFAULT_FLOAT = Float.valueOf(0);
	public final static Double DEFAULT_DOUBLE = Double.valueOf(0);
}
