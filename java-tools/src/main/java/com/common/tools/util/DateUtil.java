package com.common.tools.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.common.tools.util.PluginConstants.DATE_TIME;
import static com.common.tools.util.PluginConstants.MONTH_FORMAT;

/**
 * 方便的实用程序使用Java日期。
 */
@Slf4j
public final class DateUtil {
	private static final String days = "(monday|mon|tuesday|tues|tue|wednesday|wed|thursday|thur|thu|friday|fri|saturday|sat|sunday|sun)"; // longer before shorter matters
	private static final String mos = "(Jan|January|Feb|February|Mar|March|Apr|April|May|Jun|June|Jul|July|Aug|August|Sep|Sept|September|Oct|October|Nov|November|Dec|December)";
	private static final Pattern datePattern1 = Pattern.compile("(\\d{4})[./-](\\d{1,2})[./-](\\d{1,2})");
	private static final Pattern datePattern2 = Pattern.compile("(\\d{1,2})[./-](\\d{1,2})[./-](\\d{4})");
	private static final Pattern datePattern3 = Pattern.compile(mos + "[ ]*[,]?[ ]*(\\d{1,2})(st|nd|rd|th|)[ ]*[,]?[ ]*(\\d{4})", Pattern.CASE_INSENSITIVE);
	private static final Pattern datePattern4 = Pattern.compile("(\\d{1,2})(st|nd|rd|th|)[ ]*[,]?[ ]*" + mos + "[ ]*[,]?[ ]*(\\d{4})", Pattern.CASE_INSENSITIVE);
	private static final Pattern datePattern5 = Pattern.compile("(\\d{4})[ ]*[,]?[ ]*" + mos + "[ ]*[,]?[ ]*(\\d{1,2})(st|nd|rd|th|)", Pattern.CASE_INSENSITIVE);
	private static final Pattern datePattern6 = Pattern.compile(days + "[ ]+" + mos + "[ ]+(\\d{1,2})[ ]+(\\d{2}:\\d{2}:\\d{2})[ ]+[A-Z]{1,3}\\s+(\\d{4})", Pattern.CASE_INSENSITIVE);
	private static final Pattern timePattern1 = Pattern.compile("(\\d{2})[:.](\\d{2})[:.](\\d{2})[.](\\d{1,10})([+-]\\d{2}[:]?\\d{2}|Z)?");
	private static final Pattern timePattern2 = Pattern.compile("(\\d{2})[:.](\\d{2})[:.](\\d{2})([+-]\\d{2}[:]?\\d{2}|Z)?");
	private static final Pattern timePattern3 = Pattern.compile("(\\d{2})[:.](\\d{2})([+-]\\d{2}[:]?\\d{2}|Z)?");
	private static final Pattern dayPattern = Pattern.compile(days, Pattern.CASE_INSENSITIVE);
	private static final Map<String, String> months = new LinkedHashMap<>();

	static {
		// Month name to number map
		months.put("jan", "1");
		months.put("january", "1");
		months.put("feb", "2");
		months.put("february", "2");
		months.put("mar", "3");
		months.put("march", "3");
		months.put("apr", "4");
		months.put("april", "4");
		months.put("may", "5");
		months.put("jun", "6");
		months.put("june", "6");
		months.put("jul", "7");
		months.put("july", "7");
		months.put("aug", "8");
		months.put("august", "8");
		months.put("sep", "9");
		months.put("sept", "9");
		months.put("september", "9");
		months.put("oct", "10");
		months.put("october", "10");
		months.put("nov", "11");
		months.put("november", "11");
		months.put("dec", "12");
		months.put("december", "12");
	}

	private DateUtil() {
		super();
	}

