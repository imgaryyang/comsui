package com.suidifu.matryoshaka.test.scripts;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.FinancialContractSnapshot;
import com.suidifu.matryoshka.snapshot.PaymentPlanExtraChargeSnapshot;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.sun.utils.DateUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyReason;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 随心还-延期还款 Created by louguanyang on 2017/4/21.
 */
public class SXHDeferredPaymentServices implements CustomizeServices {
	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {
		logger.info("SXHDeferredPaymentServices has been started");
		String uniqueId = preRequest.getOrDefault("uniqueId", "");
		String contractNo = preRequest.getOrDefault("contractNo", "");
		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {
			logger.error("SXHDeferredPaymentServices:uniqueId和contractNo需要赋值一个");
			postRequest.put("errorMsg", "uniqueId和contractNo需要赋值一个");
			return false;
		}
		String requestData = preRequest.getOrDefault("requestData", "");
		logger.info("SXHDeferredPaymentServices get sandboxDataSet");
		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);
		if (null == sandboxDataSet) {
			logger.error("SXHDeferredPaymentServices:sandboxDataSet is null");
			postRequest.put("errorMsg", "sandboxDataSet is null");
			return false;
		}
		FinancialContractSnapshot financialContractSnapshot = sandboxDataSet.getFinancialContractSnapshot();
		if (null == financialContractSnapshot) {
			logger.error("SXHDeferredPaymentServices:financialContractSnapshot is null");
			postRequest.put("errorMsg", "financialContractSnapshot is null");
			return false;
		}
		if(!financialContractSnapshot.isAllowFreewheelingRepayment()){
			logger.error("SXHDeferredPaymentServices:信托合同["+financialContractSnapshot.getContractNo()+"]不支持随心还");
			postRequest.put("errorMsg", "信托合同不支持随心还");
			return false;
		}
		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);
		try{
			if(CollectionUtils.isEmpty(assetSetSnapshotList)){
				logger.error("SXHDeferredPaymentServices:assetSetSnapshotList is Empty");
				postRequest.put("errorMsg", "assetSetSnapshotList is Empty");
				return false;
			}
			if(CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()){
				logger.error("SXHDeferredPaymentServices:还款计划数不匹配");
				postRequest.put("errorMsg", "还款计划数不匹配");
				return false;
			}
			int globalMonDelta = 0;
			for(int i = 0, len = models.size(); i < len; i ++){
				PaymentPlanSnapshot assetSetSnapshot = assetSetSnapshotList.get(i);
				RepaymentPlanModifyRequestDataModel modifyRequestDataModel = models
						.get(i);
				String asset_interest_principal_fingerPrint = modifyRequestDataModel
						.asset_interest_principal_fingerPrint();
				if (!assetSetSnapshot.check_asset_interest_principal_fingerPrint(asset_interest_principal_fingerPrint)) {
					logger.error("SXHDeferredPaymentServices:还款计划基础费用不匹配");
					postRequest.put("errorMsg", "还款计划基础费用不匹配");
					return false;
				}
				if (assetSetSnapshot.is_clear_repayment_plan()) {
					logger.error("SXHDeferredPaymentServices:还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]已结清不可变更");
					postRequest.put("errorMsg", "还款计划已结清");
					return false;
				}
				if (assetSetSnapshot.is_overdue_repayment_plan()) {
					logger.error("SXHDeferredPaymentServices:还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]已逾期不可变更");
					postRequest.put("errorMsg", "还款计划已逾期");
					return false;
				}
				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();
				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {
					logger.error("SXHDeferredPaymentServices:还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]部分结算不可变更");
					postRequest.put("errorMsg", "还款计划部分结算");
					return false;
				}
				if (assetSetSnapshot.is_today_repayment_plan()) {
					logger.error("SXHDeferredPaymentServices:还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]还款当日不允许变更");
					postRequest.put("errorMsg", "还款当日不允许变更");
					return false;
				}
				PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot = assetSetSnapshot
						.getAssetSetExtraChargeSnapshot();
				if (null == assetSetExtraChargeSnapshot) {
					logger.error("SXHDeferredPaymentServices:assetSetExtraChargeSnapshot is null");
					postRequest.put("errorMsg", "assetSetExtraChargeSnapshot is null");
					return false;
				}

				String assetExtraFeeFingerPrint = modifyRequestDataModel.assetExtraFeeFingerPrint();
				if (!assetSetExtraChargeSnapshot.check_assetExtraFeeFingerPrint(assetExtraFeeFingerPrint)) {
					logger.error("SXHDeferredPaymentServices:还款计划其他费用不匹配");
					postRequest.put("errorMsg", "还款计划其他费用不匹配");
					return false;
				}
				// 延期还款仅仅变更月份
				Date assetRecycleDate = assetSetSnapshot.getAssetRecycleDate();
				Date model_date = modifyRequestDataModel.getDate();
				int snapshot_day = DateUtils.day(assetRecycleDate);
				int model_day = DateUtils.day(model_date);
				if (snapshot_day != model_day) {
					logger.error("SXHDeferredPaymentServices:延期还款仅可变更月份");
					postRequest.put("errorMsg", "延期还款仅可变更月份");
					return false;
				}
				
				// 还款日为29/30/31日时不得延期
				if (Arrays.asList(29, 30, 31).contains(model_day)) {
					logger.error("SXHDeferredPaymentServices:还款日为29/30/31日时不得延期");
					postRequest.put("errorMsg", "还款日为29/30/31日时不得延期");
					return false;
				}

				Date addOneMonth = DateUtils.addMonths(assetRecycleDate, 1);
				boolean one_month_late = DateUtils.isSameMonth(addOneMonth, model_date);
				Date addTwoMonths = DateUtils.addMonths(assetRecycleDate, 2);
				boolean two_months_late = DateUtils.isSameMonth(addTwoMonths, model_date);

				if (!one_month_late && !two_months_late) {
					logger.error("SXHDeferredPaymentServices:延期还款月份变更在两个月内");
					postRequest.put("errorMsg", "延期还款月份变更在两个月内");
					return false;
				}
				int monDelta = one_month_late ? 1 : 2;
				// 月份变化
				if (globalMonDelta == 0) {
					globalMonDelta = monDelta;
					continue;
				}
				// 月份变化量相同
				if (globalMonDelta != monDelta) {
					logger.error("SXHDeferredPaymentServices:月份变化量必须相同");
					postRequest.put("errorMsg", "月份变化量必须相同");
					return false;
				}
			}
			postRequest.putAll(preRequest);
			postRequest.put("fn","200001");
			postRequest.put("requestReason", RepaymentPlanModifyReason.REASON_4.getOrdinal() + "");
			logger.info("SXHDeferredPaymentServices is end");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SXHDeferredPaymentServices exception occurred");
			return false;
		}
	}

	/*@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler,
                            Map<String, String> parameters,
                            Map<String, String> postRequest) {
		// 仅支持佰仟信贷合同
		if (!sandboxDataSetHandler.isAllowFreewheelingRepayment()) {
			postRequest.put("errorMsg", "信托合同不支持随心还");
			return false;
		}
		try {
			if (CollectionUtils.isEmpty(assetSetSnapshotList)) {
				postRequest.put("errorMsg", "assetSetSnapshotList is Empty");
				return false;
			}
			String requestData = (String)parameters.getOrDefault("requestData","");
			List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData,
					RepaymentPlanModifyRequestDataModel.class);
			if (CollectionUtils.isEmpty(models) || models.size() != assetSetSnapshotList.size()) {
				postRequest.put("errorMsg", "还款计划数不匹配");
				return false;
			}
			int globalMonDelta = 0;
			for (int i = 0; i < assetSetSnapshotList.size(); i++) {
				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot)assetSetSnapshotList.get(i);
				RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel)models.get(i);
				String asset_interest_principal_fingerPrint = modifyRequestDataModel
						.asset_interest_principal_fingerPrint();
				if (!assetSetSnapshot
						.check_asset_interest_principal_fingerPrint(asset_interest_principal_fingerPrint)) {
					postRequest.put("errorMsg", "还款计划基础费用不匹配");
					return false;
				}

				if (assetSetSnapshot.is_immutable()) {
					postRequest.put("errorMsg", "还款计划不可变更");
					return false;
				}

				// 还款当日不允许变更
				if (assetSetSnapshot.is_today_repayment_plan()) {
					postRequest.put("errorMsg", "还款当日不允许变更");
					return false;
				}

				PaymentPlanExtraChargeSnapshot assetSetExtraChargeSnapshot = assetSetSnapshot
						.getAssetSetExtraChargeSnapshot();
				if (null == assetSetExtraChargeSnapshot) {
					postRequest.put("errorMsg", "assetSetExtraChargeSnapshot is null");
					return false;
				}

				String assetExtraFeeFingerPrint = modifyRequestDataModel.assetExtraFeeFingerPrint();
				if (!assetSetExtraChargeSnapshot.check_assetExtraFeeFingerPrint(assetExtraFeeFingerPrint)) {
					postRequest.put("errorMsg", "还款计划其他费用不匹配");
					return false;
				}

				// 延期还款仅仅变更月份
				Date assetRecycleDate = assetSetSnapshot.getAssetRecycleDate();
				Date model_date = modifyRequestDataModel.getDate();
				int snapshot_day = DateUtils.day(assetRecycleDate);
				int model_day = DateUtils.day(model_date);
				if (snapshot_day != model_day) {
					postRequest.put("errorMsg", "延期还款仅可变更月份");
					return false;
				}
				// FIXME 还款日为29/30/31日时不得延期
				if (Arrays.asList(29, 30, 31).contains(model_day)) {
					postRequest.put("errorMsg", "还款日为29/30/31日时不得延期");
					return false;
				}

				Date addOneMonth = DateUtils.addMonths(assetRecycleDate, 1);
				boolean one_month_late = DateUtils.isSameMonth(addOneMonth, model_date);
				Date addTwoMonths = DateUtils.addMonths(assetRecycleDate, 2);
				boolean two_months_late = DateUtils.isSameMonth(addTwoMonths, model_date);

				if (!one_month_late && !two_months_late) {
					postRequest.put("errorMsg", "延期还款月份变更在两个月内");
					return false;
				}
				int monDelta = one_month_late ? 1 : 2;
				// 月份变化
				if (globalMonDelta == 0) {
					globalMonDelta = monDelta;
					continue;
				}
				// 月份变化量相同
				if (globalMonDelta != monDelta) {
					postRequest.put("errorMsg", "月份变化量必须相同");
					return false;
				}
			}

			// 校验完成填写post参数
			postRequest.putAll(parameters);
			postRequest.put("fn", "200001");
			postRequest.put("requestReason", RepaymentPlanModifyReason.REASON_4.getOrdinal() + "");

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/
}
