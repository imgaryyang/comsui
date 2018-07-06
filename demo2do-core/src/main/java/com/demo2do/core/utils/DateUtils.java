/**
 * 
 */
package com.demo2do.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date related utilities
 * 
 * @author Downpour
 */
public abstract class DateUtils {

    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public static final long DAY_IN_MILLISECOND = 24 * 60 * 60 * 1000;
    
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date asDay(Date date) {
		return parseDate(format(date, "yyyy-MM-dd"), "yyyy-MM-dd");
	}
	
	/**
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date asDay(String dateString) {
		return parseDate(dateString, "yyyy-MM-dd");
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date asMonth(Date date) {
		return parseDate(format(date, "yyyy-MM"), "yyyy-MM");
	}
	
	/**
	 *
     * @param dateString
     * @return
	 */
	public static Date asMonth(String dateString) {
		return parseDate(dateString, "yyyy-MM");
	}

	/**
	 * 
	 * @return
	 */
	public static String today() {
		return format(new Date());
	}
	
	/**
	 * 
	 * @return
	 */
	public static String monthAsString() {
		return format(new Date(), "yyyy-MM");
	}
	
	/**
	 * 
	 * @return
	 */
	public static String nextMonthAsString() {
		return format(DateUtils.addMonths(new Date(), 1), "yyyy-MM");
	}
	
    /**
     * Do a wrap of {@link org.apache.commons.lang.time.DateUtils#parseDate(String, String[])} 
     *
     * @param dateString
     * @param pattern
     * @return
     * 
     * @see {@link org.apache.commons.lang.time.DateUtils#parseDate(String, String[])}
     */
    public static Date parseDate(String dateString, String pattern) {
        return parseDate(dateString, new String[] { pattern });
    }

