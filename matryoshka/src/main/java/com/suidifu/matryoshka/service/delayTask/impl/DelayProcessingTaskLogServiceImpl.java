package com.suidifu.matryoshka.service.delayTask.impl;

import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.matryoshka.delayTask.DelayProcessingTaskLog;
import com.suidifu.matryoshka.service.delayTask.DelayProcessingTaskLogService;

/**
 *
 * Created by louguanyang on 2017/5/3.
 */
@Service("delayProcessingTaskLogService4M")
public class DelayProcessingTaskLogServiceImpl extends GenericServiceImpl<DelayProcessingTaskLog> implements
        DelayProcessingTaskLogService {
}
