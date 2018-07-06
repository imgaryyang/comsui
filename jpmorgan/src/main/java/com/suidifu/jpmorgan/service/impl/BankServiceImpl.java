package com.suidifu.jpmorgan.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.Bank;
import com.suidifu.jpmorgan.service.BankService;

@Service("bankService")
public class BankServiceImpl extends GenericServiceImpl<Bank> implements
		BankService {

	private static Log logger = LogFactory.getLog(BankServiceImpl.class);

	@Override
	@Cacheable("banks")
	public Map<String, String> getCachedBanks() {
		logger.info("start cache banks");
		List<Bank> banks = this.loadAll(Bank.class);
		Map<String, String> bankMap = new HashMap<String, String>();
		for (Bank bank : banks) {
			String bankName = bank.getBankFullName();
			bankMap.put(bankName, bank.getStdBankCode());
		}
		return bankMap;
	}

	@Override
	@CacheEvict(value = "banks", allEntries = true, beforeInvocation=true)
	public void evictCachedBanks() {
		
		logger.info("evict cache for banks");
	}

}
