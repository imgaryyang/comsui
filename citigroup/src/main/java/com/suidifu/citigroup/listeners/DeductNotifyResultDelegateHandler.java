package com.suidifu.citigroup.listeners;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Created by qinweichao on 2017/10/19.
 */

@Component("deductNotifyResultDelegateHandler")
public class DeductNotifyResultDelegateHandler implements NotifyResultDelegateHandler {

    private static final Log logger = LogFactory.getLog(DeductNotifyResultDelegateHandler.class);

    @Override
    public void onResult(NotifyJob result) {
        try{
            if(!NotifyJob.RESPONSE_CODE_200_OK.equals(result.getLastTimeHttpResponseCode())) {
                logger.info("citigroup send to deduct error result:"+JsonUtils.toJsonString(result));

            }
        }catch (Exception e){
            e.printStackTrace();

        }

    }
}
