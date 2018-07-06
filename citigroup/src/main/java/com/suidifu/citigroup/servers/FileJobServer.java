package com.suidifu.citigroup.servers;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.suidifu.citigroup.servers.notify.MorganStanleyNotifyResultListener;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.suidifu.swift.notifyserver.notifyserver.impl.NotifyJobServerImpl;

/**
 * 文件处理推送服务
 * Created by louguanyang on 2017/5/23.
 */

public class FileJobServer extends NotifyJobServerImpl {
    
    @Autowired
    private MorganStanleyNotifyResultListener morganStanleyNotifyResultListener;

    private NotifyServerConfig notifyServerConfig;
    
    private static Log LOGGER = LogFactory.getLog(FileJobServer.class);
    
    public FileJobServer() {
        super();
    }

	@PostConstruct
	public void initService() {

		LOGGER.info("begin to init server in citigroup");
		this.start();
		this.addListener(morganStanleyNotifyResultListener);
		LOGGER.info("end to init server in citigroup");
	}

    @Override
    public NotifyServerConfig getNotifyServerConfig() {
      
        return this.notifyServerConfig;
    }

	public void setNotifyServerConfig(NotifyServerConfig notifyServerConfig) {
		this.notifyServerConfig = notifyServerConfig;
	}
   
}
