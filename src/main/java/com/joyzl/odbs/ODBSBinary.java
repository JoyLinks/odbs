/*-
 * www.joyzl.net
 * 重庆骄智科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.joyzl.codec.BigEndianInputStream;
import com.joyzl.codec.BigEndianOutputStream;
import com.joyzl.codec.DataInput;
import com.joyzl.codec.DataOutput;

/**
 * ODBS 二进制序列化/反序列化编码
 * 
 * @author ZhangXi
 * @date 2020年6月12日
 */
public final class ODBSBinary extends ODBSTypes {

	private final ODBS odbs;

	public ODBSBinary(ODBS o) {
		if (o == null) {
			throw new NullPointerException("ODBS");
		}
		odbs = o;
	}

	public final void writeEntity(Object entity, OutputStream output) throws IOException {
		writeEntity(entity, (DataOutput) new BigEndianOutputStream(output));
	}

	public final void writeEntity(Object entity, DataOutput writer) throws IOException {
		writeEntity(odbs.findDesc(entity.getClass()), entity, writer);
	}

	private final void writeEntity(int type, Object entity, DataOutput writer) throws IOException {
		ODBSDescription description = odbs.findDesc(type);
		if (description == null) {
			throw new RuntimeException("对象描述索引不存在" + type);
		}
		if (description.getSourceClass() == entity.getClass()) {
			// 实例与定义相同
		} else {
			// 集合或实体成员可能存在继承关系
			// private Attribute a = new AttributeBoolean();
			// 此时type索引指向 Attribute 实例，但实际上是 AttributeBoolean 实例
			// 因此需要通过实例Class获取描述类型
			description = odbs.findDesc(entity.getClass());
		}
		writeEntity(description, entity, writer);
	}

	private final void writeEntity(ODBSDescription description, Object entity, DataOutput writer) throws IOException {
		if (description == null) {
			throw new IllegalStateException("对象描述不存在:" + entity.getClass().getName());
		}

		// 分包计数

		// 对象类型
		writer.writeVarint(description.getIndex());
		// 字段序列
		Object value;
		ODBSField field;
		for (int index = 0; index < description.getFieldCount(); index++) {
			field = description.getField(index);
			// 二进制仅序列化成对字段
			if (field.isPaired()) {
				value = field.getValue(entity);
				if (field.isDefaultValue(value)) {
					// 字段为值类型 0 值无须传递
					// 字段为对象类型 null 值无须传递
					continue;
				} else {
					// 字段索引
					writer.writeVarint(index);
					// 字段值
					if (ODBSTypes.isValue(field.getType().value())) {
						writeValue(field.getType().value(), value, writer);
					} else if (ODBSTypes.isBase(field.getType().value())) {
						writeBase(field.getType().value(), value, writer);
					} else if (ODBSTypes.isEnum(field.getType().value())) {
						writeEnum(field.getType().value(), value, writer);
					} else if (ODBSTypes.isEntity(field.getType().value())) {
						writeEntity(field.getType().value(), value, writer);
					} else if (ODBSTypes.isArray(field.getType().value())) {
						writeArray(field.getType().further(), value, writer);
					} else if (ODBSTypes.isList(field.getType().value())) {
						writeList(field.getType().further(), (List<?>) value, writer);
					} else if (ODBSTypes.isSet(field.getType().value())) {
						writeSet(field.getType().further(), (Set<?>) value, writer);
					} else if (ODBSTypes.isMap(field.getType().value())) {
						writeMap(field.getType().further(), (Map<?, ?>) value, writer);
					} else if (ODBSTypes.isAny(field.getType().value())) {
						writeObject(value, writer);
					} else {
						throw new IllegalStateException("不支持的数据类型" + entity.getClass() + " " + field.getName());
					}
				}
			}
		}
		// 输出字段总数标记结束
		writer.writeVarint(description.getFieldCount());
	}

	private final void writeObject(Object value, DataOutput writer) throws IOException {
		// 定义为Object类型的对象，目前只支持基本类型和实体对象
		int type = ODBSTypes.getType(value);
		if (ODBSTypes.isBase(type)) {
			// 对象类型
			writer.writeVarint(type);
			// 对象值
			writeBase(type, value, writer);
		} else {
			ODBSDescription description = odbs.findDesc(value.getClass());
			if (description == null) {
				throw new IllegalStateException("不支持的对象类型");
			} else {
				// writeEntity方法会自己写入对象类型
				writeEntity(description, value, writer);
			}
		}
	}

