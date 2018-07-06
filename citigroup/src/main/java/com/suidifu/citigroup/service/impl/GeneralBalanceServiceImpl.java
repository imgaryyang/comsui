package com.suidifu.citigroup.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.demo2do.core.persistence.support.Filter;
import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.citigroup.api.util.ApiSignUtils;
import com.suidifu.citigroup.entity.GeneralBalance;
import com.suidifu.citigroup.entity.GeneralBalanceSqlMode;
import com.suidifu.citigroup.service.GeneralBalanceService;

@Service("generalBalanceService")
public class GeneralBalanceServiceImpl extends GenericServiceImpl<GeneralBalance> implements GeneralBalanceService {

	@Override
	public GeneralBalance getGeneralBanlanceByFinancialContractUuid(String financialContractUuid) {
		
		if (StringUtils.isEmpty(financialContractUuid)) {
			return null;
		}
		
		Filter filter = new Filter();
		
		filter.addEquals("relatedFinancialContractUuid",financialContractUuid );
		
		List<GeneralBalance> list = this.list(GeneralBalance.class, filter);
		
		if (CollectionUtils.isNotEmpty(list)) {

			return	list.get(0);
			
		}
		
		return null;
	}


	@Override
	public String getOldVersionByRelatedFinancialContractUuid(String financialContractUuid) {
		
		if (StringUtils.isEmpty(financialContractUuid)) {
			
			return StringUtils.EMPTY;
			
		}
		
		String sql = "select version_lock from general_balance where related_financial_contract_uuid =:financialContractUuid";
		
		List<String> versionLocks = this.genericDaoSupport.queryForSingleColumnList(sql, "financialContractUuid", financialContractUuid, String.class);
		
		if (CollectionUtils.isNotEmpty(versionLocks)) {
			
			return versionLocks.get(0);
			
		}
		return StringUtils.EMPTY;
	}
	/**
	 * 传入的bankSavingFreeze金额需要调用者根据需求精确正负值
	 */
	@Override
	public void updateBankSavingFreezeForFreezing(String financialContractUuid, BigDecimal bankSavingFreeze) {
		
		if (StringUtils.isEmpty(financialContractUuid)||null==bankSavingFreeze) {
			return;
		}
		Map<String, Object> params = new HashMap<>();
		StringBuffer sql = new StringBuffer("UPDATE general_balance set  bank_saving_freeze = bank_saving_freeze+:bankSavingFreeze ");
		String oldVersion = this.getOldVersionByRelatedFinancialContractUuid(financialContractUuid);
		String newVersion = UUID.randomUUID().toString();
		sql.append(" ,bank_saving_loan = bank_saving_loan-:bankSavingFreeze ");
		
		sql.append(",lastest_modified_time = :lastestModifiedTime,version_lock=:newVersion where related_financial_contract_uuid = :financialContractUuid and version_lock = :oldVersion");
		
		params.put("financialContractUuid",financialContractUuid );
		params.put("oldVersion",oldVersion );
		params.put("newVersion", newVersion);
		params.put("bankSavingFreeze",bankSavingFreeze );
		params.put("lastestModifiedTime",new Date() );
		
		this.genericDaoSupport.executeSQL(sql.toString(), params);

		String versionLock = this.getOldVersionByRelatedFinancialContractUuid(financialContractUuid);
		
		if (!newVersion.equals(versionLock)) {
			
			throw new RuntimeException("GeneralBalance with version_lock update fail! with related_financial_contract_uuid:[" + financialContractUuid + "]");
			
		}
		
	}
	
	/**
	 * 传入的bankSavingFreeze金额需要调用者根据需求精确正负值
	 */
	@Override
	public void updateBankSavingFreeze(String financialContractUuid, BigDecimal bankSavingFreeze,Boolean isSuccess) {
		
		if (StringUtils.isEmpty(financialContractUuid)||null==bankSavingFreeze) {
			return;
		}
		if (null==isSuccess) {
			this.updateBankSavingFreezeForFreezing(financialContractUuid, bankSavingFreeze);
		}else if (isSuccess) {
			this.updateBankSavingFreezeForFreezingSuccess(financialContractUuid, bankSavingFreeze);
		}else {
			this.updateBankSavingFreezeForFreezingFail(financialContractUuid, bankSavingFreeze);
		}
	}
	
	
	
