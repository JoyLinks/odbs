/*-
 * www.joyzl.net
 * 中翌智联（重庆）科技有限公司
 * Copyright © JOY-Links Company. All rights reserved.
 */
package com.joyzl.odbs;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 对象反射相关辅助方法
 * 
 * @author ZhangXi
 * @date 2020年6月3日
 */
public final class ODBSReflect {

	private ODBSReflect() {
		// 禁止实例化
	}

	/**
	 * 当前是否运行于模块模式(--m/--module启动模式)
	 */
	public static boolean isModule() {
		return ODBSReflect.class.getModule().isNamed();
	}

	/**
	 * 检查类是否可用
	 */
	public final static boolean canUsable(Class<?> clazz) {
		if (clazz.isHidden()) {
			// 隐藏类
			return false;
		}
		if (clazz.isAnnotation()) {
			// 注解类
			return false;
		}
		if (clazz.isInterface()) {
			// 接口
			return false;
		}
		if (clazz.isMemberClass()) {
			// 成员类(类中定义的类)
			return true;
		}
		if (clazz.isLocalClass()) {
			// 局部类(方法中定义的类)
			return false;
		}
		if (clazz.isPrimitive()) {
			// 基本类型 boolean, byte, char, short, int, long, float, double
			return false;
		}
		if (clazz.isSynthetic()) {
			// 编译器引入类
			return false;
		}
		if (clazz.isAnonymousClass()) {
			// 匿名类(缺失固定名称)
			return false;
		}
		// clazz.isSealed();
		return true;
	}

	/**
	 * 检查指定类是否可访问并具有构造函数用于实例化
	 */
	public final static boolean canInstance(Class<?> clazz, Class<?>... argments) {
		if (Modifier.isAbstract(clazz.getModifiers())) {
			return false;
		}
		if (Modifier.isInterface(clazz.getModifiers())) {
			return false;
		}
		if (Modifier.isNative(clazz.getModifiers())) {
			return false;
		}
		if (Modifier.isPrivate(clazz.getModifiers())) {
			return false;
		}
		if (Modifier.isProtected(clazz.getModifiers())) {
			return false;
		}
		if (Modifier.isStatic(clazz.getModifiers())) {
			return false;
		}
		if (Modifier.isSynchronized(clazz.getModifiers())) {
			return false;
		}
		if (Modifier.isTransient(clazz.getModifiers())) {
			return false;
		}
		try {
			// 可访问的指定参构造函数
			final Constructor<?> constructor = clazz.getConstructor(argments);
			return Modifier.isPublic(constructor.getModifiers());
		} catch (NoSuchMethodException | SecurityException e) {
			return false;
		}
	}

	/**
	 * 检查类型是否符合ODBS序列化要求
	 * 
	 * @see #canUsable(Class)
	 * @see #canInstance(Class, Class...)
	 */
	public final static boolean canSerialize(Class<?> clazz) {
		if (canUsable(clazz)) {
			if (clazz.isEnum()) {
				return Modifier.isPublic(clazz.getModifiers());
			}
			return canInstance(clazz);
		}
		return false;
	}

