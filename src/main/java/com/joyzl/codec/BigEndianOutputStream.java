/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.codec;

import java.io.IOException;
import java.io.OutputStream;

public class BigEndianOutputStream extends OutputStream implements BigEndianDataOutput {

	private final OutputStream output;

	public BigEndianOutputStream(OutputStream out) {
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