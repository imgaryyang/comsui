package com.suidifu.citigroup.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.citigroup.api.util.ApiSignUtils;
import com.suidifu.citigroup.entity.BalanceEntry;
import com.suidifu.citigroup.entity.BalanceEntrySqlMode;
import com.suidifu.citigroup.entity.RemittanceChartOfAccounts;
import com.suidifu.citigroup.service.BalanceEntryService;
import com.suidifu.citigroup.service.TableCacheService;

@Service("balanceEntryService")
public class BalanceEntryServiceImpl extends GenericServiceImpl<BalanceEntry> implements BalanceEntryService {
	
	private final String tableName = "balance_entry";
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired
	private TableCacheService tableCacheService;
	
	@Override
	public BalanceEntrySqlMode getRemittanceSqlModeBy(String financialContractUuid, String combinePreventRepetition) {
		if (StringUtils.isEmpty(combinePreventRepetition)) {
			return null;
		}
		StringBuffer sql = new StringBuffer("select id ");
		
		Map<String, Object> parameters = new HashMap<>();
		
		List<String> colunms = ApiSignUtils.getColumeName(BalanceEntrySqlMode.class);
		
		colunms.forEach(colunm -> sql.append(","+colunm));
		
		String tableName = distinguishFinancialContractByUuid(financialContractUuid);
		
		sql.append(" from "+tableName+" where combine_prevent_repetition =:combinePreventRepetition");
		
		parameters.put("combinePreventRepetition", combinePreventRepetition);

		List<BalanceEntrySqlMode> balanceEntrySqlMods = genericDaoSupport.queryForList(sql.toString(), parameters, BalanceEntrySqlMode.class, 0, 1);
		if (CollectionUtils.isEmpty(balanceEntrySqlMods)) {
			return null;
		} else {
			return balanceEntrySqlMods.get(0);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<BalanceEntrySqlMode> getRemittanceSqlModeByRemittanceApplicationUuid(String financialContractUuid, String remittanceApplicationUuid) {
		if (StringUtils.isEmpty(remittanceApplicationUuid)) {
			return null;
		}
		StringBuffer sql = new StringBuffer("select id ");
		
		Map<String, Object> parameters = new HashMap<>();
		
		List<String> colunms = ApiSignUtils.getColumeName(BalanceEntrySqlMode.class);
		
		colunms.forEach(colunm -> sql.append(","+colunm));
		
		String tableName = distinguishFinancialContractByUuid(financialContractUuid);
		
		sql.append(" from "+tableName+" where remittance_application_uuid =:remittanceApplicationUuid");
		
		parameters.put("remittanceApplicationUuid", remittanceApplicationUuid);

		List<BalanceEntrySqlMode> balanceEntrySqlMods = genericDaoSupport.queryForList(sql.toString(), parameters, BalanceEntrySqlMode.class, 0, 1);
		if (CollectionUtils.isEmpty(balanceEntrySqlMods)) {
			return ListUtils.EMPTY_LIST;
		} else {
			return balanceEntrySqlMods;
		}
	}

	@Override
	public void saveBalanceEntry(BalanceEntrySqlMode balanceEntrySqlMode) {
		
		if (null==balanceEntrySqlMode) {
			
			return;
			
		}

		String tableName = distinguishFinancialContractByUuid(balanceEntrySqlMode.getFinancialContractUuid());
		
		StringBuffer sql =new StringBuffer( " insert into "+tableName+" set ");
		
		Map<String, Object> params;
		
		try {
			
			params = ApiSignUtils.getColumesMap(balanceEntrySqlMode);
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			
			throw new RuntimeException("insert into balance_entry occur error  remittancePlanUuid["+balanceEntrySqlMode.getRemittancePlanUuid()+"]");
		}
		
		for(Map.Entry<String, Object> map:params.entrySet()){
			
			if (null!=map.getValue()) {
				
				sql.append(map.getKey()).append(" =:").append(map.getKey()).append(",");
				
			}
			
		}
		this.genericDaoSupport.executeSQL(sql.toString().substring(0,sql.length()-1), params);
		
	}

	@Override
	public void saveBalanceEntry(List<BalanceEntrySqlMode> balanceEntrySqlModes) {
		
		if (CollectionUtils.isEmpty(balanceEntrySqlModes)) {
			
			return;
		}
		String tableName = distinguishFinancialContractByUuid(balanceEntrySqlModes.get(0).getFinancialContractUuid());
		
		try {
			
			doBatchInsert(tableName, balanceEntrySqlModes);
			
		} catch (IllegalArgumentException | IllegalAccessException e) {
			
			throw new RuntimeException("insert into balance_entry occur error  genenalBalanceUuid["+balanceEntrySqlModes.get(0).getGeneralBalanceUuid()+"]");
		}
		
	}


	@Override
	public String distinguishFinancialContractByUuid(String financialContractUuid) {
		
		String name = tableCacheService.tableNameWithFinancialContractUuid(financialContractUuid);
		
		return StringUtils.isEmpty(name)?tableName:name;
	}


	@Override
	public boolean existFreezIngBalanceEntry(String remittancePlanUuid) {
		if (StringUtils.isEmpty(remittancePlanUuid)) {
			
			return false;
		}
		
		String sql = "select uuid from balance_entry where remittance_plan_uuid = :remittancePlanUuid and combine_prevent_repetition = :combinePreventRepetition";
		
		Map<String, Object> params = new HashMap<>();
		params.put("remittancePlanUuid",remittancePlanUuid );
		params.put("combinePreventRepetition", remittancePlanUuid+"_"+RemittanceChartOfAccounts.TRD_BANK_SAVING_REMITTANCE_BLOCKED_FUND_CODE+"_"+RemittanceChartOfAccounts.FREEZING);
		
		List<String> uuids = this.genericDaoSupport.queryForSingleColumnList(sql, params, String.class);
		
		if (CollectionUtils.isEmpty(uuids)) {
			return false;
		}
		return true;
		
	}

	private void doBatchInsert(String tableName,List<BalanceEntrySqlMode> balanceEntrySqlModes) throws IllegalArgumentException, IllegalAccessException{
		
		StringBuffer combineSql =new StringBuffer("");
		
		StringBuffer sql =new StringBuffer( " insert into "+tableName+" ( ");
		
		List<String> colnums = ApiSignUtils.getColumeName(BalanceEntrySqlMode.class);
		
		colnums.forEach(colnum->sql.append(colnum+","));
		
		combineSql.append(sql.subSequence(0, sql.length()-1));
		
		combineSql.append(") values ( ");
		
		colnums.forEach(colnum -> combineSql.append("? ,"));
		
		List<Map<String,Object>> columeValuesMap = ApiSignUtils.getValuesInList(balanceEntrySqlModes);
		
		this.jdbcTemplate.getJdbcOperations().batchUpdate(combineSql.subSequence(0, combineSql.length()-1)+") ",new BatchPreparedStatementSetter(){
			
			  @Override
	            public void setValues(PreparedStatement ps, int i) throws SQLException {
				  		Map<String,Object> columeValues = columeValuesMap.get(i);
				  		int index = 1;
				  		for(Map.Entry<String, Object> entry:columeValues.entrySet()){
	                	
				  			Object object = entry.getValue();
				  			
				  			ps.setObject(index, object);
	                	
				  			index++;
	                }
	            }
	                    
	            @Override
	            public int getBatchSize() {
	                return columeValuesMap.size();
	            }			
		} );
	}
	
	
}
