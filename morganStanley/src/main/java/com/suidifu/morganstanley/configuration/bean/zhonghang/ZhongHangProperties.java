package com.suidifu.morganstanley.configuration.bean.zhonghang;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 中航配置文件
 *
 * @author louguanyang
 */
@Configuration
@ConfigurationProperties(prefix = "zhonghang")
@Data
public class ZhongHangProperties {
    /**
     * morganstanley服务地址
     */
    private String morganstanleyUrl;
    private String signKey;
    private String merId;
    private String secret;
    private String signMethod;
    private String requestUrl;
    private String cachedJobQueueSize;
    private String serverIdentity;
    private String persistenceMode;
    private String notifyUrlToSignUp;
    private String signTransType;
    private String queryTransType;
    private String signUpGroupName;
}