package com.suidifu.rabbitmq.manager.commands;

import java.util.Map;

/**
 * Created by wukai on 2017/12/12.
 */
public interface Command {

    boolean exec(Map<String, String> args);

    default String classpath() {
        return this.getClass().getCanonicalName();
    }
}
