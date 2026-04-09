package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import com.joyzl.EnumCode;

public class TypeEnumCode extends ODBSType {

	private final Class<? extends EnumCode> CLASS;
	private final EnumCode[] constants;

	public TypeEnumCode(Class<? extends EnumCode> c) {
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
			method.set().invokeExact(entity, (EnumCode) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final EnumCode value;
		try {
			value = (EnumCode) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeEnumCode(out, value);
		}
	}

	@Override
	<O, I> void write2(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		final EnumCode value = read(codec, in);
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> EnumCode read(ODBSCodec<O, I> codec, I in) throws IOException {
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
		codec.writeEnumCode(out, (EnumCode) value);
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(EnumCode.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, EnumCode.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}