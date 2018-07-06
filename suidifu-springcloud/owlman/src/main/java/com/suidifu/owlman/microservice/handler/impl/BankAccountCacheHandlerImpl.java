package com.suidifu.owlman.microservice.handler.impl;

import com.suidifu.owlman.microservice.model.DepositeAccountInfoCache;
import com.suidifu.owlman.microservice.spec.DataStatisticsCacheSpec;
import com.zufangbao.sun.entity.account.DepositeAccountInfo;
import com.zufangbao.sun.entity.account.DepositeAccountType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.ledgerbook.BankAccountCache;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.yunxin.handler.ChartOfAccountProcessor;
import java.util.Collection;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class BankAccountCacheHandlerImpl implements BankAccountCache{

	private static Log log = LogFactory.getLog(BankAccountCacheHandlerImpl.class);

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private ChartOfAccountProcessor chartOfAccountProcessor;

	@Autowired
	private CacheManager cacheManager;
	
	private static long TIMEOUT = 1000 * 60 * 1;
	
	private static long TIMEOUT_BANK_ACCOUNT = 1000 * 60 * 2;
	
	public static long getTIMEOUT() {
		return TIMEOUT;
	}

	public static void setTIMEOUT(long tIMEOUT) {
		TIMEOUT = tIMEOUT;
	}
	
	@Override
	public DepositeAccountInfo extractFirstBankAccountFrom(FinancialContract financialContract){
		HashMap<String,DepositeAccountInfo> bankAccountCache = getBankAccountCache(financialContract);
		return extractFirstBankAccount(bankAccountCache, DepositeAccountType.BANK);
	}
	
	@Override
	public DepositeAccountInfo extractThirdPartyPaymentChannelAccountFrom(FinancialContract financialContract, String ... unionPayKeys){
		HashMap<String,DepositeAccountInfo> bankAccountCache = getBankAccountCache(financialContract);
		if(unionPayKeys !=null && unionPayKeys.length!=0){
			for (String unionPayKey : unionPayKeys) {
				DepositeAccountInfo info = bankAccountCache.get(unionPayKey);
				if(info!=null){
					return info;
				}
			}
		}
		return extractFirstBankAccount(bankAccountCache, DepositeAccountType.UINON_PAY);
	}

	@Override
	public DepositeAccountInfo extractFirstAccrualAccountFrom(FinancialContract financialContract, String accrualKey) {
		HashMap<String,DepositeAccountInfo> bankAccountCache = getBankAccountCache(financialContract);
		DepositeAccountInfo info = bankAccountCache.get(accrualKey);
		if(info!=null && info.getAccount_type()==DepositeAccountType.ACCRUAL){
			return info;
		}
		return null;
	}

	public DepositeAccountInfo extractFirstBankAccount(
			HashMap<String, DepositeAccountInfo> accountSet, DepositeAccountType depositAccountType) {
		
		for(DepositeAccountInfo info:accountSet.values())
		{
			if(info!=null&&info.getAccount_type()!=null&&info.getAccount_type().equals(depositAccountType))
				return info;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String,DepositeAccountInfo>  getBankAccountCache(String financialContractUuid) {
		if(StringUtils.isEmpty(financialContractUuid)) return new HashMap<String, DepositeAccountInfo>();
		
		Cache cache = cacheManager.getCache(DataStatisticsCacheSpec.BANK_ACCOUNT_VS_FINANCIAL_CONTRACT_CACHE_KEY);
		Cache.ValueWrapper cacheWrapper = cache.get(financialContractUuid);
		if(cacheWrapper != null){
			HashMap<String,DepositeAccountInfo> data = (HashMap<String,DepositeAccountInfo>)cacheWrapper.get();
			if(data!=null ){
				log.debug("get RepaymentData from cache.");
				return data;
			}
		}
		
		FinancialContract contract= financialContractService.getFinancialContractBy(financialContractUuid);
		return getBankAccountCache(contract);
	}
	
	@Override
	public HashMap<String,DepositeAccountInfo>  getBankAccountCache(FinancialContract contract) {
		if(contract==null||StringUtils.isEmpty(contract.getFinancialContractUuid())) return new HashMap<String, DepositeAccountInfo>();
		
		Cache cache = cacheManager.getCache(DataStatisticsCacheSpec.BANK_ACCOUNT_VS_FINANCIAL_CONTRACT_CACHE_KEY);
		Cache.ValueWrapper cacheWrapper = cache.get(contract.getFinancialContractUuid());
		if(cacheWrapper != null){
			DepositeAccountInfoCache data = (DepositeAccountInfoCache)cacheWrapper.get();
			if(data!=null && data.getAccountInfoMap()!=null && !data.isExpired(TIMEOUT_BANK_ACCOUNT)){
				log.debug("get RepaymentData from cache.");
				return data.getAccountInfoMap();
			}
		}
		
		HashMap<String,DepositeAccountInfo> accountSet= financialContractService.extractDepositeAccount(contract);
		refresh_entry_book(accountSet.values());
		cache.put(contract.getFinancialContractUuid(), new DepositeAccountInfoCache(accountSet,System.currentTimeMillis()));
		return accountSet;
	}
	
	public void  refresh_entry_book(Collection<DepositeAccountInfo> accountSide)
	{
		chartOfAccountProcessor.refresh_entry_book(accountSide);
	}


}
