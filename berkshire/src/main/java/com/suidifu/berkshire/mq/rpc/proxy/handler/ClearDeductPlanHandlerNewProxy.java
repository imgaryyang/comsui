package com.suidifu.berkshire.mq.rpc.proxy.handler;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.suidifu.hathaway.mq.annotations.v2.MicroService;
import com.suidifu.owlman.microservice.handler.ClearDeductPlanHandler;
import com.zufangbao.sun.utils.ClearingVoucherParameters;
import com.zufangbao.wellsfargo.silverpool.cashauditing.dst.handler.DstJobClearingDeductPlan;
import org.springframework.stereotype.Component;

@Component("clearDeductPlanHandlerNewProxy")
public class ClearDeductPlanHandlerNewProxy implements ClearDeductPlanHandler{

	@Autowired
	private DstJobClearingDeductPlan dstJobClearingDeductPlan;
	
	@MicroService(beanName="clearDeductPlanHandlerProxy",
	methodName="criticalMarker",
	sync=true,
	vhostName="/business",
	exchangeName="exchange-business",
	routingKey = "clear-deduct-plan")
	public Map<String, String> criticalMarker(List<String> deductPlanUuidList) {
		
		return dstJobClearingDeductPlan.criticalMarker(deductPlanUuidList);
	}

	@MicroService(beanName="clearDeductPlanHandlerProxy",
	methodName="reconciliationClearingDeductPlan",
	sync=true,
	vhostName="/business",
	exchangeName="exchange-business",
	routingKey = "clear-deduct-plan")
	public boolean reconciliationClearingDeductPlan(List<ClearingVoucherParameters> clearingVoucherParametersList) {
		
		return dstJobClearingDeductPlan.reconciliationClearingDeductPlan(clearingVoucherParametersList);
	}

}
