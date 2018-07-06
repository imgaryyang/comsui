package com.zufangbao.earth.yunxin.api.handler.impl;

import static com.zufangbao.gluon.api.earth.v3.model.ApiMessage.CONTRACT_NOT_EXIST;

import com.alibaba.fastjson.annotation.JSONField;
import com.zufangbao.earth.yunxin.api.handler.RepaymentPlanApiHandler;
import com.zufangbao.earth.yunxin.api.model.RepurchaseDetail;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanBatchQueryModel;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanDetail;
import com.zufangbao.earth.yunxin.api.model.query.RepaymentPlanQueryModel;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.repurchase.RepurchaseDoc;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.FinancialContractService;
import com.zufangbao.sun.service.RepurchaseService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.AssetSetActiveStatus;
import com.zufangbao.sun.yunxin.entity.PlanType;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanExtraChargeHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraChargeService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanExtraDataService;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;
import com.zufangbao.wellsfargo.yunxin.handler.ContractApiHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("RepaymentPlanApiHandler")
public class RepaymentPlanApiHandlerImpl implements RepaymentPlanApiHandler {

	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private ContractApiHandler contractApiHandler;

	@Autowired
	private RepaymentPlanExtraChargeService repaymentPlanExtraChargeService;

	@Autowired
	private RepaymentPlanExtraChargeHandler repaymentPlanExtraChargeHandler;

	@Autowired
	private FinancialContractService financialContractService;

	@Autowired
	private RepurchaseService repurchaseService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private RepaymentPlanExtraDataService repaymentPlanExtraDataService;

	@Override
	public List<RepaymentPlanDetail> queryRepaymentPlanDetail(RepaymentPlanQueryModel queryModel) {

		Contract contract = contractApiHandler.getContractBy(queryModel.getUniqueId(), queryModel.getContractNo());
		/*
            * 请求参数新增“信托产品代码”选填字段，当该字段为非空时，做如下校验：
            * a.校验信托产品代码是否正确存在，若错误或不存在，返回失败+失败原因；
            * b.校验贷款合同唯一编号/贷款合同与产品代码是否存在映射关系，若不存在映射关系，返回失败+失败原因
            */
        String financialContractNo = queryModel.getFinancialContractNo();
        if (com.zufangbao.sun.utils.StringUtils.isNotEmpty(financialContractNo)) {
            contractApiHandler.checkAndReturnFinancialContract(financialContractNo, contract);
        }
        List<AssetSet> assetSetList = repaymentPlanService.get_all_open_or_frozen_repayment_plan_list(contract);
		return convertRepaymentPlanDetails(assetSetList,false);
	}

	@Override
	public List<RepaymentPlanDetail> convertRepaymentPlanDetails(List<AssetSet> assetSetList,boolean isBatchQuery) {
		List<RepaymentPlanDetail> repaymentPlanDetails = new ArrayList<RepaymentPlanDetail>();
		for (AssetSet repaymentPlan : assetSetList) {
			String assetSetUuid = repaymentPlan.getAssetUuid();
			Map<String, BigDecimal> amountMap = repaymentPlanExtraChargeService.getAssetSetExtraChargeModels(assetSetUuid);
			BigDecimal totalOverDueFee = repaymentPlanExtraChargeService.getTotalOverDueFee(amountMap, assetSetUuid);

//			RepaymentPlanExtraData repaymentPlanExtraData = repaymentPlanExtraDataService.getRepaymentPlanExtraDataByAssetUuid(repaymentPlan.getAssetUuid());
//			String repayScheduleNo=repaymentPlan.getRepayScheduleNo();
//			if(null != repaymentPlanExtraData && null != repaymentPlanExtraData.getRepayScheduleNo()) {
//				repaymentPlan.setRepayScheduleNo(repaymentPlanExtraData.getRepayScheduleNo());
//			}

			if (repaymentPlan.getActiveStatus() == AssetSetActiveStatus.FROZEN) {
				if (repaymentPlan.getPlanType() == PlanType.PREPAYMENT) {
					repaymentPlanDetails.add(getRepaymentPlanDetail(isBatchQuery, repaymentPlan, amountMap, totalOverDueFee));
				}
			} else {
				repaymentPlanDetails.add(getRepaymentPlanDetail(isBatchQuery, repaymentPlan, amountMap, totalOverDueFee));
			}
//			// TODO 防止事务导致的提交
//			repaymentPlan.setRepayScheduleNo(repayScheduleNo);
		}
		return repaymentPlanDetails;
	}

