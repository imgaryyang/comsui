package com.suidifu.bridgewater.api.service.batch.v2;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.DeductPlanApplicationCheckLog;

/**
 * @author wukai
 *
 */
public interface DeductPlanApplicationCheckLogService extends GenericService<DeductPlanApplicationCheckLog> {

	/**
	 * 根据扣款唯一编号查询日志
	 * @param deductId
	 * @return
	 */
	public DeductPlanApplicationCheckLog findOne(String deductId);
	
	
	public int calculateDeductPlanApplicationCheckLogBy(String batchDeductId);
	
	
	public List<DeductPlanApplicationCheckLog> findDeductPlanApplicationCheckLogListBy(String batchDeductApplicationUuid);
}
