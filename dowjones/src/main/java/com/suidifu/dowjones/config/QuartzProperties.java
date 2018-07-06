package com.suidifu.dowjones.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/20 <br>
 * @time: 下午5:03 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@ConfigurationProperties(prefix = "quartz")
@Component
@Data
public class QuartzProperties {
    private String schedulerName;
    private String applicationContextSchedulerContextKey;
    private Boolean overwriteExistingJobs;
    private List<ScheduleJobConfig> scheduleJob = new ArrayList<>();
}