package com.suidifu.dowjones.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/4 <br>
 * @time: 18:54 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
@ConfigurationProperties(prefix = "ftp")
@Component
public class FTPProperties {
    private String ip;
    private String port;
    private String name;
    private String password;
    private String rootPath;

    // 针对百度乾隆项目

    private String baiduIp;
    private String baiduPort;
    private String baiduName;
    private String baiduPassword;
    private String baiduRootPath;

    //针对腾讯ABS项目
    private String tencentIp;
    private String tencentPort;
    private String tencentName;
    private String tencentPassword;
    private String tencentRootPath;
}