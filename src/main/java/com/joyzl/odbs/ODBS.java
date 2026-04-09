package com.joyzl.odbs;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.joyzl.EnumCode;
import com.joyzl.EnumCodeText;
import com.joyzl.EnumText;

public final class ODBS {

	/** 用户定义的用于序列化的实体类型 */
	private final Map<Class<?>, TypeEntity> CLASSES;
	private final Map<String, TypeEntity> NAMES;
	private final TypeEntity[] TYPES;
	private final byte[] SIGNATURE;

	private ODBS(Map<Class<?>, TypeEntity> classes) {
		TYPES = classes.values().toArray(new TypeEntity[classes.size()]);
		CLASSES = Map.copyOf(classes);
		SIGNATURE = sign(TYPES);

		// 建立类型名称字典
		// 基于字符串的序列化须通过名称定位类型
		TypeEntity t;
		final Map<String, TypeEntity> temp = new HashMap<>();
		for (TypeEntity type : TYPES) {
			t = temp.put(type.name(), type);
			if (t != null) {
				System.err.println("ODBS 类型名称重复" + t.name());
			}
		}
		NAMES = Map.copyOf(temp);
	}

	public final static ODBS initialize(Package... packages) {
		final String[] names = new String[packages.length];
		for (int index = 0; index < packages.length; index++) {
			names[index] = packages[index].getName();
		}
		return initialize(names);
	}

	@SuppressWarnings("unchecked")
	public final static ODBS initialize(String... packages) {
		// 扫描序列化类型（枚举和类）
		final Map<Class<?>, ODBSType> types = new TreeMap<>(new ClassComparator());
		for (String packega : packages) {
			List<Class<?>> cs = ODBSReflect.scanClass(packega);
			if (cs == null || cs.isEmpty()) {
				System.err.println("在模块或包未找到任何类：" + packega);
			} else {
				for (Class<?> clazz : cs) {
					if (ODBSReflect.canSerialize(clazz)) {
						types.put(clazz, null);
					}
				}
			}
		}

		// 建立序列化类型和索引
		int index = 0;
		TypeEntity entity;
		final Map<Class<?>, TypeEntity> entities = new TreeMap<>(new ClassComparator());
		for (Entry<Class<?>, ODBSType> entry : types.entrySet()) {
			if (entry.getKey().isEnum()) {
				if (EnumCodeText.class.isAssignableFrom(entry.getKey())) {
					entry.setValue(new TypeEnumCodeText((Class<? extends EnumCodeText>) entry.getKey()));
				} else if (EnumCode.class.isAssignableFrom(entry.getKey())) {
					entry.setValue(new TypeEnumCode((Class<? extends EnumCode>) entry.getKey()));
				} else if (EnumText.class.isAssignableFrom(entry.getKey())) {
					entry.setValue(new TypeEnumText((Class<? extends EnumText>) entry.getKey()));
				} else {
					entry.setValue(new TypeEnum((Class<? extends Enum<?>>) entry.getKey()));
				}
			} else {
				entity = new TypeEntity(entry.getKey(), index);
				entities.put(entry.getKey(), entity);
				entry.setValue(entity);
				index++;
			}
		}

		// 构建序列化方法描述
		for (TypeEntity e : entities.values()) {
			e.resolve(types);
		}

		// 返回构建的序列化描述实例
		return new ODBS(entities);
	}

	/** 计算用户定义类型和参与序列化方法的签名 */
	private byte[] sign(TypeEntity[] types) {
		try {
			final MessageDigest digest = MessageDigest.getInstance("SHA1");
			for (TypeEntity t : types) {
				digest.update(t.type().getName().getBytes());
				for (ODBSMethod m : t.methods()) {
					digest.update(m.name().getBytes());
					digest.update(m.type().type().getName().getBytes());
				}
			}
			return digest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/** 获取序列化定义的查验字符串 */
	public String checkString() {
		final StringBuilder builder = new StringBuilder();
		for (TypeEntity type : TYPES) {
			if (builder.length() > 0) {
				builder.append('\n');
			}
			builder.append(type.index());
			builder.append('\t');
			builder.append(type);
			for (ODBSMethod m : type.methods()) {
				builder.append(',');
				builder.append(m.type());
				builder.append(' ');
				builder.append(m.name());
			}
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		return "ODBS Types " + typeSize();
	}

	/** 获取序列化定义类型的签名 */
	public byte[] signature() {
		return SIGNATURE;
	}

	public int typeSize() {
		return TYPES.length;
	}

	TypeEntity get(int i) {
		return TYPES[i];
	}

	TypeEntity get(Class<?> c) {
		TypeEntity t = CLASSES.get(c);
		if (t == null) {
			c = c.getSuperclass();
			if (c != null) {
				t = CLASSES.get(c);
				if (t == null) {
					c = c.getSuperclass();
					if (c != null) {
						t = CLASSES.get(c);
						if (t == null) {
							c = c.getSuperclass();
							if (c != null) {
								t = CLASSES.get(c);
								if (t == null) {
									c = c.getSuperclass();
									if (c != null) {
										t = CLASSES.get(c);
									}
								}
							}
						}
					}
				}
			}
		}
		return t;
	}

	TypeEntity find(CharSequence chars) {
		return NAMES.get(chars.toString());
	}
}