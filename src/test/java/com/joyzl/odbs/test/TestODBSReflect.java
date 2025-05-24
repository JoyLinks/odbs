package com.joyzl.odbs.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

	@Test
	void testGeneric() throws Exception {
		// final List<String> list = new ArrayList<>();
		// list.add("TEST");
		//
		// ODBSReflect.findGeneric(list.getClass().getMethod("get", int.class));
		//
		// assertEquals(ODBSReflect.findListGeneric(list.getClass()),
		// String.class);
	}
}
