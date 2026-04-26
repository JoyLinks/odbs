package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.math.BigInteger;

final class BaseBigInteger extends BaseType {

	public final static BaseBigInteger INSTANCE = new BaseBigInteger();

	private BaseBigInteger() {
	}

	@Override
	public Class<?> type() {
		return BigInteger.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (BigInteger) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O> void write1(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException {
		final BigInteger value;
		try {
			value = (BigInteger) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeBigInteger(out, value);
		}
	}

	@Override
	<I> void read(Object entity, ODBSMethod method, ODBSDecode<I> codec, I in) throws IOException {
		final BigInteger value = codec.readBigInteger(in);
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<I> Object read(ODBSDecode<I> codec, I in) throws IOException {
		return codec.readBigInteger(in);
	}

	@Override
	<O> void write(Object value, ODBSEncode<O> codec, O out) throws IOException {
		codec.writeBigInteger(out, (BigInteger) value);
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(BigInteger.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, BigInteger.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}