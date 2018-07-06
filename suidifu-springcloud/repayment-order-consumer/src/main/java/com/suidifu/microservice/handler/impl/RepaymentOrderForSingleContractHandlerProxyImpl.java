package com.suidifu.microservice.handler.impl;

import com.suidifu.hathaway.mq.annotations.MqAsyncRpcMethod;
import com.suidifu.hathaway.mq.annotations.MqRpcBusinessUuid;
import com.suidifu.hathaway.mq.annotations.MqRpcPriority;
import com.suidifu.hathaway.mq.annotations.MqSyncRpcMethod;
import com.suidifu.owlman.microservice.handler.RepaymentOrderForSingleContractHandlerProxy;
import com.zufangbao.sun.api.model.repayment.PaymentOrderRequestModel;
import org.springframework.stereotype.Component;

@Component("repaymentOrderForSingleContractHandlerProxy")
public class RepaymentOrderForSingleContractHandlerProxyImpl implements RepaymentOrderForSingleContractHandlerProxy {

	@Override
	@MqAsyncRpcMethod(beanName="repaymentOrderHandlerProxy",methodName="repaymentOrderSingleForEasyPay")
	public void repaymentOrderSingleForEasyPay(@MqRpcBusinessUuid String contractUuid, String repaymentOrdeUuid, @MqRpcPriority int priority) {
		// TODO Auto-generated method stub

	}

	@Override
	@MqSyncRpcMethod(beanName="repaymentOrderHandlerProxy",methodName="cancelRepaymentOrderForSingleContract")
	public void cancelRepaymentOrderForSingleContract(@MqRpcBusinessUuid String contractUuid, String repaymentOrderUuid, @MqRpcPriority int priority) {
		// TODO Auto-generated method stub

	}

	@Override
	@MqAsyncRpcMethod(beanName="repaymentOrderHandlerProxy",methodName="onlineDeductRepaymentOrder")
	public void onlineDeductPaymentOrderPay(@MqRpcBusinessUuid String contractUuid, String orderUuid, PaymentOrderRequestModel commandModel, String paymentOrderUuid, @MqRpcPriority int priority) {
		// TODO Auto-generated method stub
	}

}
