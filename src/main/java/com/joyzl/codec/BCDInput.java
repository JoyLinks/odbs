package com.joyzl.codec;

import java.io.IOException;

/**
 * BCD(Binary Coded Decimal)单个字节解码读取，与字节序无关
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2023年7月29日
 */
public interface BCDInput extends DataInput {

	/** @see #readBCD8421(int) */
	default int readBCD() throws IOException {
		return readBCD8421();
	}

	/**
	 * BCD8421字节解码为数值，范围0~99
	 */
	default int readBCD8421() throws IOException {
		int value = readUnsignedByte();
		return (value >>> 4) * 10 + (value & 0x0F);
	}

	/**
	 * BCD余3码字节解码为数值，范围0~99
	 */
	default int readBCD3() throws IOException {
		int value = readUnsignedByte();
		return ((value >>> 4) - 3) * 10 + (value & 0x0F) - 3;
	}

	/**
	 * BCD2421字节解码为数值，范围0~99
	 */
	default int readBCD2421() throws IOException {
		int value = readUnsignedByte();
		int a = value >>> 4;
		int b = value & 0x0F;
		if (a > 10) {
			a -= 6;
		}
		if (b > 10) {
			b -= 6;
		}
		return a * 10 + b;
	}

	/**
	 * BCD5421字节解码为数值，范围0~99
	 */
	default int readBCD5421() throws IOException {
		int value = readUnsignedByte();
		int a = value >>> 4;
		int b = value & 0x0F;
		if (a > 7) {
			a -= 3;
		}
		if (b > 7) {
			b -= 3;
		}
		return a * 10 + b;
	}
}