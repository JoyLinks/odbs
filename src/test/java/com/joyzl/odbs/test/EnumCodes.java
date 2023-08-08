package com.joyzl.odbs.test;

import com.joyzl.EnumCode;

/**
 * 自定义代码枚举
 * 
 * @author ZhangXi
 * @date 2023年7月26日
 */
public enum EnumCodes implements EnumCode {
	RECORD(2), NOTICE(4), WARNING(6), DANGER(8);

	private final int code;

	EnumCodes(int c) {
		code = c;
	}

	@Override
	public int code() {
		return code;
	}
}