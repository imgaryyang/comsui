package com.suidifu.barclays.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.suidifu.barclays.handler.JobHandler;
import com.zufangbao.sun.utils.SpringContextUtil;

public class JobFactory {

	static String AUDIT_BILL_JOB = "auditBillJob";
	static String CASH_FLOW_JOB = "cashflowJob";
	static String TRANSACTION_RECORD_JOB = "transactionRecordJob";
	
	static String DAILY_RESET_JOB = "dailyResetJob";

	static String HISTROY_CASH_FLOW_JOB = "historyCashflowJob";
	
	private final static Map<String, String> JobBeanNameMapper = new HashMap<String, String>() {
		{
			put(AUDIT_BILL_JOB, "auditBillJob");
			put(CASH_FLOW_JOB, "cashflowJob");
			put(TRANSACTION_RECORD_JOB, "transactionRecordJob");
			put(DAILY_RESET_JOB, "dailyResetJob");
			put(HISTROY_CASH_FLOW_JOB, "historyCashflowJob");
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
