package com.joyzl.codec;

import java.io.EOFException;
import java.io.IOException;

/**
 * UNICODE UTF-8 Reader
 * 
 * @author ZhangXi 2026年4月12日
 */
public interface UTF8Reader {

	/*-
	 * RFC 3629
	 * 单字节 0xxxxxxx
	 * 双字节 110xxxxx 10xxxxxx
	 * 三字节 1110xxxx 10xxxxxx 10xxxxxx
	 * 四字节 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
	 */

	/** byte(0~255) EOF(-1) */
	int read() throws IOException;

	default int readCodePoint() throws IOException {
		int value = read();
		if (value < 0) {
			throw new EOFException("流已结束");
		}

		if (value < 0x80) {
			// 单字节 0xxxxxxx
			return value;
		}

		if ((value >>> 5) == 0b110) {
			// 双字节 110xxxxx 10xxxxxx
			int code = value & 0b11111;

			value = read();
			if (value < 0) {
				throw new EOFException("流意外结束");
			}
			if ((value >>> 6) != 0b10) {
				throw new IOException("无效的编码" + value);
			}
			code = code << 6 | (value & 0b111111);

			if (code < 0x80 || code > 0x7FF) {
				throw new IOException("无效的码值" + code);
			}
			return code;
		}

		if ((value >>> 4) == 0b1110) {
			// 三字节 1110xxxx 10xxxxxx 10xxxxxx
			int code = value & 0b1111;

			value = read();
			if (value < 0) {
				throw new EOFException("流意外结束");
			}
			if ((value >>> 6) != 0b10) {
				throw new IOException("无效的编码" + value);
			}
			code = code << 6 | (value & 0b111111);

			value = read();
			if (value < 0) {
				throw new EOFException("流意外结束");
			}
			if ((value >>> 6) != 0b10) {
				throw new IOException("无效的编码" + value);
			}
			code = code << 6 | (value & 0b111111);

			if (code < 0x800 || (code >= 0xD800 && code <= 0xDFFF)) {
				throw new IOException("无效的码值" + code);
			}
			return code;
		}

		if ((value >>> 3) == 0b11110) {
			// 四字节 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
			int code = value & 0b111;

			value = read();
			if (value < 0) {
				throw new EOFException("流意外结束");
			}
			if ((value >>> 6) != 0b10) {
				throw new IOException("无效的编码" + value);
			}
			code = code << 6 | (value & 0b111111);

			value = read();
			if (value < 0) {
				throw new EOFException("流意外结束");
			}
			if ((value >>> 6) != 0b10) {
				throw new IOException("无效的编码" + value);
			}
			code = code << 6 | (value & 0b111111);

			value = read();
			if (value < 0) {
				throw new EOFException("流意外结束");
			}
			if ((value >>> 6) != 0b10) {
				throw new IOException("无效的编码" + value);
			}
			code = code << 6 | (value & 0b111111);

			if (code < 0x10000 || code > 0x10FFFF) {
				throw new IOException("无效的码值" + code);
			}
			return code;
		}

		throw new IOException("无效编码" + value);
	}

	static boolean readBolean(char[] value, int length) {
		if (length == 4) {
			if (value[0] == 't') {
				if (value[1] == 'r') {
					if (value[2] == 'u') {
						if (value[3] == 'e') {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	static byte readByte(char[] value, int length) {
		if (length == 1) {
			if (value[0] == '-') {
				throw new NumberFormatException(new String(value, 0, length));
			} else {
				return (byte) (value[0] - '0');
			}
		}

		int v = 0;
		if (value[0] == '-') {
			v = value[1] - '0';
		} else {
			v = value[0] - '0';
			v = v * 10 + value[1] - '0';
		}

		return (byte) v;
	}
}