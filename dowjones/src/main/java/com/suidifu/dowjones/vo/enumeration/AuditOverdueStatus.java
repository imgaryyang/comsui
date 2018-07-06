package com.suidifu.dowjones.vo.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逾期状态
 *
 * @author louguanyang
 */
@AllArgsConstructor
@Getter
public enum AuditOverdueStatus {
    /**
     * 正常 未逾期
     **/
    NORMAL(0, "正常"),
    /**
     * 待确认
     **/
    UNCONFIRMED(1, "待确认"),
    /**
     * 已逾期
     **/
    OVERDUE(2, "已逾期");
    private int key;
    private String desc;
}