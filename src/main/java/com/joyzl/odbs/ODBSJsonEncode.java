package com.joyzl.odbs;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.joyzl.EnumCode;
import com.joyzl.EnumCodeText;
import com.joyzl.EnumText;

final class ODBSJsonEncode implements ODBSEncode<JSONWriter> {

	private final ODBS odbs;

	/** 输出键名格式 */
	private JSONName KEY_NAME_FORMAT = JSONName.UPPER_CAMEL_CASE;
	/** 日期时间格式 */
	private DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/** 日期格式 */
	private DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	/** 日期时间格式 */
	private DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
	/** 时间格式 */
	private DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
	/** 表示类型的键 */
	private String KEY_TYPE = "*";

	public ODBSJsonEncode(ODBS odbs) {
		this.odbs = odbs;
	}

	/**
	 * 获取类型键，当类型不明确时通过此键值指定类型名称
	 */
	public String getKeyType() {
		return KEY_TYPE;
	}

	/**
	 * 设置类型键，当类型不明确时通过此键值指定类型名称
	 */
	public void setKeyType(String value) {
		KEY_TYPE = value;
	}

	/**
	 * 获取键名格式
	 */
	public JSONName getKeyNameFormat() {
		return KEY_NAME_FORMAT;
	}

	/**
	 * 设置键名格式，默认为大骆驼（帕斯卡），此设置同时影响对序列化和反序列化
	 */
	public void setKeyNameFormat(JSONName value) {
		KEY_NAME_FORMAT = value;
	}

	/**
	 * 获取时间格式化
	 */
	public DateTimeFormatter getTimeFormatter() {
		return TIME_FORMATTER;
	}

	/**
	 * 设置时间格式化，默认为 HH:mm:ss，仅对 LocalTime 类型有效
	 */
	public void setTimeFormatter(DateTimeFormatter value) {
		TIME_FORMATTER = value;
	}

	/**
	 * 获取日期格式化
	 */
	public DateTimeFormatter getDateFormatter() {
		return DATE_FORMATTER;
	}

	/**
	 * 设置日期格式化，默认为 uuuu-MM-dd，仅对 LocalDate 类型有效
	 */
	public void setDateFormatter(DateTimeFormatter value) {
		DATE_FORMATTER = value;
	}

	/**
	 * 获取日期时间格式化
	 */
	public DateTimeFormatter getDateTimeFormatter() {
		return DATE_TIME_FORMATTER;
	}

	/**
	 * 设置日期时间格式化，默认为 uuuu-MM-dd HH:mm:ss，仅对 LocalDateTime 类型有效
	 */
	public void setDateTimeFormatter(DateTimeFormatter value) {
		DATE_TIME_FORMATTER = value;
	}

	/**
	 * 获取日期时间格式化
	 */
	public DateFormat getDateFormat() {
		return DATE_FORMAT;
	}

	/**
	 * 设置日期时间格式化，默认为 yyyy-MM-dd HH:mm:ss，仅对 Date 类型有效
	 */
	public void setDateFormat(SimpleDateFormat value) {
		DATE_FORMAT = value;
	}

	////////////////////////////////////////////////////////////////////////////////

	@Override
	public void writeArray(JSONWriter out, ODBSType value, Object values) throws IllegalArgumentException, IOException {
		if (value instanceof ValueType) {
			// 为值数组提供特殊处理
			// 避免值被装箱为对象
			if (value.type() == byte.class) {
				final byte[] v = (byte[]) values;
				out.beginArray();
				for (int i = 0; i < v.length; i++) {
					out.writeValue(v[i]);
				}
				out.endArray();
			} else if (value.type() == int.class) {
				final int[] v = (int[]) values;
				out.beginArray();
				for (int i = 0; i < v.length; i++) {
					out.writeValue(v[i]);
				}
				out.endArray();
			} else if (value.type() == char.class) {
				final char[] v = (char[]) values;
				out.beginArray();
				for (int i = 0; i < v.length; i++) {
					out.writeValue(v[i]);
				}
				out.endArray();
			} else if (value.type() == short.class) {
				final short[] v = (short[]) values;
				out.beginArray();
				for (int i = 0; i < v.length; i++) {
					out.writeValue(v[i]);
				}
				out.endArray();
			} else if (value.type() == float.class) {
				final float[] v = (float[]) values;
				out.beginArray();
				for (int i = 0; i < v.length; i++) {
					out.writeValue(v[i]);
				}
				out.endArray();
			} else if (value.type() == double.class) {
				final double[] v = (double[]) values;
				out.beginArray();
				for (int i = 0; i < v.length; i++) {
					out.writeValue(v[i]);
				}
				out.endArray();
			} else if (value.type() == long.class) {
				final long[] v = (long[]) values;
				out.beginArray();
				for (int i = 0; i < v.length; i++) {
					out.writeValue(v[i]);
				}
				out.endArray();
			} else if (value.type() == boolean.class) {
				final boolean[] v = (boolean[]) values;
				out.beginArray();
				for (int i = 0; i < v.length; i++) {
					out.writeValue(v[i]);
				}
				out.endArray();
			} else {
				throw new IOException("未知值类型");
			}
		} else {
			final int size = Array.getLength(values);
			out.beginArray();
			for (int i = 0; i < size; i++) {
				value.write(Array.get(values, i), this, out);
			}
			out.endArray();
		}
	}

	@Override
	public void writeCollection(JSONWriter out, ODBSType value, Collection<?> values) throws IOException {
		out.beginArray();
		final Iterator<?> i = values.iterator();
		while (i.hasNext()) {
			value.write(i.next(), this, out);
		}
		out.endArray();
	}

