package com.suidifu.jpmorgan.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;

@Entity
@Table(name = "schedule_job")
public class ScheduleJob {

	@Id
	@GeneratedValue
	private Long id;

	/** 任务uuid */
	private String jobUuid;

	/** 任务名称 */
	private String jobName;

	/** 任务分组 */
	private String jobGroup;

	/** 任务状态 0禁用 1启用 2删除 */
	private JobStatus jobStatus;

	/** 任务运行时间表达式 */
	private String cronExpression;

	/** 任务描述 */
	private String desc;
	
	private String workerUuid;
	
	@Column(columnDefinition = "text")
	private String workingParms;

	public ScheduleJob() {
		super();
	}

	public ScheduleJob(String jobUuid, String jobName, String jobGroup,
			JobStatus jobStatus, String cronExpression, String desc) {
		super();
		this.jobUuid = jobUuid;
		this.jobName = jobName;
		this.jobGroup = jobGroup;
		this.jobStatus = jobStatus;
		this.cronExpression = cronExpression;
		this.desc = desc;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobUuid() {
		return jobUuid;
	}

	public void setJobUuid(String jobUuid) {
		this.jobUuid = jobUuid;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public JobStatus getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getWorkerUuid() {
		return workerUuid;
	}

	public void setWorkerUuid(String workerUuid) {
		this.workerUuid = workerUuid;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getWorkingParms() {
		
		try {
			if (StringUtils.isEmpty(this.workingParms)) {
				return new HashMap<String, Object>();
			}
			return (Map<String, Object>) JSON.parse(this.workingParms);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
		
	}

	public void setWorkingParms(String workingParms) {
		this.workingParms = workingParms;
	}

}
