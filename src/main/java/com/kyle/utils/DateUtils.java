package com.kyle.utils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author carroll
 * @Date 2017-07-25 18:06
 **/
public class DateUtils extends OldDateUtils {
    /** 年月日时间格式化*/
    public static final String YMD_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 时分秒时间格式化
     */
    public static final String HMS_TIME_FORMAT = "HH:mm:ss";

    /** 年月日时间格式化*/
    public static final String YMD_HMS_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(YMD_HMS_DATE_FORMAT);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(YMD_DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(HMS_TIME_FORMAT);

    /**
     * 获取当前日期
     * @return LocalDate
     */
    public static LocalDate getCurrentLocalDate() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间
     * @return LocalDateTime
     */
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     *
     * @return String
     */
    public static String getCurrentDate() {
        return getCurrentDate(YMD_DATE_FORMAT);
    }

    /**
     * 获取当前时间
     * @return String
     */
    public static String getCurrentDateTime() {
        return getCurrentDateTime(YMD_HMS_DATE_FORMAT);
    }

    /**
     * 根据指定的格式返回日期的String类型
     * @param format
     * @return String
     */
    public static String getCurrentDate(String format) {
        return getCurrentLocalDate().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 根据指定格式返回时间的String类型
     * @param format
     * @return String
     */
    public static String getCurrentDateTime(String format) {
        return getCurrentLocalDateTime().format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 返回LocalDate
     * @param strDate
     * @return LocalDate
     */
    public static LocalDate parseToLocalDate(String strDate) {
        return parseToLocalDate(strDate, DATE_FORMATTER);
    }

    /**
     * 返回LocalDate
     * @param strDate
     * @param dateTimeFormatter
     * @return LocalDate
     */
    public static LocalDate parseToLocalDate(String strDate, DateTimeFormatter dateTimeFormatter) {
        return LocalDate.parse(strDate, dateTimeFormatter);
    }

    /**
     * 返回LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate parseToLocalDate(Date date) {
        return parseToLocalDateTime(date).toLocalDate();
    }

    /**
     * 返回LocalTime
     *
     * @param strTime
     * @return LocalTime
     */
    public static LocalTime parseToLocalTime(String strTime) {
        return parseToLocalTime(strTime, TIME_FORMATTER);
    }

    /**
     * 返回LocalTime
     *
     * @param strTime
     * @param dateTimeFormatter
     * @return LocalTime
     */
    public static LocalTime parseToLocalTime(String strTime, DateTimeFormatter dateTimeFormatter) {
        return LocalTime.parse(strTime, dateTimeFormatter);
    }

    /**
     * 返回LocalTime
     *
     * @param date
     * @return
     */
    public static LocalTime parseToLocalTime(Date date) {
        return parseToLocalDateTime(date).toLocalTime();
    }

    /**
     * 返回LocalDateTime
     * @param strDate
     * @return LocalDateTime
     */
    public static LocalDateTime parseToLocalDateTime(String strDate) {
        return parseToLocalDateTime(strDate, DATE_TIME_FORMATTER);
    }

    /**
     * 返回LocalDateTime
     * @param strDate
     * @param dateTimeFormatter
     * @return LocalDateTime
     */
    public static LocalDateTime parseToLocalDateTime(String strDate, DateTimeFormatter dateTimeFormatter) {
        return LocalDateTime.parse(strDate, dateTimeFormatter);
    }

    /**
     * 返回LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime parseToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 对某个日期进行计算获取特定的日期
     *
     * @param date
     * @param plus
     * @param chronoUnit
     * @return LocalDate
     */
    public static LocalDate addDate(LocalDate date, int plus, ChronoUnit chronoUnit){
        return date.plus(plus, chronoUnit);
    }

    /**
     * 对某个时间进行计算获取特定的时间
     *
     * @param dateTime
     * @param plus
     * @param chronoUnit
     * @return LocalDateTime
     */
    public static LocalDateTime addDateTime(LocalDateTime dateTime, int plus, ChronoUnit chronoUnit) {
        return dateTime.plus(plus, chronoUnit);
    }

    /**
     * 计算两个日期相差多少天
     * @param date1
     * @param date2
     * @return long
     */
    public static long getDifferentDays(LocalDate date1, LocalDate date2) {
        return date1.until(date2, ChronoUnit.DAYS);
    }

    /**
     * 计算两个日期相差多少周
     * @param date1
     * @param date2
     * @return long
     */
    public static long getDifferentWeek(LocalDate date1, LocalDate date2) {
        return date1.until(date2, ChronoUnit.WEEKS);
    }

    /**
     * 计算两个日期相差多少月
     * @param date1
     * @param date2
     * @return long
     */
    public static long getDifferentMonth(LocalDate date1, LocalDate date2) {
        return date1.until(date2, ChronoUnit.MONTHS);
    }

    /**
     * 计算两个日期相差多少年
     * @param date1
     * @param date2
     * @return long
     */
    public static long getDifferentYear(LocalDate date1, LocalDate date2) {
        return date1.until(date2, ChronoUnit.YEARS);
    }

    /**
     * 计算两个时间的秒数差
     *
     * @param startTime
     * @param endTime
     * @return long
     */
    public static long getSecondsBetween(LocalTime startTime, LocalTime endTime) {
        return startTime.until(endTime, ChronoUnit.SECONDS);
    }

    /**
     * 计算两个时间的秒数差
     *
     * @param startTime
     * @param endTime
     * @return long
     */
    public static long getSecondsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.until(endTime, ChronoUnit.SECONDS);
    }

    /**
     * 计算两个时间的分钟数差
     *
     * @param startTime
     * @param endTime
     * @return long
     */
    public static long getMinutesBetween(LocalTime startTime, LocalTime endTime) {
        return startTime.until(endTime, ChronoUnit.MINUTES);
    }

    /**
     * 计算两个时间的分钟数差
     *
     * @param startTime
     * @param endTime
     * @return long
     */
    public static long getMinutesBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return startTime.until(endTime, ChronoUnit.MINUTES);
    }

    /**
     * 比较两个日期大小
     * @param date1
     * @param date2
     * @return int
     */
    public static int compareDate(String date1, String date2) {
        return parseToLocalDate(date1).compareTo(parseToLocalDate(date2));
    }

    /**
     * 获取距离目标日期相差minusWeek周的某天（周一到周日）的日期
     * @param date
     * @param minusWeek
     * @param dayOfWeek
     * @return LocalDate
     */
    public static LocalDate getWeekDay(LocalDate date, long minusWeek, DayOfWeek dayOfWeek) {
        return date.minusWeeks(minusWeek).with(dayOfWeek);
    }

    /**
     * 两个日期是否为同一天
     * @param date1
     * @param date2
     * @return boolean
     */
    public static boolean isSameDay(LocalDate date1, LocalDate date2) {
        return date1.isEqual(date2);
    }

    /**
     * 获取目标日期是当年第几周
     * @param date
     * @return int
     */
    public static int getWeekOfYear(LocalDate date) {
        return date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
    }

    /**
     * 获取指定时间差的目标日期
     * @param dateString 原日期 yyyy-MM-dd字符串格式
     * @param dateDiff 时间差
     * @param chronoUnit 单位
     * @return yyyy-MM-dd 以字符串表示的目标日期
     */
    public static String getTargetDate(String dateString, int dateDiff, ChronoUnit chronoUnit) {
        LocalDate sourceDate = LocalDate.parse(dateString);
        LocalDate targetDate = sourceDate.plus(dateDiff, chronoUnit);
        return targetDate.format(DATE_FORMATTER);
    }

    /**
     * 获取指定日期月第一天
     * @param localDate
     * @return LocalDate
     */
    public static LocalDate getFirstDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取指定日期当月最后一天
     * @param localDate
     * @return LocalDate
     */
    public static LocalDate getLastDayOfMonth(LocalDate localDate) {
        return localDate.with(TemporalAdjusters.lastDayOfMonth());
    }


    /**
     * 返回2个日期相差天数
     * @param smdate
     * @param bdate
     * @return long
     * @throws ParseException
     */
    public static long getDifferentDays(String smdate, String bdate) throws ParseException{
        LocalDate localDate1 = LocalDate.parse(smdate);
        LocalDate localDate2 = LocalDate.parse(bdate);
        return getDifferentDays(localDate1, localDate2);
    }

    /**
     * 检查某一天是否是给定的星期（一、二、三等等）
     * @param strDate
     * @return boolean
     */
    public static boolean checkIsSomeDay(String strDate, DayOfWeek dayOfWeek) {
        LocalDate localDate = LocalDate.parse(strDate);
        return localDate.getDayOfWeek() == dayOfWeek;
    }

    /**
     * 判断指定日期是否是工作日（周一至周五）
     *
     * @param localDate
     * @return boolean
     */
    public static boolean checkIsWorkDay(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            return false;
        }

        return true;
    }

