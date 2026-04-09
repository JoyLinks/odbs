/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.odbs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.joyzl.odbs.ODBSReflect;

class TestODBSReflect {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testScan() {
		List<String> items;
		items = ODBSReflect.scanModule("com.joyzl.odbs");
		assertTrue(items.size() > 0);
		items = ODBSReflect.scanPackage("com.joyzl");
		assertTrue(items.size() > 0);

		items = ODBSReflect.scan("com.joyzl.odbs");
		assertTrue(items.size() > 0);
		items = ODBSReflect.scan("com.joyzl");
		assertTrue(items.size() > 0);

		items = ODBSReflect.scanModule("com.joyzl.odbs1");
		assertTrue(items == null);
		items = ODBSReflect.scanPackage("com.joyzl1");
		assertTrue(items.size() == 0);
	}

	@Test
	void testScanClass() {
		List<Class<?>> items;
		items = ODBSReflect.scanClass("com.joyzl.odbs");
		assertTrue(items.size() > 0);
		items = ODBSReflect.scanClass("com.joyzl");
		assertTrue(items.size() > 0);
	}

	// @Test
	void testMethodHandle() throws Throwable {
		// 可变参数暂无法支持
		Method method = EntityVarArgs.class.getMethod("setEntityBases", EntityBase[].class);
		method.setAccessible(true);
		MethodHandle handler = MethodHandles.publicLookup().unreflect(method);
		assertEquals(1, method.getParameterCount());

		handler = handler.asType(MethodType.methodType(void.class, Object.class, method.getParameterTypes()[0]));

		final Object entity = new EntityVarArgs();
		final Object value = new EntityBase[0];
		handler.invokeExact(entity, (Object[]) null);
		handler.invokeExact(entity, value);
	}
}