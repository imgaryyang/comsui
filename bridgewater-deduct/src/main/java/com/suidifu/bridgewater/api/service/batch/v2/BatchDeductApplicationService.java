/**
 * 
 */
package com.suidifu.bridgewater.api.service.batch.v2;

import java.util.List;

import com.demo2do.core.service.GenericService;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchProcessStatus;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.NotifyStatus;

public interface BatchDeductApplicationService extends GenericService<BatchDeductApplication> {

	boolean saveBatchDeductInfo(BatchDeductApplication batchDeductApplication);
	
	List<BatchDeductApplication> loadWaitingProcessBatchDeductApplications();
	
	public void updateBatchDeductApplicationStatus(Long id,BatchProcessStatus batchProcessStatus,NotifyStatus batchNotifyStatus,Integer totalLines,Integer failCount, String erroMsg);
	
	public void increaseBatchDeductApplicationErrorCount(Long id);

	public List<BatchDeductApplication> getNeedToNotifyCounterBatchDeductApplications();
	
	public BatchDeductApplication getBatchDeductApplicationBy(String batchDeductApplicationUuid);
	
	public void updateBatchDeductApplicationByUuid(String batchDeductApplicationUuid,int actualCount);

	public String getCurrentAccessVersion(String batchDeductApplicationUuid);
}