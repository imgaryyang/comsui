package com.suidifu.rabbitmq.manager.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wukai on 2017/12/12.
 */
@Data
@NoArgsConstructor
public class CommandModel {

    private String name;

    private String classPath;

    private String description;

    private boolean needInteract;

    private List<ParameterModel> parameterModels = new ArrayList<>();

    public CommandModel(String name, String classPath, String description) {
        this.name = name;
        this.classPath = classPath;
        this.description = description;
        this.needInteract = true;
    }

    public Map<String, String> extractArguments() {

        Map<String, String> args = new HashMap<String, String>();

        for (ParameterModel parameterModel : parameterModels
                ) {
            args.put(parameterModel.getName(), parameterModel.getValue());
        }
        return args;
    }

    public void addParameterModel(ParameterModel ParameterModel) {
        this.parameterModels.add(ParameterModel);
    }

}

