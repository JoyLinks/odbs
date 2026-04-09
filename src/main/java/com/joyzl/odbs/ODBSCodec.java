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

import com.joyzl.EnumCode;
import com.joyzl.EnumCodeText;
import com.joyzl.EnumText;

abstract class ODBSCodec<O, I> {

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

	abstract void writeArray(O out, ODBSType value, Object values) throws IllegalArgumentException, IOException;

	abstract void writeBigDecimal(O out, BigDecimal value) throws IOException;

	abstract void writeBigInteger(O out, BigInteger value) throws IOException;

	abstract void writeBoolean(O out, boolean value) throws IOException;

	abstract void writeBoolean(O out, Boolean value) throws IOException;

	abstract void writeByte(O out, byte value) throws IOException;

	abstract void writeChar(O out, char value) throws IOException;

	abstract void writeCollection(O out, ODBSType value, Collection<?> values) throws IOException;

	abstract void writeDate(O out, Date value) throws IOException;

	abstract void writeDouble(O out, double value) throws IOException;

	abstract void writeEntity(O out, TypeEntity type, Object value) throws IOException;

	abstract void writeEnum(O out, Enum<?> value) throws IOException;

	abstract void writeEnumCode(O out, EnumCode value) throws IOException;

	abstract void writeEnumCodeText(O out, EnumCodeText value) throws IOException;

	abstract void writeEnumText(O out, EnumText value) throws IOException;

	abstract void writeField(O out, ODBSMethod method) throws IOException;

	abstract void writeFloat(O out, float value) throws IOException;

	abstract void writeInt(O out, int value) throws IOException;

	abstract void writeList(O out, ODBSType value, List<?> values) throws IOException;

	abstract void writeLocalDateTime(O out, LocalDateTime value) throws IOException;

	abstract void writeLocaleDate(O out, LocalDate value) throws IOException;

	abstract void writeLocalTime(O out, LocalTime value) throws IOException;

	abstract void writeLong(O out, long value) throws IOException;

	abstract void writeMap(O out, ODBSType key, ODBSType value, Map<?, ?> values) throws IOException;

	abstract void writeObject(O out, TypeObject type, Object value) throws IOException;

	abstract void writeSet(O out, ODBSType value, Set<?> values) throws IOException;

	abstract void writeShort(O out, short value) throws IOException;

	abstract void writeString(O out, String value) throws IOException;
}