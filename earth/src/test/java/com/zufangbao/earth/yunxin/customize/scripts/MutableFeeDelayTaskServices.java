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
import com.zufangbao.sun.yunxin.entity.api.mutableFee.MutableFeeReasonCode;
import org.apache.commons.logging.Log;

import java.util.*;

/**
 * 费用浮动后置任务
 * Created by fanxiaofan on 2017/5/18.
 */
public class MutableFeeDelayTaskServices implements DelayTaskServices {
	@Override
	public boolean evaluate(Result processedResult, DelayProcessingTaskCacheHandler delayProcessingTaskHandler,
							SandboxDataSetHandler sandboxDataSetHandler, Map<String, Object> inputMap, Map<String, Object> resultMap, Log log) {
		try {
			// 调用时无需转发不必判断processedResult
			String financialContractUuid = (String) inputMap.get(SandboxDataSetSpec.FINANCIAL_CONTRACT_UUID);
			String contractUuid = (String) inputMap.get(SandboxDataSetSpec.CONTRACT_UUID);
			List<String> repaymentPlanNoList = (List<String>) inputMap.get(SandboxDataSetSpec.REPAYMENT_PLAN_NO_LIST);
			String configUuid = (String) inputMap.getOrDefault(SandboxDataSetSpec.CONFING_UUID, "");

			SandboxDataSet sandboxDataSet = sandboxDataSetHandler
					.get_sandbox_by_repaymentPlanNoList(financialContractUuid, contractUuid, repaymentPlanNoList);

			String extraItem = (String) inputMap.getOrDefault(SandboxDataSetSpec.EXTRA_ITEM, "");
			if (StringUtils.isEmpty(extraItem)) {
				return false;
			}

			Map<String, Object> extraParam = JsonUtils.parse(extraItem);
			String reason = (String) extraParam.getOrDefault(SandboxDataSetSpec.ExtraItem.REASON_CODE, "");
			int reasonCode = Integer.valueOf(reason);

			String changeType = null;
			if (MutableFeeReasonCode.OVERDUE.ordinal() == reasonCode) {
				changeType = "7";
			} else if (MutableFeeReasonCode.CHARGE_CHANGE.ordinal() == reasonCode) {
				changeType = "9";
			}
			if (changeType == null) {
				return false;
			}

			String idCardNo = sandboxDataSet.getIdCardNo();
			String uniqueId = sandboxDataSet.getContractUniqueId();
			Date taskExecuteDate = DateUtils.addDays(DateUtils.getToday(), 1);

			List<PaymentPlanSnapshot> paymentPlanSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
			for (PaymentPlanSnapshot paymentPlanSnapshot : paymentPlanSnapshotList) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("tradeNo", UUID.randomUUID().toString().replaceAll("-", ""));
				map.put("loanNo", uniqueId);
				map.put("thirdUserNo", idCardNo);
				map.put("changeType", changeType);
				map.put("installmentNo", paymentPlanSnapshot.getCurrentPeriod());
				map.put("tradeTime", paymentPlanSnapshot.getLastModifiedTime());

				map.put("principal",  extraParam.get(SandboxDataSetSpec.ExtraItem.PRINCIPAL));
				map.put("interest",  extraParam.get(SandboxDataSetSpec.ExtraItem.INTEREST));
				map.put("repayCharge",  extraParam.get(SandboxDataSetSpec.ExtraItem.REPAY_CHARGE));

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
