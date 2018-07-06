package com.zufangbao.earth.yunxin.api.dataSync.task;

import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.data_sync_function_point;
import com.zufangbao.wellsfargo.yunxin.data.sync.handler.DataSyncHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSyncTask {
    private static final Log logger = LogFactory.getLog(DataSyncTask.class);
    @Autowired
    private DataSyncHandler dataSyncHandler;

    /**
     * 1 补刷mq发送失败的jv。(数据在一小时之前) (1000个每次)
     */
    public void fillHandleMissingJV() {
        logger.info(GloableLogSpec.AuditLogHeaderSpec() + data_sync_function_point.HANDLE_MQ_MISSING_JV);
        dataSyncHandler.handleMissingJV();
    }
}

