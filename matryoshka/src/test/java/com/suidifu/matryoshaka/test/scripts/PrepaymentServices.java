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

import java.util.List;
import java.util.Map;

/**
 * 提前结清 Created by louguanyang on 2017/4/23.
 */
public class PrepaymentServices implements CustomizeServices {
	@Override
	public boolean evaluate(SandboxDataSetHandler sandboxDataSetHandler, Map<String, String> preRequest, Map<String, String> postRequest, Log logger) {
		logger.info("PrepaymentServices has been started");
		String uniqueId = (String) preRequest.getOrDefault("uniqueId","");
		String contractNo = (String)preRequest.getOrDefault("contractNo","");
		if (StringUtils.isEmpty(uniqueId) && StringUtils.isEmpty(contractNo)) {
			logger.error("PrepaymentServices: uniqueId和contractNo需要赋值一个");
			postRequest.put("errorMsg", "uniqueId和contractNo需要赋值一个");
			return false;
		}
		String requestData = (String)preRequest.getOrDefault("requestData","");
		List<RepaymentPlanModifyRequestDataModel> models = JsonUtils.parseArray(requestData, RepaymentPlanModifyRequestDataModel.class);
		logger.info("PrepaymentServices get sandboxDataSet");
		SandboxDataSet sandboxDataSet = sandboxDataSetHandler.get_sandbox_for_apply_repurchase_by_contract_uniqueId_contractNo(uniqueId, contractNo);
		if (null == sandboxDataSet) {
			logger.error("PrepaymentServices: sandboxDataSet is null");
			postRequest.put("errorMsg", "sandboxDataSet is null");
			return false;
		}
		if (null == sandboxDataSet.getFinancialContractSnapshot()) {
			logger.error("PrepaymentServices: financialContractSnapshot is null");
			postRequest.put("errorMsg", "financialContractSnapshot is null");
			return false;
		}
		if(!sandboxDataSet.getFinancialContractSnapshot().isAllowFreewheelingRepayment()){
			logger.error("PrepaymentServices: 信托合同["+sandboxDataSet.getFinancialContractSnapshot().getContractNo()+"]不支持随心还");
			postRequest.put("errorMsg", "信托合同不支持随心还");
			return false;
		}
		List<PaymentPlanSnapshot> assetSetSnapshotList = sandboxDataSet.getPaymentPlanSnapshotList();
		try{
			if(CollectionUtils.isEmpty(assetSetSnapshotList)){
				logger.error("PrepaymentServices: assetSetSnapshotList is Empty");
				postRequest.put("errorMsg", "assetSetSnapshotList is Empty");
				return false;
			}
			for(int i = 0, len = models.size(); i < len; i ++){
				PaymentPlanSnapshot assetSetSnapshot = (PaymentPlanSnapshot)assetSetSnapshotList.get(i);
				//还款成功和逾期
				if (assetSetSnapshot.is_clear_repayment_plan()) {
					logger.error("PrepaymentServices: 还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]已结清不可变更");
					postRequest.put("errorMsg", "还款计划已结清");
					return false;
				}
				if (assetSetSnapshot.is_overdue_repayment_plan()) {
					logger.error("PrepaymentServices: 还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]已逾期不可变更");
					postRequest.put("errorMsg", "还款计划已逾期");
					return false;
				}
				OnAccountStatus onAccountStatus = assetSetSnapshot.getOnAccountStatus();
				if (OnAccountStatus.ON_ACCOUNT != onAccountStatus) {
					logger.error("PrepaymentServices: 还款计划[" + assetSetSnapshot.getSingleLoanContractNo() + "]部分结算不可变更");
					postRequest.put("errorMsg", "还款计划部分结算");
					return false;
				}
			}

			postRequest.putAll(preRequest);
			postRequest.put("fn","200001");
			postRequest.put("requestReason", RepaymentPlanModifyReason.REASON_8.getOrdinal() + "");
			logger.info("PrepaymentServices is end");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("PrepaymentServices exception occurred");
			return false;
		}
	}
/*	@Override
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
			for (PaymentPlanSnapshot assetSetSnapshot : assetSetSnapshotList) {
				if (assetSetSnapshot.is_immutable()) {
					postRequest.put("errorMsg", "还款计划不可变更");
					return false;
				}
			}

			// 校验完成填写post参数
			postRequest.putAll(parameters);
			postRequest.put("fn", "200001");
			postRequest.put("requestReason", RepaymentPlanModifyReason.REASON_8.getOrdinal() + "");

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/
}
