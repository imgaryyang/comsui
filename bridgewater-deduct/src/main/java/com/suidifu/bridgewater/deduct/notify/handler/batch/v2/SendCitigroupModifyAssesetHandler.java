package com.suidifu.bridgewater.deduct.notify.handler.batch.v2;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Created by qinweichao on 2017/9/28.
 */
@Component("sendCitigroupModifyAsseset")
public class SendCitigroupModifyAssesetHandler implements NotifyResultDelegateHandler {

    private static final Log logger = LogFactory.getLog(SendCitigroupModifyAssesetHandler.class);

    @Override
    public void onResult(NotifyJob result) {

        if (!NotifyJob.RESPONSE_CODE_200_OK.equals(result.getLastTimeHttpResponseCode())){
           logger.error("#DeductSendCitigroupModifyAssesetHandlerHandler error#result["+ JsonUtils.toJsonString(result)+"]");
        }else {
            logger.info("#DeductSendMorganStanleyModifyAssesetHandler#deductApplicationUuid["+ result.getBusinessId()+"]");
        }

    }

}
