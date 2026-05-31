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
	<O> void write1(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException {
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
	<O> void write2(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<I> void read(Object entity, ODBSMethod method, ODBSDecode<I> codec, I in) throws IOException {
		final EnumText value = read(codec, in);
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<I> EnumText read(ODBSDecode<I> codec, I in) throws IOException {
		final int value = codec.readEnumText(in);
		if (value >= 0 && value < constants.length) {
			return constants[value];
		} else {
			return null;
		}
	}

	@Override
	<O> void write(Object value, ODBSEncode<O> codec, O out) throws IOException {
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