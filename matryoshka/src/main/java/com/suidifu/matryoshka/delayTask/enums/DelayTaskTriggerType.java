package com.suidifu.matryoshka.delayTask.enums;

/**
 * 后置任务配置 - 触发类型
 * Created by louguanyang on 2017/5/3.
 */
public enum DelayTaskTriggerType {
    /**
     * 变更触发
     */
    MODIFY_REPAYMENT_PLAN(0, "enum.delay-task-trigger-type.modify-repayment-plan"),
    /**
     * 销账触发
     */
    RECONCILIATION(1, "enum.delay-task-trigger-type.reconciliation"),
    /**
     * 生成回购单触发
     */
    GENERATE_REPURCHASE_DOC(2, "enum.delay-task-trigger-type.generate_repurchase_doc"),

    /**
     * 批量变更触发
     */
    BATCH_MODIFY_REPAYMENT_PLAN(10001, "enum.delay-task-trigger-type.batch_modify_repayment_plan"),

    /**
     * 批量变更触发
     */
    BATCH_MUTABLE_FEE(10002, "enum.delay-task-trigger-type.batch_mutable_fee"),

    ;

    private Integer code;
    private String key;

    DelayTaskTriggerType(Integer code, String key) {
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
