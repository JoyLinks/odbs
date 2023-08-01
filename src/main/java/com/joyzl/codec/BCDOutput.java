package com.joyzl.codec;

import java.io.IOException;

/**
 * BCD(Binary Coded Decimal)单个字节编码写入，与字节序无关
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2023年7月29日
 */
public interface BCDOutput extends DataOutput {

	/** @see #writeBCD8421(int) */
	default void writeBCD(int value) throws IOException {
		writeBCD8421(value);
	}

	/**
	 * 数值编码为BCD8421字节，范围0~99，不足2位值自动左补零
	 * 
	 * <pre>
	 * VALUE:98
	 * BYTES:
	 *  0
	 * +--+
	 * |98|
	 * +--+
	 * </pre>
	 */
	default void writeBCD8421(int value) throws IOException {
		writeByte((value / 10) << 4 | (value % 10));
	}

	/**
	 * 数值编码为BCD余3码字节，范围0~99，不足2位值自动左补零
	 * 
	 * <pre>
	 * VALUE:98
	 * BYTES:
	 *  0
	 * +--+
	 * |CB|
	 * +--+
	 * </pre>
	 */
	default void writeBCD3(int value) throws IOException {
		writeByte((value / 10 + 3) << 4 | (value % 10 + 3));
	}

	/**
	 * 数值编码为BCD2421字节，范围0~99，不足2位值自动左补零
	 * 
	 * <pre>
	 * VALUE:98
	 * BYTES:
	 *  0
	 * +--+
	 * |FE|
	 * +--+
	 * </pre>
	 */
	default void writeBCD2421(int value) throws IOException {
		/*-
		 * BCD 2421
		 * +-+----+--+
		 * |0|0000| 0|
		 * |1|0001| 1|
		 * |2|0010| 2|
		 * |3|0011| 3|
		 * |4|0100| 4|
		 * |5|1011|11|
		 * |6|1100|12|
		 * |7|1101|13|
		 * |8|1110|14|
		 * |9|1111|15|
		 * +-+----+--+
		 */
		int a = value / 10;
		int b = value % 10;
		if (a > 4) {
			a += 6;
		}
		if (b > 4) {
			b += 6;
		}
		writeByte(a << 4 | b);
	}

	/**
	 * 数值编码为BCD5421字节，范围0~99，不足2位值自动左补零
	 * 
	 * <pre>
	 * VALUE:98
	 * BYTES:
	 *  0
	 * +--+
	 * |CB|
	 * +--+
	 * </pre>
	 */
	default void writeBCD5421(int value) throws IOException {
		/*-
		 * BCD 5421
		 * +-+----+--+
		 * |0|0000| 0|
		 * |1|0001| 1|
		 * |2|0010| 2|
		 * |3|0011| 3|
		 * |4|0100| 4|
		 * |5|1000| 8|
		 * |6|1001| 9|
		 * |7|1010|10|
		 * |8|1011|11|
		 * |9|1100|12|
		 * +-+----+--+
		 */
		int a = value / 10;
		int b = value % 10;
		if (a > 4) {
			a += 3;
		}
		if (b > 4) {
			b += 3;
		}
		writeByte(a << 4 | b);
	}

	/**
	 * 数值编码为BCD5211字节，范围0~99，不足2位值自动左补零
	 * 
	 * <pre>
	 * VALUE:98
	 * BYTES:
	 *  0
	 * +--+
	 * |FD|
	 * +--+
	 * </pre>
	 */
	default void writeBCD5211(int value) throws IOException {
		/*-
		 * BCD 5211
		 * +-+----+--+
		 * |0|0000| 0|
		 * |1|0001| 1|
		 * |2|0100| 4|
		 * |3|0101| 5|
		 * |4|0111| 7|
		 * |5|1000| 8|
		 * |6|1001| 9|
		 * |7|1100|12|
		 * |8|1101|13|
		 * |9|1111|15|
		 * +-+----+--+
		 */

	}

	/**
	 * 数值编码为BCD余3循环字节，范围0~99，不足2位值自动左补零
	 * 
	 * <pre>
	 * VALUE:98
	 * BYTES:
	 *  0
	 * +--+
	 * |FD|
	 * +--+
	 * </pre>
	 */
	default void writeBCD3C(int value) throws IOException {
		/*-
		 * BCD 余3循环
		 * +-+----+--+
		 * |0|0010| 2|
		 * |1|0110| 6|
		 * |2|0111| 7|
		 * |3|0101| 5|
		 * |4|0100| 4|
		 * |5|1100|12|
		 * |6|1101|13|
		 * |7|1111|15|
		 * |8|1110|14|
		 * |9|1010|10|
		 * +-+----+--+
		 */

	}
}