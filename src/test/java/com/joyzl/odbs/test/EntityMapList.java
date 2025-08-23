package com.joyzl.odbs.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityMapList {

	private Map<String, List<LocalDate>> values;

	public Map<String, List<LocalDate>> getValues() {
		return values;
	}

	public void setValues(Map<String, List<LocalDate>> vs) {
		values = vs;
	}

	static EntityMapList createNullValue() {
		final EntityMapList entity = new EntityMapList();
		entity.setValues(null);
		return entity;
	}

	static EntityMapList createEmptyValue() {
		final EntityMapList entity = new EntityMapList();
		entity.setValues(new HashMap<>());
		return entity;
	}

	static EntityMapList createNormalValue() {
		final EntityMapList entity = new EntityMapList();
		entity.setValues(new HashMap<>());
		for (int i = 0; i < 10; i++) {
			List<LocalDate> items = new ArrayList<>();
			items.add(LocalDate.MAX);
			items.add(LocalDate.MIN);
			items.add(LocalDate.EPOCH);
			entity.getValues().put("KEY:" + i, items);
		}
		return entity;
	}

	static void assertEntity(EntityMapList a, EntityMapList b) {
		assertEquals(a.getValues(), b.getValues());
	}
}