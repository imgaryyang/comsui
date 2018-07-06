package com.suidifu.citigroup.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.suidifu.citigroup.service.TableCacheService;
import com.zufangbao.sun.entity.financial.FinancialContractConfigurationCode;
import com.zufangbao.sun.service.FinancialContractConfigurationService;

@Component("tableCacheService")
public class TableCacheServiceImpl implements TableCacheService {

	private static Log logger = LogFactory.getLog(TableCacheServiceImpl.class);
	
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	@Override
	@Cacheable(value="tableNamesByCodeCache",key="#financialContractUuid")
	public String tableNameWithFinancialContractUuid(String financialContractUuid) {
		
		String context =  financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid,FinancialContractConfigurationCode.BALANCE_ENTRY_TABLES_GATHER.getCode());
		
		if (StringUtils.isEmpty(context)) {
			return StringUtils.EMPTY;
		}
		return context;
	}
	
	@Override
	@CacheEvict(value="tableNamesByCodeCache",key="#financialContractUuid")
	public void cacheEvict(String financialContractUuid) {
		logger.info("cacheEvict tableNamesByCodeCache by key[financialContractUuid:"+financialContractUuid+"]");
	}
	
	
	@Override
	@Cacheable(value="validFinancialContractIsInBalance",key="#financialContractUuid")
	public boolean validFinancialContractIsInBalance(String financialContractUuid) {
		
		String contextJson = financialContractConfigurationService.getFinancialContractConfigContentContent(financialContractUuid, FinancialContractConfigurationCode.ALLOW_FINANCIAL_CONTRACT_DO_BALANCE.getCode());
		
		if (StringUtils.isEmpty(contextJson)) {
			
			return false;
			
		}
		return true;
	}
	
	@Override
	@CacheEvict(value="validFinancialContractIsInBalance",key="#financialContractUuid")
	public void cacheContextEvict(String financialContractUuid) {
		logger.info("cacheEvict tableNamesByCodeCache by key[financialContractUuid:"+financialContractUuid+"]");
	}
	
}
