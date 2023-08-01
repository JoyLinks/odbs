/*
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 对象描述
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2020年6月3日
 */
public final class ODBSDescription {

	/**
	 * 检查源对象是否符合序列化要求
	 * 
	 * @param clazz
	 * @return true / false
	 */
	public final static boolean check(Class<?> clazz) {
		if (clazz.isAnnotation()) {
			// 注解类
			return false;
		}
		if (clazz.isAnonymousClass()) {
			// 匿名类
			return false;
		}
		if (clazz.isInterface()) {
			// 接口
			return false;
		}
		if (clazz.isLocalClass()) {
			// 局部类
			return false;
		}
		if (clazz.isPrimitive()) {
			// 基本类型 boolean, byte, char, short, int, long, float, double
			return false;
		}
		if (clazz.isSynthetic()) {
			// 编译器引入类
			return false;
		}
		if (clazz.isMemberClass()) {
			//
			return false;
		}

		if (contains(clazz.getModifiers(), Modifier.ABSTRACT)) {
			return false;
		}
		if (contains(clazz.getModifiers(), Modifier.INTERFACE)) {
			return false;
		}
		if (contains(clazz.getModifiers(), Modifier.PRIVATE)) {
			return false;
		}
		if (contains(clazz.getModifiers(), Modifier.NATIVE)) {
			return false;
		}
		if (contains(clazz.getModifiers(), Modifier.PROTECTED)) {
			return false;
		}
		if (contains(clazz.getModifiers(), Modifier.STATIC)) {
			return false;
		}

		if (clazz.isEnum()) {
			return true;
		}

		try {
			if (clazz.getConstructor() == null) {
				// 可访问(public)的无参构造函数
				return false;
			}
		} catch (NoSuchMethodException | SecurityException e) {
			return false;
		}
		return true;
	}

	static boolean contains(int source, int value) {
		return (source & value) != 0;
	}

	////////////////////////////////////////////////////////////////////////////////

	private final int INDEX;
	private final String NAME;
	private final Class<?> CLASS;
	private final Constructor<?> CLASS_CONSTRUCTOR;
	private final ODBSField[] FIELDS;

	private Class<?> override;
	private Constructor<?> override_constructor;

	public ODBSDescription(Class<?> clazz, int idx) {
		CLASS = clazz;
		NAME = clazz.getCanonicalName();
		INDEX = idx;

		try {
			CLASS_CONSTRUCTOR = clazz.getConstructor();
			CLASS_CONSTRUCTOR.setAccessible(true);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}

		// 查找所有有效的方法
		HashMap<String, Method> getter_setter = new HashMap<>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (ODBSField.check(method)) {
				getter_setter.put(method.getName().toLowerCase(), method);
			}
		}

		// get和set需要对应,否则序列化之后无法对象化
		// 通过get获取值,通过set设置值

		idx = 0;
		TreeSet<ODBSField> fields = new TreeSet<>(new Comparator<ODBSField>() {
			@Override
			public int compare(ODBSField f1, ODBSField f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});

		Method setter = null, getter = null;
		for (Map.Entry<String, Method> entry : getter_setter.entrySet()) {
			getter = entry.getValue();
			if (entry.getKey().startsWith("get")) {
				setter = getter_setter.get("set" + entry.getKey().substring(3));
			} else if (entry.getKey().startsWith("is")) {
				setter = getter_setter.get("set" + entry.getKey().substring(2));
			}

			if (setter != null) {
				if (setter.getParameterCount() == 1 && getter.getParameterCount() == 0 && getter.getReturnType() != Void.TYPE) {
					fields.add(new ODBSField(entry.getValue(), setter, idx++));
				}
			}
		}

		FIELDS = fields.toArray(new ODBSField[fields.size()]);
	}

	/**
	 * 覆盖源对象
	 * 
	 * @param clazz 继承自源对象的覆盖对象
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public void override(Class<?> clazz) {
		if (CLASS.isAssignableFrom(clazz)) {
			override = clazz;
			try {
				override_constructor = clazz.getConstructor();
				override_constructor.setAccessible(true);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new IllegalArgumentException(e);
			}
		} else {
			throw new IllegalArgumentException(clazz + "未继承自" + CLASS);
		}
	}

	/**
	 * 创建对象新实例,如果制定了继承对象覆盖则实例化覆盖对象
	 * 
	 * @return 新实例
	 * @throws Exception
	 */
	public final Object newInstence() {
		try {
			if (override == null) {
				return CLASS_CONSTRUCTOR.newInstance();
			} else {
				return override_constructor.newInstance();
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建对象数组实例
	 * 
	 * @return length 数组元素数量（数组长度）
	 */
	public final Object[] newArray(int length) {
		if (override == null) {
			return (Object[]) Array.newInstance(CLASS, length);
		} else {
			return (Object[]) Array.newInstance(override, length);
		}
	}

	/**
	 * 获取描述对象的索引
	 * 
	 * @return 0~n
	 */
	public final int getIndex() {
		return INDEX;
	}

	/**
	 * 获取对象完整名称
	 * 
	 * @return 例如:"com.joyzl.odbs"
	 */
	public final String getName() {
		return NAME;
	}

	/**
	 * 判断是否有字段,其含义为是否检索到具有符合JavaBean的getXXX/isXXX/setXXX方法
	 * 
	 * @return true / false
	 */
	public final boolean hasMethods() {
		return FIELDS.length > 0;
	}

	/**
	 * 源对象是否指定了覆盖对象
	 * 
	 * @return true/false
	 */
	public final boolean isOverride() {
		return override != null;
	}

	/**
	 * 获取源对象类型
	 * 
	 * @return Class<?>
	 */
	public final Class<?> getSourceClass() {
		return CLASS;
	}

	/**
	 * 获取覆盖对象类型
	 * 
	 * @return
	 */
	public final Class<?> getOverrideClass() {
		return override;
	}

	/**
	 * 获取指定索引的字段描述{@link ODBSField}
	 * 
	 * @param index
	 * @return ODBSField
	 */
	public final ODBSField getField(int index) {
		return FIELDS[index];
	}

	/**
	 * 获取指定名称的字段描述{@link ODBSField}
	 * 
	 * @param name "User"对应getUser/setUser
	 * @return ODBSField / null
	 */
	public final ODBSField getField(String name) {
		for (int index = 0; index < FIELDS.length; index++) {
			if (name.equals(FIELDS[index].getName())) {
				return FIELDS[index];
			}
		}
		return null;
	}

	/**
	 * 获取指定名称的字段描述{@link ODBSField}
	 * 
	 * @param name "User"对应getUser/setUser
	 * @return ODBSField / null
	 */
	public final ODBSField getField(JSONName format, String name) {
		for (int index = 0; index < FIELDS.length; index++) {
			if (name.equals(FIELDS[index].getName(format))) {
				return FIELDS[index];
			}
		}
		return null;
	}

	/**
	 * 获取字段数量
	 * 
	 * @return 0~n
	 */
	public final int getFieldCount() {
		return FIELDS.length;
	}

	/**
	 * 获取所有字段描述数组
	 * 
	 * @return ODBSField[]
	 */
	public final ODBSField[] getFields() {
		return FIELDS;
	}

	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(INDEX);
		builder.append('\t');
		builder.append(CLASS.getName());
		for (int index = 0; index < FIELDS.length; index++) {
			builder.append(',');
			builder.append(FIELDS[index]);
		}
		return builder.toString();
	}
}
