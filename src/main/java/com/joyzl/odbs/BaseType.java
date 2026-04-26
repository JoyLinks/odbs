package com.joyzl.odbs;

import java.io.IOException;

/**
 * 基本类型（实例化既不可变，可空）
 * 
 * @author ZhangXi 2026年3月25日
 */
abstract class BaseType extends ODBSType {

	@Override
	<O> void write2(Object entity, ODBSMethod method, ODBSEncode<O> codec, O out) throws IOException {
		write1(entity, method, codec, out);
	}
}