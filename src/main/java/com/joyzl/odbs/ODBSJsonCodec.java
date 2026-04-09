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
import java.util.Arrays;
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

abstract class ODBSJsonCodec extends ODBSCodec<JSONWriter, JSONReader> {

	protected final ODBS odbs;

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

	public ODBSJsonCodec(ODBS odbs) {
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
	public void getKeyType(String value) {
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
	Object readArray(JSONReader in, ODBSType value, Object values) throws IOException {
		in.beginArray();
		if (values == null) {
			values = value.newArray(100);
		}
		int i = 0;
		if (value instanceof ValueType) {
			// 为值数组提供特殊处理
			// 避免值被装箱为对象
			if (value.type() == byte.class) {
				byte[] a = (byte[]) values;
				while (in.readNext()) {
					if (i >= a.length) {
						a = Arrays.copyOf(a, i * 2);
					}
					in.readValue();
					a[i++] = in.getByte();
				}
				if (a.length != i) {
					values = Arrays.copyOf(a, i);
				} else {
					values = a;
				}
			} else if (value.type() == int.class) {
				int[] a = (int[]) values;
				while (in.readNext()) {
					if (i >= a.length) {
						a = Arrays.copyOf(a, i * 2);
					}
					in.readValue();
					a[i++] = in.getInt();
				}
				if (a.length != i) {
					values = Arrays.copyOf(a, i);
				} else {
					values = a;
				}
			} else if (value.type() == char.class) {
				char[] a = (char[]) values;
				while (in.readNext()) {
					if (i >= a.length) {
						a = Arrays.copyOf(a, i * 2);
					}
					in.readValue();
					a[i++] = in.getChar();
				}
				if (a.length != i) {
					values = Arrays.copyOf(a, i);
				} else {
					values = a;
				}
			} else if (value.type() == short.class) {
				short[] a = (short[]) values;
				while (in.readNext()) {
					if (i >= a.length) {
						a = Arrays.copyOf(a, i * 2);
					}
					in.readValue();
					a[i++] = in.getShort();
				}
				if (a.length != i) {
					values = Arrays.copyOf(a, i);
				} else {
					values = a;
				}
			} else if (value.type() == float.class) {
				float[] a = (float[]) values;
				while (in.readNext()) {
					if (i >= a.length) {
						a = Arrays.copyOf(a, i * 2);
					}
					in.readValue();
					a[i++] = in.getFloat();
				}
				if (a.length != i) {
					values = Arrays.copyOf(a, i);
				} else {
					values = a;
				}
			} else if (value.type() == double.class) {
				double[] a = (double[]) values;
				while (in.readNext()) {
					if (i >= a.length) {
						a = Arrays.copyOf(a, i * 2);
					}
					in.readValue();
					a[i++] = in.getDouble();
				}
				if (a.length != i) {
					values = Arrays.copyOf(a, i);
				} else {
					values = a;
				}
			} else if (value.type() == long.class) {
				long[] a = (long[]) values;
				while (in.readNext()) {
					if (i >= a.length) {
						a = Arrays.copyOf(a, i * 2);
					}
					in.readValue();
					a[i++] = in.getLong();
				}
				if (a.length != i) {
					values = Arrays.copyOf(a, i);
				} else {
					values = a;
				}
			} else if (value.type() == boolean.class) {
				boolean[] a = (boolean[]) values;
				while (in.readNext()) {
					if (i >= a.length) {
						a = Arrays.copyOf(a, i * 2);
					}
					in.readValue();
					a[i++] = in.getBoolean();
				}
				if (a.length != i) {
					values = Arrays.copyOf(a, i);
				} else {
					values = a;
				}
			} else {
				throw new IOException("未知值类型");
			}
		} else {
			Object[] a = (Object[]) values;
			while (in.readNext()) {
				if (i >= a.length) {
					a = Arrays.copyOf(a, i * 2);
				}
				a[i++] = value.read(this, in);
			}
			if (a.length != i) {
				values = Arrays.copyOf(a, i);
			} else {
				values = a;
			}
		}
		return values;
	}

	@Override
	<T> T readEntity(JSONReader in, TypeEntity type, T entity) throws IOException {
		in.beginObject();
		if (entity == null) {
			entity = type.newInstance();
		}

		ODBSMethod method;
		while (in.readKey()) {
			method = type.find(KEY_NAME_FORMAT, in.chars());
			if (method == null || method.set() == null) {
				in.readIgnore();
			} else {
				if ("EnumCodes".contentEquals(in.chars())) {
					in.chars().toString();
				}
				method.type().read(entity, method, this, in);
			}
		}
		return entity;
	}

	@Override
	Object readObject(JSONReader in, TypeObject type, Object value) throws IOException {
		in.beginObject();
		if (in.readKey()) {
			if (KEY_TYPE.contentEquals(in.chars())) {
				in.readValue();
				final TypeEntity t = odbs.find(in.chars());
				if (t == null) {
					throw new IOException("ODBSJson 类型无效 " + in.chars());
				}

				if (value == null) {
					value = t.newInstance();
				}

				ODBSMethod method;
				while (in.readKey()) {
					method = t.find(KEY_NAME_FORMAT, in.chars());
					if (method == null || method.set() == null) {
						in.readIgnore();
					} else {
						method.type().read(value, method, this, in);
					}
				}
				return value;
			} else {
				throw new IOException("ODBSJson 无类型标记");
			}
		} else {
			throw new IOException("ODBSJson 无类型标记");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	<K, V> void readMap(JSONReader in, ODBSType key, ODBSType value, Map<K, V> values) throws IOException {
		in.beginObject();
		Object k;
		while (in.readKey()) {

			// 临时解决方案
			if (key.type() == Boolean.class)
				k = in.getBoolean();
			else if (key.type() == Byte.class)
				k = in.getByte();
			else if (key.type() == Character.class)
				k = in.getChar();
			else if (key.type() == Short.class)
				k = in.getShort();
			else if (key.type() == Integer.class)
				k = in.getInt();
			else if (key.type() == Long.class)
				k = in.getLong();
			else if (key.type() == Float.class)
				k = in.getFloat();
			else if (key.type() == Double.class)
				k = in.getDouble();
			else if (key.type() == BigDecimal.class)
				k = in.getBigDecimal();
			else if (key.type() == BigInteger.class)
				k = in.getBigInteger();
			else if (key.type() == Date.class)
				k = in.getDate(DATE_FORMAT);
			else if (key.type() == LocalTime.class)
				k = in.getLocalTime(TIME_FORMATTER);
			else if (key.type() == LocalDate.class)
				k = in.getLocalDate(DATE_FORMATTER);
			else if (key.type() == LocalDateTime.class)
				k = in.getLocalDateTime(DATE_TIME_FORMATTER);
			else if (key.type() == String.class)
				k = in.getString();
			else
				throw new IOException("不支持的键类型");

			values.put((K) k, (V) value.read(this, in));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	<V> void readCollection(JSONReader in, ODBSType value, Collection<V> values) throws IOException {
		in.beginArray();
		while (in.readNext()) {
			values.add((V) value.read(this, in));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	<V> void readList(JSONReader in, ODBSType value, List<V> values) throws IOException {
		in.beginArray();
		while (in.readNext()) {
			values.add((V) value.read(this, in));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	<V> void readSet(JSONReader in, ODBSType value, Set<V> values) throws IOException {
		in.beginArray();
		while (in.readNext()) {
			values.add((V) value.read(this, in));
		}
	}

	@Override
	int readEnum(JSONReader in) throws IOException {
		return in.readEnumValue(KEY_NAME_FORMAT);
	}

	@Override
	int readEnumCode(JSONReader in) throws IOException {
		return in.readEnumValue(KEY_NAME_FORMAT);
	}

	@Override
	int readEnumCodeText(JSONReader in) throws IOException {
		return in.readEnumValue(KEY_NAME_FORMAT);
	}

	@Override
	int readEnumText(JSONReader in) throws IOException {
		return in.readEnumValue(KEY_NAME_FORMAT);
	}

	@Override
	LocalDate readLocalDate(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getLocalDate(DATE_FORMATTER);
		}
		return null;
	}

	@Override
	LocalDateTime readLocalDateTime(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getLocalDateTime(DATE_TIME_FORMATTER);
		}
		return null;
	}

	@Override
	LocalTime readLocalTime(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getLocalTime(TIME_FORMATTER);
		}
		return null;
	}

	@Override
	long readLong(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getLong();
		}
		return 0;
	}

	@Override
	short readShort(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getShort();
		}
		return 0;
	}

	@Override
	String readString(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getString();
		}
		return null;
	}

	@Override
	BigDecimal readBigDecimal(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getBigDecimal();
		}
		return null;
	}

	@Override
	BigInteger readBigInteger(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getBigInteger();
		}
		return null;
	}

	@Override
	boolean readBool(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getBoolean();
		}
		return false;
	}

	@Override
	boolean readBoolea(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getBoolean();
		}
		return false;
	}

