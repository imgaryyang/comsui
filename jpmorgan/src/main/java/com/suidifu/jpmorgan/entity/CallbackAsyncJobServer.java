package com.suidifu.jpmorgan.entity;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.suidifu.jpmorgan.entity.unionpay.AsyncNotifyResultListener;
import com.suidifu.swift.notifyserver.notifyserver.Exceptions.NotifyJobLocationCorruptException;
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
 * Created by whb on 17-6-28.
 */
@Component("callbackAsyncJobServer")
public class CallbackAsyncJobServer extends NotifyJobServerImpl {
    private static final Log LOGGER = LogFactory.getLog(CallbackAsyncJobServer.class);

    @Autowired
    private AsyncNotifyResultListener asyncNotifyResultListener;

    @Value("#{config['notifyserver.groupCacheJobQueueMap']}")
    private String groupCacheJobQueueMapStr;
    
    @Value("#{config['notifyserver.groupCacheJobQueueMapForMq']}")
    private String  groupCacheJobQueueMapForMqStr;
    
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
    
	 private Map<String, Integer> groupCacheJobQueueMap;

	 private Map<String, Integer> groupCacheJobQueueMapForMq;
	 
    @PostConstruct
    public void init(){
    	this.groupCacheJobQueueMap = JSON.parseObject(groupCacheJobQueueMapStr,new TypeReference<Map<String, Integer>>(){});
    	this.groupCacheJobQueueMapForMq = JSON.parseObject(groupCacheJobQueueMapForMqStr,new TypeReference<Map<String, Integer>>(){});
        this.start();
        this.addListener(asyncNotifyResultListener);
    }

    @Override
    public NotifyServerConfig getNotifyServerConfig() {
    	 NotifyServerConfig serverConfig = new NotifyServerConfig();
 	     try {
 	        serverConfig.setGroupCacheJobQueueMap(groupCacheJobQueueMap);
 	       serverConfig.setGroupCacheJobQueueMapForMq(groupCacheJobQueueMapForMq);
 	        serverConfig.setServerIdentity(serverIdentity);
 	        serverConfig.setPersistenceMode(persistenceMode);
 	 	     serverConfig.setResponseResultFileDir(reportLogDir);
 	 	     serverConfig.setProtocolType(notifyserver_protocolType);
			serverConfig.setSelfAddr(notifyserver_selfAddr);
	 	     serverConfig.setQueueNumScope(notifyserver_queueNumScope);
		} catch (NotifyJobLocationCorruptException e) {
			e.printStackTrace();
		}
        return serverConfig;
    }
}
