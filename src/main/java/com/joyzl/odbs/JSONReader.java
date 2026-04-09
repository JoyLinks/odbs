package com.joyzl.odbs;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Supplier;

final class JSONReader extends JSONCodec {

	private final static ThreadLocal<JSONReader> JSON_READER = ThreadLocal.withInitial(new Supplier<JSONReader>() {
		@Override
		public JSONReader get() {
			return new JSONReader();
		}
	});

	public final static JSONReader instance(Reader reader) {
		final JSONReader w = JSON_READER.get();
		w.reader = reader;
		w.begin = w.end = 0;
		return w;
	}

	private Reader reader;
	private int begin, end;

	private JSONReader() {
	}

	/*-
	 * 对象和数组采用两套读取方式
	 * 
	 * 对象开始后首先读取键然后读取值，有键必有值，无键则终止
	 * beginObject();
	 * while(readKey()){
	 * 		readValue()/readIgnore();
	 * }
	 * 
	 * 数组开始后须探测是否还有值，有值则继续读取，无值则终止
	 * beginArray();
	 * while(readNext()){
	 * 		readValue()/readIgnore();
	 * }
	 */

	/** 开始对象读取 */
	public void beginObject() throws IOException {
		int c;
		if (begin > 0) {
			c = begin;
			begin = 0;
		} else {
			c = readSkip();
		}
		if (c != OBJECT_BEGIN) {
			throw new IOException("非预期对象");
		}
	}

	/** 开始数组读取 */
	public void beginArray() throws IOException {
		int c;
		if (begin > 0) {
			c = begin;
			begin = 0;
		} else {
			c = readSkip();
		}
		if (c != ARRAY_BEGIN) {
			throw new IOException("非预期数组");
		}
	}

	/** beginArray() 探查数组是否还有下一个值 */
	public boolean readNext() throws IOException {
		if (end == ARRAY_END) {
			end = 0;
			return false;
		}

		// 此方法会消耗掉值开始的第一个字符
		// 因此必须将此字符报告出来

		int c = readSkip();
		if (c == COMMA) {
			c = readSkip();
		}
		if (c == ARRAY_END) {
			// 数组结束
			return false;
		} else {
			begin = c;
		}
		return true;
	}

	/** beginObject() 读取对象的下一个键 */
	public boolean readKey() throws IOException {
		// 如果有引号包围则丢弃双引号
		// 有引号的键允许字符转义
		// 有效键必须结束于 ':'
		// 其余情况结束于 }

		// JSON5 允许尾随逗号 {"key":value,}
		// 空对象无任何键 {}

		if (end == OBJECT_END) {
			// 读取值或忽略值时已消耗掉结束符
			end = 0;
			return false;
		}

		int c = readSkip();
		if (c == COMMA) {
			c = readSkip();
		}
		if (c == OBJECT_END) {
			return false;
		}

		builder.setLength(0);
		if (c == QUOTES) {
			// 双引号包围的键
			while ((c = reader.read()) >= 0) {
				if (c == ESCAPE) {
					c = readEscape();
					if (c == 0) {
						continue;
					}
				}
				if (c == QUOTES) {
					if (readSkip() == COLON) {
						return true;
					} else {
						throw new IOException("键语法错误 " + builder.toString());
					}
				}
				builder.append((char) c);
			}
		} else if (c == QUOTE) {
			// 单引号包围的键
			while ((c = reader.read()) >= 0) {
				if (c == ESCAPE) {
					c = readEscape();
					if (c == 0) {
						continue;
					}
				}
				if (c == QUOTE) {
					if (readSkip() == COLON) {
						return true;
					} else {
						throw new IOException("键语法错误 " + builder.toString());
					}
				}
				builder.append((char) c);
			}
		} else {
			// 无引号键
			builder.append((char) c);
			while ((c = reader.read()) >= 0) {
				if (c == COLON) {
					return true;
				}
				if (Character.isWhitespace(c)) {
					if (readSkip() == COLON) {
						return true;
					} else {
						throw new IOException("键语法错误 " + builder.toString());
					}
				}
				builder.append((char) c);
			}
		}
		throw new IOException("字符流意外结束");
	}

