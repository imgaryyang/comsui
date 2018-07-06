package com.suidifu.barclays.entity;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suidifu.barclays.handler.PaymentAsyncHandler;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.suidifu.swift.notifyserver.notifyserver.impl.NotifyJobServerImpl;

@Component(value = "paymentAsyncJobServer")
public class PaymentAsyncJobServer extends NotifyJobServerImpl{

    @Autowired
    private PaymentAsyncHandler paymentAsyncHandler;

    @Value("#{config['notifyserver.cachedJobQueueSize']}")
    private String cachedJobQueueSize;

    @Value("#{config['notifyserver.serverIdentity']}")
    private String serverIdentity;

    @Value("#{config['notifyserver.persistenceMode']}")
    private int  persistenceMode;

    @PostConstruct
    public void init() {
        this.start();
        this.addListener(paymentAsyncHandler);
    }

    @Override
    public NotifyServerConfig getNotifyServerConfig() {
        NotifyServerConfig serverConfig = new NotifyServerConfig();
        serverConfig.setCachedJobQueueSize(cachedJobQueueSize);
        serverConfig.setServerIdentity(serverIdentity);
        serverConfig.setPersistenceMode(persistenceMode);
        return serverConfig;
    }
	
}
