package com.suidifu.microservice.handler;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.microservice.entity.SourceDocument;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;

/**
 * @author louguanyang at 2018/3/14 11:47
 * @mail louguanyang@hzsuidifu.com
 */
public interface SourceDocumentHandler {

    public SourceDocument createUnSignedDeductRepaymentOrderSourceDocument(String customerUuid,
        FinancialContract financialContract, RepaymentOrder repaymentOrder, PaymentOrder paymentOrder)
        throws GiottoException;
}
