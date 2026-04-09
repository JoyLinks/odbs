package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

final class BaseByte extends BaseType {

	public final static BaseByte INSTANCE = new BaseByte();

	private BaseByte() {
	}

	@Override
	public Class<?> type() {
		return Byte.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (Byte) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> void write1(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, O out) throws IOException {
		final Byte value;
		try {
			value = (Byte) method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (value != null) {
			codec.writeField(out, method);
			codec.writeByte(out, value.byteValue());
		}
	}

	@Override
	<O, I> void read(Object entity, ODBSMethod method, ODBSCodec<O, I> codec, I in) throws IOException {
		final Byte value = Byte.valueOf(codec.readByte(in));
		try {
			method.set().invokeExact(entity, value);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O, I> Object read(ODBSCodec<O, I> codec, I in) throws IOException {
		return codec.readByte(in);
	}

	@Override
	<O, I> void write(Object value, ODBSCodec<O, I> codec, O out) throws IOException {
		codec.writeByte(out, ((Byte) value).byteValue());
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Byte.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			return handler.asType(MethodType.methodType(void.class, Object.class, Byte.class));
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}