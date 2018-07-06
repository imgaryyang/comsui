package com.suidifu.owlman.microservice.enumation;

import com.zufangbao.sun.BasicEnum;
import com.zufangbao.sun.utils.ApplicationMessageUtils;

/**
 * @author 作者 zhenghangbo
 * @version 创建时间：Nov 16, 2016 5:21:28 PM
 * 类说明
 */
public enum SecondJournalVoucherType implements BasicEnum {
    ONLINE_PAYMENT_BILL("enum.second-journal-voucher-type.online-payment-bill"),
    INTERFACE_PAYMNET_BILL("enum.second-journal-voucher-type.interface-payment-bil"),
    APP_UPLOAD_PAYMENT_BILL("enum.second-journal-voucher-type.app-upload-payment-bill");

    private String key;


    SecondJournalVoucherType(String key) {
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