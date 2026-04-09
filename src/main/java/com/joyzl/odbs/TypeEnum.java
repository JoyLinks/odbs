package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

final class TypeEnum extends ODBSType {

	private final Class<? extends Enum<?>> CLASS;
	private final Enum<?>[] constants;

	public TypeEnum(Class<? extends Enum<?>> c) {
		constants = c.getEnumConstants();
		CLASS = c;
	}

	@Override
	public Class<?> type() {
		return CLASS;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (Enum<?>) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final Enum<?> value;
		try {
			value = (Enum<?>) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeEnum(out, value);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		final int value = codec.readEnum(in);
		try {
			if (value >= 0 && value < constants.length) {
				method.set().invokeExact(entity, constants[value]);
			} else {
				method.set().invokeExact(entity, (Enum<?>) null);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> Enum<?> read(ODBSCodec<O, I> codec, I in) throws IOException {
		final int value = codec.readEnum(in);
		if (value >= 0 && value < constants.length) {
			return constants[value];
		} else {
			return null;
		}
	}

	@Override
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeEnum(out, (Enum<?>) value);
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Enum.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Enum.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}