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
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyReason;
import org.apache.commons.logging.Log;

import java.util.*;
import java.util.stream.Collectors;

//import com.zufangbao.sun.entity.perInterface.snapshots.PaymentPlanSnapshot;
//import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSet;
//import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSetSpec;
//import com.zufangbao.sun.yunxin.delayTask.DelayProcessingTask;

/**
 * Created by fanxiaofan on 2017/5/9.
 */
public class ZhongAnRepaymentPlanChargeDelayTaskServices implements DelayTaskServices {
	@Override
	public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler,
                            SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap, Log log) {
		try {

			if (processedResult == null || !StringUtils.equalsIngoreNull("0", processedResult.getCode())) {
				return false;
			}
			String financialContractUuid = (String) inputMap.get(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID);
			String contractUuid = (String) inputMap.get(SandboxDataSetSpec.CONTRACT_UUID);
			List<String> repaymentPlanNoSrcList = (List<String>)inputMap.get(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST);
			List<String> repaymentPlanNoTarList = (List<String>)inputMap.get(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST);


			SandboxDataSet sandboxDataSetSrc = sandboxDataSetHandler
					.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid, repaymentPlanNoSrcList);
			SandboxDataSet sandboxDataSetTar = sandboxDataSetHandler
					.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid, repaymentPlanNoTarList);

			String reason = (String) sandboxDataSetTar.getExtraData().getOrDefault("reasonCode", null);
			int reasonCode = Integer.valueOf(reason);

			String changeType = null;
			if (RepaymentPlanModifyReason.REASON_7.getOrdinal() == reasonCode) {
				changeType = "3";
			} else if (RepaymentPlanModifyReason.REASON_8.getOrdinal() == reasonCode) {
				changeType = "4";
			} else if (RepaymentPlanModifyReason.REASON_6.getOrdinal() == reasonCode) {
				changeType = "5";
			} else if (RepaymentPlanModifyReason.REASON_9.getOrdinal() == reasonCode) {
				changeType = "6";
			}
			if (changeType == null) {
				return false;
			}

			String idCardNo = sandboxDataSetTar.getIdCardNo();
			String uniqueId = sandboxDataSetTar.getContractUniqueId();
			Date taskExecuteDate = DateUtils.addDays(DateUtils.getToday(), 1);

			List<PaymentPlanSnapshot> paymentPlanSnapshotSrcList = sandboxDataSetSrc.getPaymentPlanSnapshotList();
			Map<Integer,PaymentPlanSnapshot> srcMap=paymentPlanSnapshotSrcList.stream().collect(Collectors.toMap(PaymentPlanSnapshot::getCurrentPeriod, fc -> fc));
			
			List<PaymentPlanSnapshot> paymentPlanSnapshotList = sandboxDataSetTar.getPaymentPlanSnapshotList();
			for (PaymentPlanSnapshot paymentPlanSnapshot : paymentPlanSnapshotList) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("tradeNo", UUID.randomUUID().toString());
				map.put("loanNo", uniqueId);
				map.put("thirdUserNo", idCardNo);

				PaymentPlanSnapshot srcSnapshot=srcMap.get(paymentPlanSnapshot.getCurrentPeriod());
				// 提前结清、退货installmentNo直接传0
				if (changeType.equals("4") || changeType.equals("5") || changeType.equals("6")) {
					map.put("installmentNo", 0);
				}
				else{
					map.put("installmentNo", paymentPlanSnapshot.getCurrentPeriod());
				}

				//所有第0期，应收表中的应收金额不用做减法
				int installmentNo=(int) map.get("installmentNo");
				if (0 == installmentNo) {
					map.put("principal", paymentPlanSnapshot.getAssetPrincipalValue());
					map.put("interest", paymentPlanSnapshot.getAssetInterestValue());
					map.put("repayCharge", paymentPlanSnapshot.getRepayCharge());
				}else{
					map.put("principal", paymentPlanSnapshot.getAssetPrincipalValue().subtract(srcSnapshot.getAssetPrincipalValue()));
					map.put("interest", paymentPlanSnapshot.getAssetInterestValue().subtract(srcSnapshot.getAssetInterestValue()));
					map.put("repayCharge",paymentPlanSnapshot.getRepayCharge().subtract(srcSnapshot.getRepayCharge()));

				}

				map.put("changeType", changeType);
				map.put("tradeTime", paymentPlanSnapshot.getCreateTime());

				JSONObject jsonObject = new JSONObject(map);
				String workParam = JsonUtils.toJSONString(jsonObject);

				DelayProcessingTask task = new DelayProcessingTask(paymentPlanSnapshot, taskExecuteDate, workParam,"");
				delayProcessingTaskHandler.save_to_db_cache(task);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