	public static Date parseDate(String dateStr) {
		if (dateStr == null) {
			return null;
		}
		dateStr = dateStr.trim();
		if ("".equals(dateStr)) {
			return null;
		}

		// Determine which date pattern (Matcher) to use
		Matcher matcher = datePattern1.matcher(dateStr);

		String year, month = null, day, mon = null, remains;

		if (matcher.find()) {
			year = matcher.group(1);
			month = matcher.group(2);
			day = matcher.group(3);
			remains = matcher.replaceFirst("");
		} else {
			matcher = datePattern2.matcher(dateStr);
			if (matcher.find()) {
				month = matcher.group(1);
				day = matcher.group(2);
				year = matcher.group(3);
				remains = matcher.replaceFirst("");
			} else {
				matcher = datePattern3.matcher(dateStr);
				if (matcher.find()) {
					mon = matcher.group(1);
					day = matcher.group(2);
					year = matcher.group(4);
					remains = matcher.replaceFirst("");
				} else {
					matcher = datePattern4.matcher(dateStr);
					if (matcher.find()) {
						day = matcher.group(1);
						mon = matcher.group(3);
						year = matcher.group(4);
						remains = matcher.replaceFirst("");
					} else {
						matcher = datePattern5.matcher(dateStr);
						if (matcher.find()) {
							year = matcher.group(1);
							mon = matcher.group(2);
							day = matcher.group(3);
							remains = matcher.replaceFirst("");
						} else {
							matcher = datePattern6.matcher(dateStr);
							if (!matcher.find()) {
								error("Unable to parse: " + dateStr);
							}
							year = matcher.group(5);
							mon = matcher.group(2);
							day = matcher.group(3);
							remains = matcher.group(4);
						}
					}
				}
			}
		}

		if (mon != null) {   // Month will always be in Map, because regex forces this.
			month = months.get(mon.trim().toLowerCase());
		}

		// Determine which date pattern (Matcher) to use
		String hour = null, min = null, sec = "00", milli = "0", tz = null;
		remains = remains.trim();
		matcher = timePattern1.matcher(remains);
		if (matcher.find()) {
			hour = matcher.group(1);
			min = matcher.group(2);
			sec = matcher.group(3);
			milli = matcher.group(4);
			if (matcher.groupCount() > 4) {
				tz = matcher.group(5);
			}
		} else {
			matcher = timePattern2.matcher(remains);
			if (matcher.find()) {
				hour = matcher.group(1);
				min = matcher.group(2);
				sec = matcher.group(3);
				if (matcher.groupCount() > 3) {
					tz = matcher.group(4);
				}
			} else {
				matcher = timePattern3.matcher(remains);
				if (matcher.find()) {
					hour = matcher.group(1);
					min = matcher.group(2);
					if (matcher.groupCount() > 2) {
						tz = matcher.group(3);
					}
				} else {
					matcher = null;
				}
			}
		}

		if (matcher != null) {
			remains = matcher.replaceFirst("");
		}

		// Clear out day of week (mon, tue, wed, ...)
		if (StringUtils.length(remains) > 0) {
			Matcher dayMatcher = dayPattern.matcher(remains);
			if (dayMatcher.find()) {
				remains = dayMatcher.replaceFirst("").trim();
			}
		}
		if (StringUtils.length(remains) > 0) {
			remains = remains.trim();
			if (!remains.equals(",") && (!remains.equals("T"))) {
				error("Issue parsing data/time, other characters present: " + remains);
			}
		}

		Calendar c = Calendar.getInstance();
		c.clear();
		if (tz != null) {
			if ("z".equalsIgnoreCase(tz)) {
				c.setTimeZone(TimeZone.getTimeZone("GMT"));
			} else {
				c.setTimeZone(TimeZone.getTimeZone("GMT" + tz));
			}
		}

		// Regex prevents these from ever failing to parse
		int y = Integer.parseInt(year);
		int m = Integer.parseInt(month) - 1;    // months are 0-based
		int d = Integer.parseInt(day);

		if (m < 0 || m > 11) {
			error("Month must be between 1 and 12 inclusive, date: " + dateStr);
		}
		if (d < 1 || d > 31) {
			error("Day must be between 1 and 31 inclusive, date: " + dateStr);
		}

		if (matcher == null) {   // no [valid] time portion
			c.set(y, m, d);
		} else {
			// Regex prevents these from ever failing to parse.
			int h = Integer.parseInt(hour);
			int mn = Integer.parseInt(min);
			int s = Integer.parseInt(sec);
			int ms = Integer.parseInt(milli);

			if (h > 23) {
				error("Hour must be between 0 and 23 inclusive, time: " + dateStr);
			}
			if (mn > 59) {
				error("Minute must be between 0 and 59 inclusive, time: " + dateStr);
			}
			if (s > 59) {
				error("Second must be between 0 and 59 inclusive, time: " + dateStr);
			}

			// regex enforces millis to number
			c.set(y, m, d, h, mn, s);
			c.set(Calendar.MILLISECOND, ms);
		}
		return c.getTime();
	}

	private static void error(String msg) {
		throw new IllegalArgumentException(msg);
	}

