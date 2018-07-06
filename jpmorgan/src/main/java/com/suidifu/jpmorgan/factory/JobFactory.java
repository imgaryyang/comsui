package com.suidifu.jpmorgan.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.suidifu.jpmorgan.handler.JobHandler;
import com.suidifu.jpmorgan.util.SpringContextUtil;

public class JobFactory {

	public static String DISTRIBUTE_JOB = "distributeJob";
	public static String PAYMENT_JOB = "paymentJob";
	public static String BUSINESS_STATUS_JOB = "businessStatusJob";
	public static String TIMEOUT_JOB = "timeoutJob";
	public static String STATUS_WRITEBACK_JOB = "statusWritebackJob";
	public static String EXTRA_QUERY_JOB = "extraQueryJob";
	
	private final static Map<String, String> JobBeanNameMapper = new HashMap<String, String>() {
		{
			put(DISTRIBUTE_JOB, "distributeJob");
			put(PAYMENT_JOB, "paymentJob");
			put(BUSINESS_STATUS_JOB, "businessStatusJob");
			put(TIMEOUT_JOB, "timeoutJob");
			put(STATUS_WRITEBACK_JOB, "statusWritebackJob");
			put(EXTRA_QUERY_JOB, "extraQueryJob");
		}
	};
	
	public static <T extends JobHandler> T newInstance(String jobGroup) {
		String beanName = JobBeanNameMapper.getOrDefault(jobGroup, StringUtils.EMPTY);
		if(StringUtils.isEmpty(beanName)) {
			return null;
		}
		return SpringContextUtil.getBean(beanName);
	}
	
}
