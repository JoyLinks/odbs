package com.joyzl.odbs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.joyzl.codec.BigEndianInputStream;
import com.joyzl.codec.BigEndianOutputStream;
import com.joyzl.codec.DataInput;
import com.joyzl.codec.DataOutput;

public final class ODBSBinary {

	private final ODBS odbs;
	private final ODBSBinaryEncode encode;
	private final ODBSBinaryDecode decode;

	public ODBSBinary(ODBS odbs) {
		encode = new ODBSBinaryEncode(odbs);
		decode = new ODBSBinaryDecode(odbs);
		this.odbs = odbs;
	}

	public void writeEntities(Collection<?> entities, OutputStream out) throws IOException {
		writeEntities(entities, (DataOutput) new BigEndianOutputStream(out));
	}

	public void writeEntities(Collection<?> entities, DataOutput out) throws IOException {
		// 写入实体数量
		out.writeVarint(entities.size());
		for (Object entity : entities) {
			writeEntity(entity, out);
		}
	}

	public void writeEntity(Object entity, OutputStream out) throws IOException {
		writeEntity(entity, (DataOutput) new BigEndianOutputStream(out));
	}

	public void writeEntity(Object entity, DataOutput out) throws IOException {
		final TypeEntity type = odbs.get(entity.getClass());
		if (type == null) {
			throw new IOException("ODBS Binary 类型无效");
		}
		// 写入实体类型标识
		out.writeVarint(type.index());
		encode.writeEntity(out, type, entity);
	}

	public final <T> List<T> readEntities(InputStream in) throws IOException {
		final List<T> entities = new ArrayList<>();
		readEntities(entities, (DataInput) new BigEndianInputStream(in));
		return entities;
	}

	public <T> List<T> readEntities(DataInput input) throws IOException {
		final List<T> entities = new ArrayList<>();
		readEntities(entities, (DataInput) input);
		return entities;
	}

	public <T> void readEntities(Collection<T> entities, InputStream in) throws IOException {
		readEntities(entities, (DataInput) new BigEndianInputStream(in));
	}

	public <T> void readEntities(Collection<T> entities, DataInput in) throws IOException {
		// 读取实体数量
		int size = in.readVarint();
		while (size-- > 0) {
			entities.add(readEntity(null, in));
		}
	}

	public <T> T readEntity(InputStream in) throws IOException {
		return readEntity(null, (DataInput) new BigEndianInputStream(in));
	}

	public <T> T readEntity(DataInput in) throws IOException {
		// 读取实体类型标识
		final TypeEntity t = odbs.get(in.readVarint());
		if (t != null) {
			return decode.readEntity(in, t, null);
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}

	public <T> T readEntity(T entity, InputStream in) throws IOException {
		return readEntity(entity, (DataInput) new BigEndianInputStream(in));
	}

	public <T> T readEntity(T entity, DataInput in) throws IOException {
		// 读取实体类型标识
		final TypeEntity t = odbs.get(in.readVarint());
		if (t != null) {
			return decode.readEntity(in, t, entity);
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}

	public <T> List<T> readStrictEntities(Class<T> type, InputStream in) throws IOException {
		final List<T> entities = new ArrayList<>();
		readStrictEntities(entities, type, (DataInput) new BigEndianInputStream(in));
		return entities;
	}

	public <T> List<T> readStrictEntities(Class<T> type, DataInput in) throws IOException {
		final List<T> entities = new ArrayList<>();
		readStrictEntities(entities, type, in);
		return entities;
	}

	public <T> void readStrictEntities(Collection<T> entities, Class<T> type, InputStream in) throws IOException {
		readStrictEntities(entities, type, (DataInput) new BigEndianInputStream(in));
	}

	public <T> void readStrictEntities(Collection<T> entities, Class<T> type, DataInput in) throws IOException {
		final TypeEntity t = odbs.get(type);
		if (type != null) {
			// 读取实体数量
			int size = in.readVarint();
			while (size-- > 0) {
				entities.add(decode.readEntity(in, t, null));
			}
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}

	public <T> T readStrictEntity(T entity, InputStream in) throws IOException {
		return readStrictEntity(entity, (DataInput) new BigEndianInputStream(in));
	}

	public <T> T readStrictEntity(T entity, DataInput in) throws IOException {
		final TypeEntity t = odbs.get(entity.getClass());
		if (t != null) {
			return decode.readEntity(in, t, entity);
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}

	public <T> T readStrictEntity(Class<T> type, InputStream in) throws IOException {
		return readStrictEntity(type, (DataInput) new BigEndianInputStream(in));
	}

	public <T> T readStrictEntity(Class<T> type, DataInput in) throws IOException {
		final TypeEntity t = odbs.get(type);
		if (type != null) {
			return decode.readEntity(in, t, null);
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}

	public ODBS odbs() {
		return odbs;
	}
}