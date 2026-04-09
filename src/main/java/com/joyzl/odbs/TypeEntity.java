package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 编码或解码明确类型的实体
 * 
 * @author ZhangXi 2026年4月6日
 */
final class TypeEntity extends ODBSType {

	private final int index;
	private final Class<?> CLASS;

	private Class<?> override;
	private Constructor<?> constructor;
	private ODBSMethod[] methods;

	public TypeEntity(Class<?> c, int i) {
		try {
			constructor = c.getConstructor();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		CLASS = c;
		index = i;
	}

	@Override
	public Class<?> type() {
		return CLASS;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final Object value;
		try {
			value = method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeEntity(out, this, value);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		final Object value = codec.readEntity(in, this, entity);
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> Object read(ODBSCodec<O, I> codec, I in) throws IOException {
		return codec.readEntity(in, this, null);
	}

	@Override
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeEntity(out, this, value);
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Object.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Object.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}

	/** 查找指定名称的方法 */
	public ODBSMethod find(JSONName format, CharSequence name) {
		for (int index = 0; index < methods.length; index++) {
			if (methods[index].match(format, name)) {
				return methods[index];
			}
		}
		return null;
	}

	public int methodSize() {
		return methods.length;
	}

	public ODBSMethod[] methods() {
		return methods;
	}

	public int index() {
		return index;
	}

	public void override(Class<?> c) throws NoSuchMethodException, SecurityException {
		if (CLASS.isAssignableFrom(c)) {
			constructor = c.getConstructor();
			override = c;
		} else {
			throw new IllegalArgumentException(c + "未继承自" + CLASS);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T newInstance() {
		try {
			return (T) constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object newArray(int length) {
		if (override == null) {
			return Array.newInstance(CLASS, length);
		} else {
			return Array.newInstance(override, length);
		}
	}

	void resolve(Map<?, ODBSType> types) {
		// 查找所有有效的方法
		// 20250414修正重载方法干扰（不能按名称匹配，必须检查类型）
		final HashSet<Method> getters = new HashSet<>();
		final HashSet<Method> setters = new HashSet<>();
		final Method[] items = CLASS.getMethods();
		for (Method m : items) {
			if (ODBSReflect.canSerialize(m)) {
				// 20250722 排除 is() set() get() 方法
				// 这些方法无法在JSON时具有名称（剥离后为空字符串）

				if (m.getName().length() > 2) {
					if (m.getParameterCount() == 0) {
						if (m.getReturnType() != Void.TYPE) {
							if (m.getName().startsWith("is")) {
								getters.add(m);
								continue;
							}
						}
					}
				}
				if (m.getName().length() > 3) {
					if (m.getParameterCount() == 0) {
						if (m.getName().startsWith("get")) {
							if (m.getReturnType() != Void.TYPE) {
								getters.add(m);
								continue;
							}
						}
					}
					if (m.getParameterCount() == 1) {
						if (m.getName().startsWith("set")) {
							if (m.getReturnType() == Void.TYPE) {
								setters.add(m);
								continue;
							}
						}
					}
				}
			}
		}

		// 遍历构建方法对应关系
		final Map<String, Method[]> ms = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		// get和set需要对应,否则序列化之后无法还原对象
		// 支持未对应的方法，在JSON序列化时可用
		// 通过get获取值,通过set设置值

		String name;
		Method getter, setter;
		Iterator<Method> settersIterator;
		Iterator<Method> gettersIterator = getters.iterator();
		while (gettersIterator.hasNext()) {
			getter = gettersIterator.next();
			name = ODBSMethod.name(getter);
			if (name == null) {
				gettersIterator.remove();
				continue;
			}

			settersIterator = setters.iterator();
			while (settersIterator.hasNext()) {
				setter = settersIterator.next();
				if (setter.getParameterTypes()[0] == getter.getReturnType()) {
					if (name.equals(ODBSMethod.name(setter))) {
						ms.put(name, new Method[] { getter, setter });
						gettersIterator.remove();
						settersIterator.remove();
						break;
					}
				}
			}
		}
		if (getters.size() > 0) {
			gettersIterator = getters.iterator();
			while (gettersIterator.hasNext()) {
				getter = gettersIterator.next();
				name = ODBSMethod.name(getter);
				ms.put(name, new Method[] { getter, null });
			}
		}
		if (setters.size() > 0) {
			settersIterator = setters.iterator();
			while (settersIterator.hasNext()) {
				setter = settersIterator.next();
				name = ODBSMethod.name(setter);
				ms.put(name, new Method[] { null, setter });
			}
		}

		int index = 0;
		ODBSMethod method;
		methods = new ODBSMethod[ms.size()];
		for (Entry<String, Method[]> m : ms.entrySet()) {
			method = new ODBSMethod(m.getValue()[0], m.getValue()[1], types, index);
			methods[index++] = method;
		}
	}
}