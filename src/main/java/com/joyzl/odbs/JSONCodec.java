/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.odbs;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * JSON 读取辅助类
 * 
 * <pre>
 * JSON5 https://spec.json5.org
 * 键名允许没有引号 key:"value"
 * 允许最后的键值对或数组元素尾部逗号 {k:v,}[1,]
 * 允许字符串续行 \+0A \0D \2028 \2029
 * 允许常量 IEEE754 Infinity -Infinity NaN
 * 允许注释
 * </pre>
 * 
 * @author ZhangXi
 * @date 2020年8月22日
 */
final class JSONCodec {

	final static String NULL = "null";

	final static char SPACE = ' ';
	final static char ESCAPE = '\\';
	final static char QUOTE = '\'';
	final static char QUOTES = '"';
	final static char COLON = ':';
	final static char COMMA = ',';
	final static char ARRAY_END = ']';
	final static char ARRAY_BEGIN = '[';
	final static char OBJECT_END = '}';
	final static char OBJECT_BEGIN = '{';

	public static JSONCodec instence(ODBSJson oj, Reader reader) {
		JSONCodec JR = new JSONCodec(oj);
		JR.setReader(reader);
		return JR;
	}

	public static JSONCodec instence(ODBSJson oj, Writer writer) {
		JSONCodec JR = new JSONCodec(oj);
		JR.setWriter(writer);
		return JR;
	}

	////////////////////////////////////////////////////////////////////////////////

	private final StringBuilder sb = new StringBuilder();
	private Reader reader;
	private Writer writer;

	/** 当前中断是否有双引号包围 */
	private boolean quotes;
	/** 当前读取的字符 */
	private int c;

	JSONCodec(ODBSJson oj) {
		// 获取独立的格式化实例，确保多线程安全

	}

	public void setReader(Reader r) {
		reader = r;
		c = -1;
	}

	public void setWriter(Writer w) {
		writer = w;
		c = -1;
	}

	public boolean hasString() {
		return sb.length() > 0;
	}

	public String getString() {
		return sb.toString();
	}

	public CharSequence getCharSequence() {
		return sb;
	}

	public char lastChar() {
		return (char) c;
	}

	public int lastCode() {
		return c;
	}

	/**
	 * 判断最后字符是否结构标记<br>
	 * 六个结构标记{}[],:
	 */
	public boolean isStructure() {
		return isStructure(c);
	}

	/** 是否结构字符 */
	public boolean isStructure(int c) {
		return c == OBJECT_BEGIN || c == OBJECT_END || c == ARRAY_BEGIN || c == ARRAY_END || c == COMMA || c == COLON;
	}

	/** 是否结构字符'{' */
	public boolean isObjectBegin() {
		return c == OBJECT_BEGIN;
	}

	/** 是否结构字符'[' */
	public boolean isArrayBegin() {
		return c == ARRAY_BEGIN;
	}

	/** 是否结构字符'}' */
	public boolean isObjectEnd() {
		return c == OBJECT_END;
	}

	/** 是否结构字符']' */
	public boolean isArrayEnd() {
		return c == ARRAY_END;
	}

	/** 是否结构字符',' */
	public boolean isComma() {
		return c == COMMA;
	}

	/** 是否结构字符':' */
	public boolean isColon() {
		return c == COLON;
	}

	public boolean getBoolean() {
		return "true".contentEquals(sb);
	}

	public int getInt() {
		if (is0x()) {
			return Integer.parseInt(sb, 2, sb.length(), 16);
		}
		return Integer.parseInt(sb, 0, sb.length(), 10);
	}

	public long getLong() {
		if (is0x()) {
			return Long.parseLong(sb, 2, sb.length(), 16);
		}
		return Long.parseLong(sb, 0, sb.length(), 10);
	}

