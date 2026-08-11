package com.joyzl.codec;

import java.io.IOException;

public interface UTF8Writer {

	/*-
	 * RFC 3629
	 * 单字节 0xxxxxxx
	 * 双字节 110xxxxx 10xxxxxx
	 * 三字节 1110xxxx 10xxxxxx 10xxxxxx
	 * 四字节 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
	 */

	/** byte(0~255) */
	void write(int value) throws IOException;

	default void write(CharSequence value) throws IOException {
		int len = value.length();
		for (int i = 0; i < len; i++) {
			write(value.charAt(i));
		}
	}

	default void writeCodePoint(int code) throws IOException {
		if (code < 0 || code > 0x10FFFF || (code >= 0xD800 && code <= 0xDFFF)) {
			throw new IllegalArgumentException("无效码值" + code);
		}

		if (code < 0x80) {
			// 单字节 0xxxxxxx
			write(code);
		} else if (code < 0x800) {
			// 双字节 110xxxxx 10xxxxxx
			write(0xC0 | (code >> 6));
			write(0x80 | (code & 0x3F));
		} else if (code < 0x10000) {
			// 三字节 1110xxxx 10xxxxxx 10xxxxxx
			write(0xE0 | (code >> 12));
			write(0x80 | ((code >> 6) & 0x3F));
			write(0x80 | (code & 0x3F));
		} else {
			// 四字节 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
			write(0xF0 | (code >> 18));
			write(0x80 | ((code >> 12) & 0x3F));
			write(0x80 | ((code >> 6) & 0x3F));
			write(0x80 | (code & 0x3F));
		}
	}

	default void writeBoolean(boolean value) throws IOException {
		if (value) {
			// true
			write('t');
			write('r');
			write('u');
			write('e');
		} else {
			// false
			write('f');
			write('a');
			write('l');
			write('s');
			write('e');
		}
	}

	/** -128 ~ 127 */
	default void writeByte(byte value) throws IOException {
		if (value < 0) {
			write('-');
			if (value == Byte.MIN_VALUE) {
				write('1');
				write('2');
				write('8');
				return;
			} else {
				value = (byte) -value;
			}
		}

		int c;
		if (value > 100) {
			write('1');
			value -= 100;

			write(Character.forDigit(c = value / 10, 10));
			value -= c * 10;
		} else if (value > 10) {
			write(Character.forDigit(c = value / 10, 10));
			value -= c * 10;
		}
		write(Character.forDigit(value, 10));
	}