	/** 读取键或数组值，不能读取对象和数组 */
	public boolean readValue() throws IOException {
		// 如果值有双引号包围则丢弃双引号
		// 如果值前后有空白则忽略这些空白
		// 值可能结束于 , ] }

		int c;
		if (begin > 0) {
			c = begin;
			begin = 0;
		} else {
			c = readSkip();
			if (c == ARRAY_END || c == OBJECT_END) {
				end = c;
				return false;
			}
		}

		builder.setLength(0);
		if (c == QUOTES) {
			// 双引号字符串
			// 执行可能的字符转义
			while ((c = reader.read()) >= 0) {
				if (c == ESCAPE) {
					c = readEscape();
					if (c == 0) {
						continue;
					}
				}
				if (c == QUOTES) {
					// 继续直到逗号或结构字符
					end = readSkip();
					return true;
				}
				builder.append((char) c);
			}
		} else if (c == QUOTE) {
			// 单引号字符串
			// 执行可能的字符转义
			while ((c = reader.read()) >= 0) {
				if (c == ESCAPE) {
					c = readEscape();
					if (c == 0) {
						continue;
					}
				}
				if (c == QUOTE) {
					// 继续直到逗号或结构字符
					end = readSkip();
					return true;
				}
				builder.append((char) c);
			}
		} else {
			// 没有双引号的字符串
			// 不执行任何字符转义
			builder.append((char) c);
			while ((c = reader.read()) >= 0) {
				if (c == COMMA || c == OBJECT_END || c == ARRAY_END) {
					// 须报告被消耗的结束符
					end = c;
					return true;
				}
				if (Character.isWhitespace(c)) {
					// 继续直到逗号或结构字符
					end = readSkip();
					return true;
				}
				builder.append((char) c);
			}
		}
		throw new IOException("字符流意外结束");
	}

	/** 读取并忽略值 */
	public void readIgnore() throws IOException {
		// 只能从值位置开始忽略
		// 可能从根位置开始忽略

		// 忽略单值 key:value,key:'value',key:"value"
		// 忽略数组 key:[...],
		// 忽略对象 key:{...},

		// 可能的干扰
		// 字符串中的结构字符
		// 注释中的结构字符

		int c;
		if (begin > 0) {
			c = begin;
			begin = 0;
		} else {
			c = readSkip();
			if (c == ARRAY_END || c == OBJECT_END) {
				end = c;
				return;
			}
		}

		if (c == OBJECT_BEGIN) {
			// 对象值
			// 字符串值，防止字符串中的结构字符干扰
			// 值间的注释，防止注释干扰
			int tag = 1;
			while ((c = reader.read()) >= 0) {
				if (c == OBJECT_END) {
					if (--tag > 0) {
						continue;
					} else {
						// 继续直到逗号或结构字符
						end = readSkip();
						return;
					}
				}
				if (c == OBJECT_BEGIN) {
					tag++;
					continue;
				}
				if (c == QUOTES) {
					while ((c = reader.read()) >= 0) {
						if (c == QUOTES) {
							break;
						}
						if (c == ESCAPE) {
							reader.read();
						}
					}
					continue;
				}
				if (c == QUOTE) {
					while ((c = reader.read()) >= 0) {
						if (c == QUOTE) {
							break;
						}
						if (c == ESCAPE) {
							reader.read();
						}
					}
					continue;
				}
				if (c == '/') {
					readComment();
				}
			}
		} else //
		if (c == ARRAY_BEGIN) {
			// 数组值
			// 字符串值，防止字符串中的结构字符干扰
			// 值间的注释，防止注释干扰
			int tag = 1;
			while ((c = reader.read()) >= 0) {
				if (c == ARRAY_END) {
					if (--tag > 0) {
						continue;
					} else {
						// 继续直到逗号或结构字符
						end = readSkip();
						return;
					}
				}
				if (c == ARRAY_BEGIN) {
					tag++;
					continue;
				}
				if (c == QUOTES) {
					while ((c = reader.read()) >= 0) {
						if (c == QUOTES) {
							break;
						}
						if (c == ESCAPE) {
							reader.read();
						}
					}
					continue;
				}
				if (c == QUOTE) {
					while ((c = reader.read()) >= 0) {
						if (c == QUOTE) {
							break;
						}
						if (c == ESCAPE) {
							reader.read();
						}
					}
					continue;
				}
				if (c == '/') {
					readComment();
				}
			}
		} else //
		if (c == QUOTES) {
			// 双引号字符串值
			// 忽略转义字符，防止转义干扰
			// 字符串中不能有注释
			while ((c = reader.read()) >= 0) {
				if (c == QUOTES) {
					// 继续直到逗号或结构字符
					end = readSkip();
					return;
				}
				if (c == ESCAPE) {
					reader.read();
				}
			}
		} else //
		if (c == QUOTE) {
			// 单引号字符串值
			// 忽略转义字符，防止转义干扰
			// 字符串中不能有注释
			while ((c = reader.read()) >= 0) {
				if (c == QUOTE) {
					// 继续直到逗号或结构字符
					end = readSkip();
					return;
				}
				if (c == ESCAPE) {
					reader.read();
				}
			}
		} else {
			// 无引号值
			// 空白必须结束，防止注释干扰
			// 值紧贴注释，防止注释干扰 k:0//紧贴的注释
			while ((c = reader.read()) >= 0) {
				if (c == COMMA || c == ARRAY_END || c == OBJECT_END) {
					// 忽略值时需要报告结束
					// 因为忽略值可能会消耗结构结束符
					// 如果不报告会导致其它方法不知道已结束
					end = c;
					return;
				}
				if (Character.isWhitespace(c)) {
					// 继续直到逗号或结构字符
					end = readSkip();
					return;
				}
				if (c == '/') {
					readComment();
				}
			}
		}
		throw new IOException("字符流意外结束");
	}

