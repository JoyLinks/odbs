/*-
 * www.joyzl.net
 * 重庆骄智科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.codec;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 数据编码为字节，大端序(BIG_ENDIAN)
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
public interface BigEndianDataOutput extends DataOutput {

	@Override
	default void writeShort(int value) throws IOException {
		writeShort((short) value);
	}

	/** 写入short值,2 Byte, 16 Bit */
	default void writeShort(short value) throws IOException {
		writeByte(value >>> 8);
		writeByte(value);
	}

	/** 写入medium值,3 Byte, 24 Bit */
	default void writeMedium(int value) throws IOException {
		writeByte(value >>> 16);
		writeByte(value >>> 8);
		writeByte(value);
	}

	/** 写入integer值,4 Byte, 32 Bit */
	@Override
	default void writeInt(int value) throws IOException {
		writeByte(value >>> 24);
		writeByte(value >>> 16);
		writeByte(value >>> 8);
		writeByte(value);
	}

	/** 写入long值,8 Byte, 64 Bit */
	@Override
	default void writeLong(long value) throws IOException {
		writeByte((int) (value >>> 56));
		writeByte((int) (value >>> 48));
		writeByte((int) (value >>> 40));
		writeByte((int) (value >>> 32));
		writeByte((int) (value >>> 24));
		writeByte((int) (value >>> 16));
		writeByte((int) (value >>> 8));
		writeByte((int) value);
	}

	/** 写入float值,4 Byte, 32 Bit */
	@Override
	default void writeFloat(float value) throws IOException {
		writeInt(Float.floatToRawIntBits(value));
	}

	/** 写入double值,8 Byte, 64 Bit */
	@Override
	default void writeDouble(double value) throws IOException {
		writeLong(Double.doubleToRawLongBits(value));
	}

	/** 写入long double值(IEEE754 Binary128),16 Byte, 128 Bit */
	default void writeLongDouble(BigDecimal value) throws IOException {
		// TODO 未来支持
		throw new UnsupportedOperationException();
	}
}