	/**
	 * 传入的bankSavingFreeze金额需要调用者根据需求精确正负值
	 */
	@Override
	public void updateBankSavingFreezeForFreezingSuccess(String financialContractUuid, BigDecimal bankSavingFreeze) {
		
		if (StringUtils.isEmpty(financialContractUuid)||null==bankSavingFreeze) {
			return;
		}
		Map<String, Object> params = new HashMap<>();
		StringBuffer sql = new StringBuffer("UPDATE general_balance set  bank_saving_freeze = bank_saving_freeze-:bankSavingFreeze ");
		String oldVersion = this.getOldVersionByRelatedFinancialContractUuid(financialContractUuid);
		String newVersion = UUID.randomUUID().toString();
		sql.append(" ,pay_able = pay_able+:bankSavingFreeze ");
		
		sql.append(",lastest_modified_time = :lastestModifiedTime,version_lock=:newVersion where related_financial_contract_uuid = :financialContractUuid and version_lock = :oldVersion");
		
		params.put("financialContractUuid",financialContractUuid );
		params.put("oldVersion",oldVersion );
		params.put("newVersion", newVersion);
		params.put("bankSavingFreeze",bankSavingFreeze );
		params.put("lastestModifiedTime",new Date() );
		
		this.genericDaoSupport.executeSQL(sql.toString(), params);

		String versionLock = this.getOldVersionByRelatedFinancialContractUuid(financialContractUuid);
		
		if (!newVersion.equals(versionLock)) {
			
			throw new RuntimeException("GeneralBalance with version_lock update fail! with related_financial_contract_uuid:[" + financialContractUuid + "]");
			
		}
		
	}
	
	
	/**
	 * 传入的金额为正值
	 * @param financialContractUuid
	 * @param bankSavingFreeze
	 */
	
	public void updateBankSavingFreezeForFreezingFail(String financialContractUuid, BigDecimal bankSavingFreeze) {
		
		if (StringUtils.isEmpty(financialContractUuid)||null==bankSavingFreeze) {
			return;
		}
		Map<String, Object> params = new HashMap<>();
		StringBuffer sql = new StringBuffer("UPDATE general_balance set  bank_saving_freeze = bank_saving_freeze-:bankSavingFreeze ");
		String oldVersion = this.getOldVersionByRelatedFinancialContractUuid(financialContractUuid);
		String newVersion = UUID.randomUUID().toString();
		sql.append(" ,bank_saving_loan = bank_saving_loan+:bankSavingFreeze ");
		sql.append(",lastest_modified_time = :lastestModifiedTime,version_lock=:newVersion where related_financial_contract_uuid = :financialContractUuid and version_lock = :oldVersion");
		
		params.put("financialContractUuid",financialContractUuid );
		params.put("oldVersion",oldVersion );
		params.put("newVersion", newVersion);
		params.put("bankSavingFreeze",bankSavingFreeze );
		params.put("lastestModifiedTime",new Date() );
		
		this.genericDaoSupport.executeSQL(sql.toString(), params);

		String versionLock = this.getOldVersionByRelatedFinancialContractUuid(financialContractUuid);
		
		if (!newVersion.equals(versionLock)) {
			
			throw new RuntimeException("GeneralBalance with version_lock update fail! with related_financial_contract_uuid:[" + financialContractUuid + "]");
			
		}
		
	}
	
	
	@Override
	public GeneralBalanceSqlMode getGeneralBalanceSqlModeBy(String financialContractUuid) {
		if (StringUtils.isEmpty(financialContractUuid)) {
			return null;
		}
		StringBuffer sql = new StringBuffer("select id ");
		
		Map<String, Object> parameters = new HashMap<>();
		
		List<String> colunms = ApiSignUtils.getColumeName(GeneralBalanceSqlMode.class);
		
		colunms.forEach(colunm -> sql.append(","+colunm));
		
		sql.append(" from general_balance where related_financial_contract_uuid =:financialContractUuid ");
		
		parameters.put("financialContractUuid", financialContractUuid);
		List<GeneralBalanceSqlMode> remittanceSqlModes = genericDaoSupport.queryForList(sql.toString(), parameters, GeneralBalanceSqlMode.class, 0, 1);
		if (CollectionUtils.isNotEmpty(remittanceSqlModes)) {
			return remittanceSqlModes.get(0);
		} 
		return null;
	}

