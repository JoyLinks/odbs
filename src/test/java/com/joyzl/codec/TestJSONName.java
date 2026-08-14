package com.joyzl.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.joyzl.odbs.JSONName;

class TestJSONName {

	@Test
	void test1() {
		final String[] names = JSONName.precut("DevID");
		assertEquals("DevID", names[JSONName.UPPER_CAMEL_CASE.ordinal()]);
		assertEquals("devID", names[JSONName.LOWER_CAMEL_CASE.ordinal()]);
		assertEquals("dev-id", names[JSONName.KEBAB_CASE.ordinal()]);
		assertEquals("dev_id", names[JSONName.SNAKE_CASE.ordinal()]);
		assertEquals("devid", names[JSONName.LOWER_CASE.ordinal()]);
		assertEquals("DEVID", names[JSONName.UPPER_CASE.ordinal()]);

		assertTrue(JSONName.match(names, JSONName.SNAKE_CASE, "dev-ID"));
		assertTrue(JSONName.match(names, JSONName.SNAKE_CASE, "dev_ID"));
	}

	@Test
	void test2() {
		final String[] names = JSONName.precut("PartIDLst");
		assertEquals("PartIDLst", names[JSONName.UPPER_CAMEL_CASE.ordinal()]);
		assertEquals("partIDLst", names[JSONName.LOWER_CAMEL_CASE.ordinal()]);
		assertEquals("part-id-lst", names[JSONName.KEBAB_CASE.ordinal()]);
		assertEquals("part_id_lst", names[JSONName.SNAKE_CASE.ordinal()]);
		assertEquals("partidlst", names[JSONName.LOWER_CASE.ordinal()]);
		assertEquals("PARTIDLST", names[JSONName.UPPER_CASE.ordinal()]);

		assertTrue(JSONName.match(names, JSONName.UPPER_CASE, "part-ID-lst"));
	}

	@Test
	void test3() {
		final String[] names = JSONName.precut("NRIC");
		assertEquals("NRIC", names[JSONName.UPPER_CAMEL_CASE.ordinal()]);
		assertEquals("nRIC", names[JSONName.LOWER_CAMEL_CASE.ordinal()]);
		assertEquals("nric", names[JSONName.KEBAB_CASE.ordinal()]);
		assertEquals("nric", names[JSONName.SNAKE_CASE.ordinal()]);
		assertEquals("nric", names[JSONName.LOWER_CASE.ordinal()]);
		assertEquals("NRIC", names[JSONName.UPPER_CASE.ordinal()]);
	}

	@Test
	void test4() {
		final String[] names = JSONName.precut("MyNRIC");
		assertEquals("MyNRIC", names[JSONName.UPPER_CAMEL_CASE.ordinal()]);
		assertEquals("myNRIC", names[JSONName.LOWER_CAMEL_CASE.ordinal()]);
		assertEquals("my-nric", names[JSONName.KEBAB_CASE.ordinal()]);
		assertEquals("my_nric", names[JSONName.SNAKE_CASE.ordinal()]);
		assertEquals("mynric", names[JSONName.LOWER_CASE.ordinal()]);
		assertEquals("MYNRIC", names[JSONName.UPPER_CASE.ordinal()]);
	}

	@Test
	void test5() {
		final String[] names = JSONName.precut("MyNRICNumber");
		assertEquals("MyNRICNumber", names[JSONName.UPPER_CAMEL_CASE.ordinal()]);
		assertEquals("myNRICNumber", names[JSONName.LOWER_CAMEL_CASE.ordinal()]);
		assertEquals("my-nric-number", names[JSONName.KEBAB_CASE.ordinal()]);
		assertEquals("my_nric_number", names[JSONName.SNAKE_CASE.ordinal()]);
		assertEquals("mynricnumber", names[JSONName.LOWER_CASE.ordinal()]);
		assertEquals("MYNRICNUMBER", names[JSONName.UPPER_CASE.ordinal()]);
	}
}