package com.suidifu.owlman.microservice.handler;

import com.suidifu.owlman.microservice.model.DeductReturnModel;
import com.zufangbao.gluon.exception.repayment.RepaymentOrderDeductCallBackException;


public interface PaymentOrderHandlerProxy {

	void updatePaymentOrderFail(String repaymentOrderUuid, String paymentOrderUuid, int paymentOrderStatus, String remark, int priority);

	void deductNotifyHandle(String contractUuid, String paymentOrderUuid, String orderUuid, DeductReturnModel deductReturnModel, int priority) throws RepaymentOrderDeductCallBackException;

	void cancelPaymentOrder(String businessId, String paymentOrderUuid, int priority);

	String generatePaymentOrder(String repaymentOrderUuid, String modelString, int priority);
}
