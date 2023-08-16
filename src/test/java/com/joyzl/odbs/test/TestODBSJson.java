/*
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.joyzl.odbs.JSONName;
import com.joyzl.odbs.ODBSJson;

class TestODBSJson extends TestODBS {

	static ODBSJson JSON;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		JSON = new ODBSJson(odbs);
		JSON.setIgnoreNull(true);
		// JSON.setIgnoreNull(false);
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

	void print(InputStream input) throws IOException {
		final Reader out = new InputStreamReader(input, "UTF-8");
		while (out.ready()) {
			System.out.print((char) out.read());
		}
		System.out.println();
	}

	@Test
	void testNameFormat() {
		String[] names = JSONName.precut("getUserName");
		assertEquals(names[JSONName.DEFAULT.ordinal()], "UserName");
		assertEquals(names[JSONName.UPPER_CAMEL_CASE.ordinal()], "UserName");
		assertEquals(names[JSONName.LOWER_CAMEL_CASE.ordinal()], "userName");
		assertEquals(names[JSONName.KEBAB_CASE.ordinal()], "user-name");
		assertEquals(names[JSONName.SNAKE_CASE.ordinal()], "user_name");
		assertEquals(names[JSONName.LOWER_CASE.ordinal()], "username");
		assertEquals(names[JSONName.UPPER_CASE.ordinal()], "USERNAME");

		names = JSONName.precut("getNRIC");
		assertEquals(names[JSONName.DEFAULT.ordinal()], "NRIC");
		assertEquals(names[JSONName.UPPER_CAMEL_CASE.ordinal()], "NRIC");
		assertEquals(names[JSONName.LOWER_CAMEL_CASE.ordinal()], "nRIC");
		assertEquals(names[JSONName.KEBAB_CASE.ordinal()], "nric");
		assertEquals(names[JSONName.SNAKE_CASE.ordinal()], "nric");
		assertEquals(names[JSONName.LOWER_CASE.ordinal()], "nric");
		assertEquals(names[JSONName.UPPER_CASE.ordinal()], "NRIC");

		names = JSONName.precut("isB");
		assertEquals(names[JSONName.DEFAULT.ordinal()], "B");
		assertEquals(names[JSONName.UPPER_CAMEL_CASE.ordinal()], "B");
		assertEquals(names[JSONName.LOWER_CAMEL_CASE.ordinal()], "b");
		assertEquals(names[JSONName.KEBAB_CASE.ordinal()], "b");
		assertEquals(names[JSONName.SNAKE_CASE.ordinal()], "b");
		assertEquals(names[JSONName.LOWER_CASE.ordinal()], "b");
		assertEquals(names[JSONName.UPPER_CASE.ordinal()], "B");
	}

	@Test
	void testHuman() {
		// https://spec.json5.org/

		// 根节点可以是任意类型。（JSON 原始标准 [4] 只容许 object 和 array 作为根节点，[1] 已放宽）
		// 单行、多行注释
		// 无引号的键（那么是否容许空格、冒号、转义符）
		// 最后可加逗号
		// 字符串（JSON 标准只能用双引号）
		// 小数点为首的数字（JSON 标准中，小数点前必须有最少一个数字）
		// 小数点后没有数字（JSON 标准中，小数点后必须有最少一个数字）
		// 十六进制 0X 0x
		// 二进制 0B 0b
		// 八进制 0
		// 无穷 Infinity/-Infinity
		// NaN
	}

	@Test
	void testNull() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		EntityBase source = null;
		JSON.writeEntity(source, writer);

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityBase target = null;
		target = (EntityBase) JSON.readEntity(EntityBase.class, reader);
		Assertions.assertNull(target);
	}

	@Test
	void testEmpty() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		// 空集合[]
		JSON.writeEntity(Collections.emptyList(), writer);
		// 空对象{}
		JSON.writeEntity(new EntityEmpty(), writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		Object result;
		// 读空流
		result = JSON.readEntity(EntityBase.class, Reader.nullReader());
		Assertions.assertNull(result);
		// 空集合[]
		result = JSON.readEntity(EntityEmpty.class, reader);
		Assertions.assertInstanceOf(Collection.class, result);
		// 空对象{}
		result = JSON.readEntity(EntityEmpty.class, reader);
		Assertions.assertInstanceOf(EntityEmpty.class, result);
	}

	@Test
	void testNullValue() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityBase source = EntityBase.createNullValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		final EntityBase target = new EntityBase();
		JSON.readEntity(target, reader);
		EntityBase.assertEntity(source, target);
	}

	@Test
	void testMinValue() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityBase source = EntityBase.createMinValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		final EntityBase target = new EntityBase();
		JSON.readEntity(target, reader);
		EntityBase.assertEntity(source, target);
	}

	@Test
	void testMaxValue() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityBase source = EntityBase.createMaxValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		final EntityBase target = new EntityBase();
		JSON.readEntity(target, reader);
		EntityBase.assertEntity(source, target);
	}

	@Test
	void testArray() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final Collection<EntityBase> sources = new ArrayList<>();
		sources.add(EntityBase.createNullValue());
		sources.add(EntityBase.createMinValue());
		sources.add(EntityBase.createMaxValue());
		JSON.writeEntity(sources, writer);
		writer.flush();

		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		Object targets = JSON.readEntity(EntityBase.class, reader);
		assertInstanceOf(Collection.class, targets);
		Assertions.assertEquals(sources, targets);
	}

	@Test
	void testArrayNullValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityArray source = EntityArray.createNullValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityArray target = new EntityArray();
		target = (EntityArray) JSON.readEntity(target, reader);
		EntityArray.assertEntity(source, target);
	}

	@Test
	void testArrayEmptyValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityArray source = EntityArray.createEmptyValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityArray target = new EntityArray();
		target = (EntityArray) JSON.readEntity(target, reader);
		EntityArray.assertEntity(source, target);
	}

	@Test
	void testArrayNormalValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityArray source = EntityArray.createNormalValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityArray target = new EntityArray();
		target = (EntityArray) JSON.readEntity(target, reader);
		EntityArray.assertEntity(source, target);
	}

	@Test
	void testSet() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final Collection<EntitySet> sources = new ArrayList<>();
		sources.add(EntitySet.createNullValue());
		sources.add(EntitySet.createEmptyValue());
		sources.add(EntitySet.createNormalValue());
		JSON.writeEntity(sources, writer);
		writer.flush();

		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		Object targets = JSON.readEntity(EntitySet.class, reader);
		assertInstanceOf(Collection.class, targets);
		// assertEquals(sources, targets);
	}

	@Test
	void testSetNullValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntitySet source = EntitySet.createNullValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntitySet target = new EntitySet();
		target = (EntitySet) JSON.readEntity(target, reader);
		EntitySet.assertEntity(source, target);
	}

	@Test
	void testSetEmptyValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntitySet source = EntitySet.createEmptyValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntitySet target = new EntitySet();
		target = (EntitySet) JSON.readEntity(target, reader);
		EntitySet.assertEntity(source, target);
	}

	@Test
	void testSetNormalValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntitySet source = EntitySet.createNormalValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntitySet target = new EntitySet();
		target = (EntitySet) JSON.readEntity(target, reader);
		EntitySet.assertEntity(source, target);
	}

	@Test
	void testList() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final Collection<EntityList> sources = new ArrayList<>();
		sources.add(EntityList.createNullValue());
		sources.add(EntityList.createEmptyValue());
		sources.add(EntityList.createNormalValue());
		JSON.writeEntity(sources, writer);
		writer.flush();

		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		Object targets = JSON.readEntity(EntityList.class, reader);
		assertInstanceOf(Collection.class, targets);
		// assertEquals(sources, targets);
	}

	@Test
	void testListNullValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityList source = EntityList.createNullValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityList target = new EntityList();
		target = (EntityList) JSON.readEntity(target, reader);
		EntityList.assertEntity(source, target);
	}

	@Test
	void testListEmptyValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityList source = EntityList.createEmptyValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityList target = new EntityList();
		target = (EntityList) JSON.readEntity(target, reader);
		EntityList.assertEntity(source, target);
	}

	@Test
	void testListNormalValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityList source = EntityList.createNormalValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityList target = new EntityList();
		target = (EntityList) JSON.readEntity(target, reader);
		EntityList.assertEntity(source, target);
	}

	@Test
	void testMap() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final Collection<EntityMap> sources = new ArrayList<>();
		sources.add(EntityMap.createNullValue());
		sources.add(EntityMap.createEmptyValue());
		sources.add(EntityMap.createNormalValue());
		JSON.writeEntity(sources, writer);
		writer.flush();

		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		Object targets = JSON.readEntity(EntityMap.class, reader);
		assertInstanceOf(Collection.class, targets);
		// assertEquals(sources, targets);
	}

	@Test
	void testMapNullValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityMap source = EntityMap.createNullValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityMap target = new EntityMap();
		target = (EntityMap) JSON.readEntity(target, reader);
		EntityMap.assertEntity(source, target);
	}

	@Test
	void testMapEmptyValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityMap source = EntityMap.createEmptyValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityMap target = new EntityMap();
		target = (EntityMap) JSON.readEntity(target, reader);
		EntityMap.assertEntity(source, target);
	}

	@Test
	void testMapNormalValues() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityMap source = EntityMap.createNormalValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		print(new ByteArrayInputStream(output.toByteArray()));
		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityMap target = new EntityMap();
		target = (EntityMap) JSON.readEntity(target, reader);
		EntityMap.assertEntity(source, target);
	}

	@Test
	void testBaseIgnores() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityBase source = EntityBase.createMinValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		// {"Username":"13883062895","Password":"200ceb26807d6bf99fd6f4f0d1ca54d4","Employee":"238253448244322"}

		EntityEmpty target = new EntityEmpty();
		target = (EntityEmpty) JSON.readEntity(target, reader);
		assertNotNull(target);
	}

	@Test
	void testArrayIgnores() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityArray source = EntityArray.createNormalValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityEmpty target = new EntityEmpty();
		target = (EntityEmpty) JSON.readEntity(target, reader);
		assertNotNull(target);
	}

	@Test
	void testSetIgnores() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntitySet source = EntitySet.createNormalValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityEmpty target = new EntityEmpty();
		target = (EntityEmpty) JSON.readEntity(target, reader);
		assertNotNull(target);
	}

	@Test
	void testListIgnores() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityList source = EntityList.createNormalValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityEmpty target = new EntityEmpty();
		target = (EntityEmpty) JSON.readEntity(target, reader);
		assertNotNull(target);
	}

	@Test
	void testMapIgnores() throws IOException, ParseException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final Writer writer = new OutputStreamWriter(output, "UTF-8");

		final EntityMap source = EntityMap.createNormalValue();
		JSON.writeEntity(source, writer);
		writer.flush();

		final InputStream input = new ByteArrayInputStream(output.toByteArray());
		final Reader reader = new InputStreamReader(input, "UTF-8");

		EntityEmpty target = new EntityEmpty();
		target = (EntityEmpty) JSON.readEntity(target, reader);
		assertNotNull(target);
	}
}
