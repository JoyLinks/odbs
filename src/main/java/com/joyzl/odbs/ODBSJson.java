package com.joyzl.odbs;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ODBSJson {

	private final ODBS odbs;
	private final ODBSJsonEncode encode;
	private final ODBSJsonDecode decode;

	public ODBSJson(ODBS odbs) {
		encode = new ODBSJsonEncode(odbs);
		decode = new ODBSJsonDecode(odbs);
		this.odbs = odbs;
	}

	public void writeEntities(Collection<?> entities, Writer writer) throws IOException {
		final JSONWriter out = JSONWriter.instance(writer);
		out.beginArray();
		TypeEntity t = null;
		for (Object v : entities) {
			if (t == null) {
				t = odbs.get(v.getClass());
				if (t == null) {
					throw new IOException("ODBS JSON 类型无效");
				}
			}
			encode.writeEntity(out, t, v);
		}
		out.endArray();
	}

	public void writeEntity(Object entity, Writer writer) throws IOException {
		final TypeEntity t = odbs.get(entity.getClass());
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		encode.writeEntity(JSONWriter.instance(writer), t, entity);
	}

	public <T> List<T> readEntities(Class<T> type, Reader reader) throws IOException {
		final TypeEntity t = odbs.get(type);
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		final List<T> entities = new ArrayList<>();
		decode.readList(JSONReader.instance(reader), t, entities);
		return entities;
	}

	public <T> void readEntities(Collection<T> entities, Class<T> type, Reader reader) throws IOException {
		final TypeEntity t = odbs.get(type);
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		decode.readCollection(JSONReader.instance(reader), t, entities);
	}

	public <T> T readEntity(Class<T> type, Reader reader) throws IOException {
		final TypeEntity t = odbs.get(type);
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		return decode.readEntity(JSONReader.instance(reader), t, null);
	}

	public <T> T readEntity(T instence, Reader reader) throws IOException {
		final TypeEntity t = odbs.get(instence.getClass());
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		return decode.readEntity(JSONReader.instance(reader), t, instence);
	}

	public List<String> readStrings(Reader reader) throws IOException {
		final List<String> values = new ArrayList<>();
		readStrings(values, reader);
		return values;
	}

	public void readStrings(Collection<String> values, Reader reader) throws IOException {
		final JSONReader in = JSONReader.instance(reader);
		in.beginArray();
		while (in.readNext()) {
			in.readValue();
			values.add(in.getString());
		}
	}

	public ODBS odbs() {
		return odbs;
	}

	////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取类型键，当类型不明确时通过此键值指定类型名称
	 */
	public String getKeyType() {
		return encode.getKeyType();
	}

	/**
	 * 设置类型键，当类型不明确时通过此键值指定类型名称
	 */
	public void getKeyType(String value) {
		encode.setKeyType(value);
		decode.setKeyType(value);
	}

	/**
	 * 获取键名格式
	 */
	public JSONName getKeyNameFormat() {
		return encode.getKeyNameFormat();
	}

	/**
	 * 设置键名格式，默认为大骆驼（帕斯卡），此设置同时影响对序列化和反序列化
	 */
	public void setKeyNameFormat(JSONName value) {
		encode.setKeyNameFormat(value);
		decode.setKeyNameFormat(value);
	}

	/**
	 * 获取时间格式化
	 */
	public DateTimeFormatter getTimeFormatter() {
		return encode.getDateFormatter();
	}

	/**
	 * 设置时间格式化，默认为 HH:mm:ss，仅对 LocalTime 类型有效
	 */
	public void setTimeFormatter(DateTimeFormatter value) {
		encode.setTimeFormatter(value);
		decode.setTimeFormatter(value);
	}

	/**
	 * 获取日期格式化
	 */
	public DateTimeFormatter getDateFormatter() {
		return encode.getDateFormatter();
	}

	/**
	 * 设置日期格式化，默认为 uuuu-MM-dd，仅对 LocalDate 类型有效
	 */
	public void setDateFormatter(DateTimeFormatter value) {
		encode.setDateFormatter(value);
		decode.setDateFormatter(value);
	}

	/**
	 * 获取日期时间格式化
	 */
	public DateTimeFormatter getDateTimeFormatter() {
		return encode.getDateTimeFormatter();
	}

	/**
	 * 设置日期时间格式化，默认为 uuuu-MM-dd HH:mm:ss，仅对 LocalDateTime 类型有效
	 */
	public void setDateTimeFormatter(DateTimeFormatter value) {
		encode.setDateTimeFormatter(value);
		decode.setDateTimeFormatter(value);
	}

	/**
	 * 获取日期时间格式化
	 */
	public DateFormat getDateFormat() {
		return encode.getDateFormat();
	}

	/**
	 * 设置日期时间格式化，默认为 yyyy-MM-dd HH:mm:ss，仅对 Date 类型有效
	 */
	public void setDateFormat(SimpleDateFormat value) {
		encode.setDateFormat(value);
		decode.setDateFormat(value);
	}
}