package com.joyzl.odbs;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 方法类型
 * 
 * @author ZhangXi 2026年3月25日
 */
final class ODBSMethod {

	private final int index;
	private final ODBSType type;
	private final String[] names;
	private final MethodHandle get, set;

	public ODBSMethod(Method getter, Method setter, Map<?, ODBSType> types, int i) {
		index = i;
		try {
			if (getter == null) {
				if (setter == null) {
					throw new IllegalArgumentException();
				} else {
					setter.setAccessible(true);
					names = JSONName.precut(name(setter));
					type = resolve(setter.getGenericParameterTypes()[0], types);
					set = type.methodHandle(setter);
					get = null;
				}
			} else {
				getter.setAccessible(true);
				names = JSONName.precut(name(getter));
				type = resolve(getter.getGenericReturnType(), types);
				get = type.methodHandle(getter);

				if (setter == null) {
					set = null;
				} else {
					setter.setAccessible(true);
					set = type.methodHandle(setter);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public int index() {
		return index;
	}

	public String name() {
		return names[0];
	}

	public ODBSType type() {
		return type;
	}

	public MethodHandle set() {
		return set;
	}

	public MethodHandle get() {
		return get;
	}

	/** 获取指定命名格式的键名称 **/
	public String name(JSONName format) {
		return names[format.ordinal()];
	}

	/** 检查键名是否匹配方法名称 */
	public boolean match(JSONName format, CharSequence name) {
		return JSONName.match(names, format, name);
	}

	public ODBSType resolve(Type type, Map<?, ODBSType> types) {
		if (type instanceof Class<?> c) {
			// 原始类型、普通类、数组、枚举

			ODBSType o = baseType(c);
			if (o != null) {
				return o;
			}
			o = types.get(c);
			if (o != null) {
				return o;
			}
			if (c.isArray())
				return new TypeArray(resolve(c.getComponentType(), types));
			else if (Map.class.isAssignableFrom(c))
				return new TypeMap(null, null);
			else if (Set.class.isAssignableFrom(c))
				return new TypeSet(null);
			else if (List.class.isAssignableFrom(c))
				return new TypeList(null);
			else if (Collection.class.isAssignableFrom(c))
				return new TypeCollection(null);
			else
				return new TypeObject(c);
		} else if (type instanceof ParameterizedType p) {
			// 参数化类型 List<T> SetT<> Map<K,V>

			// 获取使用泛型的主类 List/Set/Map
			type = p.getRawType();
			if (type instanceof Class<?> c) {
				if (c.isArray()) {
					ODBSType o = resolve(c.getComponentType(), types);
					return new TypeArray(o);
				} else if (Map.class.isAssignableFrom(c)) {
					ODBSType key = resolve(p.getActualTypeArguments()[0], types);
					ODBSType value = resolve(p.getActualTypeArguments()[1], types);
					return new TypeMap(key, value);
				} else if (Set.class.isAssignableFrom(c)) {
					ODBSType o = resolve(p.getActualTypeArguments()[0], types);
					return new TypeSet(o);
				} else if (List.class.isAssignableFrom(c)) {
					ODBSType o = resolve(p.getActualTypeArguments()[0], types);
					return new TypeList(o);
				} else if (Collection.class.isAssignableFrom(c)) {
					ODBSType o = resolve(p.getActualTypeArguments()[0], types);
					return new TypeCollection(o);
				} else {
					throw new IllegalStateException("无效类型" + c);
				}
			} else {
				throw new IllegalStateException("无效类型" + type);
			}
		} else if (type instanceof GenericArrayType) {
			// 泛型数组 T[]
			throw new IllegalStateException("无效类型" + type);
		} else if (type instanceof TypeVariable) {
			// 类型变量 T、K、V
			throw new IllegalStateException("无效类型" + type);
		} else if (type instanceof WildcardType) {
			// 通配类型 <? extends Number>
			throw new IllegalStateException("无效类型" + type);
		} else {
			// 未知类型
			throw new IllegalStateException("无效类型" + type);
		}
	}

	final ODBSType baseType(Type type) {
		if (type == boolean.class)
			return ValueBool.INSTANCE;
		else if (type == byte.class)
			return ValueByte.INSTANCE;
		else if (type == char.class)
			return ValueChar.INSTANCE;
		else if (type == short.class)
			return ValueShort.INSTANCE;
		else if (type == int.class)
			return ValueInt.INSTANCE;
		else if (type == long.class)
			return ValueLong.INSTANCE;
		else if (type == float.class)
			return ValueFloat.INSTANCE;
		else if (type == double.class)
			return ValueDouble.INSTANCE;
		else if (type == Boolean.class)
			return BaseBoolean.INSTANCE;
		else if (type == Byte.class)
			return BaseByte.INSTANCE;
		else if (type == Character.class)
			return BaseCharacter.INSTANCE;
		else if (type == Short.class)
			return BaseShort.INSTANCE;
		else if (type == Integer.class)
			return BaseInteger.INSTANCE;
		else if (type == Long.class)
			return BaseLong.INSTANCE;
		else if (type == Float.class)
			return BaseFloat.INSTANCE;
		else if (type == Double.class)
			return BaseDouble.INSTANCE;
		else if (type == BigDecimal.class)
			return BaseBigDecimal.INSTANCE;
		else if (type == BigInteger.class)
			return BaseBigInteger.INSTANCE;
		else if (type == Date.class)
			return BaseDate.INSTANCE;
		else if (type == LocalTime.class)
			return BaseLocalTime.INSTANCE;
		else if (type == LocalDate.class)
			return BaseLocalDate.INSTANCE;
		else if (type == LocalDateTime.class)
			return BaseLocalDateTime.INSTANCE;
		else if (type == String.class)
			return BaseString.INSTANCE;
		else
			return null;
	}

	/* getUser/isUser -> User */
	public static String name(Method method) {
		return name(method.getName());
	}

	/* getUser/isUser -> User */
	public static String name(String value) {
		if (value.startsWith("is")) {
			return value.substring(2);
		} else if (value.startsWith("get")) {
			return value.substring(3);
		} else if (value.startsWith("set")) {
			return value.substring(3);
		} else {
			return null;
		}
	}
}