package com.joyzl.odbs.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.joyzl.odbs.ODBSBinary;

class TestODBSTime extends TestODBS {

	static ODBSBinary BINARY;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		BINARY = new ODBSBinary(odbs);
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void testBinary() throws Exception {
		final ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
		final EntityBase source = EntityBase.createMaxValue();
		final int size = 1000000;

		long time = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			output.reset();
			BINARY.writeEntity(source, output);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("ODBS Binary write " + size + " :" + time + " ms");
		System.out.println("ODBS Binary entity " + output.size() + " byte");

		final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
		final EntityBase target = new EntityBase();
		time = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			input.mark(0);
			BINARY.readEntity(target, input);
			input.reset();
		}
		time = System.currentTimeMillis() - time;
		System.out.println("ODBS Binary read " + size + " :" + time + " ms");
		EntityBase.assertEntity(source, target);

		// ODBS Binary write 1000000 :3282 ms
		// ODBS Binary entity 176 byte
		// ODBS Binary read 1000000 :3275 ms
	}

}