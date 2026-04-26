package com.joyzl.codec;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TestUTF8Writer {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void testLong() throws IOException {
		final int size = 1000000;
		final StringBuilder builder = new StringBuilder(1024);
		final ByteBufferWriter writer = new ByteBufferWriter(1024);
		final long value = System.currentTimeMillis();

		long time = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			builder.setLength(0);
			builder.append(value);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("StringBuilder long:" + time + "ms");

		time = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			writer.reset();
			writer.writeLong(value);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("UTF8Writer long:" + time + "ms");
		writer.close();
	}

	@Test
	void testInt() throws IOException {
		final int size = 1000000;
		final StringBuilder builder = new StringBuilder(1024);
		final ByteBufferWriter writer = new ByteBufferWriter(1024);
		final int value = 10000;

		long time = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			builder.setLength(0);
			builder.append(value);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("StringBuilder int:" + time + "ms");

		time = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			writer.reset();
			writer.writeLong(value);
		}
		time = System.currentTimeMillis() - time;
		System.out.println("UTF8Writer int:" + time + "ms");
		writer.close();
	}
}