/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
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