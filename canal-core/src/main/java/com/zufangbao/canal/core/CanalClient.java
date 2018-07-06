package com.zufangbao.canal.core;

import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import com.zufangbao.canal.core.rowprocesser.RowProcessor;

/**
 * CanalClient监控数据库表更改
 * 
 * @author lisf
 *
 */
@Component
public class CanalClient implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(CanalClient.class);

	@Resource(name = "simpleRowProcessor")
	private RowProcessor rowProcesser;

	@Resource(name = "canalTask")
	private CanalTask canalTask;

	@Value("${canal.server.fixed-delay}")
	private int delay;

	private void init() {
		rowProcesser.init();
		new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor(new CustomizableThreadFactory("<T_canalclient>"))).scheduleWithFixedDelay(canalTask, delay);
		LOG.info("CanalClient已启动....");
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		init();
	}

}
