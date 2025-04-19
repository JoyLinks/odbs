/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

import java.lang.annotation.Annotation;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.joyzl.EnumCode;

/**
 * ODBS(Object Data Binary Stream) 对象数据二进制序列化
 * 
 * <p>
 * 支持的值类型boolean,byte,char,short,int,long,float,double<br>
 * boolean默认值false,其余值类型默认值为0
 * </p>
 * <p>
 * 支持的基础类型Boolean,Byte,Character,Short,Integer,Long,Float,Double,BigDecimal,Date,LocalTime,LocalDate,LocalDateTime,String<br>
 * 基础类型默认值null
 * </p>
 * <p>
 * 支持枚举类型Enum,具有自定义枚举代码的枚举须实现{@link EnumCode}接口<br>
 * 不支持枚举数组或集合，如果需要表示多个枚举值请采用基于位的聚合式枚举
 * </p>
 * <p>
 * 支持的集合类型<br>
 * Array<br>
 * List:ArrayList,LinkedList,Stack,Vector<br>
 * Set:HashSet,LinkedHashSet,TreeSet<br>
 * Map:HashMap,Hashtable,LinkedHashMap,TreeMap<br>
 * 当无法准确确定集合类型时,默认采用ArrayList/HashSet/HashMap三种集合<br>
 * 在数组和集合中不能含有null元素,这将导致序列化失败(抛出NullPointerException),这主要基于以下两点考虑。<br>
 * 1.在绝大部分应用场景中,数组和集合中的null元素并无实际意义。<br>
 * 2.实现在数组和集合中能够传递null元素的多种序列化方案,都将导致字节增多。<br>
 * </p>
 * <p>
 * Map集合的键类型不能使用Array/List/Set/Map作为Map集合的键类型,如果使用这些类型作为Map键类型将导致序列化失败(抛出IllegalArgumentException)
 * </p>
 * <p>
 * 实体必须具有无参构造函数，反序列化过程中会自动通过无参构造函数创建对象<br>
 * 实体对象不能存在循环引用，这将导致堆栈溢出(抛出StackOverflowError)<br>
 * </p>
 * <p>
 * ODBS通过实体对象的getXXX/isXXX/setXXX方法获取和设置对象值，与JavaBean规范相似，忽略非public的方法;
 * getXXX/isXXX必须具有非Void的返回值,setXXX必须具有与getXXX/isXXX返回类型相同的1个输入参数。<br>
 * 实体对象的getXXX方法不希望被序列化时，请调整命名方式，例如:将getName()改成theName()即可。<br>
 * 方法名不能使用中文,否则可能会导致签名校验失败。<br>
 * </p>
 * 
 * @author ZhangXi
 * @date 2020年6月3日
 */
public final class ODBS {

	// 1.扫描所需的类(定义的实体对象)
	// 2.建立类索引和字段索引,可通过整数序号定位类型
	// 3.实体类支持继承类覆盖(继承覆盖)

	// 序列化(Write to stream)
	// 1.类名查找缓存的对象描述类(对象描述类用于加快序列化速度)
	// 2.通过对象描述类将实例序列化

	// 对象化(Read from stream)
	// 1.索引查找类型描述对象
	// 2.通过描述对象将数据流对象化

	private ODBSDescription[] DESCRIPTIONS;
	private ODBSEnumeration[] ENUMERATIONS;

	private final Map<Class<?>, ODBSEnumeration> ENUMS = new HashMap<>();
	private final Map<Class<?>, ODBSDescription> CLASSES = new HashMap<>();

	/** 对象描述签名，用于比对客户端和服务端实体对象是否一致 */
	private byte[] SIGNATURE;

	/** 实体对象序列化限制，单次序列化超过此限制将分段 */
	public final static int LIMIT = 256;

	/**
	 * {@link ODBS#initialize(String...)}
	 */
	private ODBS() {
		// 禁止直接实例化
	}

