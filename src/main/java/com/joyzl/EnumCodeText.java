/*
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl;

/**
 * 枚举自定义代码和文本
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2020年6月6日
 */
public interface EnumCodeText extends EnumCode, EnumText {

	/**
	 * 根据聚合值获取枚举字符串(逗号分隔)
	 * 
	 * @param <T> 实现{@link EnumCodeText}接口的枚举实例
	 * @param clazz
	 * @param code
	 * @return 实例 / null
	 */
	static <T extends EnumCodeText> String stringOf(Class<T> clazz, int code) {
		StringBuilder builder = new StringBuilder();
		final T[] items = clazz.getEnumConstants();
		for (int index = 0; index < items.length; index++) {
			if ((items[index].code() & code) != 0) {
				if (builder.length() > 0) {
					builder.append(',');
				}
				builder.append(items[index].text());
			}
		}
		return builder.toString();
	}
}
