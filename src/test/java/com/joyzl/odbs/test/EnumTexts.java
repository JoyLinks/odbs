/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.odbs.test;

import com.joyzl.EnumText;

/**
 * 自定义文本枚举
 * 
 * @author ZhangXi
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