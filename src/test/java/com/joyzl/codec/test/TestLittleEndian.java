package com.joyzl.codec.test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.joyzl.codec.LittleEndianDataInput;
import com.joyzl.codec.LittleEndianDataOutput;

class TestLittleEndian {

	final ByteArrayOutputStream out = new ByteArrayOutputStream();
	final LittleEndianDataOutput output = new LittleEndianDataOutput() {
		@Override
		public void writeByte(int b) {
			out.write(b);
		}
	};
	ByteArrayInputStream in;
	final LittleEndianDataInput input = new LittleEndianDataInput() {
		@Override
		public byte readByte() {
			return (byte) in.read();
		}
	};

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
	void testValues() throws IOException {
		output.writeByte(Byte.MIN_VALUE);
		output.writeByte(Byte.MAX_VALUE);
		output.writeBoolean(Boolean.FALSE);
		output.writeBoolean(Boolean.TRUE);
		output.writeShort(Short.MIN_VALUE);
		output.writeShort(Short.MAX_VALUE);
		output.writeMedium(0x800000);
		output.writeMedium(0x7FFFFF);
		output.writeInt(Integer.MIN_VALUE);
		output.writeInt(Integer.MAX_VALUE);
		output.writeLong(Long.MIN_VALUE);
		output.writeLong(Long.MAX_VALUE);

		output.writeFloat(Float.POSITIVE_INFINITY);
		output.writeFloat(Float.NEGATIVE_INFINITY);
		output.writeFloat(Float.NaN);
		output.writeFloat(Float.MAX_VALUE);
		output.writeFloat(Float.MIN_NORMAL);
		output.writeFloat(Float.MIN_VALUE);

		output.writeDouble(Double.MIN_NORMAL);
		output.writeDouble(Double.POSITIVE_INFINITY);
		output.writeDouble(Double.NEGATIVE_INFINITY);
		output.writeDouble(Double.NaN);
		output.writeDouble(Double.MAX_VALUE);
		output.writeDouble(Double.MIN_NORMAL);
		output.writeDouble(Double.MIN_VALUE);

		output.writeByte(0xFF);
		output.writeShort(0xFFFF);
		output.writeMedium(0xFFFFFF);
		output.writeInt(0xFFFFFFFF);

		in = new ByteArrayInputStream(out.toByteArray());

		assertEquals(input.readByte(), Byte.MIN_VALUE);
		assertEquals(input.readByte(), Byte.MAX_VALUE);
		assertEquals(input.readBoolean(), Boolean.FALSE);
		assertEquals(input.readBoolean(), Boolean.TRUE);
		assertEquals(input.readShort(), Short.MIN_VALUE);
		assertEquals(input.readShort(), Short.MAX_VALUE);
		assertEquals(input.readMedium(), 0x800000);
		assertEquals(input.readMedium(), 0x7FFFFF);
		assertEquals(input.readInt(), Integer.MIN_VALUE);
		assertEquals(input.readInt(), Integer.MAX_VALUE);
		assertEquals(input.readLong(), Long.MIN_VALUE);
		assertEquals(input.readLong(), Long.MAX_VALUE);

		assertEquals(input.readFloat(), Float.POSITIVE_INFINITY);
		assertEquals(input.readFloat(), Float.NEGATIVE_INFINITY);
		assertEquals(input.readFloat(), Float.NaN);
		assertEquals(input.readFloat(), Float.MAX_VALUE);
		assertEquals(input.readFloat(), Float.MIN_NORMAL);
		assertEquals(input.readFloat(), Float.MIN_VALUE);

		assertEquals(input.readDouble(), Double.MIN_NORMAL);
		assertEquals(input.readDouble(), Double.POSITIVE_INFINITY);
		assertEquals(input.readDouble(), Double.NEGATIVE_INFINITY);
		assertEquals(input.readDouble(), Double.NaN);
		assertEquals(input.readDouble(), Double.MAX_VALUE);
		assertEquals(input.readDouble(), Double.MIN_NORMAL);
		assertEquals(input.readDouble(), Double.MIN_VALUE);

		assertEquals(input.readUnsignedByte(), 0xFF);
		assertEquals(input.readUnsignedShort(), 0xFFFF);
		assertEquals(input.readUnsignedMedium(), 0xFFFFFF);
		assertEquals(input.readUnsignedInt(), 0xFFFFFFFFL);

		assertEquals(in.available(), 0);
	}

