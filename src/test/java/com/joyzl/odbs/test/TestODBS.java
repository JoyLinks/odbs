/*
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.joyzl.odbs.ODBS;

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
	}

	class EntityExtend extends EntityBase {
		// 用于测试被继承的实体
	}
}
