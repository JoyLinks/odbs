package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

final class ValueDouble extends ValueType {

	public final static ValueDouble INSTANCE = new ValueDouble();

	private ValueDouble() {
	}

	@Override
	public Class<?> type() {
		return double.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, 0D);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final double value;
		try {
			value = (double) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != 0) {
			codec.writeField(out, method);
			codec.writeDouble(out, value);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final double value;
		try {
			value = (double) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		codec.writeField(out, method);
		codec.writeDouble(out, value);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		final double value = codec.readDouble(in);
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
			return handler.asType(MethodType.methodType(double.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, double.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}