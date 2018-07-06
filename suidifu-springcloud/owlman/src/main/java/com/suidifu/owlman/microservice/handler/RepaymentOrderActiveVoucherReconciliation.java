package com.suidifu.owlman.microservice.handler;

import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import java.util.List;

/**
 * Created by MieLongJun on 18-2-28.
 */
public interface RepaymentOrderActiveVoucherReconciliation {
    boolean repaymentOrderRecoverDetails(
            List<RepaymentOrderReconciliationParameters> sourceDocumentReconciliationStepThreeParameterList);
}
