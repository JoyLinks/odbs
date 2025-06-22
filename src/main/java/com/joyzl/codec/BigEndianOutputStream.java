/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
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