	private final void writeArray(ODBSType type, Object values, DataOutput writer) throws IOException {
		// writer.writeVarint(type.value());
		if (ODBSTypes.isValue(type.value())) {
			writeValues(type.value(), values, writer);
		} else if (ODBSTypes.isBase(type.value())) {
			writeBases(type.value(), values, writer);
		} else if (ODBSTypes.isEnum(type.value())) {
			writeEnums(type.value(), values, writer);
		} else if (ODBSTypes.isEntity(type.value())) {
			final int length = Array.getLength(values);
			writer.writeVarint(length);
			for (int index = 0; index < length; index++) {
				writeEntity(type.value(), Array.get(values, index), writer);
			}
		} else if (ODBSTypes.isArray(type.value())) {
			final int length = Array.getLength(values);
			writer.writeVarint(length);
			for (int index = 0; index < length; index++) {
				writeArray(type.further(), Array.get(values, index), writer);
			}
		} else if (ODBSTypes.isList(type.value())) {
			final int length = Array.getLength(values);
			writer.writeVarint(length);
			for (int index = 0; index < length; index++) {
				writeList(type.further(), (List<?>) Array.get(values, index), writer);
			}
		} else if (ODBSTypes.isSet(type.value())) {
			final int length = Array.getLength(values);
			writer.writeVarint(length);
			for (int index = 0; index < length; index++) {
				writeSet(type.further(), (Set<?>) Array.get(values, index), writer);
			}
		} else if (ODBSTypes.isMap(type.value())) {
			final int length = Array.getLength(values);
			writer.writeVarint(length);
			for (int index = 0; index < length; index++) {
				writeMap(type.further(), (Map<?, ?>) Array.get(values, index), writer);
			}
		} else {
			throw new IllegalStateException("意外的数组值类型:" + type);
		}
	}

	private final void writeList(ODBSType type, List<?> values, DataOutput writer) throws IOException {
		final int length = values.size();
		writer.writeVarint(length);
		if (ODBSTypes.isBase(type.value())) {
			for (int index = 0; index < length; index++) {
				writeBase(type.value(), values.get(index), writer);
			}
		} else if (ODBSTypes.isEnum(type.value())) {
			for (int index = 0; index < length; index++) {
				writeEnum(type.value(), values.get(index), writer);
			}
		} else if (ODBSTypes.isEntity(type.value())) {
			for (int index = 0; index < length; index++) {
				writeEntity(type.value(), values.get(index), writer);
			}
		} else if (ODBSTypes.isArray(type.value())) {
			for (int index = 0; index < length; index++) {
				writeArray(type.further(), (Object[]) values.get(index), writer);
			}
		} else if (ODBSTypes.isList(type.value())) {
			for (int index = 0; index < length; index++) {
				writeList(type.further(), (List<?>) values.get(index), writer);
			}
		} else if (ODBSTypes.isSet(type.value())) {
			for (int index = 0; index < length; index++) {
				writeSet(type.further(), (Set<?>) values.get(index), writer);
			}
		} else if (ODBSTypes.isMap(type.value())) {
			for (int index = 0; index < length; index++) {
				writeMap(type.further(), (Map<?, ?>) values.get(index), writer);
			}
		} else {
			throw new IllegalStateException("意外的List值类型:" + type);
		}
	}

	private final void writeSet(ODBSType type, Set<?> values, DataOutput writer) throws IOException {
		writer.writeVarint(values.size());
		if (ODBSTypes.isBase(type.value())) {
			for (Object value : values) {
				writeBase(type.value(), value, writer);
			}
		} else if (ODBSTypes.isEnum(type.value())) {
			for (Object value : values) {
				writeEnum(type.value(), value, writer);
			}
		} else if (ODBSTypes.isEntity(type.value())) {
			for (Object value : values) {
				writeEntity(type.value(), value, writer);
			}
		} else if (ODBSTypes.isArray(type.value())) {
			for (Object value : values) {
				writeArray(type.further(), value, writer);
			}
		} else if (ODBSTypes.isList(type.value())) {
			for (Object value : values) {
				writeList(type.further(), (List<?>) value, writer);
			}
		} else if (ODBSTypes.isSet(type.value())) {
			for (Object value : values) {
				writeSet(type.further(), (Set<?>) value, writer);
			}
		} else if (ODBSTypes.isMap(type.value())) {
			for (Object value : values) {
				writeMap(type.further(), (Map<?, ?>) value, writer);
			}
		} else {
			throw new IllegalStateException("意外的Set值类型:" + type);
		}
	}

