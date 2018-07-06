package com.suidifu.matryoshaka.test.scripts;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.FinancialContractSnapshot;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyReason;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;

//import com.zufangbao.sun.entity.perInterface.snapshots.PaymentPlanSnapshot;

/**
 * Created by louguanyang on 2017/4/23.
 */
public class ChargeChangeServices implements CustomizeServices {
	/*
	 * @Override public boolean evaluate(SandboxDataSetHandler
	 * sandboxDataSetHandler, Map<String, String> parameters, Map<String,
	 * String> postRequest) { // 仅支持佰仟信贷合同 if
	 * (!sandboxDataSetHandler.isAllowFreewheelingRepayment()) {
	 * postRequest.put("errorMsg", "信托合同不支持随心还"); return false; } try { if
	 * (CollectionUtils.isEmpty(assetSetSnapshotList)) {
	 * postRequest.put("errorMsg", "assetSetSnapshotList is Empty"); return
	 * false; } String requestData = parameters.getOrDefault("requestData", "");
	 * List<RepaymentPlanModifyRequestDataModel> models =
	 * JsonUtils.parseArray(requestData,
	 * RepaymentPlanModifyRequestDataModel.class); if
	 * (CollectionUtils.isEmpty(models) || models.size() !=
	 * assetSetSnapshotList.size()) { postRequest.put("errorMsg", "还款计划数不匹配");
	 * return false; } for (int i = 0; i < assetSetSnapshotList.size(); i++) {
	 * PaymentPlanSnapshot assetSetSnapshot = assetSetSnapshotList.get(i);
	 * RepaymentPlanModifyRequestDataModel modifyRequestDataModel = models
	 * .get(i); String assetFingerPrint =
	 * modifyRequestDataModel.assetFingerPrint(); if
	 * (!assetSetSnapshot.checkAssetFingerPrint(assetFingerPrint)) {
	 * postRequest.put("errorMsg", "还款计划基础费用不匹配"); return false; } if
	 * (assetSetSnapshot.is_immutable()) { postRequest.put("errorMsg",
	 * "还款计划不可变更"); return false; } }
	 * 
	 * // 校验完成填写post参数 postRequest.putAll(parameters); postRequest.put("fn",
	 * "200001"); postRequest.put("requestReason",
	 * RepaymentPlanModifyReason.REASON_9.getOrdinal() + "");
	 * 
	 * return true; } catch (Exception e) { e.printStackTrace(); return false; }
	 * }
	 */

	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest,
			Map<String, String> postRequest, Log logger) {
		logger.info("ChargeChangeServices has been started");
		String uniqueId = (String) preRequest.get("uniqueId");
		String contractNo = (String) preRequest.get("contractNo");
		String requestData = (String) preRequest.getOrDefault("requestData", "");
		logger.info("get sandboxDataSet");
		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);
		if (null == sandboxDataSet) {
			logger.error("sandboxDataSet is null");
			postRequest.put("errorMsg", "sandboxDataSet is null");
			return false;
		}
		FinancialContractSnapshot financialContractSnapshot = sandboxDataSet.getFinancialContractSnapshot();
		if (null == financialContractSnapshot) {
			logger.error("financialContractSnapshot is null");
			postRequest.put("errorMsg", "financialContractSnapshot is null");
			return false;
		}
		if (!financialContractSnapshot.isAllowFreewheelingRepayment()) {
			logger.error("信托合同["+financialContractSnapshot.getContractNo()+"]不支持随心还");
			postRequest.put("errorMsg", "信托合同不支持随心还");
			return false;
		}

		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData,
				RepaymentPlanModifyRequestDataModel.class);
		try {
			if (CollectionUtils.isEmpty(assetSetSnapshotList)) {
				logger.error("assetSetSnapshotList is Empty");
				postRequest.put("errorMsg", "assetSetSnapshotList is Empty");
				return false;
			}
			if (CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()) {
				logger.error("还款计划数不匹配");
				postRequest.put("errorMsg", "还款计划数不匹配");
				return false;
			}
			for (int i = 0, len = models.size(); i < len; i++) {
				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot) assetSetSnapshotList.get(i);
				if (assetSetSnapshot.is_clear_repayment_plan()) {
					logger.error("还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]已结清不可变更");
					postRequest.put("errorMsg", "还款计划已结清");
					return false;
				}
				if (assetSetSnapshot.is_overdue_repayment_plan()) {
					logger.error("还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]已逾期不可变更");
					postRequest.put("errorMsg", "还款计划已逾期");
					return false;
				}
				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();
				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {
					logger.error("还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]部分结算不可变更");
					postRequest.put("errorMsg", "还款计划部分结算");
					return false;
				}
			}
			postRequest.putAll(preRequest);
			postRequest.put("fn", "200001");
			postRequest.put("requestReason", RepaymentPlanModifyReason.REASON_9.getOrdinal() + "");
			logger.info("ChargeChangeServices is end");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ChargeChangeServices exception occurred");
			return false;
		}
	}
}
