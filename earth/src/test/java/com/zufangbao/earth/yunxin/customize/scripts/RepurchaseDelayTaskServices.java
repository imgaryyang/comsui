package com.zufangbao.earth.yunxin.customize.scripts;

import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.suidifu.matryoshka.snapshot.RepurchaseDocSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import org.apache.commons.logging.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 回购后置任务逻辑
 * @author louguanyang on 2017/5/8.
 */
public class RepurchaseDelayTaskServices implements DelayTaskServices{
    @Override
    public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskCacheHandler, SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap, Log log) {
        try{
            Date now = new Date();
            String contractUniqueId = (String)inputMap.get("uniqueId");
            String contractNo = (String) inputMap.get("contractNo");
            String taskConfigUuid =(String) inputMap.get("taskConfigUuid");

            SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(contractUniqueId, contractNo);
            ContractSnapshot contractSnapshot = sandboxDataSet.getContractSnapshot();
            RepurchaseDocSnapshot repurchaseDocSnapshot = sandboxDataSet.getRepurchaseDocSnapshot();

            DelayProcessingTask task = new DelayProcessingTask();
            task.setCreateTime(now);
            task.setConfigUuid(taskConfigUuid);
            task.setRepurchaseDocUuid(repurchaseDocSnapshot.getRepurchaseDocUuid());
            task.setTaskExecuteDate(DateUtils.addDays(now, 1));

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("tradeNo", UUID.randomUUID().toString().replaceAll("-", "")); // 交易号
            map.put("loanNo", contractSnapshot.getUniqueId()); //支付号
            map.put("thirdUserNo",contractSnapshot.getIdCardNo());//商户用户号
            map.put("installmentNo", repurchaseDocSnapshot.getFstPeriod() +"");//当前期数
            map.put("principal",repurchaseDocSnapshot.getRepurchasePrincipal());//应还本金
            map.put("interest",repurchaseDocSnapshot.getRepurchaseInterest());//应还利息
            map.put("repayCharge",repurchaseDocSnapshot.getRepurchaseFee());//应还费用
            map.put("changeType",8);//变更利息
            map.put("tradeTime",repurchaseDocSnapshot.getCreateTime());//交易时间
            String workParams = JsonUtils.toJSONString(map);
            task.setWorkParams(workParams);

            task.setFinancialContractUuid(contractSnapshot.getFinancialContractUuid());
            task.setContractUuid(contractSnapshot.getUuid());
            task.setCustomerUuid(contractSnapshot.getCustomerUuid());
            task.setCreateTime(now);
            task.setLastModifyTime(now);

            delayProcessingTaskCacheHandler.save_to_db_cache(task);

            return true;
        }catch (Exception e){
            return false;
        }
    }
}
