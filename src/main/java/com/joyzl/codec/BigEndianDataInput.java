/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.codec;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * 字节解码为数据，大端序(BIG_ENDIAN)
 * 
 * <pre>
 * VALUE: 0x76543210
 * READS: 0 ~ 3
 *  0  1  2  3
 * +--+--+--+--+
 * |76|54|32|10|
 * +--+--+--+--+
 * </pre>
 * 
 * @author ZhangXi
 * @date 2023年7月28日
 */
public interface BigEndianDataInput extends DataInput {

	/** 读取short值,2 Byte, 16 Bit */
	@Override
	default short readShort() throws IOException {
		return (short) (readByte() << 8 | readByte() & 0xFF);
	}

	/** 读取short值(无符号),2 Byte, 16 Bit */
	@Override
	default int readUnsignedShort() throws IOException {
		return readShort() & 0xFFFF;
	}

	/** 读取medium值,3 Byte, 24 Bit */
	default int readMedium() throws IOException {
		return (readByte() & 0xFF) << 16 | (readByte() & 0xFF) << 8 | readByte() & 0xFF;
	}

	/** 读取medium值(无符号),3 Byte, 24 Bit */
	default int readUnsignedMedium() throws IOException {
		return readMedium() & 0xFFFFFF;
	}

	/** 读取integer值,4 Byte, 32 Bit */
	@Override
	default int readInt() throws IOException {
		return (readByte() & 0xFF) << 24 | (readByte() & 0xFF) << 16 | (readByte() & 0xFF) << 8 | readByte() & 0xFF;
	}

	/** 读取integer值(无符号),4 Byte, 32 Bit */
	default long readUnsignedInt() throws IOException {
		return readInt() & 0xFFFFFFFFL;
	}

	/** 读取long值,8 Byte, 64 Bit */
	@Override
	default long readLong() throws IOException {
		return ((long) readByte() & 0xFF) << 56 | ((long) readByte() & 0xFF) << 48 | ((long) readByte() & 0xFF) << 40 | ((long) readByte() & 0xFF) << 32 | ((long) readByte() & 0xFF) << 24 | ((long) readByte() & 0xFF) << 16 | ((long) readByte() & 0xFF) << 8 | (long) readByte() & 0xFF;
	}

	/** 读取float值(IEEE754),4 Byte, 32 Bit */
	@Override
	default float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	/** 读取double值(IEEE754),8 Byte, 64 Bit */
	@Override
	default double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	/** 读取long double值(IEEE754 Binary128),16 Byte */
	default BigDecimal readLongDouble() throws IOException {
		// TODO 未来支持
		throw new UnsupportedOperationException();
	}
}