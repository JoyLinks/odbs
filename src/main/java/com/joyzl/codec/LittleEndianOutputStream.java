package com.joyzl.codec;

import java.io.IOException;
import java.io.OutputStream;

public class LittleEndianOutputStream extends OutputStream implements LittleEndianDataOutput, LittleEndianBCDOutput {

	private final OutputStream output;

	public LittleEndianOutputStream(OutputStream out) {
		output = out;
	}

	@Override
	public void write(int b) throws IOException {
		writeByte((byte) b);
	}

	@Override
	public void writeByte(int b) throws IOException {
		output.write(b);
	}
}