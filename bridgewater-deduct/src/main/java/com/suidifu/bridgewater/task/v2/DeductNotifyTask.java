package com.suidifu.bridgewater.task.v2;

import com.suidifu.bridgewater.deduct.notify.handler.batch.v2.DeductNotifyJobServer;
import com.suidifu.bridgewater.handler.DeductApplicationBusinessHandler;
import com.suidifu.bridgewater.model.v2.BasicTask;
import com.zufangbao.gluon.api.bridgewater.deduct.model.v2.DeductRequestModel;
import com.zufangbao.sun.api.model.deduct.DeductApplicationReceiveStatus;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductRequestLogService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductRequestLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by qinweichao on 2017/9/13.
 */

@Component("deductNotifyTask")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeductNotifyTask extends BasicTask {

    private static Log logger = LogFactory.getLog(DeductNotifyTask.class);

    @Autowired
    private DeductApplicationBusinessHandler deductApplicationBusinessHandler;

    @Autowired
    private DeductNotifyJobServer deductSendCitigroupNotifyJobServer;

    @Autowired
    private DeductApplicationService deductApplicationService;

    @Autowired
    DeductRequestLogService deductRequestLogService;

    @Value("#{config['notifyserver.groupCacheJobQueueMap_group1']}")
    private String groupNameForDeductBusinessAcceptance;

    @Value("#{config['urlToCitigroup']}")
    private String urlToCitigroup;

    @Value("#{config['urlForCitigroupCallBack']}")
    private String urlForCitigroupCallBack;

    @Value("#{config['urlForJpmorganCallback']}")
    private String urlForJpmorganCallback;

    public void run(){
        logger.info("# execDeductPushMorganStanley begin.");
        long start = System.currentTimeMillis();

        try{

            List<String> deductApplicationUuids = deductApplicationBusinessHandler.getDeductApplicationUuidWaittingToCitigroup();

            logger.info("扣款等待业务校验，总计："+deductApplicationUuids.size()+"笔");
            loopNotifydeductToCitigroup(deductApplicationUuids);

            long end = System.currentTimeMillis();
            logger.info("# execDeductPushMorganStanley end use:" + (end-start) + "ms");
        }catch (Exception e){
            logger.info("# execDeductPushMorganStanley error.");
            e.printStackTrace();
        }



    }

    private void loopNotifydeductToCitigroup(List<String> deductApplicationUuids){
        for (String deductApplicationUuid : deductApplicationUuids) {

            try {

                DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);

                if (deductApplication == null || deductApplication.getPlanNotifyCitigroupNumber() <= deductApplication.getActualNotifyCitigroupNumber()){
                    continue;
                }

                String newCheckResponseNo = UUID.randomUUID().toString();
                deductApplicationBusinessHandler.updatDeductApplicationToCitigroup(deductApplication, DeductApplicationReceiveStatus.CREATE.ordinal(), newCheckResponseNo);

                DeductRequestLog deductRequestLog = deductRequestLogService.getDeductRequestLogByRequestNo(deductApplication.getRequestNo());
                DeductRequestModel model = deductRequestLog.getDeductRequestModel(deductApplication.getDeductApplicationUuid(),
                        urlForJpmorganCallback, urlForCitigroupCallBack, newCheckResponseNo);

                deductApplicationBusinessHandler.pushJobToCitigroup(model, deductSendCitigroupNotifyJobServer, deductRequestLog.getPostProcessUrl(),groupNameForDeductBusinessAcceptance);

            } catch (Exception e) {
                logger.error("#扣款发送citigroup异常，扣款申请号［"+deductApplicationUuid+"］！");
                e.printStackTrace();
            }
        }
    }


}
