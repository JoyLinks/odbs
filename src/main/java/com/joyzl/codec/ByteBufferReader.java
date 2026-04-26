package com.joyzl.codec;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferReader extends InputStream implements UTF8Reader {

	private final ByteBuffer buffer;

	public ByteBufferReader(ByteBuffer b) {
		buffer = b;
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
}