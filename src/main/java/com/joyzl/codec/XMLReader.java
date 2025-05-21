package com.joyzl.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * XML读取
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2024年3月17日
 */
public class XMLReader {

	// https://www.w3.org/TR/REC-xml/
	// TODO 未支持命名空间校验

	private InputStream input;
	private Reader reader;

	/** 当前元素类型 */
	private XMLElementType type = XMLElementType.UNKNOWN;
	/** 当前元素名称和属性 */
	private final StringBuilder buffer = new StringBuilder(256);
	/** name attribute1 value1 attribute2 value2 content */
	private final int[] segments = new int[128];
	private int segmentSize;
	/** 当前标签是否已关闭 */
	private boolean end;
	/** 当前深度 */
	private int depth;
	/** 当前字符值 */
	private int value;

	public XMLReader() {
	}

	public XMLReader(String text) {
		setSource(text);
	}

	public XMLReader(InputStream input) {
		setReader(input);
	}

	/*-
	 * buffer和segments的对应关系
	 * buffer:   [p:name]
	 * segments: [0  4]
	 * 
	 * buffer:   [p:name content]
	 * segments: [0  4]
	 * 
	 * buffer:   [p:name attribute1 value1 attribute2 value2 content]
	 * segments: [0  4         14     20         30     36]
	 */

	public boolean nextElement() throws IOException {
		// 当前字符可能的情形
		// 1从未读取 value=0
		// 2标签结束符 value='>'
		// 3下一级标签名称第一个字符 value='n'
		if (value <= 0 || value == '>') {
			// 读取直到元素开始标记'<'
			while (true) {
				value = reader.read();
				if (value < 0) {
					return false;
				}
				if (value == '<') {
					break;
				}
				// 忽略其它
			}
			// '<'后的第一个字符
			value = reader.read();
		}

		if (end) {
			depth--;
		}

		// 判断元素类型
		// '<N' '<?' '</' '<!'
		if (value == '?') {
			// <?xml version="1.0" encoding="UTF-8"?>
			type = XMLElementType.PROLOG;
			// 读取名称和属性
			buffer.setLength(0);
			end = readNameAttributes();
			depth++;
			// 检测字符编码
			checkEncoding();
			return true;
		} else //
		if (value == '!') {
			// <!-- -->
			// <!-- comment -->
			value = reader.read();
			if (value == '-') {
				value = reader.read();
				if (value == '-') {
					type = XMLElementType.COMMENT;
					// 标签名称空
					segmentSize = 2;
					segments[0] = segments[1] = 0;

					// 读取注释
					buffer.setLength(0);
					readComment();
					end = true;
					depth++;
					return true;
				} else {
					buffer.append('-');
					buffer.append((char) value);
				}
			} else {
				buffer.setLength(0);
				buffer.append((char) value);
			}
			// <!DOCTYPE >
			type = XMLElementType.DOCTYPE;
			// 读取名称和属性
			readNameAttributes();
			end = true;
			depth++;
			return true;
		} else //
		if (value == '/') {
			// </name>
			// 读取直到元素结束标记'>'
			readNameEnd();
			end = true;
			// depth--;
			return true;
		} else {
			// <name/>
			// <name attribute="value"/>
			// <name attribute="value">Text</name>
			// <name attribute="value"><A>...</A></name>
			// <prop xmlns="http://example.com/neon/litmus/">value</prop>
			// <D:multistatus xmlns:D="DAV:">
			type = XMLElementType.NORMAL;
			buffer.setLength(0);
			buffer.append((char) value);
			// 读取名称和属性
			if (readNameAttributes()) {
				end = true;
			} else if (specialContent()) {
				end = readContent(reader, buffer);
			} else {
				end = readContent();
			}
			depth++;
			return true;
		}
	}

	/** 检测XML字符编码 */
	private void checkEncoding() throws IOException {
		if (input == null) {
			return;
		}
		final String name = getAttributeValue("encoding");
		if (name != null && name.length() > 0) {
			reader = new InputStreamReader(input, name);
		}
	}

