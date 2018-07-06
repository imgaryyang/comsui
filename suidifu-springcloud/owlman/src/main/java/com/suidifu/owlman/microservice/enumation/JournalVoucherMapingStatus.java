package com.suidifu.owlman.microservice.enumation;

import com.zufangbao.sun.utils.ApplicationMessageUtils;

/**
 * @author 作者 zhenghangbo
 * @version 创建时间：Nov 24, 2016 4:03:35 PM
 * 类说明
 */
public enum JournalVoucherMapingStatus {


    /**
     * 核销中
     */
    WRITE_OFFING("enum.journal-voucher-status.voucher_created"),
    /**
     * 已核销
     */
    WRITE_OFF("enum.journal-voucher-status.voucher_issued");

    private String key;


    /**
     * @param key
     */
    JournalVoucherMapingStatus(String key) {
        this.key = key;

    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    public int getOrdinal() {

        return this.ordinal();
    }

    /**
     * Get alias of order status
     *
     * @return
     */

    public String getChineseMessage() {
        return ApplicationMessageUtils.getChineseMessage(this.getKey());
    }
}
