package com.suidifu.matryoshaka.test.scripts;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import org.apache.commons.logging.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//import com.zufangbao.sun.entity.perInterface.snapshots.ContractSnapshot;
//import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSet;
//import com.zufangbao.sun.yunxin.delayTask.DelayProcessingTask;

public class ReconciliationDelayTaskServices implements DelayTaskServices {
    @Override
    public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler, SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap, Log log) {

        Date now = new Date();
        String contractUniqueId = (String)inputMap.get("uniqueId");
        String contractNo = (String) inputMap.get("contractNo");

        SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_contract_uniqueId_contractNo(contractUniqueId, contractNo);
        ContractSnapshot contractSnapshot = sandboxDataSet.getContractSnapshot();
        Map<String, String> extraData = new HashMap<String, String>();

        DelayProcessingTask task = new DelayProcessingTask();

        task.setTaskExecuteDate(DateUtils.addDays(now, 1));
        task.setWorkParams(extraData.get("workParams"));
        task.setFinancialContractUuid(contractSnapshot.getFinancialContractUuid());
        task.setContractUuid(contractSnapshot.getUuid());
        task.setCustomerUuid(contractSnapshot.getCustomerUuid());
        //task.setDelayProcessingTaskType(DelayTaskTriggerType.RECONCILIATION.ordinal());
        task.setCreateTime(now);
        task.setLastModifyTime(now);

        delayProcessingTaskHandler.save_to_db_cache(task);
        return true;
    }
}