	@Override
	public List<GeneralBalanceSqlMode> getGeneralBalanceSqlModeListBy(List<String> financialContractUuids) {
		if (CollectionUtils.isEmpty(financialContractUuids)) {
			return null;
		}
		StringBuffer sql = new StringBuffer("select id ");
		
		Map<String, Object> parameters = new HashMap<>();
		
		List<String> colunms = ApiSignUtils.getColumeName(GeneralBalanceSqlMode.class);
		
		colunms.forEach(colunm -> sql.append(","+colunm));
		
		sql.append(" from general_balance where related_financial_contract_uuid in (:financialContractUuids) ");
		
		parameters.put("financialContractUuids", financialContractUuids);
		
		List<GeneralBalanceSqlMode> remittanceSqlModes = genericDaoSupport.queryForList(sql.toString(), parameters, GeneralBalanceSqlMode.class);
		if (CollectionUtils.isNotEmpty(remittanceSqlModes)) {
			return remittanceSqlModes;
		} 
			return Collections.emptyList();
	}

	@Override
	public void updatePayAbleAndBankSavingLoan(String financialContractUuid, BigDecimal amount) {
		
		if (StringUtils.isEmpty(financialContractUuid)||null==amount) {
			return;
		}
		Map<String, Object> params = new HashMap<>();
		
		String oldVersion = this.getOldVersionByRelatedFinancialContractUuid(financialContractUuid);

		String newVersion = UUID.randomUUID().toString();
		
		params.put("financialContractUuid",financialContractUuid );
		params.put("oldVersion",oldVersion );
		params.put("newVersion", newVersion);
		params.put("amount",amount );
		params.put("lastestModifiedTime",new Date() );
		
		String sql = "UPDATE general_balance set bank_saving_loan = bank_saving_loan+:amount, pay_able = pay_able-:amount,lastest_modified_time = :lastestModifiedTime,version_lock=:newVersion where related_financial_contract_uuid = :financialContractUuid and version_lock = :oldVersion";
		
		this.genericDaoSupport.executeSQL(sql, params);

		String versionLock = this.getOldVersionByRelatedFinancialContractUuid(financialContractUuid);
		
		if (!newVersion.equals(versionLock)) {
			
			throw new RuntimeException("GeneralBalance with version_lock update fail! with related_financial_contract_uuid:[" + financialContractUuid + "]");
			
		}
		
	}

	@Override
	public void saveGeneralBalance(GeneralBalanceSqlMode generalBalanceSqlMode){
		
		if (null==generalBalanceSqlMode) {
			return;
		}
		StringBuffer sql =new StringBuffer( "insert into general_balance set ");
		
		Map<String, Object> params;
		
		try {
			params = ApiSignUtils.getColumesMap(generalBalanceSqlMode);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException("insert into general_balance occur error  relatedFinancialContractUuid["+generalBalanceSqlMode.getRelatedFinancialContractUuid()+"]");
		}
		
		for(Map.Entry<String, Object> map:params.entrySet()){
			
			if (null!=map.getValue()) {
				sql.append(map.getKey()).append(" =:").append(map.getKey()).append(",");
			}
			
		}
		this.genericDaoSupport.executeSQL(sql.toString().substring(0,sql.length()-1), params);
	}

	@Override
	public BigDecimal getValidBankSavingLoan(String financialContractUuid) {
		
		
		if (StringUtils.isEmpty(financialContractUuid)) {
			
			return null;
			
		}
		
		String sql = "select bank_saving_loan from general_balance where related_financial_contract_uuid =:financialContractUuid";
		
		List<BigDecimal> payAbles = this.genericDaoSupport.queryForSingleColumnList(sql, "financialContractUuid", financialContractUuid, BigDecimal.class);
		
		if (CollectionUtils.isNotEmpty(payAbles)) {
			
			return payAbles.get(0);
			
		}
		 return null;
	}

	@Override
	public String getGeneralBanlanceuUuidByFinancialContractUuid(String financialContractUuid) {
		if (StringUtils.isEmpty(financialContractUuid)) {
			
			return StringUtils.EMPTY;
			
		}
		
		String sql = "select uuid from general_balance where related_financial_contract_uuid =:financialContractUuid";
		
		List<String> uuids = this.genericDaoSupport.queryForSingleColumnList(sql, "financialContractUuid", financialContractUuid, String.class);
		
		if (CollectionUtils.isNotEmpty(uuids)) {
			
			return uuids.get(0);
			
		}
		return StringUtils.EMPTY;
	}
	
}

