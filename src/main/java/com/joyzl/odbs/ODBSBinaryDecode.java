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
import java.util.Set;

import com.joyzl.codec.DataInput;

final class ODBSBinaryDecode implements ODBSDecode<DataInput> {

	protected final ODBS odbs;

	public ODBSBinaryDecode(ODBS odbs) {
		this.odbs = odbs;
	}

	@Override
	public Object readArray(DataInput in, ODBSType type, Object values) throws IOException {
		final int size = in.readVarint();
		if (values == null) {
			values = type.newArray(size);
		} else {
			if (Array.getLength(values) != size) {
				values = type.newArray(size);
			}
		}

		if (type instanceof ValueType) {
			// 为值数组提供特殊处理
			// 避免值被装箱为对象
			if (type.type() == byte.class) {
				final byte[] v = (byte[]) values;
				in.readFully(v);
			} else if (type.type() == int.class) {
				final int[] v = (int[]) values;
				for (int i = 0; i < size; i++) {
					v[i] = in.readVarint();
				}
			} else if (type.type() == char.class) {
				final char[] v = (char[]) values;
				for (int i = 0; i < size; i++) {
					v[i] = (char) in.readVarint();
				}
			} else if (type.type() == short.class) {
				final short[] v = (short[]) values;
				for (int i = 0; i < size; i++) {
					v[i] = (short) in.readVarint();
				}
			} else if (type.type() == float.class) {
				final float[] v = (float[]) values;
				for (int i = 0; i < size; i++) {
					v[i] = in.readFloat();
				}
			} else if (type.type() == double.class) {
				final double[] v = (double[]) values;
				for (int i = 0; i < size; i++) {
					v[i] = in.readDouble();
				}
			} else if (type.type() == long.class) {
				final long[] v = (long[]) values;
				for (int i = 0; i < size; i++) {
					v[i] = in.readVarlong();
				}
			} else if (type.type() == boolean.class) {
				final boolean[] v = (boolean[]) values;
				for (int i = 0; i < size; i++) {
					v[i] = in.readBoolean();
				}
			} else {
				throw new IOException("未知值类型");
			}
		} else {
			for (int i = 0; i < size; i++) {
				Array.set(values, i, type.read(this, in));
			}
		}
		return values;
	}

	@Override
	public BigDecimal readBigDecimal(DataInput in) throws IOException {
		return in.readDecimal();
	}

	@Override
	public BigInteger readBigInteger(DataInput in) throws IOException {
		final byte[] bytes = new byte[in.readUnsignedByte()];
		in.readFully(bytes);
		return new BigInteger(bytes);
	}

	@Override
	public boolean readBool(DataInput in) {
		return true;
	}

	@Override
	public boolean readBoolea(DataInput in) throws IOException {
		return in.readBoolean();
	}

	@Override
	public byte readByte(DataInput in) throws IOException {
		return in.readByte();
	}

	@Override
	public char readChar(DataInput in) throws IOException {
		return in.readChar();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> void readCollection(DataInput in, ODBSType type, Collection<V> values) throws IOException {
		final int size = in.readVarint();
		for (int i = 0; i < size; i++) {
			values.add((V) type.read(this, in));
		}
	}

	@Override
	public Date readDate(DataInput in) throws IOException {
		return in.readDate();
	}

	@Override
	public double readDouble(DataInput in) throws IOException {
		return in.readDouble();
	}

	@Override
	public int readEnum(DataInput in) throws IOException {
		return in.readVarint();
	}

	@Override
	public int readEnumCode(DataInput in) throws IOException {
		return in.readVarint();
	}

	@Override
	public int readEnumCodeText(DataInput in) throws IOException {
		return in.readVarint();
	}

	@Override
	public int readEnumText(DataInput in) throws IOException {
		return in.readVarint();
	}

	@Override
	public float readFloat(DataInput in) throws IOException {
		return in.readFloat();
	}

	@Override
	public int readInt(DataInput in) throws IOException {
		return in.readVarint();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> void readList(DataInput in, ODBSType value, List<V> values) throws IOException {
		int size = in.readVarint();
		while (size-- > 0) {
			values.add((V) value.read(this, in));
		}
	}

	@Override
	public LocalDate readLocalDate(DataInput in) throws IOException {
		return in.readLocalDate();
	}

	@Override
	public LocalDateTime readLocalDateTime(DataInput in) throws IOException {
		return in.readLocalDateTime();
	}

	@Override
	public LocalTime readLocalTime(DataInput in) throws IOException {
		return in.readLocalTime();
	}

	@Override
	public long readLong(DataInput in) throws IOException {
		return in.readLong();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K, V> void readMap(DataInput in, ODBSType key, ODBSType value, Map<K, V> values) throws IOException {
		int size = in.readVarint();
		while (size-- > 0) {
			values.put((K) key.read(this, in), (V) value.read(this, in));
		}
	}

	@Override
	public <T> T readEntity(DataInput in, TypeEntity type, T entity) throws IOException {
		if (entity == null) {
			entity = type.newInstance();
		}

		// 实体字段解码
		ODBSMethod method;
		int index = in.readVarint();
		for (int i = 0; i < type.methods().length; i++) {
			method = type.methods()[i];
			if (i < index) {
				if (method.set() != null) {
					method.type().give(entity, method);
				}
			} else {
				if (method.set() != null) {
					method.type().read(entity, method, this, in);
				}
				index = in.readVarint();
			}
		}
		return entity;
	}

	@Override
	public Object readObject(DataInput in, TypeObject type, Object value) throws IOException {
		final TypeEntity t = odbs.get(in.readVarint());
		if (t != null) {
			return readEntity(in, t, value);
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V> void readSet(DataInput in, ODBSType type, Set<V> values) throws IOException {
		final int size = in.readVarint();
		for (int i = 0; i < size; i++) {
			values.add((V) type.read(this, in));
		}
	}

	@Override
	public short readShort(DataInput in) throws IOException {
		return in.readShort();
	}

	@Override
	public String readString(DataInput in) throws IOException {
		return in.readString();
	}
}