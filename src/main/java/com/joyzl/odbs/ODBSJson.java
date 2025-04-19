/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * ODBS JSON序列化/反序列化编码
 * <p>
 * JSON规范 RFC4627 https://www.json.org
 * </p>
 * 
 * @author ZhangXi
 * @date 2020年7月28日
 */
public final class ODBSJson {

	private final ODBS odbs;

	public ODBSJson(ODBS o) {
		if (o == null) {
			throw new NullPointerException("ODBS");
		}
		odbs = o;
	}

	/** 是否忽略空值 */
	private boolean IGNORE_NULL = true;
	/** 忽略未定义的字段 */
	private boolean IGNORE_UNDEFINED_FIELD = true;
	/** 是否用双引号包围键名 */
	private boolean QUOTES_KEY = true;
	/** 是否输出枚举对象 */
	private boolean ENUM_OBJECT = true;
	/** 键名格式 */
	private JSONName KEY_NAME_FORMAT = JSONName.DEFAULT;

	/** 时间格式 */
	private DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
	/** 日期格式 */
	private DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd");
	/** 日期时间格式 */
	private DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");
	/** 日期时间格式 */
	private Supplier<DateFormat> DATE_FORMAT = new Supplier<DateFormat>() {
		@Override
		public DateFormat get() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};
	/** 数字格式化 */
	private Supplier<NumberFormat> NUMBER_FORMAT = new Supplier<NumberFormat>() {
		@Override
		public NumberFormat get() {
			NumberFormat format = NumberFormat.getInstance();
			format.setGroupingUsed(false);
			format.setMaximumFractionDigits(Double.MAX_EXPONENT);
			return format;
		}
	};

	public final void writeEntity(Object entity, Writer writer) throws IOException {
		if (entity == null) {
			return;
		}

		writeEntity(odbs.findDesc(entity.getClass()), entity, JSONCodec.instence(this, writer));
	}

	public final void writeEntity(Collection<?> entities, Writer writer) throws IOException {
		if (entities == null) {
			return;
		}
		if (entities.size() == 0) {
			writer.write(JSONCodec.ARRAY_BEGIN);
			writer.write(JSONCodec.ARRAY_END);
			return;
		}

		JSONCodec codec = JSONCodec.instence(this, writer);
		codec.writeTag(JSONCodec.ARRAY_BEGIN);
		for (Object value : entities) {
			int type = ODBSTypes.getType(value.getClass());
			if (ODBSTypes.isBase(type)) {
				writeBase(type, value, codec);
			} else if (ODBSTypes.isEnum(type)) {
				writeEnum(odbs.findEnum(value.getClass()), value, codec);
			} else if (ODBSTypes.isEntity(type)) {
				writeEntity(odbs.findDesc(value.getClass()), value, codec);
			} else if (ODBSTypes.isArray(type)) {
				// writeArray(type.further(), (Object[]) value, codec);
				throw new IllegalStateException("意外的集合值类型:" + type);
			} else if (ODBSTypes.isList(type)) {
				// writeList(type.further(), (List<?>) value, codec);
				throw new IllegalStateException("意外的集合值类型:" + type);
			} else if (ODBSTypes.isSet(type)) {
				// writeSet(type.further(), (Set<?>) value, codec);
				throw new IllegalStateException("意外的集合值类型:" + type);
			} else if (ODBSTypes.isMap(type)) {
				// writeMap(type.further(), (Map<?, ?>) value, codec);
				throw new IllegalStateException("意外的集合值类型:" + type);
			} else {
				throw new IllegalStateException("意外的集合值类型:" + type);
			}
		}
		codec.writeTag(JSONCodec.ARRAY_END);
	}

	private final void writeEntity(int type, Object entity, JSONCodec writer) throws IOException {
		ODBSDescription description = odbs.findDesc(type);
		if (description == null) {
			throw new RuntimeException("对象描述索引不存在" + type);
		}
		if (description.getSourceClass() == entity.getClass()) {
			// 实例与定义相同
		} else {
			// 实体可能存在继承关系
			description = odbs.findDesc(entity.getClass());
		}
		writeEntity(description, entity, writer);
	}

	private final void writeEntity(ODBSDescription description, Object entity, JSONCodec writer) throws IOException {
		if (description == null) {
			throw new RuntimeException("对象描述不存在" + entity.getClass().getName());
		}

		writer.writeTag(JSONCodec.OBJECT_BEGIN);
		// 字段序列
		Object value;
		ODBSField field;
		for (int index = 0; index < description.getFieldCount(); index++) {
			field = description.getField(index);
			if (field.hasGetter()) {
				value = field.getValue(entity);
				if (value == null) {
					if (IGNORE_NULL) {
						// 忽略空值
					} else {
						writer.writeKey(field.getName(KEY_NAME_FORMAT));
						writer.writeValue(JSONCodec.NULL);
					}
					continue;
				} else if (ODBSTypes.isValue(field.getType().value())) {
					writer.writeKey(field.getName(KEY_NAME_FORMAT));
					writeValue(field.getType().value(), value, writer);
				} else if (ODBSTypes.isBase(field.getType().value())) {
					writer.writeKey(field.getName(KEY_NAME_FORMAT));
					writeBase(field.getType().value(), value, writer);
				} else if (ODBSTypes.isEnum(field.getType().value())) {
					writer.writeKey(field.getName(KEY_NAME_FORMAT));
					writeEnum(field.getType().value(), value, writer);
				} else if (ODBSTypes.isEntity(field.getType().value())) {
					writer.writeKey(field.getName(KEY_NAME_FORMAT));
					writeEntity(field.getType().value(), value, writer);
				} else if (ODBSTypes.isArray(field.getType().value())) {
					writer.writeKey(field.getName(KEY_NAME_FORMAT));
					writeArray(field.getType().further(), value, writer);
				} else if (ODBSTypes.isList(field.getType().value())) {
					writer.writeKey(field.getName(KEY_NAME_FORMAT));
					writeList(field.getType().further(), (List<?>) value, writer);
				} else if (ODBSTypes.isSet(field.getType().value())) {
					writer.writeKey(field.getName(KEY_NAME_FORMAT));
					writeSet(field.getType().further(), (Set<?>) value, writer);
				} else if (ODBSTypes.isMap(field.getType().value())) {
					writer.writeKey(field.getName(KEY_NAME_FORMAT));
					writeMap(field.getType().further(), (Map<?, ?>) value, writer);
				} else if (ODBSTypes.isAny(field.getType().value())) {
					// TODO 需要优化，目前仅转换为字符串输出
					writer.writeKey(field.getName(KEY_NAME_FORMAT));
					writer.writeString(value.toString());
				} else {
					throw new IllegalStateException("不支持的数据类型:" + field);
				}
			}
		}
		writer.writeTag(JSONCodec.OBJECT_END);
	}

	private final void writeKey(int type, Object value, JSONCodec writer) throws IOException {
		switch (type) {
			case ODBSTypes.BOOLEAN:
				writer.writeKey(value.toString());
				break;
			case ODBSTypes.BYTE:
				writer.writeKey(value.toString());
				break;
			case ODBSTypes.CHARACTER:
				writer.writeKey(value.toString());
				break;
			case ODBSTypes.SHORT:
				writer.writeKey(value.toString());
				break;
			case ODBSTypes.INTEGER:
				writer.writeKey(value.toString());
				break;
			case ODBSTypes.LONG:
				writer.writeKey(value.toString());
				break;
			case ODBSTypes.FLOAT:
				writer.writeKey(writer.NUMBER_FORMAT.format(value));
				break;
			case ODBSTypes.DOUBLE:
				writer.writeKey(writer.NUMBER_FORMAT.format(value));
				break;
			case ODBSTypes.BIG_DECIMAL:
				writer.writeKey(value.toString());
				break;
			case ODBSTypes.DATE:
				writer.writeKey(writer.DATE_FORMAT.format(value));
				break;
			case ODBSTypes.LOCAL_TIME:
				writer.writeKey(TIME_FORMATTER.format((LocalTime) value));
				break;
			case ODBSTypes.LOCAL_DATE:
				writer.writeKey(DATE_FORMATTER.format((LocalDate) value));
				break;
			case ODBSTypes.LOCAL_DATE_TIME:
				writer.writeKey(DATE_TIME_FORMATTER.format((LocalDateTime) value));
				break;
			case ODBSTypes.STRING:
				writer.writeKey(value.toString());
				break;
			default:
				throw new IllegalStateException("意外的基础类型:" + type);
		}
	}

