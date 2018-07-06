package com.suidifu.dowjones.config;

import com.suidifu.dowjones.utils.FTPUtils;
import com.suidifu.dowjones.utils.FileUtils;
import com.suidifu.dowjones.utils.HTTPCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/4 <br>
 * @time: 19:10 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Configuration
@Slf4j
public class FTPConfig {
    @Resource
    private FTPProperties ftpProperties;
    @Resource
    private HTTPProperties httpProperties;

    @Bean
    public FTPUtils ftpUtils() {
        log.info("\nip is:{}\nport is:{}\nname is:{}\npassword is:{}\nrootPath is:{}\n"
                        + "baiduip is:{}\nbaiduport is:{}\nbaidu name is {}\nbaidu passwd is {}\nbaidu root path is {}\n",
                ftpProperties.getIp(),
                ftpProperties.getPort(),
                ftpProperties.getName(),
                ftpProperties.getPassword(),
                ftpProperties.getRootPath(),
                ftpProperties.getBaiduIp(), ftpProperties.getBaiduPort(), ftpProperties.getBaiduName(), ftpProperties.getBaiduPassword(),
                ftpProperties.getBaiduRootPath());

        FTPUtils ftpUtils = FTPUtils.getInstance();
        FileUtils fileUtils = FileUtils.getInstance();
        ftpUtils.setIp(ftpProperties.getIp());
        ftpUtils.setPort(Integer.parseInt(ftpProperties.getPort()));
        ftpUtils.setName(ftpProperties.getName());
        ftpUtils.setPassword(ftpProperties.getPassword());
        fileUtils.setRootPath(ftpProperties.getRootPath());

        ftpUtils.setBaiduIp(ftpProperties.getBaiduIp());
        ftpUtils.setBaiduPort(ftpProperties.getBaiduPort() == null ? 22 : Integer.parseInt(ftpProperties.getBaiduPort()));
        ftpUtils.setBaiduName(ftpProperties.getBaiduName());
        ftpUtils.setBaiduPassword(ftpProperties.getBaiduPassword());
        ftpUtils.setBaiduRootPath(ftpProperties.getBaiduRootPath());

        ftpUtils.setTencentIp(ftpProperties.getTencentIp());
        ftpUtils.setTencentPort(ftpProperties.getTencentPort() == null ? 22 : Integer.parseInt(ftpProperties.getTencentPort()));
        ftpUtils.setTencentName(ftpProperties.getTencentName());
        ftpUtils.setTencentPassword(ftpProperties.getTencentPassword());
        ftpUtils.setTencentRootPath(ftpProperties.getTencentRootPath());

        return ftpUtils;
    }

    @Bean
    public FileUtils fileUtils() {
        FileUtils fileUtils = FileUtils.getInstance();
        fileUtils.setRootPath(ftpProperties.getRootPath());

        return fileUtils;
    }

    @Bean
    public HTTPCallBack httpCallBack() {
        log.info("url is:{}", httpProperties.getUrl());
        log.info("endpoint is:{}", httpProperties.getEndpoint());

        HTTPCallBack httpCallBack = HTTPCallBack.getInstance();
        httpCallBack.setUrl(httpProperties.getUrl());
        httpCallBack.setEndPoint(httpProperties.getEndpoint());

        return httpCallBack;
    }
}