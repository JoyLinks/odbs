package com.joyzl.odbs;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ODBSJson extends ODBSJsonCodec {

	public ODBSJson(ODBS odbs) {
		super(odbs);
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
			writeEntity(out, t, v);
		}
		out.endArray();
	}

	public void writeEntity(Object entity, Writer writer) throws IOException {
		final TypeEntity t = odbs.get(entity.getClass());
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		writeEntity(JSONWriter.instance(writer), t, entity);
	}

	public <T> List<T> readEntities(Class<T> type, Reader reader) throws IOException {
		final TypeEntity t = odbs.get(type);
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		final List<T> entities = new ArrayList<>();
		readList(JSONReader.instance(reader), t, entities);
		return entities;
	}

	public <T> void readEntities(Collection<T> entities, Class<T> type, Reader reader) throws IOException {
		final TypeEntity t = odbs.get(type);
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		readCollection(JSONReader.instance(reader), t, entities);
	}

	public <T> T readEntity(Class<T> type, Reader reader) throws IOException {
		final TypeEntity t = odbs.get(type);
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		return readEntity(JSONReader.instance(reader), t, null);
	}

	public <T> T readEntity(T instence, Reader reader) throws IOException {
		final TypeEntity t = odbs.get(instence.getClass());
		if (t == null) {
			throw new IOException("ODBS JSON 类型无效");
		}
		return readEntity(JSONReader.instance(reader), t, instence);
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
}