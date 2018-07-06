package com.suidifu.matryoshka.cache;

import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.delayTask.DelayProcessingTaskLog;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskService;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.exception.SunException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.zufangbao.sun.yunxin.delayTask.DelayProcessingTaskCacheSpec.CACHE_KEY;

/**
 *
 * Created by louguanyang on 2017/5/3.
 */
@Component("delayProcessingTaskCacheService")
public class DelayProcessingTaskCacheServiceImpl implements DelayProcessingTaskService {

    private static final Log LOGGER = LogFactory.getLog(DelayProcessingTaskCacheServiceImpl.class);

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void saveOrUpdate(DelayProcessingTask task) throws SunException {
        try {
            Cache cache = getDelayProcessingTaskCache();
            if (cache == null) {
                LOGGER.error(CACHE_KEY + " not found !");
                return;
            }
            if (task == null) {
                LOGGER.error("task is null !");
                return;
            }
            String delayProcessingTaskUuid = task.getUuid();
            if (StringUtils.isEmpty(delayProcessingTaskUuid)) {
                LOGGER.error("delayProcessingTaskUuid is empty !");
                return;
            }
            delete(delayProcessingTaskUuid);
            cache.put(delayProcessingTaskUuid, new DelayProcessingTaskCache(task));
        } catch (Exception e) {
            e.printStackTrace();
            throw new SunException("save or update by db exception " + e.getMessage());
        }
    }

    private Cache getDelayProcessingTaskCache() {
        return cacheManager.getCache(CACHE_KEY);
    }

    @Override
    public void delete(String delayProcessingTaskUuid) {
        Cache cache = getDelayProcessingTaskCache();
        if (null != cache && null != cache.get(delayProcessingTaskUuid)) {
            cache.evict(delayProcessingTaskUuid);
        }
    }

    @Override
    public DelayProcessingTask getByUuid(String delayProcessingTaskUuid) {
        Cache cache = getDelayProcessingTaskCache();
        if (cache == null) {
            LOGGER.error(CACHE_KEY + " not found !");
            return null;
        }
        DelayProcessingTaskCache delayProcessingTaskCache = cache.get(delayProcessingTaskUuid,
                DelayProcessingTaskCache.class);
        if (delayProcessingTaskCache == null) {
            return null;
        }
        return delayProcessingTaskCache.getDelayProcessingTask();
    }

    @Override
    public List<DelayProcessingTask> get_by_repaymentPlanUuid(String repaymentPlanUuid) {
        return Collections.emptyList();
    }

    @Override
    public List<DelayProcessingTask> get_by_repurchaseDocUuid(String repurchaseDocUuid) {
        return Collections.emptyList();
    }

    @Override
    public List<DelayProcessingTask> getByConfigUuid(String configUuid, String financialContractUuid) {
        return null;
    }


    @Override
    public void updateByTask(DelayProcessingTask task) throws Exception {

    }

    @Override
    public List<DelayProcessingTaskLog> getDelayProcessingTaskLogByConfigUuid() {
        return null;
    }

    @Override
    public int saveDelayProcessingTaskLog(List<DelayProcessingTaskLog> logs) throws Exception {
        return 0;
    }

    @Override
    public void delDelayProcessingTask(List<DelayProcessingTaskLog> logs) throws Exception {

    }

    @Override
    public List<DelayProcessingTask> getDelayProcessingTaskByFinancialContract(String financialContractUuid) {
        return null;
    }
}
