/**
 * 
 */
package com.suidifu.hathaway.mq.sender;

import java.util.Collection;


/**
 * @author wukai
 *
 */
public interface MessageSender {

	void publishASyncRPCMessage(String requestUuid, String businessUuid, String beanName, String methodName, Object data, int priority);

	void publishASyncRPCMessage(String requestUuid, String businessUuid, String beanName, String methodName, Object[] datas, int priority);
	
	Object publishSyncRPCMessage(String requestUuid, String businessUuid, String beanName, String methodName, Object[] args, int priority) throws Throwable;
	
	String selectQueue(String BusinessId);
	
	Collection<String> selectAllQueueIndexes();
	
	void registerProducerName(String producerName);

}
