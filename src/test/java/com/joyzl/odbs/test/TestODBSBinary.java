/*
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.joyzl.codec.BigEndianInputStream;
import com.joyzl.codec.BigEndianOutputStream;
import com.joyzl.odbs.ODBSBinary;

class TestODBSBinary extends TestODBS {

	static ODBSBinary BINARY;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		BINARY = new ODBSBinary(odbs);
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
	void testNull() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		Exception e = null;
		final EntityBase source = null;
		try {
			BINARY.writeEntity(source, writer);
		} catch (Exception ex) {
			e = ex;
		}
		assertNotNull(e);
		assertTrue(e instanceof NullPointerException);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);

		final EntityBase target = new EntityBase();
		BINARY.readEntity(target, reader);
	}

	@Test
	void testEmpty() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityEmpty source = new EntityEmpty();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("EMPTY:" + input.available());

		final EntityEmpty target = new EntityEmpty();
		BINARY.readEntity(target, reader);
	}

	@Test
	void testNullValue() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityBase source = EntityBase.createNullValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("NULL VALUE:" + input.available());

		final EntityBase target = new EntityBase();
		BINARY.readEntity(target, reader);
		EntityBase.assertEntity(source, target);
	}

	@Test
	void testMinValue() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityBase source = EntityBase.createMinValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("MIN VALUE:" + input.available());

		final EntityBase target = new EntityBase();
		BINARY.readEntity(target, reader);
		EntityBase.assertEntity(source, target);
	}

	@Test
	void testMaxValue() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityBase source = EntityBase.createMaxValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("MAX VALUE:" + input.available());

		EntityBase target = new EntityBase();
		target = (EntityBase) BINARY.readEntity(target, reader);
		EntityBase.assertEntity(source, target);
	}

	@Test
	void testArrayNullValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityArray source = EntityArray.createNullValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("ARRAY NULL VALUES:" + input.available());

		EntityArray target = new EntityArray();
		target = (EntityArray) BINARY.readEntity(target, reader);
		EntityArray.assertEntity(source, target);
	}

	@Test
	void testArrayEmptyValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityArray source = EntityArray.createEmptyValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("ARRAY EMPTY VALUES:" + input.available());

		EntityArray target = new EntityArray();
		target = (EntityArray) BINARY.readEntity(target, reader);
		EntityArray.assertEntity(source, target);
	}

	@Test
	void testArrayNormalValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityArray source = EntityArray.createNormalValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("ARRAY NORMAL VALUES:" + input.available());

		EntityArray target = new EntityArray();
		target = (EntityArray) BINARY.readEntity(target, reader);
		EntityArray.assertEntity(source, target);
	}

	@Test
	void testSetNullValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntitySet source = EntitySet.createNullValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("SET NULL VALUES:" + input.available());

		EntitySet target = new EntitySet();
		target = (EntitySet) BINARY.readEntity(target, reader);
		EntitySet.assertEntity(source, target);
	}

	@Test
	void testSetEmptyValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntitySet source = EntitySet.createEmptyValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("SET EMPTY VALUES:" + input.available());

		EntitySet target = new EntitySet();
		target = (EntitySet) BINARY.readEntity(target, reader);
		EntitySet.assertEntity(source, target);
	}

	@Test
	void testSetNormalValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntitySet source = EntitySet.createNormalValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("SET NORMAL VALUES:" + input.available());

		EntitySet target = new EntitySet();
		target = (EntitySet) BINARY.readEntity(target, reader);
		EntitySet.assertEntity(source, target);
	}

	@Test
	void testListNullValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityList source = EntityList.createNullValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("LIST NULL VALUES:" + input.available());

		EntityList target = new EntityList();
		target = (EntityList) BINARY.readEntity(target, reader);
		EntityList.assertEntity(source, target);
	}

	@Test
	void testListEmptyValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityList source = EntityList.createEmptyValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("LIST EMPTY VALUES:" + input.available());

		EntityList target = new EntityList();
		target = (EntityList) BINARY.readEntity(target, reader);
		EntityList.assertEntity(source, target);
	}

	@Test
	void testListNormalValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityList source = EntityList.createNormalValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("LIST NORMAL VALUES:" + input.available());

		EntityList target = new EntityList();
		target = (EntityList) BINARY.readEntity(target, reader);
		EntityList.assertEntity(source, target);
	}

	@Test
	void testMapNullValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityMap source = EntityMap.createNullValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("MAP NULL VALUES:" + input.available());

		EntityMap target = new EntityMap();
		target = (EntityMap) BINARY.readEntity(target, reader);
		EntityMap.assertEntity(source, target);
	}

	@Test
	void testMapEmptyValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityMap source = EntityMap.createEmptyValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("MAP EMPTY VALUES:" + input.available());

		EntityMap target = new EntityMap();
		target = (EntityMap) BINARY.readEntity(target, reader);
		EntityMap.assertEntity(source, target);
	}

	@Test
	void testMapNormalValues() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		final EntityMap source = EntityMap.createNormalValue();
		BINARY.writeEntity(source, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);
		System.out.println("MAP NORMAL VALUES:" + input.available());

		EntityMap target = new EntityMap();
		target = (EntityMap) BINARY.readEntity(target, reader);
		EntityMap.assertEntity(source, target);
	}

	@Test
	void testExtend() throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final BigEndianOutputStream writer = new BigEndianOutputStream(output);

		EntityExtend entity = new EntityExtend();
		BINARY.writeEntity(entity, writer);

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final BigEndianInputStream reader = new BigEndianInputStream(input);

		BINARY.readEntity(entity, reader);

		// EXCEPTION
		// BINARY.writeEntity("String", writer);
	}
}
