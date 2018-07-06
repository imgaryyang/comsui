/**
 *
 */
package com.suidifu.owlman.microservice.enumation;

import com.zufangbao.sun.BasicEnum;
import org.apache.commons.lang.StringUtils;

/**
 * @author hjl
 */
public enum AuditJobAndCashFlowSingleSigns implements BasicEnum {
    /**
     * 单个流水还单个对账任务
     */
    ONE_TO_ONE("enum.audit-Job-and-cash-flow-single-signs.one-to-one"),
    /**
     * 单个流水还多个对账任务
     */
    ONE_CASHFLOW("enum.audit-Job-and-cash-flow-single-signs.one-cashflow"),

    /**
     * 单个对账任务还多个流水
     */
    ONE_AUDITJOB("enum.audit-Job-and-cash-flow-single-signs.one-auditjob"),

    /**
     * 多个流水还多个对账任务
     */
    MANY_TO_MANY("enum.audit-Job-and-cash-flow-single-signs.many-to-many");
    private String key;

    private AuditJobAndCashFlowSingleSigns(String key) {
        this.key = key;
    }

    public static AuditJobAndCashFlowSingleSigns fromKey(String key) {
        for (AuditJobAndCashFlowSingleSigns item : AuditJobAndCashFlowSingleSigns.values()) {
            if (StringUtils.equals(item.getKey(), key)) {
                return item;
            }
        }
        return null;
    }

    public static AuditJobAndCashFlowSingleSigns fromValue(int value) {
        for (AuditJobAndCashFlowSingleSigns item : AuditJobAndCashFlowSingleSigns.values()) {
            if (item.ordinal() == value) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getKey() {
        return key;
    }
}
