package com.common.tools.util.copier;

import com.common.tools.util.PluginConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.objenesis.Objenesis;
import org.springframework.objenesis.ObjenesisStd;
import org.springframework.objenesis.instantiator.ObjectInstantiator;

/**
 * 实体类属性拷贝工具
 *
 * @author gongliangjun 2020/06/13 5:16 PM
 */
public class PropertiesCopier {
  private static final ConcurrentHashMap<String, BeanCopier> COPIER_CACHE =
      new ConcurrentHashMap<>();

  public static <O, T> T mapper(final O source, final Class<T> targetClass) {
    return copyProperties(source, targetClass, null);
  }

  public static <O, T> T mapper(
      final O source, final Class<T> targetClass, final CopierAction<T> copierAction) {
    final T target = copyProperties(source, targetClass, null);
    copierAction.run(target);
    return target;
  }

  public static <O, T> T mapper(
      final O source, final Class<T> targetClass, final Converter converter) {
    return copyProperties(source, targetClass, converter);
  }

  public static <O, T> T mapper(
      final O source,
      final Class<T> targetClass,
      final Converter converter,
      final CopierAction<T> copierAction) {
    final T target = copyProperties(source, targetClass, converter);
    copierAction.run(target);
    return target;
  }

  public static <O, T> void mapperObject(final O source, final T target) {
    copyObject(source, target, null);
  }

  public static <O, T> void mapperObject(
      final O source, final T target, final CopierAction<T> copierAction) {
    copyObject(source, target, null);
    copierAction.run(target);
  }

  public static <O, T> void mapperObject(
      final O source, final T target, final Converter converter) {
    copyObject(source, target, converter);
  }

  public static <O, T> void mapperObject(
      final O source,
      final T target,
      final Converter converter,
      final CopierAction<T> copierAction) {
    copyObject(source, target, converter);
    copierAction.run(target);
  }

  public static <O, T> List<T> listMapper(final List<O> sourceList, final Class<T> targetClass) {
    final List<T> targetList = new ArrayList<>();
    sourceList.forEach(
        source -> {
          targetList.add(mapper(source, targetClass));
        });
    return targetList;
  }

  public static <O, T> List<T> listMapper(
      final List<O> sourceList, final Class<T> targetClass, final CopierAction<T> copierAction) {
    final List<T> targetList = new ArrayList<T>();
    sourceList.forEach(
        source -> {
          targetList.add(mapper(source, targetClass, copierAction));
        });
    return targetList;
  }

  public static <O, T> List<T> listMapper(
      final List<O> sourceList, final Class<T> targetClass, final Converter converter) {
    final List<T> targetList = new ArrayList<>();
    sourceList.forEach(
        source -> {
          targetList.add(mapper(source, targetClass, converter));
        });
    return targetList;
  }

  public static <O, T> List<T> listMapper(
      final List<O> sourceList,
      final Class<T> targetClass,
      final Converter converter,
      final CopierAction<T> copierAction) {
    final List<T> targetList = new ArrayList<>();
    sourceList.forEach(
        source -> {
          targetList.add(mapper(source, targetClass, converter, copierAction));
        });
    return targetList;
  }

  /**
   * DB Entity Copy Properties 专用
   *
   * @param source 源数据
   * @param target 目标数据
   * @param <O> 源数据类型
   * @param <T> 目标数据类型
   */
  public static <O, T> void copyEntities(final O source, final T target) {
    BeanUtils.copyProperties(
        source,
        target,
        PluginConstants.EntityDomain.FIELD_CREATED_BY,
        PluginConstants.EntityDomain.FIELD_CREATION_DATE,
        PluginConstants.EntityDomain.FIELD_LAST_UPDATED_BY,
        PluginConstants.EntityDomain.FIELD_LAST_UPDATE_DATE,
        PluginConstants.EntityDomain.FIELD_OBJECT_VERSION_NUMBER);
  }

  /**
   * DB Entity Copy Properties 专用
   *
   * @param source 源数据
   * @param target 目标数据
   * @param copierAction 拷贝结束执行
   * @param <O> 源数据类型
   * @param <T> 目标数据类型
   */
  public static <O, T> void copyEntities(
      final O source, final T target, final CopierAction<T> copierAction) {
    BeanUtils.copyProperties(
        source,
        target,
        PluginConstants.EntityDomain.FIELD_CREATED_BY,
        PluginConstants.EntityDomain.FIELD_CREATION_DATE,
        PluginConstants.EntityDomain.FIELD_LAST_UPDATED_BY,
        PluginConstants.EntityDomain.FIELD_LAST_UPDATE_DATE,
        PluginConstants.EntityDomain.FIELD_OBJECT_VERSION_NUMBER);
    copierAction.run(target);
  }

  private static <O, T> T copyProperties(
      final O source, final Class<T> targetClass, final Converter converter) {
    final String copierKey = generateKey(source.getClass(), targetClass, converter);
    final BeanCopier copier;
    if (!COPIER_CACHE.containsKey(copierKey)) {
      if (converter == null) {
        copier = BeanCopier.create(source.getClass(), targetClass, false);
      } else {
        copier = BeanCopier.create(source.getClass(), targetClass, true);
      }
      COPIER_CACHE.put(copierKey, copier);
    } else {
      copier = COPIER_CACHE.get(copierKey);
    }

    final Objenesis objenesis = new ObjenesisStd();
    final ObjectInstantiator<T> objectInstant = objenesis.getInstantiatorOf(targetClass);
    final T targetInstance = objectInstant.newInstance();
    copier.copy(source, targetInstance, converter);
    return targetInstance;
  }

  private static <O, T> void copyObject(final O source, final T target, final Converter converter) {
    final String copierKey = generateKey(source.getClass(), target.getClass(), converter);
    final BeanCopier copier;
    if (!COPIER_CACHE.containsKey(copierKey)) {
      if (converter == null) {
        copier = BeanCopier.create(source.getClass(), target.getClass(), false);
      } else {
        copier = BeanCopier.create(source.getClass(), target.getClass(), true);
      }
      COPIER_CACHE.put(copierKey, copier);
    } else {
      copier = COPIER_CACHE.get(copierKey);
    }
    copier.copy(source, target, converter);
  }

  private static String generateKey(
      final Class<?> sourceClass, final Class<?> targetClass, final Converter converter) {
    if (converter != null) {
      return sourceClass.toString() + "," + targetClass.toString() + "," + converter.toString();
    } else {
      return sourceClass.toString() + "," + targetClass.toString();
    }
  }

  @FunctionalInterface
  public interface CopierAction<T> {

    /**
     * copier 执行操作
     *
     * @param param 执行参数
     */
    void run(T param);
  }
}