	/** 跳过空白和注释 */
	private int readSkip() throws IOException {
		// 只能在允许任意空白的位置略过
		// 忽略可能出现的注释
		int c;
		while ((c = reader.read()) >= 0) {
			if (Character.isWhitespace(c)) {
				continue;
			}
			if (c == '/') {
				readComment();
				continue;
			}
			return c;
		}
		throw new IOException("字符流意外结束");
	}

	/** 读取并忽略注释，必须事先读取到 '/' */
	private void readComment() throws IOException {
		int c = reader.read();
		if (c == '/') {
			// 单行注释 //...
			while ((c = reader.read()) >= 0) {
				if (c == '\r' || c == '\n' || c == 0x2028 || c == 0x2029) {
					return;
				}
			}
		} else //
		if (c == '*') {
			// 多行注释 /*...*/
			while ((c = reader.read()) >= 0) {
				if (c == '*') {
					c = reader.read();
					if (c == '/') {
						return;
					}
				}
			}
		} else {
			// 在键值之外读取到非法的 '/'
			throw new IOException("语法错误，非法的 '/'");
		}
		throw new IOException("字符流意外结束");
	}

	/**
	 * 读取转义字符，必须在检测到转义标记字符'\'之后<br>
	 * 续行符也由'\'开始，后跟换行
	 */
	private int readEscape() throws IOException {
		int c = reader.read();
		if (c < 0) {
			throw new IOException("字符流意外结束");
		}
		switch (c) {
			case '"': // 双引号
				builder.append('"');
				break;
			case '\'': // 单引号 JSON5
				builder.append('\'');
				break;
			case '\\': // 反斜杠
				builder.append('\\');
				break;
			case '/': // 斜杠
				builder.append('/');
				break;
			case 'b': // 退格符
				builder.append('\b');
				break;
			case 'f': // 换页符
				builder.append('\f');
				break;
			case 'n': // 换行符
				builder.append('\n');
				break;
			case 'r': // 回车符
				builder.append('\r');
				break;
			case 't': // 制表符
				builder.append('\t');
				break;
			case 'v': // 制表符 JSON5
				builder.append('\u000B');
				break;
			case '0': // 空字符
				builder.append('\u0000');
				break;
			case 'x': // \x00
			{
				int x = 0;
				c = reader.read();
				if (c < 0) {
					throw new IOException("字符流意外结束");
				} else {
					x = Character.digit(c, 16) * 16;
				}
				c = reader.read();
				if (c < 0) {
					throw new IOException("字符流意外结束");
				} else {
					x += Character.digit(c, 16);
				}
				builder.append((char) x);
				break;
			}
			case 'u': // \u005C
			{
				int u = 0;
				c = reader.read();
				if (c < 0) {
					throw new IOException("字符流意外结束");
				} else {
					u = Character.digit(c, 16) * 16 * 16 * 16;
				}
				c = reader.read();
				if (c < 0) {
					throw new IOException("字符流意外结束");
				} else {
					u += Character.digit(c, 16) * 16 * 16;
				}
				c = reader.read();
				if (c < 0) {
					throw new IOException("字符流意外结束");
				} else {
					u += Character.digit(c, 16) * 16;
				}
				c = reader.read();
				if (c < 0) {
					throw new IOException("字符流意外结束");
				} else {
					u += Character.digit(c, 16);
				}
				builder.append((char) u);
				break;
			}
			case 0xA: // 续行
				break;
			case 0xD: // 续行
			{ // 检查 \r\n 情况
				c = reader.read();
				if (c < 0) {
					throw new IOException("字符流意外结束");
				}
				if (c == 0x0A) {
					break;
				}
				if (c == ESCAPE) {
					return readEscape();
				}
				// 这是个特殊情况需要返回
				// 应为这可能是个关键的结束符
				return c;
			}
			case 0x2028, 0x2029: // 续行
				break;
			default:
				if (c >= 0x20) {
					// JSON5 允许其它字符自己转义自己
					// \a\b\c\d
					builder.append((char) c);
				}
		}
		return 0;
	}

