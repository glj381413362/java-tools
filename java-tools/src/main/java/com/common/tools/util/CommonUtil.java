package com.common.tools.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import static com.common.tools.util.StringUtil.strFormat;

/**
 * <p>util </p>
 *
 * @author gongliangjun 2019/07/01 11:18
 */
@Slf4j
public class CommonUtil {


	/**
	 * 判断对象是否为空，并且打印日志
	 *
	 * @param logger a.
	 * @param object  object.  object.
	 * @param msgT msgT. msgT.
	 * @param params params. params.
	 * @author gongliangjun 2020-07-22 10:38 AM
	 * @return boolean
	 */
	public static boolean objIsNull(Logger logger, Object object, String msgT, Object... params) {

		boolean res = null == object;
		if (null == logger) {
			log.info(strFormat(msgT, params) + " :[{}]", res);
		} else {
			logger.info(strFormat(msgT, params) + " :[{}]", res);
		}
		return res;
	}
	/**
	 * 判断对象是否为空，并且打印日志
	 *
	 * @param object  object. .
 	 * @param msgT msgT. .
	 * @param params params. .
	 * @return boolean .
	 * @author gongliangjun 2019-12-25 5:50 PM
	 */
	public static boolean objIsNull(Object object, String msgT, Object... params) {
		return objIsNull(null,object,msgT,params);
	}

	/**
	 * 判断对象是否不为空，并且打印日志
	 *
	 * @param logger logger.
	 * @param object  object.
	 * @param msgT msgT.
	 * @param params params.
	 * @author gongliangjun 2020-07-22 10:37 AM
	 * @return boolean .
	 */
	public static boolean objIsNotNull(Logger logger,Object object, String msgT, Object... params) {
		boolean res = null != object;
		if (null == logger) {
			log.info(strFormat(msgT, params) + " :[{}]", res);
		} else {
			logger.info(strFormat(msgT, params) + " :[{}]", res);
		}
		return res;
	}
	/**
	 * 判断对象是否不为空，并且打印日志
	 *
	 * @param object  object. .
	 * @param msgT msgT. .
	 * @param params params. .
	 * @return boolean .
	 * @author gongliangjun 2019-12-25 5:50 PM
	 */
	public static boolean objIsNotNull(Object object, String msgT, Object... params) {
		return objIsNotNull(null,object,msgT,params);
	}


}