	/**
	 * 读取标签名称直至空白以及'>'或'/>'
	 * 
	 * @return 标签是否结束 '/>'
	 */
	private boolean readNameAttributes() throws IOException {
		// <name/>
		// <name></name>
		// <name attribute="value"/>
		// <n:name attribute="value"/>

		segmentSize = 2;
		segments[0] = 0;

		while ((value = reader.read()) >= 0) {
			if (Character.isWhitespace(value)) {
				segments[1] = buffer.length();
				return readAttributes();
			}
			if (value == ':') {
				segments[0] = buffer.length();
				continue;
			}
			if (value == '>') {
				segments[1] = buffer.length();
				return false;
			}
			if (value == '/' || value == '?') {
				value = reader.read();
				if (value == '>') {
					segments[1] = buffer.length();
					return true;
				} else {
					// 错误的格式
					continue;
				}
			}
			buffer.append((char) value);
		}
		// 意外结束
		throw new IOException("XML文本流意外结束");
	}

	/**
	 * 读取标签名称直至'>'
	 */
	private void readNameEnd() throws IOException {
		// <name></name>

		segmentSize = 2;
		segments[0] = 0;

		buffer.setLength(0);
		while ((value = reader.read()) >= 0) {
			if (Character.isWhitespace(value)) {
				continue;
			}
			if (value == ':') {
				segments[0] = buffer.length();
				continue;
			}
			if (value == '>') {
				segments[1] = buffer.length();
				return;
			}
			buffer.append((char) value);
		}
		// 意外结束
		throw new IOException("XML文本流意外结束");
	}

	/**
	 * 读取标签属性直至'>'或'/>'
	 * 
	 * @return 标签是否结束 '/>'
	 */
	private boolean readAttributes() throws IOException {
		// <name attribute/>
		// <name attribute />
		// <name attribute=/>
		// <name attribute= />
		// <name attribute=value/>
		// <name attribute=value />
		// <name attribute="value"/>
		// <name attribute="value">Text</name>
		// <name attribute="value"><A>...</A></name>
		// <img src=//www.baidu.com/img/gs.gif>

		int chars = 0;
		// 读取名称时不包含空白字符，引号""中的值包含空白字符
		boolean skipWhitespace = true;
		// 读取值无引号时空白或'>'结束
		boolean valueChars = false;

		while ((value = reader.read()) >= 0) {
			if (value == '&') {
				if (readEscape()) {
					chars++;
					continue;
				}
			}
			if (value == '=') {
				// name
				segments[segmentSize++] = buffer.length();
				valueChars = true;
				continue;
			}
			if (value == '\'' || value == '\"') {
				if (skipWhitespace) {
					skipWhitespace = false;
				} else {
					segments[segmentSize++] = buffer.length();
					skipWhitespace = true;
					valueChars = false;
					chars = 0;
				}
				continue;
			}
			if (Character.isWhitespace(value)) {
				if (skipWhitespace) {
					// 无值的属性
					if (chars > 0) {
						// NAME
						segments[segmentSize++] = buffer.length();
						// VALUE EMPTY
						segments[segmentSize++] = buffer.length();
						valueChars = false;
						chars = 0;
					}
					continue;
				}
			}
			if (value == '/' || value == '?') {
				if (valueChars) {
					// 属性值中允许出现'/'和'?'字符
				} else if (skipWhitespace) {
					value = reader.read();
					if (value == '>') {
						// 根据数量修正空值
						if ((segmentSize & 1) == 1) {
							// 奇数量 补空值
							segments[segmentSize++] = buffer.length();
						} else if (chars > 0) {
							// NAME
							segments[segmentSize++] = buffer.length();
							// VALUE EMPTY
							segments[segmentSize++] = buffer.length();
						}
						return true;
					} else {
						// 错误的格式
						throw new IOException("XML错误格式:" + (char) value);
					}
				}
			}
			if (value == '>') {
				// 根据数量修正空值
				if ((segmentSize & 1) == 1) {
					// 奇数量 补空值
					segments[segmentSize++] = buffer.length();
				} else if (chars > 0) {
					// NAME
					segments[segmentSize++] = buffer.length();
					// VALUE EMPTY
					segments[segmentSize++] = buffer.length();
				}
				return false;
			}
			buffer.append((char) value);
			chars++;
		}
		// 意外结束
		throw new IOException("XML文本流意外结束");
	}

