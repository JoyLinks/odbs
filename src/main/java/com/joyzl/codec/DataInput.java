/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.codec;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 字节解码为数据
 * 
 * <pre>
 * VALUE: 0x76543210
 * READS: 0 ~ 3
 *  0  1  2  3
 * +--+--+--+--+
 * |76|54|32|10|
 * +--+--+--+--+
 * </pre>
 * 
 * @author ZhangXi
 * @date 2023年7月28日
 */
public interface DataInput extends java.io.DataInput {

	@Override
	default void readFully(byte b[]) throws IOException {
		// 阻塞直到读取所有字节
	}

	@Override
	default void readFully(byte b[], int off, int len) throws IOException {
		// 阻塞直到读取所有字节
	}

	@Override
	default int skipBytes(int n) throws IOException {
		int index;
		for (index = 0; index < n; index++) {
			readByte();
		}
		return index;
	}

	////////////////////////////////////////////////////////////////////////////////

	@Override
	byte readByte() throws IOException;

	/** 读取byte值(无符号),2 Byte, 16 Bit */
	@Override
	default int readUnsignedByte() throws IOException {
		return readByte() & 0xFF;
	}

	/** 读取boolean值,1 Byte, 8 Bit */
	@Override
	default boolean readBoolean() throws IOException {
		return readByte() != 0;
	}

	@Override
	short readShort() throws IOException;

	@Override
	int readUnsignedShort() throws IOException;

	@Override
	int readInt() throws IOException;

	@Override
	long readLong() throws IOException;

	@Override
	float readFloat() throws IOException;

	@Override
	double readDouble() throws IOException;

	/** 读取integer值(无符号,变长),1~4 Byte, 8~32 Bit */
	default int readVarint() throws IOException {
		int value = 0;
		for (int shift = 0; shift < 64; shift += 7) {
			final byte b = readByte();
			value |= (long) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				break;
			}
		}
		return value;
	}

	/** 读取long值(无符号,变长),1~8 Byte, 8~64 Bit */
	default long readVarlong() throws IOException {
		long result = 0;
		for (int shift = 0; shift < 64; shift += 7) {
			final byte b = (byte) readByte();
			result |= (long) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				break;
			}
		}
		return result;
	}

	/** 读取decimal值,n Byte, n*8 Bit */
	default BigDecimal readDecimal() throws IOException {
		final byte[] bytes = new byte[readByte()];
		for (int index = 0; index < bytes.length; index++) {
			bytes[index] = readByte();
		}
		final int scale = readByte();
		return new BigDecimal(new BigInteger(bytes), scale);
	}

	/** 读取Date值,8 Byte */
	default Date readDate() throws IOException {
		return new Date(readLong());
	}

	/** 读取LocalTime值,1~7 Byte */
	default LocalTime readLocalTime() throws IOException {
		// 参考:LocalTime.readExternal(DataInput in);
		int hour = readByte();
		int minute = 0;
		int second = 0;
		int nano = 0;
		if (hour < 0) {
			hour = ~hour;
		} else {
			minute = readByte();
			if (minute < 0) {
				minute = ~minute;
			} else {
				second = readByte();
				if (second < 0) {
					second = ~second;
				} else {
					nano = readInt();
				}
			}
		}
		return LocalTime.of(hour, minute, second, nano);
	}

	/** 读取LocalDate值,6 Byte */
	default LocalDate readLocalDate() throws IOException {
		// 参考:LocalDate.readExternal(DataInput in);
		final int year = readInt();
		final int month = readByte();
		final int dayOfMonth = readByte();
		return LocalDate.of(year, month, dayOfMonth);
	}

	/** 读取LocalDateTime值,7~13 Byte */
	default LocalDateTime readLocalDateTime() throws IOException {
		// 参考:LocalDateTime.readExternal(DataInput in);
		final LocalDate date = readLocalDate();
		final LocalTime time = readLocalTime();
		return LocalDateTime.of(date, time);
	}

	/** 读取char值,1~2 Byte */
	@Override
	default char readChar() throws IOException {
		// 参考:https://www.unicode.org/versions/Unicode15.0.0/
		return (char) readVarint();
	}

	/**
	 * 读取char值
	 * 
	 * @param size 字符数
	 * @return char[size]
	 */
	default char[] readChars(int size) throws IOException {
		final char[] chars = new char[size];
		for (size = 0; size < chars.length; size++) {
			chars[size] = readChar();
		}
		return chars;
	}

	/** 读取String值,n byte */
	default String readString() throws IOException {
		return String.valueOf(readChars(readVarint()));
	}

	/** 读取ASCII值,1 Byte */
	default char readASCII() throws IOException {
		return (char) readByte();
	}

	/**
	 * 读取ASCII值
	 * 
	 * @param size 字符数
	 * @return char[size]
	 */
	default char[] readASCIIs(int size) throws IOException {
		final char[] asciis = new char[size];
		for (size = 0; size < asciis.length; size++) {
			asciis[size] = (char) readByte();
		}
		return asciis;
	}

	/** 读取ASCII String值,1 Byte, 8 Bit */
	default String readASCIIString() throws IOException {
		return String.valueOf(readASCIIs(readVarint()));
	}

	@Override
	@Deprecated
	default String readLine() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	default String readUTF() {
		throw new UnsupportedOperationException();
	}
}