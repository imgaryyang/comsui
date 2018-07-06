package com.suidifu.owlman.microservice.handler;

import java.util.List;
import java.util.Map;
import com.zufangbao.sun.utils.ClearingVoucherParameters;


public interface ClearDeductPlanHandler {
	Map<String, String> criticalMarker(List<String> deductPlanUuidList);
	boolean reconciliationClearingDeductPlan(List<ClearingVoucherParameters>  clearingVoucherParametersList);
}
