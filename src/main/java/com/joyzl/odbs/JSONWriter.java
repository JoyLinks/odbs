package com.joyzl.odbs;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Supplier;

public final class JSONWriter extends JSONCodec {

	private final static ThreadLocal<JSONWriter> JSON_WRITER = ThreadLocal.withInitial(new Supplier<JSONWriter>() {
		@Override
		public JSONWriter get() {
			return new JSONWriter();
		}
	});

	public final static JSONWriter instance(Writer writer) {
		final JSONWriter w = JSON_WRITER.get();
		w.writer = writer;
		w.level = 0;
		return w;
	}

	private Writer writer;
	private int level;

	private JSONWriter() {
	}

	public void beginArray() throws IOException {
		writeValueComma();
		writer.write(JSONCodec.ARRAY_BEGIN);
		level = level << 2;
	}

	public void endArray() throws IOException {
		writer.write(JSONCodec.ARRAY_END);
		level = level >>> 2;
	}

	public void beginObject() throws IOException {
		writeValueComma();
		writer.write(JSONCodec.OBJECT_BEGIN);
		level = level << 2;
	}

	public void endObject() throws IOException {
		writer.write(JSONCodec.OBJECT_END);
		level = level >>> 2;
	}

	private void writeKeyComma() throws IOException {
		// 每2位表示结构层级是否输出过值
		// 00 未输出键和值 01 已输出键 10 已输出值
		// 重复调用不影响结果因此可在任意需要输出逗号的位置判定

		int v = level & 3;
		if (v == 1) {
			writer.write(JSONCodec.COMMA);
		} else if (v == 0) {
			level += 1;
		}
	}

	private void writeValueComma() throws IOException {
		// 每2位表示结构层级是否输出过值
		// 00 未输出键和值 01 已输出键 10 已输出值
		// 重复调用不影响结果因此可在任意需要输出逗号的位置判定

		int v = level & 3;
		if (v == 2) {
			writer.write(JSONCodec.COMMA);
		} else if (v == 0) {
			level += 2;
		}
	}

	/** "key": */
	public void writeKey(CharSequence value) throws IOException {
		writeKeyComma();
		writer.write(JSONCodec.QUOTES);
		writer.append(value);
		writer.write(JSONCodec.QUOTES);
		writer.write(JSONCodec.COLON);
	}

	/** value */
	public void writeValue(int value) throws IOException {
		writeValueComma();
		builder.setLength(0);
		builder.append(value);
		writer.append(builder);
	}

	/** value */
	public void writeValue(long value) throws IOException {
		writeValueComma();
		builder.setLength(0);
		builder.append(value);
		writer.append(builder);
	}

	/** value */
	public void writeValue(float value) throws IOException {
		writeValueComma();
		builder.setLength(0);
		builder.append(value);
		writer.append(builder);
	}

	/** value */
	public void writeValue(double value) throws IOException {
		writeValueComma();
		builder.setLength(0);
		builder.append(value);
		writer.append(builder);
	}

	/** value */
	public void writeValue(boolean value) throws IOException {
		writeValueComma();
		builder.setLength(0);
		builder.append(value);
		writer.append(builder);
	}

	/** "value" */
	public void writeValue(char value) throws IOException {
		writeValueComma();
		writer.write(JSONCodec.QUOTES);
		writeEscape(value);
		writer.write(JSONCodec.QUOTES);
	}

	/** value */
	public void writeValue(BigInteger value) throws IOException {
		writeValueComma();
		writer.write(value.toString());
	}

	/** value */
	public void writeValue(BigDecimal value) throws IOException {
		writeValueComma();
		writer.write(value.toString());
	}

	/** "value" */
	public void writeValue(DateFormat format, Date value) throws IOException {
		writeValueComma();
		writer.write(JSONCodec.QUOTES);
		synchronized (format) {
			writer.write(format.format(value));
		}
		writer.write(JSONCodec.QUOTES);
	}

	/** "value" */
	public void writeValue(DateTimeFormatter format, LocalDate value) throws IOException {
		writeValueComma();
		writer.write(JSONCodec.QUOTES);
		format.formatTo(value, writer);
		writer.write(JSONCodec.QUOTES);
	}

	/** "value" */
	public void writeValue(DateTimeFormatter format, LocalTime value) throws IOException {
		writeValueComma();
		writer.write(JSONCodec.QUOTES);
		format.formatTo(value, writer);
		writer.write(JSONCodec.QUOTES);
	}

	/** "value" */
	public void writeValue(DateTimeFormatter format, LocalDateTime value) throws IOException {
		writeValueComma();
		writer.write(JSONCodec.QUOTES);
		format.formatTo(value, writer);
		writer.write(JSONCodec.QUOTES);
	}

	/** "value" */
	public void writeValue(CharSequence value) throws IOException {
		writeValueComma();
		writer.write(JSONCodec.QUOTES);
		for (int i = 0; i < value.length(); i++) {
			writeEscape(value.charAt(i));
		}
		writer.write(JSONCodec.QUOTES);
	}

	private void writeEscape(char value) throws IOException {
		// 转义 \" \\ \/ \b \f \n \r \t \u0000
		if (value < 0x20) {
			// 所有控制字符须转义
			writer.append(ESCAPE);
			switch (value) {
				case '\b':
					writer.append('b');
					break;
				case '\f':
					writer.append('f');
					break;
				case '\n':
					writer.append('n');
					break;
				case '\r':
					writer.append('r');
					break;
				case '\t':
					// 水平制表符
					writer.append('t');
					break;
				case '\u000B':
					// \v 垂直制表符 JSON5
					writer.append('v');
					break;
				default:
					// 十六进制 \u0000
					writer.append('u');
					if (value < 0x10) {
						writer.append('0');
						writer.append('0');
						writer.append('0');
						writer.append(Character.forDigit(value, 16));
					} else {
						writer.append('0');
						writer.append('0');
						writer.append('1');
						writer.append(Character.forDigit(value - 0x10, 16));
					}
			}
		} else if (value == QUOTE) {
			// \'
			writer.append(ESCAPE);
			writer.append(QUOTE);
		} else if (value == QUOTES) {
			// \"
			writer.append(ESCAPE);
			writer.append(QUOTES);
		} else if (value == ESCAPE) {
			// \\
			writer.append(ESCAPE);
			writer.append(ESCAPE);
		} else if (value == '/') {
			// \/
			// 20200928
			// JSON.parse() 不支持斜杠 '\/' 转义
			writer.append(ESCAPE);
			writer.append('/');
		} else {
			writer.append(value);
		}
	}
}