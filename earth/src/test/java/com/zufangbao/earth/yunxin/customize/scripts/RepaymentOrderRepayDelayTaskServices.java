package com.zufangbao.earth.yunxin.customize.scripts;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.ContractSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.sun.utils.AmountUtils;
import com.zufangbao.sun.utils.DateUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;

public class RepaymentOrderRepayDelayTaskServices implements DelayTaskServices{
    @Override
    public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler, SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap, Log log) {

        Date tradeTime = (Date)inputMap.get("tradeTime");
        Date assetRecycleDate = (Date)inputMap.get("assetRecycleDate");
        if(tradeTime!=null && assetRecycleDate!=null && com.zufangbao.sun.utils.DateUtils.compareTwoDatesOnDay(tradeTime,assetRecycleDate)>0){
            return false;
        }

        Date now = new Date();
        String contractUniqueId = (String)inputMap.get("uniqueId");
        String contractNo = (String) inputMap.get("contractNo");
        String repaymentPlanUuid = (String)inputMap.get("repaymentPlanUuid");
        SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_contract_uniqueId_contractNo(contractUniqueId, contractNo);
        ContractSnapshot contractSnapshot = sandboxDataSet.getContractSnapshot();
        DelayProcessingTask task = new DelayProcessingTask();

        task.setTaskExecuteDate(DateUtils.addDays(now, 1));

        task.setFinancialContractUuid(contractSnapshot.getFinancialContractUuid());
        task.setContractUuid(contractSnapshot.getUuid());
        task.setCustomerUuid(contractSnapshot.getCustomerUuid());
        task.setRepaymentPlanUuid(repaymentPlanUuid);
        task.setRepurchaseDocUuid((String)inputMap.get("repurchaseDocUuid"));
        task.setConfigUuid((String)inputMap.get("configUuid"));
        task.setCreateTime(now);
        task.setLastModifyTime(now);

        Map<String,String> workParams = new HashMap<String,String>();
        int currentPeriod = (Integer) inputMap.get("assetSetCurrentPeriod");
        com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail detailAmount = (com.zufangbao.sun.yunxin.entity.model.RepaymentChargesDetail)inputMap.get("detailAmount");
        workParams.put("tradeNo", "BQ"+contractSnapshot.getUniqueId());
        workParams.put("loanNo", contractSnapshot.getUniqueId());
        workParams.put("thirdUserNo", sandboxDataSet.getIdCardNo());
        workParams.put("installmentNo", currentPeriod+"");

        workParams.put("paidPrincipal", AmountUtils.transToAmount(detailAmount.getLoanAssetPrincipal())+"");
        workParams.put("paidInterest", AmountUtils.transToAmount(detailAmount.getLoanAssetInterest())+"");
        workParams.put("paidRepayCharge", AmountUtils.transToAmount(detailAmount.get_total_fee_except_principal_interest())+"");
        workParams.put("repayType", getRepayType((com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType)inputMap.get("repaymentBusinessType"),repaymentPlanUuid)+"");
        workParams.put("tradeTime", tradeTime==null?"":com.zufangbao.sun.utils.DateUtils.format(tradeTime, com.zufangbao.sun.utils.DateUtils.LONG_DATE_FORMAT));
        task.setWorkParams(JsonUtils.toJsonString(workParams));

        delayProcessingTaskHandler.save_to_db_cache(task);
        resultMap.put("delayProcessingTaskUuid", task.getUuid());
        return true;
    }
    private int getRepayType(com.zufangbao.sun.entity.repayment.order.RepaymentBusinessType repaymentBusinessType, String assetSetUuid){
        //1. 提前结清还款
        //2. 退货还款
        //3. 回购还款
        //4. 正常还款
        if(repaymentBusinessType.isRepurchase()){
            return 3;
        }
        if(StringUtils.isEmpty(assetSetUuid)){
            return 1;
        }
        return 4;
    }

}
