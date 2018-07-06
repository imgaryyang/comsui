package com.suidifu.bridgewater.handler.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.bridgewater.api.entity.deduct.batch.v2.BatchDeductApplication;
import com.suidifu.bridgewater.api.service.batch.v2.BatchDeductApplicationService;
import com.suidifu.bridgewater.handler.single.v2.BatchDeductApplicationBusinessHandler;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by qinweichao on 2017/9/5.
 */

@Component("batchDeductApplicationBusinessHandler")
public class BatchDeductApplicationBusinessHandlerImpl implements BatchDeductApplicationBusinessHandler {

    @Autowired
    private GenericDaoSupport genericDaoSupport;

    @Autowired
    private BatchDeductApplicationService batchDeductApplicationService;

    private static final Log logger = LogFactory.getLog(BatchDeductApplicationBusinessHandlerImpl.class);

    @Override
    public void updateBatchDeductApplicationAccordingPartDeductApplication(String batchDeductApplicationUuid) {
        if (StringUtils.isEmpty(batchDeductApplicationUuid)){
            return;
        }

        Map<String, Object> parms = new HashMap<String, Object>();

            try {
                String preAccessVersion = batchDeductApplicationService.getCurrentAccessVersion(batchDeductApplicationUuid);
                parms.put("preAccessVersion", preAccessVersion);
                parms.put("accessVersion", UUID.randomUUID().toString());
                parms.put("batchDeductApplicationUuid", batchDeductApplicationUuid);
                String executeSql = "update t_batch_deduct_application set version = :accessVersion,executed_count = executed_count+1 where batch_deduct_application_uuid =:batchDeductApplicationUuid and version =:preAccessVersion";
                genericDaoSupport.executeSQL(executeSql, parms);

                String validateSql = "select id from t_batch_deduct_application where batch_deduct_application_uuid =:batchDeductApplicationUuid and version =:accessVersion";
                int updatedRows = genericDaoSupport.queryForInt(validateSql, parms);

                if (0 >= updatedRows) {
                    logger.info(GloableLogSpec.AuditLogHeaderSpec()  + "nothing BatchDeductApplication update and batchDeductApplicationUuid:"+batchDeductApplicationUuid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


    }

    @Override
    public void updateBatchDeductApplicationByUuid(String batchDeductApplicationUuid, int actualCount) {
        if (StringUtils.isEmpty(batchDeductApplicationUuid)){
            return;
        }

        try {
            String preAccessVersion = batchDeductApplicationService.getCurrentAccessVersion(batchDeductApplicationUuid);
            Map<String, Object> params = new HashMap<>();
            params.put("actualCount",actualCount );
            params.put("preAccessVersion", preAccessVersion);
            params.put("accessVersion", UUID.randomUUID().toString());
            params.put("batchDeductApplicationUuid",batchDeductApplicationUuid );
            
            logger.info("updateBatchDeductApplicationByUuidactualDeductPlanSize["+actualCount+"]version["+params.get("accessVersion")+"]");
            String sql = "UPDATE t_batch_deduct_application set version = :accessVersion, actual_count =(actual_count+:actualCount) where batch_deduct_application_uuid =:batchDeductApplicationUuid and version =:preAccessVersion";
            genericDaoSupport.executeSQL(sql, params);

            String validateSql = "select id from t_batch_deduct_application where batch_deduct_application_uuid =:batchDeductApplicationUuid and version =:accessVersion";
            int updatedRows = genericDaoSupport.queryForInt(validateSql, params);

            if (0 >= updatedRows) {
                logger.info(GloableLogSpec.AuditLogHeaderSpec()  + "nothing BatchDeductApplication update and batchDeductApplicationUuid:"+batchDeductApplicationUuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBatchDeductApplicationSetCountFail(String batchDeductApplicationUuid, int failCount) {
        if (StringUtils.isEmpty(batchDeductApplicationUuid)){
            return;
        }

        try {
            String preAccessVersion = batchDeductApplicationService.getCurrentAccessVersion(batchDeductApplicationUuid);
            Map<String, Object> params = new HashMap<>();
            params.put("failCount",failCount );
            params.put("preAccessVersion", preAccessVersion);
            params.put("accessVersion", UUID.randomUUID().toString());
            params.put("batchDeductApplicationUuid",batchDeductApplicationUuid );
            String sql = "UPDATE t_batch_deduct_application set version = :accessVersion, fail_count =(fail_count+:failCount) where batch_deduct_application_uuid =:batchDeductApplicationUuid and version =:preAccessVersion";
            genericDaoSupport.executeSQL(sql, params);

            String validateSql = "select id from t_batch_deduct_application where batch_deduct_application_uuid =:batchDeductApplicationUuid and version =:accessVersion";
            int updatedRows = genericDaoSupport.queryForInt(validateSql, params);

            if (0 >= updatedRows) {
                logger.info(GloableLogSpec.AuditLogHeaderSpec()  + "nothing BatchDeductApplication update and batchDeductApplicationUuid:"+batchDeductApplicationUuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
