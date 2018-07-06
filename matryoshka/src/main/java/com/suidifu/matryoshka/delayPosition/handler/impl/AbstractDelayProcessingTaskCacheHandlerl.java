package com.suidifu.matryoshka.delayPosition.handler.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.zufangbao.sun.yunxin.exception.SunException;

/**
 *
 * Created by louguanyang on 2017/5/8.
 */
public abstract class AbstractDelayProcessingTaskCacheHandlerl implements DelayProcessingTaskCacheHandler {

    private static final Log LOGGER = LogFactory.getLog(AbstractDelayProcessingTaskCacheHandlerl.class);

	@Autowired
	private DelayProcessingTaskService delayProcessingTaskDBService;

	@Autowired
	private DelayProcessingTaskService delayProcessingTaskCacheService;

	@Override
	public void saveOrUpdate(DelayProcessingTask task, boolean inCache) {
		try {
			if (null == task) {
				LOGGER.error("saveOrUpdate fail, task is null.");
				return;
			}
			delayProcessingTaskDBService.saveOrUpdate(task);
			if (inCache) {
				delayProcessingTaskCacheService.saveOrUpdate(task);
			}
		} catch (SunException e) {
			e.printStackTrace();
			LOGGER.warn("save or update has exception, error msg:" + e.getMessage());
		}
	}

	@Override
	public void save_to_db(DelayProcessingTask task) {
		saveOrUpdate(task, false);
	}

	@Override
	public void save_to_db_cache(DelayProcessingTask task) {
		saveOrUpdate(task, true);
	}

	@Override
	public DelayProcessingTask getByUuid(String delayProcessingTaskUuid) {
		try {
			DelayProcessingTask delayProcessingTask = delayProcessingTaskCacheService
					.getByUuid(delayProcessingTaskUuid);
			if (delayProcessingTask == null) {
				delayProcessingTask = delayProcessingTaskDBService.getByUuid(delayProcessingTaskUuid);
				if (delayProcessingTask != null) {
					LOGGER.info("save delayProcessingTask to cache!");
					delayProcessingTaskCacheService.saveOrUpdate(delayProcessingTask);
				}
			}
			return delayProcessingTask;
		} catch (SunException e) {
			e.printStackTrace();
			LOGGER.warn("get by uuid has exception, error msg:" + e.getMessage());
			return null;
		}
	}
}
