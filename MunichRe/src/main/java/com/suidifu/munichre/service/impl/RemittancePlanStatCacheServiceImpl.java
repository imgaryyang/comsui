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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.suidifu.munichre.RepaymentPlanDetail;
import com.suidifu.munichre.service.RemittancePlanStatCacheService;
import com.suidifu.munichre.util.DateUtils;

@Component("remittancePlanStatCacheService")
public class RemittancePlanStatCacheServiceImpl extends NamedParameterJdbcDaoSupport implements RemittancePlanStatCacheService {

	@Autowired
	public RemittancePlanStatCacheServiceImpl(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	@Override
	public String get_remittance_plan_stat_cache_interval_uuid(String financialContractUuid, String paymentChanelUuid,Date lastModifiedTime) {
		if(StringUtils.isEmpty(financialContractUuid)|| StringUtils.isEmpty(paymentChanelUuid)||lastModifiedTime==null){
			return null;
		}
		String sql = "SELECT uuid "
				+ "FROM remittance_plan_stat_cache "
				+ "WHERE financial_contract_uuid=:financial_contract_uuid "
				+ "AND payment_channel_uuid=:payment_channel_uuid "
				+ "AND start_time<=:last_modified_time AND end_time>:last_modified_time "
				+ "LIMIT 1";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("financial_contract_uuid", financialContractUuid);
		params.put("payment_channel_uuid", paymentChanelUuid);
		params.put("last_modified_time", lastModifiedTime);
		List<Map<String,Object>> results = getNamedParameterJdbcTemplate().queryForList(sql, params);
		if(CollectionUtils.isEmpty(results)){
			return null;
		}
		return (String)results.get(0).get("uuid");
	}
	
	@Override
	public void update_count_amount(String remittancePlanStatCacheUuid, BigDecimal deltaSucAmount, 
			int deltaSucNum, int deltaFailNum, BigDecimal plan_principal, 
			BigDecimal plan_interest, BigDecimal plan_loan_service_fee, 
			BigDecimal plan_amount) {
		String sql = "UPDATE remittance_plan_stat_cache "
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
		params.put("uuid", remittancePlanStatCacheUuid);
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
	public void create_remittance_plan_stat_cache(String financial_contract_uuid,String payment_channel_uuid,
			Integer payment_gateway,Date last_modified_time,
			BigDecimal suc_amount,int suc_num, 
			int fail_num, BigDecimal plan_principal, 
			BigDecimal plan_interest, BigDecimal plan_loan_service_fee, 
			BigDecimal plan_amount) {
		String sql = "INSERT INTO `remittance_plan_stat_cache` "
				+ "(`uuid`, `financial_contract_uuid`, "
				+ "`payment_channel_uuid`, `payment_gateway`, "
				+ "`start_time`, `end_time`, "
				+ "`suc_amount`, `suc_num`, "
				+ "`fail_num`, `last_modified_time`, "
				+ "`plan_principal`, `plan_interest`, "
				+ "`plan_loan_service_fee`, `plan_amount`) "
				+ "VALUES "
				+ "(:uuid, :financial_contract_uuid, "
				+ ":payment_channel_uuid, :payment_gateway, "
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
		params.put("payment_channel_uuid", payment_channel_uuid);
		params.put("payment_gateway", payment_gateway);
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
	public List<RepaymentPlanDetail> getAssetDetails(String financial_contract_uuid, String contract_no) {
		String sql = "SELECT asset_interest_value assetInterest, "
				+ "asset_uuid assetUuid, "
				+ "asset_fair_value assetAmount "
				+ "FROM asset_set "
				+ "WHERE "
				+ "contract_id IN "
				+ "(SELECT id "
				+ "FROM contract "
				+ "WHERE "
				+ "financial_contract_uuid = :financial_contract_uuid "
				+ "AND contract_no = :contract_no) "
				+ "AND (active_status = '0' "
				+ "OR (active_status = '2' "
				+ "AND repayment_plan_type = '1' "
				+ "AND can_be_rollbacked = '1'))";
		
		Map<String, Object> params = new HashMap<>();
		params.put("financial_contract_uuid", financial_contract_uuid);
		params.put("contract_no", contract_no);
		List<RepaymentPlanDetail> repaymentPlanDetails = getNamedParameterJdbcTemplate().query(sql,params, BeanPropertyRowMapper.newInstance(RepaymentPlanDetail.class));
		return repaymentPlanDetails;
	}
	
	@Override
	public BigDecimal getLoanServiceFee(String assetSetUuid){
		String sql = "SELECT "
				+ "SUM("
				+ "CASE second_account_name "
				+ "WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' "
				+ "THEN account_amount "
				+ "ELSE 0 END) "
				+ "FROM asset_set_extra_charge "
				+ "WHERE asset_set_uuid = :assetSetUuid";
		Map<String, Object> params = new HashMap<>();
		params.put("assetSetUuid", assetSetUuid);
		List<BigDecimal> loanServiceFeeList = getNamedParameterJdbcTemplate().queryForList(sql, params, BigDecimal.class);
		if(CollectionUtils.isEmpty(loanServiceFeeList) || loanServiceFeeList.size() != 1) {
			return BigDecimal.ZERO;
		}
		if (loanServiceFeeList.get(0) == null) {
			return BigDecimal.ZERO;
		}
		return loanServiceFeeList.get(0);
	}
	
}
