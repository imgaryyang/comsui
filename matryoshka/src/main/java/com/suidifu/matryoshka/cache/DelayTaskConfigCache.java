package com.suidifu.matryoshka.cache;


import java.io.Serializable;

import com.suidifu.matryoshka.delayTask.DelayProcessingTaskConfig;

/**
 *
 * Created by louguanyang on 2017/5/6.
 */
public class DelayTaskConfigCache implements Serializable {

    private static final long serialVersionUID = 7391083979801791575L;

    private final static long TIMEOUT = 1000 * 60 * 60 * 24 * 5;

    private DelayProcessingTaskConfig delayProcessingTaskConfig;

    private long timeStamp;

    public DelayTaskConfigCache() {
        super();
    }

    public DelayTaskConfigCache(DelayProcessingTaskConfig delayProcessingTaskConfig) {
        super();
        this.delayProcessingTaskConfig = delayProcessingTaskConfig;
        this.timeStamp = System.currentTimeMillis();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public DelayProcessingTaskConfig getDelayProcessingTaskConfig() {
        return delayProcessingTaskConfig;
    }

    public void setDelayProcessingTaskConfig(DelayProcessingTaskConfig delayProcessingTaskConfig) {
        this.delayProcessingTaskConfig = delayProcessingTaskConfig;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    private boolean isExpired(long timeout) {
        return (System.currentTimeMillis() - getTimeStamp()) > timeout;
    }

    public boolean needUpdate() {
        return isExpired(TIMEOUT);
    }
}
