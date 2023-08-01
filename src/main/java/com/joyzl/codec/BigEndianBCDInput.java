package com.joyzl.codec;

import java.io.IOException;

/**
 * BCD(Binary Coded Decimal)，大端序(BIG_ENDIAN)
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2023年7月29日
 */
public interface BigEndianBCDInput extends BCDInput {

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
	 */
	default int readBCD8421s(int size) throws IOException {
		int value = 0;
		while (size > 0) {
			value *= 100;
			value += readBCD8421();
			size -= 2;
		}
		return value;
	}

	/**
	 * BCD8421字节解码为字符串
	 */
	default String readBCD8421String(int size) throws IOException {
		int value;
		char[] values = new char[size];
		while (size > 0) {
			value = readBCD8421();
			values[values.length - size--] = Character.forDigit(value / 10, 10);
			values[values.length - size--] = Character.forDigit(value % 10, 10);
		}
		return String.valueOf(values);
	}

	/**
	 * BCD余3码字节解码为数值，最多10位有效数值
	 */
	default int readBCD3s(int size) throws IOException {
		int value = 0;
		while (size > 0) {
			value *= 100;
			value += readBCD3();
			size -= 2;
		}
		return value;
	}

	/**
	 * BCD余3码字节解码为字符串
	 */
	default String readBCD3String(int size) throws IOException {
		int value;
		char[] values = new char[size];
		while (size > 0) {
			value = readBCD3();
			values[values.length - size--] = Character.forDigit(value / 10, 10);
			values[values.length - size--] = Character.forDigit(value % 10, 10);
		}
		return String.valueOf(values);
	}

	/**
	 * BCD2421字节解码为数值，最多10位有效数值
	 */
	default int readBCD2421s(int size) throws IOException {
		int value = 0;
		while (size > 0) {
			value *= 100;
			value += readBCD2421();
			size -= 2;
		}
		return value;
	}

	/**
	 * BCD2421字节解码为字符串
	 */
	default String readBCD2421String(int size) throws IOException {
		int value;
		char[] values = new char[size];
		while (size > 0) {
			value = readBCD2421();
			values[values.length - size--] = Character.forDigit(value / 10, 10);
			values[values.length - size--] = Character.forDigit(value % 10, 10);
		}
		return String.valueOf(values);
	}

	/**
	 * BCD5421字节解码为数值，最多10位有效数值
	 */
	default int readBCD5421s(int size) throws IOException {
		int value = 0;
		while (size > 0) {
			value *= 100;
			value += readBCD5421();
			size -= 2;
		}
		return value;
	}

	/**
	 * BCD5421字节解码为字符串
	 */
	default String readBCD5421String(int size) throws IOException {
		int value;
		char[] values = new char[size];
		while (size > 0) {
			value = readBCD5421();
			values[values.length - size--] = Character.forDigit(value / 10, 10);
			values[values.length - size--] = Character.forDigit(value % 10, 10);
		}
		return String.valueOf(values);
	}
}