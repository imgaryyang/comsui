package com.suidifu.owlman.microservice.handler;

import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import java.util.List;

public interface RepaymentOrderReconciliationHandler {
    //stage 1
    boolean fetchVirtualAccountAndBusinessPaymentVoucherTransfer(List<RepaymentOrderReconciliationParameters> parametersList);

    //stage 2
    boolean repaymentOrderRecoverDetails(List<RepaymentOrderReconciliationParameters> parametersList);

    //stage 3
    boolean unfreezeCapital(List<RepaymentOrderReconciliationParameters> parametersList);
}