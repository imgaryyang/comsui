package com.suidifu.munichre.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
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

import com.suidifu.munichre.ActualPaymentDetail;
import com.suidifu.munichre.service.DeductPlanStatCacheService;
import com.suidifu.munichre.util.DateUtils;

@Component("deductPlanStatCacheService")
public class DeductPlanStatCacheServiceImpl extends NamedParameterJdbcDaoSupport implements DeductPlanStatCacheService {
	@Autowired
	public DeductPlanStatCacheServiceImpl(DataSource dataSource){
		setDataSource(dataSource);
	}

	@Override
	public String get_deduct_plan_stat_cache_interval_uuid(String financialContractUuid, String paymentChanelUuid,
			String pgClearingAccount, Date completeTime) {
		if(StringUtils.isEmpty(financialContractUuid)|| StringUtils.isEmpty(paymentChanelUuid)||completeTime==null){
			return null;
		}
		StringBuffer sqlBuffer = new StringBuffer("select uuid from deduct_plan_stat_cache where financial_contract_uuid=:financial_contract_uuid"
				+ " AND payment_channel_uuid=:payment_channel_uuid AND start_time<=:complete_Time AND end_time>:complete_Time");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("financial_contract_uuid", financialContractUuid);
		params.put("payment_channel_uuid", paymentChanelUuid);
		params.put("complete_Time", completeTime);
		if(!StringUtils.isEmpty(pgClearingAccount)){
			sqlBuffer.append(" AND pg_clearing_account=:pg_clearing_account ");
			params.put("pg_clearing_account", pgClearingAccount);
		}else{
			sqlBuffer.append(" AND (pg_clearing_account is null or  pg_clearing_account = '')");
		}
		sqlBuffer.append(" limit 1");
		List<Map<String,Object>> results = getNamedParameterJdbcTemplate().queryForList(sqlBuffer.toString(), params);
		if(CollectionUtils.isEmpty(results)){
			return null;
		}
		return (String)results.get(0).get("uuid");
	}

	@Override
	public void update_count_amount(String deductPlanStatCacheUuid, BigDecimal deltaSucAmount, int deltaSucNum, int deltaFailNum, ActualPaymentDetail detail, BigDecimal flag) {
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		String addSql = "";
		
		if(detail != null && flag != BigDecimal.ZERO) {
				addSql = ", actual_repayment_amount = actual_repayment_amount+(:actualRepaymentAmount), "
					+ "actual_total_fee = actual_total_fee+(:actualTotalFee), "
					+ "actual_principal = actual_principal+(:actualPrincipal), "
					+ "actual_interest = actual_interest+(:actualInterest), "
					+ "actual_loan_service_fee = actual_loan_service_fee+(:actualLoanServiceFee), "
					+ "actual_tech_fee = actual_tech_fee+(:actualTechFee), "
					+ "actual_other_fee = actual_other_fee+(:actualOtherFee), "
					+ "actual_overdue_penalty = actual_overdue_penalty+(:actualOverduePenalty), "
					+ "actual_overdue_default_fee = actual_overdue_default_fee+(:actualOverdueDefaultFee), "
					+ "actual_overdue_service_fee = actual_overdue_service_fee+(:actualOverdueServiceFee), "
					+ "actual_overdue_other_fee = actual_overdue_other_fee+(:actualOverdueOtherFee)";
				
				params.put("actualRepaymentAmount", detail.getActualRepaymentAmount().multiply(flag));
				
				// 扣款成功金额大于0时，此次更新为成功记录
				boolean isSucRecord = deltaSucAmount.compareTo(BigDecimal.ZERO) > 0;
	
				// 仅当此次更新为成功记录时变更下列数据
				params.put("actualTotalFee", isSucRecord?detail.getActualTotalFee().multiply(flag):BigDecimal.ZERO);
				params.put("actualPrincipal", isSucRecord?detail.getActualPrincipal().multiply(flag):BigDecimal.ZERO);
				params.put("actualInterest", isSucRecord?detail.getActualInterest().multiply(flag):BigDecimal.ZERO);
				params.put("actualLoanServiceFee", isSucRecord?detail.getActualLoanServiceFee().multiply(flag):BigDecimal.ZERO);
				params.put("actualTechFee", isSucRecord?detail.getActualTechFee().multiply(flag):BigDecimal.ZERO);
				params.put("actualOtherFee", isSucRecord?detail.getActualOtherFee().multiply(flag):BigDecimal.ZERO);
				params.put("actualOverduePenalty", isSucRecord?detail.getActualOverduePenalty().multiply(flag):BigDecimal.ZERO);
				params.put("actualOverdueDefaultFee", isSucRecord?detail.getActualOverdueDefaultFee().multiply(flag):BigDecimal.ZERO);
				params.put("actualOverdueServiceFee", isSucRecord?detail.getActualOverdueServiceFee().multiply(flag):BigDecimal.ZERO);
				params.put("actualOverdueOtherFee", isSucRecord?detail.getActualOverdueOtherFee().multiply(flag):BigDecimal.ZERO);
		}
		
		String sql = "UPDATE deduct_plan_stat_cache "
				+ "SET "
				+ "suc_amount = suc_amount+(:deltaSucAmount), "
				+ "suc_num = suc_num+(:deltaSucNum), "
				+ "fail_num = fail_num+(:deltaFailNum), "
				+ "last_modified_time = SYSDATE() "
				+ addSql
				+ " WHERE uuid=:uuid";
		params.put("uuid", deductPlanStatCacheUuid);
		params.put("deltaSucAmount", deltaSucAmount);
		params.put("deltaSucNum", deltaSucNum);
		params.put("deltaFailNum", deltaFailNum);
		getNamedParameterJdbcTemplate().update(sql, params);
	}

