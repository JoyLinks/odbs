package com.joyzl.codec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream implements LittleEndianDataInput, BigEndianDataInput {

	private boolean be = true;
	private final ByteBuffer buffer;

	public ByteBufferInputStream(ByteBuffer b) {
		buffer = b;
	}

	/** 切换为大端编码格式 */
	public void bigEndian() {
		be = true;
	}

	/** 切换为小端编码格式 */
	public void littleEndian() {
		be = false;
	}

	public ByteBuffer buffer() {
		return buffer;
	}

	public int size() {
		return buffer.remaining();
	}

	public void reset() {
		buffer.position(0);
	}

	@Override
	public int read() throws IOException {
		if (buffer.hasRemaining()) {
			return buffer.get() & 0xFF;
		}
		return -1;
	}

	@Override
	public byte readByte() throws IOException {
		return buffer.get();
	}

	@Override
	public double readDouble() throws IOException {
		if (be) {
			return BigEndianDataInput.super.readDouble();
		} else {
			return LittleEndianDataInput.super.readDouble();
		}
	}

	@Override
	public float readFloat() throws IOException {
		if (be) {
			return BigEndianDataInput.super.readFloat();
		} else {
			return LittleEndianDataInput.super.readFloat();
		}
	}

	@Override
	public int readInt() throws IOException {
		if (be) {
			return BigEndianDataInput.super.readInt();
		} else {
			return LittleEndianDataInput.super.readInt();
		}
	}

	@Override
	public long readLong() throws IOException {
		if (be) {
			return BigEndianDataInput.super.readLong();
		} else {
			return LittleEndianDataInput.super.readLong();
		}
	}

	@Override
	public int readMedium() throws IOException {
		if (be) {
			return BigEndianDataInput.super.readMedium();
		} else {
			return LittleEndianDataInput.super.readMedium();
		}
	}

	@Override
	public short readShort() throws IOException {
		if (be) {
			return BigEndianDataInput.super.readShort();
		} else {
			return LittleEndianDataInput.super.readShort();
		}
	}

	@Override
	public long readUnsignedInt() throws IOException {
		if (be) {
			return BigEndianDataInput.super.readUnsignedInt();
		} else {
			return LittleEndianDataInput.super.readUnsignedInt();
		}
	}

	@Override
	public int readUnsignedMedium() throws IOException {
		if (be) {
			return BigEndianDataInput.super.readUnsignedMedium();
		} else {
			return LittleEndianDataInput.super.readUnsignedMedium();
		}
	}

	@Override
	public int readUnsignedShort() throws IOException {
		if (be) {
			return BigEndianDataInput.super.readUnsignedShort();
		} else {
			return LittleEndianDataInput.super.readUnsignedShort();
		}
	}
}