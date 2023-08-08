/*
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
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 对象反射相关辅助方法
 * 
 * @author ZhangXi
 * @date 2020年6月3日
 */
public final class ODBSReflect {

	/** Java Resource 的路径分隔符 */
	final static char PATH_SEPARATER = '/';
	/** Java 命名空间分隔符 */
	final static char NAME_SEPARATER = '.';

	private ODBSReflect() {
		// 禁止实例化
	}

	/**
	 * 监测聚合值中是否包含指定值
	 * 
	 * @param source
	 * @param value
	 * @return
	 */
	protected final static boolean contains(int source, int value) {
		return (source & value) != 0;
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
	 * 查找指定类型的指定注解
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
	 * 搜索并返回指定模块/包中的类
	 * 
	 * @param name "sis.common" 模块/包名称
	 * @return List<Class<?>>
	 */
	public final static List<Class<?>> scanClass(String name) {
		final List<Class<?>> classes = new ArrayList<>();
		// sis.common.action -> sis/common/action
		final String path = name.replace(NAME_SEPARATER, PATH_SEPARATER);
		final Optional<ResolvedModule> optional = ModuleLayer.boot().configuration().findModule(name);
		if (optional == null || optional.isEmpty()) {
			final Set<ResolvedModule> modules = ModuleLayer.boot().configuration().modules();
			for (ResolvedModule module : modules) {
				for (String packega : module.reference().descriptor().packages()) {
					if (packega.equals(name)) {
						scanClassFromModule(module, path, classes);
						return classes;
					}
				}
			}

			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			try {
				URL url;
				final Enumeration<URL> urls = loader.getResources(path);
				while (urls.hasMoreElements()) {
					url = urls.nextElement();
					if (url == null) {
						// 不应出现的情况
					} else if ("file".equals(url.getProtocol())) {
						// file:/D:/SmartIndustrySystem/sis-common/bin/sis/common
						scanClassFromFile(new File(url.toURI()), name, classes);
					} else if ("jar".equals(url.getProtocol())) {
						// jar:file:/D:/SmartIndustrySystem/sis-network/lib/netty-all-4.1.22.Final.jar!/io/netty/buffer
						scanClassFromJar(((JarURLConnection) url.openConnection()).getJarFile(), name, classes);
					} else {
						// jrt:/com.joyzl.common/com/joyzl/common/Reflecter.class
						throw new RuntimeException("暂未处理的URL类型 " + url);
					}
				}
			} catch (IOException | URISyntaxException ex) {
				throw new RuntimeException(ex);
			}
		} else {
			scanClassFromModule(optional.get(), path, classes);
		}
		return classes;
	}

	private final static void scanClassFromJar(JarFile parent, String packega, List<Class<?>> classes) {
		Enumeration<JarEntry> entries = parent.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (!entry.isDirectory()) {
				String name = entry.getName();
				// sis/servo/action/CompanyCreate.class
				if (name.startsWith(packega) && name.endsWith(".class")) {
					// "sis/servo/action/CompanyCreate.class"->"sis.servo.action.CompanyCreate"
					name = name.substring(0, name.length() - 6);
					name = name.replace(PATH_SEPARATER, NAME_SEPARATER);
					scanClassFromName(name, classes);
				}
			}
		}
	}

