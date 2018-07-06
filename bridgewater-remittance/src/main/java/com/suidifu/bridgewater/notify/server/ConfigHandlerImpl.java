/**
 *
 */
package com.suidifu.bridgewater.notify.server;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;


/**
 * @author wsh
 */
@Component
public class ConfigHandlerImpl implements ConfigHandler {


	@Value("#{config['notifyserver.groupCacheJobQueueMap_group0']}")
	private String groupNameForPushToOutlier;

	@Value("#{config['notifyserver.groupCacheJobQueueMap']}")
	private String groupCacheJobQueueMapStr;

	@Value("#{config['notifyserver.groupCacheJobQueueMapForMq']}")
	private String groupCacheJobQueueMapForMqStr;

	private Map<String, Integer> groupCacheJobQueueMap;
	
	private Map<String, Integer> groupCacheJobQueueMapForMq;
	
	private static Log LOGGER = LogFactory.getLog(ConfigHandlerImpl.class);

	@PostConstruct
	public void init() {
		
		LOGGER.info("initialize groupNameMap");
		
		groupCacheJobQueueMap= JSON.parseObject(this.groupCacheJobQueueMapStr,new com.alibaba.fastjson.TypeReference<Map<String,Integer>>(){} );
		
		groupCacheJobQueueMapForMq= JSON.parseObject(this.groupCacheJobQueueMapForMqStr,new com.alibaba.fastjson.TypeReference<Map<String,Integer>>(){} );

		LOGGER.info("end to initialize groupNameMap with reulst["+groupCacheJobQueueMap+"]");
		
	}

	/* (non-Javadoc)
	 * @see com.suidifu.bridgewater.handler.ConfigHandler#getNotifyGroupNameBy(java.lang.String)
	 */
	@Override
	public String getNotifyGroupNameBy(String finanContractUuid) {
		if (groupCacheJobQueueMap.containsKey(finanContractUuid)) {
			return finanContractUuid;
		} else if (groupCacheJobQueueMap.containsKey(finanContractUuid + "-PushToOutlier")) {
			return finanContractUuid + "-PushToOutlier";
		} else {
			return groupNameForPushToOutlier;
		}
	}

	@Override
	public Map<String, Integer> getNotifyServerGroupConfig() {

		return groupCacheJobQueueMap;
	}


	@Override
	public Map<String, Integer> getNotifyServerGroupConfigForMq() {

		return groupCacheJobQueueMapForMq;
	}
}





