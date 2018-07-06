package com.suidifu.morganstanley.configuration.bean.yntrust;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 云南信托相关配置
 *
 * @author louguanyang at 2017/10/26 11:18
 * @mail louguanyang@hzsuidifu.com
 */
@Configuration
@ConfigurationProperties(prefix = "yntrust")
@Data
public class YntrustProperties {
    /**
     * morganstanley服务地址
     */
    private String morganstanleyUrl;
    /**
     * 文件上传接口上传地址
     */
    private String uploadPath;
}
