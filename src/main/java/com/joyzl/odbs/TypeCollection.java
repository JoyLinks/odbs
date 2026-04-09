package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

final class TypeCollection extends ODBSType {

	private final ODBSType type;

	public TypeCollection(ODBSType t) {
		type = t;
	}

	@Override
	public Class<?> type() {
		return Collection.class;
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
		final Collection<Object> values;
		try {
			values = (Collection<Object>) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (values != null) {
			codec.writeField(out, method);
			codec.writeCollection(out, type, values);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		Collection<Object> values;
		try {
			values = (Collection<Object>) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (values == null) {
			values = new ArrayList<>();
		}
		codec.readCollection(in, type, values);
		try {
			method.set().invokeExact(entity, values);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> Object read(ODBSCodec<O, I> codec, I in) throws IOException {
		final Collection<Object> values = new ArrayList<>();
		codec.readCollection(in, type, values);
		return values;
	}

	@Override
	@SuppressWarnings("unchecked")
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeCollection(out, type, (Collection<Object>) value);
	}

	@Override
	public String toString() {
		return "Collection<" + type + ">";
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Collection.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Collection.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}