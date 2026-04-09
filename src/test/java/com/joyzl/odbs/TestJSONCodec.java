package com.joyzl.odbs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

class TestJSONCodec {

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
			{key:{k1:v1},kby:{k2:v2}}
			""";

	@Test
	void testReadArray() throws Exception {
		final StringReader reader = new StringReader(array);
		final JSONReader codec = JSONReader.instance(reader);

		// 读取规则每次读值应判断结束标志
		// 读取器不能过滤尾随逗号，应由读取者判断

		// []
		codec.beginArray();
		assertFalse(codec.readNext());

		// [1]
		codec.beginArray();
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("1", codec.getString());
		assertFalse(codec.readNext());

		// [1,2]
		codec.beginArray();
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("1", codec.getString());
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("2", codec.getString());
		assertFalse(codec.readNext());

		// [内有注释]
		codec.beginArray();
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("1", codec.getString());
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("2", codec.getString());
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("3", codec.getString());
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("4", codec.getString());
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("AB", codec.getString());
		assertFalse(codec.readNext());

		// [[...],[...]]
		codec.beginArray();
		assertTrue(codec.readNext());

		codec.beginArray();
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("a1", codec.getString());
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("b1", codec.getString());
		assertFalse(codec.readNext());

		assertTrue(codec.readNext());

		codec.beginArray();
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("a2", codec.getString());
		assertTrue(codec.readNext());
		assertTrue(codec.readValue());
		assertEquals("b2", codec.getString());
		assertFalse(codec.readNext());

		assertFalse(codec.readNext());

		// END
		assertEquals('\n', reader.read());
	}

	@Test
	void testReadObject() throws Exception {
		final StringReader reader = new StringReader(object);
		final JSONReader codec = JSONReader.instance(reader);

		// 读取规则每次读键值应判断结束标志
		// 读取器不能过滤尾随逗号，应由读取者判断

		// {}
		codec.beginObject();
		assertFalse(codec.readKey());

		// {k:v}
		codec.beginObject();
		assertTrue(codec.readKey());
		assertEquals("k", codec.getString());
		assertTrue(codec.readValue());
		assertEquals("v", codec.getString());
		assertFalse(codec.readKey());

		// { key : value }
		codec.beginObject();
		assertTrue(codec.readKey());
		assertEquals("key", codec.getString());
		assertTrue(codec.readValue());
		assertEquals("value", codec.getString());
		assertFalse(codec.readKey());

		// { "key" : "value" , }
		codec.beginObject();
		assertTrue(codec.readKey());
		assertEquals("key", codec.getString());
		assertTrue(codec.readValue());
		assertEquals("value", codec.getString());
		assertFalse(codec.readKey());

		// {内有注释}
		codec.beginObject();
		assertTrue(codec.readKey());
		assertEquals("key", codec.getString());
		assertTrue(codec.readValue());
		assertEquals("value", codec.getString());
		assertFalse(codec.readKey());

		// {key:{k1:v1},kby:{k2:v2}}
		codec.beginObject();
		assertTrue(codec.readKey());
		assertEquals("key", codec.getString());

		codec.beginObject();
		assertTrue(codec.readKey());
		assertEquals("k1", codec.getString());
		assertTrue(codec.readValue());
		assertEquals("v1", codec.getString());
		assertFalse(codec.readKey());

		assertTrue(codec.readKey());
		assertEquals("kby", codec.getString());

		codec.beginObject();
		assertTrue(codec.readKey());
		assertEquals("k2", codec.getString());
		assertTrue(codec.readValue());
		assertEquals("v2", codec.getString());
		assertFalse(codec.readKey());

		assertFalse(codec.readKey());

		// END
		assertEquals('\n', reader.read());
	}

	@Test
	void testIgnoreArray() throws Exception {
		final StringReader reader = new StringReader(array);
		final JSONReader codec = JSONReader.instance(reader);

		// []
		codec.beginArray();
		assertFalse(codec.readNext());

		// [1]
		codec.beginArray();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertFalse(codec.readNext());

		// [1,2]
		codec.beginArray();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertFalse(codec.readNext());

		// [内有注释]
		codec.beginArray();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertFalse(codec.readNext());

		// 多维数组
		codec.beginArray();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertTrue(codec.readNext());
		codec.readIgnore();
		assertFalse(codec.readNext());

		// END
		assertEquals('\n', reader.read());
	}

	@Test
	void testIgnoreObjectValue() throws Exception {
		final StringReader reader = new StringReader(object);
		final JSONReader codec = JSONReader.instance(reader);

		// {}
		codec.beginObject();
		assertFalse(codec.readKey());

		// {k:v}
		codec.beginObject();
		codec.readKey();
		codec.readIgnore();
		assertFalse(codec.readKey());

		// { key : value }
		codec.beginObject();
		codec.readKey();
		codec.readIgnore();
		assertFalse(codec.readKey());

		// { "key" : "value" , }
		codec.beginObject();
		codec.readKey();
		codec.readIgnore();
		assertFalse(codec.readKey());

		// {内有注释}
		codec.beginObject();
		codec.readKey();
		codec.readIgnore();
		assertFalse(codec.readKey());

		// {key:{k1:v1},kby:{k2:v2}}
		codec.beginObject();
		codec.readKey();
		codec.readIgnore();
		codec.readKey();
		codec.readIgnore();
		assertFalse(codec.readKey());

		// END
		assertEquals('\n', reader.read());
	}

}