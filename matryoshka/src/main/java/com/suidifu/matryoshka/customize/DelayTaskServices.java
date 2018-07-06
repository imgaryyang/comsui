package com.suidifu.matryoshka.customize;

import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import org.apache.commons.logging.Log;

import java.util.Map;

/**
 * 后置任务Task 脚本编译器
 * Created by louguanyang on 2017/5/4.
 */
public interface DelayTaskServices {
    boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler, SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap, Log logger);
}
