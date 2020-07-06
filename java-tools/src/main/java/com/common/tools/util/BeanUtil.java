package com.common.tools.util;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * <p>
 * 【依赖】org.springframework.spring-beans
 * </p>
 *
 * @author gongliangjun 2019/07/01 11:18
 */
public class BeanUtil {
	public final static String[] whoFields = new String[]{"creationDate", "createdBy", "lastUpdateDate", "lastUpdatedBy", "objectVersionNumber", "tableId"};


	/**
	 *
	 */
	private static List<String> placeholders = new ArrayList<String>() {{
		add("$empty$");
	}};


	public static <T, S> S copySourceToTarget(T t, S s) {

		BeanUtils.copyProperties(t, s, getNullPropertyNames(t));
		return s;
	}

	public static <T, S> List<S> copyListSourceToTarget(List<T> ts, Class<S> s) {
		List<S> res = new ArrayList<>(ts.size());
		for (T t : ts) {
			S newInstance = null;
			try {
				newInstance = s.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			BeanUtils.copyProperties(t, newInstance, getNullPropertyNames(t));
			res.add(newInstance);
		}
		return res;
	}

	public static <T, S> S copySourceToTarget(T t, S s, boolean ignoreWhoField) {
		String[] both = getNullPropertyNames(t);
		if (ignoreWhoField) {
			both = ArrayUtils.addAll(both, whoFields);
		}
		BeanUtils.copyProperties(t, s, both);
		return s;
	}

	public static <T, S> S copySourceToTarget(T t, S s, boolean ignoreWhoField, boolean ignoreNullField) {
		String[] both = new String[]{};
		if (ignoreNullField) {
			both = getNullPropertyNames(t);
		}
		if (ignoreWhoField) {
			both = ArrayUtils.addAll(both, whoFields);
		}
		BeanUtils.copyProperties(t, s, both);
		return s;
	}

	public static <T, S> S copySourceToTarget(T t, S s, boolean ignoreWhoField, boolean ignoreNullField, String[] ignores) {
		String[] both = new String[]{};
		if (ignoreNullField) {
			both = getNullPropertyNames(t);
		}
		both = ArrayUtils.addAll(both, ignores);
		if (ignoreWhoField) {
			both = ArrayUtils.addAll(both, whoFields);
		}
		BeanUtils.copyProperties(t, s, both);
		return s;
	}

	private static <T> List<T> convert(List<?> sourceList, Class<T> targetClass, boolean isIgnoreProperties) {
		if (sourceList == null || sourceList.size() == 0) {
			return new ArrayList<T>(0);
		}
		List<T> list = new ArrayList<T>(sourceList.size());
		for (Object obj : sourceList) {
			String[] ignoreProperties = (String[]) null;
			if (isIgnoreProperties) {
				ignoreProperties = getNullPropertyNames(obj);
			}
			list.add(convert(obj, targetClass, ignoreProperties));
		}
		return list;
	}

	private static <T> T convert(Object source, Class<T> targetClass, String... ignoreProperties) {
		if (source == null) {
			return null;
		}
		try {
			T target = targetClass.newInstance();
			BeanUtils.copyProperties(source, target, ignoreProperties);
			return target;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 1.获取当前对象为空的属性，方便复制属性值
	 * 2.场景：假设我们需要将某一个字段更新数据库为空字符串，根据约定前端会传递指定的字符，此方法将指定的字符转换成空字符串
	 *
	 * @param source
	 * @return
	 */
	public static String[] getNullPropertyNames(Object source) {
		final BeanWrapper src = new BeanWrapperImpl(source);
		java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

		Set<String> emptyNames = new HashSet<>();
		for (java.beans.PropertyDescriptor pd : pds) {
			Object srcValue = src.getPropertyValue(pd.getName());
			//===============================================================================
			//  判断是否属性的值为指定的字符，是则替换成空字符串
			//===============================================================================
			if (placeholders.contains(srcValue)) {
				try {
					//===============================================================================
					//  调用setXXX方法设置值
					//===============================================================================
					pd.getWriteMethod().invoke(source, "");
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

			}
			if (srcValue == null) {
				emptyNames.add(pd.getName());
			}
		}
		String[] result = new String[emptyNames.size()];
		return emptyNames.toArray(result);
	}

	/**
	 * 深度克隆对象
	 *
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static Object deepClone(Object objSource) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (null == objSource) {
			return null;
		}
		// 获取源对象类型
		Class<?> clazz = objSource.getClass();
		Object objDes = clazz.newInstance();
		// 获得源对象所有属性
		Field[] fields = getAllFields(objSource);
		// 循环遍历字段，获取字段对应的属性值
		for (Field field : fields) {
			field.setAccessible(true);
			// 如果该字段是 static + final 修饰
			if (field.getModifiers() >= 24) {
				continue;
			}
			try {
				// 设置字段可见，即可用get方法获取属性值。
				field.set(objDes, field.get(objSource));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return objDes;
	}

	/**
	 * 获取包括父类所有的属性
	 *
	 * @param objSource
	 * @return
	 */
	public static Field[] getAllFields(Object objSource) {
		/*获得当前类的所有属性(private、protected、public)*/
		List<Field> fieldList = new ArrayList<Field>();
		Class tempClass = objSource.getClass();
		while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {//当父类为null的时候说明到达了最上层的父类(Object类).
			fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
			tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
		}
		Field[] fields = new Field[fieldList.size()];
		fieldList.toArray(fields);
		return fields;
	}


}
