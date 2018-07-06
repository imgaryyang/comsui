package com.suidifu.morganstanley.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 摩根斯坦利 Notify Server 配置
 *
 * @author louguanyang at 2017/12/1 13:51
 * @mail louguanyang@hzsuidifu.com
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "morgan-notify-server")
public class MorganStanleyNotifyConfig {
    private String cachedJobQueueSize;
    private String serverIdentity;
    private Integer persistenceMode;
    private String requestUrl;
    private String dbFilePath;
    /*
     * 异步导入资产包 补漏 队列名
     */
    private String asyncGroupName;
    
	/*
     * 异步导入资产包 回调 队列名
     */
    private String callbackGroupName;
    /**
     * 文件处理 队列名
     */
    private String fileNotifyGroupName;
    /**
     * 签约 队列名
     */
    private String signUpGroupName;
    
    /**
     * 转账通知队列名
     */
    private String transferNotifyGroupName;
    
    private Map<String, Integer> groupCacheJobQueueMap;
    private String responseResultFileDir;
    private String merId;
    private String secret;
    private String defaultCallbackUrl;
    private Long makeUpPeriod;

    
	public Map<String, Integer> getGroupCacheJobQueueMap() {
		return groupCacheJobQueueMap;
	}
	public void setGroupCacheJobQueueMap(Map<String, Integer> groupCacheJobQueueMap) {
		this.groupCacheJobQueueMap = groupCacheJobQueueMap;
	}
    
    
}
