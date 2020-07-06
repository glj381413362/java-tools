/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 * All Rights Reserved
 *
 */

package com.common.tools.util;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.common.tools.util.StringUtil.strFormat;

/**
 * <p>
 * 集合处理工具类
 * </p>
 *
 * @author gongliangjun 2019/12/28 7:14 PM
 */
public class ListUtil {
	/**
	 * logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ListUtil.class);

	/**
	 * 判断数组是否为空，并且打印判断结果
	 *
	 * @param list
	 * @param msgT
	 * @param params
	 * @return boolean
	 * @author gongliangjun 2019-12-25 5:14 PM
	 */
	public static boolean listIsEmpty(Logger logger, List list, String msgT, Object... params) {
		boolean res = CollectionUtils.isEmpty(list);
		if (null == logger) {
			LOG.info(strFormat(msgT, params) + " :[{}]", res);
		} else {
			logger.info(strFormat(msgT, params) + " :[{}]", res);
		}
		return res;
	}

	/**
	 * 判断数组是否为空，并且打印判断结果
	 *
	 * @param list
	 * @param msgT
	 * @param params
	 * @return boolean
	 * @author gongliangjun 2019-12-25 5:14 PM
	 */
	public static boolean listIsEmpty(List list, String msgT, Object... params) {
		return listIsEmpty(null, list, msgT, params);
	}

	/**
	 * 判断数组是否不为空，并且打印判断结果
	 *
	 * @param list
	 * @param msgT
	 * @param params
	 * @return boolean
	 * @author gongliangjun 2019-12-25 5:14 PM
	 */
	public static boolean listIsNotEmpty(Logger logger, List list, String msgT, Object... params) {
		boolean res = CollectionUtils.isNotEmpty(list);
		if (null == logger) {
			LOG.info(strFormat(msgT, params) + " :[{}]", res);
		} else {
			logger.info(strFormat(msgT, params) + " :[{}]", res);
		}
		return res;
	}

	/**
	 * 判断数组是否不为空，并且打印判断结果
	 *
	 * @param list
	 * @param msgT
	 * @param params
	 * @return boolean
	 * @author gongliangjun 2019-12-25 5:14 PM
	 */
	public static boolean listIsNotEmpty(List list, String msgT, Object... params) {
		return listIsNotEmpty(null, list, msgT, params);
	}


	/**
	 * @param sourceList
	 * @param perListSize
	 * @return 子List集合
	 *
	 * @description 按指定大小，分隔集合List，将集合按规定个数分为n个部分
	 */
	public static <T> List<List<T>> splitList(List<T> sourceList, int perListSize) {
		if (sourceList == null || sourceList.size() == 0 || perListSize < 1) {
			return null;
		}

		List<List<T>> result = new ArrayList<List<T>>();

		int size = sourceList.size();
		int count = (size + perListSize - 1) / perListSize;

		for (int i = 0; i < count; i++) {
			List<T> subList = sourceList.subList(i * perListSize, ((i + 1)
					* perListSize > size ? size : perListSize * (i + 1)));
			result.add(subList);
		}
		return result;
	}

	/**
	 * @param source
	 * @param n
	 * @return 子List集合
	 *
	 * @description 将一个list均分成n个子list, 主要通过偏移量来实现
	 */
	public static <T> List<List<T>> averageAssign(List<T> source, int n) {
		final List<List<T>> result = new ArrayList<List<T>>();
		// (先计算出余数)
		int remaider = source.size() % n;
		// 然后是商
		int number = source.size() / n;
		int offset = 0;// 偏移量
		for (int i = 0; i < n; i++) {
			List<T> value = null;
			if (remaider > 0) {
				value = source.subList(i * number + offset, (i + 1) * number
						+ offset + 1);
				remaider--;
				offset++;
			} else {
				value = source.subList(i * number + offset, (i + 1) * number
						+ offset);
			}
			result.add(value);
		}
		return result;
	}

}