	private final void writeValue(int type, Object value, JSONCodec writer) throws IOException {
		switch (type) {
			case ODBSTypes._boolean:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes._byte:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes._char:
				writer.writeString(value.toString());
				break;
			case ODBSTypes._short:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes._int:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes._long:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes._float:
				writer.writeValue(writer.NUMBER_FORMAT.format(value));
				break;
			case ODBSTypes._double:
				writer.writeValue(writer.NUMBER_FORMAT.format(value));
				break;
			default:
				throw new IllegalStateException("意外的值类型:" + type);
		}
	}

	private final void writeBase(int type, Object value, JSONCodec writer) throws IOException {
		switch (type) {
			case ODBSTypes.BOOLEAN:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes.BYTE:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes.CHARACTER:
				writer.writeString(value.toString());
				break;
			case ODBSTypes.SHORT:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes.INTEGER:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes.LONG:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes.FLOAT:
				writer.writeValue(writer.NUMBER_FORMAT.format(value));
				break;
			case ODBSTypes.DOUBLE:
				writer.writeValue(writer.NUMBER_FORMAT.format(value));
				break;
			case ODBSTypes.BIG_DECIMAL:
				writer.writeValue(value.toString());
				break;
			case ODBSTypes.DATE:
				writer.writeString(writer.DATE_FORMAT.format(value));
				break;
			case ODBSTypes.LOCAL_TIME:
				writer.writeString(TIME_FORMATTER.format((LocalTime) value));
				break;
			case ODBSTypes.LOCAL_DATE:
				writer.writeString(DATE_FORMATTER.format((LocalDate) value));
				break;
			case ODBSTypes.LOCAL_DATE_TIME:
				writer.writeString(DATE_TIME_FORMATTER.format((LocalDateTime) value));
				break;
			case ODBSTypes.STRING:
				writer.writeString(value.toString());
				break;
			default:
				throw new IllegalStateException("意外的基础类型:" + type);
		}
	}

	private final void writeEnum(int type, Object value, JSONCodec writer) throws IOException {
		writeEnum(odbs.findEnum(type), value, writer);
	}

	private final void writeEnum(ODBSEnumeration type, Object value, JSONCodec writer) throws IOException {
		if (ENUM_OBJECT) {
			writer.writeTag(JSONCodec.OBJECT_BEGIN);
			writer.writeKey("value");
			writer.writeValue(Integer.toString(type.getValue(value)));
			String name = type.getName(value);
			if (name != null) {
				writer.writeKey("name");
				writer.writeString(name);
			}
			name = type.getText(value);
			if (name != null) {
				writer.writeKey("text");
				writer.writeString(name);
			}
			writer.writeTag(JSONCodec.OBJECT_END);
		} else {
			writer.writeValue(Integer.toString(type.getValue(value)));
		}
	}

