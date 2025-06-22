/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.odbs.test;

import com.joyzl.EnumCodeText;

/**
 * 自定义代码和文本枚举
 * 
 * @author ZhangXi
 * @date 2023年7月26日
 */
public enum EnumCodeTexts implements EnumCodeText {

	RECORD(2, "记录"), NOTICE(4, "通知"), WARNING(6, "警告"), DANGER(8, "严重");

	private final int code;
	private final String text;

	EnumCodeTexts(int c, String t) {
		code = c;
		text = t;
	}

	@Override
	public int code() {
		return code;
	}

	@Override
	public String text() {
		return text;
	}
}
