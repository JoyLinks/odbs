package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

final class BaseInteger extends BaseType {

	public final static BaseInteger INSTANCE = new BaseInteger();

	private BaseInteger() {
	}

	@Override
	public Class<?> type() {
		return Integer.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (Integer) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final Integer value;
		try {
			value = (Integer) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeInt(out, value.intValue());
		}
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		final Integer value = Integer.valueOf(codec.readInt(in));
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> Object read(ODBSCodec<O, I> codec, I in) throws IOException {
		return codec.readInt(in);
	}

	@Override
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeInt(out, ((Integer) value).intValue());
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Integer.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Integer.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}