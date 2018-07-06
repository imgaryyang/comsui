package com.suidifu.pricewaterhouse.yunxin.task;

import com.suidifu.pricewaterhouse.yunxin.handler.InstitutionReconciliationRPCHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhenghangbo on 28/03/2017.
 */
@Component
public class InstitutionReconciliationHandleTask {


    private static Log logger = LogFactory.getLog(InstitutionReconciliationHandleTask.class);
    @Autowired
    private InstitutionReconciliationRPCHandler institutionReconciliationRPCHandler;

    public void handleMissedTranscationCommand(){

        try {
            institutionReconciliationRPCHandler.loopProduceMQInformation();
        }catch  (Exception e) {
            logger.error("occur exception when handle missing transcation command");
            e.printStackTrace();
        }
    }


}
