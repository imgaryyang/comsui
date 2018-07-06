package com.suidifu.matryoshka.delayPosition.handler;

import com.suidifu.matryoshka.delayTask.DelayProcessingTaskConfig;
import com.suidifu.xcode.handler.SourceCodeCompilerHandler;

/**
 * 后置处理任务缓存Handler
 * Created by louguanyang on 2017/5/6.
 */
public interface DelayTaskConfigCacheHandler {

    DelayProcessingTaskConfig getConfigByUuid(String delayTaskConfigUuid);

    Object getCompiledObject(DelayProcessingTaskConfig config);

    Object getCompiledObjectDelayTaskConfigUuid(String delayTaskConfigUuid);

    void register(String businessType, SourceCodeCompilerHandler sourceCodeCompilerHandler);

    void clearAll();

    void clearByUrl(String url);

}
