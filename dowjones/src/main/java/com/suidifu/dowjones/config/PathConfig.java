package com.suidifu.dowjones.config;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Z.juxer
 * Created by veda on 26/12/2017.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "path")
@Component
public class PathConfig implements Serializable {

    private String reportTask;

    private String baiduQianlongReportTask;

    private String tencentAbsReportTask;

}