	/** -32768 ~ 32767 */
	default void writeShort(short value) throws IOException {
		if (value < 0) {
			write('-');
			if (value == Short.MIN_VALUE) {
				write('3');
				write('2');
				write('7');
				write('6');
				write('8');
				return;
			} else {
				value = (short) -value;
			}
		}

		int c;
		if (value < 10) {
			write('0' + value);
		} else if (value < 100) {
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else if (value < 1000) {
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else if (value < 10000) {
			write('0' + (c = value / 1000));
			value -= c * 1000;
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else {
			write('0' + (c = value / 10000));
			value -= c * 10000;
			write('0' + (c = value / 1000));
			value -= c * 1000;
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		}
	}

	/** -2147483648 ~ 2147483647 */
	default void writeInteger(int value) throws IOException {
		if (value < 0) {
			write('-');
			if (value == Integer.MIN_VALUE) {
				write('2');
				write('1');
				write('4');
				write('7');
				write('8');
				write('3');
				write('6');
				write('4');
				write('8');
				return;
			} else {
				value = -value;
			}
		}

		int c;
		if (value < 10) {
			write('0' + value);
		} else if (value < 100) {
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else if (value < 1000) {
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else if (value < 10000) {
			write('0' + (c = value / 1000));
			value -= c * 1000;
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else if (value < 100000) {
			write('0' + (c = value / 10000));
			value -= c * 10000;
			write('0' + (c = value / 1000));
			value -= c * 1000;
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else if (value < 1000000) {
			write('0' + (c = value / 100000));
			value -= c * 100000;
			write('0' + (c = value / 10000));
			value -= c * 10000;
			write('0' + (c = value / 1000));
			value -= c * 1000;
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else if (value < 10000000) {
			write('0' + (c = value / 1000000));
			value -= c * 1000000;
			write('0' + (c = value / 100000));
			value -= c * 100000;
			write('0' + (c = value / 10000));
			value -= c * 10000;
			write('0' + (c = value / 1000));
			value -= c * 1000;
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else if (value < 100000000) {
			write('0' + (c = value / 10000000));
			value -= c * 10000000;
			write('0' + (c = value / 1000000));
			value -= c * 1000000;
			write('0' + (c = value / 100000));
			value -= c * 100000;
			write('0' + (c = value / 10000));
			value -= c * 10000;
			write('0' + (c = value / 1000));
			value -= c * 1000;
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else if (value < 1000000000) {
			write('0' + (c = value / 100000000));
			value -= c * 100000000;
			write('0' + (c = value / 10000000));
			value -= c * 10000000;
			write('0' + (c = value / 1000000));
			value -= c * 1000000;
			write('0' + (c = value / 100000));
			value -= c * 100000;
			write('0' + (c = value / 10000));
			value -= c * 10000;
			write('0' + (c = value / 1000));
			value -= c * 1000;
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		} else {
			write('0' + (c = value / 1000000000));
			value -= c * 1000000000;
			write('0' + (c = value / 100000000));
			value -= c * 100000000;
			write('0' + (c = value / 10000000));
			value -= c * 10000000;
			write('0' + (c = value / 1000000));
			value -= c * 1000000;
			write('0' + (c = value / 100000));
			value -= c * 100000;
			write('0' + (c = value / 10000));
			value -= c * 10000;
			write('0' + (c = value / 1000));
			value -= c * 1000;
			write('0' + (c = value / 100));
			value -= c * 100;
			write('0' + (c = value / 10));
			value -= c * 10;
			write('0' + value);
		}
	}

	/** -9223372036854775808 ~ 9223372036854775807 */
	default void writeLong(long value) throws IOException {
		if (value == 0) {
			write('0');
			return;
		}
		if (value < 0) {
			write('-');
			if (value == Long.MIN_VALUE) {
				write('9');
				write('2');
				write('2');
				write('3');
				write('3');
				write('7');
				write('2');
				write('0');
				write('3');
				write('6');
				write('8');
				write('5');
				write('4');
				write('7');
				write('7');
				write('5');
				write('8');
				write('0');
				write('8');
				return;
			} else {
				value = -value;
			}
		}

		int c;
		if (value > 1000000000000000000L) {
			write('0' + (c = (int) (value / 1000000000000000000L)));
			value -= c * 1000000000000000000L;
			write('0' + (c = (int) (value / 100000000000000000L)));
			value -= c * 100000000000000000L;
			write('0' + (c = (int) (value / 10000000000000000L)));
			value -= c * 10000000000000000L;
			write('0' + (c = (int) (value / 1000000000000000L)));
			value -= c * 1000000000000000L;
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 100000000000000000L) {
			write('0' + (c = (int) (value / 100000000000000000L)));
			value -= c * 100000000000000000L;
			write('0' + (c = (int) (value / 10000000000000000L)));
			value -= c * 10000000000000000L;
			write('0' + (c = (int) (value / 1000000000000000L)));
			value -= c * 1000000000000000L;
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 10000000000000000L) {
			write('0' + (c = (int) (value / 10000000000000000L)));
			value -= c * 10000000000000000L;
			write('0' + (c = (int) (value / 1000000000000000L)));
			value -= c * 1000000000000000L;
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 1000000000000000L) {
			write('0' + (c = (int) (value / 1000000000000000L)));
			value -= c * 1000000000000000L;
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 100000000000000L) {
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 10000000000000L) {
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 1000000000000L) {
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 100000000000L) {
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 10000000000L) {
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 1000000000) {
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 100000000) {
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 10000000) {
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 1000000) {
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 100000) {
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 10000) {
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 1000) {
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 100) {
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value > 10) {
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else {
			write('0' + (int) (value));
		}

		/*-
		if (value < 10) {
			write('0' + (int) (value));
		} else if (value < 100) {
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 1000) {
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 10000) {
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 100000) {
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 1000000) {
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 10000000) {
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 100000000) {
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 1000000000) {
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 10000000000L) {
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 100000000000L) {
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 1000000000000L) {
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 10000000000000L) {
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 100000000000000L) {
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 1000000000000000L) {
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 10000000000000000L) {
			write('0' + (c = (int) (value / 1000000000000000L)));
			value -= c * 1000000000000000L;
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 100000000000000000L) {
			write('0' + (c = (int) (value / 10000000000000000L)));
			value -= c * 10000000000000000L;
			write('0' + (c = (int) (value / 1000000000000000L)));
			value -= c * 1000000000000000L;
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else if (value < 1000000000000000000L) {
			write('0' + (c = (int) (value / 100000000000000000L)));
			value -= c * 100000000000000000L;
			write('0' + (c = (int) (value / 10000000000000000L)));
			value -= c * 10000000000000000L;
			write('0' + (c = (int) (value / 1000000000000000L)));
			value -= c * 1000000000000000L;
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		} else {
			write('0' + (c = (int) (value / 1000000000000000000L)));
			value -= c * 1000000000000000000L;
			write('0' + (c = (int) (value / 100000000000000000L)));
			value -= c * 100000000000000000L;
			write('0' + (c = (int) (value / 10000000000000000L)));
			value -= c * 10000000000000000L;
			write('0' + (c = (int) (value / 1000000000000000L)));
			value -= c * 1000000000000000L;
			write('0' + (c = (int) (value / 100000000000000L)));
			value -= c * 100000000000000L;
			write('0' + (c = (int) (value / 10000000000000L)));
			value -= c * 10000000000000L;
			write('0' + (c = (int) (value / 1000000000000L)));
			value -= c * 1000000000000L;
			write('0' + (c = (int) (value / 100000000000L)));
			value -= c * 100000000000L;
			write('0' + (c = (int) (value / 10000000000L)));
			value -= c * 10000000000L;
			write('0' + (c = (int) (value / 1000000000)));
			value -= c * 1000000000;
			write('0' + (c = (int) (value / 100000000)));
			value -= c * 100000000;
			write('0' + (c = (int) (value / 10000000)));
			value -= c * 10000000;
			write('0' + (c = (int) (value / 1000000)));
			value -= c * 1000000;
			write('0' + (c = (int) (value / 100000)));
			value -= c * 100000;
			write('0' + (c = (int) (value / 10000)));
			value -= c * 10000;
			write('0' + (c = (int) (value / 1000)));
			value -= c * 1000;
			write('0' + (c = (int) (value / 100)));
			value -= c * 100;
			write('0' + (c = (int) (value / 10)));
			value -= c * 10;
			write('0' + (int) (value));
		}
		*/
	}

	default void writeFloat(float value) throws IOException {
		if (value == 0.0f) {
			write('0');
			return;
		}
		if (Float.isNaN(value)) {
			write('N');
			write('a');
			write('N');
			return;
		}
		if (Float.isInfinite(value)) {
			if (value < 0) {
				write('-');
			}
			write('I');
			write('n');
			write('f');
			write('i');
			write('n');
			write('i');
			write('t');
			write('y');
			return;
		}

		if (value < 0) {
			value = -value;
			write('-');
		}

	}
}