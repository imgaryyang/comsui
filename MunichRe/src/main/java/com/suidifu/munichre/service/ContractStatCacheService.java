package com.suidifu.munichre.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.suidifu.munichre.RepaymentPlanDetail;

public interface ContractStatCacheService {

	String get_contract_stat_cache_interval_uuid(String financialContractUuid, Date lastModifiedTime);
	
	void create_contract_stat_cache(String financial_contract_uuid, Date last_modified_time, BigDecimal suc_amount, 
			int suc_num, int fail_num, BigDecimal plan_principal, BigDecimal plan_interest, BigDecimal plan_loan_service_fee,
			BigDecimal plan_amount);

	List<RepaymentPlanDetail> getAssetDetails(String financial_contract_uuid, Long contract_id);

	BigDecimal getLoanServiceFee(List<String> assetSetUuids);

	void update_count_amount(String contractStatCacheUuid, BigDecimal deltaSucAmount, int deltaSucNum,
			int deltaFailNum, BigDecimal plan_principal, BigDecimal plan_interest, BigDecimal plan_loan_service_fee,
			BigDecimal plan_amount);
}