	/**
	 * 读取所有子元素为文本
	 */
	private String readChildren() throws IOException {
		if (end) {
			return null;
		}
		if (value <= 0) {
			// 从未读取，全部读取为文本
			while ((value = reader.read()) >= 0) {
				buffer.append((char) value);
			}
			return buffer.toString();
		}

		// <name> <a/> <b></b> <c>content</c> <!-- * --> </name>

		int d = depth + 1;
		buffer.append('<');
		buffer.append((char) value);
		while ((value = reader.read()) >= 0) {
			if (value == '<') {
				value = reader.read();
				if (value == '/') {
					if (d == depth) {
						break;// END '/'
					}
					buffer.append('<');
					d--;
				} else if (value == '!') {
					buffer.append('<');
				} else {
					buffer.append('<');
					d++;
				}
			} else if (value == '/') {
				buffer.append('/');
				value = reader.read();
				if (value == '>') {
					d--;
				}
			}
			// 忽略无用空白
			// if (d == depth) {
			// if (Character.isWhitespace(value)) {
			// continue;
			// }
			// }
			buffer.append((char) value);
		}
		return getContent();
	}

	/**
	 * 读取标签内容直至'&lt;'或'&lt;/'
	 * 
	 * @return 标签是否结束 '&lt;/name>'
	 */
	private boolean readContent() throws IOException {
		// ...<name>
		// ...</name>
		// <![CDATA[文本内容]]>
		// <![CDATA[<greeting>Hello, world!</greeting>]]>

		boolean cdata = false;
		final int length = buffer.length();
		while ((value = reader.read()) >= 0) {
			if (Character.isWhitespace(value)) {
				// 过滤有效字符之前的空白
				if (length == buffer.length()) {
					continue;
				}
			}
			if (value == '<') {
				value = reader.read();
				if (value == '!') {
					value = reader.read();
					if (value == '[') {
						value = reader.read();
						if (value == 'C') {
							value = reader.read();
							if (value == 'D') {
								value = reader.read();
								if (value == 'A') {
									value = reader.read();
									if (value == 'T') {
										value = reader.read();
										if (value == 'A') {
											value = reader.read();
											if (value == '[') {
												buffer.setLength(length);
												// 读取CDATA内容直至']]>'
												while (true) {
													value = reader.read();
													if (value < 0) {
														// 意外结束
														throw new IOException("XML文本流意外结束");
													}
													if (value == ']') {
														value = reader.read();
														if (value == ']') {
															value = reader.read();
															if (value == '>') {
																break;
															} else {
																buffer.append(']');
																buffer.append(']');
															}
														} else {
															buffer.append(']');
														}
													}
													buffer.append((char) value);
												}
												cdata = true;
												continue;
											} else {
												buffer.append('[');
												buffer.append('C');
												buffer.append('D');
												buffer.append('A');
												buffer.append('T');
												buffer.append('A');
												buffer.append('[');
											}
										} else {
											buffer.append('[');
											buffer.append('C');
											buffer.append('D');
											buffer.append('A');
											buffer.append('T');
											buffer.append('A');
										}
									} else {
										buffer.append('[');
										buffer.append('C');
										buffer.append('D');
										buffer.append('A');
										buffer.append('T');
									}
								} else {
									buffer.append('[');
									buffer.append('C');
									buffer.append('D');
									buffer.append('A');
								}
							} else {
								buffer.append('[');
								buffer.append('C');
								buffer.append('D');
							}
						} else {
							buffer.append('[');
							buffer.append('C');
						}
					} else {
						buffer.append('[');
					}
				} else //
				if (value == '/') {
					endStrip(length);
					while ((value = reader.read()) != '>') {
						// 丢弃结束标签名称
					}
					// 结束于当前标签最后一个字符'>'
					return true;
				} else {
					endStrip(length);
					// 结束于下一级标签名称第一个字符
					return false;
				}
			}
			if (value == '&') {
				if (readEscape()) {
					continue;
				}
			}
			if (cdata) {
				// 忽略CDATA之后的文本内容
				continue;
			}
			buffer.append((char) value);
		}
		// 意外结束
		throw new IOException("XML文本流意外结束");
	}

