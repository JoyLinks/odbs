package com.joyzl.codec;

import java.io.IOException;

/**
 * BCD(Binary Coded Decimal)，小端序(LITTLE_ENDIAN)
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2023年7月29日
 */
public interface LittleEndianBCDInput extends BCDInput {

	/** @see #readBCD8421s(int) */
	default int readBCDs(int size) throws IOException {
		return readBCD8421s(size);
	}

	/** @see #readBCD8421String(int) */
	default String readBCDString(int size) throws IOException {
		return readBCD8421String(size);
	}

	/**
	 * BCD8421字节解码为数值，最多10位有效数值
	 * 
	 * <pre>
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |21|43|65|87|09|
	 * +--+--+--+--+--+
	 * VALUE:987654321
	 * </pre>
	 */
	default int readBCD8421s(int size) throws IOException {
		int s = 1, value = 0;
		while (size > 0) {
			value += readBCD8421() * s;
			s *= 100;
			size -= 2;
		}
		return value;
	}

	/**
	 * BCD8421字节解码为字符串
	 * 
	 * <pre>
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |21|43|65|87|09|
	 * +--+--+--+--+--+
	 * VALUE:"987654321"
	 * </pre>
	 */
	default String readBCD8421String(int size) throws IOException {
		int value;
		char[] values = new char[size];
		while (size > 0) {
			value = readBCD8421();
			values[--size] = Character.forDigit(value / 10, 10);
			values[--size] = Character.forDigit(value % 10, 10);
		}
		return String.valueOf(values);
	}

	/**
	 * BCD余3码字节解码为数值，最多10位有效数值
	 * 
	 * <pre>
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |54|76|98|BA|0C|
	 * +--+--+--+--+--+
	 * VALUE:987654321
	 * </pre>
	 */
	default int readBCD3s(int size) throws IOException {
		int s = 1, value = 0;
		while (size > 0) {
			value += readBCD3() * s;
			s *= 100;
			size -= 2;
		}
		return value;
	}

	/**
	 * BCD余3码字节解码为字符串
	 * 
	 * <pre>
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |54|76|98|BA|0C|
	 * +--+--+--+--+--+
	 * VALUE:"987654321"
	 * </pre>
	 */
	default String readBCD3String(int size) throws IOException {
		int value;
		char[] values = new char[size];
		while (size > 0) {
			value = readBCD3();
			values[--size] = Character.forDigit(value / 10, 10);
			values[--size] = Character.forDigit(value % 10, 10);
		}
		return String.valueOf(values);
	}

	/**
	 * BCD2421字节解码为数值，最多10位有效数值
	 * 
	 * <pre>
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |21|43|CB|ED|0F|
	 * +--+--+--+--+--+
	 * VALUE:987654321
	 * </pre>
	 */
	default int readBCD2421s(int size) throws IOException {
		int s = 1, value = 0;
		while (size > 0) {
			value += readBCD2421() * s;
			s *= 100;
			size -= 2;
		}
		return value;
	}

	/**
	 * BCD2421字节解码为字符串
	 * 
	 * <pre>
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |21|43|CB|ED|0F|
	 * +--+--+--+--+--+
	 * VALUE:"987654321"
	 * </pre>
	 */
	default String readBCD2421String(int size) throws IOException {
		int value;
		char[] values = new char[size];
		while (size > 0) {
			value = readBCD2421();
			values[--size] = Character.forDigit(value / 10, 10);
			values[--size] = Character.forDigit(value % 10, 10);
		}
		return String.valueOf(values);
	}

	/**
	 * BCD5421字节解码为数值，最多10位有效数值
	 * 
	 * <pre>
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |21|43|98|BA|0C|
	 * +--+--+--+--+--+
	 * VALUE:987654321
	 * </pre>
	 */
	default int readBCD5421s(int size) throws IOException {
		int s = 1, value = 0;
		while (size > 0) {
			value += readBCD5421() * s;
			s *= 100;
			size -= 2;
		}
		return value;
	}

	/**
	 * BCD5421字节解码为字符串
	 * 
	 * <pre>
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |21|43|98|BA|0C|
	 * +--+--+--+--+--+
	 * VALUE:"987654321"
	 * </pre>
	 */
	default String readBCD5421String(int size) throws IOException {
		int value;
		char[] values = new char[size];
		while (size > 0) {
			value = readBCD5421();
			values[--size] = Character.forDigit(value / 10, 10);
			values[--size] = Character.forDigit(value % 10, 10);
		}
		return String.valueOf(values);
	}
}