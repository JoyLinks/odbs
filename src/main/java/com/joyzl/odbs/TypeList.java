package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

final class TypeList extends ODBSType {

	private final ODBSType type;

	public TypeList(ODBSType t) {
		type = t;
	}

	@Override
	public Class<?> type() {
		return List.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (List<?>) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final List<Object> values;
		try {
			values = (List<Object>) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (values != null) {
			codec.writeField(out, method);
			codec.writeList(out, type, values);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		List<Object> values;
		try {
			values = (List<Object>) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (values == null) {
			values = new ArrayList<>();
		}
		codec.readList(in, type, values);
		try {
			method.set().invokeExact(entity, values);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> Object read(ODBSCodec<O, I> codec, I in) throws IOException {
		final List<Object> values = new ArrayList<>();
		codec.readList(in, type, values);
		return values;
	}

	@Override
	@SuppressWarnings("unchecked")
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeList(out, type, (List<Object>) value);
	}

	@Override
	public String toString() {
		return "List<" + type + ">";
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(List.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, List.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}