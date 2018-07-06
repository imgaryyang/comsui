/**
 * 
 */
package com.suidifu.hathaway.redis.handler.financial;

import com.suidifu.hathaway.redis.entity.financial.FastFinancialContract;

/**
 * @author wukai
 *
 */
public interface FastFinancialContractHandler {
	
	public FastFinancialContract getFastFinancialContractListBy(String financialContractUuid);

}
