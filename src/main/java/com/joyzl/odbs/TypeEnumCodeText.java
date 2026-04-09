package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import com.joyzl.EnumCodeText;

public class TypeEnumCodeText extends ODBSType {

	private final Class<? extends EnumCodeText> CLASS;
	private final EnumCodeText[] constants;

	public TypeEnumCodeText(Class<? extends EnumCodeText> c) {
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
			method.set().invokeExact(entity, (EnumCodeText) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final EnumCodeText value;
		try {
			value = (EnumCodeText) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeEnumCodeText(out, value);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		final EnumCodeText value = read(codec, in);
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> EnumCodeText read(ODBSCodec<O, I> codec, I in) throws IOException {
		final int code = codec.readEnumCode(in);
		for (int i = 0; i < constants.length; i++) {
			if (constants[i].code() == code) {
				return constants[i];
			}
		}
		return null;
	}

	@Override
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeEnumCodeText(out, (EnumCodeText) value);
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(EnumCodeText.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, EnumCodeText.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}