package com.zufangbao.earth.yunxin.customize.scripts;

import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.suidifu.matryoshka.customize.DelayTaskServices;
import com.suidifu.matryoshka.delayPosition.handler.DelayProcessingTaskCacheHandler;
import com.suidifu.matryoshka.delayTask.DelayProcessingTask;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.suidifu.matryoshka.snapshot.SandboxDataSetSpec;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyReason;
import org.apache.commons.logging.Log;

import java.util.*;

/**
 * 变更还款时间文件
 * @author louguanyang on 2017/5/8.
 */
public class ZhongAnRepaymentPlanDateDelayTaskServices implements DelayTaskServices {
    @Override
    public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler,
                            SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object>
                                     inputMap, Map<String, Object> resultMap, Log log) {
        try {
            if (processedResult != null && !StringUtils.equalsIngoreNull("0", processedResult.getCode())) {
                return false;
            }
            List<String> repaymentPlanNoList = (List<String>)inputMap.get(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST);
            String financialContractUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "");
            String contractUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.CONTRACT_UUID, "");
            String configUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.CONFING_UUID, "");

            SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid,repaymentPlanNoList);
            String changeType = sandboxDataSet.getExtraData().getOrDefault("reasonCode", null);
            if (changeType == null || configUuid == null) {
                return false;
            }
            if (StringUtils.equalsIngoreNull(changeType, RepaymentPlanModifyReason.REASON_4.getOrdinal() + "")) {
                changeType = "1";
            } else if (StringUtils.equalsIngoreNull(changeType, RepaymentPlanModifyReason.REASON_5.getOrdinal() + "")) {
                changeType = "2";
            } else {
                return false;
            }
            String idCardNo = sandboxDataSet.getIdCardNo();
            String uniqueId = sandboxDataSet.getContractUniqueId();
            Date taskExecuteDate = DateUtils.addDays(DateUtils.getToday(), 1);

            List<PaymentPlanSnapshot> paymentPlanSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
            for (PaymentPlanSnapshot paymentPlanSnapshot: paymentPlanSnapshotList) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("tradeNo", UUID.randomUUID().toString().replaceAll("-", ""));
                map.put("loanNo", uniqueId);
                map.put("thirdUserNo", idCardNo);
                map.put("installmentNo", paymentPlanSnapshot.getCurrentPeriod());
                map.put("agreedRepayDate", paymentPlanSnapshot.getAssetRecycleDate());
                map.put("changeType", changeType);
                map.put("tradeTime", paymentPlanSnapshot.getCreateTime());

                JSONObject jsonObject = new JSONObject(map);
                String workParam = JsonUtils.toJSONString(jsonObject);

                DelayProcessingTask task = new DelayProcessingTask(paymentPlanSnapshot, taskExecuteDate, workParam,
                        configUuid);
                delayProcessingTaskHandler.save_to_db_cache(task);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
