package com.joyzl.odbs.test;

import com.joyzl.EnumText;

/**
 * 自定义文本枚举
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2023年7月26日
 */
public enum EnumTexts implements EnumText {
	RECORD("记录"), NOTICE("通知"), WARNING("警告"), DANGER("严重");

	private final String text;

	EnumTexts(String t) {
		text = t;
	}

	@Override
	public String text() {
		return text;
	}
}