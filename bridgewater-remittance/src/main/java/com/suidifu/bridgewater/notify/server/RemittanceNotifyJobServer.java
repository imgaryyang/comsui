package com.suidifu.bridgewater.notify.server;

import com.suidifu.swift.notifyserver.notifyserver.Exceptions.NotifyJobLocationCorruptException;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.suidifu.swift.notifyserver.notifyserver.impl.NotifyJobServerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component("remittanceNotifyJobServer")
public class RemittanceNotifyJobServer extends NotifyJobServerImpl {
	@Value("#{config['notifyserver.serverIdentity']}")
	 private String serverIdentity;
	 @Value("#{config['notifyserver.persistenceMode']}")
	 private int  persistenceMode;
	 
	 @Value("#{config['notifyserver.reportLogDir']}")
	 private String reportLogDir;
	 
	 @Value("#{config['notifyserver.protocolType']}")
	 private Integer notifyserver_protocolType;
	 
	 @Value("#{config['notifyserver.selfAddr']}")
	 private String notifyserver_selfAddr;
	 
	 @Value("#{config['notifyserver.queueNumScope']}")
	 private String notifyserver_queueNumScope;
	 
	 @Autowired
	 private ConfigHandler configHandler;
	 
	 private static Log logger = LogFactory.getLog(RemittanceNotifyJobServer.class);
	 
	 @Autowired
	 private RemittanceNotifyResultListener remittanceNotifyResultListener;

	@PostConstruct
	public void initNotifyService() {

		logger.info("begin to initNotifyService");

		this.start();

		this.addListener(remittanceNotifyResultListener);

		logger.info("end to initNotifyService");
	}

	@Override
	public NotifyServerConfig getNotifyServerConfig() {
		NotifyServerConfig serverConfig = new NotifyServerConfig();
	      try {
	   	      serverConfig.setServerIdentity(serverIdentity);
	         serverConfig.setPersistenceMode(persistenceMode);
	         serverConfig.setResponseResultFileDir(reportLogDir);
	         serverConfig.setGroupCacheJobQueueMap(configHandler.getNotifyServerGroupConfig());
	         serverConfig.setGroupCacheJobQueueMapForMq(configHandler.getNotifyServerGroupConfigForMq());
	         serverConfig.setProtocolType(notifyserver_protocolType);
	    	   serverConfig.setSelfAddr(notifyserver_selfAddr);
	    	   serverConfig.setQueueNumScope(notifyserver_queueNumScope);
			} catch (NotifyJobLocationCorruptException e) {
				e.printStackTrace();
			}
	      
		   return serverConfig;
	}

	public void setServerIdentity(String serverIdentity) {
		this.serverIdentity = serverIdentity;
	}

	public void setPersistenceMode(int persistenceMode) {
		this.persistenceMode = persistenceMode;
	}

	public void setReportLogDir(String reportLogDir) {
		this.reportLogDir = reportLogDir;
	}


}
