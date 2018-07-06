package com.suidifu.jpmorgan.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.ScheduleJob;

public interface ScheduleJobService extends GenericService<ScheduleJob> {

	List<ScheduleJob> getEnabledJobs();
	
	int getEnabledJobCountByJobGroup(String jobGroup);
}
