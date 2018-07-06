/**
 * 
 */
package com.zufangbao.earth.yunxin.handler.impl.deduct.notify.v2;

import com.alibaba.fastjson.JSON;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.suidifu.swift.notifyserver.notifyserver.impl.NotifyJobServerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author wukai
 *
 */
@Component("DeductBusinessNotifyJobServer")
public class DeductBusinessNotifyJobServer extends NotifyJobServerImpl {
	
	 @Value("#{config['notifyserver.serverIdentity']}")
	 private String serverIdentity;
	 @Value("#{config['notifyserver.persistenceMode']}")
	 private int  persistenceMode;
	 
	 @Value("#{config['notifyserver.groupCacheJobQueueMap']}")
	 private String groupCacheJobQueueMap;
	 
	 @Value("#{config['notifyserver.responseResultFileDir']}")
	 private String responseResultFileDir;
	 
//	 @Value("${notifyserver.protocolType:0}")
	 private int  protocolType = 0;
	 
	 private static final Log logger = LogFactory.getLog(DeductBusinessNotifyJobServer.class);
	 
	 @Autowired
	 private DeductNotifyResultListener deductNotifyResultListener;
	 
	@PostConstruct
	public void initNotifyService(){
		
		logger.info("begin to initNotifyService");
		
		this.start();

		this.addListener(deductNotifyResultListener);
		
		logger.info("end to initNotifyService");
	}

	 @Override
	 public NotifyServerConfig getNotifyServerConfig() {
		  NotifyServerConfig serverConfig = new NotifyServerConfig();
		  serverConfig.setServerIdentity(serverIdentity);
	      serverConfig.setPersistenceMode(persistenceMode);
	      serverConfig.setGroupCacheJobQueueMap(JSON.parseObject(this.groupCacheJobQueueMap,new com.alibaba.fastjson.TypeReference<Map<String,Integer>>(){} ));
	      serverConfig.setResponseResultFileDir(responseResultFileDir);
	      serverConfig.setProtocolType(protocolType);
	      return serverConfig;
	 }

	 public String getServerIdentity() {
		 return serverIdentity;
	 }

	 public void setServerIdentity(String serverIdentity) {
		 this.serverIdentity = serverIdentity;
	 }

	public int getPersistenceMode() {
		return persistenceMode;
	}

	public void setPersistenceMode(int persistenceMode) {
		this.persistenceMode = persistenceMode;
	}

}
