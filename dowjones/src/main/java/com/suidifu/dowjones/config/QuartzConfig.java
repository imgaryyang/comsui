package com.suidifu.dowjones.config;

import com.suidifu.dowjones.quartz.AutoWiringSpringBeanJobFactory;
import lombok.extern.slf4j.Slf4j;
import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/20 <br>
 * @time: 下午3:46 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Configuration
@Slf4j
public class QuartzConfig {
    @Resource
    private QuartzProperties quartzProperties;

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) {
        //实例化
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        //将spring管理job自定义工厂交由调度器维护
        factory.setJobFactory(jobFactory);
        //设置覆盖已存在的任务
        factory.setOverwriteExistingJobs(quartzProperties.getOverwriteExistingJobs());
        //项目启动完成后，等待10秒后开始执行调度器初始化
        factory.setStartupDelay(10);
        //设置调度器自动运行
        factory.setAutoStartup(true);
        //设置数据源，使用与项目统一数据源
//        factory.setDataSource(dataSource);
        //设置上下文spring bean name
        factory.setApplicationContextSchedulerContextKey(quartzProperties.getApplicationContextSchedulerContextKey());
        //返回
        return factory;
    }
}