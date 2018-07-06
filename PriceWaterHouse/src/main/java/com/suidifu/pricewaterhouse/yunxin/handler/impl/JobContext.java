package com.suidifu.pricewaterhouse.yunxin.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.suidifu.hathaway.job.Job;
import com.suidifu.hathaway.job.Step;

public class JobContext<S>
{


	private HashMap<Step,List<Object>> rawDataTable;
	Job job;
	private S jobTarget;
	private Map<String,String> syncMarks;
	public Map<Step, List<Object>> getRawDataTable() {
		return rawDataTable;
	}
	public void setRawDataTable(HashMap<Step, List<Object>> rawDataTable) {
		this.rawDataTable = rawDataTable;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public S getJobTarget() {
		return jobTarget;
	}
	public void setJobTarget(S jobTarget) {
		this.jobTarget = jobTarget;
	}
	public Map<String, String> getSyncMarks() {
		return syncMarks;
	}
	public void setSyncMarks(Map<String, String> syncMarks) {
		this.syncMarks = syncMarks;
	}
	
}