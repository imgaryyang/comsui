package com.suidifu.owlman.microservice.handler;

/**
 * Created by zhusy on 2018/3/12.
 */
public interface ThirdPartyVoucherRepaymentOrderWithReconciliation {

    void generateThirdPartVoucherWithReconciliation(String contractUuid, String repaymentOrderUuid,
            String paymentOrderUuid, int priority);
}
