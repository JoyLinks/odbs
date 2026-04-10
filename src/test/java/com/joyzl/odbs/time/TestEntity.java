package com.joyzl.odbs.time;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestEntity {

	private long id;
	private boolean gender;
	private String name, type, remark;
	private LocalDateTime created, updated;
	private TestEnum state;
	private long timestamp;

	private List<String> address = new ArrayList<>();
	private List<String> email = new ArrayList<>();

	public List<String> getEmail() {
		return email;
	}

	public void setEmail(List<String> value) {
		if (email != value) {
			email.clear();
			email.addAll(value);
		}
	}

	public List<String> getAddress() {
		return address;
	}

	public void setAddress(List<String> value) {
		if (address != value) {
			address.clear();
			address.addAll(value);
		}
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long value) {
		timestamp = value;
	}

	public TestEnum getState() {
		return state;
	}

	public void setState(TestEnum value) {
		state = value;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime value) {
		updated = value;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime value) {
		created = value;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String value) {
		remark = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String value) {
		type = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public boolean getGender() {
		return gender;
	}

	public void setGender(boolean value) {
		gender = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long value) {
		id = value;
	}
}