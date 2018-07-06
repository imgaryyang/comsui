package com.suidifu.citigroup.service;

import java.math.BigDecimal;
import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.citigroup.entity.GeneralBalance;
import com.suidifu.citigroup.entity.GeneralBalanceSqlMode;

public interface GeneralBalanceService extends GenericService<GeneralBalance> {
	
	GeneralBalanceSqlMode getGeneralBalanceSqlModeBy(String financialContractUuid);
	
	void saveGeneralBalance(GeneralBalanceSqlMode generalBalanceSqlMode);
	
	List<GeneralBalanceSqlMode> getGeneralBalanceSqlModeListBy(List<String> financialContractUuids);

	GeneralBalance getGeneralBanlanceByFinancialContractUuid(String financialContractUuid);
	
	String getGeneralBanlanceuUuidByFinancialContractUuid(String financialContractUuid);
	
	String getOldVersionByRelatedFinancialContractUuid(String financialContractUuid);
	
	void updateBankSavingFreezeForFreezing(String financialContractUuid,BigDecimal bankSavingFreeze);
	
	void updatePayAbleAndBankSavingLoan(String financialContractUuid,BigDecimal amount);
	
	BigDecimal getValidBankSavingLoan(String financialContractUuid);
	
	void updateBankSavingFreezeForFreezingSuccess(String financialContractUuid, BigDecimal bankSavingFreeze);
	 
	void updateBankSavingFreezeForFreezingFail(String financialContractUuid, BigDecimal bankSavingFreeze);
	
	void updateBankSavingFreeze(String financialContractUuid, BigDecimal bankSavingFreeze,Boolean isSuccess);
}
