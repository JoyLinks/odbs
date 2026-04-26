package com.joyzl.odbs;

import java.io.IOException;
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

abstract interface ODBSDecode<I> {

	abstract Object readArray(I in, ODBSType value, Object values) throws IOException;

	abstract BigDecimal readBigDecimal(I in) throws IOException;

	abstract BigInteger readBigInteger(I in) throws IOException;

	abstract boolean readBool(I in) throws IOException;

	abstract boolean readBoolea(I in) throws IOException;

	abstract byte readByte(I in) throws IOException;

	abstract char readChar(I in) throws IOException;

	abstract <V> void readCollection(I in, ODBSType value, Collection<V> values) throws IOException;

	abstract Date readDate(I in) throws IOException;

	abstract double readDouble(I in) throws IOException;

	abstract <V> V readEntity(I in, TypeEntity type, V value) throws IOException;

	abstract int readEnum(I in) throws IOException;

	abstract int readEnumCode(I in) throws IOException;

	abstract int readEnumCodeText(I in) throws IOException;

	abstract int readEnumText(I in) throws IOException;

	abstract float readFloat(I in) throws IOException;

	abstract int readInt(I in) throws IOException;

	abstract <V> void readList(I in, ODBSType value, List<V> values) throws IOException;

	abstract LocalDate readLocalDate(I in) throws IOException;

	abstract LocalDateTime readLocalDateTime(I in) throws IOException;

	abstract LocalTime readLocalTime(I in) throws IOException;

	abstract long readLong(I in) throws IOException;

	abstract <K, V> void readMap(I in, ODBSType key, ODBSType value, Map<K, V> values) throws IOException;

	abstract Object readObject(I in, TypeObject type, Object value) throws IOException;

	abstract <V> void readSet(I in, ODBSType value, Set<V> values) throws IOException;

	abstract short readShort(I in) throws IOException;

	abstract String readString(I in) throws IOException;

}