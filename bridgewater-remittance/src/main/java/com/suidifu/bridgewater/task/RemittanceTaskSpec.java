package com.suidifu.bridgewater.task;

public class RemittanceTaskSpec {
	public final static String PARAM_LIMIT_SIZE = "limit";
	public final static int DEFAULT_LIMIT_SIZE = 100;
	public final static String PARAM_SLEEP_MILLIS = "loopSleep";
	public final static long DEFAULT_SLEEP_MILLIS = 0L;
	public final static String PARAM_DAY_SIZE = "day";
	public final static int DEFAULT_DAY_SIZE = 14 ;
	
	public final static String PARAM_QUERY_STATUS_DELAY = "queryStatusDelay";
	public final static int DEFAULT_QUERY_STATUS_DELAY = 60;
	
	
	public final static String PARAM_NOTIFY_OUTLIER_DELAY = "notifyOutlierDelay";
	public final static int DEFAULT_NOTIFY_OUTLIER_DELAY = 60;
}