	private final void writeMap(ODBSType type, Map<?, ?> values, DataOutput writer) throws IOException {
		writer.writeVarint(values.size());
		if (ODBSTypes.isBase(type.key())) {
			if (ODBSTypes.isBase(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeBase(type.key(), value.getKey(), writer);
					writeBase(type.value(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isEnum(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeBase(type.key(), value.getKey(), writer);
					writeEnum(type.value(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isEntity(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeBase(type.key(), value.getKey(), writer);
					writeEntity(type.value(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isArray(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeBase(type.key(), value.getKey(), writer);
					writeArray(type.further(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isList(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeBase(type.key(), value.getKey(), writer);
					writeList(type.further(), (List<?>) value.getValue(), writer);
				}
			} else if (ODBSTypes.isSet(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeBase(type.key(), value.getKey(), writer);
					writeSet(type.further(), (Set<?>) value.getValue(), writer);
				}
			} else if (ODBSTypes.isMap(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeBase(type.key(), value.getKey(), writer);
					writeMap(type.further(), (Map<?, ?>) value.getValue(), writer);
				}
			} else {
				throw new IllegalStateException("意外的Map值类型 " + type);
			}
		} else if (ODBSTypes.isEnum(type.key())) {
			if (ODBSTypes.isBase(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEnum(type.key(), value.getKey(), writer);
					writeBase(type.value(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isEnum(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEnum(type.key(), value.getKey(), writer);
					writeEnum(type.value(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isEntity(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEnum(type.key(), value.getKey(), writer);
					writeEntity(type.value(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isArray(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEnum(type.key(), value.getKey(), writer);
					writeArray(type.further(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isList(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEnum(type.key(), value.getKey(), writer);
					writeList(type.further(), (List<?>) value.getValue(), writer);
				}
			} else if (ODBSTypes.isSet(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEnum(type.key(), value.getKey(), writer);
					writeSet(type.further(), (Set<?>) value.getValue(), writer);
				}
			} else if (ODBSTypes.isMap(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEnum(type.key(), value.getKey(), writer);
					writeMap(type.further(), (Map<?, ?>) value.getValue(), writer);
				}
			} else {
				throw new IllegalStateException("意外的Map值类型 " + type);
			}
		} else if (ODBSTypes.isEntity(type.key())) {
			if (ODBSTypes.isBase(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEntity(type.key(), value.getKey(), writer);
					writeBase(type.value(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isEnum(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEntity(type.key(), value.getKey(), writer);
					writeEnum(type.value(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isEntity(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEntity(type.key(), value.getKey(), writer);
					writeEntity(type.value(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isArray(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEntity(type.key(), value.getKey(), writer);
					writeArray(type.further(), value.getValue(), writer);
				}
			} else if (ODBSTypes.isList(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEntity(type.key(), value.getKey(), writer);
					writeList(type.further(), (List<?>) value.getValue(), writer);
				}
			} else if (ODBSTypes.isSet(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEntity(type.key(), value.getKey(), writer);
					writeSet(type.further(), (Set<?>) value.getValue(), writer);
				}
			} else if (ODBSTypes.isMap(type.value())) {
				for (Map.Entry<?, ?> value : values.entrySet()) {
					writeEntity(type.key(), value.getKey(), writer);
					writeMap(type.further(), (Map<?, ?>) value.getValue(), writer);
				}
			} else {
				throw new IllegalStateException("意外的Map值类型:" + type);
			}
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

	private final void writeEnum(int type, Object value, DataOutput writer) throws IOException {
		final ODBSEnumeration enumeration = odbs.findEnum(type);
		// writer.writeVarint(enumeration.getIndex());
		writer.writeVarint(enumeration.getValue(value));
	}

	private final void writeEnums(int type, Object values, DataOutput writer) throws IOException {
		final ODBSEnumeration enumeration = odbs.findEnum(type);
		final int length = Array.getLength(values);
		// 数组长度
		writer.writeVarint(length);
		// 数组值
		for (int index = 0; index < length; index++) {
			writer.writeVarint(enumeration.getValue(Array.get(values, index)));
		}
	}

	private final void writeBases(int type, Object values, DataOutput writer) throws IOException {
		final int length = Array.getLength(values);
		// 数组长度
		writer.writeVarint(length);
		// 数组值
		switch (type) {
			case ODBSTypes.BOOLEAN:
				for (int index = 0; index < length; index++) {
					writer.writeBoolean((Boolean) Array.get(values, index));
				}
				break;
			case ODBSTypes.BYTE:
				for (int index = 0; index < length; index++) {
					writer.writeByte((Byte) Array.get(values, index));
				}
				break;
			case ODBSTypes.CHARACTER:
				for (int index = 0; index < length; index++) {
					writer.writeChar((Character) Array.get(values, index));
				}
				break;
			case ODBSTypes.SHORT:
				for (int index = 0; index < length; index++) {
					writer.writeShort((Short) Array.get(values, index));
				}
				break;
			case ODBSTypes.INTEGER:
				for (int index = 0; index < length; index++) {
					writer.writeInt((Integer) Array.get(values, index));
				}
				break;
			case ODBSTypes.LONG:
				for (int index = 0; index < length; index++) {
					writer.writeLong((Long) Array.get(values, index));
				}
				break;
			case ODBSTypes.FLOAT:
				for (int index = 0; index < length; index++) {
					writer.writeFloat((Float) Array.get(values, index));
				}
				break;
			case ODBSTypes.DOUBLE:
				for (int index = 0; index < length; index++) {
					writer.writeDouble((Double) Array.get(values, index));
				}
				break;
			case ODBSTypes.BIG_DECIMAL:
				for (int index = 0; index < length; index++) {
					writer.writeDecimal((BigDecimal) Array.get(values, index));
				}
				break;
			case ODBSTypes.DATE:
				for (int index = 0; index < length; index++) {
					writer.writeDate((Date) Array.get(values, index));
				}
				break;
			case ODBSTypes.LOCAL_TIME:
				for (int index = 0; index < length; index++) {
					writer.writeLocalTime((LocalTime) Array.get(values, index));
				}
				break;
			case ODBSTypes.LOCAL_DATE:
				for (int index = 0; index < length; index++) {
					writer.writeLocalDate((LocalDate) Array.get(values, index));
				}
				break;
			case ODBSTypes.LOCAL_DATE_TIME:
				for (int index = 0; index < length; index++) {
					writer.writeLocalDateTime((LocalDateTime) Array.get(values, index));
				}
				break;
			case ODBSTypes.STRING:
				for (int index = 0; index < length; index++) {
					writer.writeString((String) Array.get(values, index));
				}
				break;
			default:
				throw new IllegalStateException("意外的数组基础类型:" + type);
		}
	}

	private final void writeBase(int type, Object value, DataOutput writer) throws IOException {
		switch (type) {
			case ODBSTypes.BOOLEAN:
				writer.writeBoolean((Boolean) value);
				break;
			case ODBSTypes.BYTE:
				writer.writeByte((Byte) value);
				break;
			case ODBSTypes.CHARACTER:
				writer.writeChar((Character) value);
				break;
			case ODBSTypes.SHORT:
				writer.writeShort((Short) value);
				break;
			case ODBSTypes.INTEGER:
				writer.writeInt((Integer) value);
				break;
			case ODBSTypes.LONG:
				writer.writeLong((Long) value);
				break;
			case ODBSTypes.FLOAT:
				writer.writeFloat((Float) value);
				break;
			case ODBSTypes.DOUBLE:
				writer.writeDouble((Double) value);
				break;
			case ODBSTypes.BIG_DECIMAL:
				writer.writeDecimal((BigDecimal) value);
				break;
			case ODBSTypes.DATE:
				writer.writeDate((Date) value);
				break;
			case ODBSTypes.LOCAL_TIME:
				writer.writeLocalTime((LocalTime) value);
				break;
			case ODBSTypes.LOCAL_DATE:
				writer.writeLocalDate((LocalDate) value);
				break;
			case ODBSTypes.LOCAL_DATE_TIME:
				writer.writeLocalDateTime((LocalDateTime) value);
				break;
			case ODBSTypes.STRING:
				writer.writeString((String) value);
				break;
			default:
				throw new IllegalStateException("意外的基础类型:" + type);
		}
	}

	private final void writeValues(int type, Object values, DataOutput writer) throws IOException {
		final int length = Array.getLength(values);
		// 数组长度
		writer.writeVarint(length);
		// 数组值
		switch (type) {
			case ODBSTypes._boolean:
				for (int index = 0; index < length; index++) {
					writer.writeBoolean(Array.getBoolean(values, index));
				}
				break;
			case ODBSTypes._byte:
				for (int index = 0; index < length; index++) {
					writer.writeByte(Array.getByte(values, index));
				}
				break;
			case ODBSTypes._char:
				for (int index = 0; index < length; index++) {
					writer.writeChar(Array.getChar(values, index));
				}
				break;
			case ODBSTypes._short:
				for (int index = 0; index < length; index++) {
					writer.writeShort(Array.getShort(values, index));
				}
				break;
			case ODBSTypes._int:
				for (int index = 0; index < length; index++) {
					writer.writeInt(Array.getInt(values, index));
				}
				break;
			case ODBSTypes._long:
				for (int index = 0; index < length; index++) {
					writer.writeLong(Array.getLong(values, index));
				}
				break;
			case ODBSTypes._float:
				for (int index = 0; index < length; index++) {
					writer.writeFloat(Array.getFloat(values, index));
				}
				break;
			case ODBSTypes._double:
				for (int index = 0; index < length; index++) {
					writer.writeDouble(Array.getDouble(values, index));
				}
				break;
			default:
				throw new IllegalStateException("意外的数组值类型:" + type);
		}
	}

	private final void writeValue(int type, Object value, DataOutput writer) throws IOException {
		switch (type) {
			case ODBSTypes._boolean:
				writer.writeBoolean((boolean) value);
				break;
			case ODBSTypes._byte:
				writer.writeByte((byte) value);
				break;
			case ODBSTypes._char:
				writer.writeChar((char) value);
				break;
			case ODBSTypes._short:
				writer.writeShort((short) value);
				break;
			case ODBSTypes._int:
				writer.writeInt((int) value);
				break;
			case ODBSTypes._long:
				writer.writeLong((long) value);
				break;
			case ODBSTypes._float:
				writer.writeFloat((float) value);
				break;
			case ODBSTypes._double:
				writer.writeDouble((double) value);
				break;
			default:
				throw new IllegalStateException("意外的值类型 " + type);
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////

	public final <T> T readEntity(T instence, InputStream input) throws IOException {
		return readEntity(instence, (DataInput) new BigEndianInputStream(input));
	}

	public final <T> T readEntity(T instence, DataInput reader) throws IOException {
		int type = reader.readVarint();
		ODBSDescription description = odbs.findDesc(type);
		return readEntity(description, instence, reader);
	}

	private final Object readEntity(int type, DataInput reader) throws IOException {
		// 传入的type只能用于定义判断
		// private Attribute = new AttributeBoolean();
		// 由于实体可能存在继承关系，因此需要通过实例类型获取描述对象
		type = reader.readVarint();

		ODBSDescription description = odbs.findDesc(type);
		if (description == null) {
			throw new RuntimeException("对象描述不存在:" + type);
		}

		return readEntity(description, null, reader);
	}

	@SuppressWarnings("unchecked")
	private final <T> T readEntity(ODBSDescription description, T entity, DataInput reader) throws IOException {
		int empty = 0;
		ODBSField field = null;

		if (entity == null) {
			entity = (T) description.newInstence();
		}

		// 字段序列
		int index = reader.readVarint();
		while (index >= 0 && index < description.getFieldCount()) {
			while (empty < index) {
				// 将未传递的字段置为默认值
				field = description.getField(empty++);
				if (field.hasSetter()) {
					field.setDefault(entity);
				}
			}
			empty++;

			field = description.getField(index);
			// 字段值
			if (ODBSTypes.isValue(field.getType().value())) {
				field.setValue(entity, readValue(field.getType().value(), reader));
			} else if (ODBSTypes.isBase(field.getType().value())) {
				field.setValue(entity, readBase(field.getType().value(), reader));
			} else if (ODBSTypes.isEnum(field.getType().value())) {
				field.setValue(entity, readEnum(field.getType().value(), reader));
			} else if (ODBSTypes.isEntity(field.getType().value())) {
				field.setValue(entity, readEntity(field.getType().value(), reader));
			} else if (ODBSTypes.isArray(field.getType().value())) {
				field.setValue(entity, readArray(field.getType().further(), reader));
			} else if (ODBSTypes.isList(field.getType().value())) {
				field.setValue(entity, readList(field.getType().further(), field.getValue(entity), reader));
			} else if (ODBSTypes.isSet(field.getType().value())) {
				field.setValue(entity, readSet(field.getType().further(), field.getValue(entity), reader));
			} else if (ODBSTypes.isMap(field.getType().value())) {
				field.setValue(entity, readMap(field.getType().further(), field.getValue(entity), reader));
			} else if (ODBSTypes.isAny(field.getType().value())) {
				field.setValue(entity, readObject(reader));
			} else {
				throw new IllegalStateException("意外的字段数据类型:" + field.getType());
			}

			index = reader.readVarint();
		}

		while (empty < index) {
			// 将未传递的字段置为默认值
			field = description.getField(empty++);
			if (field.hasSetter()) {
				field.setDefault(entity);
			}
		}
		return entity;
	}

	private final Object readObject(DataInput reader) throws IOException {
		// 定义类型为Object的字段目前只支持基本类型和实体
		int type = reader.readVarint();
		if (ODBSTypes.isBase(type)) {
			return readBase(type, reader);
		} else {
			ODBSDescription description = odbs.findDesc(type);
			if (description == null) {
				throw new IllegalStateException("意外的字段数据类型:" + type);
			} else {
				return readEntity(description, null, reader);
			}
		}
	}

	private final Object readArray(ODBSType type, DataInput reader) throws IOException {
		final int length = reader.readVarint();
		if (ODBSTypes.isValue(type.value())) {
			return readValues(type.value(), length, reader);
		} else if (ODBSTypes.isBase(type.value())) {
			return readBases(type.value(), length, reader);
		} else if (ODBSTypes.isEnum(type.value())) {
			return readEnums(type.value(), length, reader);
		} else if (ODBSTypes.isEntity(type.value())) {
			ODBSDescription description = odbs.findDesc(type.value());
			Object values = Array.newInstance(description.getSourceClass(), length);
			for (int index = 0; index < length; index++) {
				Array.set(values, index, readEntity(type.value(), reader));
			}
			return values;
		} else if (ODBSTypes.isArray(type.value())) {
			Object[] values = new Object[length];
			for (int index = 0; index < length; index++) {
				values[index] = readArray(type.further(), reader);
			}
			return values;
		} else if (ODBSTypes.isList(type.value())) {
			List<?>[] values = new List<?>[length];
			for (int index = 0; index < length; index++) {
				values[index] = readList(type.further(), null, reader);
			}
			return values;
		} else if (ODBSTypes.isSet(type.value())) {
			Set<?>[] values = new Set<?>[length];
			for (int index = 0; index < length; index++) {
				values[index] = readSet(type.further(), null, reader);
			}
			return values;
		} else if (ODBSTypes.isMap(type.value())) {
			Map<?, ?>[] values = new Map<?, ?>[length];
			for (int index = 0; index < length; index++) {
				values[index] = readMap(type.further(), null, reader);
			}
			return values;
		} else {
			throw new IllegalStateException("意外的数组值类型:" + type);
		}
	}

	private final List<?> readList(ODBSType type, List<Object> values, DataInput reader) throws IOException {
		if (values == null) {
			values = new ArrayList<>();
		} else {
			values.clear();
		}

		int length = reader.readVarint();
		if (ODBSTypes.isBase(type.value())) {
			values = readBases(type.value(), length, values, reader);
		} else if (ODBSTypes.isEnum(type.value())) {
			values = readEnums(type.value(), length, values, reader);
		} else if (ODBSTypes.isEntity(type.value())) {
			while (length-- > 0) {
				values.add(readEntity(type.value(), reader));
			}
		} else if (ODBSTypes.isArray(type.value())) {
			while (length-- > 0) {
				values.add(readArray(type.further(), reader));
			}
		} else if (ODBSTypes.isList(type.value())) {
			while (length-- > 0) {
				values.add(readList(type.further(), null, reader));
			}
		} else if (ODBSTypes.isSet(type.value())) {
			while (length-- > 0) {
				values.add(readSet(type.further(), null, reader));
			}
		} else if (ODBSTypes.isMap(type.value())) {
			while (length-- > 0) {
				values.add(readMap(type.further(), null, reader));
			}
		} else {
			throw new IllegalStateException("意外的List值类型:" + type);
		}
		return values;
	}

	private final Set<?> readSet(ODBSType type, Set<Object> values, DataInput reader) throws IOException {
		if (values == null) {
			values = new HashSet<>();
		} else {
			values.clear();
		}

		int length = reader.readVarint();
		if (ODBSTypes.isBase(type.value())) {
			values = readBases(type.value(), length, values, reader);
		} else if (ODBSTypes.isEnum(type.value())) {
			values = readEnums(type.value(), length, values, reader);
		} else if (ODBSTypes.isEntity(type.value())) {
			while (length-- > 0) {
				values.add(readEntity(type.value(), reader));
			}
		} else if (ODBSTypes.isArray(type.value())) {
			while (length-- > 0) {
				values.add(readArray(type.further(), reader));
			}
		} else if (ODBSTypes.isList(type.value())) {
			while (length-- > 0) {
				values.add(readList(type.further(), null, reader));
			}
		} else if (ODBSTypes.isSet(type.value())) {
			while (length-- > 0) {
				values.add(readSet(type.further(), null, reader));
			}
		} else if (ODBSTypes.isMap(type.value())) {
			while (length-- > 0) {
				values.add(readMap(type.further(), null, reader));
			}
		} else {
			throw new IllegalStateException("意外的Set值类型 " + type);
		}
		return values;
	}

	private final Map<?, ?> readMap(ODBSType type, Map<Object, Object> values, DataInput reader) throws IOException {
		if (values == null) {
			values = new HashMap<>();
		} else {
			values.clear();
		}

		int length = reader.readVarint();
		if (ODBSTypes.isBase(type.key())) {
			if (ODBSTypes.isBase(type.value())) {
				while (length-- > 0) {
					values.put(readBase(type.key(), reader), readBase(type.value(), reader));
				}
			} else if (ODBSTypes.isEnum(type.value())) {
				while (length-- > 0) {
					values.put(readBase(type.key(), reader), readEnum(type.value(), reader));
				}
			} else if (ODBSTypes.isEntity(type.value())) {
				while (length-- > 0) {
					values.put(readBase(type.key(), reader), readEntity(type.value(), reader));
				}
			} else if (ODBSTypes.isArray(type.value())) {
				while (length-- > 0) {
					values.put(readBase(type.key(), reader), readArray(type.further(), reader));
				}
			} else if (ODBSTypes.isList(type.value())) {
				while (length-- > 0) {
					values.put(readBase(type.key(), reader), readList(type.further(), null, reader));
				}
			} else if (ODBSTypes.isSet(type.value())) {
				while (length-- > 0) {
					values.put(readBase(type.key(), reader), readSet(type.further(), null, reader));
				}
			} else if (ODBSTypes.isMap(type.value())) {
				while (length-- > 0) {
					values.put(readBase(type.key(), reader), readMap(type.further(), null, reader));
				}
			} else {
				throw new IllegalStateException("意外的Map值类型:" + type);
			}
		} else if (ODBSTypes.isEnum(type.key())) {
			if (ODBSTypes.isBase(type.value())) {
				while (length-- > 0) {
					values.put(readEnum(type.key(), reader), readBase(type.value(), reader));
				}
			} else if (ODBSTypes.isEnum(type.value())) {
				while (length-- > 0) {
					values.put(readEnum(type.key(), reader), readEnum(type.value(), reader));
				}
			} else if (ODBSTypes.isEntity(type.value())) {
				while (length-- > 0) {
					values.put(readEnum(type.key(), reader), readEntity(type.value(), reader));
				}
			} else if (ODBSTypes.isArray(type.value())) {
				while (length-- > 0) {
					values.put(readEnum(type.key(), reader), readArray(type.further(), reader));
				}
			} else if (ODBSTypes.isList(type.value())) {
				while (length-- > 0) {
					values.put(readEnum(type.key(), reader), readList(type.further(), null, reader));
				}
			} else if (ODBSTypes.isSet(type.value())) {
				while (length-- > 0) {
					values.put(readEnum(type.key(), reader), readSet(type.further(), null, reader));
				}
			} else if (ODBSTypes.isMap(type.value())) {
				while (length-- > 0) {
					values.put(readEnum(type.key(), reader), readMap(type.further(), null, reader));
				}
			} else {
				throw new IllegalStateException("意外的Map值类型:" + type);
			}
		} else if (ODBSTypes.isEntity(type.key())) {
			if (ODBSTypes.isBase(type.value())) {
				while (length-- > 0) {
					values.put(readEntity(type.key(), reader), readBase(type.value(), reader));
				}
			} else if (ODBSTypes.isEnum(type.value())) {
				while (length-- > 0) {
					values.put(readEnum(type.key(), reader), readEnum(type.value(), reader));
				}
			} else if (ODBSTypes.isEntity(type.value())) {
				while (length-- > 0) {
					values.put(readEntity(type.key(), reader), readEntity(type.value(), reader));
				}
			} else if (ODBSTypes.isArray(type.value())) {
				while (length-- > 0) {
					values.put(readEntity(type.key(), reader), readArray(type.further(), reader));
				}
			} else if (ODBSTypes.isList(type.value())) {
				while (length-- > 0) {
					values.put(readEntity(type.key(), reader), readList(type.further(), null, reader));
				}
			} else if (ODBSTypes.isSet(type.value())) {
				while (length-- > 0) {
					values.put(readEntity(type.key(), reader), readSet(type.further(), null, reader));
				}
			} else if (ODBSTypes.isMap(type.value())) {
				while (length-- > 0) {
					values.put(readEntity(type.key(), reader), readMap(type.further(), null, reader));
				}
			} else {
				throw new IllegalStateException("意外的Map值类型:" + type);
			}
		} else if (ODBSTypes.isArray(type.key())) {
			throw new IllegalStateException("不支持Array作为Map键类型:" + type);
		} else if (ODBSTypes.isList(type.key())) {
			throw new IllegalStateException("不支持List作为Map键类型:" + type);
		} else if (ODBSTypes.isSet(type.key())) {
			throw new IllegalStateException("不支持Set作为Map键类型:" + type);
		} else if (ODBSTypes.isMap(type.key())) {
			throw new IllegalStateException("不支持Map作为Map键类型:" + type);
		} else {
			throw new IllegalStateException("意外的Map键类型: " + type);
		}

		return values;
	}

	private final <T extends Collection<? super Object>> T readEnums(final int type, int length, T collection, DataInput reader) throws IOException {
		// 枚举数组读取避免多次获取描述对象
		// 枚举不存在继承情况
		final ODBSEnumeration enumeration = odbs.findEnum(type);
		while (length-- > 0) {
			collection.add(enumeration.getConstant(reader.readVarint()));
		}
		return collection;
	}

	private final Object readEnums(final int type, int length, DataInput reader) throws IOException {
		// 枚举数组读取避免多次获取描述对象
		// 枚举不存在继承情况
		final ODBSEnumeration enumeration = odbs.findEnum(type);
		Object array = Array.newInstance(enumeration.getSourceClass(), length);
		for (int index = 0; index < length; index++) {
			Array.set(array, index, enumeration.getConstant(reader.readVarint()));
		}
		return array;
	}

	private final Object readEnum(final int type, DataInput reader) throws IOException {
		// 枚举不存在继承情况
		final ODBSEnumeration enumeration = odbs.findEnum(type);
		return enumeration.getConstant(reader.readVarint());
	}

	private final <T extends Collection<? super Object>> T readBases(final int type, int length, T collection, DataInput reader) throws IOException {
		// collection.clear();

		// 基础类型不存在继承情况
		switch (type) {
			case ODBSTypes.BOOLEAN:
				while (length-- > 0) {
					collection.add(Boolean.valueOf(reader.readBoolean()));
				}
				break;
			case ODBSTypes.BYTE:
				while (length-- > 0) {
					collection.add(Byte.valueOf(reader.readByte()));
				}
				break;
			case ODBSTypes.CHARACTER:
				while (length-- > 0) {
					collection.add(Character.valueOf(reader.readChar()));
				}
				break;
			case ODBSTypes.SHORT:
				while (length-- > 0) {
					collection.add(Short.valueOf(reader.readShort()));
				}
				break;
			case ODBSTypes.INTEGER:
				while (length-- > 0) {
					collection.add(Integer.valueOf(reader.readInt()));
				}
				break;
			case ODBSTypes.LONG:
				while (length-- > 0) {
					collection.add(Long.valueOf(reader.readLong()));
				}
				break;
			case ODBSTypes.FLOAT:
				while (length-- > 0) {
					collection.add(Float.valueOf(reader.readFloat()));
				}
				break;
			case ODBSTypes.DOUBLE:
				while (length-- > 0) {
					collection.add(Double.valueOf(reader.readDouble()));
				}
				break;
			case ODBSTypes.BIG_DECIMAL:
				while (length-- > 0) {
					collection.add(reader.readDecimal());
				}
				break;
			case ODBSTypes.DATE:
				while (length-- > 0) {
					collection.add(reader.readDate());
				}
				break;
			case ODBSTypes.LOCAL_TIME:
				while (length-- > 0) {
					collection.add(reader.readLocalTime());
				}
				break;
			case ODBSTypes.LOCAL_DATE:
				while (length-- > 0) {
					collection.add(reader.readLocalDate());
				}
				break;
			case ODBSTypes.LOCAL_DATE_TIME:
				while (length-- > 0) {
					collection.add(reader.readLocalDateTime());
				}
				break;
			case ODBSTypes.STRING:
				while (length-- > 0) {
					collection.add(reader.readString());
				}
				break;
			default:
				throw new IllegalStateException("意外的数组基础类型 " + type);
		}
		return collection;
	}

	private final Object readBases(final int type, int length, DataInput reader) throws IOException {
		// 基础类型不存在继承情况
		switch (type) {
			case ODBSTypes.BOOLEAN:
				final Boolean[] booleans = new Boolean[length];
				for (int index = 0; index < length; index++) {
					booleans[index] = reader.readBoolean();
				}
				return booleans;
			case ODBSTypes.BYTE:
				final Byte[] bytes = new Byte[length];
				for (int index = 0; index < length; index++) {
					bytes[index] = reader.readByte();
				}
				return bytes;
			case ODBSTypes.CHARACTER:
				final Character[] chars = new Character[length];
				for (int index = 0; index < length; index++) {
					chars[index] = reader.readChar();
				}
				return chars;
			case ODBSTypes.SHORT:
				final Short[] shorts = new Short[length];
				for (int index = 0; index < length; index++) {
					shorts[index] = reader.readShort();
				}
				return shorts;
			case ODBSTypes.INTEGER:
				final Integer[] integers = new Integer[length];
				for (int index = 0; index < length; index++) {
					integers[index] = reader.readInt();
				}
				return integers;
			case ODBSTypes.LONG:
				final Long[] longs = new Long[length];
				for (int index = 0; index < length; index++) {
					longs[index] = reader.readLong();
				}
				return longs;
			case ODBSTypes.FLOAT:
				final Float[] floats = new Float[length];
				for (int index = 0; index < length; index++) {
					floats[index] = reader.readFloat();
				}
				return floats;
			case ODBSTypes.DOUBLE:
				final Double[] doubles = new Double[length];
				for (int index = 0; index < length; index++) {
					doubles[index] = reader.readDouble();
				}
				return doubles;
			case ODBSTypes.BIG_DECIMAL:
				final BigDecimal[] decimals = new BigDecimal[length];
				for (int index = 0; index < length; index++) {
					decimals[index] = reader.readDecimal();
				}
				return decimals;
			case ODBSTypes.DATE:
				final Date[] dates = new Date[length];
				for (int index = 0; index < length; index++) {
					dates[index] = reader.readDate();
				}
				return dates;
			case ODBSTypes.LOCAL_TIME:
				final LocalTime[] localtimes = new LocalTime[length];
				for (int index = 0; index < length; index++) {
					localtimes[index] = reader.readLocalTime();
				}
				return localtimes;
			case ODBSTypes.LOCAL_DATE:
				final LocalDate[] localdates = new LocalDate[length];
				for (int index = 0; index < length; index++) {
					localdates[index] = reader.readLocalDate();
				}
				return localdates;
			case ODBSTypes.LOCAL_DATE_TIME:
				final LocalDateTime[] localdatetimes = new LocalDateTime[length];
				for (int index = 0; index < length; index++) {
					localdatetimes[index] = reader.readLocalDateTime();
				}
				return localdatetimes;
			case ODBSTypes.STRING:
				final String[] strings = new String[length];
				for (int index = 0; index < length; index++) {
					strings[index] = reader.readString();
				}
				return strings;
			default:
				throw new IllegalStateException("意外的数组基础类型 " + type);
		}
	}

	private final Object readBase(final int type, DataInput reader) throws IOException {
		switch (type) {
			case ODBSTypes.BOOLEAN:
				return reader.readBoolean();
			case ODBSTypes.BYTE:
				return reader.readByte();
			case ODBSTypes.CHARACTER:
				return reader.readChar();
			case ODBSTypes.SHORT:
				return reader.readShort();
			case ODBSTypes.INTEGER:
				return reader.readInt();
			case ODBSTypes.LONG:
				return reader.readLong();
			case ODBSTypes.FLOAT:
				return reader.readFloat();
			case ODBSTypes.DOUBLE:
				return reader.readDouble();
			case ODBSTypes.BIG_DECIMAL:
				return reader.readDecimal();
			case ODBSTypes.DATE:
				return reader.readDate();
			case ODBSTypes.LOCAL_TIME:
				return reader.readLocalTime();
			case ODBSTypes.LOCAL_DATE:
				return reader.readLocalDate();
			case ODBSTypes.LOCAL_DATE_TIME:
				return reader.readLocalDateTime();
			case ODBSTypes.STRING:
				return reader.readString();
			default:
				throw new IllegalStateException("意外的基础类型:" + type);
		}
	}

	private final Object readValues(final int type, int length, DataInput reader) throws IOException {
		// 数组读取通过此方法避免过多的装箱拆箱操作
		// 值类型不存在继承情况
		switch (type) {
			case ODBSTypes._boolean:
				final boolean[] booleans = new boolean[length];
				for (int index = 0; index < length; index++) {
					booleans[index] = reader.readBoolean();
				}
				return booleans;
			case ODBSTypes._byte:
				final byte[] bytes = new byte[length];
				for (int index = 0; index < length; index++) {
					bytes[index] = reader.readByte();
				}
				return bytes;
			case ODBSTypes._char:
				final char[] chars = new char[length];
				for (int index = 0; index < length; index++) {
					chars[index] = reader.readChar();
				}
				return chars;
			case ODBSTypes._short:
				final short[] shorts = new short[length];
				for (int index = 0; index < length; index++) {
					shorts[index] = reader.readShort();
				}
				return shorts;
			case ODBSTypes._int:
				final int[] ints = new int[length];
				for (int index = 0; index < length; index++) {
					ints[index] = reader.readInt();
				}
				return ints;
			case ODBSTypes._long:
				final long[] longs = new long[length];
				for (int index = 0; index < length; index++) {
					longs[index] = reader.readLong();
				}
				return longs;
			case ODBSTypes._float:
				final float[] floats = new float[length];
				for (int index = 0; index < length; index++) {
					floats[index] = reader.readFloat();
				}
				return floats;
			case ODBSTypes._double:
				final double[] doubles = new double[length];
				for (int index = 0; index < length; index++) {
					doubles[index] = reader.readDouble();
				}
				return doubles;
			default:
				throw new IllegalStateException("意外的数组值类型:" + type);
		}
	}

	private final Object readValue(final int type, DataInput readr) throws IOException {
		switch (type) {
			case ODBSTypes._boolean:
				return readr.readBoolean();
			case ODBSTypes._byte:
				return readr.readByte();
			case ODBSTypes._char:
				return readr.readChar();
			case ODBSTypes._short:
				return readr.readShort();
			case ODBSTypes._int:
				return readr.readInt();
			case ODBSTypes._long:
				return readr.readLong();
			case ODBSTypes._float:
				return readr.readFloat();
			case ODBSTypes._double:
				return readr.readDouble();
			default:
				throw new IllegalStateException("意外的值类型:" + type);
		}
	}
}