	private final static void scanClassFromModule(ResolvedModule module, String path, List<Class<?>> classes) {
		try (ModuleReader reader = module.reference().open()) {
			reader.list().forEach(new Consumer<String>() {
				@Override
				public void accept(String name) {
					// com/joyzl/scm/store/views/products/ProductWindow.class
					if (name.endsWith("module-info.class") || name.endsWith("package-info.class")) {
						// 过滤module-info名称
					} else if (name.startsWith(path) && name.endsWith(".class")) {
						// 转换名称为'.'分隔符，".class" 长度为 6
						// "com/joyzl/scm/store/views/products/ProductWindow.class"->"com.joyzl.scm.store.views.products.ProductWindow"
						name = name.substring(0, name.length() - 6);
						name = name.replace(PATH_SEPARATER, NAME_SEPARATER);
						scanClassFromName(name, classes);
					}
				}
			});
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 搜索并返回指定包中的所有类
	 * 
	 * @param packega "sis.common"
	 * @return List<Class<?>>
	 */
	public final static List<Class<?>> scanClassFromPackage(String packega) {
		List<Class<?>> classes = new ArrayList<>();
		try {
			// sis.common.action = sis/common/action
			String packag_path = packega.replace('.', '/');
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> urls = loader.getResources(packag_path);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if ("file".equals(url.getProtocol())) {
					// file:/D:/SmartIndustrySystem/sis-common/bin/sis/common
					String path = URLDecoder.decode(url.getFile(), "UTF-8");
					scanClassFromFile(new File(path), packega, classes);
				} else if ("jar".equals(url.getProtocol())) {
					// jar:file:/D:/SmartIndustrySystem/sis-network/lib/netty-all-4.1.22.Final.jar!/io/netty/buffer
					JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
					scanClassFromFile(jar, packag_path, classes);
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return classes;
	}

	/**
	 * 搜索并返回指定包中的文件
	 * 
	 * @param packega "sis.common"
	 * @return List<String>
	 */
	public final static List<String> scanFileFromPackage(String packega, String extension) {
		List<String> files = new ArrayList<>();
		try {
			// sis.common.action = sis/common/action
			String packag_path = packega.replace('.', '/');
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Enumeration<URL> urls = loader.getResources(packag_path);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if ("file".equals(url.getProtocol())) {
					// file:/D:/SmartIndustrySystem/sis-common/bin/sis/common
					String path = URLDecoder.decode(url.getFile(), "UTF-8");
					scanFileFromFile(new File(path), packega, files, extension);
				} else if ("jar".equals(url.getProtocol())) {
					// jar:file:/D:/SmartIndustrySystem/sis-network/lib/netty-all-4.1.22.Final.jar!/io/netty/buffer
					JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
					scanFileFromFile(jar, packag_path, files, extension);
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		return files;
	}

	private final static void scanClassFromFile(JarFile parent, String packega, List<Class<?>> classes) {
		Enumeration<JarEntry> entries = parent.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (!entry.isDirectory()) {
				String name = entry.getName();
				// sis/servo/action/CompanyCreate.class
				if (name.startsWith(packega) && name.endsWith(".class")) {
					// sis/servo/action/CompanyCreate.class=sis.servo.action.CompanyCreate
					name = name.substring(0, name.length() - 6);
					name = name.replace('/', '.');
					scanClassFromName(name, classes);
				}
			}
		}
	}

	private final static void scanFileFromFile(JarFile parent, String packega, List<String> files, String extension) {
		Enumeration<JarEntry> entries = parent.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (!entry.isDirectory()) {
				String name = entry.getName();
				// sis/servo/action/CompanyCreate.class
				if (name.startsWith(packega) && name.endsWith(extension)) {
					// sis/servo/action/CompanyCreate.class=sis.servo.action.CompanyCreate
					name = name.substring(0, name.length() - extension.length());
					name = name.replace('/', '.');
					files.add(name);
				}
			}
		}
	}

	private final static void scanClassFromFile(File parent, String packega, List<Class<?>> classes) {
		if (parent.exists()) {
			File[] files = parent.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					scanClassFromFile(file, packega + '.' + file.getName(), classes);
				} else {
					String name = file.getName();
					if (name.endsWith(".class")) {
						name = name.substring(0, name.length() - 6);
						scanClassFromName(packega + '.' + name, classes);
					}
				}
			}
		}
	}

	private final static void scanFileFromFile(File parent, String packega, List<String> files, String extension) {
		if (parent.exists()) {
			File[] fs = parent.listFiles();
			for (File file : fs) {
				if (file.isDirectory()) {
					scanFileFromFile(file, packega + '.' + file.getName(), files, extension);
				} else {
					String name = file.getName();
					if (name.endsWith(extension)) {
						name = name.substring(0, name.length() - extension.length());
						files.add(packega + '.' + name);
					}
				}
			}
		}
	}

	private final static void scanClassFromName(String name, List<Class<?>> classes) {
		try {
			classes.add(Class.forName(name));
		} catch (Exception e) {
			// 忽略此异常,不中断运行
			e.printStackTrace();
		}
	}
}
