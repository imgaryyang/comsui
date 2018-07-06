package com.suidifu.bridgewater.handler.batch.async.v2;

/**
 * @author wukai
 *
 */
public interface IAsynProcessingBatchRequestHandler {

	public void processingBatchRequest(String batchDeductApplicationUuid,String  merId, String secret);
}
