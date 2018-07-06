package com.suidifu.jpmorgan.service;

import java.util.Map;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.Bank;

public interface BankService extends GenericService<Bank> {

	/**
	 * bank表数据（带缓存）
	 */
	public Map<String, String> getCachedBanks();
	
	/**
	 * 清除bank表数据缓存
	 */
	public void evictCachedBanks();

}
