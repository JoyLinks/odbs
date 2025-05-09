/*-
 * www.joyzl.net
 * 重庆骄智科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

/**
 * JSON键名格式
 * 
 * @author ZhangXi
 * @date 2023年7月13日
 */
public enum JSONName {

	/**
	 * 默认，保持JavaBean格式，getUserName() UserName
	 */
	DEFAULT,
	/**
	 * 大骆驼（帕斯卡），PascalCase， getUserName() UserName
	 */
	UPPER_CAMEL_CASE,
	/**
	 * 小骆驼，camelCase，getUserName() userName
	 */
	LOWER_CAMEL_CASE,
	/**
	 * 烤肉串，kebab-case， getUserName() user-name
	 */
	KEBAB_CASE,
	/**
	 * 蛇形，snake_case， getUserName() user_name
	 */
	SNAKE_CASE,
	/**
	 * 全小写，getUserName() username
	 */
	LOWER_CASE,
	/**
	 * 全大写，getUserName() USERNAME
	 */
	UPPER_CASE;

	final static char UNDERLINE = '_';
	final static char HYPHEN = '-';

	/**
	 * 根据标准的JavaBean方法名称生成多种键名格式，数组索引对应{@link JSONName}枚举索引。<br>
	 * getUserName() ->
	 * [UserName,UserName,userName,user-name,user_name,username]
	 * 
	 * @param name
	 * @return String[]
	 */
	public static String[] precut(String name) {
		final String[] names = new String[JSONName.values().length];

		// getUser/isUser -> User
		if (name.startsWith("is")) {
			name = name.substring(2);
		} else if (name.startsWith("get")) {
			name = name.substring(3);
		} else if (name.startsWith("set")) {
			name = name.substring(3);
		} else {
			throw new IllegalArgumentException("方法名无效");
		}

		// DEFAULT
		names[DEFAULT.ordinal()] = name;

		if (name.length() > 1) {
			// UPPER_CAMEL_CASE
			// LOWER_CAMEL_CASE
			if (Character.isUpperCase(name.charAt(0))) {
				names[UPPER_CAMEL_CASE.ordinal()] = name;
				names[LOWER_CAMEL_CASE.ordinal()] = Character.toLowerCase(name.charAt(0)) + name.substring(1);
			} else {
				names[UPPER_CAMEL_CASE.ordinal()] = Character.toUpperCase(name.charAt(0)) + name.substring(1);
				names[LOWER_CAMEL_CASE.ordinal()] = name;
			}

			// KEBAB_CASE
			// SNAKE_CASE
			// 注意：特殊格式 getNRIC,getMyNRIC,getMyNRICNumber
			char c = 0;
			final StringBuilder KEBAB = new StringBuilder();
			final StringBuilder SNAKE = new StringBuilder();
			for (int index = 0; index < name.length(); index++) {
				if (index > 0) {
					if (Character.isLowerCase(c) && Character.isUpperCase(name.charAt(index))) {
						KEBAB.append(HYPHEN);
						SNAKE.append(UNDERLINE);
					}
				}
				c = name.charAt(index);
				KEBAB.append(Character.toLowerCase(c));
				SNAKE.append(Character.toLowerCase(c));
			}
			names[KEBAB_CASE.ordinal()] = KEBAB.toString();
			names[SNAKE_CASE.ordinal()] = SNAKE.toString();

			// LOWER_CASE
			names[LOWER_CASE.ordinal()] = name.toLowerCase();
			// UPPER_CASE
			names[UPPER_CASE.ordinal()] = name.toUpperCase();
		} else if (name.length() == 1) {
			// UPPER_CAMEL_CASE
			names[UPPER_CAMEL_CASE.ordinal()] = name.toUpperCase();
			// LOWER_CAMEL_CASE
			names[LOWER_CAMEL_CASE.ordinal()] = name = name.toLowerCase();
			// KEBAB_CASE
			names[KEBAB_CASE.ordinal()] = name;
			// SNAKE_CASE
			names[SNAKE_CASE.ordinal()] = name;
			// LOWER_CASE
			names[LOWER_CASE.ordinal()] = name;
			// UPPER_CASE
			names[UPPER_CASE.ordinal()] = name.toUpperCase();
		} else {
			throw new IllegalArgumentException("方法名称/键名不能为空字符串");
		}
		return names;
	}
}