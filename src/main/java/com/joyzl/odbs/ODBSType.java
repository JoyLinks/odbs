/*
 * Copyright © 2017-2025 重庆骄智科技有限公司.
 * 本软件根据 Apache License 2.0 开源，详见 LICENSE 文件。
 */
package com.joyzl.odbs;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 类型
 * 
 * @author ZhangXi
 * @date 2020年8月6日
 */
public final class ODBSType {

	/** 键类型,Map集合时才存在 */
	private final int key;
	/** 值类型,字段主类型 */
	private final int value;
	/** 值类型为数组或集合时,此为元素类型 */
	private final ODBSType further;
	/** 原始类型 */
	private final Class<?> keyClass, valueClass;

	private ODBSType(int k, Class<?> kc, int v, Class<?> vc, ODBSType f) {
		key = k;
		keyClass = kc;
		value = v;
		valueClass = vc;
		further = f;
	}

	public int key() {
		return key;
	}

	public Class<?> keyClass() {
		return keyClass;
	}

	public int value() {
		return value;
	}

	public Class<?> valueClass() {
		return valueClass;
	}

	public ODBSType further() {
		return further;
	}

	/**
	 * 根据主类和子类创建ODBSType对象实例
	 * 
	 * @param main
	 * @param classes 子类型
	 * @return
	 */
	@Deprecated
	public final static ODBSType make(ODBS odbs, Class<?> main, Class<?>... classes) {
		int m = ODBSTypes.getType(main);

		if (ODBSTypes.isValue(m)) {
			return new ODBSType(0, null, m, main, null);
		} else if (ODBSTypes.isBase(m)) {
			return new ODBSType(0, null, m, main, null);
		} else if (ODBSTypes.isEnum(m)) {
			return new ODBSType(0, null, odbs.findEnumType(main), main, null);
		} else if (ODBSTypes.isEntity(m)) {
			return new ODBSType(0, null, odbs.findDescType(main), main, null);
		} else if (ODBSTypes.isArray(m)) {
			return new ODBSType(0, null, m, main, make(odbs, null, main.getComponentType()));
		} else if (ODBSTypes.isList(m)) {
			if (classes != null && classes.length > 0) {
				return new ODBSType(0, null, m, main, make(odbs, null, classes[0]));
			} else {
				throw new IllegalStateException("未能识别List的子类型:" + main);
			}
		} else if (ODBSTypes.isSet(m)) {
			if (classes != null && classes.length > 0) {
				return new ODBSType(0, null, m, main, make(odbs, null, classes[0]));
			} else {
				throw new IllegalStateException("未能识别Set的子类型:" + main);
			}
		} else if (ODBSTypes.isMap(m)) {
			if (classes != null && classes.length > 1) {
				return new ODBSType(0, null, m, main, make(odbs, classes[0], classes[1]));
			} else {
				throw new IllegalStateException("未能识别Map的子类型:" + main);
			}
		} else if (ODBSTypes.isAny(m)) {
			return new ODBSType(0, null, m, main, null);
		} else {
			throw new IllegalStateException("类型无效，不支持的类型:" + main);
		}
	}

