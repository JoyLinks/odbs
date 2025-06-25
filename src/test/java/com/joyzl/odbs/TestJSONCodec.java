package com.joyzl.odbs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

class TestJSONCodec {

	final ODBS odbs = ODBS.initialize(new String[0]);
	final String array = """
			//空数组
			[]
			//单值数组
			[1]
			//多值数组
			[1,2]
			//内部有注释的数组
			[1, 2,
			// 单行注释
			"3" ,
			/*
			多行注释
			*/
			4,
			// 字符串续行
			"A\\
			B",
			]
			[["a1","b1"],["a2","b2"]]
			""";
	final String object = """
			//空对象
			{}
			//单值对象
			{k:v}
			{ key : value }
			{ "key" : "value" , }
			//内有注释的对象
			{
			// 单行注释
			key
			// 单行注释
			:
			/*
			多行注释
			*/
			value
			}
			{key:{k1:v1}}
			""";

	@Test
	void testReadArray() throws Exception {
		final StringReader reader = new StringReader(array);
		final JSONCodec codec = JSONCodec.instence(new ODBSJson(odbs), reader);

		// 读取规则每次读值应判断结束标志
		// 读取器不能过滤尾随逗号，应由读取者判断

		// []
		assertEquals('[', codec.readSkip());
		assertFalse(codec.readValue());
		assertEquals(']', codec.lastChar());

		// [1]
		assertEquals('[', codec.readSkip());
		assertTrue(codec.readValue());
		assertEquals("1", codec.getString());
		assertEquals(']', codec.lastChar());

		// [1,2]
		assertEquals('[', codec.readSkip());
		assertTrue(codec.readValue());
		assertEquals("1", codec.getString());
		assertEquals(',', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("2", codec.getString());
		assertEquals(']', codec.lastChar());

		// [内有注释]
		assertEquals('[', codec.readSkip());
		assertTrue(codec.readValue());
		assertEquals("1", codec.getString());
		assertEquals(',', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("2", codec.getString());
		assertEquals(',', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("3", codec.getString());
		assertEquals(',', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("4", codec.getString());
		assertEquals(',', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("AB", codec.getString());
		assertEquals(',', codec.lastChar());
		assertFalse(codec.readValue());
		assertEquals(']', codec.lastChar());

		// [[...],[...]]
		assertEquals('[', codec.readSkip());
		assertFalse(codec.readValue());
		assertEquals('[', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("a1", codec.getString());
		assertEquals(',', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("b1", codec.getString());
		assertEquals(']', codec.lastChar());

		assertEquals(',', codec.readSkip());

		assertEquals('[', codec.readSkip());
		assertTrue(codec.readValue());
		assertEquals("a2", codec.getString());
		assertEquals(',', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("b2", codec.getString());
		assertEquals(']', codec.lastChar());

		assertEquals(']', codec.readSkip());

		// END
		assertEquals('\n', reader.read());
	}

	@Test
	void testReadObject() throws Exception {
		final StringReader reader = new StringReader(object);
		final JSONCodec codec = JSONCodec.instence(new ODBSJson(odbs), reader);

		// 读取规则每次读键值应判断结束标志
		// 读取器不能过滤尾随逗号，应由读取者判断

		// {}
		assertEquals('{', codec.readSkip());
		assertFalse(codec.readKey());
		assertEquals('}', codec.lastChar());

		// {k:v}
		assertEquals('{', codec.readSkip());
		assertTrue(codec.readKey());
		assertEquals("k", codec.getString());
		assertEquals(':', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("v", codec.getString());
		assertEquals('}', codec.lastChar());

		// { key : value }
		assertEquals('{', codec.readSkip());
		assertTrue(codec.readKey());
		assertEquals("key", codec.getString());
		assertEquals(':', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("value", codec.getString());
		assertEquals('}', codec.lastChar());

		// { "key" : "value" , }
		assertEquals('{', codec.readSkip());
		assertTrue(codec.readKey());
		assertEquals("key", codec.getString());
		assertEquals(':', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("value", codec.getString());
		assertEquals(',', codec.lastChar());
		assertFalse(codec.readValue());
		assertEquals('}', codec.lastChar());

		// {内有注释}
		assertEquals('{', codec.readSkip());
		assertTrue(codec.readKey());
		assertEquals("key", codec.getString());
		assertEquals(':', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("value", codec.getString());
		assertEquals('}', codec.lastChar());

		// {key:{k1:v1}}
		assertEquals('{', codec.readSkip());
		assertTrue(codec.readKey());
		assertEquals("key", codec.getString());
		assertEquals(':', codec.lastChar());

		assertEquals('{', codec.readSkip());
		assertTrue(codec.readKey());
		assertEquals("k1", codec.getString());
		assertEquals(':', codec.lastChar());
		assertTrue(codec.readValue());
		assertEquals("v1", codec.getString());
		assertEquals('}', codec.lastChar());

		assertEquals('}', codec.readSkip());

		// END
		assertEquals('\n', reader.read());
	}

	@Test
	void testIgnoreArray() throws Exception {
		final StringReader reader = new StringReader(array);
		final JSONCodec codec = JSONCodec.instence(new ODBSJson(odbs), reader);

		// []
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// [1]
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// [1,2]
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// [内有注释]
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// 多维数组
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// END
		assertEquals('\n', reader.read());
	}

	@Test
	void testIgnoreObject() throws Exception {
		final StringReader reader = new StringReader(object);
		final JSONCodec codec = JSONCodec.instence(new ODBSJson(odbs), reader);

		// {}
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// {k:v}
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// { key : value }
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// { "key" : "value" , }
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// {内有注释}
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// {key:{k1:v1}}
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// END
		assertEquals('\n', reader.read());
	}

	@Test
	void testIgnoreArrayValue() throws Exception {
		final StringReader reader = new StringReader(array);
		final JSONCodec codec = JSONCodec.instence(new ODBSJson(odbs), reader);

		// []
		assertEquals('[', codec.readSkip());
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// [1]
		assertEquals('[', codec.readSkip());
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// [1,2]
		assertEquals('[', codec.readSkip());
		codec.readIgnore();
		assertEquals(',', codec.lastChar());
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// [内有注释]
		assertEquals('[', codec.readSkip());
		codec.readIgnore();
		codec.readIgnore();
		codec.readIgnore();
		codec.readIgnore();
		codec.readIgnore();
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// 多维数组 [[],[]]
		assertEquals('[', codec.readSkip());
		codec.readIgnore();
		codec.readIgnore();
		codec.readIgnore();
		codec.readIgnore();
		assertEquals(']', codec.lastChar());

		// END
		assertEquals('\n', reader.read());
	}

	@Test
	void testIgnoreObjectValue() throws Exception {
		final StringReader reader = new StringReader(object);
		final JSONCodec codec = JSONCodec.instence(new ODBSJson(odbs), reader);

		// {}
		assertEquals('{', codec.readSkip());
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// {k:v}
		assertEquals('{', codec.readSkip());
		codec.readIgnore();
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// { key : value }
		assertEquals('{', codec.readSkip());
		codec.readIgnore();
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// { "key" : "value" , }
		assertEquals('{', codec.readSkip());
		codec.readIgnore();
		codec.readIgnore();
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// {内有注释}
		assertEquals('{', codec.readSkip());
		codec.readIgnore();
		codec.readIgnore();
		assertEquals('}', codec.lastChar());

		// {key:{k1:v1}}
		assertEquals('{', codec.readSkip());
		codec.readIgnore();
		codec.readIgnore();
		assertEquals('}', codec.readSkip());

		// END
		assertEquals('\n', reader.read());
	}

	@Test
	void testNumber() throws Exception {
		final ODBSJson json = new ODBSJson(odbs);

		// 小数点为首的数字（JSON 标准中，小数点前必须有最少一个数字）
		// 小数点后没有数字（JSON 标准中，小数点后必须有最少一个数字）

		assertEquals(0.8, json.getNumberFormat().parse(".8"));
		assertEquals(8L, json.getNumberFormat().parse("8."));

		// 十六进制 0X 0x
		// 二进制 0B 0b

		Integer.parseInt("A", 16);

	}
}