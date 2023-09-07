/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

/**
 * 类型
 * 
 * @author ZhangXi
 * @date 2020年8月6日
 */
public final class ODBSType {

	private final int key;
	private final int value;
	private final ODBSType further;

	private ODBSType(int k, int v, ODBSType f) {
		key = k;
		value = v;
		further = f;
	}

	public int key() {
		return key;
	}

	public int value() {
		return value;
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
	public final static ODBSType make(ODBS odbs, Class<?> main, Class<?>... classes) {
		int m = ODBSTypes.getType(main);

		if (ODBSTypes.isValue(m)) {
			return new ODBSType(0, m, null);
		} else if (ODBSTypes.isBase(m)) {
			return new ODBSType(0, m, null);
		} else if (ODBSTypes.isEnum(m)) {
			return new ODBSType(0, odbs.findEnumType(main), null);
		} else if (ODBSTypes.isEntity(m)) {
			return new ODBSType(0, odbs.findDescType(main), null);
		} else if (ODBSTypes.isArray(m)) {
			return new ODBSType(0, m, make(odbs, null, main.getComponentType()));
		} else if (ODBSTypes.isList(m)) {
			return new ODBSType(0, m, make(odbs, null, classes[0]));
		} else if (ODBSTypes.isSet(m)) {
			return new ODBSType(0, m, make(odbs, null, classes[0]));
		} else if (ODBSTypes.isMap(m)) {
			return new ODBSType(0, m, make(odbs, classes[0], classes[1]));
		} else if (ODBSTypes.isAny(m)) {
			return new ODBSType(0, m, null);
		} else {
			throw new IllegalStateException("值类型无效，不支持的类型:" + m);
		}
	}

	/**
	 * 根据键和值类型创建ODBSType对象实例
	 * 
	 * @param key Map键
	 * @param value Map值
	 * @return
	 */
	public final static ODBSType make(ODBS odbs, Class<?> key, Class<?> value) {
		int k = key == null ? ODBSTypes.UNKNOW : ODBSTypes.getType(key);
		int v = value == null ? ODBSTypes.UNKNOW : ODBSTypes.getType(value);

		if (k == ODBSTypes.UNKNOW) {
			if (ODBSTypes.isValue(v)) {
				return new ODBSType(k, v, null);
			} else if (ODBSTypes.isBase(v)) {
				return new ODBSType(k, v, null);
			} else if (ODBSTypes.isEnum(v)) {
				return new ODBSType(k, odbs.findEnumType(value), null);
			} else if (ODBSTypes.isEntity(v)) {
				return new ODBSType(k, odbs.findDescType(value), null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(k, v, make(odbs, null, value.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				return new ODBSType(k, v, make(odbs, null, ODBSReflect.findListGeneric(value)));
			} else if (ODBSTypes.isSet(v)) {
				return new ODBSType(k, v, make(odbs, null, ODBSReflect.findSetGeneric(value)));
			} else if (ODBSTypes.isMap(v)) {
				Class<?>[] classes = ODBSReflect.findMapGeneric(value);
				return new ODBSType(k, v, make(odbs, classes[0], classes[1]));
			} else {
				throw new IllegalStateException("值类型无效，不支持的类型:" + v);
			}
		} else if (ODBSTypes.isBase(k)) {
			if (ODBSTypes.isBase(v)) {
				return new ODBSType(k, v, null);
			} else if (ODBSTypes.isEnum(v)) {
				return new ODBSType(k, odbs.findEnumType(value), null);
			} else if (ODBSTypes.isEntity(v)) {
				return new ODBSType(k, odbs.findDescType(value), null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(k, v, make(odbs, key, value.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				return new ODBSType(k, v, make(odbs, key, ODBSReflect.findListGeneric(value)));
			} else if (ODBSTypes.isSet(v)) {
				return new ODBSType(k, v, make(odbs, key, ODBSReflect.findSetGeneric(value)));
			} else if (ODBSTypes.isMap(v)) {
				Class<?>[] classes = ODBSReflect.findMapGeneric(value);
				return new ODBSType(k, v, make(odbs, classes[0], classes[1]));
			} else {
				throw new IllegalStateException("值类型无效，不支持的类型:" + v);
			}
		} else if (ODBSTypes.isEnum(k)) {
			if (ODBSTypes.isBase(v)) {
				return new ODBSType(odbs.findEnumType(key), v, null);
			} else if (ODBSTypes.isEnum(v)) {
				return new ODBSType(odbs.findEnumType(key), odbs.findEnumType(value), null);
			} else if (ODBSTypes.isEntity(v)) {
				return new ODBSType(odbs.findEnumType(key), odbs.findDescType(value), null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(odbs.findEnumType(key), v, make(odbs, null, value.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				return new ODBSType(odbs.findEnumType(key), v, make(odbs, null, ODBSReflect.findListGeneric(value)));
			} else if (ODBSTypes.isSet(v)) {
				return new ODBSType(odbs.findEnumType(key), v, make(odbs, null, ODBSReflect.findSetGeneric(value)));
			} else if (ODBSTypes.isMap(v)) {
				Class<?>[] classes = ODBSReflect.findMapGeneric(value);
				return new ODBSType(odbs.findEnumType(key), v, make(odbs, classes[0], classes[1]));
			} else {
				throw new IllegalStateException("值类型无效，不支持的类型:" + v);
			}
		} else if (ODBSTypes.isEntity(k)) {
			if (ODBSTypes.isBase(v)) {
				return new ODBSType(odbs.findDescType(key), v, null);
			} else if (ODBSTypes.isEnum(v)) {
				return new ODBSType(odbs.findDescType(key), odbs.findEnumType(value), null);
			} else if (ODBSTypes.isEntity(v)) {
				return new ODBSType(odbs.findDescType(key), odbs.findDescType(value), null);
			} else if (ODBSTypes.isArray(v)) {
				return new ODBSType(odbs.findDescType(key), v, make(odbs, null, value.getComponentType()));
			} else if (ODBSTypes.isList(v)) {
				return new ODBSType(odbs.findDescType(key), v, make(odbs, null, ODBSReflect.findListGeneric(value)));
			} else if (ODBSTypes.isSet(v)) {
				return new ODBSType(odbs.findDescType(key), v, make(odbs, null, ODBSReflect.findSetGeneric(value)));
			} else if (ODBSTypes.isMap(v)) {
				Class<?>[] classes = ODBSReflect.findMapGeneric(value);
				return new ODBSType(odbs.findDescType(key), v, make(odbs, classes[0], classes[1]));
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
}