	@Override
	public void writeEnum(JSONWriter out, Enum<?> value) throws IOException {
		out.beginObject();
		// "name":"xxx"
		out.writeKey(JSONName.NAME(KEY_NAME_FORMAT));
		out.writeValue(value.name());
		// ,"value":0
		out.writeKey(JSONName.VALUE(KEY_NAME_FORMAT));
		out.writeValue(value.ordinal());
		out.endObject();
	}

	@Override
	public void writeEnumCode(JSONWriter out, EnumCode value) throws IOException {
		out.beginObject();
		// "name":"xxx"
		out.writeKey(JSONName.NAME(KEY_NAME_FORMAT));
		out.writeValue(value.name());
		// ,"value":0
		out.writeKey(JSONName.VALUE(KEY_NAME_FORMAT));
		out.writeValue(value.code());
		out.endObject();
	}

	@Override
	public void writeEnumCodeText(JSONWriter out, EnumCodeText value) throws IOException {
		out.beginObject();
		// "name":"xxx"
		out.writeKey(JSONName.NAME(KEY_NAME_FORMAT));
		out.writeValue(value.name());
		// "text":"xxx"
		out.writeKey(JSONName.TEXT(KEY_NAME_FORMAT));
		out.writeValue(value.text());
		// ,"value":0
		out.writeKey(JSONName.VALUE(KEY_NAME_FORMAT));
		out.writeValue(value.code());
		out.endObject();
	}

	@Override
	public void writeEnumText(JSONWriter out, EnumText value) throws IOException {
		out.beginObject();
		// "name":"xxx"
		out.writeKey(JSONName.NAME(KEY_NAME_FORMAT));
		out.writeValue(value.name());
		// "text":"xxx"
		out.writeKey(JSONName.TEXT(KEY_NAME_FORMAT));
		out.writeValue(value.text());
		// ,"value":0
		out.writeKey(JSONName.VALUE(KEY_NAME_FORMAT));
		out.writeValue(value.ordinal());
		out.endObject();
	}

	@Override
	public void writeList(JSONWriter out, ODBSType value, List<?> values) throws IOException {
		out.beginArray();
		for (int i = 0; i < values.size(); i++) {
			value.write(values.get(i), this, out);
		}
		out.endArray();
	}

	@Override
	public void writeSet(JSONWriter out, ODBSType value, Set<?> values) throws IOException {
		out.beginArray();
		final Iterator<?> i = values.iterator();
		while (i.hasNext()) {
			value.write(i.next(), this, out);
		}
		out.endArray();
	}

	@Override
	public void writeMap(JSONWriter out, ODBSType key, ODBSType value, Map<?, ?> values) throws IOException {
		out.beginObject();
		final Iterator<? extends Map.Entry<?, ?>> i = values.entrySet().iterator();
		Entry<?, ?> entry;
		while (i.hasNext()) {
			entry = i.next();
			out.writeKeyBegin();
			key.write(entry.getKey(), this, out);
			out.writeKeyEnd();
			// out.writeKey(entry.getKey().toString());
			value.write(entry.getValue(), this, out);
		}
		out.endObject();
	}

	@Override
	public void writeField(JSONWriter out, ODBSMethod method) throws IOException {
		out.writeKey(method.name(KEY_NAME_FORMAT));
	}

	@Override
	public void writeEntity(JSONWriter out, TypeEntity type, Object value) throws IOException {
		out.beginObject();
		// 实体字段编码
		ODBSMethod method;
		for (int index = 0; index < type.methods().length; index++) {
			method = type.methods()[index];
			if (method.get() != null) {
				method.type().write2(value, method, this, out);
			}
		}
		out.endObject();
	}

	@Override
	public void writeObject(JSONWriter out, TypeObject type, Object value) throws IOException {
		final TypeEntity t = odbs.get(value.getClass());
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}

		out.beginObject();
		// 输出类型标记
		out.writeKey(KEY_TYPE);
		out.writeValue(t.name());
		// 实体字段编码
		ODBSMethod method;
		for (int index = 0; index < t.methods().length; index++) {
			method = t.methods()[index];
			if (method.get() != null) {
				method.type().write2(value, method, this, out);
			}
		}
		out.endObject();
	}

	@Override
	public void writeBigDecimal(JSONWriter out, BigDecimal value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeBigInteger(JSONWriter out, BigInteger value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeBoolean(JSONWriter out, boolean value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeBoolean(JSONWriter out, Boolean value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeByte(JSONWriter out, byte value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeChar(JSONWriter out, char value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeDate(JSONWriter out, Date value) throws IOException {
		out.writeValue(DATE_FORMAT, value);
	}

	@Override
	public void writeDouble(JSONWriter out, double value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeFloat(JSONWriter out, float value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeInt(JSONWriter out, int value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeLocalDateTime(JSONWriter out, LocalDateTime value) throws IOException {
		out.writeValue(DATE_TIME_FORMATTER, value);
	}

	@Override
	public void writeLocaleDate(JSONWriter out, LocalDate value) throws IOException {
		out.writeValue(DATE_FORMATTER, value);
	}

	@Override
	public void writeLocalTime(JSONWriter out, LocalTime value) throws IOException {
		out.writeValue(TIME_FORMATTER, value);
	}

	@Override
	public void writeLong(JSONWriter out, long value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeShort(JSONWriter out, short value) throws IOException {
		out.writeValue(value);
	}

	@Override
	public void writeString(JSONWriter out, String value) throws IOException {
		out.writeValue(value);
	}
}