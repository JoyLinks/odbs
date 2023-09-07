/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
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