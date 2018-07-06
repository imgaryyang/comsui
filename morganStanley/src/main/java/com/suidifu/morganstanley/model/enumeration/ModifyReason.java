package com.suidifu.morganstanley.model.enumeration;

public enum ModifyReason {

    /**
     * 提前结清
     */
    REASON_1("enum.repayment-plan-modify-reason.reason-1"),
    /**
     * 提前部分还款
     */
    REASON_2("enum.repayment-plan-modify-reason.reason-2"),
    /**
     * 错误更正
     */
    REASON_3("enum.repayment-plan-modify-reason.reason-3"),
    /**
     * 随心还-延期还款
     */
    REASON_4("enum.repayment-plan-modify-reason.reason-4"),
    /**
     * 随心还-变更还款日期
     */
    REASON_5("enum.repayment-plan-modify-reason.reason-5"),
    /**
     * 提前结清(无提前结清手续费)
     */
    REASON_6("enum.repayment-plan-modify-reason.reason-6"),
    /**
     * 随心还-取消随心还服务
     */
    REASON_7("enum.repayment-plan-modify-reason.reason-7"),
    /**
     * 提前结清（有提前还款手续费）
     */
    REASON_8("enum.repayment-plan-modify-reason.reason-8"),
    /**
     * 退货
     */
    REASON_9("enum.repayment-plan-modify-reason.reason-9"),
    /**
     * 普通变更
     */
    REASON_10("enum.repayment-plan-modify-reason.reason-10");

    private String key;


    ModifyReason(String key) {
        this.key = key;
    }

    /**
     * 获取变更还款计划枚举类
     *
     * @param code 编码
     * @return 变更还款计划枚举类
     */
    public static ModifyReason get(String code) {
        switch (code) {
            case "1":
                return REASON_1;
            case "2":
                return REASON_2;
            case "3":
                return REASON_3;
            case "4":
                return REASON_4;
            case "5":
                return REASON_5;
            case "6":
                return REASON_6;
            case "7":
                return REASON_7;
            case "8":
                return REASON_8;
            case "9":
                return REASON_9;
            case "10":
                return REASON_10;
            default:
                return null;
        }
    }

    public String getKey() {
        return key;
    }
}