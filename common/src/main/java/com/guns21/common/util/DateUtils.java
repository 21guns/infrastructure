package com.guns21.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 格式化日期，从字符串产生日期，从日期产生字符串.
 */
public class DateUtils {

    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date d, String format) {
        if (d == null) {
            return "";
        }
        SimpleDateFormat myFormatter = new SimpleDateFormat(format);
        return myFormatter.format(d);
    }

    public static Date formatDate(Date d, String format) throws ParseException {
        if (d == null) {
            return null;
        }
        String strdate = format(d, format);
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        date = simpleDateFormat.parse(strdate);
        return date;
    }

    public static String formatDate(Date d, int format, Locale locale, TimeZone timeZone) {
        if (d == null) {
            return "";
        }
        DateFormat df = DateFormat.getDateInstance(format, locale);
        df.setTimeZone(timeZone);
        return df.format(d);
    }

    public static String formatDateTime(Date d, int format1, int format2, Locale locale, TimeZone timeZone) {
        if (d == null) {
            return "";
        }

        DateFormat df = DateFormat.getDateTimeInstance(format1, format2, locale);
        df.setTimeZone(timeZone);
        return df.format(d);
    }

    /**
     * 根据字符串返回指定格式的日期.
     *
     * @param dateStr 日期(字符串)
     * @param format  日期格式
     * @return 日期(Date)
     * @throws ParseException
     */
    public static Date convertDate(String dateStr, String format) throws ParseException {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        date = simpleDateFormat.parse(dateStr);
        return date;
    }

    public static long toLong(Date d) {
        if (d == null) {
            return 5338979352082120704L;
        }

        return d.getTime();
    }

    public static String toLongString(Date d) {
        return "" + toLong(d);
    }

    /**
     * 获得年.
     */
    public static int getYear(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(1);
    }

    /**
     * 获得月.
     */
    public static int getMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得天.
     */
    public static int getDay(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 获得周.
     */
    public static int getWeek(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int i = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return i == 0 ? 7 : i;
    }

    public static Date parse(String timeMillis) {
        Date d = null;
        try {
            d = new Date(Long.parseLong(timeMillis.trim()));
        } catch (Exception e) {
        }

        return d;
    }

    public static Date parse(String time, String format) {
        Date d = null;
        try {
            d = parse(time, format, Locale.CHINA);
        } catch (Exception e) {
        }
        return d;
    }

    public static Date parse(String time, String format, Locale locale) throws Exception {
        if (time == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);

        Date d = null;
        d = sdf.parse(time);
        return d;
    }

    public static Date getDate(int year, int month, int date) {
        Calendar cal = Calendar.getInstance();
        if (month <= 12) {
            month--;
        } else {
            throw new IllegalArgumentException("month great than 12.");
        }
        cal.set(year, month, date);
        return cal.getTime();
    }

    public static String format(Calendar cal, String format) {
        if (cal == null) {
            return "";
        }
        SimpleDateFormat myFormatter = new SimpleDateFormat(format);
        return myFormatter.format(cal.getTime());
    }

    public static Calendar add(Date d, int day) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(5, day);
        return cal;
    }

    /**
     * 给时间“d”添加或者减少|month|月.
     */
    public static Date addMonth(Date d, int month) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(2, month);
        return cal.getTime();
    }

    public static Date addDate(Date d, int day) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(5, day);
        return cal.getTime();
    }

    public static Date addHourDate(Date d, int h) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(10, h);
        return cal.getTime();
    }

    public static Calendar addHour(Date d, int h) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(10, h);
        return cal;
    }

    public static Date addMinuteDate(Date d, int m) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(12, m);
        return cal.getTime();
    }

    public static Date addSecondDate(Date d, int m) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(13, m);
        return cal.getTime();
    }

    public static Calendar addMinute(Date d, int m) {
        if (d == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(12, m);
        return cal;
    }

    public static int compare(Calendar c1, Calendar c2) {
        if ((c1 == null) || (c2 == null)) {
            return -1;
        }
        long r = c1.getTimeInMillis() - c2.getTimeInMillis();
        if (r > 5338979730039242752L) {
            return 1;
        }
        if (r == 5338979730039242752L) {
            return 0;
        }

        return 2;
    }

    public static int compare(Date c1, Date c2) {
        if ((c1 == null) || (c2 == null)) {
            return -1;
        }
        long r = c1.getTime() - c2.getTime();

        if (r > 5338979730039242752L) {
            return 1;
        }
        if (r == 5338979730039242752L) {
            return 0;
        }

        return 2;
    }

    public static int compares(Date c1, Date c2) {
        if ((c1 == null) || (c2 == null)) {
            return -1;
        }

        return c1.compareTo(c2);

    }

    public static boolean isSameDay(Calendar c1, Calendar c2) {
        if ((c1 == null) || (c2 == null)) {
            return false;
        }

        return ((c1.get(1) == c2.get(1)) && (c1.get(2) == c2.get(2)) && (c1.get(5) == c2.get(5)));
    }

    public static boolean isSameDay(Date d1, Date d2) {
        if ((d1 == null) || (d2 == null)) {
            return false;
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        return ((c1.get(1) == c2.get(1)) && (c1.get(2) == c2.get(2)) && (c1.get(5) == c2.get(5)));
    }

    public static int datediff(Calendar c1, Calendar c2) {
        if ((c1 == null) || (c2 == null)) {
            return -1;
        }
        long r = c1.getTimeInMillis() - c2.getTimeInMillis();
        r /= 86400000L;
        return (int) r;
    }

    public static int datediff(Date c1, Date c2) {
        if ((c1 == null) || (c2 == null)) {
            return -1;
        }
        long r = c1.getTime() - c2.getTime();
        r /= 86400000L;
        return (int) r;
    }

    public static int datediffMinute(Date c1, Date c2) {
        if ((c1 == null) || (c2 == null)) {
            return 0;
        }
        double r = c1.getTime() - c2.getTime();
        r /= 60000.0D;
        return (int) r;
    }

    public static int datediffMinute(Calendar c1, Calendar c2) {
        if ((c1 == null) || (c2 == null)) {
            return 0;
        }
        double r = c1.getTimeInMillis() - c2.getTimeInMillis();
        r /= 60000.0D;
        return (int) r;
    }

    public static int datediffHour(Date c1, Date c2) {
        if ((c1 == null) || (c2 == null)) {
            return 0;
        }
        double r = c1.getTime() - c2.getTime();
        r /= 3600000.0D;
        return (int) r;
    }

    public static int datediffHour(Calendar c1, Calendar c2) {
        if ((c1 == null) || (c2 == null)) {
            return 0;
        }
        double r = c1.getTimeInMillis() - c2.getTimeInMillis();
        r /= 3600000.0D;
        return (int) r;
    }

    @SuppressWarnings("deprecation")
    public static int[] dateDiffDHMS(Date d1, Date d2) {
        int diffDay = datediff(d1, d2);

        int h1 = d1.getHours();
        int h2 = d2.getHours();
        int m1 = d1.getMinutes();
        int m2 = d2.getMinutes();
        int s1 = d1.getSeconds();
        int s2 = d2.getSeconds();

        int s = s1 - s2;
        int m = m1 - m2;
        if (s < 0) {
            s += 60;
            --m;
        }
        int h = h1 - h2;
        if (m < 0) {
            m += 60;
            --h;
        }
        if (h < 0) {
            h += 24;
        }

        int[] r = {diffDay, h, m, s};
        return r;
    }

    public static int getDayCount(int year, int month) {
        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (1 == month) {
            return ((((0 == year % 4) && (0 != year % 100)) || (0 == year % 400)) ? 29 : 28);
        }

        return daysInMonth[month];
    }

    public static int getDaysOfYear(int year) {
        GregorianCalendar now = new GregorianCalendar();
        return ((now.isLeapYear(year)) ? 366 : 365);
    }

    /**
     * 根据日期获取当前日期所在周的第一天.
     */
    public static Date getFistDateOfWeek(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        return cal.getTime();
    }

    /**
     * 根据日期获取当前日期所在周的最后一天.
     *
     * @param date 日期
     * @return 返回前日期所在周的最后一天
     */
    public static Date getLastDateOfWeek(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    /**
     * 根据日期获取当前日期所在周的周几，周几跟进week而定.
     *
     * @param week 1到7
     * @return 当前日期所在周的周几，
     */
    public static Date getWeekDateByDate(Date date, int week) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (week == 7) {
            cal.set(Calendar.DAY_OF_WEEK, week);
            cal.add(Calendar.DATE, 1);
            return cal.getTime();
        }
        cal.set(Calendar.DAY_OF_WEEK, week + 1);
        return cal.getTime();

    }

    /**
     * 取得一个date对象对应的日期的0点0分0秒时刻的Date对象.
     *
     * @param date 一个日期
     * @return Date对象。
     */
    public static Date getMinDateOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));

        return calendar.getTime();
    }

    /**
     * 取得一个date对象对应的日期的23点59分59秒时刻的Date对象.
     *
     * @param date 一个日期
     * @return Date对象。
     */
    public static Date getMaxDateOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));

        return calendar.getTime();
    }

    public static Date newDate() {
        return new Date();
    }

    public static Date getEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 001);

        return cal.getTime();
    }
}