package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

final class TypeSet extends ODBSType {

	private final ODBSType type;

	public TypeSet(ODBSType t) {
		type = t;
	}

	@Override
	public Class<?> type() {
		return Set.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (Set<?>) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final Set<Object> values;
		try {
			values = (Set<Object>) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (values != null) {
			codec.writeField(out, method);
			codec.writeSet(out, type, values);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		Set<Object> values;
		try {
			values = (Set<Object>) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (values == null) {
			values = new HashSet<>();
		}
		codec.readSet(in, type, values);
		try {
			method.set().invokeExact(entity, values);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> Object read(ODBSCodec<O, I> codec, I in) throws IOException {
		final Set<Object> values = new HashSet<>();
		codec.readSet(in, type, values);
		return values;
	}

	@Override
	@SuppressWarnings("unchecked")
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeSet(out, type, (Set<Object>) value);
	}

	@Override
	public String toString() {
		return "Set<" + type + ">";
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Set.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Set.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}