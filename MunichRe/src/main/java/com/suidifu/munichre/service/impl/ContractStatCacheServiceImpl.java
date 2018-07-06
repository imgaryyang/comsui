package com.suidifu.munichre.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.suidifu.munichre.RepaymentPlanDetail;
import com.suidifu.munichre.service.ContractStatCacheService;
import com.suidifu.munichre.util.DateUtils;

@Component("contractStatCacheService")
public class ContractStatCacheServiceImpl  extends NamedParameterJdbcDaoSupport implements ContractStatCacheService {
	
	@Autowired
	public ContractStatCacheServiceImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public String get_contract_stat_cache_interval_uuid(String financialContractUuid, Date lastModifiedTime) {
		String sql = "SELECT uuid "
				+ "FROM contract_stat_cache "
				+ "WHERE financial_contract_uuid=:financial_contract_uuid "
				+ "AND start_time<=:last_modified_time AND end_time>:last_modified_time "
				+ "LIMIT 1";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("financial_contract_uuid", financialContractUuid);
		params.put("last_modified_time", lastModifiedTime);
		List<Map<String,Object>> results = getNamedParameterJdbcTemplate().queryForList(sql, params);
		if(CollectionUtils.isEmpty(results)){
			return null;
		}
		return (String)results.get(0).get("uuid");
	}

	@Override
	public void create_contract_stat_cache(String financial_contract_uuid, Date last_modified_time,
			BigDecimal suc_amount, int suc_num, int fail_num, BigDecimal plan_principal, BigDecimal plan_interest,
			BigDecimal plan_loan_service_fee, BigDecimal plan_amount) {

		String sql = "INSERT INTO `contract_stat_cache` "
				+ "(`uuid`, `financial_contract_uuid`, "
				+ "`start_time`, `end_time`, "
				+ "`suc_amount`, `suc_num`, "
				+ "`fail_num`, `last_modified_time`, "
				+ "`plan_principal`, `plan_interest`, "
				+ "`plan_loan_service_fee`, `plan_amount`) "
				+ "VALUES "
				+ "(:uuid, :financial_contract_uuid, "
				+ ":start_time, :end_time, "
				+ ":suc_amount, :suc_num, "
				+ ":fail_num, SYSDATE(), "
				+ ":plan_principal, :plan_interest, "
				+ ":plan_loan_service_fee, :plan_amount);";
		
		Map<String,Object> params = new HashMap<String,Object>();
		Date start_time_interval = DateUtils.getPreNeighbourHour(last_modified_time);
		Date end_time_interval = DateUtils.getNextNeighbourHour(last_modified_time);
		params.put("uuid", UUID.randomUUID().toString());
		params.put("financial_contract_uuid", financial_contract_uuid);
		params.put("start_time", start_time_interval);
		params.put("end_time", end_time_interval);
		params.put("suc_amount", suc_amount);
		params.put("suc_num", suc_num);
		params.put("fail_num", fail_num);
		params.put("plan_principal", plan_principal);
		params.put("plan_interest", plan_interest);
		params.put("plan_loan_service_fee", plan_loan_service_fee);
		params.put("plan_amount", plan_amount);
		getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	@Override
	public void update_count_amount(String contractStatCacheUuid, BigDecimal deltaSucAmount, 
			int deltaSucNum, int deltaFailNum, BigDecimal plan_principal, 
			BigDecimal plan_interest, BigDecimal plan_loan_service_fee, 
			BigDecimal plan_amount) {
		String sql = "UPDATE contract_stat_cache "
				+ "SET suc_amount = suc_amount+(:deltaSucAmount), "
				+ "suc_num = suc_num+(:deltaSucNum), "
				+ "fail_num = fail_num+(:deltaFailNum), "
				+ "plan_principal = plan_principal+(:plan_principal), "
				+ "plan_interest = plan_interest+(:plan_interest), "
				+ "plan_loan_service_fee = plan_loan_service_fee+(:plan_loan_service_fee), "
				+ "plan_amount = plan_amount+(:plan_amount), "
				+ "last_modified_time = SYSDATE() "
				+ "WHERE uuid = :uuid";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uuid", contractStatCacheUuid);
		params.put("deltaSucAmount", deltaSucAmount);
		params.put("deltaSucNum", deltaSucNum);
		params.put("deltaFailNum", deltaFailNum);
		params.put("plan_principal", plan_principal);
		params.put("plan_interest", plan_interest);
		params.put("plan_loan_service_fee", plan_loan_service_fee);
		params.put("plan_amount", plan_amount);
		
		getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	@Override
	public List<RepaymentPlanDetail> getAssetDetails(String financial_contract_uuid, Long contract_id) {
		String sql = "SELECT asset_interest_value assetInterest, "
				+ "asset_uuid assetUuid, "
				+ "asset_fair_value assetAmount "
				+ "FROM asset_set "
				+ "WHERE "
				+ "contract_id = :contract_id "
				+ "AND financial_contract_uuid = :financial_contract_uuid "
				+ "AND (active_status = '0' "
				+ "OR (active_status = '2' "
				+ "AND repayment_plan_type = '1' "
				+ "AND can_be_rollbacked = '1'))";
		
		Map<String, Object> params = new HashMap<>();
		params.put("financial_contract_uuid", financial_contract_uuid);
		params.put("contract_id", contract_id);
		List<RepaymentPlanDetail> repaymentPlanDetails = getNamedParameterJdbcTemplate().query(sql,params, BeanPropertyRowMapper.newInstance(RepaymentPlanDetail.class));
		return repaymentPlanDetails;
	}
	
	@Override
	public BigDecimal getLoanServiceFee(List<String> assetSetUuids){
        if (CollectionUtils.isEmpty(assetSetUuids)) {
            return BigDecimal.ZERO;
        }

        String sql = "SELECT "
				+ "SUM("
				+ "CASE second_account_name "
				+ "WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' "
				+ "THEN account_amount "
				+ "ELSE 0 END) "
				+ "FROM asset_set_extra_charge "
				+ "WHERE asset_set_uuid IN (:assetSetUuids)";
		Map<String, Object> params = new HashMap<>();
		params.put("assetSetUuids", assetSetUuids);
		List<BigDecimal> loanServiceFeeList = getNamedParameterJdbcTemplate().query(sql,params, new SingleColumnRowMapper(BigDecimal.class));
		if(CollectionUtils.isEmpty(loanServiceFeeList) || loanServiceFeeList.size() != 1 || loanServiceFeeList.get(0) == null) {
			return BigDecimal.ZERO;
		}
		return loanServiceFeeList.get(0);
	}
}
