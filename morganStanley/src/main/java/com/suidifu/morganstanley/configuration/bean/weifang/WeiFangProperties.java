package com.suidifu.morganstanley.configuration.bean.weifang;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hwr on 17-11-3.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "weifang")
public class WeiFangProperties {
    private boolean enable;
}
