package com.suidifu.dowjones.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * Created by veda on 26/12/2017.
 */
@Component
@Slf4j
public class SchedulerAllJob implements ApplicationListener<ContextRefreshedEvent> {
    private static final String KEY_STATUS = "status";
    private static final String KEY_JOB_CLASS = "jobClass";
    private static final String KEY_JOB_NAME = "jobName";
    private static final String KEY_JOB_GROUP = "jobGroup";
    private static final String KEY_CRON_EXPRESSION = "cronExpression";
    private static final String KEY_CRON_TRIGGER_NAME = "cronTriggerName";
    private static final String KEY_CRON_TRIGGER_GROUP = "cronTriggerGroup";
    private static final String KEY_SHOULD_RECOVER = "shouldRecover";
    private static final String KEY_DURABILITY = "durability";
    private static final String KEY_PRIORITY = "priority";
    private static final String VALUE_DEFAULT_JOB_NAME = "default-job-name";
    private static final String VALUE_DEFAULT_CRON_TRIGGER_NAME = "default-cron-trigger-name";
    @javax.annotation.Resource
    private PathConfig pathConfig;

    @javax.annotation.Resource
    private QuartzProperties quartzProperties;

    @javax.annotation.Resource
    private StdScheduler schedulerFactoryBean;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            String filePath = pathConfig.getReportTask();

            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(filePath);
            for (Resource resource : resources) {
                Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                scheduleSingleJob(schedulerFactoryBean, properties);
            }

            List<ScheduleJobConfig> scheduleJobConfigs = quartzProperties.getScheduleJob();
            for (ScheduleJobConfig scheduleJobConfig : scheduleJobConfigs) {
                scheduleSingleJob(schedulerFactoryBean, scheduleJobConfig);
            }

        } catch (IOException | SchedulerException | ClassNotFoundException e) {
            log.error("exception message is:{}", ExceptionUtils.getStackTrace(e));
        }
    }


    /**
     * 配置Job
     * 此处的任务可以配置可以放到properties或者是放到数据库中
     * 如果此时需要做到动态的定时任务，请参考：http://blog.csdn.net/liuchuanhong1/article/details/60873295
     * 博客中的ScheduleRefreshDatabase类
     */
    private void scheduleSingleJob(Scheduler scheduler, Properties properties)
            throws SchedulerException, ClassNotFoundException {
        /*
         *  此处可以先通过任务名查询数据库，
         *  如果数据库中存在该任务，则按照ScheduleRefreshDatabase类中的方法，更新任务的配置以及触发器
         *  如果此时数据库中没有查询到该任务，则按照下面的步骤新建一个任务，并配置初始化的参数，并将配置存到数据库中
         */

        int status = Integer.parseInt((String) properties.getOrDefault(KEY_STATUS, 0));
        if (!Objects.equals(status, 0)) {
            return;
        }

        String name = (String) properties.getOrDefault(KEY_JOB_NAME, VALUE_DEFAULT_JOB_NAME);
        String group = (String) properties.getOrDefault(KEY_JOB_GROUP, Scheduler.DEFAULT_GROUP);
        String cronExpression = (String) properties.getOrDefault(KEY_CRON_EXPRESSION, "");

        String cronTriggerName = (String) properties.getOrDefault(KEY_CRON_TRIGGER_NAME, VALUE_DEFAULT_CRON_TRIGGER_NAME);
        String cronTriggerGroup = (String) properties.getOrDefault(KEY_CRON_TRIGGER_GROUP, Scheduler.DEFAULT_GROUP);

        boolean shouldRecover = Boolean.valueOf((String) properties.getOrDefault(KEY_SHOULD_RECOVER, "false"));
        boolean durability = Boolean.valueOf((String) properties.getOrDefault(KEY_DURABILITY, "false"));
        int priority = Integer.parseInt((String) properties.getOrDefault(KEY_PRIORITY, Trigger.DEFAULT_PRIORITY));

        String jobClassName = (String) properties.get(KEY_JOB_CLASS);

        JobDetail jobDetail = JobBuilder.newJob((Class) Class.forName(jobClassName))
                .withIdentity(name, group)
                .requestRecovery(shouldRecover)
                .storeDurably(durability)
                .build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(cronTriggerName, cronTriggerGroup)
                .withSchedule(scheduleBuilder)
                .withPriority(priority)
                .build();

        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    private void scheduleSingleJob(Scheduler scheduler, ScheduleJobConfig scheduleJobConfig)
            throws SchedulerException, ClassNotFoundException {
        log.info("\ngetStatus is:{}\n", scheduleJobConfig.getStatus());
        if (!Objects.equals(scheduleJobConfig.getStatus(), 0) && scheduleJobConfig.getStatus() != null) {
            return;
        }

        JobDetail jobDetail = JobBuilder.newJob((Class) Class.forName(scheduleJobConfig.getJobClassName()))
                .requestRecovery(scheduleJobConfig.getRequestsRecovery())
                .storeDurably(scheduleJobConfig.getDurability())
                .build();

        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJobConfig.getCronExpression());

        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withSchedule(scheduleBuilder)
                .withPriority(scheduleJobConfig.getPriority())
                .build();

        scheduler.scheduleJob(jobDetail, cronTrigger);
    }
}