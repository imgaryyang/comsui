package com.suidifu.jpmorgan.factory;

import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.suidifu.jpmorgan.entity.ScheduleJob;
import com.suidifu.jpmorgan.handler.JobHandler;

@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		//System.out.println("任务成功运行");
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        //System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]");
                
        JobHandler jobHandler = JobFactory.newInstance(scheduleJob.getJobGroup());
        
        //TODO jobHandler is null
        //System.out.println("jobHandler : " + jobHandler + " , job name: " + scheduleJob.getJobName());
        Map<String, Object> workingParms = scheduleJob.getWorkingParms();
        jobHandler.run(workingParms);
        
	}

}
