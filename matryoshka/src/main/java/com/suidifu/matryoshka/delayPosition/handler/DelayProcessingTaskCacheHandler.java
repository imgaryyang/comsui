package com.suidifu.matryoshka.delayPosition.handler;

import com.suidifu.matryoshka.delayTask.DelayProcessingTask;

/**
 * Created by louguanyang on 2017/5/8.
 */
public interface DelayProcessingTaskCacheHandler {
    public  void saveOrUpdate(DelayProcessingTask task, boolean in_cache);

    public void save_to_db(DelayProcessingTask task);

    public void save_to_db_cache(DelayProcessingTask task);

    public  DelayProcessingTask getByUuid(String delayProcessingTaskUuid);

    public void  customizeDelayTask();
}
