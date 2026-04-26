package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

final class BaseShort extends BaseType {

	public final static BaseShort INSTANCE = new BaseShort();

	private BaseShort() {
	}

	@Override
	public Class<?> type() {
		return Short.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (Short) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O> void write1(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException {
		final Short value;
		try {
			value = (Short) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeShort(out, value.shortValue());
		}
	}

	@Override
	<I> void read(Object entity, ODBSMethod method, ODBSDecode<I> codec, I in) throws IOException {
		final Short value = Short.valueOf(codec.readShort(in));
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<I> Object read(ODBSDecode<I> codec, I in) throws IOException {
		return codec.readShort(in);
	}

	@Override
	<O> void write(Object value, ODBSEncode<O> codec, O out) throws IOException {
		codec.writeShort(out, ((Short) value).shortValue());
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Short.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Short.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}