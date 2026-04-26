package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

final class BaseDouble extends BaseType {

	public final static BaseDouble INSTANCE = new BaseDouble();

	private BaseDouble() {
	}

	@Override
	public Class<?> type() {
		return Double.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (Double) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O> void write1(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException {
		final Double value;
		try {
			value = (Double) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeDouble(out, value.doubleValue());
		}
	}

	@Override
	<I> void read(Object entity, ODBSMethod method, ODBSDecode<I> codec, I in) throws IOException {
		final Double value = Double.valueOf(codec.readDouble(in));
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<I> Object read(ODBSDecode<I> codec, I in) throws IOException {
		return codec.readDouble(in);
	}

	@Override
	<O> void write(Object value, ODBSEncode<O> codec, O out) throws IOException {
		codec.writeDouble(out, ((Double) value).doubleValue());
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Double.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Double.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}