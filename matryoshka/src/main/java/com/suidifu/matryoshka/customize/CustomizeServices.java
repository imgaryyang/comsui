package com.suidifu.matryoshka.customize;

import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import org.apache.commons.logging.Log;

import java.util.Map;

/**
 * Created by louguanyang on 2017/4/5.
 */
public interface CustomizeServices {
    boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger);
}
