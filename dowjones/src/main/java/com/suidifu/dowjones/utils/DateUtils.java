package com.suidifu.dowjones.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/8 <br>
 * @time: 13:55 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
public class DateUtils {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_FORMAT_YYYYMM = "yyyyMM";
    private static final String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final long DAY_IN_MILLISECOND = 24 * 60 * 60 * 1000;


    /**
     * 构造方法
     */
    private DateUtils() {
    }

    public static String getDateFormatYYMMDD(Date time) {
        return new SimpleDateFormat(DATE_FORMAT).format(time);
    }

    public static String getDateFormatYYMM(Date time) {
        return new SimpleDateFormat(DATE_FORMAT_YYYYMM).format(time);
    }

    public static String transferYYYYMMDDHHmmss2YYYYMMDD(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        try {
            Date tmp = new SimpleDateFormat(LONG_DATE_FORMAT).parse(time);
            return new SimpleDateFormat(DATE_FORMAT).format(tmp);
        } catch (ParseException ignored) {

        }
        return null;
    }

    public static String getYesterdayFormatYYMMDD() {
        return new SimpleDateFormat(DATE_FORMAT).format(yesterday());
    }

    public static String getDateFormatYYMMDDHHMMSS(Date time) {
        return new SimpleDateFormat(LONG_DATE_FORMAT).format(time);
    }

    public static Date yesterday() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.add(Calendar.DATE, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static int getDayFromDate(String time) {
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT).parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
            return 0;
        }
    }

    /**
     * 获得指定日期的前一天
     *
     * @param date 指定日期
     * @return 指定日期前一天，以"yyyy-MM-dd"格式显示
     */
    public static String convertBeforeDate2String(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        return DateFormatUtils.format(c, Constants.DATE_PATTERN);
    }

    /**
     * 获得指定日期的前一天
     *
     * @param date 指定日期
     * @return 指定日期前一天，以"yyyy-MM-dd"格式显示
     */
    public static String convertBeforeDate2String(String date) {

        Date parseDate = null;
        try {
            parseDate = org.apache.commons.lang3.time.DateUtils.parseDate(date, Constants.DATE_PATTERN);
        } catch (ParseException e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
        }
        return convertBeforeDate2String(parseDate);
    }


    /**
     * 获得指定日期的后一天
     *
     * @param date 指定日期
     * @return 指定日期前一天，以"yyyy-MM-dd"格式显示
     */
    public static String convertNextDate2String(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(Objects.requireNonNull(date));
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        return DateFormatUtils.format(c, Constants.DATE_PATTERN);
    }


    /**
     * 获得指定日期的后一天
     *
     * @param date 指定日期
     * @return 指定日期后一天，以"yyyy-MM-dd"格式显示
     */
    public static String convertNextDate2String(String date) {
        Date parseDate = null;
        try {
            parseDate = org.apache.commons.lang3.time.DateUtils.parseDate(date, Constants.DATE_PATTERN);
        } catch (ParseException e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
        }
        return convertNextDate2String(parseDate);
    }

    public static String getFileDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        return DateFormatUtils.format(c, Constants.FILE_DATE_PATTERN);
    }

    public static String getFileDate(String date) {
        Date parseDate = null;
        try {
            parseDate = org.apache.commons.lang3.time.DateUtils.parseDate(date, Constants.DATE_PATTERN);
        } catch (ParseException e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
        }

        Calendar c = Calendar.getInstance();
        c.setTime(parseDate);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day);

        return DateFormatUtils.format(c, Constants.FILE_DATE_PATTERN);
    }

    public static String convertDate2String(Date date) {
        return DateFormatUtils.format(date, Constants.DATE_PATTERN);
    }

    public static Date getDateFrom(String date, String format) {
        if (StringUtils.isEmpty(date)){
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException ignored) {
            return null;
        }
    }
    /**
     * 加几天
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        return org.apache.commons.lang.time.DateUtils.addDays(date, days);
    }

    /**
     * 减几天
     * @param date
     * @param days
     * @return
     */
    public static Date substractDays(Date date, int days) {
        return org.apache.commons.lang.time.DateUtils.addDays(date, -days);
    }

    public static int compareTwoDatesOnDay(Date start, Date end) {
        if(start.getTime() == end.getTime())
            return 0;
        start = org.apache.commons.lang.time.DateUtils.truncate(start, Calendar.DATE);
        end = org.apache.commons.lang.time.DateUtils.truncate(end, Calendar.DATE);
        long l1 = start.getTime();
        long l2 = end.getTime();
        return (int)((l1 - l2) / DAY_IN_MILLISECOND);
    }

    public static int distanceDaysBetween(Date startDate,Date endDate){
        return Math.abs(compareTwoDatesOnDay(startDate, endDate));
    }
}