	@Override
	public void create_deduct_plan_stat_cache(String financial_contract_uuid,String payment_channel_uuid,Integer payment_gateway,String pg_account,String pg_clearing_account,Date completeTime,BigDecimal suc_amount,int suc_num, int fail_num, ActualPaymentDetail detail) {
//		String sql = "INSERT INTO `deduct_plan_stat_cache` (`uuid`, `financial_contract_uuid`, `payment_channel_uuid`, `payment_gateway`, `pg_account`, `pg_clearing_account`, `start_time`, `end_time`, `suc_amount`, `suc_num`, `fail_num`, `last_modified_time`) VALUES"
//				+ " (:uuid, :financial_contract_uuid, :payment_channel_uuid, :payment_gateway,:pg_account, :pg_clearing_account, :start_time, :end_time, :suc_amount, :suc_num, :fail_num, SYSDATE());";
		Map<String,Object> params = new HashMap<String,Object>();
		
		String addColumns = "";
		String addValues = "";
		
		if(detail != null) {
			addColumns = ", `actual_repayment_amount`, "
					+ "`actual_total_fee`, "
					+ "`actual_principal`, "
					+ "`actual_interest`, "
					+ "`actual_loan_service_fee`, "
					+ "`actual_tech_fee`, "
					+ "`actual_other_fee`, "
					+ "`actual_overdue_penalty`, "
					+ "`actual_overdue_default_fee`, "
					+ "`actual_overdue_service_fee`, "
					+ "`actual_overdue_other_fee`";
			
			addValues = ", :actualRepaymentAmount, "
					+ ":actualTotalFee, "
					+ ":actualPrincipal, "
					+ ":actualInterest, "
					+ ":actualLoanServiceFee, "
					+ ":actualTechFee, "
					+ ":actualOtherFee, "
					+ ":actualOverduePenalty, "
					+ ":actualOverdueDefaultFee, "
					+ ":actualOverdueServiceFee, "
					+ ":actualOverdueOtherFee";
			
			params.put("actualRepaymentAmount", detail.getActualRepaymentAmount());
			
			// 扣款成功金额大于0时，此次操作为插入成功记录
			boolean isSucRecord = suc_amount.compareTo(BigDecimal.ZERO) > 0;
			// 仅当此次操作为插入成功记录时记录下列数据
			params.put("actualTotalFee", isSucRecord?detail.getActualTotalFee():BigDecimal.ZERO);
			params.put("actualPrincipal", isSucRecord?detail.getActualPrincipal():BigDecimal.ZERO);
			params.put("actualInterest", isSucRecord?detail.getActualInterest():BigDecimal.ZERO);
			params.put("actualLoanServiceFee", isSucRecord?detail.getActualLoanServiceFee():BigDecimal.ZERO);
			params.put("actualTechFee", isSucRecord?detail.getActualTechFee():BigDecimal.ZERO);
			params.put("actualOtherFee", isSucRecord?detail.getActualOtherFee():BigDecimal.ZERO);
			params.put("actualOverduePenalty", isSucRecord?detail.getActualOverduePenalty():BigDecimal.ZERO);
			params.put("actualOverdueDefaultFee", isSucRecord?detail.getActualOverdueDefaultFee():BigDecimal.ZERO);
			params.put("actualOverdueServiceFee", isSucRecord?detail.getActualOverdueServiceFee():BigDecimal.ZERO);
			params.put("actualOverdueOtherFee", isSucRecord?detail.getActualOverdueOtherFee():BigDecimal.ZERO);
		}
		
		String sql = "INSERT INTO `deduct_plan_stat_cache` "
				+ "(`uuid`, `financial_contract_uuid`, "
				+ "`payment_channel_uuid`, `payment_gateway`, "
				+ "`pg_account`, `pg_clearing_account`, "
				+ "`start_time`, `end_time`, "
				+ "`suc_amount`, `suc_num`, "
				+ "`fail_num`, `last_modified_time`"
				+ addColumns
				+ ")"
				+ " VALUES "
				+ "(:uuid, :financial_contract_uuid, "
				+ ":payment_channel_uuid, :payment_gateway, "
				+ ":pg_account, :pg_clearing_account, "
				+ ":start_time, :end_time, "
				+ ":suc_amount, :suc_num, "
				+ ":fail_num, SYSDATE()"
				+ addValues
				+ ");";
		
		Date start_time_interval = DateUtils.getPreNeighbourHour(completeTime);
		Date end_time_interval = DateUtils.getNextNeighbourHour(completeTime);
		params.put("uuid", UUID.randomUUID().toString());
		params.put("financial_contract_uuid", financial_contract_uuid);
		params.put("payment_channel_uuid", payment_channel_uuid);
		params.put("payment_gateway", payment_gateway);
		params.put("pg_account", pg_account);
		params.put("pg_clearing_account", pg_clearing_account==null?"":pg_clearing_account);
		params.put("start_time", start_time_interval);
		params.put("end_time", end_time_interval);
		params.put("suc_amount", suc_amount);
		params.put("suc_num", suc_num);
		params.put("fail_num", fail_num);
		getNamedParameterJdbcTemplate().update(sql, params);
	}
	