	/**
	 * 读取转义字符直至';'结束，必须事先明确读取到'&'，如果转义判定失败所有字符按普通文本读取
	 * 
	 * @return 是否成功转义
	 */
	private boolean readEscape() throws IOException {
		value = reader.read();
		// '<' &lt;
		if (value == 'l') {
			value = reader.read();
			if (value == 't') {
				value = reader.read();
				if (value == ';') {
					buffer.append('<');
					return true;
				} else {
					buffer.append('&');
					buffer.append('l');
					buffer.append('t');
					return false;
				}
			} else {
				buffer.append('&');
				buffer.append('l');
				return false;
			}
		}
		// '>' &gt;
		if (value == 'g') {
			value = reader.read();
			if (value == 't') {
				value = reader.read();
				if (value == ';') {
					buffer.append('>');
					return true;
				} else {
					buffer.append('&');
					buffer.append('g');
					buffer.append('t');
					return false;
				}
			} else {
				buffer.append('&');
				buffer.append('g');
				return false;
			}
		}
		// '&' &amp;
		// "'" &apos;
		if (value == 'a') {
			value = reader.read();
			if (value == 'm') {
				value = reader.read();
				if (value == 'p') {
					value = reader.read();
					if (value == ';') {
						buffer.append('&');
						return true;
					} else {
						buffer.append('&');
						buffer.append('a');
						buffer.append('m');
						buffer.append('p');
						return false;
					}
				} else {
					buffer.append('&');
					buffer.append('a');
					buffer.append('m');
					return false;
				}
			} else //
			if (value == 'p') {
				value = reader.read();
				if (value == 'o') {
					value = reader.read();
					if (value == 's') {
						value = reader.read();
						if (value == ';') {
							buffer.append('\'');
							return true;
						} else {
							buffer.append('&');
							buffer.append('a');
							buffer.append('p');
							buffer.append('o');
							buffer.append('s');
							return false;
						}
					} else {
						buffer.append('&');
						buffer.append('a');
						buffer.append('p');
						buffer.append('o');
						return false;
					}
				} else {
					buffer.append('&');
					buffer.append('a');
					buffer.append('p');
					return false;
				}
			} else {
				buffer.append('&');
				buffer.append('a');
				return false;
			}
		}
		// '"' &quot;
		if (value == 'q') {
			value = reader.read();
			if (value == 'u') {
				value = reader.read();
				if (value == 'o') {
					value = reader.read();
					if (value == 't') {
						value = reader.read();
						if (value == ';') {
							buffer.append('\"');
							return true;
						} else {
							buffer.append('&');
							buffer.append('q');
							buffer.append('u');
							buffer.append('o');
							buffer.append('t');
							return false;
						}
					} else {
						buffer.append('&');
						buffer.append('q');
						buffer.append('u');
						buffer.append('o');
						return false;
					}
				} else {
					buffer.append('&');
					buffer.append('q');
					buffer.append('u');
					return false;
				}
			} else {
				buffer.append('&');
				buffer.append('q');
				return false;
			}
		}
		// '*' &#2147483647;
		// '*' &#xEEEEFFFF;
		if (value == '#') {
			final int index = buffer.length();
			while (true) {
				value = reader.read();
				if (value == ';') {
					break;
				}
				buffer.append((char) value);
				if (buffer.length() - index > 10) {
					// 已超出可能的范围，转义失败
					// #号之后最多10字符
					buffer.append('&');
					buffer.append('#');
					return false;
				}
			}
			if (buffer.charAt(index) == 'x') {
				value = Integer.parseInt(buffer, index + 1, buffer.length(), 16);
			} else {
				value = Integer.parseInt(buffer, index, buffer.length(), 10);
			}
			buffer.setLength(index);
			buffer.appendCodePoint(value);
			// buffer.append((char) value);
			return true;
		}

		// 不是转义字符
		buffer.append('&');
		return false;
	}

	/** 读取注释直至'-->' */
	private void readComment() throws IOException {
		// <!-- -->
		// <!-- comment -->
		int length = buffer.length();
		while ((value = reader.read()) >= 0) {
			if (Character.isWhitespace(value)) {
				// 过滤有效字符之前的空白
				if (length == buffer.length()) {
					continue;
				}
			}
			if (value == '-') {
				value = reader.read();
				if (value == '-') {
					value = reader.read();
					if (value == '>') {
						// 移除尾部空白
						endStrip(length);
						return;
					} else {
						buffer.append('-');
						buffer.append('-');
					}
				} else {
					buffer.append('-');
				}
			}
			buffer.append((char) value);
		}
		// 意外结束
		throw new IOException("XML文本流意外结束");
	}

