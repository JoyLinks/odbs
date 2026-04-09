/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.odbs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestODBS {

	static final ODBS odbs = ODBS.initialize("com.joyzl.odbs.test");

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.out.println(odbs.checkString());
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void test() {
		odbs.get(Object.class);
	}

	@Test
	void testNameFormat() {
		String[] names = JSONName.precut("UserName");
		assertEquals(names[JSONName.UPPER_CAMEL_CASE.ordinal()], "UserName");
		assertEquals(names[JSONName.LOWER_CAMEL_CASE.ordinal()], "userName");
		assertEquals(names[JSONName.KEBAB_CASE.ordinal()], "user-name");
		assertEquals(names[JSONName.SNAKE_CASE.ordinal()], "user_name");
		assertEquals(names[JSONName.LOWER_CASE.ordinal()], "username");
		assertEquals(names[JSONName.UPPER_CASE.ordinal()], "USERNAME");

		names = JSONName.precut("NRIC");
		assertEquals(names[JSONName.UPPER_CAMEL_CASE.ordinal()], "NRIC");
		assertEquals(names[JSONName.LOWER_CAMEL_CASE.ordinal()], "nRIC");
		assertEquals(names[JSONName.KEBAB_CASE.ordinal()], "nric");
		assertEquals(names[JSONName.SNAKE_CASE.ordinal()], "nric");
		assertEquals(names[JSONName.LOWER_CASE.ordinal()], "nric");
		assertEquals(names[JSONName.UPPER_CASE.ordinal()], "NRIC");

		names = JSONName.precut("B");
		assertEquals(names[JSONName.UPPER_CAMEL_CASE.ordinal()], "B");
		assertEquals(names[JSONName.LOWER_CAMEL_CASE.ordinal()], "b");
		assertEquals(names[JSONName.KEBAB_CASE.ordinal()], "b");
		assertEquals(names[JSONName.SNAKE_CASE.ordinal()], "b");
		assertEquals(names[JSONName.LOWER_CASE.ordinal()], "b");
		assertEquals(names[JSONName.UPPER_CASE.ordinal()], "B");
	}

	@Test
	void testNumber() throws Exception {
		// 小数点为首的数字（JSON 标准中，小数点前必须有最少一个数字）
		// 小数点后没有数字（JSON 标准中，小数点后必须有最少一个数字）

		assertEquals(0.8, Double.parseDouble(".8"));
		assertEquals(8, Double.parseDouble("8."));
		assertEquals(Double.POSITIVE_INFINITY, Double.parseDouble("Infinity"));
	}
}