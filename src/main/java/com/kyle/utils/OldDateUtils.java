package com.kyle.utils;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author carroll
 * @Date 2017-07-25 18:06
 **/
public class OldDateUtils {
    /**
     * 年月日时间格式化
     */
    public static final String YMD_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 年月日时分秒时间格式化
     */
    public static final String YMD_HMS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 年月日时分秒时间格式化
     */
    public static final String YMD_HMS_DATE_SIMPLE_FORMAT = "yyyyMMddHHmmss";
    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static final String MONTH = "month";

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getNowDate() {
        return getStrDateFormat(new Date(), YMD_HMS_DATE_FORMAT);
    }

    public static String getNowDate(String format) {
        return getStrDateFormat(new Date(), format);
    }

    /**
     * 使用预设格式将字符串转为Date
     */
    public static Date parse(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD_DATE_FORMAT);
        return sdf.parse(strDate);
    }

    /**
     * 使用参数Format将字符串转为Date
     */
    public static Date parse(String strDate, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(strDate);
    }

    public static Date convertStrToDate(String strDate, String format) throws ParseException {
        if (format.equals(YMD_HMS_DATE_SIMPLE_FORMAT)) {
            String reg = "^\\d{14}$";
            if (!strDate.matches(reg)) {
                ParseException e = new ParseException("时间格式错误", 0);
                e.fillInStackTrace();
                throw e;
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.parse(strDate);
    }

    public static String getStrTimeFormat(Date date) {
        return getStrDateFormat(date, YMD_HMS_DATE_FORMAT);
    }


    /**
     * 获取昨天
     *
     * @return
     */
    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Date getDate(Date dateTime, String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            if (null != dateTime) {
                return dateFormat.parse(getStrDateFormat(dateTime, format));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取时间格式化的字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String getStrDateFormat(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        if (null != date) {
            return dateFormat.format(date);
        } else {
            return "";
        }
    }

    /**
     * 计算两个日期的相差多少年
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getToDate(Date d1, Date d2) {
        long d = (d2.getTime() - d1.getTime());
        return d / 86400000 / 365;
    }

    /**
     * 根据开始时间和年限 计算结束时间
     *
     * @param d1 开始时间
     * @param nx 年限
     * @return endDate
     */
    public static Date getEndDate(Date d1, BigDecimal nx) {
        Calendar c = Calendar.getInstance();
        c.setTime(d1);
        c.add(Calendar.YEAR, nx.intValue());
        return c.getTime();
    }

    /**
     * 根据日期计算距离今天的时间
     *
     * @param d
     * @return
     */
    public static long getDaysFromNow(Date d) {
        Date now = new Date();
        long day = d.getTime() - now.getTime();
        day = day / 86400000;
        if (day < 0) {
            day = 0;
        }
        return day;
    }

    /**
     * 获取距离当前时间的天数,向上取整
     *
     * @param date
     * @return
     */
    public static int getDifferentDaysFromNow(Date date) {
        return (int) ((new Date().getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     *
     * @param date     日期
     * @param dateType 类型
     * @param amount   数值
     * @return 计算后日期
     */
    public static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }
        return myDate;
    }

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            return compare_date(dt1, dt2);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static int compare_date(Date DATE1, Date DATE2) {
        Date dt1 = DATE1;
        Date dt2 = DATE2;
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return df.format(c.getTime());
    }

    /**
     * 获得前N周周一日期
     *
     * @param last 前N周,为0时为当前周一
     * @return yyyy-MM-dd
     */
    public static String getLastMondays(Integer last) {
        Calendar cal = Calendar.getInstance();
        //n为推迟的周数，0本周，1向前推迟一周，依次类推
        int n = last;
        String monday;
        cal.add(Calendar.DATE, -n * 7);
        //想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        monday = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return monday;
    }

    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(final Date date) {
        return isTheDay(date, new Date());
    }

    /***
     * 判断时间是否在今天之前
     *
     * @param date
     * @return
     */
    public static boolean isBeforeToday(final Date date) {
        return date.getTime() < dayBegin(new Date()).getTime();
    }

    /**
     * 是否是指定日期
     *
     * @param date
     * @param day
     * @return
     */
    public static boolean isTheDay(final Date date, final Date day) {
        return date.getTime() >= dayBegin(day).getTime()
                && date.getTime() <= dayEnd(day).getTime();
    }

    /**
     * 获取指定时间的那天 00:00:00.000 的时间
     *
     * @param date
     * @return
     */
    public static Date dayBegin(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * 获取指定时间的那天 23:59:59.999 的时间
     *
     * @param date
     * @return
     */
    public static Date dayEnd(final Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }


    /**
     * 获取当前日期是一年的第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }


    /**
     * 得到某年某周的第一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, week);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c.getTime();
    }

    /**
     * 得到某年某周的最后一天
     *
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.WEEK_OF_YEAR, week);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        return c.getTime();

    }

    /**
     * 取得指定日期所在周的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }

    /**
     * 获取指定时间差的目标日期
     *
     * @param dateString 原日期 yyyy-MM-dd字符串格式
     * @param dateDiff   时间差
     * @param chronoUnit 单位
     * @return yyyy-MM-dd 以字符串表示的目标日期
     */
    public static String getTargetDate(String dateString, int dateDiff, ChronoUnit chronoUnit) {
        LocalDate sourceDate = LocalDate.parse(dateString);
        LocalDate targetDate = sourceDate.plus(dateDiff, chronoUnit);
        String month = targetDate.getMonthValue() < 10 ? "0" + targetDate.getMonthValue() : "" + targetDate.getMonthValue();
        String day = targetDate.getDayOfMonth() < 10 ? "0" + targetDate.getDayOfMonth() : "" + targetDate.getDayOfMonth();
        return targetDate.getYear() + "-" + month + "-" + day;
    }

    /**
     * 取得指定日期所在周的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
        return c.getTime();
    }

    /**
     * 获取指定日期月第一天
     *
     * @param date 指定日期
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return c.getTime();
    }

    /**
     * 获取指定日期月最后一天
     *
     * @param date 指定日期
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 获取指定日期年最后一天日期
     *
     * @param date 指定日期
     * @return Date
     */
    public static Date getLastDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 获取指定日期年第一天日期
     *
     * @param date 指定日期
     * @return Date
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return YMD_DATE_FORMAT;
    }

    /**
     * /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD_DATE_FORMAT);
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     */
    public static long daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD_DATE_FORMAT);
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        return (time2 - time1) / (1000 * 3600 * 24);
    }

    /**
     * 相差月份
     *
     * @param d1 结束时间
     * @param d2 开始时间
     * @return
     */
    public static long getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if (c1.getTimeInMillis() < c2.getTimeInMillis()) {
            return 0;
        }
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
        int yearInterval = year1 - year2;
        /** 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数 */
        if (month1 < month2 || month1 == month2 && day1 < day2) {
            yearInterval--;
        }
        /** 获取月数差值 */
        int monthInterval = (month1 + 12) - month2;
        if (day1 < day2) {
            monthInterval--;
        }
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }

    /**
     * 日期相差数 (支持天，月，周)
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param datetype  日期类型
     * @return
     */
    public static int timeBettwen(String startTime, String endTime, String datetype) {
        //天数
        int days = 0;
        int weeks = 0;
        int months = 0;
        try {
            //时间转换类
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(startTime);
            Date date2 = sdf.parse(endTime);
            Calendar can1 = Calendar.getInstance();
            can1.setTime(date1);
            Calendar can2 = Calendar.getInstance();
            can2.setTime(date2);
            int year1 = can1.get(Calendar.YEAR);
            int year2 = can2.get(Calendar.YEAR);
            Calendar can = null;
            if (can1.before(can2)) {
                days -= can1.get(Calendar.DAY_OF_YEAR);
                days += can2.get(Calendar.DAY_OF_YEAR);
                weeks -= can1.get(Calendar.WEEK_OF_YEAR);
                weeks += can2.get(Calendar.WEEK_OF_YEAR);
                months -= can1.get(Calendar.MONTH);
                months += can2.get(Calendar.MONTH);
                can = can1;
            } else {
                days -= can2.get(Calendar.DAY_OF_YEAR);
                days += can1.get(Calendar.DAY_OF_YEAR);
                weeks -= can2.get(Calendar.WEEK_OF_YEAR);
                weeks += can1.get(Calendar.WEEK_OF_YEAR);
                months -= can2.get(Calendar.MONTH);
                months += can1.get(Calendar.MONTH);
                can = can2;
            }
            for (int i = 0; i < Math.abs(year2 - year1); i++) {
                days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
                weeks += can.getActualMaximum(Calendar.WEEK_OF_YEAR);
                months += can.getActualMaximum(Calendar.MONTH) + 1;
                can.add(Calendar.YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (datetype.equals(DAY)) {
            return days;
        } else if (datetype.equals(WEEK)) {
            return weeks;
        } else if (datetype.equals(MONTH)) {
            return months;
        }
        return 0;
    }

    /**
     * 检查某一天是否是周日
     *
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static boolean checkIsSunDay(String strDate) throws ParseException {
        Date date = convertStrToDate(strDate, YMD_DATE_FORMAT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * 检查某一天是否是月末
     *
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static boolean checkIsMonthEnd(String strDate) throws ParseException {
        Date date = convertStrToDate(strDate, YMD_DATE_FORMAT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        return calendar.get(Calendar.DAY_OF_MONTH) == 1;
    }

    /**
     * 检查某一天是否是年末
     *
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static boolean checkIsYearEnd(String strDate) throws ParseException {
        Date date = convertStrToDate(strDate, YMD_DATE_FORMAT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        return calendar.get(Calendar.DAY_OF_MONTH) == 1 && calendar.get(Calendar.MONTH) == 0;
    }

    /**
     * 获取指定日期的最小时间
     *
     * @param date
     * @return
     */
    public static Date getDateMinTime(Date date) {
        if (date == null) {
            return null;
        }
        return getDate(date, YMD_DATE_FORMAT);
    }

    /**
     * 获取指定日期最大时间
     *
     * @param date
     * @return
     */
    public static Date getDateMaxTime(Date date) {
        if (date == null) {
            return null;
        }
        try {
            Date result = parse(getStrDateFormat(date, YMD_DATE_FORMAT) + " 23:59:59.999", YMD_HMS_DATE_FORMAT + ".SSS");
            return result;
        } catch (ParseException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        getTargetDate("2017-06-14", -10, ChronoUnit.DAYS);
    }
}
