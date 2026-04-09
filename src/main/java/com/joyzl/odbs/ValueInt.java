package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

final class ValueInt extends ValueType {

	public final static ValueInt INSTANCE = new ValueInt();

	private ValueInt() {
	}

	@Override
	public Class<?> type() {
		return int.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, 0);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final int value;
		try {
			value = (int) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != 0) {
			codec.writeField(out, method);
			codec.writeInt(out, value);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final int value;
		try {
			value = (int) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		codec.writeField(out, method);
		codec.writeInt(out, value);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		final int value = codec.readInt(in);
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> Object read(ODBSCodec<O, I> codec, I in) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(int.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, int.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}