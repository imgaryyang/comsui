package com.suidifu.jpmorgan.controller;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suidifu.jpmorgan.ConfigInitializer;
import com.suidifu.jpmorgan.entity.ScheduleJob;
import com.suidifu.jpmorgan.factory.QuartzJobFactory;
import com.suidifu.jpmorgan.service.ScheduleJobService;
import com.zufangbao.gluon.resolver.JsonViewResolver;

@Controller
@RequestMapping("/quartz")
public class QuartzController {
	
	@Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	ScheduleJobService scheduleJobService;
	@Autowired
	JsonViewResolver jsonViewResolver;
	@Autowired
	ConfigInitializer configInitializer;
	
	private static final Log logger = LogFactory.getLog(QuartzController.class);

	@RequestMapping(value = "start", method = RequestMethod.POST)
	public @ResponseBody String start() {
		try {
			// 刷新初始化配置
			configInitializer.configInitialization();
			
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
	        
	        //获取任务信息数据
	        List<ScheduleJob> jobList = scheduleJobService.getEnabledJobs();
	        
	        if(CollectionUtils.isEmpty(jobList)) {
	        	logger.warn("no enabled jobs!");
	        	return jsonViewResolver.errorJsonResult("no enabled jobs!");
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
	            
	        logger.info("schedule jobs 启动成功...");
	        return jsonViewResolver.jsonResult("config 刷新成功,schedule jobs 刷新成功...");
		} catch (SchedulerException e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误!");
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("系统错误!");
		}
	}
	
	@RequestMapping(value = "jobStatus", method = RequestMethod.POST)
	public @ResponseBody String queryJobStatus() {
		
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			
			List<ScheduleJob> jobList = scheduleJobService.getEnabledJobs();
	        
	        if(CollectionUtils.isEmpty(jobList)) {
	        	logger.warn("no enabled jobs!");
	        	return jsonViewResolver.errorJsonResult("no enabled jobs!");
			}
			
	        for (ScheduleJob job : jobList) {
	        	TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
	        	
	        	TriggerState triggerState = scheduler.getTriggerState(triggerKey);
	        	logger.info("job name:" + job.getJobName() + ",job status:" + triggerState);
	        	
	        	if(TriggerState.NONE.equals(triggerState) || TriggerState.ERROR.equals(triggerState)) {
	        		return jsonViewResolver.errorJsonResult("not all jobs are running normally!");
	        	}
	        	
	        }
			
			return jsonViewResolver.sucJsonResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			return jsonViewResolver.errorJsonResult("查询异常!");
		}
		
	}

}
