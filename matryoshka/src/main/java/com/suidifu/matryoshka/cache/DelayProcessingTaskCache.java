package com.suidifu.matryoshka.cache;


import java.io.Serializable;

import com.suidifu.matryoshka.delayTask.DelayProcessingTask;

/**
 * Created by louguanyang on 2017/5/8.
 */
public class DelayProcessingTaskCache implements Serializable {
    private static final long serialVersionUID = 253459129408854555L;

    private final static long TIMEOUT = 1000 * 60 * 60 * 24 * 5;

    private DelayProcessingTask delayProcessingTask;
    private long timeStamp;

    public DelayProcessingTaskCache() {
        super();
    }

    public DelayProcessingTaskCache(DelayProcessingTask delayProcessingTask) {
        this.delayProcessingTask = delayProcessingTask;
        this.timeStamp = System.currentTimeMillis();
    }

    public DelayProcessingTask getDelayProcessingTask() {
        return delayProcessingTask;
    }

    public void setDelayProcessingTask(DelayProcessingTask delayProcessingTask) {
        this.delayProcessingTask = delayProcessingTask;
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
