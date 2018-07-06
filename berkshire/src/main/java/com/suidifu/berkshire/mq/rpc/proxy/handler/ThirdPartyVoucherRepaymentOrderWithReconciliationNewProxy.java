package com.suidifu.berkshire.mq.rpc.proxy.handler;

import com.suidifu.hathaway.mq.annotations.v2.MicroService;
import com.suidifu.owlman.microservice.handler.ThirdPartyVoucherRepaymentOrderWithReconciliation;
import com.zufangbao.wellsfargo.yunxin.handler.ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhusy on 2018/3/12.
 */
@Component("thirdPartyVoucherRepaymentOrderWithReconciliationNewProxy")
public class ThirdPartyVoucherRepaymentOrderWithReconciliationNewProxy implements
    ThirdPartyVoucherRepaymentOrderWithReconciliation {

    @Autowired
    private ThirdPartyVoucherRepaymentOrderWithReconciliationNoSession thirdPartyVoucherRepaymentOrderWithReconciliationNoSession;

    @Override
    @MicroService(beanName="thirdPartyVoucherRepaymentOrderWithReconciliation",
        methodName="generateThirdPartVoucherWithReconciliation",
        sync=true,
        vhostName="/business",
        exchangeName="exchange-business",
        routingKey = "recon-repayment-order-deduct")
    public void generateThirdPartVoucherWithReconciliation(String contractUuid, String repaymentOrderUuid,
        String paymentOrderUuid, int priority) {
        thirdPartyVoucherRepaymentOrderWithReconciliationNoSession.generateThirdPartVoucherWithReconciliation(contractUuid, repaymentOrderUuid, paymentOrderUuid, priority);
    }
}
