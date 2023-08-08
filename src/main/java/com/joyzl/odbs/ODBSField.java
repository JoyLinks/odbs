/*
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

/**
 * 字段
 * <p>
 * 未来可改进为ReflectASM方案
 * </p>
 *
 * @author ZhangXi
 * @date 2020年6月3日
 */
public final class ODBSField {

	/**
	 * 检查对象方法是否符合序列化要求
	 *
	 * @param method
	 * @return true / false
	 */
	public final static boolean check(Method method) {
		if (method.isBridge()) {
			// 桥接方法
			return false;
		}
		if (method.isDefault()) {
			// 默认方法
			return false;
		}
		if (method.isSynthetic()) {
			// 编译器引入方法
			return false;
		}
		if (method.isVarArgs()) {
			// 可变参数
			return false;
		}

		if ((method.getModifiers() & Modifier.PUBLIC) != 0) {
			if (method.getParameterCount() > 1) {
				// 参数过多
				return false;
			}

			if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
				return method.getParameterCount() == 0;
			} else if (method.getName().startsWith("set")) {
				return method.getParameterCount() == 1;
			} else {
				return false;
			}
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////////////

	private final int INDEX;
	private final String[] NAMES;
	private final Method GETTER;
	private final Method SETTER;

	private final Object defaultValue;
	private ODBSType type;

	public ODBSField(Method getter, Method setter, int idx) {
		GETTER = getter;
		SETTER = setter;
		INDEX = idx;
		// getUser/isUser -> User
		// NAME = getter.getName().startsWith("is") ?
		// getter.getName().substring(2) : getter.getName().substring(3);
		// 20230713 为JSON支持多种名称格式
		NAMES = JSONName.precut(getter.getName());

		getter.setAccessible(true);
		setter.setAccessible(true);

		Class<?> getterType = getter.getReturnType();
		Class<?> setterType = setter.getParameterTypes()[0];
		if (getterType == setterType) {
			defaultValue = ODBSTypes.getDefaultValue(setterType);
		} else {
			throw new IllegalArgumentException("GETTER " + getter + " 和 SETTER " + setter + " 数据类型不匹配");
		}
	}

	public final int getIndex() {
		return INDEX;
	}

	/**
	 * 获取默认方法名称
	 * 
	 * @return getName() -> "Name"
	 */
	public final String getName() {
		// return GETTER.getName() + '/' + SETTER.getName();
		// 原合并名称替换为剥离get/set/is前缀的名称，以便对JSON提供支持
		// getUser/isUser -> User
		// return NAME;
		// 20230713 JSON多种名称格式，索引0为默认名称
		return NAMES[0];
	}

	/**
	 * 获取指定命名格式的方法/字段名称
	 * 
	 * @param format {@link JSONName}
	 * @return String
	 */
	public final String getName(JSONName format) {
		// 获取指定命名格式的键名称
		return NAMES[format.ordinal()];
	}

	public final ODBSType getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	public final <T> T getValue(Object instence) {
		try {
			return (T) GETTER.invoke(instence);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	public final void setValue(Object instence, Object value) {
		try {
			SETTER.invoke(instence, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(getName(), e);
		}
	}

	/**
	 * 设置实例字段值为默认值
	 *
	 * @param instence 对象实例
	 * @throws Exception
	 */
	public final void setDefault(Object instence) {
		if (ODBSTypes.isList(type.value()) || ODBSTypes.isSet(type.value())) {
			// 集合字段不能设置默认值，只能清空集合，可避免重复创建集合
			// 集合中的值也是集合的，只能保留字段集合实例
			final Collection<?> value = getValue(instence);
			if (value != null) {
				value.clear();
			}
		} else if (ODBSTypes.isMap(type.value())) {
			// 集合字段不能设置默认值，只能清空集合，可避免重复创建集合
			// 集合中的值也是集合的，只能保留字段集合实例
			final Map<?, ?> value = getValue(instence);
			if (value != null) {
				value.clear();
			}
		} else {
			try {
				SETTER.invoke(instence, defaultValue);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 测试实例字段值是否为默认值
	 *
	 * @param instence 对象实例
	 * @return true / false
	 * @throws Exception
	 */
	public final boolean isDefault(Object instence) {
		// 如果是值类型 默认值 0 / false
		// 如果是对象类型 默认值 null
		try {
			final Object value = GETTER.invoke(instence);
			return value == null || value == defaultValue || value.equals(defaultValue);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 测试实例字段值是否为默认值
	 *
	 * @param instence 对象实例
	 * @return true / false
	 * @throws Exception
	 */
	public final boolean isDefaultValue(Object value) {
		// 如果是值类型 默认值 0 / false
		// 如果是对象类型 默认值 null
		return value == null || value == defaultValue || value.equals(defaultValue);
	}

	/**
	 * 提供Field类型的后期确定，必须在所有参与序列化/反序列化的枚举和实体对象索引建立完成之后才能处理字段类型描述,<br>
	 * 因为字段类型可能会引用到具体的实体和枚举，需要通过索引进行标识
	 */
	public final void makeType(ODBS odbs) {
		type = ODBSType.make(odbs, GETTER.getReturnType(), ODBSReflect.findGeneric(GETTER));
	}

	@Override
	public String toString() {
		if (type == null) {
			return "-- " + NAMES[0];
		}
		return type.toString() + " " + NAMES[0];
	}
}
