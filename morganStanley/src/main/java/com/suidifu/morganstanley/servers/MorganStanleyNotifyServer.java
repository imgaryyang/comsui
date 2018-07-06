package com.suidifu.morganstanley.servers;

import com.suidifu.morganstanley.servers.notify.MorganStanleyNotifyResultListener;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.suidifu.swift.notifyserver.notifyserver.impl.NotifyJobServerImpl;
import lombok.extern.log4j.Log4j2;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * MorganStanley NotifyServer 服务
 *
 * @author louguanyang at 2017/12/1 00:10
 * @mail louguanyang@hzsuidifu.com
 */
@Log4j2
public class MorganStanleyNotifyServer extends NotifyJobServerImpl {

    @Resource
    private MorganStanleyNotifyResultListener morganStanleyNotifyResultListener;

    private NotifyServerConfig notifyServerConfig;

    @PostConstruct
    public void initService() {
        log.info("begin to init server in morganStanley");
        this.start();
        this.addListener(morganStanleyNotifyResultListener);
        log.info("end to init server in morganStanley");
    }

    @Override
    public NotifyServerConfig getNotifyServerConfig() {
        return notifyServerConfig;
    }

    public void setNotifyServerConfig(NotifyServerConfig notifyServerConfig) {
        this.notifyServerConfig = notifyServerConfig;
    }
}