    /**
     * Do a wrap of {@link org.apache.commons.lang.time.DateUtils#parseDate(String, String[])}
     *
     * @param dateString
     * @param patterns
     * @return
     * 
     * @see {@link org.apache.commons.lang.time.DateUtils#parseDate(String, String[])}
     */
    public static Date parseDate(String dateString, String[] patterns) {
        try {
            return org.apache.commons.lang.time.DateUtils.parseDate(dateString, patterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Parse date using DEFAULT_DATE_PATTERN
     * 
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static Date parseDate(int year, int month, int date) {
        return parseDate(format(year, month, date), DEFAULT_DATE_PATTERN);
    }

    /**
     * Format date according to year, month and date to DEFAULT_DATE_PATTERN
     * 
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static String format(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    /**
     * Format date according to year, month and date to yyyy-MM
     * 
     * @param year
     * @param month
     * @return
     */
    public static String format(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
    }
    
    /**
     * Format date according to input date to DEFAULT_DATE_PATTERN
     * 
     * @param date
     * @return
     */
    public static String format(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
          calendar.setTime(date);
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
    

    /**
     * Format date according to input date to DEFAULT_DATE_PATTERN
     * 
     * @param date
     * @return
     */
    public static String format(Date date, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

    /**
     * 
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
    	return org.apache.commons.lang.time.DateUtils.addDays(date, days);
    }
    
    /**
     * 
     * @param date
     * @param weeks
     * @return
     */
    public static Date addWeeks(Date date, int weeks) {
    	return org.apache.commons.lang.time.DateUtils.addWeeks(date, weeks);
    }
    
    /**
     * 
     * @param date
     * @param months
     * @return
     */
    public static Date addMonths(Date date, int months) {
    	return org.apache.commons.lang.time.DateUtils.addMonths(date, months);
    }
    
    /**
     * Get current time millis as a string
     * 
     * @return
     */
    public static String getCurrentTimeMillis() {
        return String.valueOf(System.currentTimeMillis());
    }
    
    /**
     * Get first month day
     * 
     * @param date
     * @return
     */
    public static Date getFirstMonthDay(Date date) {
    	
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    /**
     * Get first month day for calendar.  There are differences between the first day of the month and the first day for calendar.
     * 
     * eg. First day for calendar for 2008.5.18 is 2008.4.27
     * 
     * @param date
     * @return
     */
    public static Date getFirstMonthDayForCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Set first day of week is Sunday
        calendar.setFirstDayOfWeek(0);

        calendar.add(Calendar.DAY_OF_YEAR, -(calendar.get(Calendar.DAY_OF_WEEK) - 1));
        return calendar.getTime();
    }

    /**
     * Get last month day
     * 
     * @param date
     * @return
     */
    public static Date getLastMonthDay(Date date) {

    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }
    
    /**
     * Get last month day for calendar.  There are differences between the last day of the month and the last day for calendar.
     * 
     * eg. Last day for calendar for 2008.6.18 is 2008.7.5
     * 
     * @param date
     * @return
     */
    public static Date getLastMonthDayForCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        // Set first day of week is Sunday
        calendar.setFirstDayOfWeek(0);

        calendar.add(Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK - calendar.get(Calendar.DAY_OF_WEEK));
        return calendar.getTime();
    }

    /**
     * Get the days of month
     * 
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get this year
     * 
     * @return
     */
    public static int year() {
    	return year(new Date());
    }
    
    /**
     * Get year of the date
     * 
     * @param date
     * @return
     */
    public static int year(Date date) {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    
    /**
     * Get year of the date
     * 
     * @param date
     * @return
     */
    public static String getYear(Date date) {
        return String.valueOf(year(date));
    }
    
    /**
     * Get this month
     * 
     * @return
     */
    public static int month() {
    	return month(new Date());
    }
    
    /**
     * Get month of the date
     * 
     * @param date
     * @return
     */
    public static int month(Date date) {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }
    
    /**
     * 
     * @return
     */
    public static int day() {
    	return day(new Date());
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static int day(Date date) {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * Get month of the date
     * 
     * @param date
     * @return
     */
    public static String getMonth(Date date) {
        int month = month(date);
        return month < 10 ? "0" + month : "" + month;
    }

    /**
     * Get day of the date
     * 
     * @param date
     * @return
     */
    public static String getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.DATE));
    }
    
    /**
     * 
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date) {
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek == 0 || dayOfWeek == 6; 
    }

    /**
     * To determine whether two date are the same day
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
    
    /**
     * To determine whether two date are the same month
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
    }

    /**
     * Get interval days between start and end
     * 
     * @param start
     * @param end
     * @return
     */
    public static List<Date> getIntervalDays(Date start, Date end) {
        List<Date> dates = new ArrayList<Date>();
        if(start.after(end)) {
            throw new IllegalArgumentException("The start date must not be after end date");
        } else {
            for(Date date = start; !isSameDay(date, end); date = org.apache.commons.lang.time.DateUtils.addDays(date, 1)) {
                dates.add(date);
            }
            dates.add(end);
        }
        return dates;
    }

	/**
	 * compare the two dates, and return the subtraction between d1 and d2(d1 - d2)
	 * result > 0 when d1 > d2 and result < 0 when d1 < d2 
	 * 
	 * @param start
	 *            Date
	 * @param end
	 *            Date
	 * @return int
	 */
	public static int compareTwoDatesOnDay(Date start, Date end) {
		if(start.getTime() == end.getTime())
			return 0;
		start = org.apache.commons.lang.time.DateUtils.truncate(start, Calendar.DATE);
		end = org.apache.commons.lang.time.DateUtils.truncate(end, Calendar.DATE);
		long l1 = start.getTime();
		long l2 = end.getTime();
		return (int)((l1 - l2) / DAY_IN_MILLISECOND);
	}
	
    /**
     * Get interval months between start and end
     * 
     * @param start
     * @param end
     * @return
     */
    public static List<Date> getIntervalMonths(Date start, Date end) {
        List<Date> dates = new ArrayList<Date>();
        if(start.after(end)) {
            throw new IllegalArgumentException("The start date must not be after end date");
        } else {
            for(Date date = start; !isSameMonth(date, end); date = org.apache.commons.lang.time.DateUtils.addMonths(date, 1)) {
                dates.add(date);
            }
            dates.add(end);
        }
        return dates;
    }
    
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return calendar.getTime();
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		return calendar.getTime();
	}

    /**
     * 得到昨天的日期
     */
    public static Date getYesterday(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }
}
