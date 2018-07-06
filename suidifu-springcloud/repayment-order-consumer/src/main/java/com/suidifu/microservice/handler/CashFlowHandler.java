package com.suidifu.microservice.handler;

import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.repayment.order.PaymentOrder;
import java.util.List;

public interface CashFlowHandler {
    List<CashFlow> getCashFlowBy(String hostAccountNo, PaymentOrder paymentorder, boolean isNeedMatchName, boolean isNeedAssignRemark);
}