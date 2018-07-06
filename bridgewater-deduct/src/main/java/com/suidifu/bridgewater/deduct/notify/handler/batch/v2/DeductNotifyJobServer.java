package com.suidifu.bridgewater.deduct.notify.handler.batch.v2;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.suidifu.swift.notifyserver.notifyserver.impl.NotifyJobServerImpl;

/**
 * @author wukai
 *
 */
@Component("deductNotifyJobServer")
public class DeductNotifyJobServer extends NotifyJobServerImpl {
	
	 @Value("#{config['notifyserver.serverIdentity']}")
	 private String serverIdentity;
	 @Value("#{config['notifyserver.persistenceMode']}")
	 private int  persistenceMode;
	 
	 @Value("#{config['notifyserver.groupCacheJobQueueMap']}")
	 private String groupCacheJobQueueMap;
	 
	 @Value("#{config['notifyserver.reportLogDir']}")
	 private String reportLogDir;
	 
	 private static Log logger = LogFactory.getLog(DeductNotifyJobServer.class);
	
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
	      serverConfig.setResponseResultFileDir(reportLogDir);
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
