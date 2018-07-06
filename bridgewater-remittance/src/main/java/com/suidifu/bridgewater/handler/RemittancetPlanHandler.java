package com.suidifu.bridgewater.handler;

import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.CommonException;

public interface RemittancetPlanHandler {
	
	public TradeSchedule saveRemittanceInfoBeforeResendForFailedPlan(String remittancePlanUuid) throws CommonException;

	void updateRemittancePlanExecLogAndDetailAfterSendFailBy(String remittancePlanUuid, String remittanceExecReqNo, String remittanceApplicationDetailUuid, String remittanceApplicationUuid, String executionRemark);

	void updateRemittanceStatusAfterResendSuccessBy(String remittanceApplicationUuid, String remittanceDetailUuid, String remittancePlanUuid);
	
}
