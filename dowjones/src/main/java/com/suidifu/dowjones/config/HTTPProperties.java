package com.suidifu.dowjones.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/16 <br>
 * @time: 17:00 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Data
@ConfigurationProperties(prefix = "callback")
@Component
public class HTTPProperties {
    private String url;

    private String endpoint;
}