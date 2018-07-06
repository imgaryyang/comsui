package com.suidifu.microservice.handler;


import com.suidifu.giotto.exception.GiottoException;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import com.zufangbao.sun.entity.repayment.order.RepaymentOrder;

public interface ThirdPartVoucherV2_0Handler {

	public String createDeductRepaymentOrderSourceDocumentUuid(RepaymentOrder repaymentOrder,
      PaymentOrder paymentOrder) throws GiottoException;

}
