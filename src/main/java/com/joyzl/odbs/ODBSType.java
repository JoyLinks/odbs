package com.joyzl.odbs;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

abstract class ODBSType {

	/** 类型对应的实际类 */
	public abstract Class<?> type();

	/** 类型名称 */
	public String name() {
		return type().getSimpleName();
	}

	/** 构建类型的数组实例 */
	public Object newArray(int length) {
		return Array.newInstance(type(), length);
	}

	/** 方法协变句柄 */
	abstract MethodHandle methodHandle(Method method) throws IllegalAccessException;

	/** 实例方法设置默认值 */
	abstract void give(Object entity, ODBSMethod method);

	/** 编码实例方法值，非空值和默认值编码 */
	abstract <O> void write1(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException;

	/** 编码实例方法值，返回是否被编码标识，非空值均编码 */
	abstract <O> void write2(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException;

	/** 编码值 */
	abstract <O> void write(Object value, ODBSEncode<O> codec, O out) throws IOException;

	/** 解码实例方法值 */
	abstract <I> void read(Object entity, ODBSMethod method, ODBSDecode<I> codec, I in) throws IOException;

	/** 解码值 */
	abstract <I> Object read(ODBSDecode<I> codec, I in) throws IOException;

	@Override
	public String toString() {
		return type().getSimpleName();
	}
}