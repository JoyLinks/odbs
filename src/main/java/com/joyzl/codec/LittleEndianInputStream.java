/*-
 * www.joyzl.net
 * 重庆骄智科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.codec;

import java.io.IOException;
import java.io.InputStream;

public class LittleEndianInputStream extends InputStream implements LittleEndianDataInput {

	private final InputStream input;

	public LittleEndianInputStream(InputStream in) {
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