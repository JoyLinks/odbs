/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
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
 * 数据编码为字节
 * 
 * <pre>
 * VALUE: 0x76543210
 * WRITE: 0 ~ 3
 *  0  1  2  3
 * +--+--+--+--+
 * |76|54|32|10|
 * +--+--+--+--+
 * </pre>
 * 
 * @author ZhangXi
 * @date 2023年7月28日
 */
public interface DataOutput extends java.io.DataOutput {

	@Override
	default void write(int b) throws IOException {
		writeByte((byte) b);
	}

	@Override
	default void write(byte[] values) throws IOException {
		for (int index = 0; index < values.length; index++) {
			writeByte(values[index]);
		}
	}

	@Override
	default void write(byte[] values, int offset, int length) throws IOException {
		for (; length > 0; offset++, length--) {
			writeByte(values[offset]);
		}
	}

	////////////////////////////////////////////////////////////////////////////////

	@Override
	void writeByte(int b) throws IOException;

	default void writeByte(byte value) throws IOException {
		writeByte((int) value);
	}

	@Override
	default void writeBoolean(boolean value) throws IOException {
		writeByte(value ? 1 : 0);
	}

	@Override
	default void writeShort(int value) throws IOException {
		writeShort((short) value);
	}

	void writeShort(short value) throws IOException;

	@Override
	void writeInt(int value) throws IOException;

	@Override
	void writeLong(long value) throws IOException;

	@Override
	void writeFloat(float value) throws IOException;

	@Override
	void writeDouble(double value) throws IOException;

	/** 写入integer值(变长),1~5 Byte, 8~32 Bit */
	default void writeVarint(int value) throws IOException {
		// TODO 取消循环模式改为判断模式
		while ((value & ~0x7F) != 0) {
			writeByte((value & 0x7F) | 0x80);
			value >>>= 7;
		}
		writeByte(value);
	}

	/** 写入long值(变长),1~10 Byte, 8~64 Bit */
	default void writeVarlong(long value) throws IOException {
		// TODO 取消循环模式改为判断模式
		while ((value & ~0x7FL) != 0) {
			writeByte(((int) value & 0x7F) | 0x80);
			value >>>= 7;
		}
		writeByte((int) value);
	}

	/** 写入decimal值,n Byte, n*8 Bit */
	default void writeDecimal(BigDecimal value) throws IOException {
		BigInteger theInt = value.unscaledValue();
		byte[] bytes = theInt.toByteArray();
		writeByte(bytes.length);
		write(bytes);
		writeByte(value.scale());
	}

	/** 写入Date值,8 Byte */
	default void writeDate(Date value) throws IOException {
		writeLong(value.getTime());
	}

	/** 写入LocalTime值,1~7 Byte */
	default void writeLocalTime(LocalTime value) throws IOException {
		// 参考:LocalTime.writeExternal(DataOutput out);
		int hour = value.getHour();
		int minute = value.getMinute();
		int second = value.getSecond();
		int nano = value.getNano();
		if (nano == 0) {
			if (second == 0) {
				if (minute == 0) {
					writeByte(~hour);
				} else {
					writeByte(hour);
					writeByte(~minute);
				}
			} else {
				writeByte(hour);
				writeByte(minute);
				writeByte(~second);
			}
		} else {
			writeByte(hour);
			writeByte(minute);
			writeByte(second);
			writeInt(nano);
		}
	}

	/** 写入LocalDate值,6 Byte */
	default void writeLocalDate(LocalDate value) throws IOException {
		// 参考:LocalDate.writeExternal(DataOutput out);
		writeInt(value.getYear());
		writeByte(value.getMonthValue());
		writeByte(value.getDayOfMonth());
	}

	/** 写入LocalDateTime值,7~13 Byte */
	default void writeLocalDateTime(LocalDateTime value) throws IOException {
		// 参考:LocalDateTime.writeExternal(DataOutput out);
		writeLocalDate(value.toLocalDate());
		writeLocalTime(value.toLocalTime());
	}

	@Override
	default void writeChar(int value) throws IOException {
		writeChar((char) value);
	}

