package com.suidifu.matryoshka.delayPosition.handler.impl;

import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.DelayPositionHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayPosition.handler.DelayTaskConfigCacheHandler;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.productCategory.ProductCategory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhenghangbo on 12/05/2017.
 */
public  abstract  class AbstractDelayPositionHandler  implements DelayPositionHandler {
    private final static Log log = LogFactory.getLog(AbstractDelayPositionHandler.class);

    @Autowired
    private DelayTaskConfigCacheHandler delayTaskConfigCacheHandler;

    @Override
    public void delayPositionDefaultTaskHandler(Result delayPostResult, ProductCategory productCategory, DelayProcessingTaskCacheHandler delayProcessingTaskHandler, SandboxDataSetHandler sandboxDataSetHandler) {
        Map<String, Object> data = delayPostResult.getData();
        String delayTaskConfigUuid = productCategory.getDelayTaskConfigUuid();
        DelayTaskServices delayTaskServices = (DelayTaskServices) delayTaskConfigCacheHandler.getCompiledObjectDelayTaskConfigUuid(delayTaskConfigUuid);
        if ( null != delayTaskServices) {
            HashMap<String, Object> inputMap = new HashMap<String,Object>();
               /* HashMap<String, Object> inputMap = getInputMap((SandboxDataSet)postRequest.get("sandboxDataSet"), data);*/
            Map<String, Object> resultMap = new HashMap<>();
            boolean evaluate_result = delayTaskServices.evaluate(delayPostResult, delayProcessingTaskHandler,
                    sandboxDataSetHandler, inputMap, resultMap, log);
        }
    }

}
