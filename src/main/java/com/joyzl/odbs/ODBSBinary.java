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

public final class ODBSBinary extends ODBSBinaryCodec {

	public ODBSBinary(ODBS odbs) {
		super(odbs);
	}

	public void writeEntities(Collection<?> entities, OutputStream out) throws IOException {
		writeEntities(entities, (DataOutput) new BigEndianOutputStream(out));
	}

	public void writeEntities(Collection<?> entities, DataOutput out) throws IOException {
		// 实体数量标识
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

		// 实体类型标识
		out.writeVarint(type.index());
		writeEntity(out, type, entity);
	}

	public void writeStrictEntity(Object entity, DataOutput out) throws IOException {
		final TypeEntity t = odbs.get(entity.getClass());
		if (t == null) {
			throw new IOException("ODBS Binary 类型无效");
		}
		// 没有实体类型标识
		writeEntity(out, t, entity);
	}

	public final <T> List<T> readEntities(InputStream in) throws IOException {
		if (in.available() > 0) {
			final List<T> entities = new ArrayList<>();
			readEntities(entities, (DataInput) new BigEndianInputStream(in));
			return entities;
		}
		return null;
	}

	public <T> List<T> readEntities(DataInput input) throws IOException {
		final List<T> entities = new ArrayList<>();
		readEntities(entities, (DataInput) input);
		return entities;
	}

	public <T> void readEntities(Collection<T> entities, InputStream in) throws IOException {
		if (in.available() > 0) {
			readEntities(entities, (DataInput) new BigEndianInputStream(in));
		}
	}

	public <T> void readEntities(Collection<T> entities, DataInput in) throws IOException {
		int size = in.readVarint();
		while (size-- > 0) {
			entities.add(readEntity(null, in));
		}
	}

	public <T> T readEntity(InputStream in) throws IOException {
		if (in.available() > 0) {
			return readEntity(null, (DataInput) new BigEndianInputStream(in));
		}
		return null;
	}

	public <T> T readEntity(DataInput in) throws IOException {
		// 读取实体类型标识
		final TypeEntity t = odbs.get(in.readVarint());
		if (t != null) {
			return readEntity(in, t, null);
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}

	public <T> T readEntity(T instence, InputStream in) throws IOException {
		if (in.available() > 0) {
			return readEntity(instence, (DataInput) new BigEndianInputStream(in));
		}
		return null;
	}

	public <T> T readEntity(T entity, DataInput in) throws IOException {
		// 读取实体类型标识
		final TypeEntity t = odbs.get(in.readVarint());
		if (t != null) {
			return readEntity(in, t, entity);
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}

	public <T> T readStrictEntity(T entity, DataInput in) throws IOException {
		final TypeEntity t = odbs.get(entity.getClass());
		if (t != null) {
			return readEntity(in, t, entity);
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}

	public <T> T readStrictEntity(Class<T> type, DataInput in) throws IOException {
		final TypeEntity t = odbs.get(type);
		if (type != null) {
			return readEntity(in, t, null);
		} else {
			throw new IOException("ODBS Binary 类型无效");
		}
	}
}