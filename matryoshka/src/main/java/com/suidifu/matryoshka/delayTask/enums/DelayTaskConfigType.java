package com.suidifu.matryoshka.delayTask.enums;

/**
 * 后置任务配置 - 任务类型
 * Created by louguanyang on 2017/5/3.
 */
public enum DelayTaskConfigType {
    /**
     * 实时处理
     */
    REAL_TIME(0, "enum.delay-task-config-type.real-time"),
    /**
     * 批量处理
     */
    BATCH(1, "enum.delay-task-config-type.batch");
    private Integer code;
    private String key;

    DelayTaskConfigType(Integer code, String key) {
        this.code = code;
        this.key = key;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
