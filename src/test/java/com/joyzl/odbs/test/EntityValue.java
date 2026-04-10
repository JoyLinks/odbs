package com.joyzl.odbs.test;

/**
 * 测试继承实体
 * 
 * @author ZhangXi 2026年3月24日
 */
public abstract class EntityValue {

	private long id;

	public EntityValue() {
		id = System.currentTimeMillis();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}