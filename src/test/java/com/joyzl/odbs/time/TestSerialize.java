package com.joyzl.odbs.time;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.joyzl.codec.ByteBufferInputStream;
import com.joyzl.codec.ByteBufferOutputStream;
import com.joyzl.codec.DataInput;
import com.joyzl.codec.DataOutput;
import com.joyzl.odbs.ODBS;
import com.joyzl.odbs.ODBSBinary;
import com.joyzl.odbs.ODBSJson;

/**
 * 序列化性能测试
 * 
 * @author ZhangXi 2026年4月10日
 */
public class TestSerialize {

	public static void main(String[] args) throws Exception {
		final TestSerialize test = new TestSerialize();
		final Blackhole b = new Blackhole();
		final Blackhole j = new Blackhole();
		test.setup();

		// 预热
		for (int i = 0; i < 100000; i++) {
			test.serializeBinary(b);
		}
		for (int i = 0; i < 100000; i++) {
			test.deserializeBinary(b);
		}
		b.reset();
		for (int i = 0; i < 100000; i++) {
			test.serializeJson(j);
		}
		for (int i = 0; i < 100000; i++) {
			test.deserializeJson(j);
		}
		j.reset();

		// 正式测试
		long start, duration;

		// BINARY
		start = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			test.serializeBinary(b);
		}
		duration = System.nanoTime() - start;
		System.out.println("BYTE序列化: " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms");

		start = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			test.deserializeBinary(b);
		}
		duration = System.nanoTime() - start;
		System.out.println("BYTE反序列: " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms");
		System.out.println(b);

		// JSON
		start = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			test.serializeJson(j);
		}
		duration = System.nanoTime() - start;
		System.out.println("JSON序列化: " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms");

		start = System.nanoTime();
		for (int i = 0; i < 1000000; i++) {
			test.deserializeJson(j);
		}
		duration = System.nanoTime() - start;
		System.out.println("JSON反序列: " + TimeUnit.NANOSECONDS.toMillis(duration) + " ms");
		System.out.println(j);
	}

	private ODBSJson json;
	private ODBSBinary binary;

	private ByteBufferOutputStream outputBinary, outputJson;
	private ByteBufferInputStream inputBinary, inputJson;

	private Writer writer;
	private Reader reader;

	private TestEntity entity;

	public void setup() throws IOException {
		final ODBS odbs = ODBS.initialize("com.joyzl.odbs.jmh");
		binary = new ODBSBinary(odbs);
		json = new ODBSJson(odbs);

		entity = new TestEntity();
		entity.setId(System.currentTimeMillis());
		entity.setGender(true);
		entity.setName("JOYZL");
		entity.setType("JOYZL ODBS Binary");
		entity.setRemark("JOYZL ODBS Binary JMH 性能测试");
		entity.setState(TestEnum.VALID);
		entity.setCreated(LocalDateTime.now());
		entity.setUpdated(LocalDateTime.now());
		entity.setTimestamp(System.currentTimeMillis());
		entity.getAddress().add("重庆市渝北区回兴街道石盘河社区");
		entity.getAddress().add("重庆市渝中区大坪时代天街");
		entity.getEmail().add("simon.zhang@msn.com");
		entity.getEmail().add("931661600@qq.com");

		outputBinary = new ByteBufferOutputStream(1024);
		binary.writeEntity(entity, (DataOutput) outputBinary);
		inputBinary = new ByteBufferInputStream(outputBinary.buffer().flip());
		outputBinary = new ByteBufferOutputStream(1024);

		outputJson = new ByteBufferOutputStream(1024);
		writer = new OutputStreamWriter(outputJson, StandardCharsets.UTF_8);
		json.writeEntity(entity, writer);
		writer.flush();
		inputJson = new ByteBufferInputStream(outputJson.buffer().flip());
		reader = new InputStreamReader(inputJson, StandardCharsets.UTF_8);
		outputJson = new ByteBufferOutputStream(65535);
		writer = new OutputStreamWriter(outputJson, StandardCharsets.UTF_8);
	}

	public void serializeBinary(Blackhole bh) throws IOException {
		outputBinary.reset();
		binary.writeEntity(entity, (DataOutput) outputBinary);
		bh.consume(outputBinary.size());
	}

	public void deserializeBinary(Blackhole bh) throws IOException {
		inputBinary.reset();
		entity = binary.readEntity((DataInput) inputBinary);
		bh.consume(entity);
	}

	public void serializeJson(Blackhole bh) throws IOException {
		outputJson.reset();
		json.writeEntity(entity, writer);
		bh.consume(outputJson.size());
	}

	public void deserializeJson(Blackhole bh) throws IOException {
		inputJson.reset();
		entity = json.readEntity(TestEntity.class, reader);
		bh.consume(entity);
	}

	static class Blackhole {
		private int byteSize;
		private int objectSize;

		public void consume(int bytes) {
			byteSize += bytes;
		}

		public void consume(Object entity) {
			if (entity != null) {
				objectSize++;
			}
		}

		public void reset() {
			byteSize = 0;
			objectSize = 0;
		}

		@Override
		public String toString() {
			return "BYTE:" + byteSize + ",OBJ:" + objectSize;
		}
	}
}