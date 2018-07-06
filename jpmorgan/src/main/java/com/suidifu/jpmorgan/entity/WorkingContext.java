package com.suidifu.jpmorgan.entity;

import java.util.HashMap;
import java.util.Map;

public class WorkingContext {

	private String workerUuid;
	private int workingSlot;
	private Map<String, String> workingParameters;
	
	public WorkingContext() {
		super();
	}

	public WorkingContext(String workerUuid, int workingSlot) {
		super();
		this.workerUuid = workerUuid;
		this.workingSlot = workingSlot;
	}

	public String getWorkerUuid() {
		return workerUuid;
	}

	public void setWorkerUuid(String workerUuid) {
		this.workerUuid = workerUuid;
	}

	public int getWorkingSlot() {
		return workingSlot;
	}

	public void setWorkingSlot(int workingSlot) {
		this.workingSlot = workingSlot;
	}

	public void setWorkingParameters(Map<String, String> params) {
		workingParameters = params;
	}
	
	public Map<String, String> getWorkingParameters() {
		return workingParameters;
	}

//	public Object getWorkingParameters(String Key) {
//		if (CollectionUtils.isEmpty(workingParameters)) {
//			return StringUtils.EMPTY;
//		}
//		return workingParameters.getOrDefault(Key, StringUtils.EMPTY);
//	}
	
	public void appendWorkingParameters(Map<String, String> parms) {
		if(null == workingParameters) {
			workingParameters = new HashMap<String, String>();
		}
		workingParameters.putAll(parms);
	}

}
