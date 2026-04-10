package com.joyzl.odbs.time;

import com.joyzl.EnumCodeText;

public enum TestEnum implements EnumCodeText {

	UNKNOWN(0, "未知"), VALID(1, "有效"), INVALID(2, "无效");

	private final int code;
	private final String text;

	private TestEnum(int code, String text) {
		this.code = code;
		this.text = text;
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