	@Override
	public ActualPaymentDetail getDetail(String deductApplicationUuid) {
		if(StringUtils.isEmpty(deductApplicationUuid)) {
			return null;
		}
		String sql = "SELECT "
				        +"SUM(CASE third_account_name "
								+"WHEN 'TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE' THEN account_amount "
                                +"ELSE 0 "
                            +"END) actualPrincipal, "
                        +"SUM(CASE third_account_name "
                               +"WHEN 'TRD_RECIEVABLE_LOAN_ASSET_INTEREST' THEN account_amount "
                               +"ELSE 0 "
                            +"END) actualInterest, "
                        +"SUM(CASE third_account_name "
                                +"WHEN 'TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE' THEN account_amount "
                                +"ELSE 0 "
                            +"END) actualTechFee, "
                        +"SUM(CASE third_account_name "
                                +"WHEN 'TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount "
                                +"ELSE 0 "
                            +"END) actualLoanServiceFee, "
                        +"SUM(CASE third_account_name "
                                +"WHEN 'TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE' THEN account_amount "
                                +"ELSE 0 "
                            +"END) actualOtherFee, "
                        +"SUM(CASE second_account_name "
                                +"WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount "
                                +"ELSE 0 "
                            +"END) actualOverduePenalty, "
                        +"SUM(CASE third_account_name "
                                +"WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount "
                                +"ELSE 0 "
                            +"END) actualOverdueDefaultFee, "
                        +"SUM(CASE third_account_name "
                                +"WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount "
                                +"ELSE 0 "
                            +"END) actualOverdueServiceFee, "
                        +"SUM(CASE third_account_name "
                                +"WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount "
                                +"ELSE 0 "
                            +"END) actualOverdueOtherFee "
                +"FROM t_deduct_application_detail "
                +"WHERE deduct_application_uuid = :deductApplicationUuid";
		
		Map<String, Object> params = new HashMap<>();
		params.put("deductApplicationUuid", deductApplicationUuid);
		List<ActualPaymentDetail> actualPaymentDetails = getNamedParameterJdbcTemplate().query(sql,params, BeanPropertyRowMapper.newInstance(ActualPaymentDetail.class));
		if(CollectionUtils.isEmpty(actualPaymentDetails) || actualPaymentDetails.size() != 1) {
			return null;
		}
		return actualPaymentDetails.get(0);
	}
	
}
