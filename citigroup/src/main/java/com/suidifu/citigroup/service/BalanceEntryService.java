package com.suidifu.citigroup.service;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.citigroup.entity.BalanceEntry;
import com.suidifu.citigroup.entity.BalanceEntrySqlMode;

public interface BalanceEntryService extends GenericService<BalanceEntry>{
	
	String  distinguishFinancialContractByUuid(String financialContractUuid);
	
	void saveBalanceEntry(BalanceEntrySqlMode balanceEntrySqlMode);

	BalanceEntrySqlMode getRemittanceSqlModeBy(String financialContractUuid, String combinePreventRepetition);
	
	void saveBalanceEntry(List<BalanceEntrySqlMode> balanceEntrySqlModes);
	
	List<BalanceEntrySqlMode> getRemittanceSqlModeByRemittanceApplicationUuid(String financialContractUuid, String remittanceApplicationUuid);
	
	boolean existFreezIngBalanceEntry(String remittancePlanUuid);
	
}
