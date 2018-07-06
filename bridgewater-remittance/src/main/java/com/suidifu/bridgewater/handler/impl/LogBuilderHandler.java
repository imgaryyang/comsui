package com.suidifu.bridgewater.handler.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.persistence.GenericDaoSupport;

@Component("logBuilderHandler")
public class LogBuilderHandler {
	
	private static final Log logger = LogFactory.getLog(LogBuilderHandler.class);

	@Autowired 
	private GenericDaoSupport genericDaoSupport;
	
	public void logRemittance(String remittanceApplicationUuid, String id) {
		String sentence = "select remittance_application_uuid,financial_contract_uuid,financial_contract_id,financial_product_code,planned_total_amount,actual_total_amount,plan_notify_number,actual_notify_number,execution_status,transaction_recipient,execution_remark,create_time,opposite_receive_date,last_modified_time,total_count,actual_count,version_lock,check_status,notify_status from t_remittance_application where remittance_application_uuid =:remittanceApplicationUuid ";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("remittanceApplicationUuid", remittanceApplicationUuid);
		List<Model> remittanceSqlModes = genericDaoSupport.queryForList(sentence, parameters, Model.class, 0, 1);
		if (CollectionUtils.isEmpty(remittanceSqlModes)) {
			logger.info(id+"xxxxxxxxxremittancexxxxxxxxxxxisnull");
		}else {
			Model model = remittanceSqlModes.get(0);
			logger.info(id+model);
		}
	}
}
