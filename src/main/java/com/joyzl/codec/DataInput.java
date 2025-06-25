/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.codec;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
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
		for (int index = 0; index < b.length; index++) {
			b[index] = readByte();
		}
	}

	@Override
	default void readFully(byte b[], int off, int len) throws IOException {
		// 阻塞直到读取所有字节
		for (; len > 0; off++, len--) {
			b[off] = readByte();
		}
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

	/** 读取integer值(无符号,变长),1~5 Byte, 8~32 Bit */
	default int readVarint() throws IOException {
		/*-
		 * ZhangXi 20250622
		 * 取消循环模式改为判断模式
		int value = 0;
		for (int shift = 0; shift < 64; shift += 7) {
			final byte b = readByte();
			value |= (long) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				break;
			}
		}
		return value;
		*/

		byte b = readByte();
		if ((b & 0x80) != 0) {
			int value = b & 0x7F;
			b = readByte();
			value |= (b & 0x7F) << 7;
			if ((b & 0x80) != 0) {
				b = readByte();
				value |= (b & 0x7F) << 14;
				if ((b & 0x80) != 0) {
					b = readByte();
					value |= (b & 0x7F) << 21;
					if ((b & 0x80) != 0) {
						b = readByte();
						value |= (b & 0x7F) << 28;
						if ((b & 0x80) != 0) {
							// 溢出4字节整形范围
							throw new IOException("Varint overflow");
						}
					}
				}
			}
			return value;
		} else {
			return b;
		}
	}

	/** 读取long值(无符号,变长),1~10 Byte, 8~64 Bit */
	default long readVarlong() throws IOException {
		/*-
		 * ZhangXi 20250622
		 * 取消循环模式改为判断模式
		long result = 0;
		for (int shift = 0; shift < 64; shift += 7) {
			final byte b = (byte) readByte();
			result |= (long) (b & 0x7F) << shift;
			if ((b & 0x80) == 0) {
				break;
			}
		}
		return result;
		*/

		byte b = readByte();
		if ((b & 0x80) != 0) {
			long value = b & 0x7F;
			b = readByte();
			value |= (b & 0x7F) << 7;
			if ((b & 0x80) != 0) {
				b = readByte();
				value |= (b & 0x7F) << 14;
				if ((b & 0x80) != 0) {
					b = readByte();
					value |= (b & 0x7F) << 21;
					if ((b & 0x80) != 0) {
						b = readByte();
						value |= (b & 0x7FL) << 28;
						if ((b & 0x80) != 0) {
							b = readByte();
							value |= (b & 0x7FL) << 35;
							if ((b & 0x80) != 0) {
								b = readByte();
								value |= (b & 0x7FL) << 42;
								if ((b & 0x80) != 0) {
									b = readByte();
									value |= (b & 0x7FL) << 49;
									if ((b & 0x80) != 0) {
										b = readByte();
										value |= (b & 0x7FL) << 56;
										if ((b & 0x80) != 0) {
											b = readByte();
											value |= (b & 0x7FL) << 63;
											if ((b & 0x80) != 0) {
												// 溢出8字节整形范围
												throw new IOException("Varint overflow");
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return value;
		} else {
			return b;
		}
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

	/** 读取UTF8值,1~4 Byte codePoint(须调用者处理代理项) */
	default int readUTF8() throws IOException {
		// UTF-8 RFC3629
		// 00000000-0000007F | 0xxxxxxx
		// 00000080-000007FF | 110xxxxx 10xxxxxx
		// 00000800-0000FFFF | 1110xxxx 10xxxxxx 10xxxxxx
		// 00010000-0010FFFF | 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx

		// 常量
		// 0x80 = 0b10000000
		// 0xC0 = 0b11000000
		// 0xE0 = 0b11100000
		// 0xF0 = 0b11110000
		// 0xF8 - 0b11111000
		// 0x3F = 0b00111111
		// 0x1F = 0b00011111
		// 0x0F = 0b00001111
		// 0x07 = 0b00000111

		int code = readByte();
		if ((code & 0x80) == 0) {
		} else if ((code & 0xE0) == 0xC0) {
			code = (code & 0x1F) << 6;
			code |= (readByte() & 0x3F);
		} else if ((code & 0xF0) == 0xE0) {
			code = (code & 0x0F) << 12;
			code |= (readByte() & 0x3F) << 6;
			code |= (readByte() & 0x3F);
		} else if ((code & 0xF8) == 0xF0) {
			code = (code & 0x07) << 18;
			code |= (readByte() & 0x3F) << 12;
			code |= (readByte() & 0x3F) << 6;
			code |= (readByte() & 0x3F);
		} else {
			// 非法字节
			code = 0;
		}
		return code;
	}

	/** 读取UTF8字符，调用者应确保CharBuffer长度合适 */
	default void readUTF8(CharBuffer buffer) throws IOException {
		if (buffer.isReadOnly()) {
			throw new ReadOnlyBufferException();
		}
		int code;
		while (buffer.hasRemaining()) {
			code = readUTF8();
			if (Character.isBmpCodePoint(code)) {
				buffer.put((char) code);
			} else if (Character.isValidCodePoint(code)) {
				buffer.put(Character.highSurrogate(code));
				buffer.put(Character.lowSurrogate(code));
			}
		}
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