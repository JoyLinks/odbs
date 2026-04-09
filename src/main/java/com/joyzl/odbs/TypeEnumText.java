package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import com.joyzl.EnumText;

final class TypeEnumText extends ODBSType {

	private final Class<? extends EnumText> CLASS;
	private final EnumText[] constants;

	public TypeEnumText(Class<? extends EnumText> c) {
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
			method.set().invokeExact(entity, (EnumText) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final EnumText value;
		try {
			value = (EnumText) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeEnumText(out, value);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		final int value = codec.readEnumText(in);
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
	<O, I> EnumText read(ODBSCodec<O, I> codec, I in) throws IOException {
		final int value = codec.readEnumText(in);
		if (value >= 0 && value < constants.length) {
			return constants[value];
		} else {
			return null;
		}
	}

	@Override
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeEnumText(out, (EnumText) value);
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(EnumText.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, EnumText.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}