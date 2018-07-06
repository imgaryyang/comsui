package com.suidifu.barclays.controller;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.suidifu.barclays.factory.QuartzJobFactory;
import com.zufangbao.sun.yunxin.entity.barclays.ScheduleJob;
import com.zufangbao.sun.yunxin.service.barclays.ScheduleJobService;

@Controller
@RequestMapping("/quartz")
public class QuartzController {
	
	@Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	ScheduleJobService scheduleJobService;
	

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	public void start() {
		/*try {

			Scheduler scheduler = schedulerFactoryBean.getScheduler();
	        System.out.println(scheduler);
	        
	        //获取任务信息数据
	        List<ScheduleJob> jobList = scheduleJobService.getEnabledJobs();
	        
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
	        
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

}
