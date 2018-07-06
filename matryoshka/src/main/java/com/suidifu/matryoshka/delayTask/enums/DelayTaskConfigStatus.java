package com.suidifu.matryoshka.delayTask.enums;

/**
 * 后置任务配置 - 配置有效状态
 * Created by louguanyang on 2017/5/3.
 */
public enum DelayTaskConfigStatus {
    /**
     * 无效
     */
    INVALID(0, "enum.delay-task-config-status.invalid"),
    /**
     * 可用
     */
    VALID(1, "enum.delay-task-config-status.valid");
    private Integer code;
    private String key;

    DelayTaskConfigStatus(Integer code, String key) {
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