	private final void writeArray(ODBSType type, Object values, JSONCodec writer) throws IOException {
		if (ODBSTypes.isValue(type.value())) {
			writeValues(type.value(), values, writer);
		} else if (ODBSTypes.isBase(type.value())) {
			writeBases(type.value(), values, writer);
		} else if (ODBSTypes.isEnum(type.value())) {
			final int length = Array.getLength(values);
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (int index = 0; index < length; index++) {
				writeEnum(type.value(), Array.get(values, index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isEntity(type.value())) {
			final int length = Array.getLength(values);
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (int index = 0; index < length; index++) {
				writeEntity(type.value(), Array.get(values, index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isArray(type.value())) {
			final int length = Array.getLength(values);
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (int index = 0; index < length; index++) {
				writeArray(type.further(), Array.get(values, index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isList(type.value())) {
			final int length = Array.getLength(values);
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (int index = 0; index < length; index++) {
				writeList(type.further(), (List<?>) Array.get(values, index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isSet(type.value())) {
			final int length = Array.getLength(values);
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (int index = 0; index < length; index++) {
				writeSet(type.further(), (Set<?>) Array.get(values, index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isMap(type.value())) {
			final int length = Array.getLength(values);
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (int index = 0; index < length; index++) {
				writeMap(type.further(), (Map<?, ?>) Array.get(values, index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else {
			throw new IllegalStateException("意外的数组值类型:" + type);
		}
	}

	private final void writeValues(int type, Object values, JSONCodec writer) throws IOException {
		final int length = Array.getLength(values);
		writer.writeTag(JSONCodec.ARRAY_BEGIN);
		switch (type) {
			case ODBSTypes._boolean:
				for (int index = 0; index < length; index++) {
					writer.writeValue(Boolean.toString(Array.getBoolean(values, index)));
				}
				break;
			case ODBSTypes._byte:
				for (int index = 0; index < length; index++) {
					writer.writeValue(Byte.toString(Array.getByte(values, index)));
				}
				break;
			case ODBSTypes._char:
				for (int index = 0; index < length; index++) {
					writer.writeString(Character.toString(Array.getChar(values, index)));
				}
				break;
			case ODBSTypes._short:
				for (int index = 0; index < length; index++) {
					writer.writeValue(Short.toString(Array.getShort(values, index)));
				}
				break;
			case ODBSTypes._int:
				for (int index = 0; index < length; index++) {
					writer.writeValue(Integer.toString(Array.getInt(values, index)));
				}
				break;
			case ODBSTypes._long:
				for (int index = 0; index < length; index++) {
					writer.writeValue(Long.toString(Array.getLong(values, index)));
				}
				break;
			case ODBSTypes._float:
				for (int index = 0; index < length; index++) {
					writer.writeValue(writer.NUMBER_FORMAT.format(Array.getFloat(values, index)));
				}
				break;
			case ODBSTypes._double:
				for (int index = 0; index < length; index++) {
					writer.writeValue(writer.NUMBER_FORMAT.format(Array.getDouble(values, index)));
				}
				break;
			default:
				throw new IllegalStateException("意外的数组值类型:" + type);
		}
		writer.writeTag(JSONCodec.ARRAY_END);
	}

	private final void writeBases(int type, Object values, JSONCodec writer) throws IOException {
		writer.writeTag(JSONCodec.ARRAY_BEGIN);
		final int length = Array.getLength(values);
		Object value = null;
		switch (type) {
			case ODBSTypes.BOOLEAN:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeValue(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			case ODBSTypes.BYTE:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeValue(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			case ODBSTypes.CHARACTER:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeString(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			case ODBSTypes.SHORT:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeValue(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			case ODBSTypes.INTEGER:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeValue(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			case ODBSTypes.LONG:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeValue(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			case ODBSTypes.FLOAT:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeValue(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			case ODBSTypes.DOUBLE:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeValue(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			case ODBSTypes.BIG_DECIMAL:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeValue(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			case ODBSTypes.DATE:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeString(value == null ? JSONCodec.NULL : writer.DATE_FORMAT.format(value));
				}
				break;
			case ODBSTypes.LOCAL_TIME:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeString(value == null ? JSONCodec.NULL : TIME_FORMATTER.format((LocalTime) value));
				}
				break;
			case ODBSTypes.LOCAL_DATE:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeString(value == null ? JSONCodec.NULL : DATE_FORMATTER.format((LocalDate) value));
				}
				break;
			case ODBSTypes.LOCAL_DATE_TIME:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeString(value == null ? JSONCodec.NULL : DATE_TIME_FORMATTER.format((LocalDateTime) value));
				}
				break;
			case ODBSTypes.STRING:
				for (int index = 0; index < length; index++) {
					value = Array.get(values, index);
					writer.writeString(value == null ? JSONCodec.NULL : value.toString());
				}
				break;
			default:
				throw new IllegalStateException("意外的数组基础类型:" + type);
		}
		writer.writeTag(JSONCodec.ARRAY_END);
	}

	private final void writeList(ODBSType type, List<?> values, JSONCodec writer) throws IOException {
		if (ODBSTypes.isBase(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			int length = values.size();
			for (int index = 0; index < length; index++) {
				writeBase(type.value(), values.get(index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isEnum(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			int length = values.size();
			for (int index = 0; index < length; index++) {
				writeEnum(type.value(), values.get(index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isEntity(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			int length = values.size();
			for (int index = 0; index < length; index++) {
				writeEntity(type.value(), values.get(index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isArray(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			int length = values.size();
			for (int index = 0; index < length; index++) {
				writeArray(type.further(), (Object[]) values.get(index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isList(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			int length = values.size();
			for (int index = 0; index < length; index++) {
				writeList(type.further(), (List<?>) values.get(index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isSet(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			int length = values.size();
			for (int index = 0; index < length; index++) {
				writeSet(type.further(), (Set<?>) values.get(index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isMap(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			int length = values.size();
			for (int index = 0; index < length; index++) {
				writeMap(type.further(), (Map<?, ?>) values.get(index), writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else {
			throw new IllegalStateException("意外的List值类型:" + type);
		}
	}

	private final void writeSet(ODBSType type, Set<?> values, JSONCodec writer) throws IOException {
		if (ODBSTypes.isBase(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (Object value : values) {
				writeBase(type.value(), value, writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isEnum(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (Object value : values) {
				writeEnum(type.value(), value, writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isEntity(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (Object value : values) {
				writeEntity(type.value(), value, writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isArray(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (Object value : values) {
				writeArray(type.further(), value, writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isList(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (Object value : values) {
				writeList(type.further(), (List<?>) value, writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isSet(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (Object value : values) {
				writeSet(type.further(), (Set<?>) value, writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else if (ODBSTypes.isMap(type.value())) {
			writer.writeTag(JSONCodec.ARRAY_BEGIN);
			for (Object value : values) {
				writeSet(type.further(), (Set<?>) value, writer);
			}
			writer.writeTag(JSONCodec.ARRAY_END);
		} else {
			throw new IllegalStateException("意外的Set值类型:" + type);
		}
	}

	private final void writeMap(ODBSType type, Map<?, ?> values, JSONCodec writer) throws IOException {
		if (ODBSTypes.isBase(type.key())) {
			if (ODBSTypes.isBase(type.value())) {
				writer.writeTag(JSONCodec.OBJECT_BEGIN);
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeKey(type.key(), value.getKey(), writer);
					writeBase(type.value(), value.getValue(), writer);
				}
				writer.writeTag(JSONCodec.OBJECT_END);
			} else if (ODBSTypes.isEnum(type.value())) {
				writer.writeTag(JSONCodec.OBJECT_BEGIN);
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeKey(type.key(), value.getKey(), writer);
					writeEnum(type.value(), value.getValue(), writer);
				}
				writer.writeTag(JSONCodec.OBJECT_END);
			} else if (ODBSTypes.isEntity(type.value())) {
				writer.writeTag(JSONCodec.OBJECT_BEGIN);
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeKey(type.key(), value.getKey(), writer);
					writeEntity(type.value(), value.getValue(), writer);
				}
				writer.writeTag(JSONCodec.OBJECT_END);
			} else if (ODBSTypes.isArray(type.value())) {
				writer.writeTag(JSONCodec.OBJECT_BEGIN);
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeKey(type.key(), value.getKey(), writer);
					writeArray(type.further(), value.getValue(), writer);
				}
				writer.writeTag(JSONCodec.OBJECT_END);
			} else if (ODBSTypes.isList(type.value())) {
				writer.writeTag(JSONCodec.OBJECT_BEGIN);
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeKey(type.key(), value.getKey(), writer);
					writeList(type.further(), (List<?>) value.getValue(), writer);
				}
				writer.writeTag(JSONCodec.OBJECT_END);
			} else if (ODBSTypes.isSet(type.value())) {
				writer.writeTag(JSONCodec.OBJECT_BEGIN);
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeKey(type.key(), value.getKey(), writer);
					writeSet(type.further(), (Set<?>) value.getValue(), writer);
				}
				writer.writeTag(JSONCodec.OBJECT_END);
			} else if (ODBSTypes.isMap(type.value())) {
				writer.writeTag(JSONCodec.OBJECT_BEGIN);
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeKey(type.key(), value.getKey(), writer);
					writeMap(type.further(), (Map<?, ?>) value.getValue(), writer);
				}
				writer.writeTag(JSONCodec.OBJECT_END);
			} else {
				throw new IllegalStateException("意外的Map值类型:" + type);
			}
		} else if (ODBSTypes.isEntity(type.key())) {
			throw new IllegalStateException("不支持Entity作为Map键类型:" + type);
		} else if (ODBSTypes.isArray(type.key())) {
			throw new IllegalStateException("不支持Array作为Map键类型:" + type);
		} else if (ODBSTypes.isList(type.key())) {
			throw new IllegalStateException("不支持List作为Map键类型:" + type);
		} else if (ODBSTypes.isSet(type.key())) {
			throw new IllegalStateException("不支持Set作为Map键类型:" + type);
		} else if (ODBSTypes.isMap(type.key())) {
			throw new IllegalStateException("不支持Map作为Map键类型:" + type);
		} else {
			throw new IllegalStateException("意外的Map键类型:" + type);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	public final Object readEntity(Class<?> clazz, Reader reader) throws IOException, ParseException {
		final ODBSDescription description = odbs.findDesc(clazz);
		if (description == null) {
			throw new RuntimeException("对象描述不存在" + clazz.getName());
		}
		if (reader.ready()) {
			return readEntity(description, null, JSONCodec.instence(this, reader));
		} else {
			return null;
		}
	}

	public final Object readEntity(Object instence, Reader reader) throws IOException, ParseException {
		// JSON 格式不具备额外的对象类型标识，因此必须由外部指定实例
		// 外部程序可通过URL或者参数确定JSON填充的对象实例
		final ODBSDescription description = odbs.findDesc(instence.getClass());
		if (description == null) {
			throw new RuntimeException("对象描述不存在" + instence.getClass().getName());
		}
		if (reader.ready()) {
			return readEntity(description, instence, JSONCodec.instence(this, reader));
		} else {
			return instence;
		}
	}

	private final Object readEntity(ODBSDescription description, Object entity, JSONCodec reader) throws IOException, ParseException {
		// 开始
		if (reader.readSkip() > 0) {
			if (reader.lastChar() == JSONCodec.OBJECT_BEGIN) {
				// 开始读取跟对象 {...}
				entity = readEntity(description, entity, reader, true);
			} else if (reader.lastChar() == JSONCodec.ARRAY_BEGIN) {
				// 开始读取跟数组 [] [...]
				// 如果数组元素为JSON对象则类型为指定的类型
				// [{},{}]
				// 如果数据元素为基础数据类型，则全部按字符串读取，因为Java不支持JavaScript的混合类型
				// ["A","B"]、[true,false]、[1,2,3]
				final List<Object> list = new ArrayList<>();
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readSkip() == JSONCodec.OBJECT_BEGIN) {
						list.add(readEntity(description, null, reader, false));
					} else if (reader.lastChar() == JSONCodec.ARRAY_END) {
						// 空集合[]
						break;
					} else {
						if (reader.readValue()) {
							// 值数组以字符串方式返回
							list.add(reader.getString());
						}
					}
				}
				entity = list;
			} else {
				throw new ParseException("意外的开始字符:\'" + reader.lastChar() + "\' " + reader.lastValue(), 0);
			}
		} else {
			// 流已结束
		}
		return entity;
	}

	/**
	 * 都对象，开始标记是否确认(tag) readEntity(description, null, reader, false);
	 */
	private final Object readEntity(ODBSType type, JSONCodec reader, boolean tag) throws IOException, ParseException {
		final ODBSDescription description = odbs.findDesc(type.value());
		if (description == null) {
			throw new RuntimeException("对象描述不存在" + type);
		}
		if (tag) {
			return readEntity(description, null, reader, false);
		}
		if (reader.readSkip() == JSONCodec.OBJECT_BEGIN) {
			return readEntity(description, null, reader, false);
		} else {
			throw new ParseException("意外字符:" + reader.lastChar(), reader.getIndex());
		}
	}

	/**
	 * 读取对象，开始标记已确认
	 */
	private final Object readEntity(ODBSDescription description, Object entity, JSONCodec reader, boolean root) throws IOException, ParseException {
		// {}
		// { name:value }
		// { "name1":"value","name2":true }
		if (entity == null) {
			entity = description.newInstence();
		}
		ODBSField field;
		while (reader.lastChar() != JSONCodec.OBJECT_END) {
			if (reader.readKey()) {
				field = description.getField(KEY_NAME_FORMAT, reader.getString());
				if (field == null) {
					if (IGNORE_UNDEFINED_FIELD) {
						reader.readIgnore();
					} else {
						throw new IOException("未定义的字段:" + reader.getString());
					}
				} else {
					if (field.hasSetter()) {
						// "key":value
						// 必须具有可读值，否则认为JSON格式错误
						if (ODBSTypes.isValue(field.getType().value())) {
							field.setValue(entity, readValue(field.getType(), reader));
						} else if (ODBSTypes.isBase(field.getType().value())) {
							field.setValue(entity, readBase(field.getType(), reader));
						} else if (ODBSTypes.isEnum(field.getType().value())) {
							field.setValue(entity, readEnum(field.getType(), reader));
						} else if (ODBSTypes.isEntity(field.getType().value())) {
							field.setValue(entity, readEntity(field.getType(), reader, false));
						} else if (ODBSTypes.isArray(field.getType().value())) {
							field.setValue(entity, readArray(field.getType().further(), reader));
						} else if (ODBSTypes.isList(field.getType().value())) {
							field.setValue(entity, readList(field.getValue(entity), field.getType().further(), reader));
						} else if (ODBSTypes.isSet(field.getType().value())) {
							field.setValue(entity, readSet(field.getValue(entity), field.getType().further(), reader));
						} else if (ODBSTypes.isMap(field.getType().value())) {
							field.setValue(entity, readMap(field.getValue(entity), field.getType().further(), reader));
						} else if (ODBSTypes.isAny(field.getType().value())) {
							// TODO 需要优化,目前仅支持基础值
							// FIXME 如果泛型未明确会出现错误
							if (reader.readValue()) {
								field.setValue(entity, reader.getString());
							} else {
								field.setValue(entity, null);
							}
						} else {
							throw new IllegalStateException("意外的字段类型 " + field.getType());
						}
					} else {
						reader.readIgnore();
					}
				}
			} else {
				// {} 空对象情况不存在任何键
				break;
			}
		}
		// 结束情形
		// {...}
		// {{},...}
		// {...,{}}
		// [{},{}]
		if (!root) {
			reader.readSkip();
		}
		return entity;
	}

	private final Map<?, ?> readMap(Map<Object, Object> map, ODBSType type, JSONCodec reader) throws IOException, ParseException {
		// {"key1":value1,"key2":value2}
		if (reader.readSkip() == JSONCodec.OBJECT_BEGIN) {
			if (map == null) {
				map = new HashMap<>();
			}
			if (ODBSTypes.isBase(type.key())) {
				// 键为基础类型,值为Base/Enum/Entity/Array/Set/List/Map
				if (ODBSTypes.isBase(type.value())) {
					while (reader.lastChar() != JSONCodec.OBJECT_END) {
						if (reader.readKey()) {
							map.put(base(type.key(), reader), readBase(type, reader));
						}
					}
				} else if (ODBSTypes.isEnum(type.value())) {
					while (reader.lastChar() != JSONCodec.OBJECT_END) {
						if (reader.readKey()) {
							map.put(base(type.key(), reader), readEnum(type, reader));
						}
					}
				} else if (ODBSTypes.isEntity(type.value())) {
					while (reader.lastChar() != JSONCodec.OBJECT_END) {
						if (reader.readKey()) {
							map.put(base(type.key(), reader), readEntity(type, reader, false));
						}
					}
				} else if (ODBSTypes.isArray(type.value())) {
					while (reader.lastChar() != JSONCodec.OBJECT_END) {
						if (reader.readKey()) {
							map.put(base(type.key(), reader), readArray(type.further(), reader));
						}
					}
				} else if (ODBSTypes.isList(type.value())) {
					while (reader.lastChar() != JSONCodec.OBJECT_END) {
						if (reader.readKey()) {
							map.put(base(type.key(), reader), readList(null, type.further(), reader));
						}
					}
				} else if (ODBSTypes.isSet(type.value())) {
					while (reader.lastChar() != JSONCodec.OBJECT_END) {
						if (reader.readKey()) {
							map.put(base(type.key(), reader), readSet(null, type.further(), reader));
						}
					}
				} else if (ODBSTypes.isMap(type.value())) {
					while (reader.lastChar() != JSONCodec.OBJECT_END) {
						if (reader.readKey()) {
							map.put(base(type.key(), reader), readMap(null, type.further(), reader));
						}
					}
				} else {
					throw new IllegalStateException("意外的Map值类型 " + type);
				}
			} else if (ODBSTypes.isEntity(type.value())) {
				// JSON 格式无法支持实体作为Map键
				throw new IllegalStateException("不支持Entity作为Map键类型:" + type);
			} else if (ODBSTypes.isArray(type.value())) {
				// JSON 格式无法支持数组作为Map键
				throw new IllegalStateException("不支持Array作为Map键类型:" + type);
				// JSON 格式无法支持集合作为Map键
			} else if (ODBSTypes.isList(type.value())) {
				throw new IllegalStateException("不支持List作为Map键类型:" + type);
				// JSON 格式无法支持集合作为Map键
			} else if (ODBSTypes.isSet(type.value())) {
				throw new IllegalStateException("不支持Set作为Map键类型:" + type);
			} else if (ODBSTypes.isMap(type.value())) {
				// JSON 格式无法支持Map作为Map键
				throw new IllegalStateException("不支持Map作为Map键类型:" + type);
			} else {
				throw new IllegalStateException("意外的Map键类型:" + type);
			}
			reader.readSkip();
		} else {
			if (reader.lastIsStructure()) {
				// 集合只能是数组结构[]，其它结构均为错误
				throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
			} else {
				// 集合除数组以外只有null可用
				if (reader.readValue()) {
					if (reader.isNull()) {
						map = null;
					} else {
						throw new ParseException(type + "意外值:" + reader.getString(), reader.getIndex());
					}
				} else {
					throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
				}
			}
		}
		return map;
	}

	private final Set<?> readSet(Set<Object> set, ODBSType type, JSONCodec reader) throws IOException, ParseException {
		// []
		if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
			if (set == null) {
				set = new HashSet<>();
			}
			if (ODBSTypes.isBase(type.value())) {
				readBases(set, type, reader);
				// while (reader.lastChar() != JSONCodec.ARRAY_END) {
				// if (reader.readValue()) {
				// set.add(base(type.value(), reader));
				// }
				// }
			} else if (ODBSTypes.isEnum(type.value())) {
				readEnums(set, type, reader);
				// while (reader.lastChar() != JSONCodec.ARRAY_END) {
				// set.add(readEnum(type, reader));
				// }
			} else if (ODBSTypes.isEntity(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readSkip() == JSONCodec.OBJECT_BEGIN) {
						set.add(readEntity(type, reader, true));
					}
				}
			} else if (ODBSTypes.isArray(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					set.add(readArray(type.further(), reader));
				}
			} else if (ODBSTypes.isList(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					set.add(readList(null, type.further(), reader));
				}
			} else if (ODBSTypes.isSet(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					set.add(readSet(null, type.further(), reader));
				}
			} else if (ODBSTypes.isMap(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					set.add(readMap(null, type.further(), reader));
				}
			} else {
				throw new IllegalStateException("意外的Set值类型:" + type);
			}
			reader.readSkip();
		} else {
			if (reader.lastIsStructure()) {
				// 集合只能是数组结构[]，其它结构均为错误
				throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
			} else {
				// 集合除数组以外只有null可用
				if (reader.readValue()) {
					if (reader.isNull()) {
						set = null;
					} else {
						throw new ParseException(type + "意外值:" + reader.getString(), reader.getIndex());
					}
				} else {
					throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
				}
			}
		}
		return set;
	}

	private final List<?> readList(List<Object> list, ODBSType type, JSONCodec reader) throws ParseException, IOException {
		// []
		if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
			if (list == null) {
				list = new ArrayList<>();
			}
			if (ODBSTypes.isBase(type.value())) {
				readBases(list, type, reader);
				// while (reader.lastChar() != JSONCodec.ARRAY_END) {
				// if (reader.readValue()) {
				// list.add(base(type.value(), reader));
				// }
				// }
			} else if (ODBSTypes.isEnum(type.value())) {
				readEnums(list, type, reader);
				// while (reader.lastChar() != JSONCodec.ARRAY_END) {
				// list.add(readEnum(type, reader));
				// }
			} else if (ODBSTypes.isEntity(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readSkip() == JSONCodec.OBJECT_BEGIN) {
						list.add(readEntity(type, reader, true));
					}
				}
			} else if (ODBSTypes.isArray(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					list.add(readArray(type.further(), reader));
				}
			} else if (ODBSTypes.isList(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					list.add(readList(null, type.further(), reader));
				}
			} else if (ODBSTypes.isSet(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					list.add(readSet(null, type.further(), reader));
				}
			} else if (ODBSTypes.isMap(type.value())) {
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					list.add(readMap(null, type.further(), reader));
				}
			} else {
				throw new IllegalStateException("意外的List值类型 " + type);
			}
			reader.readSkip();
		} else {
			if (reader.lastIsStructure()) {
				// 集合只能是数组结构[]，其它结构均为错误
				throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
			} else {
				// 集合除数组以外只有null可用
				if (reader.readValue()) {
					if (reader.isNull()) {
						list = null;
					} else {
						throw new ParseException(type + "意外值:" + reader.getString(), reader.getIndex());
					}
				} else {
					throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
				}
			}
		}
		return list;
	}

	/**
	 * 读取数组
	 */
	private final Object readArray(ODBSType type, JSONCodec reader) throws IOException, ParseException {
		// []
		if (ODBSTypes.isValue(type.value())) {
			// [] 值数组
			return readValues(type, reader);
		} else if (ODBSTypes.isBase(type.value())) {
			// [] 基础类型数组
			return readBases(type, reader);
		} else if (ODBSTypes.isEnum(type.value())) {
			// [] 枚举数组
			return readEnums(type, reader);
		} else if (ODBSTypes.isEntity(type.value())) {
			// [{}] 对象数组
			if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
				final List<Object> values = new ArrayList<>();
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readSkip() == JSONCodec.OBJECT_BEGIN) {
						values.add(readEntity(type, reader, true));
					}
				}
				reader.readSkip();
				// 返回数组
				final ODBSDescription description = odbs.findDesc(type.value());
				return values.toArray(description.newArray(values.size()));
			} else {
				// GO TO ELSE
			}
		} else if (ODBSTypes.isArray(type.value())) {
			// [[]] 多维数组
			if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
				final List<Object> values = new ArrayList<>();
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					values.add(readArray(type.further(), reader));
				}
				reader.readSkip();
				// 返回数组
				final ODBSDescription description = odbs.findDesc(type.value());
				return values.toArray(description.newArray(values.size()));
			} else {
				// GO TO ELSE
			}
		} else if (ODBSTypes.isList(type.value())) {
			// [[]] 集合数组
			if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
				final List<Object> values = new ArrayList<>();
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					values.add(readList(null, type.further(), reader));
				}
				reader.readSkip();
				// 返回数组
				final ODBSDescription description = odbs.findDesc(type.value());
				return values.toArray(description.newArray(values.size()));
			} else {
				// GO TO ELSE
			}
		} else if (ODBSTypes.isSet(type.value())) {
			// [[]] 集合数组
			if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
				final List<Object> values = new ArrayList<>();
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					values.add(readSet(null, type.further(), reader));
				}
				reader.readSkip();
				// 返回数组
				final ODBSDescription description = odbs.findDesc(type.value());
				return values.toArray(description.newArray(values.size()));
			} else {
				// GO TO ELSE
			}
		} else if (ODBSTypes.isMap(type.value())) {
			// [{}] 字典数组
			if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
				final List<Object> values = new ArrayList<>();
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					values.add(readMap(null, type.further(), reader));
				}
				reader.readSkip();
				// 返回数组
				final ODBSDescription description = odbs.findDesc(type.value());
				return values.toArray(description.newArray(values.size()));
			} else {
				// GO TO ELSE
			}
		} else {
			throw new IllegalStateException("意外的数组值类型 " + type);
		}

		// ELSE
		if (reader.lastIsStructure()) {
			// 集合只能是数组结构[]，其它结构均为错误
			throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
		} else {
			// 集合除数组以外只有null可用
			if (reader.readValue()) {
				if (reader.isNull()) {
					return null;
				} else {
					throw new ParseException(type + "意外值:" + reader.getString(), reader.getIndex());
				}
			} else {
				throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
			}
		}
	}

	private final void readEnums(Collection<? super Object> collection, ODBSType type, JSONCodec reader) throws IOException, ParseException {
		// 读取枚举集合
		// 此方法将减少不必要的类型判断
		final ODBSEnumeration enumeration = odbs.findEnum(type.value());
		while (reader.lastChar() != JSONCodec.ARRAY_END) {
			if (reader.readSkip() == JSONCodec.OBJECT_BEGIN) {
				while (reader.lastChar() != JSONCodec.OBJECT_END) {
					if (reader.readKey()) {
						if ("value".contentEquals(reader.getCharSequence())) {
							if (reader.readValue()) {
								if (reader.isNull()) {
									collection.add(null);
								} else {
									collection.add(enumeration.getConstant(Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
								}
							}
						} else {
							reader.readValue();
						}
					}
				}
				reader.readSkip();
			} else if (reader.lastChar() == JSONCodec.ARRAY_END) {
				// 空集合[]
				break;
			} else if (reader.readValue()) {
				if (reader.isNull()) {
					collection.add(null);
				} else {
					collection.add(enumeration.getConstant(Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
				}
			} else {
				throw new ParseException("期望值:" + type, reader.getIndex());
			}
		}
	}

	/**
	 * 读取枚举数组
	 */
	private final Object[] readEnums(ODBSType type, JSONCodec reader) throws IOException, ParseException {
		// "enum":[{"value":1,"name":"USER","text":"用户"},{"value":2,"name":"USER","text":"用户"},null]
		// "enum":[{"value":1,"name":"USER","text":"用户"},2,null]
		// "enum":[1,2,null]
		// "enum":[]
		// "enum":null

		final ODBSEnumeration enumeration = odbs.findEnum(type.value());
		if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
			final List<Object> values = new ArrayList<>();
			while (reader.lastChar() != JSONCodec.ARRAY_END) {
				if (reader.readSkip() == JSONCodec.OBJECT_BEGIN) {
					while (reader.lastChar() != JSONCodec.OBJECT_END) {
						if (reader.readKey()) {
							if ("value".contentEquals(reader.getCharSequence())) {
								if (reader.readValue()) {
									if (reader.isNull()) {
										values.add(null);
									} else {
										values.add(enumeration.getConstant(Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
									}
								}
							} else {
								reader.readValue();
							}
						}
					}
					reader.readSkip();
				} else if (reader.lastChar() == JSONCodec.ARRAY_END) {
					// 空集合[]
					break;
				} else if (reader.readValue()) {
					if (reader.isNull()) {
						values.add(null);
					} else {
						values.add(enumeration.getConstant(Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
					}
				} else {
					throw new ParseException("期望值:" + type, reader.getIndex());
				}
			}
			reader.readSkip();
			// 返回数组
			return values.toArray(enumeration.newArray(values.size()));
		} else {
			if (reader.lastIsStructure()) {
				// 集合只能是数组结构[]，其它结构均为错误
				throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
			} else {
				// 集合除数组以外只有null可用
				if (reader.readValue()) {
					if (reader.isNull()) {
						return null;
					} else {
						throw new ParseException(type + "意外值:" + reader.getString(), reader.getIndex());
					}
				} else {
					throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
				}
			}
		}
	}

	private final Object readEnum(ODBSType type, JSONCodec reader) throws IOException, ParseException {
		// "enum":{"value":1,"name":"USER","text":"用户"},
		// "enum":{"value":null,"name":null,"text":null},
		// "enum":1,
		// "enum":null,

		final ODBSEnumeration enumeration = odbs.findEnum(type.value());
		if (reader.readSkip() == JSONCodec.OBJECT_BEGIN) {
			Object value = null;
			while (reader.lastChar() != JSONCodec.OBJECT_END) {
				if (reader.readKey()) {
					if ("value".contentEquals(reader.getCharSequence())) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								value = null;
							} else {
								value = enumeration.getConstant(Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10));
							}
						}
					} else {
						// 由于枚举反序列化只需要值，其余字段忽略
						reader.readValue();
					}
				}
			}
			reader.readSkip();
			return value;
		} else if (reader.readValue()) {
			if (reader.isNull()) {
				return null;
			}
			return enumeration.getConstant(Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10));
		} else {
			throw new ParseException("期望值:" + type, reader.getIndex());
		}
	}

	private final void readBases(Collection<? super Object> collection, ODBSType type, JSONCodec reader) throws ParseException, IOException {
		// 读取基本类型集合
		// 此方法将减少不必要的类型判断
		switch (type.value()) {
			case ODBSTypes.BOOLEAN:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add("true".contentEquals(reader.getCharSequence()) ? Boolean.TRUE : Boolean.FALSE);
						}
					}
				}
				break;
			case ODBSTypes.BYTE:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(Byte.valueOf((byte) Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
						}
					}
				}
				break;
			case ODBSTypes.CHARACTER:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(reader.hasString() ? reader.getCharSequence().charAt(0) : ODBSTypes.DEFAULT_CHAR);
						}
					}
				}
				break;
			case ODBSTypes.SHORT:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(Short.valueOf((short) Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
						}
					}
				}
				break;
			case ODBSTypes.INTEGER:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(Integer.valueOf(Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
						}
					}
				}
				break;
			case ODBSTypes.LONG:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(Long.valueOf(Long.parseLong(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
						}
					}
				}
				break;
			case ODBSTypes.FLOAT:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(Float.valueOf(reader.getString()));
						}
					}
				}
				break;
			case ODBSTypes.DOUBLE:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(Double.valueOf(reader.getString()));
						}
					}
				}
				break;
			case ODBSTypes.BIG_DECIMAL:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(new BigDecimal(reader.getString()));
						}
					}
				}
				break;
			case ODBSTypes.DATE:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(reader.DATE_FORMAT.parse(reader.getString()));
						}
					}
				}
				break;
			case ODBSTypes.LOCAL_TIME:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(LocalTime.parse(reader.getCharSequence(), TIME_FORMATTER));
						}
					}
				}
				break;
			case ODBSTypes.LOCAL_DATE:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(LocalDate.parse(reader.getCharSequence(), DATE_FORMATTER));
						}
					}
				}
				break;
			case ODBSTypes.LOCAL_DATE_TIME:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(LocalDateTime.parse(reader.getCharSequence(), DATE_TIME_FORMATTER));
						}
					}
				}
				break;
			case ODBSTypes.STRING:
				while (reader.lastChar() != JSONCodec.ARRAY_END) {
					if (reader.readValue()) {
						if (reader.isNull()) {
							collection.add(null);
						} else {
							collection.add(reader.getString());
						}
					}
				}
				break;
			default:
				throw new IllegalStateException("意外的数组基础类型 " + type);
		}
	}

	/**
	 * 读取基础类型数组
	 */
	private final Object[] readBases(ODBSType type, JSONCodec reader) throws ParseException, IOException {
		// 读取多个基础类型值数组
		// 基础类型数组值类型相同，因此先执行类型判断后执行循环读值可减少重复类型判断
		if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
			switch (type.value()) {
				case ODBSTypes.BOOLEAN:
					final List<Boolean> booleans = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								booleans.add(null);
							} else {
								booleans.add("true".contentEquals(reader.getCharSequence()) ? Boolean.TRUE : Boolean.FALSE);
							}
						}
					}
					reader.readSkip();
					return booleans.toArray(new Boolean[booleans.size()]);
				case ODBSTypes.BYTE:
					final List<Byte> bytes = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								bytes.add(null);
							} else {
								bytes.add(Byte.valueOf((byte) Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
							}
						}
					}
					reader.readSkip();
					return bytes.toArray(new Byte[bytes.size()]);
				case ODBSTypes.CHARACTER:
					final List<Character> chars = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								chars.add(null);
							} else {
								chars.add(reader.hasString() ? reader.getCharSequence().charAt(0) : ODBSTypes.DEFAULT_CHAR);
							}
						}
					}
					reader.readSkip();
					return chars.toArray(new Character[chars.size()]);
				case ODBSTypes.SHORT:
					final List<Short> shorts = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								shorts.add(null);
							} else {
								shorts.add(Short.valueOf((short) Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
							}
						}
					}
					reader.readSkip();
					return shorts.toArray(new Short[shorts.size()]);
				case ODBSTypes.INTEGER:
					final List<Integer> integers = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								integers.add(null);
							} else {
								integers.add(Integer.valueOf(Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
							}
						}
					}
					reader.readSkip();
					return integers.toArray(new Integer[integers.size()]);
				case ODBSTypes.LONG:
					final List<Long> longs = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								longs.add(null);
							} else {
								longs.add(Long.valueOf(Long.parseLong(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10)));
							}
						}
					}
					reader.readSkip();
					return longs.toArray(new Long[longs.size()]);
				case ODBSTypes.FLOAT:
					final List<Float> floats = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								floats.add(null);
							} else {
								floats.add(Float.valueOf(reader.getString()));
							}
						}
					}
					reader.readSkip();
					return floats.toArray(new Float[floats.size()]);
				case ODBSTypes.DOUBLE:
					final List<Double> doubles = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								doubles.add(null);
							} else {
								doubles.add(Double.valueOf(reader.getString()));
							}
						}
					}
					reader.readSkip();
					return doubles.toArray(new Double[doubles.size()]);
				case ODBSTypes.BIG_DECIMAL:
					final List<BigDecimal> decimals = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								decimals.add(null);
							} else {
								decimals.add(new BigDecimal(reader.getString()));
							}
						}
					}
					reader.readSkip();
					return decimals.toArray(new BigDecimal[decimals.size()]);
				case ODBSTypes.DATE:
					final List<Date> dates = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								dates.add(null);
							} else {
								dates.add(reader.DATE_FORMAT.parse(reader.getString()));
							}
						}
					}
					reader.readSkip();
					return dates.toArray(new Date[dates.size()]);
				case ODBSTypes.LOCAL_TIME:
					final List<LocalTime> localtimes = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								localtimes.add(null);
							} else {
								localtimes.add(LocalTime.parse(reader.getCharSequence(), TIME_FORMATTER));
							}
						}
					}
					reader.readSkip();
					return localtimes.toArray(new LocalTime[localtimes.size()]);
				case ODBSTypes.LOCAL_DATE:
					final List<LocalDate> localdates = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								localdates.add(null);
							} else {
								localdates.add(LocalDate.parse(reader.getCharSequence(), DATE_FORMATTER));
							}
						}
					}
					reader.readSkip();
					return localdates.toArray(new LocalDate[localdates.size()]);
				case ODBSTypes.LOCAL_DATE_TIME:
					final List<LocalDateTime> localdatetimes = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								localdatetimes.add(null);
							} else {
								localdatetimes.add(LocalDateTime.parse(reader.getCharSequence(), DATE_TIME_FORMATTER));
							}
						}
					}
					reader.readSkip();
					return localdatetimes.toArray(new LocalDateTime[localdatetimes.size()]);
				case ODBSTypes.STRING:
					final List<String> strings = new ArrayList<>();
					while (reader.lastChar() != JSONCodec.ARRAY_END) {
						if (reader.readValue()) {
							if (reader.isNull()) {
								strings.add(null);
							} else {
								strings.add(reader.getString());
							}
						}
					}
					reader.readSkip();
					return strings.toArray(new String[strings.size()]);
				default:
					throw new IllegalStateException("意外的数组基础类型 " + type);
			}
		} else {
			if (reader.lastIsStructure()) {
				// 集合只能是数组结构[]，其它结构均为错误
				throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
			} else {
				// 集合除数组以外只有null可用
				if (reader.readValue()) {
					if (reader.isNull()) {
						return null;
					} else {
						throw new ParseException(type + "意外值:" + reader.getString(), reader.getIndex());
					}
				} else {
					throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
				}
			}
		}
	}

	/**
	 * 读取基本类型，调用此方法之前必须先读取键，支持null值
	 */
	private final Object readBase(ODBSType type, JSONCodec reader) throws IOException, ParseException {
		if (reader.readValue()) {
			if (reader.hasString()) {
				if (reader.isNull()) {
					return null;
				} else {
					switch (type.value()) {
						case ODBSTypes.BOOLEAN:
							return "true".contentEquals(reader.getCharSequence()) ? Boolean.TRUE : Boolean.FALSE;
						case ODBSTypes.BYTE:
							return Byte.valueOf((byte) Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10));
						case ODBSTypes.CHARACTER:
							// 20230713 Character=0 特殊控制字符输出时为空字符串
							return reader.hasString() ? reader.getCharSequence().charAt(0) : ODBSTypes.DEFAULT_CHAR;
						case ODBSTypes.SHORT:
							return Short.valueOf((short) Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10));
						case ODBSTypes.INTEGER:
							return Integer.valueOf(Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10));
						case ODBSTypes.LONG:
							return Long.valueOf(Long.parseLong(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10));
						case ODBSTypes.FLOAT:
							return Float.valueOf(reader.getString());
						case ODBSTypes.DOUBLE:
							return Double.valueOf(reader.getString());
						case ODBSTypes.BIG_DECIMAL:
							return new BigDecimal(reader.getString());
						case ODBSTypes.DATE:
							return reader.DATE_FORMAT.parse(reader.getString());
						case ODBSTypes.LOCAL_TIME:
							return LocalTime.parse(reader.getCharSequence(), TIME_FORMATTER);
						case ODBSTypes.LOCAL_DATE:
							return LocalDate.parse(reader.getCharSequence(), DATE_FORMATTER);
						case ODBSTypes.LOCAL_DATE_TIME:
							return LocalDateTime.parse(reader.getCharSequence(), DATE_TIME_FORMATTER);
						case ODBSTypes.STRING:
							return reader.getString();
						default:
							throw new IllegalStateException("意外的基础类型:" + type);
					}
				}
			} else {
				// ""
				// 20250116 处理空字符串
				if (type.value() == ODBSTypes.STRING) {
					return reader.getString();
				}
				if (type.value() == ODBSTypes.CHARACTER) {
					return ODBSTypes.DEFAULT_CHAR;
				}
				return null;
			}
		} else {
			throw new ParseException("期望值:" + type, reader.getIndex());
		}
	}

	/**
	 * 读取值类型数组
	 */
	private final Object readValues(ODBSType type, JSONCodec reader) throws IOException, ParseException {
		// 数组读取通过此方法避免过多的装箱拆箱操作
		// 先读出所有字符值然后才能确定数组值数量
		if (reader.readSkip() == JSONCodec.ARRAY_BEGIN) {
			final List<String> values = new ArrayList<>();
			while (reader.lastChar() != JSONCodec.ARRAY_END) {
				if (reader.readValue()) {
					values.add(reader.isNull() ? null : reader.getString());
				}
			}
			switch (type.value()) {
				case ODBSTypes._boolean:
					final boolean[] booleans = new boolean[values.size()];
					for (int index = 0; index < booleans.length; index++) {
						booleans[index] = values.get(index) == null ? false : Boolean.parseBoolean(values.get(index));
					}
					return booleans;
				case ODBSTypes._byte:
					final byte[] bytes = new byte[values.size()];
					for (int index = 0; index < bytes.length; index++) {
						bytes[index] = values.get(index) == null ? 0 : Byte.parseByte(values.get(index));
					}
					return bytes;
				case ODBSTypes._char:
					final char[] chars = new char[values.size()];
					for (int index = 0; index < chars.length; index++) {
						chars[index] = values.get(index) == null ? 0 : values.get(index).charAt(0);
					}
					return chars;
				case ODBSTypes._short:
					final short[] shorts = new short[values.size()];
					for (int index = 0; index < shorts.length; index++) {
						shorts[index] = values.get(index) == null ? 0 : Short.parseShort(values.get(index));
					}
					return shorts;
				case ODBSTypes._int:
					final int[] ints = new int[values.size()];
					for (int index = 0; index < ints.length; index++) {
						ints[index] = values.get(index) == null ? 0 : Integer.parseInt(values.get(index));
					}
					return ints;
				case ODBSTypes._long:
					final long[] longs = new long[values.size()];
					for (int index = 0; index < longs.length; index++) {
						longs[index] = values.get(index) == null ? 0 : Long.parseLong(values.get(index));
					}
					return longs;
				case ODBSTypes._float:
					final float[] floats = new float[values.size()];
					for (int index = 0; index < floats.length; index++) {
						floats[index] = values.get(index) == null ? 0 : Float.parseFloat(values.get(index));
					}
					return floats;
				case ODBSTypes._double:
					final double[] doubles = new double[values.size()];
					for (int index = 0; index < doubles.length; index++) {
						doubles[index] = values.get(index) == null ? 0 : Double.parseDouble(values.get(index));
					}
					return doubles;
				default:
					throw new IllegalStateException("意外的数组值类型:" + type);
			}
		} else {
			if (reader.lastIsStructure()) {
				// 集合只能是数组结构[]，其它结构均为错误
				throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
			} else {
				// 集合除数组以外只有null可用
				if (reader.readValue()) {
					if (reader.isNull()) {
						return null;
					} else {
						throw new ParseException(type + "意外值:" + reader.getString(), reader.getIndex());
					}
				} else {
					throw new ParseException(type + "意外字符:" + reader.lastChar(), reader.getIndex());
				}
			}
		}
	}

	/**
	 * 读取值类型，调用此方法之前必须先读取键，如果为null将返回对应值类型的默认值
	 */
	private final Object readValue(ODBSType type, JSONCodec reader) throws IOException, ParseException {
		if (reader.readValue()) {
			if (reader.hasString()) {
				switch (type.value()) {
					case ODBSTypes._boolean:
						return "true".contentEquals(reader.getCharSequence());
					case ODBSTypes._byte:
						return (byte) Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10);
					case ODBSTypes._char:
						// 20230713 char=0 特殊控制字符输出时为空字符串
						return reader.hasString() ? reader.getCharSequence().charAt(0) : ODBSTypes.DEFAULT_CHAR;
					case ODBSTypes._short:
						return (short) Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10);
					case ODBSTypes._int:
						return Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10);
					case ODBSTypes._long:
						return Long.parseLong(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10);
					case ODBSTypes._float:
						return Float.parseFloat(reader.getString());
					case ODBSTypes._double:
						return Double.parseDouble(reader.getString());
					default:
						throw new IllegalStateException("意外的值类型:" + type);
				}
			} else {
				// null / ""
				// 20250116 忽略空字符串
				switch (type.value()) {
					case ODBSTypes._boolean:
						return Boolean.FALSE;
					case ODBSTypes._byte:
						return ODBSTypes.DEFAULT_BYTE;
					case ODBSTypes._char:
						return ODBSTypes.DEFAULT_CHAR;
					case ODBSTypes._short:
						return ODBSTypes.DEFAULT_SHORT;
					case ODBSTypes._int:
						return ODBSTypes.DEFAULT_INTEGER;
					case ODBSTypes._long:
						return ODBSTypes.DEFAULT_LONG;
					case ODBSTypes._float:
						return ODBSTypes.DEFAULT_FLOAT;
					case ODBSTypes._double:
						return ODBSTypes.DEFAULT_DOUBLE;
					default:
						throw new IllegalStateException("意外的值类型:" + type);
				}
			}
		} else {
			throw new ParseException("期望值:" + type, reader.getIndex());
		}
	}

	/**
	 * 获取基础值，调用此方法之前必须先读取键和值
	 */
	private final Object base(int type, JSONCodec reader) throws ParseException {
		if (reader.isNull()) {
			return null;
		}
		switch (type) {
			case ODBSTypes.BOOLEAN:
				return "true".contentEquals(reader.getCharSequence()) ? Boolean.TRUE : Boolean.FALSE;
			case ODBSTypes.BYTE:
				int b = Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10);
				return Byte.valueOf((byte) b);
			case ODBSTypes.CHARACTER:
				return reader.getCharSequence().charAt(0);
			case ODBSTypes.SHORT:
				int s = Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10);
				return Short.valueOf((short) s);
			case ODBSTypes.INTEGER:
				return Integer.parseInt(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10);
			case ODBSTypes.LONG:
				return Long.parseLong(reader.getCharSequence(), 0, reader.getCharSequence().length(), 10);
			case ODBSTypes.FLOAT:
				return Float.valueOf(reader.getString());
			case ODBSTypes.DOUBLE:
				return Double.valueOf(reader.getString());
			case ODBSTypes.BIG_DECIMAL:
				return new BigDecimal(reader.getString());
			case ODBSTypes.DATE:
				return reader.DATE_FORMAT.parse(reader.getString());
			case ODBSTypes.LOCAL_TIME:
				return LocalTime.parse(reader.getCharSequence(), TIME_FORMATTER);
			case ODBSTypes.LOCAL_DATE:
				return LocalDate.parse(reader.getCharSequence(), DATE_FORMATTER);
			case ODBSTypes.LOCAL_DATE_TIME:
				return LocalDateTime.parse(reader.getCharSequence(), DATE_TIME_FORMATTER);
			case ODBSTypes.STRING:
				return reader.getString();
			default:
				throw new IllegalStateException("意外的基础类型:" + type);
		}
	}

	////////////////////////////////////////////////////////////////////////////////

	/**
	 * 获取是否忽略空值输出
	 */
	public boolean isIgnoreNull() {
		return IGNORE_NULL;
	}

	/**
	 * 设置是否忽略控制输出，默认忽略(true)
	 */
	public void setIgnoreNull(boolean value) {
		IGNORE_NULL = value;
	}

	/**
	 * 获取是否忽略未定义字段
	 */
	public boolean isIgnoreUndefinedField() {
		return IGNORE_UNDEFINED_FIELD;
	}

	/**
	 * 设置是否忽略未定义字段，默认忽略(true)
	 */
	public void setIgnoreUndefinedField(boolean value) {
		IGNORE_UNDEFINED_FIELD = value;
	}

	/**
	 * 获取是否输出双引号包围键名
	 */
	public boolean isQuotesKey() {
		return QUOTES_KEY;
	}

	/**
	 * 设置是否输出双引号包围键名，默认将输出双引号(true)
	 */
	public void setQuotesKey(boolean value) {
		QUOTES_KEY = value;
	}

	/**
	 * 获取是否输出枚举对象
	 */
	public boolean isEnumObject() {
		return ENUM_OBJECT;
	}

	/**
	 * 设置是否输出枚举对象，默认将输出枚举对象(true)，如果为false枚举将仅输出为整数值
	 */
	public void setEnumObject(boolean value) {
		ENUM_OBJECT = value;
	}

	/**
	 * 获取键名格式
	 */
	public JSONName getKeyNameFormat() {
		return KEY_NAME_FORMAT;
	}

	/**
	 * 设置键名格式，默认为JSONName.DEFAULT，此设置同时影响对序列化和反序列化
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
	 * 设置时间格式化，默认为HH:mm:ss，仅对LocalTime类型有效
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
	 * 设置日期格式化，默认为uuuu-MM-dd，仅对LocalDate类型有效
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
	 * 设置日期时间格式化，默认为uuuu-MM-dd HH:mm:ss，仅对LocalDateTime类型有效
	 */
	public void setDateTimeFormatter(DateTimeFormatter value) {
		DATE_TIME_FORMATTER = value;
	}

	/**
	 * 获取日期时间格式化
	 */
	public DateFormat getDateFormat() {
		return DATE_FORMAT.get();
	}

	/**
	 * 设置日期时间格式化，默认为yyyy-MM-dd HH:mm:ss，仅对Date类型有效
	 */
	public void setDateFormat(SimpleDateFormat value) {
		DATE_FORMAT = new Supplier<DateFormat>() {
			@Override
			public DateFormat get() {
				return (DateFormat) value.clone();
			}
		};
	}

	/**
	 * 获取数值时间格式化
	 */
	public NumberFormat getNumberFormat() {
		return NUMBER_FORMAT.get();
	}

	/**
	 * 设置数值时间格式化，默认不输出分组符','不转化为科学计数法
	 */
	public void setNumberFormat(NumberFormat value) {
		NUMBER_FORMAT = new Supplier<NumberFormat>() {
			@Override
			public NumberFormat get() {
				return (NumberFormat) value.clone();
			}
		};
	}
}
