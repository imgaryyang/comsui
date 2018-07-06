package com.suidifu.jpmorgan.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.JobStatus;
import com.suidifu.jpmorgan.entity.ScheduleJob;
import com.suidifu.jpmorgan.service.ScheduleJobService;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends GenericServiceImpl<ScheduleJob>
		implements ScheduleJobService {

	@Override
	public List<ScheduleJob> getEnabledJobs() {
		return genericDaoSupport.searchForList("FROM ScheduleJob WHERE jobStatus =:jobStatus", "jobStatus", JobStatus.Enable);
	}

	@Override
	public int getEnabledJobCountByJobGroup(String jobGroup) {
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("jobGroup", jobGroup);
		parms.put("jobStatus", JobStatus.Enable.ordinal());
		
		List<Map<String,Object>> queryForList = genericDaoSupport.queryForList("select id from schedule_job where job_group =:jobGroup and job_status =:jobStatus", parms);
		
		return queryForList.size();
	}

}
