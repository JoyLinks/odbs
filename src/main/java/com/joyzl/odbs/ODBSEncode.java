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

abstract interface ODBSEncode<O> {

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