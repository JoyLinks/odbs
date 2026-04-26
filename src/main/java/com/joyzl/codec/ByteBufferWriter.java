package com.joyzl.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteBufferWriter extends OutputStream implements UTF8Writer {

	private final ByteBuffer buffer;

	public ByteBufferWriter(int size) {
		buffer = ByteBuffer.allocate(size);
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
}