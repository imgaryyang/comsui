package com.suidifu.munichre.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.suidifu.munichre.RepaymentPlanDetail;

public interface RemittancePlanStatCacheService {

	String get_remittance_plan_stat_cache_interval_uuid(String financialContractUuid, String paymentChanelUuid,
			Date lastModifiedTime);

	List<RepaymentPlanDetail> getAssetDetails(String financial_contract_uuid, String contract_no);

	BigDecimal getLoanServiceFee(String asset_set_uuid);

	void create_remittance_plan_stat_cache(String financial_contract_uuid, String payment_channel_uuid,
			Integer payment_gateway, Date last_modified_time, BigDecimal suc_amount, int suc_num, int fail_num,
			BigDecimal plan_principal, BigDecimal plan_interest, BigDecimal plan_loan_service_fee,
			BigDecimal plan_amount);

	void update_count_amount(String remittancePlanStatCacheUuid, BigDecimal deltaSucAmount, int deltaSucNum,
			int deltaFailNum, BigDecimal plan_principal, BigDecimal plan_interest, BigDecimal plan_loan_service_fee,
			BigDecimal plan_amount);

}
