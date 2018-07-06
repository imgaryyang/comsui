package com.suidifu.munichre.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils extends com.demo2do.core.utils.DateUtils {
	
	public static Date getPreNeighbourHour(Date date){
		if(date==null){
			return null;
		}
		Date preHourTime = org.apache.commons.lang.time.DateUtils.addHours(date, -1);
		return org.apache.commons.lang.time.DateUtils.ceiling(preHourTime, Calendar.HOUR);
		
	}
	
	public static Date getNextNeighbourHour(Date date){
		if(date==null){
			return null;
		}
		
		return  org.apache.commons.lang.time.DateUtils.ceiling(date, Calendar.HOUR);
		
	}
	
	public static Date parseDate(String dateString){
		return parseDate(dateString,new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"});
	}
}
