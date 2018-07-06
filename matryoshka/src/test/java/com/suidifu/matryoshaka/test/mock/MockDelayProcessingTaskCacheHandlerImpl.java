package com.suidifu.matryoshaka.test.mock;

import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.zufangbao.sun.yunxin.exception.SunException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by louguanyang on 2017/5/11.
 */
@Component("mockDelayProcessingTaskCacheHandler")
public class MockDelayProcessingTaskCacheHandlerImpl implements DelayProcessingTaskCacheHandler {
    @Autowired
    private DelayProcessingTaskService delayProcessingTaskDBService;

    @Autowired
    private DelayProcessingTaskService delayProcessingTaskCacheService;
    @Override
    public void saveOrUpdate(DelayProcessingTask task, boolean in_cache) {
        try {
            if (null == task) {
                return;
            }
            delayProcessingTaskDBService.saveOrUpdate(task);
            if (in_cache) {
                delayProcessingTaskCacheService.saveOrUpdate(task);
            }
        } catch (SunException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save_to_db(DelayProcessingTask task) {
        saveOrUpdate(task, false);
    }

    @Override
    public void save_to_db_cache(DelayProcessingTask task) {
        saveOrUpdate(task, false);
    }

    @Override
    public DelayProcessingTask getByUuid(String delayProcessingTaskUuid) {
        try {
            DelayProcessingTask delayProcessingTask = delayProcessingTaskCacheService
                    .getByUuid(delayProcessingTaskUuid);
            if (delayProcessingTask == null) {
                delayProcessingTask = delayProcessingTaskDBService.getByUuid(delayProcessingTaskUuid);
                if (delayProcessingTask != null) {
                    delayProcessingTaskCacheService.saveOrUpdate(delayProcessingTask);
                }
            }
            return delayProcessingTask;
        } catch (SunException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void customizeDelayTask() {

    }
}
