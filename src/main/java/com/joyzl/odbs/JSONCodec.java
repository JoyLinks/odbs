package com.joyzl.odbs;

/**
 * JSON 编解码辅助
 * 
 * @author ZhangXi 2026年4月7日
 */
abstract class JSONCodec {

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

	/*-
	 * JSON5 https://spec.json5.org
	 * 键名允许没有引号 key:"value"
	 * 允许最后的键值对或数组元素尾部逗号 {k:v,}[1,]
	 * 允许字符串续行 \+0A \0D \2028 \2029
	 * 允许常量 IEEE754 Infinity -Infinity NaN
	 * 允许注释
	 */

	protected final StringBuilder builder = new StringBuilder(1024);

	public CharSequence chars() {
		return builder;
	}
}