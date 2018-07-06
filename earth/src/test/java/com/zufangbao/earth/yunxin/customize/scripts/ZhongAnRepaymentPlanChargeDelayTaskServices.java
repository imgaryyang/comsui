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

//import com.zufangbao.sun.entity.perInterface.snapshots.PaymentPlanSnapshot;
//import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSet;
//import com.zufangbao.sun.entity.perInterface.snapshots.SandboxDataSetSpec;
//import com.zufangbao.sun.yunxin.delayTask.DelayProcessingTask;
//import com.zufangbao.wellsfargo.yunxin.customize.DelayTaskServices;
//import com.zufangbao.wellsfargo.yunxin.handler.SandboxDataSetHandler;
//import com.zufangbao.wellsfargo.yunxin.handler.delayTask.DelayProcessingTaskCacheHandler;

/**
 * 变更应还金额文件
 * Created by fanxiaofan on 2017/5/9.
 */
public class ZhongAnRepaymentPlanChargeDelayTaskServices implements DelayTaskServices {
	@Override
	public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler,
							SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap, Log log) {
		try {

			if (processedResult == null || !StringUtils.equalsIngoreNull("0", processedResult.getCode())||inputMap==null) {
				return false;
			}
			String financialContractUuid = (String) inputMap.get(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID);
			String contractUuid = (String) inputMap.get(SandboxDataSetSpec.CONTRACT_UUID);
			List<String> repaymentPlanNoSrcList = (List<String>)inputMap.get(SandboxDataSetSpec.ORIGINAL_REPAYMENT_PLAN_NO_LIST);
			List<String> repaymentPlanNoTarList = (List<String>)inputMap.get(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST);
	        String configUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.CONFING_UUID, "");

			SandboxDataSet sandboxDataSetSrc = sandboxDataSetHandler
					.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid, repaymentPlanNoSrcList);
			SandboxDataSet sandboxDataSetTar = sandboxDataSetHandler
					.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid, repaymentPlanNoTarList);

			String reason = sandboxDataSetTar.getExtraData().getOrDefault("reasonCode", null);
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
			Map<Integer,PaymentPlanSnapshot> srcMap=new HashMap<Integer,PaymentPlanSnapshot>();
			for (PaymentPlanSnapshot paymentPlanSnapshot : paymentPlanSnapshotSrcList) {
				srcMap.put(paymentPlanSnapshot.getCurrentPeriod(), paymentPlanSnapshot);
			}
			List<PaymentPlanSnapshot> paymentPlanSnapshotList = sandboxDataSetTar.getPaymentPlanSnapshotList();
			for (PaymentPlanSnapshot paymentPlanSnapshot : paymentPlanSnapshotList) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("tradeNo", UUID.randomUUID().toString().replaceAll("-", ""));
				map.put("loanNo", uniqueId);
				map.put("thirdUserNo", idCardNo);

				PaymentPlanSnapshot srcSnapshot = srcMap.get(paymentPlanSnapshot.getCurrentPeriod());
				map.put("installmentNo", paymentPlanSnapshot.getCurrentPeriod());

				//所有第0期，应收表中的应收金额不用做减法
				int installmentNo = Integer.valueOf(map.get("installmentNo").toString());
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

				DelayProcessingTask task = new DelayProcessingTask(paymentPlanSnapshot, taskExecuteDate, workParam,configUuid);
				delayProcessingTaskHandler.save_to_db_cache(task);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
