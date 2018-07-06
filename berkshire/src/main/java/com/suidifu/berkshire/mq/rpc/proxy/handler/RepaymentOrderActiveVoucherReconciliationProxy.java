package com.suidifu.berkshire.mq.rpc.proxy.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.hathaway.mq.annotations.v2.MicroService;
import com.suidifu.owlman.microservice.handler.RepaymentOrderActiveVoucherReconciliation;
import com.zufangbao.sun.yunxin.entity.model.RepaymentOrderReconciliationParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobRepaymentOrderActiveVoucherReconciliation;

@Component("repaymentOrderActiveVoucherReconciliationProxy")
public class RepaymentOrderActiveVoucherReconciliationProxy implements RepaymentOrderActiveVoucherReconciliation{

	@Autowired
	private DstJobRepaymentOrderActiveVoucherReconciliation dstJobRepaymentOrderActiveVoucherReconciliation;

	@MicroService(beanName = "repaymentOrderActiveVoucherReconciliation", methodName = "repaymentOrderRecoverDetails", sync = true, vhostName = "/business", exchangeName = "exchange-business", routingKey = "recon-repayment-order-active")
	public boolean repaymentOrderRecoverDetails(List<RepaymentOrderReconciliationParameters> parameters) {

		return dstJobRepaymentOrderActiveVoucherReconciliation.repayment_order_recover_details(parameters);
	}

}
