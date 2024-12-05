/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.NumberFormat;

/**
 * JSON 读取辅助类
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

	/** 日期时间格式 */
	final DateFormat DATE_FORMAT;
	/** 数字格式化 */
	final NumberFormat NUMBER_FORMAT;

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
	private final ODBSJson odbs_json;
	private Reader reader;
	private Writer writer;

	/** 当前值是否有双引号包围 */
	private int quotes;
	/** 当前索引位置 */
	private int index;
	/** 当前读取的字符 */
	private int c;

	JSONCodec(ODBSJson oj) {
		odbs_json = oj;
		// 获取独立的格式化实例，确保多线程安全
		DATE_FORMAT = oj.getDateFormat();
		NUMBER_FORMAT = oj.getNumberFormat();
	}

	public void setReader(Reader r) {
		reader = r;
		c = -1;
	}

	public void setWriter(Writer w) {
		writer = w;
		c = -1;
	}

	/**
	 * 获取当前索引
	 */
	public int getIndex() {
		return index;
	}

	public boolean isNull() {
		if (quotes > 0) {
			// 区分null和"null"
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

	public int lastValue() {
		return c;
	}

	/**
	 * 判断最后字符是否结构标记<br>
	 * 六个结构标记{}[],:
	 */
	public boolean lastIsStructure() {
		switch (c) {
			case JSONCodec.OBJECT_BEGIN:
			case JSONCodec.OBJECT_END:
			case JSONCodec.ARRAY_BEGIN:
			case JSONCodec.ARRAY_END:
			case JSONCodec.COMMA:
			case JSONCodec.COLON:
				return true;
			default:
				return false;
		}
	}

	/**
	 * 读取键名，有效字符串均为合法键
	 */
	public boolean readKey() throws IOException {
		// JSON有效键永远是 ':'结束，没有键时可能的结束'}'
		// 可能有双引号，可能没有
		// 数组对象通过']'和'}'结束返回后可能会残留','
		// "" "..."

		if (lastIsStructure()) {
			// 最后字符如果是结构字符须丢弃
			// 结构字符不能包括在键名中
			sb.setLength(0);
			quotes = 0;
		} else {
			sb.setLength(0);
			if (c == JSONCodec.QUOTES) {
				quotes = 1;
			} else {
				sb.append((char) c);
				quotes = 0;
			}
		}

		while ((c = reader.read()) >= 0) {
			index++;
			if (Character.isWhitespace(c)) {
				if (sb.length() == 0) {
					// 忽略前空白
					continue;
				} else {
					// 允许键中空白
				}
			} else if (c == JSONCodec.QUOTES) {
				quotes++;
				continue;
			} else if (c == JSONCodec.COMMA) {
				if (sb.length() == 0) {
					// 忽略键前逗号
					continue;
				} else {
					// 允许键中逗号
				}
			} else if (c == JSONCodec.ESCAPE) {
				// 读取转义字符
				c = readEscape(reader);
			} else if (c == JSONCodec.COLON || c == JSONCodec.OBJECT_END) {
				// 键名结束于冒号':'
				if (quotes == 0) {
					// 键名无双引号
					return sb.length() > 0;
				} else if (quotes >= 2) {
					// 键名有双引号 ""/"..."
					return true;
				}
			}
			sb.append((char) c);
		}
		throw new EOFException();
	}

	/**
	 * 读取值
	 */
	public boolean readValue() throws IOException {
		// JSON值不能省略
		// {"key"}, {"key":}
		// 常量true false null
		// {"key":true}, {"key":null}
		// 数值 0 10 -10 10.8 e+2 e-4
		// {"key":0,"key":10.8}
		// 字符串"",转义字符
		// {"key":"","key":null,"key":"null","key":"TEXT"}
		// 值不能是对象{} 列表[]
		// 可能的结束标志 ,]}

		if (lastIsStructure()) {
			sb.setLength(0);
			quotes = 0;
		} else {
			sb.setLength(0);
			if (c == JSONCodec.QUOTES) {
				quotes = 1;
			} else {
				sb.append((char) c);
				quotes = 0;
			}
		}

		while ((c = reader.read()) >= 0) {
			index++;
			if (Character.isWhitespace(c)) {
				if (sb.length() == 0) {
					// 忽略前空白
					continue;
				} else {
					// 值中空白保留
				}
				if (quotes == 2) {
					// 尾随空白过滤
					// 双引号已成对
					continue;
				}
			} else if (c == JSONCodec.QUOTES) {
				// 如果值中出现双引号应转义
				// 首尾双引号表示字符串值
				quotes++;
				continue;
			} else if (c == JSONCodec.ESCAPE) {
				// 读取转义字符
				c = readEscape(reader);
			} else if (c == JSONCodec.COMMA || c == JSONCodec.ARRAY_END || c == JSONCodec.OBJECT_END) {
				if (quotes == 0) {
					// 有效字符返回成功
					return sb.length() > 0;
				} else if (quotes >= 2) {
					// 空字符串和有效字符返回成功
					return true;
				} else {
					// 双引号之间的结束符
				}
			}
			sb.append((char) c);
		}
		throw new EOFException();
	}

	@Deprecated
	public void readString() throws IOException {
		// "name":"text",字符串值必须有引号
		// "name":null,可能有空值且无引号
		// 转义 \" \\ \/ \b \f \n \r \t \u0000
		// 可能的结束标志 空白,]}

		if (lastIsStructure()) {
			sb.setLength(0);
		} else {
			sb.setLength(0);
			sb.append((char) c);
		}

		// 跳过前空白直到'"'/有效字符
		while ((c = reader.read()) >= 0) {
			index++;
			if (Character.isWhitespace((char) c)) {
				continue;
			} else if (c == JSONCodec.QUOTES) {
				// 字符串开始,由后续代码读取并处理转义字符
				break;
			} else if (c == JSONCodec.COMMA || c == JSONCodec.ARRAY_END || c == JSONCodec.OBJECT_END) {
				// 常量结束,例如 null
				return;
			} else {
				// throw new IOException("意外字符：" + (char) c);
				sb.append((char) c);
			}
		}
		while ((c = reader.read()) >= 0) {
			index++;
			if (c == JSONCodec.QUOTES) {
				break;
			} else if (c == JSONCodec.ESCAPE) {
				c = reader.read();
				index++;
				if (c == 'b') {
					sb.append('\b');
				} else if (c == 'f') {
					sb.append('\f');
				} else if (c == 'n') {
					sb.append('\n');
				} else if (c == 'r') {
					sb.append('\r');
				} else if (c == 't') {
					sb.append('\t');
				} else if (c == 'u') {
					char[] code = new char[4];
					code[0] = (char) reader.read();
					code[1] = (char) reader.read();
					code[2] = (char) reader.read();
					code[3] = (char) reader.read();
					index += 4;
					sb.append(Integer.parseInt(new String(code), 16));
				} else {
					sb.append((char) c);
				}
			} else {
				sb.append((char) c);
			}
		}

		// 跳过后空白直到结束标志
		while ((c = reader.read()) >= 0) {
			index++;
			if (Character.isWhitespace((char) c)) {
				continue;
			} else if (c == JSONCodec.COMMA || c == JSONCodec.ARRAY_END || c == JSONCodec.OBJECT_END) {
				return;
			} else {
				throw new IOException("意外字符：" + (char) c);
			}
		}
		throw new EOFException();
	}

	/**
	 * 读取转义字符，必须在检测到转义标记字符'\'之后
	 */
	int readEscape(Reader reader) throws IOException {
		switch (reader.read()) {
			case '"': // \" 引号 U+0022
				index++;
				return '"';
			case '\\': // \\ 反斜杠 U+005C
				index++;
				return '\\';
			case '/': // \/ 斜杠 U+002F
				index++;
				return '/';
			case 'b': // \b 退格符 U+0008
				index++;
				return '\b';
			case 'f': // \f 换页符 U+000C
				index++;
				return '\f';
			case 'n': // \n 换行符 U+000A
				index++;
				return '\n';
			case 'r': // \r 回车符 U+000D
				index++;
				return '\r';
			case 't': // \t 制表符 U+0009
				index++;
				return '\t';
			case 'u': // \u005C CODE POINT
				char[] code = new char[4];
				code[0] = (char) reader.read();
				code[1] = (char) reader.read();
				code[2] = (char) reader.read();
				code[3] = (char) reader.read();
				index += 5;
				return Integer.parseInt(new String(code), 16);
			default:
				throw new IOException("意外字符：" + (char) c);
		}
	}

	/**
	 * 跳过空白字符直到非空白字符
	 * 
	 * @return 最后一个非空白字符/-1流结束
	 */
	public int readSkip() throws IOException {
		while ((c = reader.read()) >= 0) {
			index++;
			if (Character.isWhitespace((char) c)) {
				continue;
			}
			return c;
		}
		return -1;
	}

	/**
	 * 读取并忽略当前值
	 */
	public void readIgnore() throws IOException {
		switch (readSkip()) {
			case JSONCodec.ARRAY_BEGIN:
				while (lastChar() != JSONCodec.ARRAY_END) {
					readIgnore();
				}
				return;
			case JSONCodec.OBJECT_BEGIN:
				while (lastChar() != JSONCodec.OBJECT_END) {
					readIgnore();
				}
				return;
			default:
				readValue();
				return;
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	public void writeTag(char tag) throws IOException {
		if (tag == JSONCodec.OBJECT_BEGIN || c == JSONCodec.ARRAY_BEGIN) {
			if (c > 0 && c != JSONCodec.COLON && c != JSONCodec.ARRAY_BEGIN) {
				// 值在键之后和数组开始时没有前逗号
				writer.write(JSONCodec.COMMA);
				index++;
			}
		}

		writer.write(tag);
		index++;
		c = tag;
	}

	public void writeKey(String key) throws IOException {
		if (c != JSONCodec.OBJECT_BEGIN) {
			// 只要不是对象开始，键之前必须有逗号
			// {"key":"value","key":"value"}
			// 键不会出现在数组中,只会出现在对象中
			writer.write(JSONCodec.COMMA);
			index++;
		}

		// 前后双引号
		if (odbs_json.isQuotesKey()) {
			writer.write(JSONCodec.QUOTES);
			writeEscape(key);
			writer.write(JSONCodec.QUOTES);
			index += 2;
		} else {
			writeEscape(key);
		}
		writer.write(c = JSONCodec.COLON);
		index++;
	}

	public void writeString(String value) throws IOException {
		if (c != JSONCodec.COLON && c != JSONCodec.ARRAY_BEGIN) {
			// 值在键之后和数组开始时没有前逗号
			// ["a","b"]
			// {"key":"value","key":"value"}
			writer.write(JSONCodec.COMMA);
			index++;
		}

		writer.write(JSONCodec.QUOTES);
		writeEscape(value);
		writer.write(c = JSONCodec.QUOTES);
		index += 2;
	}

	public void writeValue(String value) throws IOException {
		if (c != JSONCodec.COLON && c != JSONCodec.ARRAY_BEGIN) {
			// 值在键之后和数组开始时没有前逗号
			// [1,2]
			// {"key":value,"key":value}
			writer.write(JSONCodec.COMMA);
			index++;
		}

		writer.write(value);
		index++;
		c = SPACE;
	}

	void writeEscape(String value) throws IOException {
		// 转义字符 '"'='\"'
		// 转义 \" \\ \/ \b \f \n \r \t \u0000

		sb.setLength(0);
		for (int i = 0; i < value.length(); i++) {
			switch (c = value.charAt(i)) {
				case JSONCodec.QUOTES:
					sb.append(JSONCodec.ESCAPE);
					sb.append('"');
					break;
				case '\\':
					sb.append(JSONCodec.ESCAPE);
					sb.append('\\');
					break;
				case '/':
					// 20200928 JSON.parse() 不支持斜杠 '\/' 转义
					sb.append(JSONCodec.ESCAPE);
					sb.append('/');
					break;
				case '\b':
					sb.append(JSONCodec.ESCAPE);
					sb.append('b');
					break;
				case '\f':
					sb.append(JSONCodec.ESCAPE);
					sb.append('f');
					break;
				case '\n':
					sb.append(JSONCodec.ESCAPE);
					sb.append('n');
					break;
				case '\r':
					sb.append(JSONCodec.ESCAPE);
					sb.append('r');
					break;
				case '\t':
					sb.append(JSONCodec.ESCAPE);
					sb.append('t');
					break;
				case 0:
					// '\0' NUL 字符导致JSON 失败
					// 大部分运行环境视为字符结束，后续字符不再输出
					break;
				default:
					sb.append((char) c);
			}
		}
		writer.append(sb);
		index += sb.length();
	}
}