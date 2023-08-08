package com.joyzl.codec;

import java.io.IOException;
import java.io.InputStream;

public class BigEndianInputStream extends InputStream implements BigEndianDataInput {

	private final InputStream input;

	public BigEndianInputStream(InputStream in) {
		input = in;
	}

	@Override
	public byte readByte() throws IOException {
		return (byte) input.read();
	}

	@Override
	public int read() throws IOException {
		return input.read();
	}
}