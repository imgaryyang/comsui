package com.suidifu.morganstanley.configuration.bean.yntrust;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 批量文件任务配置
 *
 * @author louguanyang at 2017/11/1 14:36
 * @mail louguanyang@hzsuidifu.com
 */
@Configuration
@ConfigurationProperties(prefix = "yntrust.file-task")
@Data
public class YntrustFileTask {
    /**
     * 商户编号
     */
    private String merId;
    /**
     * 商户密钥
     */
    private String secret;
    /**
     * 文件扫描地址
     */
    private String scanPath;
    /**
     * 对账文件生成地址
     */
    private String rebuildPath;
}
