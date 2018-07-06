package com.suidifu.morganstanley.listeners;

import com.suidifu.morganstanley.servers.FileRepositoryRedisPersistence;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJob;
import com.suidifu.swift.notifyserver.notifyserver.NotifyResultDelegateHandler;
import com.zufangbao.sun.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static java.lang.Boolean.TRUE;

/**
 * 变更还款计划回调监听
 * @author louguanyang on 2017/5/23.
 */
@Component("modifyPlanNotifyResultDelegateHandler")
@Log4j2
public class ModifyPlanNotifyResultDelegateHandler implements NotifyResultDelegateHandler {
    private static final Log LOGGER = LogFactory.getLog("FileResult");

    @Autowired
    @Qualifier("FileRepositoryRedisPersistence")
    private FileRepositoryRedisPersistence fileRepositoryRedisPersistence;

    @Override
    public void onResult(NotifyJob result) {
        try {
            String businessId = result.getBusinessId();
            String statusInRedis = fileRepositoryRedisPersistence.get(businessId);
            if (StringUtils.isNotEmpty(statusInRedis)) {
                log.info("ModifyPlanNotifyResultListener onResult is already in Redis, businessId:{}", businessId);
                return;
            }
            fileRepositoryRedisPersistence.pushToTail(businessId, TRUE.toString());
            LOGGER.info("save to Redis, @businessId=" + businessId + "@jobDir=" + result.getJobDir() + "@responseJson=" + result.getResponseJson());
        } catch (Exception e) {
            log.error("ModifyPlanNotifyResultListener onResult occur error, error message:{}", ExceptionUtils.getStackTrace(e));
        }
    }
}