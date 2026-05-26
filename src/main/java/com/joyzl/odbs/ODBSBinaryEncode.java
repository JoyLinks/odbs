package com.joyzl.odbs;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.joyzl.EnumCode;
import com.joyzl.EnumCodeText;
import com.joyzl.EnumText;
import com.joyzl.codec.DataOutput;

final class ODBSBinaryEncode implements ODBSEncode<DataOutput> {

	protected final ODBS odbs;

	public ODBSBinaryEncode(ODBS odbs) {
		this.odbs = odbs;
	}

	@Override
	public void writeArray(DataOutput out, ODBSType value, Object values) throws IllegalArgumentException, IOException {
		if (value instanceof ValueType) {
			// 为值数组提供特殊处理
			// 避免值被装箱为对象
			if (value.type() == byte.class) {
				final byte[] v = (byte[]) values;
				out.writeVarint(v.length);
				out.write(v);
			} else if (value.type() == int.class) {
				final int[] v = (int[]) values;
				out.writeVarint(v.length);
				for (int i = 0; i < v.length; i++) {
					out.writeVarint(v[i]);
				}
			} else if (value.type() == char.class) {
				final char[] v = (char[]) values;
				out.writeVarint(v.length);
				for (int i = 0; i < v.length; i++) {
					out.writeVarint(v[i]);
				}
			} else if (value.type() == short.class) {
				final short[] v = (short[]) values;
				out.writeVarint(v.length);
				for (int i = 0; i < v.length; i++) {
					out.writeVarint(v[i]);
				}
			} else if (value.type() == float.class) {
				final float[] v = (float[]) values;
				out.writeVarint(v.length);
				for (int i = 0; i < v.length; i++) {
					out.writeFloat(v[i]);
				}
			} else if (value.type() == double.class) {
				final double[] v = (double[]) values;
				out.writeVarint(v.length);
				for (int i = 0; i < v.length; i++) {
					out.writeDouble(v[i]);
				}
			} else if (value.type() == long.class) {
				final long[] v = (long[]) values;
				out.writeVarint(v.length);
				for (int i = 0; i < v.length; i++) {
					out.writeVarlong(v[i]);
				}
			} else if (value.type() == boolean.class) {
				final boolean[] v = (boolean[]) values;
				out.writeVarint(v.length);
				for (int i = 0; i < v.length; i++) {
					out.writeBoolean(v[i]);
				}
			} else {
				throw new IOException("未知值类型");
			}
		} else {
			final int size = Array.getLength(values);
			out.writeVarint(size);
			for (int i = 0; i < size; i++) {
				value.write(Array.get(values, i), this, out);
			}
		}
	}

	@Override
	public void writeBigDecimal(DataOutput out, BigDecimal value) throws IOException {
		out.writeDecimal(value);
	}

	@Override
	public void writeBigInteger(DataOutput out, BigInteger value) throws IOException {
		final byte[] bytes = value.toByteArray();
		out.writeByte(bytes.length);
		out.write(bytes);
	}

	@Override
	public void writeBoolean(DataOutput out, boolean value) throws IOException {
		// 省略编码，因为始终为true才编码，false为默认值不输出
	}

	@Override
	public void writeBoolean(DataOutput out, Boolean value) throws IOException {
		out.writeBoolean(value.booleanValue());
	}

	@Override
	public void writeByte(DataOutput out, byte value) throws IOException {
		out.writeByte(value);
	}

	@Override
	public void writeChar(DataOutput out, char value) throws IOException {
		out.writeChar(value);
	}

	@Override
	public void writeCollection(DataOutput out, ODBSType value, Collection<?> values) throws IOException {
		out.writeVarint(values.size());
		for (Object v : values) {
			value.write(v, this, out);
		}
	}

	@Override
	public void writeDate(DataOutput out, Date value) throws IOException {
		out.writeDate(value);
	}

	@Override
	public void writeDouble(DataOutput out, double value) throws IOException {
		out.writeDouble(value);
	}

	@Override
	public void writeEnum(DataOutput out, Enum<?> value) throws IOException {
		out.writeVarint(value.ordinal());
	}

	@Override
	public void writeEnumCode(DataOutput out, EnumCode value) throws IOException {
		out.writeVarint(value.code());
	}

	@Override
	public void writeEnumCodeText(DataOutput out, EnumCodeText value) throws IOException {
		out.writeVarint(value.code());
	}

	@Override
	public void writeEnumText(DataOutput out, EnumText value) throws IOException {
		out.writeVarint(value.ordinal());
	}

	@Override
	public void writeField(DataOutput out, ODBSMethod method) throws IOException {
		out.writeVarint(method.index());
	}

	@Override
	public void writeFloat(DataOutput out, float value) throws IOException {
		out.writeFloat(value);
	}

	@Override
	public void writeInt(DataOutput out, int value) throws IOException {
		out.writeVarint(value);
	}

	@Override
	public void writeList(DataOutput out, ODBSType value, List<?> values) throws IOException {
		out.writeVarint(values.size());
		for (int i = 0; i < values.size(); i++) {
			value.write(values.get(i), this, out);
		}
	}

	@Override
	public void writeLocalDateTime(DataOutput out, LocalDateTime value) throws IOException {
		out.writeLocalDateTime(value);
	}

	@Override
	public void writeLocaleDate(DataOutput out, LocalDate value) throws IOException {
		out.writeLocalDate(value);
	}

	@Override
	public void writeLocalTime(DataOutput out, LocalTime value) throws IOException {
		out.writeLocalTime(value);
	}

	@Override
	public void writeLong(DataOutput out, long value) throws IOException {
		out.writeLong(value);
	}

	@Override
	public void writeMap(DataOutput out, ODBSType key, ODBSType value, Map<?, ?> values) throws IOException {
		out.writeVarint(values.size());
		for (Entry<?, ?> entry : values.entrySet()) {
			key.write(entry.getKey(), this, out);
			value.write(entry.getValue(), this, out);
		}
	}

	@Override
	public void writeEntity(DataOutput out, TypeEntity type, Object value) throws IOException {
		// 实体字段编码
		ODBSMethod method;
		for (int index = 0; index < type.methods().length; index++) {
			method = type.methods()[index];
			if (method.get() != null && method.set() != null) {
				method.type().write1(value, method, this, out);
			}
		}
		// 实体结束
		out.writeVarint(type.methods().length);
	}

	@Override
	public void writeObject(DataOutput out, TypeObject type, Object value) throws IOException {
		final TypeEntity t = odbs.get(value.getClass());
		if (t == null) {
			throw new IOException("ODBS Binary 类型无效");
		}

		// 实体类型标识
		out.writeVarint(t.index());
		// 实体编码
		writeEntity(out, t, value);
	}

	@Override
	public void writeSet(DataOutput out, ODBSType value, Set<?> values) throws IOException {
		out.writeVarint(values.size());
		for (Object v : values) {
			value.write(v, this, out);
		}
	}

	@Override
	public void writeShort(DataOutput out, short value) throws IOException {
		out.writeShort(value);
	}

	@Override
	public void writeString(DataOutput out, String value) throws IOException {
		out.writeString(value);
	}
}