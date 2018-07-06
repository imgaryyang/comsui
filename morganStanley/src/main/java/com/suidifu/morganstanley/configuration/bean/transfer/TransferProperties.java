package com.suidifu.morganstanley.configuration.bean.transfer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hwr on 17-11-3.
 */
@Configuration
@ConfigurationProperties(prefix = "transfer")
@Data
public class TransferProperties {
	
    private String notifyUrl;
    
    private String notifyNumber;
    
    private String requestUrl;
    
    private String localNotifyUrl;
}
