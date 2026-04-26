package com.joyzl.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import org.junit.jupiter.api.Test;

public class TestMethodHandle {

	@Test
	void test1() throws Throwable {
		final Lookup lookup = MethodHandles.publicLookup();
		final MethodHandle get = lookup.findVirtual(TestMethodHandle.class, "getValue", MethodType.methodType(int.class));
		final MethodHandle set = lookup.findVirtual(TestMethodHandle.class, "setValue", MethodType.methodType(void.class, int.class));
		final TestMethodHandle test = new TestMethodHandle();
		set.invokeExact(test, 2);
		int result = (int) get.invokeExact(test);
		assertEquals(2, result);
	}

	// @Test
	void test2() throws Throwable {

		final Lookup lookup = MethodHandles.publicLookup();
		final MethodHandle setRaw = lookup.findVirtual(TestMethodHandle.class, "setStrings", MethodType.methodType(void.class, String[].class));
		final MethodHandle setObj = setRaw.asType(MethodType.methodType(void.class, Object.class, Object.class));

		final TestMethodHandle test = new TestMethodHandle();
		final String[] values = new String[0];

		setRaw.invokeExact(test, values);
		setObj.invokeExact((Object) test, (Object[]) values);

		final MethodHandle vc = setRaw.asVarargsCollector(String[].class);
		vc.invoke(test, (Object[]) values);

		assertEquals(values, test.getStrings());
	}

	private int value;
	private String[] strings;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String[] getStrings() {
		return strings;
	}

	public void setStrings(String... strings) {
		this.strings = strings;
	}
}