	/**
	 * 初始化定义用于序列化的实体类型
	 * <p>
	 * 通过Package指定实体包可提高工程代码可维护性
	 * </p>
	 * 
	 * @see #initialize(String...)
	 */
	public final static ODBS initialize(Package... packages) {
		final String[] names = new String[packages.length];
		for (int index = 0; index < packages.length; index++) {
			names[index] = packages[index].getName();
		}
		return initialize(names);
	}

	/**
	 * 初始化定义用于序列化的实体类型
	 * <p>
	 * 将在指定的多个包中查找所有的对象类型定义，扫描对象的方法和类型建立类型描述。<br>
	 * 确保指定的包中包含所有需要进行序列化的对象类型和枚举类型。<br>
	 * ODBS序列化为了保证性能，不会对类的版本和差异进行任何检查，因此序列化和反序列化的包结构及类必须完全相同。<br>
	 * 不要将不需要进行序列化的对象或与序列化完全无关的对象放入被扫描的包中。<br>
	 * 如果对象类型在本地进行了继承，并且希望实例化为本地的对象可通过{@link #override(String...)}静态方法进行覆盖<br>
	 * </p>
	 * <p>
	 * 注意：此方法须耗费较多时间建立类型描述细节，建议在系统启动时调用。
	 * </p>
	 * 
	 * @param packages 指定多个包名称 "com.joyzl.common.entities"
	 */
	public final static ODBS initialize(String... packages) {
		// 在网络传输中必须通过序列号标识类型，且服务端和客户端必须获得相同的序列号
		// 序列号按定义的类型名称进行排序获得，必须确保服务端和客户端扫描的包相同且类型定义数量和名称相同
		// 通常情况下序列化类型应定义到单独的公共包中，以便客户端和服务器获得相同的定义

		final ODBS odbs = new ODBS();

		final TreeSet<Class<?>> enums = new TreeSet<>(new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> c1, Class<?> c2) {
				// 必须指定排序以确保服务端和客户端获得相同的排列方式
				return c1.getName().compareTo(c2.getName());
			}
		});
		final TreeSet<Class<?>> classes = new TreeSet<>(new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> c1, Class<?> c2) {
				// 必须指定排序以确保服务端和客户端获得相同的排列方式
				return c1.getName().compareTo(c2.getName());
			}
		});
		for (String packega : packages) {
			List<Class<?>> cs = ODBSReflect.scanClass(packega);
			for (Class<?> clazz : cs) {
				if (ODBSReflect.canSerialize(clazz)) {
					if (clazz.isEnum()) {
						enums.add(clazz);
					} else {
						classes.add(clazz);
					}
				}
			}
		}

		// 为查找到的枚举对象建立类型索引
		int index = 0;
		odbs.ENUMERATIONS = new ODBSEnumeration[enums.size()];
		for (Class<?> clazz : enums) {
			@SuppressWarnings("unchecked")
			ODBSEnumeration enumeration = new ODBSEnumeration((Class<Enum<?>>) clazz, ODBSTypes.ENUM + index);
			odbs.ENUMS.put(clazz, odbs.ENUMERATIONS[index] = enumeration);
			index++;
		}

		// 为查找到的实体对象建立类型索引
		index = 0;
		odbs.DESCRIPTIONS = new ODBSDescription[classes.size()];
		for (Class<?> clazz : classes) {
			ODBSDescription description = new ODBSDescription(clazz, ODBSTypes.ENTITY + index);
			odbs.CLASSES.put(clazz, odbs.DESCRIPTIONS[index] = description);
			index++;
		}

		// 遍历实体对象的所有Field(基于Bean的GetXXX/setXXX/isXXX)建立参与序列化/反序列化字段类型描述
		// 必须在所有参与序列化/反序列化的枚举和实体对象索引建立完成之后才能处理字段类型描述
		// 因为字段类型可能会引用到具体的实体和枚举，需要通过索引进行标识
		// 遍历同时生成签名
		try {
			final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			for (ODBSDescription description : odbs.DESCRIPTIONS) {
				messageDigest.update(description.getName().getBytes());
				for (ODBSField field : description.getFields()) {
					field.makeType(odbs);
					messageDigest.update(field.getName().getBytes());
					messageDigest.update(field.getType().toString().getBytes());
				}
			}
			odbs.SIGNATURE = messageDigest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		return odbs;
	}

	/**
	 * 扫描需要被覆盖的对象类型
	 * 
	 * @param packages 指定多个包名称 "com.joyzl.common.entities"
	 */
	public final void override(String... packages) {
		for (String packega : packages) {
			List<Class<?>> cs = ODBSReflect.scanClass(packega);
			for (Class<?> clazz : cs) {
				if (ODBSReflect.canSerialize(clazz)) {
					ODBSDescription description = CLASSES.get(clazz.getSuperclass());
					if (description != null) {
						description.override(clazz);
						CLASSES.put(clazz, description);
					}
				}
			}
		}
	}

	/**
	 * 指定单个被继承覆盖的对象类型
	 * 
	 * @param clazz
	 */
	public final void override(Class<?> clazz) {
		ODBSDescription description = CLASSES.get(clazz.getSuperclass());
		if (description != null) {
			description.override(clazz);
			CLASSES.put(clazz, description);
		}
	}

	/**
	 * 获取实体对象签名
	 * <p>
	 * 必须调用initialize(String... packages)方法初始化之后才会生成签名
	 * 
	 * @return byte[]
	 */
	public final byte[] signature() {
		return SIGNATURE;
	}

	/**
	 * 检查是否存在指定类的描述对象
	 * 
	 * @param clazz
	 * @return true /false
	 */
	public final boolean contains(Class<?> clazz) {
		return CLASSES.containsKey(clazz);
	}

	/**
	 * 获取对象描述类
	 * 
	 * @param index
	 * @return 如果索引超出范围则返回 null / ODBSDescription
	 */
	public final ODBSDescription findDesc(int index) {
		index -= ODBSTypes.ENTITY;
		if (index < 0 || index >= DESCRIPTIONS.length) {
			return null;
		}
		return DESCRIPTIONS[index];
	}

	/**
	 * 获取对象描述类
	 * 
	 * @param name 对象完整名称,例如:"com.joyzl.odbs"
	 * @return null / ODBSDescription
	 */
	public final ODBSDescription findDesc(String name) {
		ODBSDescription d;
		for (int index = 0; index < DESCRIPTIONS.length; index++) {
			d = DESCRIPTIONS[index];
			if (name.compareTo(d.getName()) == 0) {
				return d;
			}
		}
		return null;
	}

	/**
	 * 查找实体类型描述对象
	 * 
	 * @param clazz 实体类型
	 * @return 实体类型对应的由ODBS创建的{@link ODBSDescription}描述对象
	 */
	public final ODBSDescription findDesc(Class<?> clazz) {
		if (clazz.isAnonymousClass()) {
			// 匿名类支持
			// class com.joyzl.test.odbs.Test$1.invoke();
			return CLASSES.get(clazz.getSuperclass());
		}
		ODBSDescription description = CLASSES.get(clazz);
		while (description == null && clazz != null && clazz != Object.class) {
			// 支持被继承的实体，扩展的字段不会被序列化
			description = CLASSES.get(clazz = clazz.getSuperclass());
		}
		return description;
	}

	/**
	 * 查找实体对象类型索引
	 * 
	 * @param clazz 实体类型
	 * @return 0~n 由ODBS编排的类型索引
	 */
	public final int findDescType(Class<?> clazz) {
		final ODBSDescription description = findDesc(clazz);
		return description == null ? ODBSTypes.UNKNOW : description.getIndex();
	}

	/**
	 * 查找枚举描述对象
	 * 
	 * @param index 0~n 由ODBS编排的类型索引
	 * @return 枚举类型对应的由ODBS创建的{@link ODBSEnumeration}描述对象
	 */
	public final ODBSEnumeration findEnum(int index) {
		index -= ODBSTypes.ENUM;
		if (index < 0 || index >= ENUMERATIONS.length) {
			return null;
		}
		return ENUMERATIONS[index];
	}

	/**
	 * 查找枚举描述对象
	 * 
	 * @param name 枚举完整名称
	 * @return ODBSEnumeration / null
	 */
	public final ODBSEnumeration findEnum(String name) {
		for (int index = 0; index < ENUMERATIONS.length; index++) {
			if (name.compareTo(ENUMERATIONS[index].getName()) == 0) {
				return ENUMERATIONS[index];
			}
		}
		return null;
	}

	/**
	 * 查找枚举描述对象
	 * 
	 * @param clazz 枚举类型
	 * @return 枚举类型对应的由ODBS创建的{@link ODBSEnumeration}描述对象
	 */
	public final ODBSEnumeration findEnum(Class<?> clazz) {
		return ENUMS.get(clazz);
	}

	/**
	 * 查找枚举类型索引
	 * 
	 * @param clazz 枚举类
	 * @return 0~n 由ODBS编排的类型索引
	 */
	public final int findEnumType(Class<?> clazz) {
		final ODBSEnumeration enumeration = ENUMS.get(clazz);
		return enumeration == null ? ODBSTypes.UNKNOW : enumeration.getIndex();
	}

	/**
	 * 获取ODBS创建的类描述结构
	 */
	public final String checkString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ENUMERATIONS");
		for (ODBSEnumeration enumeration : ENUMERATIONS) {
			builder.append('\n');
			builder.append(enumeration);
		}
		builder.append("\nDESCRIPTIONS");
		for (ODBSDescription description : DESCRIPTIONS) {
			builder.append('\n');
			builder.append(description);
		}
		return builder.toString();
	}

	/**
	 * 查找具有指定注解的对象
	 * 
	 * @param a Annotation
	 * @return Map<Class<?>, T>
	 */
	public final <T extends Annotation> Map<Class<?>, T> searchDesc(Class<T> a) {
		Map<Class<?>, T> map = new TreeMap<>(new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> c1, Class<?> c2) {
				return c1.getName().compareTo(c2.getName());
			}
		});
		for (int index = 0; index < DESCRIPTIONS.length; index++) {
			ODBSDescription d = DESCRIPTIONS[index];
			T an = ODBSReflect.findAnnotation(d.getSourceClass(), a);
			if (an == null) {
				continue;
			} else {
				map.put(d.getSourceClass(), an);
			}
		}
		return map;
	}

	/**
	 * 查找具有指定名称的描述对象,注意此方法获取的结果并不绝对严谨
	 * 
	 * @param name 名称
	 * @return ODBSDescription
	 */
	public final ODBSDescription searchDesc(String name) {
		ODBSDescription description;
		for (int index = 0; index < DESCRIPTIONS.length; index++) {
			description = DESCRIPTIONS[index];
			if (description.getName().endsWith(name)) {
				return description;
			}
		}
		return null;
	}

	/**
	 * 获取所有枚举描述
	 * <p>
	 * 修改返回的集合不会影响序列化工作
	 * </p>
	 * 
	 * @return List<ODBSEnumeration>
	 */
	public final List<ODBSEnumeration> enumerations() {
		return Arrays.asList(ENUMERATIONS);
	}

	/**
	 * 获取所有对象描述
	 * <p>
	 * 修改返回的集合不会影响序列化工作
	 * </p>
	 * 
	 * @return List<ODBSDescription>
	 */
	public final List<ODBSDescription> descriptions() {
		return Arrays.asList(DESCRIPTIONS);
	}
}
