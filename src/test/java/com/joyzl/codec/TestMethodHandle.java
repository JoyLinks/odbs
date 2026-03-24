package com.joyzl.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import org.junit.jupiter.api.Test;

public class TestMethodHandle {

	@Test
	void test() throws Throwable {
		final Lookup lookup = MethodHandles.publicLookup();
		final MethodHandle get = lookup.findVirtual(TestMethodHandle.class, "getValue", MethodType.methodType(int.class));
		final MethodHandle set = lookup.findVirtual(TestMethodHandle.class, "setValue", MethodType.methodType(void.class, int.class));
		final TestMethodHandle test = new TestMethodHandle();
		set.invokeExact(test, 2);
		int result = (int) get.invokeExact(test);
		assertEquals(2, result);
	}

	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}