	private RepaymentPlanDetail getRepaymentPlanDetail(boolean isBatchQuery, AssetSet repaymentPlan,
			Map<String, BigDecimal> amountMap, BigDecimal totalOverDueFee) {
		RepaymentPlanDetail repaymentPlanDetail = new RepaymentPlanDetail(repaymentPlan, amountMap, totalOverDueFee);
		// TODO 批量查询时不返回期数,需要返回时将下面一段代码删除即可
		// start
		if (isBatchQuery) {
			repaymentPlanDetail.setCurrentPeriod(null);
		}
		// end
		return repaymentPlanDetail;
	}

	@Value("#{config['yx.batchQuery.size']}")
	@JSONField(serialize = false)
	private String batchQuerySize = "";

	@Override
	public List<Map<String, Object>> queryRepaymentPlanDetail(
			RepaymentPlanBatchQueryModel queryModel) {
		String productCode = queryModel.getProductCode();
		Date planRepaymentDate = queryModel.getDateTypePlanRepaymentDate();
		//信托产品代码存在性校验
		FinancialContract financialContract = financialContractService.getUniqueFinancialContractBy(productCode);
		if(financialContract == null) {
			throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
		}
		String fcUuid = financialContract.getFinancialContractUuid();
		//构建响应结果
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

		//获取贷款合同唯一编号List
		List<String> uniqueIdsList = queryModel.getUniqueIdsList(batchQuerySize);
		for (String uniqueId : uniqueIdsList) {
			//根据每个贷款合同唯一编号获取对应贷款合同
			Contract contract = contractService.getContractByUniqueId(uniqueId);
			if (contract == null){
				continue;
			}

			// 校验信托产品代码和贷款合同唯一编号是否有对应关系
			if (!fcUuid.equals(contract.getFinancialContractUuid())) {
				Map<String, Object> repaymentPlansMap = new HashMap<String, Object>();
				repaymentPlansMap.put("uniqueId", uniqueId);
				String failMsg = "贷款合同唯一编号[" + uniqueId + "]，该贷款合同唯一编号与信托产品代码无对应关系";
				repaymentPlansMap.put("failMsg", failMsg);
				list.add(repaymentPlansMap);
				continue;
			}

			String contractUuid = contract.getUuid();
			List<AssetSet> repaymentPlanList = repaymentPlanService.getEffectiveRepaymentPlansBy(contractUuid, financialContract.getFinancialContractUuid(), planRepaymentDate);
			if(CollectionUtils.isEmpty(repaymentPlanList)) {
				continue;
			}

			List<RepaymentPlanDetail> repaymentPlanDetails = convertRepaymentPlanDetails(repaymentPlanList,true);

			Map<String, Object> repaymentPlansMap = new HashMap<String, Object>();

			repaymentPlansMap.put("uniqueId", uniqueId);
			repaymentPlansMap.put("repaymentPlanDetails", repaymentPlanDetails);
			RepurchaseDoc repurchaseDoc = repurchaseService.getRepurchaseDocBy(contract.getId());
			if(repurchaseDoc!=null){
				repaymentPlansMap.put("repurchaseDoc",new RepurchaseDetail(repurchaseDoc));
			}
			list.add(repaymentPlansMap);
		}
		return list;
	}

	@Override
	public RepurchaseDoc queryRepurchaseDoc(RepaymentPlanQueryModel queryModel) {
		Contract contract = contractApiHandler.getContractBy(queryModel.getUniqueId(), queryModel.getContractNo());
		if (contract == null){
            throw new ApiException(CONTRACT_NOT_EXIST);
        }
		return repurchaseService.getRepurchaseDocBy(contract.getId());
	}

}
