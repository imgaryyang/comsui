package com.suidifu.bridgewater.handler;

import com.suidifu.bridgewater.api.model.RemittanceQueryResult;
import com.suidifu.bridgewater.api.model.RemittanceResultBatchPackModel;

import java.util.List;

public interface IRemittanceQueryApiHandler {

	/**
	 * 批量查询放款结果处理方法
	 * @param batchPackModel
	 * @return
	 */
	List<RemittanceQueryResult> apiRemittanceResultBatchQuery(RemittanceResultBatchPackModel batchPackModel);
}
