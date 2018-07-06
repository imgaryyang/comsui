package com.suidifu.barclays.starter;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.suidifu.barclays.factory.QuartzJobFactory;
import com.zufangbao.sun.yunxin.entity.barclays.ScheduleJob;
import com.zufangbao.sun.yunxin.service.barclays.ScheduleJobService;

@Component
public class ScheduleJobStarter {

	@Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	ScheduleJobService scheduleJobService;
	
	
	
	@PostConstruct
	public void startScheduleJobs() throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		List<ScheduleJob> jobList = scheduleJobService.getEnabledJobs();
		
		if(CollectionUtils.isEmpty(jobList)) {
			return;
		}
		
		for (ScheduleJob job : jobList) {
        	TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        	//获取trigger
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            
            //不存在，创建一个
            if (null == trigger) {
                JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
                    .withIdentity(job.getJobName(), job.getJobGroup()).build();
                jobDetail.getJobDataMap().put("scheduleJob", job);
         
                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
                    .getCronExpression());
         
                //按新的cronExpression表达式构建一个新的trigger
                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                // Trigger已存在，那么更新相应的定时设置
                //表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
                    .getCronExpression());
         
                //按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder).build();
         
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        }
		System.out.println("schedule jobs start success...");
	}
	
}
