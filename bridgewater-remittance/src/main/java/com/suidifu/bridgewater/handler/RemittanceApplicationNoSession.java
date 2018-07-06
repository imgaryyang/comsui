package com.suidifu.bridgewater.handler;

import java.util.List;
import java.util.Map;

import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.CommonException;

public interface RemittanceApplicationNoSession {

//	public String receiveCitiGroupCallback_Prototype(String paramsJson);

	/**
	 * 放款计划发送处理后，更新放款信息
	 * @param tradeSchedules 交易日程
	 * @param remittanceApplicationUuid 放款申请uuid
	 * @param requestNo 放款请求唯一编号
	 * @param isSecondRemittance TODO
	 * @param isTransfer TODO
	 */
	void processingAndUpdateRemittanceInfo(List<TradeSchedule> tradeSchedules, String remittanceApplicationUuid, String requestNo, boolean isSecondRemittance, boolean isTransfer);

	void processingAndUpdateRemittanceInfoForResend(TradeSchedule tradeSchedule) throws CommonException;

	void receiveCitiGroupCallbackAfterBusinessValidation(Map<String, String> receiveParams);

	void receiveCitiGroupCallbackAfterQuotaValidation(Map<String, String> receiveParams);
	
	void receiveJpmorganCallback(Map<String, String> receiveParams);

	void receiveCitiGroupAfterResendQuotaValidation(Map<String, String> receiveParams);

	void receiveCitiGroupAfterSecondQuotaValidation(Map<String, String> receiveParams);

}
