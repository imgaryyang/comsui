package com.zufangbao.earth.yunxin.handler.impl;

import com.zufangbao.earth.handler.PrincipalHandler;
import com.zufangbao.earth.yunxin.handler.FinancialContracCacheHandler;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.FinancialContractConfiguration;
import com.zufangbao.sun.service.FinancialContractConfigurationService;
import com.zufangbao.wellsfargo.yunxin.cache.FinancialContractConfigurationCacheSpec;
import com.zufangbao.wellsfargo.yunxin.cache.FinancialContractVsPrincipleCacheSpec;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("financialContracCacheHandler")
public class FinancialContracCacheHandlerImpl implements FinancialContracCacheHandler {

	private static Log log = LogFactory.getLog(FinancialContracCacheHandlerImpl.class);

	@Autowired
	private PrincipalHandler principalHandler;
	@Autowired
	private FinancialContractConfigurationService financialContractConfigurationService;
	
	@Override
	@Cacheable(value=FinancialContractVsPrincipleCacheSpec.CACHE_KEY,key="#princialId")
	public List<FinancialContract> getAvailableFinancialContractList(Long princialId) {
		return principalHandler.get_can_access_financialContract_list(princialId);
	}

	@Override
	@CacheEvict(value=FinancialContractVsPrincipleCacheSpec.CACHE_KEY,key="#princialId")
	public void cacheEvict(Long princialId) {
		log.info("cacheEvict FinancialContracCache by key[principalId:"+princialId+"]");
	}

	@Override
	@CacheEvict(value=FinancialContractVsPrincipleCacheSpec.CACHE_KEY,allEntries=true)
	public void allCacheEvict() {
		log.info("cacheEvict FinancialContracCache.");
	}

	@Override
	@Cacheable(value=FinancialContractConfigurationCacheSpec.CACHE_KEY,key="#financialContractUuid")
	public Map<String, String> getFinancialContractConfigurationList(String financialContractUuid) {
		List<FinancialContractConfiguration> configurations = financialContractConfigurationService.get_financialContractConfiguration_list(financialContractUuid);

		if(CollectionUtils.isEmpty(configurations))
			return Collections.emptyMap();

		Map<String,String> configMap = new HashMap<String,String>();
		configurations.forEach(a->configMap.put(a.getCode(),a.getContent()));
		return configMap;
	}

	public String getFinancialContractConfigurationContent(String financialContractUuid, String code) {
		Map<String,String> configMap = this.getFinancialContractConfigurationList(financialContractUuid);
		return configMap.get(code);
	}

}
