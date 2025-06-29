/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.odbs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * 字段
 *
 * @author ZhangXi
 * @date 2020年6月3日
 */
public final class ODBSField {

	private final String[] NAMES;
	private final Method GETTER;
	private final Method SETTER;

	private final Object defaultValue;
	private ODBSType type;

	ODBSField(Method getter, Method setter) {
		GETTER = getter;
		SETTER = setter;

		// getUser/isUser -> User
		// 20230713 为JSON预制多种名称格式
		if (getter != null) {
			NAMES = JSONName.precut(getter.getName());
		} else {
			NAMES = JSONName.precut(setter.getName());
		}

		Class<?> getterType = null, setterType = null;
		if (getter != null) {
			getter.setAccessible(true);
			getterType = getter.getReturnType();
		}
		if (setter != null) {
			setter.setAccessible(true);
			setterType = setter.getParameterTypes()[0];
		}

		if (getterType != null && setterType != null) {
			if (getterType == setterType) {
				defaultValue = ODBSTypes.getDefaultValue(setterType);
			} else {
				throw new IllegalArgumentException("GETTER " + getter + " 和 SETTER " + setter + " 类型不匹配");
			}
		} else if (getterType != null) {
			defaultValue = ODBSTypes.getDefaultValue(getterType);
		} else if (setterType != null) {
			defaultValue = ODBSTypes.getDefaultValue(setterType);
		} else {
			throw new IllegalArgumentException("未指定有效方法");
		}
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
		// TODO 是否可改进为org.ow2.asm方案
		try {
			return (T) GETTER.invoke(instence);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(instence.getClass() + "." + getName(), e);
		}
	}

	public final void setValue(Object instence, Object value) {
		// TODO 是否可改进为org.ow2.asm方案
		try {
			SETTER.invoke(instence, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(instence.getClass() + "." + getName() + ":" + value, e);
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
				throw new RuntimeException(instence.getClass() + "." + getName(), e);
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
			throw new RuntimeException(instence.getClass() + "." + getName(), e);
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
		if (GETTER != null) {
			type = ODBSType.make(odbs, GETTER.getReturnType(), ODBSReflect.findGeneric(GETTER));
			return;
		}
		if (SETTER != null) {
			type = ODBSType.make(odbs, SETTER.getParameterTypes()[0], ODBSReflect.findGeneric(SETTER));
		}
	}

	public boolean isPaired() {
		return SETTER != null && GETTER != null;
	}

	public boolean hasSetter() {
		return SETTER != null;
	}

	public boolean hasGetter() {
		return GETTER != null;
	}

	@Override
	public String toString() {
		if (type == null) {
			return "-- " + NAMES[0];
		}
		return type.toString() + " " + NAMES[0];
	}
}