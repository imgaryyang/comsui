package com.suidifu.owlman.microservice.enumation;

import com.zufangbao.sun.utils.ApplicationMessageUtils;

/**
 * @author 作者 zhenghangbo
 * @version 创建时间：Nov 16, 2016 5:33:11 PM
 * 类说明
 */
public enum ThirdJournalVoucherType {

    //提前划扣
    ADVANCE("enum.third-journal-voucher-type.advance"),
    //正常
    NORMAL("enum.third-journal-voucher-type.normal"),
    //逾期
    OVERDUE("enum.third-journal-voucher-type.overdue");

    /**
     * @param key
     */


    private String key;


    ThirdJournalVoucherType(String key) {
        this.key = key;

    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }


    /**
     * Get alias of order status
     *
     * @return
     */
    public String getAlias() {
        return this.key.substring(this.key.lastIndexOf(".") + 1);
    }

    public String getChineseMessage() {
        return ApplicationMessageUtils.getChineseMessage(this.getKey());
    }

}
