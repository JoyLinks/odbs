package com.joyzl.codec;

import java.io.IOException;

/**
 * BCD(Binary Coded Decimal)，大端序(BIG_ENDIAN)
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2023年7月29日
 */
public interface BigEndianBCDOutput extends BCDOutput {

	/** @see #writeBCD8421s(int) */
	default void writeBCDs(int value) throws IOException {
		writeBCD8421s(value);
	}

	/** @see #writeBCD8421s(CharSequence) */
	default void writeBCDs(CharSequence value) throws IOException {
		writeBCD8421s(value);
	}

	/**
	 * 数值编码为BCD8421字节，不足偶数位自动左补零
	 * 
	 * <pre>
	 * VALUE:987654321
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |09|87|65|43|21|
	 * +--+--+--+--+--+
	 * </pre>
	 */
	default void writeBCD8421s(int value) throws IOException {
		// int最多10位有效数值0xFFFFFFFF==4294967295
		int v, s = 100000000;
		while (s > 0) {
			v = value / s;
			if (v > 0) {
				writeBCD8421(v);
			}
			value = value % s;
			s /= 100;
		}
	}

	/**
	 * 字符串编码为BCD8421字节，字符串数量不足偶数自动左补零
	 * 
	 * <pre>
	 * VALUE:"987654321"
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |09|87|65|43|21|
	 * +--+--+--+--+--+
	 * </pre>
	 */
	default void writeBCD8421s(CharSequence value) throws IOException {
		int index = value.length() % 2;
		if (index != 0) {
			writeBCD8421(Character.digit(value.charAt(index++), 10));
			index = 1;
		}
		while (index < value.length()) {
			writeBCD8421(Character.digit(value.charAt(index++), 10) * 10 + Character.digit(value.charAt(index++), 10));
		}
	}

	/**
	 * 数值编码为BCD余3码字节，不足偶数位自动左补零
	 * 
	 * <pre>
	 * VALUE:987654321
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |0C|BA|98|76|54|
	 * +--+--+--+--+--+
	 * </pre>
	 */
	default void writeBCD3s(int value) throws IOException {
		// int最多10位有效数值
		int v, s = 100000000;
		while (s > 0) {
			v = value / s;
			if (v > 0) {
				writeBCD3(v);
			}
			value = value % s;
			s /= 100;
		}
	}

	/**
	 * 字符串编码为BCD余3码字节，字符串数量不足偶数自动左补零
	 * 
	 * <pre>
	 * VALUE:"987654321"
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |0C|BA|98|76|54|
	 * +--+--+--+--+--+
	 * </pre>
	 */
	default void writeBCD3s(CharSequence value) throws IOException {
		int index = value.length() % 2;
		if (index != 0) {
			writeBCD3(Character.digit(value.charAt(index++), 10));
			index = 1;
		}
		while (index < value.length()) {
			writeBCD3(Character.digit(value.charAt(index++), 10) * 10 + Character.digit(value.charAt(index++), 10));
		}
	}

	/**
	 * 数值编码为BCD2421字节，不足偶数位自动左补零
	 * 
	 * <pre>
	 * VALUE:987654321
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |0F|ED|CB|43|21|
	 * +--+--+--+--+--+
	 * </pre>
	 */
	default void writeBCD2421s(int value) throws IOException {
		// int最多10位有效数值
		int v, s = 100000000;
		while (s > 0) {
			v = value / s;
			if (v > 0) {
				writeBCD2421(v);
			}
			value = value % s;
			s /= 100;
		}
	}

	/**
	 * 字符串编码为BCD2421字节，字符串数量不足偶数自动左补零
	 * 
	 * <pre>
	 * VALUE:"987654321"
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |0F|ED|CB|43|21|
	 * +--+--+--+--+--+
	 * </pre>
	 */
	default void writeBCD2421s(CharSequence value) throws IOException {
		int index = value.length() % 2;
		if (index != 0) {
			writeBCD2421(Character.digit(value.charAt(index++), 10));
			index = 1;
		}
		while (index < value.length()) {
			writeBCD2421(Character.digit(value.charAt(index++), 10) * 10 + Character.digit(value.charAt(index++), 10));
		}
	}

	/**
	 * 数值编码为BCD5421字节，不足偶数位自动左补零
	 * 
	 * <pre>
	 * VALUE:987654321
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |0C|BA|98|43|21|
	 * +--+--+--+--+--+
	 * </pre>
	 */
	default void writeBCD5421s(int value) throws IOException {
		// int最多10位有效数值
		int v, s = 100000000;
		while (s > 0) {
			v = value / s;
			if (v > 0) {
				writeBCD5421(v);
			}
			value = value % s;
			s /= 100;
		}
	}

	/**
	 * 字符串编码为BCD5421字节，字符串数量不足偶数自动左补零
	 * 
	 * <pre>
	 * VALUE:"987654321"
	 * BYTES:
	 *  0  1  2  3  4
	 * +--+--+--+--+--+
	 * |0C|BA|98|43|21|
	 * +--+--+--+--+--+
	 * </pre>
	 */
	default void writeBCD5421s(CharSequence value) throws IOException {
		int index = value.length() % 2;
		if (index != 0) {
			writeBCD5421(Character.digit(value.charAt(index++), 10));
			index = 1;
		}
		while (index < value.length()) {
			writeBCD5421(Character.digit(value.charAt(index++), 10) * 10 + Character.digit(value.charAt(index++), 10));
		}
	}
}