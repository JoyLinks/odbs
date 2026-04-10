package com.joyzl.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteBufferOutputStream extends OutputStream implements LittleEndianDataOutput, BigEndianDataOutput {

	private boolean be = true;
	private final ByteBuffer buffer;

	public ByteBufferOutputStream(int size) {
		buffer = ByteBuffer.allocate(size);
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
		return buffer.position();
	}

	public void reset() {
		buffer.clear();
	}

	@Override
	public void write(int b) throws IOException {
		buffer.put((byte) b);
	}

	@Override
	public void writeByte(int b) throws IOException {
		buffer.put((byte) b);
	}

	@Override
	public void writeDouble(double value) throws IOException {
		if (be) {
			BigEndianDataOutput.super.writeDouble(value);
		} else {
			LittleEndianDataOutput.super.writeDouble(value);
		}
	}

	@Override
	public void writeFloat(float value) throws IOException {
		if (be) {
			BigEndianDataOutput.super.writeFloat(value);
		} else {
			LittleEndianDataOutput.super.writeFloat(value);
		}
	}

	@Override
	public void writeInt(int value) throws IOException {
		if (be) {
			BigEndianDataOutput.super.writeInt(value);
		} else {
			LittleEndianDataOutput.super.writeInt(value);
		}
	}

	@Override
	public void writeLong(long value) throws IOException {
		if (be) {
			BigEndianDataOutput.super.writeLong(value);
		} else {
			LittleEndianDataOutput.super.writeLong(value);
		}
	}

	@Override
	public void writeMedium(int value) throws IOException {
		if (be) {
			BigEndianDataOutput.super.writeMedium(value);
		} else {
			LittleEndianDataOutput.super.writeMedium(value);
		}
	}

	@Override
	public void writeShort(short value) throws IOException {
		if (be) {
			BigEndianDataOutput.super.writeShort(value);
		} else {
			LittleEndianDataOutput.super.writeShort(value);
		}
	}
}