	@Test
	void testBases() throws IOException {
		final Date DATE = new Date();
		final String CHARS = "0987654321abcdefghijklmnopqrstuvwxyz中华人民共和国ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String ASCII = "0987654321abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		output.writeDecimal(BigDecimal.ONE);
		output.writeDecimal(BigDecimal.TEN);
		output.writeDecimal(BigDecimal.ZERO);
		output.writeDate(DATE);
		output.writeLocalTime(LocalTime.MIN);
		output.writeLocalTime(LocalTime.MAX);
		output.writeLocalTime(LocalTime.NOON);
		output.writeLocalTime(LocalTime.MIDNIGHT);
		output.writeLocalDate(LocalDate.MIN);
		output.writeLocalDate(LocalDate.MAX);
		output.writeLocalDate(LocalDate.EPOCH);
		output.writeLocalDateTime(LocalDateTime.MIN);
		output.writeLocalDateTime(LocalDateTime.MAX);
		output.writeChar(Character.MIN_VALUE);
		output.writeChar(Character.MAX_VALUE);
		output.writeChars(CHARS);
		output.writeString(CHARS);
		output.writeASCII((char) 0);
		output.writeASCII((char) 127);
		output.writeASCIIs(ASCII);
		output.writeASCIIString(ASCII);

		in = new ByteArrayInputStream(out.toByteArray());

		assertEquals(input.readDecimal(), BigDecimal.ONE);
		assertEquals(input.readDecimal(), BigDecimal.TEN);
		assertEquals(input.readDecimal(), BigDecimal.ZERO);
		assertEquals(input.readDate(), DATE);
		assertEquals(input.readLocalTime(), LocalTime.MIN);
		assertEquals(input.readLocalTime(), LocalTime.MAX);
		assertEquals(input.readLocalTime(), LocalTime.NOON);
		assertEquals(input.readLocalTime(), LocalTime.MIDNIGHT);
		assertEquals(input.readLocalDate(), LocalDate.MIN);
		assertEquals(input.readLocalDate(), LocalDate.MAX);
		assertEquals(input.readLocalDate(), LocalDate.EPOCH);
		assertEquals(input.readLocalDateTime(), LocalDateTime.MIN);
		assertEquals(input.readLocalDateTime(), LocalDateTime.MAX);
		assertEquals(input.readChar(), Character.MIN_VALUE);
		assertEquals(input.readChar(), Character.MAX_VALUE);
		assertArrayEquals(input.readChars(CHARS.length()), CHARS.toCharArray());
		assertEquals(input.readString(), CHARS);
		assertEquals(input.readASCII(), 0);
		assertEquals(input.readASCII(), 127);
		assertArrayEquals(input.readASCIIs(ASCII.length()), ASCII.toCharArray());
		assertEquals(input.readASCIIString(), ASCII);

		assertEquals(in.available(), 0);
	}

	@Test
	void testOther() throws IOException {
		output.writeVarint(0);
		output.writeVarint(Integer.MIN_VALUE);
		output.writeVarint(Integer.MAX_VALUE);
		output.writeVarlong(0);
		output.writeVarlong(Long.MIN_VALUE);
		output.writeVarlong(Long.MAX_VALUE);

		in = new ByteArrayInputStream(out.toByteArray());

		assertEquals(input.readVarint(), 0);
		assertEquals(input.readVarint(), Integer.MIN_VALUE);
		assertEquals(input.readVarint(), Integer.MAX_VALUE);
		assertEquals(input.readVarlong(), 0);
		assertEquals(input.readVarlong(), Long.MIN_VALUE);
		assertEquals(input.readVarlong(), Long.MAX_VALUE);

		assertEquals(in.available(), 0);
	}
}