	/** 读取枚举值 */
	public int readEnumValue(JSONName format) throws IOException {
		// 单值枚举 key:value
		// 对象枚举 key:{...}

		int c;
		if (begin > 0) {
			c = begin;
			begin = 0;
		} else {
			c = readSkip();
		}

		if (c == ARRAY_BEGIN) {
			throw new IOException("枚举值不能为数组");
		}
		if (c == OBJECT_BEGIN) {
			int v = -1;
			while (readKey()) {
				if (JSONName.match(JSONName.VALUES, format, builder)) {
					if (readValue()) {
						v = getInt();
					} else {
						break;
					}
					while (readKey()) {
						readIgnore();
					}
					break;
				} else {
					readIgnore();
				}
			}
			if (v < 0) {
				throw new IOException("枚举值缺失");
			} else {
				return v;
			}
		} else {
			begin = c;
			if (readValue()) {
				return getInt();
			} else {
				throw new IOException("枚举值缺失");
			}
		}
	}

	boolean is0x() {
		if (builder.length() > 2) {
			if (builder.charAt(0) == '0') {
				if (builder.charAt(1) == 'x' || builder.charAt(1) == 'X') {
					return true;
				}
			}
		}
		return false;
	}

	boolean isNull() {
		// 判断最后一次读取值是否为null
		if (builder.length() == 4) {
			// 容错:忽略大小写
			if (builder.charAt(0) != 'n' && builder.charAt(0) != 'N')
				return false;
			if (builder.charAt(1) != 'u' && builder.charAt(1) != 'U')
				return false;
			if (builder.charAt(2) != 'l' && builder.charAt(2) != 'L')
				return false;
			if (builder.charAt(3) != 'l' && builder.charAt(3) != 'L')
				return false;
			return true;
		}
		return false;
	}

	/** 读取值 */
	public byte getByte() throws IOException {
		if (is0x()) {
			return (byte) Integer.parseInt(builder, 2, builder.length(), 16);
		}
		return (byte) Integer.parseInt(builder, 0, builder.length(), 10);
	}

	/** 读取值 */
	public double getDouble() throws IOException {
		return Double.parseDouble(builder.toString());
	}

	/** 读取值 */
	public float getFloat() throws IOException {
		return Float.parseFloat(builder.toString());
	}

	/** 读取值 */
	public boolean getBoolean() throws IOException {
		return "true".contentEquals(builder);
	}

	/** 读取值 */
	public int getInt() throws IOException {
		if (is0x()) {
			return Integer.parseInt(builder, 2, builder.length(), 16);
		}
		return Integer.parseInt(builder, 0, builder.length(), 10);
	}

	/** 读取值 */
	public long getLong() throws IOException {
		if (is0x()) {
			return Long.parseLong(builder, 2, builder.length(), 16);
		}
		return Long.parseLong(builder, 0, builder.length(), 10);
	}

	/** 读取值 */
	public short getShort() throws IOException {
		if (is0x()) {
			return (short) Integer.parseInt(builder, 2, builder.length(), 16);
		}
		return (short) Integer.parseInt(builder, 0, builder.length(), 10);
	}

	/** 读取值 */
	public char getChar() throws IOException {
		if (builder.isEmpty()) {
			return 0;
		}
		return builder.charAt(0);
	}

	/** 读取值 */
	public String getString() throws IOException {
		if (isNull()) {
			return null;
		}
		return builder.toString();
	}

	/** 读取值 */
	public BigDecimal getBigDecimal() throws IOException {
		if (isNull()) {
			return null;
		}
		return new BigDecimal(builder.toString());
	}

	/** 读取值 */
	public BigInteger getBigInteger() throws IOException {
		if (isNull()) {
			return null;
		}
		return new BigInteger(builder.toString());
	}

	/** 读取值 */
	public LocalDate getLocalDate(DateTimeFormatter format) throws IOException {
		if (isNull()) {
			return null;
		}
		return LocalDate.parse(builder, format);
	}

	/** 读取值 */
	public LocalDateTime getLocalDateTime(DateTimeFormatter format) throws IOException {
		if (isNull()) {
			return null;
		}
		return LocalDateTime.parse(builder, format);
	}

	/** 读取值 */
	public LocalTime getLocalTime(DateTimeFormatter format) throws IOException {
		if (isNull()) {
			return null;
		}
		return LocalTime.parse(builder, format);
	}

	/** 读取值 */
	public Date getDate(DateFormat format) throws IOException {
		if (isNull()) {
			return null;
		}
		try {
			synchronized (format) {
				return format.parse(builder.toString());
			}
		} catch (ParseException e) {
			throw new IOException(e);
		}
	}
}