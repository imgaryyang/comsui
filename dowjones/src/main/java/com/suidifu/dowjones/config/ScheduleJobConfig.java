package com.suidifu.dowjones.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/18 <br>
 * @time: 15:06 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Component
@ConfigurationProperties
@Data
public class ScheduleJobConfig {
    private String jobClassName;
    private Boolean durability;
    private Boolean requestsRecovery;
    private String cronExpression;
    private Integer status;
    private String jobName;
    private String jobGroup;
    private String cronTriggerName;
    private String cronTriggerGroup;
    private Integer priority;
}