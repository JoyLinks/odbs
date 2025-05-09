/*-
 * www.joyzl.net
 * 重庆骄智科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.codec;

import java.io.IOException;

/**
 * 字节解码为数据，小端序(LITTLE_ENDIAN)
 * 
 * <pre>
 * VALUE: 0x76543210
 * READS: 0 ~ 3
 *  0  1  2  3
 * +--+--+--+--+
 * |10|32|54|76|
 * +--+--+--+--+
 * </pre>
 * 
 * @author ZhangXi
 * @date 2023年7月28日
 */
public interface LittleEndianDataInput extends DataInput {

	/** 读取short型(低高),2 Byte, 16 Bit */
	default short readShort() throws IOException {
		return (short) (readByte() & 0xFF | readByte() << 8);
	}

	/** 读取short型(无符号,低高),2 Byte, 16 Bit */
	default int readUnsignedShort() throws IOException {
		return readShort() & 0xFFFF;
	}

	/** 读取medium型(低高),3 Byte, 24 Bit */
	default int readMedium() throws IOException {
		return readByte() & 0xFF | (readByte() & 0xFF) << 8 | (readByte() & 0xFF) << 16;
	}

	/** 读取medium型(无符号,低高),3 Byte, 24 Bit */
	default int readUnsignedMedium() throws IOException {
		return readMedium() & 0xFFFFFF;
	}

	/** 读取integer型(低高),4 Byte, 32 Bit */
	default int readInt() throws IOException {
		return readByte() & 0xFF | (readByte() & 0xFF) << 8 | (readByte() & 0xFF) << 16 | (readByte() & 0xFF) << 24;
	}

	/** 读取integer型(无符号,低高),4 Byte, 32 Bit */
	default long readUnsignedInt() throws IOException {
		return readInt() & 0xFFFFFFFFL;
	}

	/** 读取long型(低高),8 Byte, 64 Bit */
	default long readLong() throws IOException {
		return (long) readByte() & 0xFF | ((long) readByte() & 0xFF) << 8 | ((long) readByte() & 0xFF) << 16 | ((long) readByte() & 0xFF) << 24 | ((long) readByte() & 0xFF) << 32 | ((long) readByte() & 0xFF) << 40 | ((long) readByte() & 0xFF) << 48 | ((long) readByte() & 0xFF) << 56;
	}

	/** 读取float型(IEEE754),4 Byte, 32 Bit */
	default float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	/** 读取double型(IEEE754),8 Byte, 64 Bit */
	default double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}
}