	/**
	 * 根据键和值类型创建ODBSType对象实例
	 * 
	 * @param key Map键
	 * @param value Map值
	 * @return
	 */
	@Deprecated
	final static ODBSType make(ODBS odbs, Class<?> key, Class<?> value) {
		int k = key == null ? ODBSTypes.UNKNOW : ODBSTypes.getType(key);
		int v = value == null ? ODBSTypes.UNKNOW : ODBSTypes.getType(value);

		if (k == ODBSTypes.UNKNOW) {
			if (ODBSTypes.isValue(v)) {
				return new ODBSType(k, key, v, value, null);
			} else if (ODBSTypes.isBase(v)) {
				return new ODBSType(k, key, v, value, null);
			} else if (ODBSTypes.isEnum(v)) {
				return new ODBSType(k, key, odbs.findEnumType(value), value, null);
			} else if (ODBSTypes.isEntity(v)) {
				return new ODBSType(k, key, odbs.findDescType(value), value, null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(k, key, v, value, make(odbs, null, value.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				return new ODBSType(k, key, v, value, make(odbs, null, ODBSReflect.findListGeneric(value)));
			} else if (ODBSTypes.isSet(v)) {
				return new ODBSType(k, key, v, value, make(odbs, null, ODBSReflect.findSetGeneric(value)));
			} else if (ODBSTypes.isMap(v)) {
				Class<?>[] classes = ODBSReflect.findMapGeneric(value);
				return new ODBSType(k, key, v, value, make(odbs, classes[0], classes[1]));
			} else {
				throw new IllegalStateException("值类型无效，不支持的类型:" + v);
			}
		} else if (ODBSTypes.isBase(k)) {
			if (ODBSTypes.isBase(v)) {
				return new ODBSType(k, key, v, value, null);
			} else if (ODBSTypes.isEnum(v)) {
				return new ODBSType(k, key, odbs.findEnumType(value), value, null);
			} else if (ODBSTypes.isEntity(v)) {
				return new ODBSType(k, key, odbs.findDescType(value), value, null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(k, key, v, value, make(odbs, key, value.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				return new ODBSType(k, key, v, value, make(odbs, key, ODBSReflect.findListGeneric(value)));
			} else if (ODBSTypes.isSet(v)) {
				return new ODBSType(k, key, v, value, make(odbs, key, ODBSReflect.findSetGeneric(value)));
			} else if (ODBSTypes.isMap(v)) {
				Class<?>[] classes = ODBSReflect.findMapGeneric(value);
				return new ODBSType(k, key, v, value, make(odbs, classes[0], classes[1]));
			} else {
				throw new IllegalStateException("值类型无效，不支持的类型:" + v);
			}
		} else if (ODBSTypes.isEnum(k)) {
			if (ODBSTypes.isBase(v)) {
				return new ODBSType(odbs.findEnumType(key), key, v, value, null);
			} else if (ODBSTypes.isEnum(v)) {
				return new ODBSType(odbs.findEnumType(key), key, odbs.findEnumType(value), value, null);
			} else if (ODBSTypes.isEntity(v)) {
				return new ODBSType(odbs.findEnumType(key), key, odbs.findDescType(value), value, null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(odbs.findEnumType(key), key, v, value, make(odbs, null, value.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				return new ODBSType(odbs.findEnumType(key), key, v, value, make(odbs, null, ODBSReflect.findListGeneric(value)));
			} else if (ODBSTypes.isSet(v)) {
				return new ODBSType(odbs.findEnumType(key), key, v, value, make(odbs, null, ODBSReflect.findSetGeneric(value)));
			} else if (ODBSTypes.isMap(v)) {
				Class<?>[] classes = ODBSReflect.findMapGeneric(value);
				return new ODBSType(odbs.findEnumType(key), key, v, value, make(odbs, classes[0], classes[1]));
			} else {
				throw new IllegalStateException("值类型无效，不支持的类型:" + v);
			}
		} else if (ODBSTypes.isEntity(k)) {
			if (ODBSTypes.isBase(v)) {
				return new ODBSType(odbs.findDescType(key), key, v, value, null);
			} else if (ODBSTypes.isEnum(v)) {
				return new ODBSType(odbs.findDescType(key), key, odbs.findEnumType(value), value, null);
			} else if (ODBSTypes.isEntity(v)) {
				return new ODBSType(odbs.findDescType(key), key, odbs.findDescType(value), value, null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(odbs.findDescType(key), key, v, value, make(odbs, null, value.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				return new ODBSType(odbs.findDescType(key), key, v, value, make(odbs, null, ODBSReflect.findListGeneric(value)));
			} else if (ODBSTypes.isSet(v)) {
				return new ODBSType(odbs.findDescType(key), key, v, value, make(odbs, null, ODBSReflect.findSetGeneric(value)));
			} else if (ODBSTypes.isMap(v)) {
				Class<?>[] classes = ODBSReflect.findMapGeneric(value);
				return new ODBSType(odbs.findDescType(key), key, v, value, make(odbs, classes[0], classes[1]));
			} else {
				throw new IllegalStateException("值类型无效，不支持的类型:" + v);
			}
		} else {
			throw new IllegalStateException("键类型无效，不支持的类型:" + k);
		}
	}

	@Override
	public String toString() {
		if (key > 0 && value <= 0) {
			if (further == null) {
				return ODBSTypes.getName(key);
			} else {
				return ODBSTypes.getName(key) + "<" + further + ">";
			}
		}
		if (key > 0 && value > 0) {
			if (further == null) {
				return ODBSTypes.getName(key) + "," + ODBSTypes.getName(value);
			} else {
				return ODBSTypes.getName(key) + "," + ODBSTypes.getName(value) + "<" + further + ">";
			}
		}
		if (key <= 0 && value > 0) {
			if (further == null) {
				return ODBSTypes.getName(value);
			} else {
				return ODBSTypes.getName(value) + "<" + further + ">";
			}
		}
		return "X";
	}

	/** 类型匹配 */
	public final static ODBSType make(ODBS odbs, Type key, Type value) {
		int k = 0;
		Class<?> kc = null;
		if (key != null) {
			if (key instanceof Class<?> c) {
				k = ODBSTypes.getType(c);
				kc = c;
				if (ODBSTypes.isValue(k)) {
					// 值类型无须进一步匹配
				} else if (ODBSTypes.isBase(k)) {
					// 基本类型无须进一步匹配
				} else if (ODBSTypes.isEnum(k)) {
					// 获取具体枚举
					k = odbs.findEnumType(c);
				} else if (ODBSTypes.isEntity(k)) {
					// 获取具体实体
					k = odbs.findDescType(c);
				} else if (ODBSTypes.isArray(k)) {
					// 不支持数组为键
					throw new IllegalStateException("无效键类型:" + c);
				} else if (ODBSTypes.isList(k)) {
					// 不支持集合为键
					throw new IllegalStateException("无效键类型:" + c);
				} else if (ODBSTypes.isSet(k)) {
					// 不支持集合为键
					throw new IllegalStateException("无效键类型:" + c);
				} else if (ODBSTypes.isMap(k)) {
					// 不支持集合为键
					throw new IllegalStateException("无效键类型:" + c);
				} else {
					// 不支持未明确类型的键
					throw new IllegalStateException("无效键类型:" + c);
				}
			} else if (key instanceof ParameterizedType) {
				// 不支持泛型键
				throw new IllegalStateException("无效键类型:" + key);
			} else {
				throw new IllegalStateException("无效键类型:" + key);
			}
		}

		int v;
		Class<?> vc = null;
		if (value instanceof Class<?> c) {
			v = ODBSTypes.getType(c);
			vc = c;
			if (ODBSTypes.isValue(v)) {
				return new ODBSType(k, kc, v, c, null);
			} else if (ODBSTypes.isBase(v)) {
				return new ODBSType(k, kc, v, c, null);
			} else if (ODBSTypes.isEnum(v)) {
				v = odbs.findEnumType(c);
				return new ODBSType(k, kc, v, c, null);
			} else if (ODBSTypes.isEntity(v)) {
				v = odbs.findDescType(c);
				return new ODBSType(k, kc, v, c, null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(k, kc, v, c, make(odbs, null, c.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				throw new IllegalStateException("无法继续匹配泛型:" + c);
			} else if (ODBSTypes.isSet(v)) {
				throw new IllegalStateException("无法继续匹配泛型:" + c);
			} else if (ODBSTypes.isMap(v)) {
				throw new IllegalStateException("无法继续匹配泛型:" + c);
			} else if (ODBSTypes.isAny(v)) {
				return new ODBSType(0, kc, v, c, null);
			} else {
				throw new IllegalStateException("无效类型:" + c);
			}
		}
		if (value instanceof ParameterizedType p) {
			final Class<?> c = (Class<?>) p.getRawType();
			v = ODBSTypes.getType(c);
			if (ODBSTypes.isValue(v)) {
				return new ODBSType(k, kc, v, vc, null);
			} else if (ODBSTypes.isBase(v)) {
				return new ODBSType(k, kc, v, vc, null);
			} else if (ODBSTypes.isEnum(v)) {
				v = odbs.findEnumType(c);
				return new ODBSType(k, kc, v, vc, null);
			} else if (ODBSTypes.isEntity(v)) {
				v = odbs.findDescType(c);
				return new ODBSType(k, kc, v, vc, null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(k, kc, v, vc, make(odbs, null, c.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				return new ODBSType(k, kc, v, vc, make(odbs, null, p.getActualTypeArguments()[0]));
			} else if (ODBSTypes.isSet(v)) {
				return new ODBSType(k, kc, v, vc, make(odbs, null, p.getActualTypeArguments()[0]));
			} else if (ODBSTypes.isMap(v)) {
				return new ODBSType(k, kc, v, vc, make(odbs, p.getActualTypeArguments()[0], p.getActualTypeArguments()[1]));
			} else if (ODBSTypes.isAny(v)) {
				return new ODBSType(k, kc, v, vc, null);
			} else {
				throw new IllegalStateException("无效类型:" + p);
			}
		}
		throw new IllegalStateException("无效类型:" + value);
	}
}