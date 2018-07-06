package com.suidifu.citigroup.service;

public interface TableCacheService {

	String tableNameWithFinancialContractUuid(String financialContractUuid);
	
	void cacheEvict(String financialContractUuid);
	
	boolean validFinancialContractIsInBalance(String financialContractUuid);
	
	void cacheContextEvict(String financialContractUuid);
}
