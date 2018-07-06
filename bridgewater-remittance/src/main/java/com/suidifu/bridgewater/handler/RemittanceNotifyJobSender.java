package com.suidifu.bridgewater.handler;

import java.util.List;

import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittancePlanInfo;

public interface RemittanceNotifyJobSender {

	/**
	 * 
	 * 业务校验
	 */
	void pushJobToCitigroupForBusinessValidation(RemittanceCommandModel model);
	
	/**
	 * 
	 * 限额校验
	 */
	void pushJobToCitigroupForQuotaValidation(String remittanceApplicationUuid, String financialContractUuid,
			List<RemittancePlanInfo> remittancePlanInfoList, RemittanceCommandModel model, String checkRequestNo,
			List<TradeSchedule> tradeSchedules);
	
	/**
	 * 
	 * 解冻限额
	 * @param callBackUrl TODO
	 */
	void pushJobToCitigroupForUnFreezeQuota(String remittanceApplicationUuid, String financialContractUuid,
			List<RemittancePlanInfo> remittancePlanInfoList);
	/**
	 *
	 * 重新放款限额校验
	 * @param tradeSchedule TODO
	 */
	void pushJobToCitigroupForResendQuotaValidation(String remittanceApplicationUuid, String financialContractUuid,
			RemittancePlanInfo remittancePlanInfo, TradeSchedule tradeSchedule);

	/**
	 *
	 * 二次放款限额校验
	 * @param tradeSchedules TODO
	 */
	void pushJobToCitigroupForSecondRemittanceQuotaValidation(String remittanceApplicationUuid, String financialContractUuid,
			List<RemittancePlanInfo> remittancePlanInfos, List<TradeSchedule> tradeSchedules);

	void pushJobToCitigroupForResendUnFreezeQuota(String remittanceApplicationUuid, String financialContractUuid,
			RemittancePlanInfo remittancePlanInfo);

	void pushJobToCitigroupForSecondUnFreezeQuota(String remittanceApplicationUuid, String financialContractUuid,
			List<RemittancePlanInfo> remittancePlanInfoList);






}
