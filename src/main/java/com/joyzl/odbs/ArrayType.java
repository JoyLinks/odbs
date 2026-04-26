package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

final class ArrayType extends ODBSType {

	private final ODBSType type;

	public ArrayType(ODBSType t) {
		type = t;
	}

	@Override
	public Class<?> type() {
		return Array.class;
	}

	@Override
	void give(Object entity, ODBSMethod method) {
		try {
			method.set().invokeExact(entity, (Object) null);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<O> void write1(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException {
		final Object values;
		try {
			values = method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		if (values != null) {
			codec.writeField(out, method);
			codec.writeArray(out, type, values);
		}
	}

	@Override
	<O> void write2(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}

	@Override
	<I> void read(Object entity, ODBSMethod method, ODBSDecode<I> codec, I in) throws IOException {
		Object values;
		try {
			values = method.get().invokeExact(entity);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		values = codec.readArray(in, type, values);
		try {
			method.set().invokeExact(entity, values);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	<I> Object read(ODBSDecode<I> codec, I in) throws IOException {
		return codec.readArray(in, type, null);
	}

	@Override
	<O> void write(Object values, ODBSEncode<O> codec, O out) throws IOException {
		codec.writeArray(out, type, values);
	}

	@Override
	public String toString() {
		return type + "[]";
	}

	@Override
	MethodHandle methodHandle(Method method) throws IllegalAccessException {
		final MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		if (method.getParameterCount() == 0) {
			return handler.asType(MethodType.methodType(Object.class, Object.class));
		} else if (method.getParameterCount() == 1) {
			if (method.isVarArgs()) {
				// 可变参数作为固定单个数组参数
				// 否则会将传入的数组视为可变参数的单个值，这将导致类型失败
				return handler.asFixedArity().asType(MethodType.methodType(void.class, Object.class, Object.class));
			} else {
				return handler.asType(MethodType.methodType(void.class, Object.class, Object.class));
			}
		} else {
			throw new RuntimeException("方法无效" + method);
		}
	}
}