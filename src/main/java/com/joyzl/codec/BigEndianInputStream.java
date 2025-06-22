/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
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