	/**
	 * 日期按照指定格式转换成字符串
	 *
	 * @param date    日期
	 * @param pattern 默认 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String format(Date date, String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern == null ? DATE_TIME : pattern);
		return dateFormat.format(date);
	}

	/**
	 * 将时间转换成对应的格式
	 *
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static Date parse(String time, String pattern) throws ParseException {
		//===============================================================================
		//  校验事务时间必须带有时分秒
		//===============================================================================
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.parse(time);
	}

	public static String getEmailDate(Date today) {
		String todayStr;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");

		todayStr = sdf.format(today);
		return todayStr;
	}

	/**
	 * 取得两个日期间隔毫秒数（日期1-日期2）
	 *
	 * @param one
	 * @param two
	 * @return 间隔毫秒数
	 */
	public static long getDiffMilliseconds(Date one, Date two) {
		Calendar sysDate = new GregorianCalendar();

		sysDate.setTime(one);

		Calendar failDate = new GregorianCalendar();

		failDate.setTime(two);
		return (sysDate.getTimeInMillis() - failDate.getTimeInMillis());
	}

	/**
	 * 取得两个日期间隔秒数（日期1-日期2）
	 *
	 * @param one 日期1
	 * @param two 日期2
	 * @return 间隔秒数
	 */
	public static long getDiffSeconds(Date one, Date two) {
		Calendar sysDate = new GregorianCalendar();

		sysDate.setTime(one);

		Calendar failDate = new GregorianCalendar();

		failDate.setTime(two);
		return (sysDate.getTimeInMillis() - failDate.getTimeInMillis()) / 1000;
	}

	public static long getDiffMinutes(Date one, Date two) {
		Calendar sysDate = new GregorianCalendar();

		sysDate.setTime(one);

		Calendar failDate = new GregorianCalendar();

		failDate.setTime(two);
		return (sysDate.getTimeInMillis() - failDate.getTimeInMillis())
				/ (60 * 1000);
	}

	/**
	 * 取得两个日期的间隔天数
	 *
	 * @param one
	 * @param two
	 * @return 间隔天数
	 */
	public static long getDiffDays(Date one, Date two) {
		Calendar sysDate = new GregorianCalendar();

		sysDate.setTime(one);

		Calendar failDate = new GregorianCalendar();

		failDate.setTime(two);
		return (sysDate.getTimeInMillis() - failDate.getTimeInMillis())
				/ (24 * 60 * 60 * 1000);
	}


	public static String convert(String dateString, DateFormat formatIn,
								 DateFormat formatOut) {
		Date date = null;
		try {
			date = formatIn.parse(dateString);
		} catch (ParseException e) {
			log.warn("convert error:{}", e);
			return "";
		}

		return formatOut.format(date);
	}

	public static String getSmsDate(Date today) {
		String todayStr;
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");

		todayStr = sdf.format(today);
		return todayStr;
	}

	/**
	 * date转LocalDate
	 *
	 * @param dateToConvert
	 * @return java.time.LocalDate
	 * @author gongliangjun 2020-01-02 9:48 AM
	 */
	public static LocalDate convertToLocalDate(Date dateToConvert) {
		return dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
	}

	/**
	 * date 转 LocalDateTime
	 *
	 * @param dateToConvert
	 * @return java.time.LocalDateTime
	 * @author gongliangjun 2020-01-02 9:49 AM
	 */
	public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
		return dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}

	/**
	 * LocalDate 转化成 Date
	 *
	 * @param dateToConvert
	 * @return java.util.Date
	 * @author gongliangjun 2020-01-02 9:50 AM
	 */
	public static Date convertToDate(LocalDate dateToConvert) {
		return java.util.Date.from(dateToConvert.atStartOfDay()
				.atZone(ZoneId.systemDefault())
				.toInstant());
	}

	/**
	 * LocalDateTime 转化成 Date
	 *
	 * @param dateToConvert
	 * @return java.util.Date
	 * @author gongliangjun 2020-01-02 9:50 AM
	 */
	public static Date convertToDate(LocalDateTime dateToConvert) {
		return java.util.Date
				.from(dateToConvert.atZone(ZoneId.systemDefault())
						.toInstant());
	}

	/**
	 * 获取月份
	 *
	 * @param date
	 * @return java.lang.String
	 * @author gongliangjun 2019-12-28 7:41 PM
	 */
	public static String formatMonth(Date date) {

		if (date == null) {
			return null;
		}
		return new SimpleDateFormat(MONTH_FORMAT).format(date);
	}

	/**
	 * 得到系统当前的时间
	 *
	 * @return
	 */
	public static String currentTime(String format) {
		if (StringUtils.isBlank(format)) {
			return format(new Date(), DATE_TIME);
		} else {
			return format(new Date(), format);
		}
	}

	/**
	 * 获取前一天日期
	 *
	 * @param date
	 * @return
	 */
	public static Date getBeforeDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 获取下一天
	 *
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}


	public static void main(String[] args) {
		Date date = DateUtil.parseDate("2019/01/11 01:01:11");
		System.out.println(date);
	}

}