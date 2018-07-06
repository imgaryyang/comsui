package com.suidifu.munichre.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class DateUtilsTest {

	@Test
	public void testgetNeighbourHour_1() {
		Date date = DateUtils.parseDate("2016-10-31 23:23:21", "yyyy-MM-dd HH:mm:ss");
		assertEquals("2016-10-31 23:00:00",DateUtils.format(DateUtils.getPreNeighbourHour(date),"yyyy-MM-dd HH:mm:ss"));
		assertEquals("2016-11-01 00:00:00",DateUtils.format(DateUtils.getNextNeighbourHour(date),"yyyy-MM-dd HH:mm:ss"));
		
	}
	
	@Test
	public void testgetNeighbourHour_2() {
		Date date = DateUtils.parseDate("2016-10-31 10:34:21", "yyyy-MM-dd HH:mm:ss");
		assertEquals("2016-10-31 10:00:00",DateUtils.format(DateUtils.getPreNeighbourHour(date),"yyyy-MM-dd HH:mm:ss"));
		assertEquals("2016-10-31 11:00:00",DateUtils.format(DateUtils.getNextNeighbourHour(date),"yyyy-MM-dd HH:mm:ss"));
		
	}
	
	@Test
	public void testgetNeighbourHour_3() {
		Date date = DateUtils.parseDate("2016-10-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		assertEquals("2016-10-01 00:00:00",DateUtils.format(DateUtils.getPreNeighbourHour(date),"yyyy-MM-dd HH:mm:ss"));
		assertEquals("2016-10-01 01:00:00",DateUtils.format(DateUtils.getNextNeighbourHour(date),"yyyy-MM-dd HH:mm:ss"));
	}

}
