package com.suidifu.munichre;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.suidifu.munichre.StaticsConfig.ContractState;
import com.suidifu.munichre.config.MqProducer;
import com.suidifu.munichre.service.ContractStatCacheService;
import com.zufangbao.canal.core.rowprocesser.event.AbstractRowEvents;
import com.zufangbao.gluon.opensdk.DateUtils;

@Component("contract")
public class Contract  extends AbstractRowEvents {
	
	private static final Logger LOG = LoggerFactory.getLogger(Contract.class);

	@Resource(name = "mqMunichreProducer")
	private MqProducer producer;

	@Value("${journal.voucher.type}")
	private String type;

	@Value("${journal.voucher.consumer.bean}")
	private String bean;

	@Value("${journal.voucher.consumer.method}")
	private String method;
	
	@Autowired
	ContractStatCacheService contractStatCacheService;

	@Override
	public void onInsert(List<Column> afterColumnsList) {
		LOG.info("------------------contract run in onInsert method -----------------------");
		try {
			onChange(afterColumnsList, Boolean.TRUE, Boolean.FALSE, -1);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("#contract stat cache insert error# occur error with exception stack trace["+ExceptionUtils.getFullStackTrace(e)+"]");
		}
		
	}

	@Override
	public void onUpdate(List<Column> beforeColumnsList, List<Column> afterColumnsList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDelete(List<Column> beforeColumnsList) {
		// TODO Auto-generated method stub
		
	}
	
	private void onChange(List<Column> afterColumnsList, boolean insert, boolean update, int before_status) {
		LOG.info("------------------contract run in onChange method -----------------------");
		String columnName = null;
		String columnValue = null;
		String financial_contract_uuid = null;
		Date last_modified_time = null;
		Integer after_execution_status =null;
		BigDecimal total_amount = null;
		Long contract_id = null;
		String contract_uuid = null;
		boolean update_status = Boolean.FALSE;
		for (Column column : afterColumnsList) {
			columnName = column.getName();
			columnValue = column.getValue();
			if ("financial_contract_uuid".equalsIgnoreCase(columnName)){
				financial_contract_uuid = columnValue;
			}
			if ("create_time".equalsIgnoreCase(columnName)){
				last_modified_time = DateUtils.parseDate(columnValue);
			}
			if ("total_amount".equalsIgnoreCase(columnName)){
				total_amount = getBigDecimal(columnValue);
			}
			if("id".equalsIgnoreCase(columnName)) {
				contract_id = getLong(columnValue);
			}
			if("uuid".equalsIgnoreCase(columnName)){
				contract_uuid = columnValue;
			}
			if("state".equalsIgnoreCase(columnName)) {
				after_execution_status = getInteger(columnValue);
				if (column.getUpdated())
					update_status = Boolean.TRUE;
			}
		}
		if(!update_status && !insert){
			return;
		}
		//放款只监控生效
		if(after_execution_status != ContractState.EFFECTIVE.getCode()) {
			return;
		}
		List<RepaymentPlanDetail> repaymentPlanDetails = contractStatCacheService.getAssetDetails(financial_contract_uuid, contract_id);
		BigDecimal plan_principal = total_amount;
		BigDecimal plan_interest = BigDecimal.ZERO;
		BigDecimal plan_loan_service_fee = BigDecimal.ZERO;
		BigDecimal plan_amount = BigDecimal.ZERO;
		
		List<String> assetSetUuids = new ArrayList<>();
		for(RepaymentPlanDetail detail : repaymentPlanDetails) {
			plan_interest = plan_interest.add(detail.getAssetInterest());
			plan_amount = plan_amount.add(detail.getAssetAmount());
			assetSetUuids.add(detail.getAssetUuid());
		}
		plan_loan_service_fee = contractStatCacheService.getLoanServiceFee(assetSetUuids);
		
		BigDecimal delta_suc_amount = BigDecimal.ZERO;
		int delta_suc_num= 0;
		int delta_fail_num = 0;
		
		if(before_status==ContractState.INVALIDATE.getCode()){
			delta_fail_num--;
		} else if(before_status==ContractState.EFFECTIVE.getCode()){
			delta_suc_num--;
			delta_suc_amount = delta_suc_amount.subtract(total_amount);
		}
		if(after_execution_status==ContractState.EFFECTIVE.getCode()){
			delta_suc_num++;
			delta_suc_amount = delta_suc_amount.add(total_amount);
		}else if(after_execution_status==ContractState.INVALIDATE.getCode()){
			delta_fail_num++;
		}
		
		LOG.info("contract_uuid:"+contract_uuid+" contract_id:"+contract_id);
		String contractStatCacheUuid = contractStatCacheService.get_contract_stat_cache_interval_uuid(financial_contract_uuid, last_modified_time);
		if(StringUtils.isEmpty(contractStatCacheUuid)) {
			LOG.info("------------------contract run in onInsert method create -----------------------");
			LOG.info("contractStatCacheUuid["+contractStatCacheUuid+"],"
					+ "create_contract_stat_cache,"
					+ "financial_contract_uuid["+financial_contract_uuid+"],"
					+ "last_modified_time["+last_modified_time+"],"
					+ "delta_suc_amount["+delta_suc_amount+"],"
					+ "delta_suc_num["+delta_suc_num+"],"
					+ "delta_fail_num["+delta_fail_num+"]");
			contractStatCacheService.create_contract_stat_cache(financial_contract_uuid, last_modified_time, delta_suc_amount, delta_suc_num, delta_fail_num, plan_principal, plan_interest, plan_loan_service_fee, plan_amount);
		} else {
			LOG.info("------------------contract run in onInsert method update -----------------------");
			LOG.info("contractStatCacheUuid["+contractStatCacheUuid+"],"
					+ "update_count_amount,"
					+ "financial_contract_uuid["+financial_contract_uuid+"],"
					+ "last_modified_time["+last_modified_time+"],"
					+ "delta_suc_amount["+delta_suc_amount+"],"
					+ "delta_suc_num["+delta_suc_num+"],"
					+ "delta_fail_num["+delta_fail_num+"]");
			contractStatCacheService.update_count_amount(contractStatCacheUuid, delta_suc_amount, delta_suc_num, delta_fail_num, plan_principal, plan_interest, plan_loan_service_fee, plan_amount);
		}
		
	}
	
	private Long getLong(String s) {
		return (s == null || s.trim().length() == 0) ? -1l : Long.parseLong(s);
	}
}