	/** 写入char值,2 Byte, 16 Bit */
	default void writeChar(char value) throws IOException {
		writeVarint(value);
	}

	@Override
	default void writeChars(String value) throws IOException {
		writeChars((CharSequence) value);
	}

	/**
	 * 写入CharSequence值,无前导数量
	 * 
	 * @see #writeChar(char)
	 */
	default void writeChars(CharSequence value) throws IOException {
		for (int index = 0; index < value.length(); index++) {
			writeChar(value.charAt(index));
		}
	}

	/** 写入String值,具有前导数量,n Byte */
	default void writeString(CharSequence value) throws IOException {
		writeVarint(value.length());
		writeChars(value);
	}

	/** 写入ASCII值,1 Byte, 8 Bit */
	default void writeASCII(char value) throws IOException {
		writeByte(value);
	}

	/** 写入ASCII值,无前导数量 */
	default void writeASCIIs(CharSequence value) throws IOException {
		for (int index = 0; index < value.length(); index++) {
			writeASCII(value.charAt(index));
		}
	}

	/** 写入ASCII值,具有前导长度,n Byte */
	default void writeASCIIString(CharSequence value) throws IOException {
		writeVarint(value.length());
		writeASCIIs(value);
	}

	/** 写入UTF8值,1~4 Byte */
	default void writeUTF8(char value) throws IOException {
		if (value <= 0x7F) {
			// ASCII(0-127)
			writeByte(value);
		} else if (value <= 0x7FF) {
			// Unicode 2Byte(128-2047)
			writeByte(0xC0 | (value >> 6));
			writeByte(0x80 | (value & 0x3F));
		} else if (value <= 0xFFFF) {
			// Unicode 3Byte(2048-65535)

			// 兼容格式代理项
			// 处理高代理项(Surrogate High)
			if (value >= 0xD800 && value <= 0xDBFF) {
				writeByte(0xED);
				writeByte(0xA0 | ((value - 0xD800) >> 6));
				writeByte(0x80 | ((value - 0xD800) & 0x3F));
				return;
			}
			// 处理低代理项(Surrogate Low)
			if (value >= 0xDC00 && value <= 0xDFFF) {
				writeByte(0xED);
				writeByte(0xB0 | ((value - 0xDC00) >> 6));
				writeByte(0x80 | ((value - 0xDC00) & 0x3F));
				return;
			}
			// 非代理项字符
			writeByte(0xE0 | (value >> 12));
			writeByte(0x80 | ((value >> 6) & 0x3F));
			writeByte(0x80 | (value & 0x3F));
		} else if (value <= 0x10FFFF) {
			writeByte(0xF0 | (value >> 18));
			writeByte(0x80 | ((value >> 12) & 0x3F));
			writeByte(0x80 | ((value >> 6) & 0x3F));
			writeByte(0x80 | (value & 0x3F));
		} else {
			// '\uFFFD';
			writeByte(0xEF);
			writeByte(0xBF);
			writeByte(0xBD);
		}
	}

	/** 写入UTF8代理项字符 */
	default void writeUTF8(char high, char low) throws IOException {
		// 组合为完整码点(U+10000 到 U+10FFFF)
		int code = ((high - 0xD800) << 10) + (low - 0xDC00) + 0x10000;
		// 编码 4 字节 UTF-8(U+10000 到 U+10FFFF)
		writeByte(0xF0 | (code >> 18));
		writeByte(0x80 | ((code >> 12) & 0x3F));
		writeByte(0x80 | ((code >> 6) & 0x3F));
		writeByte(0x80 | (code & 0x3F));
	}

	/** 写入UTF8值,无前导数量 */
	default void writeUTF8(CharSequence value) throws IOException {
		char c;
		for (int index = 0; index < value.length(); index++) {
			c = value.charAt(index);
			if (Character.isHighSurrogate(c)) {
				writeUTF8(c, value.charAt(index++));
			} else {
				writeUTF8(c);
			}
		}
	}

	@Override
	@Deprecated
	default void writeBytes(String s) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	default void writeUTF(String s) {
		throw new UnsupportedOperationException();
	}
}