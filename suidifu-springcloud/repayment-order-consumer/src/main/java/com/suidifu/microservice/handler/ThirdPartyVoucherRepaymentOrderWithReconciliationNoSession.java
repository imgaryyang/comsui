package com.suidifu.microservice.handler;

/**
 * Created by zhusy on 2017/7/28.
 */
public interface ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession {
    void generateThirdPartVoucherWithReconciliation(String contractUuid,
                                                    String repaymentOrderUuid,
                                                    String paymentOrderUuid,
                                                    int priority);

    void thirdPartVoucherWithReconciliation(String repaymentOrderUuid, String paymentOrderUuid);
}