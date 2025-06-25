/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class TestXMLReader {

	final String XML = """
			<!-- declarations for <head> & <body> -->
			<?xml version="1.0"?>
			<!DOCTYPE greeting SYSTEM "hello.dtd">
			<root>
				<greeting>Hello, world!</greeting>
				<greeting><![CDATA[<greeting>Hello, world!</greeting>]]> </greeting>
				<!---->
				<IMG align="left"
					src="http://www.w3.org/Icons/WWW/w3c_home" />
				<br></br>
				<br/>

				<p:sub xmlns:p="http://www.joyzl.com/xml/">
					<item> TEST &amp; &lt; &gt; &apos; &quot; </item>
				</p:sub>
				<high-unicode>&#65536;</high-unicode>
			</root>
			""";

	@Test
	void test() throws IOException {
		final XMLReader reader = new XMLReader(XML);

		// <!-- ... -->
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.COMMENT);
		assertEquals(reader.depth(), 1);
		assertTrue(reader.isEnd());
		assertEquals(reader.getAttributeCount(), 0);
		assertEquals(reader.getContent(), "declarations for <head> & <body>");

		// <?xml version="1.0"?>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.PROLOG);
		assertEquals(reader.depth(), 1);
		assertTrue(reader.isEnd());
		assertFalse(reader.hasContent());
		assertEquals(reader.getAttributeCount(), 1);
		assertEquals(reader.getAttributeValue("version"), "1.0");

		// <!DOCTYPE greeting SYSTEM "hello.dtd">
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.DOCTYPE);
		assertEquals(reader.depth(), 1);
		assertTrue(reader.isEnd());
		assertFalse(reader.hasContent());
		assertEquals(reader.getAttributeCount(), 3);

		// <root>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 1);
		assertFalse(reader.isEnd());
		assertFalse(reader.hasContent());
		assertEquals(reader.getAttributeCount(), 0);

		// <greeting>Hello, world!</greeting>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 2);
		assertTrue(reader.isEnd());
		assertTrue(reader.hasContent());
		assertEquals(reader.getAttributeCount(), 0);
		assertEquals(reader.getContent(), "Hello, world!");

		// <greeting><![CDATA[<greeting>Hello, world!</greeting>]]> </greeting>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 2);
		assertTrue(reader.isEnd());
		assertTrue(reader.hasContent());
		assertEquals(reader.getAttributeCount(), 0);
		assertEquals(reader.getContent(), "<greeting>Hello, world!</greeting>");

		// <!---->
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.COMMENT);
		assertEquals(reader.depth(), 2);
		assertTrue(reader.isEnd());
		assertEquals(reader.getAttributeCount(), 0);
		assertFalse(reader.hasContent());

		// <IMG.../>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 2);
		assertTrue(reader.isEnd());
		assertEquals(reader.getAttributeCount(), 2);
		assertEquals(reader.getAttributeValue("align"), "left");
		assertEquals(reader.getAttributeValue("src"), "http://www.w3.org/Icons/WWW/w3c_home");
		assertFalse(reader.hasContent());

		// <br></br>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 2);
		assertTrue(reader.isEnd());
		assertEquals(reader.getAttributeCount(), 0);
		assertFalse(reader.hasContent());

		// <br/>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 2);
		assertTrue(reader.isEnd());
		assertEquals(reader.getAttributeCount(), 0);
		assertFalse(reader.hasContent());

		// <p:sub>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 2);
		assertFalse(reader.isEnd());
		assertEquals(reader.getAttributeCount(), 1);
		assertFalse(reader.hasContent());
		assertTrue(reader.hasPrefix());
		assertEquals(reader.getPrefix(), "p");
		assertEquals(reader.getAttributeValue("xmlns:p"), "http://www.joyzl.com/xml/");

		// <item></item>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 3);
		assertTrue(reader.isEnd());
		assertEquals(reader.getAttributeCount(), 0);
		assertTrue(reader.hasContent());
		assertEquals(reader.getContent(), "TEST & < > ' \"");

		// </p:sub>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 2);

		// <high-unicode>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 2);
		assertTrue(reader.isEnd());
		assertEquals(reader.getAttributeCount(), 0);
		assertTrue(reader.hasContent());
		assertEquals(reader.getContent(), String.valueOf(Character.toChars(65536)));

		// </root>
		assertTrue(reader.nextElement());
		assertEquals(reader.type(), XMLElementType.NORMAL);
		assertEquals(reader.depth(), 1);

		assertFalse(reader.nextElement());
		assertFalse(reader.nextElement());
	}

	@Test
	void testReadChildren() throws IOException {
		final XMLReader readerAll = new XMLReader(XML);
		assertEquals(readerAll.getChildren(), XML);

		final XMLReader reader = new XMLReader(XML);
		while (reader.nextElement()) {
			if (reader.isName("root")) {
				System.out.println(reader.getChildren());
				break;
			}
		}
		assertTrue(reader.nextElement());
	}
}