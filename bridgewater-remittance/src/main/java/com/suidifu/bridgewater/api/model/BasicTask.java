package com.suidifu.bridgewater.api.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.gluon.util.SpringContextUtil;
import com.zufangbao.sun.entity.task.DynamicBusinessTask;
import com.zufangbao.sun.utils.JsonUtils;

public class BasicTask implements Runnable {
	
	private final static Map<String, String> TASK_GROUP_MAPPER = new HashMap<String, String>() {
	{
		put("bussinessVerify", "bussinessVerifyTask");
		put("notify", "combineNoticeTask");
		put("queryStatus", "remittanceTask");
		put("auditBill", "auditBillTask");
	}};

	/**
	 * 服务主机
	 */
	private String serverHost;
	
	/**
	 * 服务组别
	 */
	private int serverGroup;
	
	/**
	 * 任务id
	 */
	private String taskId;

	/**
	 * 任务组别
	 */
	private String taskGroup;

	/**
	 * 定时表达式
	 */
	private String cronExpression;

	/**
	 * 周期性频率
	 */
	private Long periodicTrigger = 0L;
	
	/**
	 * 是否启用
	 */
	private boolean isEnabled = false;
	
	/**
	 * 工作参数
	 */
	private Map<String, String> workParams = new HashMap<String, String>();

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public int getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(int serverGroup) {
		this.serverGroup = serverGroup;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String id) {
		this.taskId = id;
	}

	public String getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(String taskGroup) {
		this.taskGroup = taskGroup;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Long getPeriodicTrigger() {
		return periodicTrigger;
	}

	public void setPeriodicTrigger(Long periodicTrigger) {
		this.periodicTrigger = periodicTrigger;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Map<String, String> getWorkParams() {
		return workParams;
	}

	public void setWorkParams(Map<String, String> params) {
		this.workParams = params;
	}
	
	@JSONField(serialize = false)
	public String getWorkParam(String key) {
		if(StringUtils.isBlank(key)) {
			return StringUtils.EMPTY;
		}
		return this.workParams.getOrDefault(key, StringUtils.EMPTY);
	}
	
	@JSONField(serialize = false)
	public int getIntegetFromWorkParamOrDefault(String key,int defaultValue) {
		int value;
		try {
			value = Integer.valueOf(getWorkParam(key));
		} catch (Exception e) {
			value = defaultValue;
		}
		return value;
	}
	
	@JSONField(serialize = false)
	public long getLongFromWorkParamOrDefault(String key,long defaultValue) {
		long value;
		try {
			value = Long.valueOf(getWorkParam(key));
		} catch (Exception e) {
			value = defaultValue;
		}
		return value;
	}

	@JSONField(serialize = false)
	public boolean isValid() {
		if (StringUtils.isBlank(this.taskGroup)) {
			return false;
		}
		if (!TASK_GROUP_MAPPER.containsKey(this.taskGroup)) {
			return false;
		}
		if (StringUtils.isBlank(this.cronExpression) && periodicTrigger == 0L) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static BasicTask convertToRunnableTask(DynamicBusinessTask task) {
		String beanName = TASK_GROUP_MAPPER.getOrDefault(task.getTaskGroup(), StringUtils.EMPTY);
		BasicTask runnableTask = SpringContextUtil.getBean(beanName);
		runnableTask.setServerHost(task.getServerHost());
		runnableTask.setServerGroup(task.getServerGroup());
		runnableTask.setTaskId(task.getTaskId());
		runnableTask.setTaskGroup(task.getTaskGroup());
		runnableTask.setIsEnabled(task.isEnabled());
		runnableTask.setPeriodicTrigger(task.getPeriodicTrigger());
		runnableTask.setCronExpression(task.getCronExpression());
		Map<String, String> workParams = JsonUtils.parse(task.getWorkParams(), Map.class);
		runnableTask.setWorkParams(workParams);
		return runnableTask;
	}

	@Override
	public void run() {
		
	}

}
