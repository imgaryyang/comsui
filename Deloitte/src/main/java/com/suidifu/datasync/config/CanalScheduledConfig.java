package com.suidifu.datasync.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 
 * @author lisf
 *
 */
@Configuration
@EnableScheduling
public class CanalScheduledConfig implements SchedulingConfigurer {

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setThreadNamePrefix("<T_canalclient>");
		taskScheduler.setPoolSize(1);
		taskScheduler.initialize();
		taskRegistrar.setTaskScheduler(taskScheduler);
	}

}
