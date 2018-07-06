package com.suidifu.matryoshka.delayTask.enums;

/**
 * 后置任务执行状态
 * Created by louguanyang on 2017/5/3.
 */
public enum DelayTaskExecuteStatus {
    /**
     * 已创建
     */
    CREATE(0, "enum.delay-task-execute-status.create"),
    /**
     * 成功
     */
    SUCCESS(1, "enum.delay-task-execute-status.success"),
    /**
     * 失败
     */
    FAIL(2, "enum.delay-task-execute-status.fail")
    ;
    private Integer code;
    private String key;

    DelayTaskExecuteStatus(Integer code, String key) {
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
