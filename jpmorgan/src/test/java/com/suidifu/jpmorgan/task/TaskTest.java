package com.suidifu.jpmorgan.task;

import java.text.ParseException;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class TaskTest {

	public static void main(String[] args) throws Exception {
//		new ClassPathXmlApplicationContext(
//				"/context/applicationContext-schedule-cronTrigger.xml");
		
//		ApplicationContext ctx = new ClassPathXmlApplicationContext("/context/applicationContext-schedule-dynamicTrigger.xml");  
//		SchedulerFactoryBean scheduler = (SchedulerFactoryBean)ctx.getBean("scheduler");  
//		
//        System.out.println("Scheduling to run tasks.");  
//        
//        for (int i = 0; i < 5; i++) {
//            try {  
//            	//MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
//                JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();  
//                jobDetail.setName("job_" + i);  
//                MyTask myTask = new MyTask();  
//                myTask.setName("task_" + i);  
//                jobDetail.getJobDataMap().put("myTask", myTask);  
//                jobDetail.setJobClass(MyJob.class);  
//                scheduler.addJob((JobDetail) jobDetail, true);  
//  
//                CronTriggerFactoryBean cronTrigger =new CronTriggerFactoryBean("cron_" + i, Scheduler.DEFAULT_GROUP, jobDetail.getName(), Scheduler.DEFAULT_GROUP);  
//                cronTrigger.setCronExpression("0/10 * * * * ?");  
//  
//                scheduler.scheduleJob(cronTrigger);  
//            } catch (ParseException e) {  
//                e.printStackTrace();  
//            } catch (SchedulerException e) {
//                e.printStackTrace();  
//            }  
//        }
        
	}
}
