package com.suidifu.bridgewater.api.service.batch.v2;

import java.util.List;

public interface DeductPlanCacheService {

	void pushDeductPlanUuidsToCache(String batchDeductApplicationUuid, List<String> deductPlanUuid);
	
	public List<String> popAllDeductPlanUuidResult(String batchDeductApplicationUuid);

}
