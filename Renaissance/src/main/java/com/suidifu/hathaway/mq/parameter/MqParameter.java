package com.suidifu.hathaway.mq.parameter;

/**
 * 
 * @author wukai
 *
 */
public class MqParameter {

	private String requestUuid;
	
	private String businessUuid;
	
	private String methodName;
	
	private String beanName;
	
	private int prority;
	
	private Object[] args;
	
	private String producerName;
	
	public MqParameter(String requestUuid, String businessUuid,
			String methodName, String beanName, int prority, Object[] args, String producerName) {
		super();
		this.requestUuid = requestUuid;
		this.businessUuid = businessUuid;
		this.methodName = methodName;
		this.beanName = beanName;
		this.prority = prority;
		this.args = args;
		this.producerName=producerName;
	}

	public String getRequestUuid() {
		return requestUuid;
	}

	public void setRequestUuid(String requestUuid) {
		this.requestUuid = requestUuid;
	}

	public String getBusinessUuid() {
		return businessUuid;
	}

	public void setBusinessUuid(String businessUuid) {
		this.businessUuid = businessUuid;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public int getPrority() {
		return prority;
	}

	public void setPrority(int prority) {
		this.prority = prority;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getProducerName() {
		return producerName;
	}

	public void setProducerName(String producerName) {
		this.producerName = producerName;
	}
}