	/**
	 * 检查方法是否符合ODBS序列化要求
	 */
	public final static boolean canSerialize(Method method) {
		if (method.isBridge()) {
			// 桥接方法
			return false;
		}
		if (method.isDefault()) {
			// 默认方法
			return false;
		}
		if (method.isSynthetic()) {
			// 编译器引入方法
			return false;
		}
		if (method.isVarArgs()) {
			// 可变参数
			return false;
		}

		if (Modifier.isPublic(method.getModifiers())) {
			if (method.getParameterCount() > 1) {
				// 参数过多
				return false;
			}

			if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
				return method.getParameterCount() == 0;
			} else if (method.getName().startsWith("set")) {
				return method.getParameterCount() == 1;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 判断类是否实现了指定的接口
	 * 
	 * @param clazz
	 * @param _interface
	 * @return true/false
	 */
	public final static boolean isImplemented(Class<?> clazz, Class<?> _interface) {
		if (clazz == null || clazz.equals(Object.class)) {
			return false;
		}
		if (_interface.isAssignableFrom(clazz)) {
			// 1 FaultType > EnumCodeText > EnumCode
			return true;
		}

		// 20200919 isAssignableFrom 已具备层级继承识别
		// Class<?>[] interfaces = clazz.getInterfaces();
		// for (int index = 0; index < interfaces.length; index++) {
		// if (_interface.equals(interfaces[index])) {
		// return true;
		// }
		// if (isImplemented(interfaces[index], _interface)) {
		// return true;
		// }
		// }
		return isImplemented(clazz.getSuperclass(), _interface);
	}

	/**
	 * 查找指定类型的指定注解，将自动递归超类
	 * 
	 * @param o 实体实例
	 * @param clazz 注解类型
	 * 
	 * @param <T> 注解类型
	 * @return 注解实例
	 */
	public final static <T extends Annotation> T findAnnotation(Class<?> o, Class<T> clazz) {
		if (o != null) {
			T a = o.getAnnotation(clazz);
			if (a == null) {
				return findAnnotation(o.getSuperclass(), clazz);
			}
			return a;
		}
		return null;
	}

	/**
	 * 获取泛型类型,例如List<T>中的T的Class实例
	 * 
	 * @return null / Class<?>[]
	 */
	public final static Class<?>[] findGeneric(Method method) {
		Type returnType = method.getGenericReturnType();
		if (returnType instanceof ParameterizedType) {
			ParameterizedType param = (ParameterizedType) returnType;
			Type[] actualTypes = param.getActualTypeArguments();
			Class<?>[] classes = new Class<?>[actualTypes.length];
			int index = 0;
			for (Type type : actualTypes) {
				if (type instanceof Class) {
					classes[index++] = (Class<?>) type;
				}
			}
			return classes;
		}
		return null;
	}

	/**
	 * 获取泛型类型
	 * <p>
	 * 必须在继承情况下指明了范型类型才能获取到具体类型。<br>
	 * 例如：
	 * 
	 * <pre>
	 * List<String> texts = new ArrayList<>();
	 * </pre>
	 * 
	 * 无法通过 texts.getClass()获取到具体的范型类型；<br>
	 * 
	 * <pre>
	 * List<String> texts = new ArrayList<String>() {
	 * };
	 * </pre>
	 * </p>
	 * 则可以通过texts.getClass()获取到具体的范型类型
	 * </p>
	 * 
	 * @param clazz
	 * @return null / Class<?>[]
	 */
	public final static Class<?>[] findGeneric(Class<?> clazz) {
		Type type = clazz.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			ParameterizedType p = (ParameterizedType) type;
			Type[] actuals = p.getActualTypeArguments();
			Class<?>[] classes = new Class<?>[actuals.length];
			for (int i = 0; i < actuals.length; i++) {
				type = actuals[i];
				if (type instanceof Class) {
					classes[i] = (Class<?>) type;
				}
			}
			return classes;
		}

		Type[] types = clazz.getGenericInterfaces();
		for (int index = 0; index < types.length; index++) {
			if (types[index] instanceof ParameterizedType) {
				ParameterizedType p = (ParameterizedType) types[index];
				Type[] actuals = p.getActualTypeArguments();
				Class<?>[] classes = new Class<?>[actuals.length];
				for (int i = 0; i < actuals.length; i++) {
					type = actuals[i];
					if (type instanceof Class) {
						classes[i] = (Class<?>) type;
					}
				}
				return classes;
			}
		}
		return null;
	}

	/**
	 * 查找List<T>集合的范型类型
	 * 
	 * @param clazz
	 * @return 范型类型 / 抛出异常
	 */
	public final static Class<?> findListGeneric(Class<?> clazz) {
		Class<?>[] classes = ODBSReflect.findGeneric(clazz);
		if (classes != null && classes.length == 1) {
			return classes[0];
		} else {
			throw new IllegalStateException("List<T>集合范型类型无效:" + clazz);
		}
	}

	/**
	 * 查找Set<T>集合的范型类型
	 * 
	 * @param clazz
	 * @return 范型类型 / 抛出异常
	 */
	public final static Class<?> findSetGeneric(Class<?> clazz) {
		Class<?>[] classes = ODBSReflect.findGeneric(clazz);
		if (classes != null && classes.length == 1) {
			return classes[0];
		} else {
			throw new IllegalStateException("Set<T>集合范型类型无效:" + clazz);
		}
	}

	/**
	 * 查找Map<K,V>集合的范型类型
	 * 
	 * @param clazz
	 * @return 范型类型,数组0为键,1为值 / 抛出异常
	 */
	public final static Class<?>[] findMapGeneric(Class<?> clazz) {
		Class<?>[] classes = ODBSReflect.findGeneric(clazz);
		if (classes != null && classes.length == 2) {
			return classes;
		} else {
			throw new IllegalStateException("Set<T>集合范型类型无效:" + clazz);
		}
	}

	/**
	 * 扫描指定模块/包中的所有类和资源<br>
	 * 模块名称 "com.joyzl.odbs"<br>
	 * 包名称 "com.joyzl.odbs.test"<br>
	 * 或模块中的包名称 "com.joyzl.odbs/com.joyzl.odbs.test"<br>
	 */
	public final static List<String> scan(String name) {
		List<String> resources;
		int index = name.indexOf('/');
		if (index > 0 && index < name.length() - 1) {
			resources = scanModule(name.substring(0, index), name.substring(index + 1));
		} else {
			resources = scanModule(name, null);
		}
		if (resources == null || resources.isEmpty()) {
			resources = scanPackage(name);
		}
		return resources;
	}

	/**
	 * 扫描指定模块/包中的所有类和资源
	 */
	public final static List<Class<?>> scanClass(String name) {
		final List<String> resources = scan(name);
		if (resources != null) {
			final List<Class<?>> classes = new ArrayList<>();
			for (String resource : resources) {
				if ("module-info.class".equalsIgnoreCase(resource)) {
					continue;
				}
				if (resource.endsWith(".class")) {
					resource = resource.substring(0, resource.length() - 6);
					resource = resource.replace('/', '.');
					try {
						classes.add(Class.forName(resource));
					} catch (Exception e) {
						// 忽略此异常,不中断运行
						// e.printStackTrace();
					}
				}
			}
			return classes;
		}
		return null;
	}

	/**
	 * 扫描指定模块中的所有类和资源
	 * 
	 * @see #scanModule(String,String)
	 */
	public final static List<String> scanModule(Module module) {
		if (module.isNamed()) {
			return scanModule(module.getName(), null);
		} else {
			return null;
		}
	}

	/**
	 * 扫描指定模块中的所有类和资源
	 * 
	 * @see #scanModule(String,String)
	 */
	public final static List<String> scanModule(String module) {
		return scanModule(module, null);
	}

	/**
	 * 扫描指定模块中的所有类和资源
	 * 
	 * @param name 模块名称例如"com.joyzl.odbs"
	 * @param pkg 模块中的包"com.joyzl.odbs.test"
	 * @return null/List&lt;String&gt;
	 */
	public final static List<String> scanModule(String name, String pkg) {
		final Optional<ResolvedModule> optional = ModuleLayer.boot().configuration().findModule(name);
		if (optional.isEmpty()) {
			return null;
		}
		if (pkg != null) {
			pkg = pkg.replace('.', '/');
		}
		final List<String> resources = new ArrayList<>();
		try (ModuleReader reader = optional.get().reference().open()) {
			String resource;
			final Iterator<String> iterator = reader.list().iterator();
			while (iterator.hasNext()) {
				resource = iterator.next();
				if (resource.endsWith("/")) {
					continue;
				}
				if (pkg != null && !resource.startsWith(pkg)) {
					continue;
				}
				resources.add(resource);
				// System.out.println(resource);
			}
			return resources;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 扫描指定包中的所有类和资源
	 * 
	 * @see #scanModule(String)
	 */
	public final static List<String> scanPackage(Package pkg) {
		return scanPackage(pkg.getName());
	}

	/**
	 * 扫描指定包中的所有类和资源
	 * 
	 * @param name 包名称例如"com.joyzl.odbs"
	 * @return null/List&lt;String&gt;
	 */
	public final static List<String> scanPackage(String name) {
		final String path = name.replace('.', '/');
		// System.out.println("scanPackage:" + name);
		try {
			URL url;
			final List<String> resources = new ArrayList<>();
			// final Enumeration<URL> urls =
			// Thread.currentThread().getContextClassLoader().getResources(path);
			final Enumeration<URL> urls = ClassLoader.getSystemClassLoader().getResources(path);
			while (urls.hasMoreElements()) {
				url = urls.nextElement();
				// System.out.println(url);
				if (url == null) {
				} else if ("file".equalsIgnoreCase(url.getProtocol())) {
					scanPackage(new File(url.toURI()), path, resources);
				} else if ("jar".equalsIgnoreCase(url.getProtocol())) {
					scanPackage(((JarURLConnection) url.openConnection()).getJarFile(), path, resources);
				} else {
					// jrt:/com.joyzl.common/com/joyzl/common/Reflecter.class
				}
			}
			return resources;
		} catch (IOException | URISyntaxException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * file:/D:/GitHub/odbs/target/test-classes/com/joyzl/
	 */
	private final static void scanPackage(File parent, String path, List<String> resources) {
		if (parent.exists()) {
			final File[] files = parent.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					scanPackage(file, path + "/" + file.getName(), resources);
				} else {
					resources.add(path + "/" + file.getName());
				}
			}
		}
	}

	private final static void scanPackage(JarFile parent, String path, List<String> resources) {
		JarEntry entry;
		final Enumeration<JarEntry> entries = parent.entries();
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			if (!entry.isDirectory()) {
				// sis/servo/action/CompanyCreate.class
				if (entry.getName().startsWith(path)) {
					resources.add(entry.getName());
				}
			}
		}
	}
}
