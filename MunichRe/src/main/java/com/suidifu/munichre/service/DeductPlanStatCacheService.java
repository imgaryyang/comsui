package com.suidifu.munichre.service;

import java.math.BigDecimal;
import java.util.Date;

import com.suidifu.munichre.ActualPaymentDetail;

public interface DeductPlanStatCacheService {
	public String get_deduct_plan_stat_cache_interval_uuid(String financialContractUuid, String paymentChanelUuid, String pgClearingAccount, Date completeTime);
	
	
	public void create_deduct_plan_stat_cache(String financial_contract_uuid, String payment_channel_uuid,
			Integer payment_gateway, String pg_account, String pg_clearing_account, Date completeTime,
			BigDecimal suc_amount, int suc_num, int fail_num, ActualPaymentDetail detail);
	
	public ActualPaymentDetail getDetail(String deductApplicationUuid);


	public void update_count_amount(String deductPlanStatCacheUuid, BigDecimal deltaSucAmount, int deltaSucNum,
			int deltaFailNum, ActualPaymentDetail detail, BigDecimal flag);
	
}