	/** 移除缓存的尾部空白 */
	private void endStrip(int length) {
		// length 为保护的字符部分
		while (buffer.length() > length) {
			if (Character.isWhitespace(buffer.charAt(buffer.length() - 1))) {
				buffer.setLength(buffer.length() - 1);
			} else {
				break;
			}
		}
	}

	/** 比较字符串与缓存指定位置是否相同 */
	private boolean equals(CharSequence name, int begin) {
		if (begin + name.length() > buffer.length()) {
			return false;
		}
		if (name.charAt(0) != buffer.charAt(begin)) {
			return false;
		}
		for (int index = 1; index < name.length(); index++) {
			if (name.charAt(index) != buffer.charAt(begin + index)) {
				return false;
			}
		}
		return true;
	}

	////////////////////////////////////////////////////////////////////////////////

	protected boolean specialContent() {
		return false;
	}

	/** 扩展特殊内容读取 */
	protected boolean readContent(Reader reader, StringBuilder buffer) throws IOException {
		return false;
	}

	////////////////////////////////////////////////////////////////////////////////

	/** 获取当前标签深度 */
	public int depth() {
		return depth;
	}

	/** 获取当前标签类型 */
	public XMLElementType type() {
		return type;
	}

	/** 获取当前标签缓存字符 */
	public CharSequence buffer() {
		return buffer;
	}

	/** 当前标签是否结束 */
	public boolean isEnd() {
		return end;
	}

	/** 检查当前标签名称 */
	public boolean isName(CharSequence name) {
		if (segmentSize > 0) {
			if (name.length() == segments[1] - segments[0]) {
				return equals(name, segments[0]);
			}
		}
		return false;
	}

	/** 获取当前标签名称 */
	public String getName() {
		if (segmentSize > 0) {
			return buffer.substring(segments[0], segments[1]);
		}
		return null;
	}

	/** 当前标签是否有名称 */
	public boolean hasName() {
		return segmentSize > 1 && segments[1] > segments[0];
	}

	/** 检查当前标签前缀 */
	public boolean isPrefix(CharSequence name) {
		if (segmentSize > 0) {
			if (name.length() == segments[0]) {
				return equals(name, 0);
			}
		}
		return false;
	}

	/** 获取当前标签前缀 */
	public String getPrefix() {
		if (segmentSize > 0) {
			return buffer.substring(0, segments[0]);
		}
		return null;
	}

	/** 当前标签是否有前缀 */
	public boolean hasPrefix() {
		return segmentSize > 0 && segments[0] > 0;
	}

	/** 当前标签是否有属性 */
	public boolean hasAttributes() {
		// prefix name
		// prefix name content
		// prefix name attribute1 value1 attribute2 value2 content
		return segmentSize > 2;
	}

	/** 当前标签属性数量 */
	public int getAttributeCount() {
		if (segmentSize > 2) {
			return (segmentSize - 2) / 2;
		}
		return 0;
	}

	/** 获取所有属性，此方法将所有属性键值对构建为新Map<String, String>集合实例返回 */
	public Map<String, String> getAttributes() {
		final Map<String, String> attributes = new HashMap<>();
		for (int index = 2; index < segmentSize; index += 2) {
			attributes.put(buffer.substring(segments[index - 1], segments[index]), buffer.substring(segments[index], segments[index + 1]));
		}
		return attributes;
	}

	/**
	 * 获取当前标签指定名称的属性索引，返回的索引值不代表属性的原始位置和顺序
	 * 
	 * @param name
	 * @return 0~n / -1 没有指定的属性
	 */
	public int getAttributeIndex(CharSequence name) {
		for (int a = 2; a < segmentSize; a += 2) {
			if (name.length() == segments[a] - segments[a - 1]) {
				if (equals(name, segments[a - 1])) {
					return a;
				}
			}
		}
		return -1;
	}

	/**
	 * 获取当前标签指定索引的属性值，此索引由{@link #getAttributeIndex(CharSequence)}方法获得
	 * 
	 * @param index 读取器分配的属性索引
	 * @return String / null
	 */
	public String getAttributeValue(int index) {
		if (index > 1 && index < segmentSize) {
			return buffer.substring(segments[index], segments[index + 1]);
		}
		return null;
	}

