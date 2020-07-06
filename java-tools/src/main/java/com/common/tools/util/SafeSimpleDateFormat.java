package com.common.tools.util;

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 此类实现线程安全（可重入）SimpleDateFormat
 * 类。它通过使用持有Map的ThreadLocal来做到这一点
 * 将SimpleDateFormat保留在ThreadLocal中的传统方法。
 *
 * 每个ThreadLocal都包含一个包含SimpleDateFormats（键控）的HashMap。
 * 每个新的SimpleDateFormat的字符串格式（例如“ yyyy / MM / dd”等）
 * 在线程执行上下文中创建的实例。
 *
 */
public class SafeSimpleDateFormat extends Format
{
    private final String _format;
    private static final ThreadLocal<Map<String, SimpleDateFormat>> _dateFormats = new ThreadLocal<Map<String, SimpleDateFormat>>()
    {
        public Map<String, SimpleDateFormat> initialValue()
        {
            return new ConcurrentHashMap<>();
        }
    };

    public static SimpleDateFormat getDateFormat(String format)
    {
        Map<String, SimpleDateFormat> formatters = _dateFormats.get();
        SimpleDateFormat formatter = formatters.get(format);
        if (formatter == null)
        {
            formatter = new SimpleDateFormat(format);
            formatters.put(format, formatter);
        }
        return formatter;
    }

    public SafeSimpleDateFormat(String format)
    {
        _format = format;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    {
        return getDateFormat(_format).format(obj, toAppendTo, pos);
    }

    @Override
    public Object parseObject(String source, ParsePosition pos)
    {
        return getDateFormat(_format).parse(source, pos);
    }

    public Date parse(String day) throws ParseException
    {
        return getDateFormat(_format).parse(day);
    }

    public void setTimeZone(TimeZone tz)
    {
        getDateFormat(_format).setTimeZone(tz);
    }

    public void setCalendar(Calendar cal)
    {
        getDateFormat(_format).setCalendar(cal);
    }

    public void setNumberFormat(NumberFormat format)
    {
        getDateFormat(_format).setNumberFormat(format);
    }

    public void setLenient(boolean lenient)
    {
        getDateFormat(_format).setLenient(lenient);
    }

    public void setDateFormatSymbols(DateFormatSymbols symbols)
    {
        getDateFormat(_format).setDateFormatSymbols(symbols);
    }

    public void set2DigitYearStart(Date date)
    {
        getDateFormat(_format).set2DigitYearStart(date);
    }
}