	@Override
	byte readByte(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getByte();
		}
		return 0;
	}

	@Override
	char readChar(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getChar();
		}
		return 0;
	}

	@Override
	Date readDate(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getDate(DATE_FORMAT);
		}
		return null;
	}

	@Override
	double readDouble(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getDouble();
		}
		return 0;
	}

	@Override
	float readFloat(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getFloat();
		}
		return 0;
	}

	@Override
	int readInt(JSONReader in) throws IOException {
		if (in.readValue()) {
			return in.getInt();
		}
		return 0;
	}

	@Override
	void writeArray(JSONWriter out, ODBSType value, Object values) throws IllegalArgumentException, IOException {
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
	void writeCollection(JSONWriter out, ODBSType value, Collection<?> values) throws IOException {
		out.beginArray();
		final Iterator<?> i = values.iterator();
		while (i.hasNext()) {
			value.write(i.next(), this, out);
		}
		out.endArray();
	}

	@Override
	void writeEnum(JSONWriter out, Enum<?> value) throws IOException {
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
	void writeEnumCode(JSONWriter out, EnumCode value) throws IOException {
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
	void writeEnumCodeText(JSONWriter out, EnumCodeText value) throws IOException {
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
	void writeEnumText(JSONWriter out, EnumText value) throws IOException {
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
	void writeList(JSONWriter out, ODBSType value, List<?> values) throws IOException {
		out.beginArray();
		for (int i = 0; i < values.size(); i++) {
			value.write(values.get(i), this, out);
		}
		out.endArray();
	}

	@Override
	void writeSet(JSONWriter out, ODBSType value, Set<?> values) throws IOException {
		out.beginArray();
		final Iterator<?> i = values.iterator();
		while (i.hasNext()) {
			value.write(i.next(), this, out);
		}
		out.endArray();
	}

	@Override
	void writeMap(JSONWriter out, ODBSType key, ODBSType value, Map<?, ?> values) throws IOException {
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
	void writeField(JSONWriter out, ODBSMethod method) throws IOException {
		out.writeKey(method.name(KEY_NAME_FORMAT));
	}

	@Override
	void writeEntity(JSONWriter out, TypeEntity type, Object value) throws IOException {
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
	void writeObject(JSONWriter out, TypeObject type, Object value) throws IOException {
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
	void writeBigDecimal(JSONWriter out, BigDecimal value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeBigInteger(JSONWriter out, BigInteger value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeBoolean(JSONWriter out, boolean value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeBoolean(JSONWriter out, Boolean value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeByte(JSONWriter out, byte value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeChar(JSONWriter out, char value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeDate(JSONWriter out, Date value) throws IOException {
		out.writeValue(DATE_FORMAT, value);
	}

	@Override
	void writeDouble(JSONWriter out, double value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeFloat(JSONWriter out, float value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeInt(JSONWriter out, int value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeLocalDateTime(JSONWriter out, LocalDateTime value) throws IOException {
		out.writeValue(DATE_TIME_FORMATTER, value);
	}

	@Override
	void writeLocaleDate(JSONWriter out, LocalDate value) throws IOException {
		out.writeValue(DATE_FORMATTER, value);
	}

	@Override
	void writeLocalTime(JSONWriter out, LocalTime value) throws IOException {
		out.writeValue(TIME_FORMATTER, value);
	}

	@Override
	void writeLong(JSONWriter out, long value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeShort(JSONWriter out, short value) throws IOException {
		out.writeValue(value);
	}

	@Override
	void writeString(JSONWriter out, String value) throws IOException {
		out.writeValue(value);
	}
}