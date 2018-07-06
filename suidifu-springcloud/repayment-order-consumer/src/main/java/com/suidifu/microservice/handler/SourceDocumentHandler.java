package com.suidifu.microservice.handler;

import com.suidifu.giotto.exception.GiottoException;
import com.suidifu.microservice.entity.SourceDocument;
import com.zufangbao.sun.entity.customer.Customer;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;
import java.math.BigDecimal;

public interface SourceDocumentHandler {
    SourceDocument createUnSignedDeductRepaymentOrderSourceDocument(String customerUuid,
                                                                    FinancialContract financialContract,
                                                                    RepaymentOrder repaymentOrder,
                                                                    PaymentOrder paymentOrder) throws GiottoException;

    SourceDocument createDepositeReceipt(CashFlow cashFlow,
                                         Long companyId,
                                         BigDecimal amount,
                                         Customer customer,
                                         String relatedContractUuid,
                                         String financialContractUuid,
                                         String virtualAccountNo,
                                         String remark);
}