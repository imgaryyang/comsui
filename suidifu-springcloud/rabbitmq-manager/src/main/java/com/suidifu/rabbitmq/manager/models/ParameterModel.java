package com.suidifu.rabbitmq.manager.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * author: wukai
 * mail: wukai@hzsuidifu.com
 * time: 2018-01-16 15:59
 * description:
 */
@Data
@NoArgsConstructor
public class ParameterModel {

    private String name;

    private String description;

    private String defaultValue;

    private String value;

    private boolean needInteract;

    public ParameterModel(String name, String defaultValue, String description) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.description = description;
        this.needInteract = true;
    }

    public ParameterModel(String name, String defaultValue, String description,
                          boolean needInteract) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.description = description;
        this.needInteract = needInteract;
    }

    public String getValue() {
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }
}
