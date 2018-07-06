/**
 * 
 */
package com.suidifu.hathaway.redis.handler.impl.financial;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.hathaway.redis.entity.financial.FastFinancialContract;
import com.suidifu.hathaway.redis.handler.financial.FastFinancialContractHandler;
import com.suidifu.hathaway.redis.serivice.financial.FastFinancialContractService;

/**
 * @author wukai
 *
 */
@Component("fastFinancialContractHandlerNoTransaction")
public class FastFinancialContractHandlerImpl implements
		FastFinancialContractHandler {
	
	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Autowired
	private FastFinancialContractService fastFinancialContractService;

	@Override
	public FastFinancialContract getFastFinancialContractListBy(
			String financialContractUuid) {
		
		if(fastFinancialContractService.hasKey(financialContractUuid)){
			return fastFinancialContractService.findOne(financialContractUuid);
		}
		
		return getFastFinancialContractFromDBBy(financialContractUuid);
	}
	private FastFinancialContract getFastFinancialContractFromDBBy(String financialContractUuid) {
		try {
			if(StringUtils.isEmpty(financialContractUuid)) return null;
			String sql	 = "SELECT financial_contract_uuid, ledger_book_no, loan_overdue_start_day,"
					+ " sys_create_penalty_flag, sys_create_statement_flag, sys_normal_deduct_flag,"
					+ " sys_overdue_deduct_flag FROM financial_contract WHERE financial_contract_uuid = :financial_contract_uuid";
			List<FastFinancialContract> queryForList = this.genericDaoSupport.queryForList(sql, "financial_contract_uuid", financialContractUuid, FastFinancialContract.class);
			
			if(CollectionUtils.isEmpty(queryForList)) {
				
				return null;
			}
			return queryForList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
