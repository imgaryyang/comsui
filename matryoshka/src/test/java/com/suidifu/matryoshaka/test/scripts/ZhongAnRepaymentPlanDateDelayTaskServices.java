package com.suidifu.matryoshaka.test.scripts;

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
import org.apache.commons.logging.Log;

import java.util.*;

//import com.zufangbao.sun.entity.perInterface.snapshots.PaymentPlanSnapshot;
//import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSet;
//import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSetSpec;

/**
 * Created by louguanyang on 2017/5/8.
 */
public class ZhongAnRepaymentPlanDateDelayTaskServices implements DelayTaskServices {
    @Override
    public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler,
                            SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object>
                                        inputMap, Map<String, Object> resultMap, Log log) {
        if (processedResult != null && !StringUtils.equalsIngoreNull("0", processedResult.getCode())) {
            return false;
        }
        ArrayList<String> repaymentPlanNoList = (ArrayList<String>) inputMap.getOrDefault(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST, Collections.emptyList());
        String financialContractUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID, "");
        String contractUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.CONTRACT_UUID, "");
        SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid,repaymentPlanNoList);

        String changeType = sandboxDataSet.getExtraData().getOrDefault("changeType", null);
        if (changeType == null) {
            return false;
        }
        
		byte[] encode_byte = Base64.getEncoder().encode(sandboxDataSet.getIdCardNo().getBytes());
		String idCardNo = new String(encode_byte);
        
        String uniqueId = sandboxDataSet.getContractUniqueId();
        Date taskExecuteDate = DateUtils.addDays(DateUtils.getToday(), 1);

        List<PaymentPlanSnapshot> paymentPlanSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
        for (PaymentPlanSnapshot paymentPlanSnapshot: paymentPlanSnapshotList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("tradeNo", UUID.randomUUID().toString());
            map.put("loanNo", uniqueId);
            map.put("thirdUserNo", idCardNo);
            map.put("installmentNo", paymentPlanSnapshot.getCurrentPeriod());
            map.put("agreedRepayDate", paymentPlanSnapshot.getAssetRecycleDate());
            map.put("changeType", changeType);
            map.put("tradeTime", paymentPlanSnapshot.getCreateTime());

            JSONObject jsonObject = new JSONObject(map);
            String workParam = JsonUtils.toJSONString(jsonObject);

            DelayProcessingTask task = new DelayProcessingTask(paymentPlanSnapshot, taskExecuteDate, workParam,"");
            delayProcessingTaskHandler.save_to_db_cache(task);
        }
        return true;
    }
}
