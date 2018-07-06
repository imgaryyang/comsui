package com.suidifu.barclays.factory;

import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.suidifu.barclays.handler.JobHandler;
import com.zufangbao.sun.yunxin.entity.barclays.ScheduleJob;

@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");

        JobHandler jobHandler = JobFactory.newInstance(scheduleJob.getJobGroup());
        
        //TODO jobHandler is null
        //System.out.println("jobHandler : " + jobHandler + " , job name: " + scheduleJob.getJobName());
        Map<String, Object> workingParms = scheduleJob.getWorkingParms();
        jobHandler.run(workingParms);
        
	}

}
