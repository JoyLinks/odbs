package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

final class BaseCharacter extends BaseType {

	public final static BaseCharacter INSTANCE = new BaseCharacter();

	private BaseCharacter() {
	}

	@Override
	public Class<?> type() {
		return Character.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (Character) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O> void write1(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException {
		final Character value;
		try {
			value = (Character) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeChar(out, value.charValue());
		}
	}

	@Override
	<I> void read(Object entity, ODBSMethod method, ODBSDecode<I> codec, I in) throws IOException {
		final Character value = Character.valueOf(codec.readChar(in));
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<I> Object read(ODBSDecode<I> codec, I in) throws IOException {
		return codec.readChar(in);
	}

	@Override
	<O> void write(Object value, ODBSEncode<O> codec, O out) throws IOException {
		codec.writeChar(out, ((Character) value).charValue());
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Character.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Character.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}