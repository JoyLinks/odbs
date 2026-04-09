package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final class TypeMap extends ODBSType {

	private final ODBSType key, value;

	public TypeMap(ODBSType k, ODBSType v) {
		value = v;
		key = k;
	}

	@Override
	public Class<?> type() {
		return Map.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (Map<?, ?>) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final Map<?, ?> values;
		try {
			values = (Map<Object, Object>) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (values != null) {
			codec.writeField(out, method);
			codec.writeMap(out, key, value, values);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		Map<Object, Object> values;
		try {
			values = (Map<Object, Object>) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (values == null) {
			values = new HashMap<>();
		}
		codec.readMap(in, key, value, values);
		try {
			method.set().invokeExact(entity, values);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Map<" + key + " " + value + ">";
	}

	@Override
	<O, I> Object read(ODBSCodec<O, I> codec, I in) throws IOException {
		final Map<Object, Object> values = new HashMap<>();
		codec.readMap(in, key, value, values);
		return values;
	}

	@Override
	@SuppressWarnings("unchecked")
	<O, I> void write(Object values, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeMap(out, key, value, (Map<Object, Object>) values);
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Map.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Map.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}