	boolean is0x() {
		if (sb.length() > 2) {
			if (sb.charAt(0) == '0') {
				if (sb.charAt(1) == 'x' || sb.charAt(1) == 'X') {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isNull() {
		if (quotes) {
			// 引号包围则不能是null常量
			// "null"
			return false;
		}
		// 判断最后一次读取值是否为null
		if (sb.length() == 4) {
			// 容错:忽略大小写
			if (sb.charAt(0) != 'n' && sb.charAt(0) != 'N')
				return false;
			if (sb.charAt(1) != 'u' && sb.charAt(1) != 'U')
				return false;
			if (sb.charAt(2) != 'l' && sb.charAt(2) != 'L')
				return false;
			if (sb.charAt(3) != 'l' && sb.charAt(3) != 'L')
				return false;
			return true;
		}
		return false;
	}

	////////////////////////////////////////////////////////////////////////////////

	/**
	 * 读取键名，有效字符串均为合法键
	 */
	public boolean readKey() throws IOException {
		// JSON 有效键永远是 ':'结束，没有键时可能的结束'}'
		// 数组对象通过']'和'}'结束返回后可能会残留','
		// 可能有双引号，可能没有(JSON5)

		if (readSkip() < 0) {
			return false;
		}
		if (isStructure(c)) {
			return false;
		}

		sb.setLength(0);
		if (c == QUOTE || c == QUOTES) {
			int quote = c;
			quotes = true;
			// 键有引号
			while ((c = reader.read()) >= 0) {
				if (c == quote) {
					if (readSkip() > 0) {
						if (c == COLON) {
							return true;
						}
						throw new EOFException("键语法错误 " + sb.toString());
					} else {
						// 意外结束
						break;
					}
				}
				if (c == ESCAPE) {
					c = readEscape();
				}
				sb.append((char) c);
			}
		} else {
			sb.append((char) c);
			quotes = false;
			// 键无引号
			while ((c = reader.read()) >= 0) {
				if (c == COLON) {
					return true;
				}
				if (Character.isWhitespace(c)) {
					if (readSkip() > 0) {
						if (c == COLON) {
							return true;
						}
						throw new EOFException("键语法错误 " + sb.toString());
					} else {
						// 意外结束
						break;
					}
				}
				if (c == ESCAPE) {
					c = readEscape();
				}
				sb.append((char) c);
			}
		}
		throw new EOFException("键意外结束 " + sb.toString());
	}

	/**
	 * 读取值（常量、数值、字符串、NULL），不能读取对象和数组
	 * 
	 * @return true 成功读取值
	 */
	public boolean readValue() throws IOException {
		// JSON 值不能省略
		// 常量 true false null
		// 数值 0 10 -10 10.8 .82 e+2 e-4 0xA 0Xa -0xA
		// IEEE754 Infinity -Infinity NaN
		// 字符串"",转义字符,续行符

		// 对象{}或数组[]不是值
		// 可能的结束标志 ,]}

		if (readSkip() < 0) {
			return false;
		}
		if (isStructure(c)) {
			return false;
		}

		// {key:value}
		// {key:value,}
		// {{key:value}}
		// {{key:value},}
		// [value]
		// [value,]
		// [[value]]
		// [[value],]

		sb.setLength(0);
		if (c == QUOTE || c == QUOTES) {
			int quote = c;
			quotes = true;
			// 值有引号
			while ((c = reader.read()) >= 0) {
				if (c == quote) {
					if (readSkip() > 0) {
						if (c == COMMA) {
							return true;
						}
						if (c == OBJECT_END || c == ARRAY_END) {
							return true;
						}
						throw new EOFException("值语法错误 " + sb.toString());
					} else {
						// 意外结束
						break;
					}
				}
				if (c == ESCAPE) {
					c = readEscape();
				}
				sb.append((char) c);
			}
		} else {
			sb.append((char) c);
			quotes = false;
			// 值无引号
			while ((c = reader.read()) >= 0) {
				if (c == COMMA) {
					return true;
				}
				if (c == OBJECT_END || c == ARRAY_END) {
					return true;
				}
				if (Character.isWhitespace(c)) {
					if (readSkip() > 0) {
						if (c == COMMA) {
							return true;
						}
						if (c == OBJECT_END || c == ARRAY_END) {
							return true;
						}
						throw new EOFException("值语法错误 " + sb.toString());
					} else {
						// 意外结束
						break;
					}
				}
				if (c == ESCAPE) {
					c = readEscape();
				}
				sb.append((char) c);
			}
		}
		throw new EOFException("值意外结束 " + sb.toString());
	}

	/**
	 * 跳过空白字符直到非空白字符，忽略注释
	 * 
	 * @return 最后一个非空白字符/-1流结束
	 */
	public int readSkip() throws IOException {
		while ((c = reader.read()) >= 0) {
			if (Character.isWhitespace(c)) {
				continue;
			}
			if (c == '/') {
				// 忽略注释
				c = reader.read();
				if (c == '/') {
					// 单行注释 //...\r\n
					while ((c = reader.read()) >= 0) {
						if (c == '\r' || c == '\n' || c == 0x2028 || c == 0x2029) {
							break;
						}
					}
					continue;
				} else if (c == '*') {
					// 多行注释 /*...*/
					while ((c = reader.read()) >= 0) {
						if (c == '*') {
							c = reader.read();
							if (c == '/') {
								break;
							}
						}
					}
					continue;
				} else {
					// 意外情况，在键值之外读取到非法的 '/'
					throw new IOException("意外的字符'/'");
				}
			}
			return c;
		}
		return -1;
	}

	/**
	 * 读取并忽略当前值
	 */
	public void readIgnore() throws IOException {
		// 忽略数组中单个值
		// 忽略键值对中的值
		// 忽略整个键值对

		// 忽略整个数组
		// 忽略整个对象

		if (readSkip() < 0) {
			return;
		}
		if (c == COMMA || c == OBJECT_END || c == ARRAY_END) {
			return;
		}

		if (c == ARRAY_BEGIN || c == OBJECT_BEGIN) {
			int tag = 1;
			do {
				if (readSkip() < 0) {
					return;
				}
				if (c == ARRAY_END || c == OBJECT_END) {
					tag--;
					continue;
				}
				if (c == ARRAY_BEGIN || c == OBJECT_BEGIN) {
					tag++;
					continue;
				}
				if (readIgnoreField() < 0) {
					return;
				}
				if (c == ARRAY_END || c == OBJECT_END) {
					tag--;
				}
			} while (tag > 0);
		} else {
			readIgnoreField();
		}
	}

	/** 忽略值或键值 */
	int readIgnoreField() throws IOException {
		if (c == COMMA) {
			// ...],[...
			return c;
		}

		int quote = 0;
		if (c == QUOTE || c == QUOTES) {
			quote = c;
		}
		while ((c = reader.read()) > 0) {
			if (c == '\\') {
				// 简化处理转义字符
				// 防止转义的引号影响处理过程
				c = reader.read();
				if (c < 0) {
					throw new EOFException();
				}
				continue;
			}
			if (quote > 0) {
				// 键值引号结束之后才能解析结构标记
				if (c == quote) {
					quote = 0;
				}
				continue;
			}
			if (isStructure(c)) {
				return c;
			}
		}
		return -1;
	}

	/**
	 * 读取转义字符，必须在检测到转义标记字符'\'之后<br>
	 * 续行符也由'\'开始，后跟:0A 0D U+2028 U+2029
	 */
	int readEscape() throws IOException {
		c = reader.read();
		if (c < 0) {
			throw new EOFException();
		}
		switch (c) {
			case '\'': // 引号 U+0027
				return '\'';
			case '"': // 引号 U+0022
				return '"';
			case '\\': // 反斜杠 U+005C
				return '\\';
			case '/': // 斜杠 U+002F
				return '/';
			case 'b': // 退格符 U+0008
				return '\b';
			case 'f': // 换页符 U+000C
				return '\f';
			case 'n': // 换行符 U+000A
				return '\n';
			case 'r': // 回车符 U+000D
				return '\r';
			case 't': // 制表符 U+0009
				return '\t';
			case 'v': // 制表符 U+000B
				return '\u000B';
			case '0': // 空值 U+0000
				return '\u0000';
			case 0xA: // 续行
				c = reader.read();
				if (c < 0) {
					throw new EOFException();
				}
				return c;
			case 0xD: // 续行
				// 检查 \r\n 情况
				c = reader.read();
				if (c < 0) {
					throw new EOFException();
				}
				if (c == 0x0A) {
					c = reader.read();
					if (c < 0) {
						throw new EOFException();
					}
				}
				return c;
			case 0x2028, 0x2029: // 续行
				c = reader.read();
				if (c < 0) {
					throw new EOFException();
				}
				return c;
			case 'u': // \u005C
				int index = sb.length();
				c = reader.read();
				if (c < 0) {
					throw new EOFException();
				}
				sb.append((char) c);
				c = reader.read();
				if (c < 0) {
					throw new EOFException();
				}
				sb.append((char) c);
				c = reader.read();
				if (c < 0) {
					throw new EOFException();
				}
				sb.append((char) c);
				c = reader.read();
				if (c < 0) {
					throw new EOFException();
				}
				sb.append((char) c);
				index = Integer.parseInt(sb, index, index + 4, 16);
				sb.setLength(sb.length() - 4);
				return index;
			default:
				if (c > 9) {
					// JSON5
					// 允许其它字符自己转义自己
					return c;
				}
				throw new IOException("意外转义字符：" + (char) c);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	/** 输出结构标记{}[] */
	public void writeTag(char tag) throws IOException {
		if (tag == OBJECT_BEGIN || c == ARRAY_BEGIN) {
			if (c > 0 && c != COLON && c != ARRAY_BEGIN) {
				// 值在键之后和数组开始时没有前逗号
				writer.write(COMMA);
			}
		}

		writer.write(tag);
		c = tag;
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(Boolean key) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			sb.setLength(0);
			sb.append(key.booleanValue());
			writeBaseKey(sb);
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(Byte key) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			sb.setLength(0);
			sb.append(key.byteValue());
			writeBaseKey(sb);
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(Short key) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			sb.setLength(0);
			sb.append(key.shortValue());
			writeBaseKey(sb);
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(Integer key) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			sb.setLength(0);
			sb.append(key.intValue());
			writeBaseKey(sb);
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(Character key) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			sb.setLength(0);
			sb.append(key.charValue());
			writeBaseKey(sb);
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(Long key) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			sb.setLength(0);
			sb.append(key.longValue());
			writeBaseKey(sb);
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(Float key, NumberFormat format) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			// sb.setLength(0);
			// sb.append(key.floatValue());
			writeBaseKey(format.format(key.floatValue()));
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(Double key, NumberFormat format) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			// sb.setLength(0);
			// sb.append(key.doubleValue());
			writeBaseKey(format.format(key.doubleValue()));
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(BigDecimal key) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			writeBaseKey(key.toPlainString());
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(Date key, DateFormat format) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			writeBaseKey(format.format(key));
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(LocalDate key, DateTimeFormatter formatter) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			sb.setLength(0);
			formatter.formatTo(key, sb);
			writeBaseKey(sb);
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(LocalTime key, DateTimeFormatter formatter) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			sb.setLength(0);
			formatter.formatTo(key, sb);
			writeBaseKey(sb);
		}
	}

	/** 输出基础值键名，通常是MAP键 */
	public void writeKey(LocalDateTime key, DateTimeFormatter formatter) throws IOException {
		if (key == null) {
			writeBaseKey(NULL);
		} else {
			sb.setLength(0);
			formatter.formatTo(key, sb);
			writeBaseKey(sb);
		}
	}

	/** 输出基础值键名，双引号包围，无转义 */
	private void writeBaseKey(CharSequence key) throws IOException {
		if (c != OBJECT_BEGIN) {
			// 只要不是对象开始，键之前必须有逗号
			// {"key":"value","key":"value"}
			// 键不会出现在数组中,只会出现在对象中
			writer.write(COMMA);
		}

		if (key == NULL) {
			writer.append(NULL);
		} else {
			writer.write(QUOTES);
			writer.append(key);
			writer.write(QUOTES);
		}
		writer.write(c = COLON);
	}

	/** 输出常规键名，执行转义 */
	public void writeKey(CharSequence key, boolean quots) throws IOException {
		if (c != OBJECT_BEGIN) {
			// 只要不是对象开始，键之前必须有逗号
			// {"key":"value","key":"value"}
			// 键不会出现在数组中,只会出现在对象中
			writer.write(COMMA);
		}

		// 前后双引号
		if (quots) {
			// 输出有双引号的键名
			writer.write(QUOTES);
			if (key == null) {
				writer.write(NULL);
			} else {
				writeEscape(key);
			}
			writer.write(QUOTES);
		} else {
			// 输出无双引号的键名
			if (key == null) {
				writer.write(NULL);
			} else {
				writeEscape(key);
			}
		}
		writer.write(c = COLON);
	}

	/** 输出字符串（须转义） */
	public void writeValue(CharSequence value) throws IOException {
		if (c != COLON && c != ARRAY_BEGIN) {
			// 值在键之后和数组开始时没有前逗号
			// ["a","b"]
			// {"key":"value","key":"value"}
			writer.write(COMMA);
		}

		if (value == null) {
			writer.write(NULL);
		} else {
			writer.write(QUOTES);
			writeEscape(value);
			writer.write(c = QUOTES);
		}
	}

	/** 输出值（无转义） */
	public void writeValue(boolean value) throws IOException {
		sb.setLength(0);
		sb.append(value);
		writeBaseValue(sb);
	}

	/** 输出值（无转义） */
	public void writeValue(byte value) throws IOException {
		sb.setLength(0);
		sb.append(value);
		writeBaseValue(sb);
	}

	/** 输出值（无转义） */
	public void writeValue(char value) throws IOException {
		sb.setLength(0);
		sb.append(QUOTES);
		sb.append(value);
		sb.append(QUOTES);
		writeBaseValue(sb);
	}

	/** 输出值（无转义） */
	public void writeValue(short value) throws IOException {
		sb.setLength(0);
		sb.append(value);
		writeBaseValue(sb);
	}

	/** 输出值（无转义） */
	public void writeValue(int value) throws IOException {
		sb.setLength(0);
		sb.append(value);
		writeBaseValue(sb);
	}

	/** 输出值（无转义） */
	public void writeValue(long value) throws IOException {
		sb.setLength(0);
		sb.append(value);
		writeBaseValue(sb);
	}

	/** 输出值（无转义） */
	public void writeValue(float value, NumberFormat format) throws IOException {
		// sb.setLength(0);
		// sb.append(value);
		writeBaseValue(format.format(value));
	}

	/** 输出值（无转义） */
	public void writeValue(double value, NumberFormat format) throws IOException {
		// sb.setLength(0);
		// sb.append(value);
		writeBaseValue(format.format(value));
	}

	/** 输出值（无转义） */
	public void writeValue(Boolean value) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(value.booleanValue());
			writeBaseValue(sb);
		}
	}

	/** 输出值（无转义） */
	public void writeValue(Byte value) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(value.byteValue());
			writeBaseValue(sb);
		}
	}

	/** 输出值（无转义） */
	public void writeValue(Short value) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(value.shortValue());
			writeBaseValue(sb);
		}
	}

	/** 输出值（无转义） */
	public void writeValue(Integer value) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(value.intValue());
			writeBaseValue(sb);
		}
	}

	/** 输出值（无转义） */
	public void writeValue(Long value) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(value.longValue());
			writeBaseValue(sb);
		}
	}

	/** 输出值（无转义） */
	public void writeValue(Float value, NumberFormat format) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			// sb.setLength(0);
			// sb.append(value.floatValue());
			writeBaseValue(format.format(value.floatValue()));
		}
	}

	/** 输出值（无转义） */
	public void writeValue(Double value, NumberFormat format) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			// sb.setLength(0);
			// sb.append(value.floatValue());
			writeBaseValue(format.format(value.doubleValue()));
		}
	}

	/** 输出值（无转义） */
	public void writeValue(BigDecimal value) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			writeBaseValue(value.toPlainString());
		}
	}

	/** 输出值 */
	public void writeValue(Character value) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(QUOTES);
			escape(value.charValue(), sb);
			sb.append(QUOTES);
			writeBaseValue(sb);
		}
	}

	/** 输出值 */
	public void writeValue(Date value, DateFormat format) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(QUOTES);
			sb.append(format.format(value));
			sb.append(QUOTES);
			writeBaseValue(sb);
		}
	}

	/** 输出值 */
	public void writeValue(LocalDate value, DateTimeFormatter formatter) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(QUOTES);
			formatter.formatTo(value, sb);
			sb.append(QUOTES);
			writeBaseValue(sb);
		}
	}

	/** 输出值 */
	public void writeValue(LocalTime value, DateTimeFormatter formatter) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(QUOTES);
			formatter.formatTo(value, sb);
			sb.append(QUOTES);
			writeBaseValue(sb);
		}
	}

	/** 输出值 */
	public void writeValue(LocalDateTime value, DateTimeFormatter formatter) throws IOException {
		if (value == null) {
			writeBaseValue(NULL);
		} else {
			sb.setLength(0);
			sb.append(QUOTES);
			formatter.formatTo(value, sb);
			sb.append(QUOTES);
			writeBaseValue(sb);
		}
	}

	/** 输出值 null */
	public void writeValue() throws IOException {
		writeBaseValue(NULL);
	}

	/** 输出值（无转义） */
	private void writeBaseValue(CharSequence value) throws IOException {
		if (c != COLON && c != ARRAY_BEGIN) {
			// 值在键之后和数组开始时没有前逗号
			// [1,2]
			// {"key":value,"key":value}
			writer.write(COMMA);
		}

		writer.append(value);
		c = SPACE;
	}

	private void writeEscape(CharSequence value) throws IOException {
		for (int i = 0; i < value.length(); i++) {
			escape(value.charAt(i), writer);
		}
	}

	private void escape(char value, Appendable writer) throws IOException {
		// 转义字符 '"'='\"'
		// 转义 \" \\ \/ \b \f \n \r \t \u0000
		switch (value) {
			case QUOTE:
				writer.append(ESCAPE);
				writer.append('\'');
				break;
			case QUOTES:
				writer.append(ESCAPE);
				writer.append('"');
				break;
			case '\\':
				writer.append(ESCAPE);
				writer.append('\\');
				break;
			case '/':
				// 20200928 JSON.parse() 不支持斜杠 '\/' 转义
				writer.append(ESCAPE);
				writer.append('/');
				break;
			case '\b':
				writer.append(ESCAPE);
				writer.append('b');
				break;
			case '\f':
				writer.append(ESCAPE);
				writer.append('f');
				break;
			case '\n':
				writer.append(ESCAPE);
				writer.append('n');
				break;
			case '\r':
				writer.append(ESCAPE);
				writer.append('r');
				break;
			case '\t':
				writer.append(ESCAPE);
				writer.append('t');
				break;
			case 0x000B:
				writer.append(ESCAPE);
				writer.append('v');
				break;
			case 0:
				// '\0' NUL 字符导致JSON 失败
				// 大部分运行环境视为字符结束，后续字符不再输出
				writer.append(ESCAPE);
				writer.append('0');
				break;
			default:
				writer.append(value);
		}
	}

	@Override
	public String toString() {
		return (char) c + " " + sb.toString();
	}
}