package com.joyzl.codec;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * XML 写入
 * 
 * @author simon (ZhangXi TEL:13883833982)
 * @date 2024年3月22日
 */
public class XMLWriter implements Closeable, Flushable {

	private final Writer writer;
	/** 当前标签是否闭合'>' */
	private boolean close;
	/** 当前标签深度 */
	private int depth;

	public XMLWriter(OutputStream output) {
		writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
	}

	public XMLWriter(Writer writer) {
		this.writer = writer;
	}

	/**
	 * 写入常规标签
	 * 
	 * <pre>
	 * &lt;name ...
	 * </pre>
	 * 
	 * @param name 标签名称
	 */
	public void writeElement(CharSequence name) throws IOException {
		if (depth > 0) {
			if (!close) {
				writer.write('>');
			}
		}

		writer.write('<');
		writer.append(name);
		close = false;
		depth++;
	}

	/**
	 * 写入常规标签
	 * 
	 * <pre>
	 * &lt;prefix:name ...
	 * </pre>
	 * 
	 * @param prefix 标签前缀
	 * @param name 标签名称
	 */
	public void writeElement(CharSequence prefix, CharSequence name) throws IOException {
		if (depth > 0) {
			if (!close) {
				writer.write('>');
			}
		}

		writer.write('<');
		writer.append(prefix);
		writer.append(':');
		writer.append(name);
		close = false;
		depth++;
	}

	/**
	 * 结束当前标签 "/>"
	 */
	public void endElement() throws IOException {
		if (depth <= 0) {
			throw new IOException("XMLWriter:START ELEMENT");
		}
		if (close) {
			throw new IOException("XMLWriter:CLOSE ELEMENT");
		}
		writer.write('/');
		writer.write('>');
		close = true;
		depth--;
	}

	/**
	 * 结束当前标签 "/>" "&lt;/name>"
	 */
	public void endElement(CharSequence name) throws IOException {
		if (depth <= 0) {
			throw new IOException("XMLWriter:START ELEMENT");
		}

		// />
		// </name>

		if (close) {
			writer.write('<');
			writer.write('/');
			writer.append(name);
			writer.write('>');
		} else {
			writer.write('/');
			writer.write('>');
		}

		close = true;
		depth--;
	}

	/**
	 * 结束当前标签 "/>" "&lt;/prefix:name>"
	 */
	public void endElement(CharSequence prefix, CharSequence name) throws IOException {
		if (depth <= 0) {
			throw new IOException("XMLWriter:START ELEMENT");
		}

		// />
		// </name>

		if (close) {
			writer.write('<');
			writer.write('/');
			writer.append(prefix);
			writer.write(':');
			writer.append(name);
			writer.write('>');
		} else {
			writer.write('/');
			writer.write('>');
		}

		close = true;
		depth--;
	}

	/**
	 * <pre>
	 * <!-- comment -->
	 * </pre>
	 * 
	 * @param comment
	 */
	public void writeComment(CharSequence comment) throws IOException {
		if (comment == null) {
			return;
		}
		if (depth > 0) {
			if (!close) {
				writer.write('>');
			}
		}

		writer.write("<!-- ");
		writer.append(comment);
		writer.write(" -->");
	}

	/**
	 * name="value"
	 * 
	 * @param name 属性名
	 * @param value 属性值
	 */
	public void writeAttribute(CharSequence name, CharSequence value) throws IOException {
		if (depth <= 0) {
			throw new IOException("XMLWriter:START ELEMENT");
		}
		if (close) {
			throw new IOException("XMLWriter:CLOSE ELEMENT");
		}

		writer.write(' ');
		writer.append(name);
		if (value != null) {
			writer.write('=');
			writer.write('\"');
			writeValue(value);
			writer.write('\"');
		}
	}

	public void writeContent(CharSequence content) throws IOException {
		if (depth <= 0) {
			throw new IOException("XMLWriter:START ELEMENT");
		}
		if (content == null) {
			return;
		}

		if (!close) {
			writer.write('>');
			close = true;
		}
		writeValue(content);
	}

	/**
	 * <pre>
	 * &lt;![CDATA[...]]>
	 * </pre>
	 */
	public void writeData(CharSequence content) throws IOException {
		if (depth <= 0) {
			throw new IOException("XMLWriter:START ELEMENT");
		}
		if (content == null) {
			return;
		}
		if (!close) {
			writer.write('>');
			close = true;
		}
		writer.write("<![CDATA[");
		writer.append(content);
		writer.write("]]>");
	}

	/**
	 * <pre>
	 * &lt;!DOCTYPE ROOT TYPE "DTD">
	 * </pre>
	 * 
	 * @param root 根元素，例如"html"
	 * @param type 类型 "SYSTEM " "PUBLIC"
	 * @param dtd 文档类型定义位置
	 */
	public void writeDocType(CharSequence root, CharSequence type, CharSequence dtd) throws IOException {
		if (depth > 0) {
			throw new IOException("XMLWriter:ROOT ELEMENT");
		}

		writer.write("<!DOCTYPE");
		if (root != null) {
			writer.write(' ');
			writer.append(root);
		}
		if (type != null) {
			writer.write(' ');
			writer.append(type);
		}
		if (dtd != null) {
			writer.write(' ');
			writer.write('\"');
			writer.append(dtd);
			writer.write('\"');
		}
		writer.write('>');
	}

	/**
	 * <pre>
	 * &lt;?xml version="1.0" encoding="UTF-8"?>
	 * </pre>
	 * 
	 * @param version 默认"1.0"
	 * @param encoding 默认"UTF-8"
	 */
	public void writeProlog(CharSequence version, CharSequence encoding) throws IOException {
		if (depth > 0) {
			throw new IOException("XMLWriter:ROOT ELEMENT");
		}

		writer.write("<?xml");
		if (version == null) {
			writer.write(" version=\"1.0\"");
		} else {
			writer.write(" version=\"");
			writer.append(version);
			writer.write('\"');
		}
		if (version == null) {
			writer.write(" encoding=\"UTF-8\"");
		} else {
			writer.write(" encoding=\"");
			writer.append(encoding);
			writer.write('\"');
		}
		writer.write("?>");
	}

	/** 输出文本值，执行字符转义 */
	private void writeValue(CharSequence cs) throws IOException {
		char c;
		for (int index = 0; index < cs.length(); index++) {
			c = cs.charAt(index);
			if (c == '<') {
				writer.write('&');
				writer.write('l');
				writer.write('t');
				writer.write(';');
				continue;
			}
			if (c == '>') {
				writer.write('&');
				writer.write('g');
				writer.write('t');
				writer.write(';');
				continue;
			}
			if (c == '&') {
				writer.write('&');
				writer.write('a');
				writer.write('m');
				writer.write('p');
				writer.write(';');
				continue;
			}
			if (c == '\'') {
				writer.write('&');
				writer.write('a');
				writer.write('p');
				writer.write('o');
				writer.write('s');
				writer.write(';');
				continue;
			}
			if (c == '\"') {
				writer.write('&');
				writer.write('q');
				writer.write('u');
				writer.write('o');
				writer.write('t');
				writer.write(';');
				continue;
			}
			if (c < 9 || !Character.isDefined(c)) {
				writer.write('&');
				writer.write('#');
				writer.write('x');
				writer.write(Integer.toHexString(c));
				writer.write(';');
				continue;
			}
			writer.write(c);
		}
	}

	@Override
	public void flush() throws IOException {
		if (writer != null) {
			writer.flush();
		}
	}

	@Override
	public void close() throws IOException {
		if (writer != null) {
			writer.close();
		}
	}
}