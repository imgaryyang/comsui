package com.suidifu.microservice.handler.impl;

import com.alibaba.fastjson.JSON;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.suidifu.swift.notifyserver.notifyserver.impl.NotifyJobServerImpl;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

//@Component("repaymentOrdeForEasyPayrNotifyServer")
public class RepaymentOrdeForEasyPayrNotifyServer extends NotifyJobServerImpl {

    private static final Log logger = LogFactory.getLog(RepaymentOrdeForEasyPayrNotifyServer.class);
    @Value("#{config['notifyserver.serverIdentity']}")
    private String serverIdentity;
    @Value("#{config['notifyserver.persistenceMode']}")
    private int persistenceMode;  // 2
    @Value("#{config['notifyserver.groupCacheJobQueueMap']}")
    private String groupCacheJobQueueMap;
    @Value("#{config['notifyserver.responseResultFileDir']}")
    private String responseResultFileDir;
    @Autowired
//	 @Qualifier("repaymentOrderForEasyPayNotifyResultListener")
    private RepaymentOrderForEasyPayNotifyResultListener repaymentOrderForEasyPayNotifyResultListener;

    @PostConstruct
    public void initNotifyService() {

        logger.info("begin to initNotifyService");

        this.start();

        this.addListener(repaymentOrderForEasyPayNotifyResultListener);

        logger.info("end to initNotifyService");
    }

    @Override
    public NotifyServerConfig getNotifyServerConfig() {
        NotifyServerConfig serverConfig = new NotifyServerConfig();
        serverConfig.setServerIdentity(serverIdentity);
        serverConfig.setPersistenceMode(persistenceMode);
        serverConfig.setGroupCacheJobQueueMap(JSON.parseObject(this.groupCacheJobQueueMap, new com.alibaba.fastjson.TypeReference<Map<String, Integer>>() {
        }));
        serverConfig.setResponseResultFileDir(responseResultFileDir);
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