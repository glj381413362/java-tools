package com.common.tools.util;

import java.io.File;


/**
 * <p>
 * 常量字符集合
 * </p>
 *
 * @author gongliangjun 2019/12/28 5:37 PM
 */
public interface PluginConstants {

	/**********************************************分隔符常量************************************************/

	String POINT_STR = ".";

	String BLANK_STR = "";

	String SPACE_STR = " ";

	String NEWLINE_STR = "\n";

	String SYS_SEPARATOR = File.separator;

	String FILE_SEPARATOR = "/";

	String BRACKET_LEFT = "[";

	String BRACKET_RIGHT = "]";

	String UNDERLINE = "_";

	String MINUS_STR = "-";


	/**********************************************编码格式************************************************/

	String UTF8 = "UTF-8";


	/**********************************************文件后缀************************************************/

	String EXCEL_XLS = ".xls";

	String EXCEL_XLSX = ".xlsx";

	String IMAGE_PNG = "png";

	String IMAGE_JPG = "jpg";

	String FILE_ZIP = ".zip";
	String FILE_GZ = ".gz";

	/**********************************************时间处理************************************************/
	String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

	String DATE_STR = "yyyy-MM-dd";

	String DATETIME_MS = "yyyyMMddHHmmssSSS";

	String DATE_SLASH_STR = "yyyy/MM/dd";

	String SHORT_FORMAT = "yyyyMMdd";

	String LONG_FORMAT = "yyyyMMddHHmmss";

	String WEB_FORMAT = "yyyy-MM-dd";

	String TIME_FORMAT = "HHmmss";

	String MONTH_FORMAT = "yyyyMM";

	String CHINESE_DT_FORMAT = "yyyy年MM月dd日";

	String NEW_FORMAT1 = "yyyy-MM-dd HH:mm";

	String NEW_FORMAT2 = "yyyy-MM-dd HH";

	String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";


	int SECOND = 1000;

	int MINUTE = 60 * SECOND;

	int HOUR = 60 * MINUTE;

	int DAY = 24 * HOUR;


	interface EntityDomain{
		String FIELD_CREATION_DATE = "creationDate";
		String FIELD_CREATED_BY = "createdBy";
		String FIELD_LAST_UPDATE_DATE = "lastUpdateDate";
		String FIELD_LAST_UPDATED_BY = "lastUpdatedBy";
		String FIELD_OBJECT_VERSION_NUMBER = "objectVersionNumber";
		String FIELD_TABLE_ID = "tableId";
	}


}