	/**
	 * 获取当前标签指定索引属性的整数值
	 * 
	 * @param index 读取器分配的属性索引
	 * @param radix 进制
	 * @return 0 默认
	 */
	public int getAttributeInt(int index, int radix) {
		if (index > 1 && index < segmentSize) {
			return Integer.parseInt(buffer, segments[index], segments[index + 1], radix);
		}
		return 0;
	}

	/**
	 * 获取当前标签指定索引属性的整数值
	 * 
	 * @param index 读取器分配的属性索引
	 * @param radix 进制
	 * @return 0 默认
	 */
	public long getAttributeLong(int index, int radix) {
		if (index > 1 && index < segmentSize) {
			return Long.parseLong(buffer, segments[index], segments[index + 1], radix);
		}
		return 0;
	}

	/**
	 * 获取当前标签指定名称的属性值
	 * 
	 * @param name
	 * @return String / null
	 */
	public String getAttributeValue(CharSequence name) {
		for (int a = 2; a < segmentSize; a += 2) {
			if (name.length() == segments[a] - segments[a - 1]) {
				if (equals(name, segments[a - 1])) {
					return buffer.substring(segments[a], segments[a + 1]);
				}
			}
		}
		return null;
	}

	/** 当前标签是否有内容 */
	public boolean hasContent() {
		// prefix name
		// prefix name content
		// prefix name attribute1 value1 attribute2 value2 content
		return buffer.length() > segments[segmentSize - 1];
	}

	/**
	 * 获取当前标签内容
	 * 
	 * @return 可能会包含空白字符
	 */
	public String getContent() {
		return buffer.substring(segments[segmentSize - 1]);
	}

	/**
	 * 获取当前标签整数值，应捕获转换失败异常
	 * 
	 * @param radix 进制
	 */
	public int getContentInt(int radix) {
		return Integer.parseInt(buffer, segments[segmentSize - 1], buffer.length(), radix);
	}

	/**
	 * 获取当前标签整数值，应捕获转换失败异常
	 * 
	 * @param radix 进制
	 */
	public long getContentLong(int radix) {
		return Long.parseLong(buffer, segments[segmentSize - 1], buffer.length(), radix);
	}

	/**
	 * 获取当前标签之内的所有子元素以文本形式返回
	 * 
	 * @return String
	 */
	public String getChildren() throws IOException {
		return readChildren();
	}

	/** 获取当前字符读取实例 */
	public Reader getReader() {
		return reader;
	}

	/**
	 * 设置当前字符读取实例，这将导致XMLReader实例重置；如果已明确XML字符编码推荐使用此方法，
	 * 将避免XMLReader检测字符编码并重建字符读取器
	 */
	public void setReader(Reader r) {
		segmentSize = 0;
		input = null;
		reader = r;
		value = 0;
		depth = 0;
	}

	/**
	 * 设置当前字符读取流，在不明确XML字符编码时可使用此方法，这将由XMLReader探测XML字符编码并重建字符读取器
	 */
	public void setReader(InputStream in) {
		segmentSize = 0;
		input = in;
		reader = new Reader() {
			@Override
			public int read() throws IOException {
				return in.read();
			}

			@Override
			public int read(char[] cbuf, int off, int len) throws IOException {
				int value = in.read();
				if (value < 0) {
					return -1;
				} else {
					len += off;
					cbuf[off] = (char) value;
					int index = off + 1;
					for (; index < len; index++) {
						value = in.read();
						if (value >= 0) {
							cbuf[index] = (char) value;
						} else {
							break;
						}
					}
					return index - off;
				}
			}

			@Override
			public void close() throws IOException {
				in.close();
			}
		};
		value = 0;
		depth = 0;
	}

	public void setSource(String xml) {
		segmentSize = 0;
		input = null;
		reader = new Reader() {
			private int index = 0;

			@Override
			public int read() throws IOException {
				if (index < xml.length()) {
					return xml.charAt(index++);
				}
				return -1;
			}

			@Override
			public int read(char[] cbuf, int off, int len) throws IOException {
				return 0;
			}

			@Override
			public void close() throws IOException {
			}
		};
		value = 0;
		depth = 0;
	}
}