    /**
     * 检查某一天是否是月末
     * @param strDate
     * @return boolean
     */
    public static boolean checkIsMonthEnd(String strDate) {
        LocalDate localDate = LocalDate.parse(strDate);
        return localDate.with(TemporalAdjusters.lastDayOfMonth()).isEqual(localDate);
    }

    /**
     * 检查某一天是否是年末
     * @param strDate
     * @return boolean
     */
    public static boolean checkIsYearEnd(String strDate) {
        LocalDate localDate = LocalDate.parse(strDate);
        return localDate.with(TemporalAdjusters.lastDayOfYear()).isEqual(localDate);
    }

    /**
     * 时间格式化字符串
     *
     * @param dateTime
     * @return yyyy-MM-dd HH:mm:ss格式
     */
    public static String formatDateTimeToStr(LocalDateTime dateTime) {
        return formatDateTimeToStr(dateTime, YMD_HMS_DATE_FORMAT);
    }

    /**
     * 时间格式化字符串
     *
     * @param dateTime
     * @param format   自定义格式
     * @return String
     */
    public static String formatDateTimeToStr(LocalDateTime dateTime, String format) {
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 日期格式化字符串
     *
     * @param date
     * @return yyyy-MM-dd格式
     */
    public static String formatDateToStr(LocalDate date) {
        return formatDateToStr(date, YMD_DATE_FORMAT);
    }

    /**
     * 日期格式化字符串
     *
     * @param date
     * @param format 自定义日期格式
     * @return String
     */
    public static String formatDateToStr(LocalDate date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 时间格式化字符串
     *
     * @param time
     * @return HH:mm:ss格式
     */
    public static String formatTimeToStr(LocalTime time) {
        return formatTimeToStr(time, HMS_TIME_FORMAT);
    }

    /**
     * 时间格式化字符串
     *
     * @param date
     * @param format 自定义日期格式
     * @return String
     */
    public static String formatTimeToStr(LocalTime date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * LocalDate转时间戳
     * @param date
     * @return long
     */
    public static long parseDateToUnix(LocalDate date){
        return parseDateTimeToUnix(date.atStartOfDay());
    }

    /**
     * LocalDateTime转时间戳
     * @param dateTime
     * @return long
     */
    public static long parseDateTimeToUnix(LocalDateTime dateTime){
        return Timestamp.valueOf(dateTime).getTime();
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp
     * @return LocalDateTime
     */
    public static LocalDateTime parseUnixToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * 时间戳转LocalDate
     *
     * @param timestamp
     * @return LocalDate
     */
    public static LocalDate parseUnixToLocalDate(long timestamp) {
        return parseUnixToLocalDateTime(timestamp).toLocalDate();
    }

    /**
     * 时间戳转LocalTime
     *
     * @param timestamp
     * @return LocalTime
     */
    public static LocalTime parseUnixToLocalTime(long timestamp) {
        return parseUnixToLocalDateTime(timestamp).toLocalTime();
    }

    /**
     * 判断目标时间是否处于某个时间开区间内
     *
     * @param targetTime
     * @param beginTime
     * @param endTime
     * @return boolean
     */
    public static boolean isBetweenTimes(LocalTime targetTime, LocalTime beginTime, LocalTime endTime) {
        return targetTime.isBefore(endTime) && targetTime.isAfter(beginTime);
    }

    /**
     * 判断目标时间是否处于某个时间关区间内
     *
     * @param targetTime
     * @param beginTime
     * @param endTime
     * @return boolean
     */
    public static boolean isInTimes(LocalTime targetTime, LocalTime beginTime, LocalTime endTime) {
        return (targetTime.isBefore(beginTime) || targetTime.equals(beginTime))
                && (targetTime.isAfter(endTime) || targetTime.equals(endTime));
    }

    /**
     * 判断目标时间是否处于某个时间开区间内
     *
     * @param targetDateTime
     * @param beginDateTime
     * @param endDateTime
     * @return boolean
     */
    public static boolean isBetweenDateTimes(LocalDateTime targetDateTime, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        return targetDateTime.isBefore(beginDateTime) && targetDateTime.isAfter(endDateTime);
    }

    /**
     * 判断目标时间是否处于某个时间关区间内
     *
     * @param targetDateTime
     * @param beginDateTime
     * @param endDateTime
     * @return boolean
     */
    public static boolean isInDateTimes(LocalDateTime targetDateTime, LocalDateTime beginDateTime, LocalDateTime endDateTime) {
        return (targetDateTime.isBefore(beginDateTime) || targetDateTime.equals(beginDateTime))
                && (targetDateTime.isAfter(endDateTime) || targetDateTime.equals(endDateTime));
    }

    /**
     * 判断目标日期是否处于某个日期开区间内
     *
     * @param targetDate
     * @param beginDate
     * @param endDate
     * @return boolean
     */
    public static boolean isBetweenDate(LocalDate targetDate, LocalDate beginDate, LocalDate endDate) {
        return targetDate.isBefore(beginDate) && targetDate.isAfter(endDate);
    }

    /**
     * 判断目标时间是否处于某个时间关区间内
     *
     * @param targetDate
     * @param beginDate
     * @param endDate
     * @return boolean
     */
    public static boolean isInDate(LocalDate targetDate, LocalDate beginDate, LocalDate endDate) {
        return (targetDate.isBefore(beginDate) || targetDate.equals(beginDate))
                && (targetDate.isAfter(endDate) || targetDate.equals(endDate));
    }

    /**
     * 获取当天开始的时间戳
     *
     * @param targetDate
     * @return
     */
    public static long getBeginningOfTheDate(LocalDate targetDate) {
        return LocalDateTime.of(targetDate, LocalTime.MIN).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 获取当天结束的时间戳
     *
     * @param targetDate
     * @return
     */
    public static long getEndOfTheDate(LocalDate targetDate) {
        return LocalDateTime.of(targetDate, LocalTime.MAX).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }
}
