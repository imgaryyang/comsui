package com.zufangbao.earth.yunxin.customize.scripts;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyReason;
import org.apache.commons.logging.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReconciliationDelayTaskServices implements DelayTaskServices{
    @Override
    public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler, SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap, Log log) {

        Date now = new Date();
        String contractUniqueId = (String)inputMap.get("uniqueId");
        String contractNo = (String) inputMap.get("contractNo");

        SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_contract_uniqueId_contractNo(contractUniqueId, contractNo);
        ContractSnapshot contractSnapshot = sandboxDataSet.getContractSnapshot();
        DelayProcessingTask task = new DelayProcessingTask();

        task.setTaskExecuteDate(com.zufangbao.sun.utils.DateUtils.addDays(now, 1));

        task.setFinancialContractUuid(contractSnapshot.getFinancialContractUuid());
        task.setContractUuid(contractSnapshot.getUuid());
        task.setCustomerUuid(contractSnapshot.getCustomerUuid());
        task.setRepaymentPlanUuid((String)inputMap.get("repaymentPlanUuid"));
        task.setRepurchaseDocUuid((String)inputMap.get("repurchaseDocUuid"));
        task.setConfigUuid((String)inputMap.get("configUuid"));
        task.setCreateTime(now);
        task.setLastModifyTime(now);

        Map<String,String> workParams = new HashMap<String,String>();
        int repayType = getRepayType((RepaymentPlanModifyReason)inputMap.get("repaymentPlanModifyReason"), (com.zufangbao.sun.yunxin.entity.AssetRecoverType)inputMap.get("assetRecoverType"));
        int currentPeriod = getCurrentPeriod(repayType, (Integer) inputMap.get("assetSetCurrentPeriod"));
        com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail detailAmount = (com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail)inputMap.get("detailAmount");
        workParams.put("tradeNo", UUID.randomUUID().toString().replaceAll("-", ""));
        workParams.put("loanNo", contractSnapshot.getUniqueId());
        workParams.put("thirdUserNo", sandboxDataSet.getIdCardNo());
        workParams.put("installmentNo", currentPeriod+"");
        workParams.put("paidPrincipal", AmountUtils.transToAmount(detailAmount.getLoanAssetPrincipal())+"");
        workParams.put("paidInterest", AmountUtils.transToAmount(detailAmount.getLoanAssetInterest())+"");
        workParams.put("paidRepayCharge", AmountUtils.transToAmount(detailAmount.get_total_fee_except_principal_interest())+"");
        workParams.put("repayType", repayType+"");
        workParams.put("tradeTime", DateUtils.format((Date)inputMap.get("tradeTime"),com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT));

        task.setWorkParams(JsonUtils.toJsonString(workParams));

        delayProcessingTaskHandler.save_to_db_cache(task);
        resultMap.put("delayProcessingTaskUuid", task.getUuid());
        return true;
    }
    private int getRepayType(RepaymentPlanModifyReason repaymentPlanModifyReason, com.zufangbao.sun.yunxin.entity.AssetRecoverType assetRecoverType){
        //1. 提前结清还款
        //2. 退货还款
        //3. 回购还款
        //4. 正常还款
        if(assetRecoverType.isRepurchaseAsset()){
            return 3;
        }

        if(repaymentPlanModifyReason==RepaymentPlanModifyReason.REASON_6
                ||repaymentPlanModifyReason==RepaymentPlanModifyReason.REASON_8){
            return 1;
        }
        if(repaymentPlanModifyReason==RepaymentPlanModifyReason.REASON_9){
            return 2;
        }

        return 4;
    }

    private int getCurrentPeriod(int repayType, Integer assetSetCurrentPeriod){
        if(repayType==3){
            return 0;
        }
        return assetSetCurrentPeriod==null?0:assetSetCurrentPeriod;
    }
}
