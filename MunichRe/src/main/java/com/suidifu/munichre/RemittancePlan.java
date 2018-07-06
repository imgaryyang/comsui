package com.suidifu.munichre;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.suidifu.munichre.StaticsConfig.TransactionCommandExecutionStatus;
import com.suidifu.munichre.config.MqProducer;
import com.suidifu.munichre.service.RemittancePlanStatCacheService;
import com.zufangbao.canal.core.rowprocesser.event.AbstractRowEvents;
import com.zufangbao.gluon.opensdk.DateUtils;

@Component("t_remittance_plan")
public class RemittancePlan extends AbstractRowEvents{
	
	private static final Logger LOG = LoggerFactory.getLogger(RemittancePlan.class);

	@Resource(name = "mqMunichreProducer")
	private MqProducer producer;

	@Value("${journal.voucher.type}")
	private String type;

	@Value("${journal.voucher.consumer.bean}")
	private String bean;

	@Value("${journal.voucher.consumer.method}")
	private String method;

	@Autowired
	private RemittancePlanStatCacheService remittancePlanStatCacheService;

	@Override
	public void onInsert(List<Column> afterColumnsList) {
		LOG.info("------------------remittance plan run in onInsert method-----------------------");
		try {
			onChange(afterColumnsList, Boolean.TRUE, Boolean.FALSE, -1);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("#remittance plan stat cache insert error# occur error with exception stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
		}
		
	}

	@Override
	public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {
		LOG.info("------------------remittance plan run in onUpdate method-----------------------");

		try {
			String columnName = null;
			String columnValue = null;
			int before_status = -1;
			for (Column column : beforeColumnsList) {
				columnName = column.getName();
				columnValue = column.getValue();
				if ("execution_status".equalsIgnoreCase(columnName))
					before_status = getInteger(columnValue);
			}
			onChange(afterColumnsList, Boolean.FALSE, Boolean.TRUE, before_status);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("#remittance plan stat cache update error# occur error with exception stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
		}
		
	}

	@Override
	public void onDelete(List<Column> beforeColumnsList) {
		// TODO Auto-generated method stub
		
	}
	
	private void onChange(List<Column> afterColumnsList, boolean insert, boolean update, int before_status) {
		LOG.info("------------------remittance plan run in onChange method11111-----------------------");

		String columnName = null;
		String columnValue = null;
		String financial_contract_uuid = null;
		String payment_channel_uuid = null;
		Integer payment_gateway = null;
		Date last_modified_time = null;
		Integer after_execution_status =null;
		String remittance_plan_uuid = null;
		BigDecimal plan_total_amount = null;
		String contract_no = null;
		boolean update_status = Boolean.FALSE;
		
		for (Column column : afterColumnsList) {
			columnName = column.getName();
			columnValue = column.getValue();
			if ("financial_contract_uuid".equalsIgnoreCase(columnName)){
				financial_contract_uuid = columnValue;
			}
			if ("payment_channel_uuid".equalsIgnoreCase(columnName)){
				payment_channel_uuid = columnValue;
			}
			if ("payment_gateway".equalsIgnoreCase(columnName)){
				payment_gateway = getInteger(columnValue);
			}
			if ("last_modified_time".equalsIgnoreCase(columnName)){
				last_modified_time = DateUtils.parseDate(columnValue);
			}
			if ("planned_total_amount".equalsIgnoreCase(columnName)){
				plan_total_amount = getBigDecimal(columnValue);
			}
			if("remittance_plan_uuid".equalsIgnoreCase(columnName)){
				remittance_plan_uuid = columnValue;
			}
			if ("execution_status".equalsIgnoreCase(columnName)) {
				after_execution_status = getInteger(columnValue);
				if (column.getUpdated()) {
					update_status = Boolean.TRUE;
				}
			}
			if("contract_no".equalsIgnoreCase(columnName)){
				contract_no = columnValue;
			}
		}
		
		if(!update_status && !insert){
			return;
		}
		
		if(after_execution_status != TransactionCommandExecutionStatus.SUCCESS.getCode() &&
				after_execution_status != TransactionCommandExecutionStatus.FAIL.getCode()){
			return;
		}
		LOG.info("------------------remittance plan run in onChange method22222-----------------------");

		List<RepaymentPlanDetail> repaymentPlanDetails = remittancePlanStatCacheService.getAssetDetails(financial_contract_uuid, contract_no);
		BigDecimal plan_principal = plan_total_amount;
		BigDecimal plan_interest = BigDecimal.ZERO;
		BigDecimal plan_loan_service_fee = BigDecimal.ZERO;
		BigDecimal plan_amount = BigDecimal.ZERO;
		BigDecimal loanServiceFee = BigDecimal.ZERO;

		for(RepaymentPlanDetail detail : repaymentPlanDetails) {
			plan_interest = plan_interest.add(detail.getAssetInterest());
			loanServiceFee = remittancePlanStatCacheService.getLoanServiceFee(detail.getAssetUuid());
			plan_loan_service_fee = plan_loan_service_fee.add(loanServiceFee);
			plan_amount = plan_amount.add(detail.getAssetAmount());
		}
		
		BigDecimal delta_suc_amount = BigDecimal.ZERO;
		int delta_suc_num= 0;
		int delta_fail_num = 0;
		BigDecimal flag = BigDecimal.ZERO;
		
		if(before_status==TransactionCommandExecutionStatus.FAIL.getCode()){
			delta_fail_num--;
		} else if(before_status==TransactionCommandExecutionStatus.SUCCESS.getCode()){
			flag = flag.subtract(BigDecimal.ONE);
			delta_suc_num--;
			delta_suc_amount = delta_suc_amount.subtract(plan_total_amount);
		}
		if(after_execution_status==TransactionCommandExecutionStatus.SUCCESS.getCode()){
			flag = flag.add(BigDecimal.ONE);
			delta_suc_num++;
			delta_suc_amount = delta_suc_amount.add(plan_total_amount);
		}else if(after_execution_status==TransactionCommandExecutionStatus.FAIL.getCode()){
			delta_fail_num++;
		}
		
		LOG.info("remittance_plan_uuid:"+remittance_plan_uuid);
		String remittancePlanStatCacheUuid = remittancePlanStatCacheService.get_remittance_plan_stat_cache_interval_uuid(financial_contract_uuid, payment_channel_uuid, last_modified_time);
		
		if(StringUtils.isEmpty(remittancePlanStatCacheUuid)) {
			LOG.info("------------------remittance plan run in onChange method create -----------------------");
			LOG.info("remittancePlanStatCacheUuid["+remittancePlanStatCacheUuid+"],"
					+ "create_deduct_plan_stat_cache,"
					+ "financial_contract_uuid["+financial_contract_uuid+"],"
					+ "payment_channel_uuid["+payment_channel_uuid+"],"
					+ "payment_gateway["+payment_gateway+"],"
					+ "last_modified_time["+last_modified_time+"],"
					+ "delta_suc_amount["+delta_suc_amount+"],"
					+ "delta_suc_num["+delta_suc_num+"],"
					+ "delta_fail_num["+delta_fail_num+"]");
			remittancePlanStatCacheService.create_remittance_plan_stat_cache(financial_contract_uuid, payment_channel_uuid, payment_gateway, last_modified_time, delta_suc_amount, delta_suc_num, delta_fail_num, plan_principal, plan_interest, plan_loan_service_fee, plan_amount);
		} else {
			LOG.info("------------------remittance plan run in onChange method update -----------------------");
			LOG.info("remittancePlanStatCacheUuid["+remittancePlanStatCacheUuid+"],"
					+ "update_count_amount,"
					+ "financial_contract_uuid["+financial_contract_uuid+"],"
					+ "payment_channel_uuid["+payment_channel_uuid+"],"
					+ "payment_gateway["+payment_gateway+"],"
					+ "last_modified_time["+last_modified_time+"],"
					+ "delta_suc_amount["+delta_suc_amount+"],"
					+ "delta_suc_num["+delta_suc_num+"],"
					+ "delta_fail_num["+delta_fail_num+"]");
			remittancePlanStatCacheService.update_count_amount(remittancePlanStatCacheUuid, delta_suc_amount, delta_suc_num, delta_fail_num, plan_principal, plan_interest, plan_loan_service_fee, plan_amount);
		}
	}
	
}
