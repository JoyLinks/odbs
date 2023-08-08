package com.joyzl.codec;

import java.io.IOException;

/**
 * 数据编码为字节，小端序(LITTLE_ENDIAN)
 * 
 * <pre>
 * VALUE: 0x76543210
 * WRITE: 0 ~ 3
 *  0  1  2  3
 * +--+--+--+--+
 * |10|32|54|76|
 * +--+--+--+--+
 * </pre>
 * 
 * @author ZhangXi
 * @date 2023年7月28日
 */
public interface LittleEndianDataOutput extends DataOutput {

	/** 写入short型(低高),2 Byte, 16 Bit */
	default void writeShort(short value) throws IOException {
		writeByte(value);
		writeByte(value >>> 8);
	}

	/** 写入medium型(低高),3 Byte, 24 Bit */
	default void writeMedium(int value) throws IOException {
		writeByte(value);
		writeByte(value >>> 8);
		writeByte(value >>> 16);
	}

	/** 写入integer型(低高),4 Byte, 32 Bit */
	default void writeInt(int value) throws IOException {
		writeByte(value);
		writeByte(value >>> 8);
		writeByte(value >>> 16);
		writeByte(value >>> 24);
	}

	/** 写入long型(低高),8 Byte, 64 Bit */
	default void writeLong(long value) throws IOException {
		writeByte((int) value);
		writeByte((int) (value >>> 8));
		writeByte((int) (value >>> 16));
		writeByte((int) (value >>> 24));
		writeByte((int) (value >>> 32));
		writeByte((int) (value >>> 40));
		writeByte((int) (value >>> 48));
		writeByte((int) (value >>> 56));
	}

	/** 写入float型,4 Byte, 32 Bit */
	default void writeFloat(float value) throws IOException {
		writeInt(Float.floatToRawIntBits(value));
	}

	/** 写入double型,8 Byte, 64 Bit */
	default void writeDouble(double value) throws IOException {
		writeLong(Double.doubleToRawLongBits(value));
	}
}