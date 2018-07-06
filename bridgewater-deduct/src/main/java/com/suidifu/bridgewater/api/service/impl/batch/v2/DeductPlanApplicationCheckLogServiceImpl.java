package com.suidifu.bridgewater.api.service.impl.batch.v2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.DeductPlanApplicationCheckLog;
import com.suidifu.bridgewater.api.service.batch.v2.DeductPlanApplicationCheckLogService;

/**
 * @author wukai
 *
 */
@Service("deductPlanApplicationCheckLogService")
public class DeductPlanApplicationCheckLogServiceImpl extends GenericServiceImpl<DeductPlanApplicationCheckLog> implements DeductPlanApplicationCheckLogService {

	@Override
	public DeductPlanApplicationCheckLog findOne(String deductId) {
		
		String sql = "FROM t_deduct_application_check_log WHERE deductId = :deductId";
		
		Map<String,Object> parameters = new HashMap<String,Object>();
		
		parameters.put("deductId", deductId);
		
		List<DeductPlanApplicationCheckLog> deductPlanApplicationCheckLogList = this.genericDaoSupport.searchForList(sql, parameters);
		
		return CollectionUtils.isEmpty(deductPlanApplicationCheckLogList) ? null : deductPlanApplicationCheckLogList.get(0);
	}

	@Override
	public int calculateDeductPlanApplicationCheckLogBy(String batchDeductId) {
		
		String sentence = " FROM t_deduct_application_check_log WHERE batch_Deduct_Id =:batchDeductId";
		
		Map<String,Object> parameters = new HashMap<String,Object>();
		
		parameters.put("batchDeductId", batchDeductId);
		
		return this.genericDaoSupport.count(sentence, parameters);
	}

	@Override
	public List<DeductPlanApplicationCheckLog> findDeductPlanApplicationCheckLogListBy(
			String batchDeductApplicationUuid) {
		
		String sentence = "FROM t_deduct_application_check_log WHERE batch_Deduct_Application_Uuid =:batchDeductApplicationUuid";
		
		Map<String, Object> parameters = new HashMap<String,Object>();
		
		parameters.put("batchDeductApplicationUuid", batchDeductApplicationUuid);
		
		List<DeductPlanApplicationCheckLog> deductPlanApplicationCheckLogList = this.genericDaoSupport.searchForList(sentence, parameters);
		
		return deductPlanApplicationCheckLogList;
	}

}
