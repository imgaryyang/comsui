package com.suidifu.matryoshka.service.delayTask;

import com.demo2do.core.service.GenericService;
import com.suidifu.matryoshka.delayTask.DelayProcessingTaskConfig;

/**
 *
 * Created by louguanyang on 2017/5/3.
 */
public interface DelayProcessingTaskConfigDBService extends GenericService<DelayProcessingTaskConfig> {
    DelayProcessingTaskConfig getValidConfig(String uuid);
    DelayProcessingTaskConfig getByProduct(String productLv1Code,String productLv2Code,String productLv3Code);
}
