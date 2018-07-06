package com.suidifu.matryoshaka.test.scripts;

import com.suidifu.matryoshka.customize.CustomizeServices;
import com.suidifu.matryoshka.handler.SandboxDataSetHandler;
import com.suidifu.matryoshka.snapshot.PaymentPlanSnapshot;
import com.suidifu.matryoshka.snapshot.SandboxDataSet;
import com.zufangbao.sun.utils.JsonUtils;
import com.zufangbao.sun.utils.StringUtils;
import com.zufangbao.sun.yunxin.entity.OnAccountStatus;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyReason;
import com.zufangbao.sun.yunxin.entity.model.api.modify.RepaymentPlanModifyRequestDataModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 退货 Created by louguanyang on 2017/4/23.
 */
public class RefundsServices implements CustomizeServices {
	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {
		logger.info("RefundsServices has been started");
		String uniqueId = (String) preRequest.getOrDefault("uniqueId","");
		String contractNo = (String)preRequest.getOrDefault("contractNo","");
		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {
			logger.error("RefundsServices: uniqueId和contractNo需要赋值一个");
			postRequest.put("errorMsg", "uniqueId和contractNo需要赋值一个");
			return false;
		}
		String requestData = (String)preRequest.getOrDefault("requestData","");
		BigDecimal assetPrincipalAmount = BigDecimal.ZERO;
		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);
		logger.info("RefundsServices get sandboxDataSet");
		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);
		if (null == sandboxDataSet) {
			logger.error("RefundsServices:sandboxDataSet is null");
			postRequest.put("errorMsg", "sandboxDataSet is null");
			return false;
		}
		if (null == sandboxDataSet.getFinancialContractSnapshot()) {
			logger.error("RefundsServices:financialContractSnapshot is null");
			postRequest.put("errorMsg", "financialContractSnapshot is null");
			return false;
		}
		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){
			logger.error("RefundsServices:信托合同["+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+"]不支持随心还");
			postRequest.put("errorMsg", "信托合同不支持随心还");
			return false;
		}
		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
		if (CollectionUtils.isEmpty(assetSetSnapshotList)) {
			logger.error("RefundsServices:assetSetSnapshotList is Empty");
			postRequest.put("errorMsg", "assetSetSnapshotList is Empty");
			return false;
		}
		try{
			if (models.size() != 1) {
				logger.error("RefundsServices:退货后还款计划只可剩余一期");
				postRequest.put("errorMsg", "退货后还款计划只可剩余一期");
				return false;
			}
			boolean isFirstPeriodMutable = false;
			for(PaymentPlanSnapshot assetSetSnapshot: assetSetSnapshotList){
				assetPrincipalAmount = assetPrincipalAmount.add(assetSetSnapshot.getAssetPrincipalValue());
				if(1 == assetSetSnapshot.getCurrentPeriod()) {
					if (assetSetSnapshot.is_clear_repayment_plan()) {
						logger.error("RefundsServices:还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]已结清不可变更");
						postRequest.put("errorMsg", "还款计划已结清");
						return false;
					}
					if (assetSetSnapshot.is_overdue_repayment_plan()) {
						logger.error("RefundsServices:还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]已逾期不可变更");
						postRequest.put("errorMsg", "还款计划已逾期");
						return false;
					}
					OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();
					if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {
						logger.error("RefundsServices:还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]部分结算不可变更");
						postRequest.put("errorMsg", "还款计划部分结算");
						return false;
					}
					if (assetSetSnapshot.is_today_repayment_plan()) {
						logger.error("RefundsServices:还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]还款当日不允许变更");
						postRequest.put("errorMsg", "还款当日不允许变更");
						return false;
					}
					isFirstPeriodMutable = true;
				}
			}
			if(!isFirstPeriodMutable){
				logger.error("RefundsServices:首期判定失败");
				postRequest.put("errorMsg", "首期判定失败");
				return false;
			}

			RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel)models.get(0);
			//变更后本金不可小于原有本金
			if(modifyRequestDataModel.getAssetPrincipal().compareTo(assetPrincipalAmount) < 0){
				logger.error("RefundsServices:非法本金");
				postRequest.put("errorMsg", "非法本金");
				return false;
			}

			postRequest.putAll(preRequest);
			postRequest.put("fn","200001");
			postRequest.put("requestReason", RepaymentPlanModifyReason.REASON_9.getOrdinal() + "");
			logger.info("RefundsServices is end");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("RefundsServices exception occurred");
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
				return false;
			}
			String requestData = (String) parameters.getOrDefault("requestData", "");
			List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData,
					RepaymentPlanModifyRequestDataModel.class);
			BigDecimal assetPrincipalAmount = BigDecimal.ZERO;
			// 变更后，只剩一期还款计划
			if (models.size() != 1) {
				postRequest.put("errorMsg", "退货后还款计划只可剩余一期");
				return false;
			}
			boolean isFirstPeriodMutable = false;
			for (PaymentPlanSnapshot assetSetSnapshot : assetSetSnapshotList) {
				assetPrincipalAmount = assetPrincipalAmount.add(assetSetSnapshot.getAssetPrincipalValue());
				// 对第一期进行判断
				if (1 == assetSetSnapshot.getCurrentPeriod()) {
					if (assetSetSnapshot.is_immutable()) {
						postRequest.put("errorMsg", "还款计划不可变更");
						return false;
					}
					// 还款当日不允许变更
					if (assetSetSnapshot.is_today_repayment_plan()) {
						postRequest.put("errorMsg", "还款当日不允许变更");
						return false;
					}
					isFirstPeriodMutable = true;
				}
			}
			// 第一期判断未通过则判定失败
			if(!isFirstPeriodMutable){
				postRequest.put("errorMsg", "首期判定失败");
				return false;
			}
			RepaymentPlanModifyRequestDataModel modifyRequestDataModel = (RepaymentPlanModifyRequestDataModel) models.get(0);
			// 本金校验
			if(modifyRequestDataModel.getAssetPrincipal().compareTo(assetPrincipalAmount) < 0){
				postRequest.put("errorMsg", "非法本金");
				return false;
			}
			// 校验完成填写post参数
			postRequest.putAll(parameters);
			postRequest.put("fn", "200001");
			postRequest.put("requestReason", RepaymentPlanModifyReason.REASON_9.getOrdinal() + "");
            return true;
        } catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/
}
