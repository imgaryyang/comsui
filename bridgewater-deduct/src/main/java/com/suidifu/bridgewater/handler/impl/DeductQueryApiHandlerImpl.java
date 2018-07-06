package com.suidifu.bridgewater.handler.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.bridgewater.fast.FastDeductApplication;
import com.suidifu.bridgewater.fast.FastDeductApplicationEnum;
import com.suidifu.giotto.handler.FastDataWithTemperatureHandler;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.StringUtils;
import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryModel;
import com.suidifu.bridgewater.api.model.BatchDeductStatusQueryResult;
import com.suidifu.bridgewater.api.model.DeductQueryResult;
import com.suidifu.bridgewater.api.model.OverdueDeductResultQueryModel;
import com.suidifu.bridgewater.handler.DeductQueryApiHandler;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.api.model.deduct.DeductInfo;
import com.zufangbao.sun.entity.contract.Contract;
import com.zufangbao.sun.handler.DeductApplicationCoreOperationHandler;
import com.zufangbao.sun.service.ContractService;
import com.zufangbao.sun.service.TransferApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.entity.AssetSet;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.handler.DeductPlanCoreOperationHandler;
import com.zufangbao.sun.yunxin.handler.RepaymentPlanHandler;
import com.zufangbao.sun.yunxin.service.RepaymentPlanService;


@Component("deductQueryApiHandler")
public class DeductQueryApiHandlerImpl implements DeductQueryApiHandler {

	@Autowired
	private DeductApplicationService deductApplicationService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private  RepaymentPlanHandler repaymentPlanHandler;
	
	@Autowired
	private  TransferApplicationService transferApplicationService;

	@Autowired
	private  DeductPlanCoreOperationHandler deductPlanCoreOperationHandler;
	
	@Autowired
	private DeductApplicationCoreOperationHandler deductApplicationCoreOperationHandler;
	
	@Autowired
	private RepaymentPlanService repaymentPlanService;

	@Autowired
	private FastDataWithTemperatureHandler fastDataWithTemperatureHandler;

	private static final int WHOLE_CONTRACT_QUERY = 1;
	private static final int SINGLE_DEDUCT = 0;
	private static final int DEDUCT_ID_QUERY = 1;
	private static final int REPAYMENT_CODE_QUERY =0;
	

	@Override
	public DeductQueryResult apideductQuery(
			OverdueDeductResultQueryModel queryModel, HttpServletRequest request) {

		// 0 查询单条deduct 1 查询整个合同下的扣款
		int strategy = calcDeudctApiQueryStrategy(queryModel);

		switch (strategy) {
		case SINGLE_DEDUCT:
			return singleDeductQuery(queryModel, request);
		case WHOLE_CONTRACT_QUERY:
			return wholeContractDeductQuery(queryModel,request);
		default:
			break;
		}

		return null;
	}
	
	private DeductQueryResult fillInfoAndCreateDeductQueryResult(String requestNo, String uniqueId, String contractNo,
			List<DeductInfo> deductInfos) {
		for (DeductInfo deductInfo : deductInfos) {
			List<String> repayScheduleNoList=getRepayScheduleNoListByRepaymentPlanNoList(deductInfo.getRepaymentCodes());
			deductInfo.setRepayScheduleNoList(repayScheduleNoList);
		}
		DeductQueryResult result = new DeductQueryResult(requestNo, uniqueId, contractNo, deductInfos);
		
		return result;

	}
	
	private List<String> getRepayScheduleNoListByRepaymentPlanNoList(List<String> repaymentPlanNoList){
		if(CollectionUtils.isEmpty(repaymentPlanNoList)){
			return Collections.emptyList();
		}
		List<AssetSet> assetSets=repaymentPlanService.getByRepaymentPlanNoList(repaymentPlanNoList);
		if(CollectionUtils.isEmpty(assetSets)){
			return Collections.emptyList();
		}
		List<String> result=assetSets.stream().map(AssetSet::getOuterRepaymentPlanNo).collect(Collectors.toList());
		return result;
	}
	
	private DeductQueryResult wholeContractDeductQuery(OverdueDeductResultQueryModel queryModel, HttpServletRequest request) {
		String contractUniqueId = queryModel.getUniqueId();
		String financialProductCode = queryModel.getFinancialProductCode();
		Contract contract = contractService.getContractByUniqueId(contractUniqueId);
		if(contract == null){
			throw new ApiException(ApiResponseCode.CONTRACT_NOT_EXIST);
		}
		List<DeductInfo> deductInfos  =new ArrayList<DeductInfo>();
		List<AssetSet> repaymentPlans = repaymentPlanService.getAllRepaymentPlanList(contract);
		for(AssetSet repaymentPlan:repaymentPlans ){
			List<DeductApplication> deductApplications = deductApplicationService.getDeductApplicationByRepaymentPlanCode(repaymentPlan.getAssetUuid());
			for(DeductApplication deductApplication :deductApplications){
				if(!StringUtils.isEmpty(financialProductCode) && !financialProductCode.equals(deductApplication.getFinancialProductCode())){
					throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
				}
				deductInfos.add(assembleDeductInfoApi(deductApplication));
			}
			
		}
		return fillInfoAndCreateDeductQueryResult(queryModel.getRequestNo(),queryModel.getUniqueId(),queryModel.getContractNo(),deductInfos);
		
	}

	private DeductQueryResult singleDeductQuery(
			OverdueDeductResultQueryModel queryModel, HttpServletRequest request) {
		FastDeductApplication fastDeductApplication = fastDataWithTemperatureHandler.getByKey(FastDeductApplicationEnum.DEDUCT_ID, queryModel.getDeductId(), FastDeductApplication.class);
		if(fastDeductApplication == null){
			throw new ApiException(ApiResponseCode.NOT_DEDUCT_ID);
		}
		String financialProductCode = queryModel.getFinancialProductCode();
		if(!StringUtils.isEmpty(financialProductCode) && !financialProductCode.equals(fastDeductApplication.getFinancialProductCode())){
			throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
		}
		return assembleQueryResultApi(queryModel,fastDeductApplication, request);

	}

	private DeductQueryResult assembleQueryResultApi(
			OverdueDeductResultQueryModel queryModel,
			FastDeductApplication fastDeductApplication, HttpServletRequest request) {

		String requestNo = queryModel.getRequestNo();
		
		List<DeductInfo> deductInfos = new ArrayList<DeductInfo>();
		deductInfos.add(assembleDeductInfoApiV(fastDeductApplication));

		return fillInfoAndCreateDeductQueryResult(requestNo,queryModel.getUniqueId(),queryModel.getContractNo(),deductInfos);
	}

	private DeductInfo assembleDeductInfoApi(
			DeductApplication deductApplication) {
		String deductId = deductApplication.getDeductId();
		
		String businessMessage = deductApplication.getExecutionRemark();	
		BigDecimal successTotalAmount = deductApplication.getActualDeductTotalAmount();
		DeductApplicationExecutionStatus executionStatus = deductApplication.getExecutionStatus();
		return createDeductInfoApi(deductId,deductApplication,successTotalAmount,executionStatus, businessMessage);
	}

	private DeductInfo assembleDeductInfoApiV(
			FastDeductApplication fastDeductApplication) {
		String deductId = fastDeductApplication.getDeductId();

		String businessMessage = fastDeductApplication.getExecutionRemark();
		BigDecimal successTotalAmount = fastDeductApplication.getActualDeductTotalAmount();
		DeductApplicationExecutionStatus executionStatus = DeductApplicationExecutionStatus.fromOrdinal(fastDeductApplication.getExecutionStatus());

		DeductInfo deductInfo = new DeductInfo();
		deductInfo.setDeductNo(deductId);
		deductInfo.setRepaymentCodes(fastDeductApplication.getRepaymentPlanCodeListJsonString());
		deductInfo.setPlanAmount(fastDeductApplication.getActualDeductTotalAmount());
		deductInfo.setBusinessResultMessage(businessMessage);
		deductInfo.setSuccessAmount(successTotalAmount);
		deductInfo.setExecutionStatus(executionStatus);
		if(executionStatus == DeductApplicationExecutionStatus.SUCCESS){
			deductInfo.setDeductSuccessTime(DateUtils.format(fastDeductApplication.getCompleteTime(),"yyyy-MM-dd HH:mm:ss"));
		}else{
			deductInfo.setDeductSuccessTime("");
		}
		return deductInfo;
	}

	private DeductInfo createDeductInfoApi(String deductId,
			DeductApplication deductApplication, BigDecimal successTotalAmount,
			DeductApplicationExecutionStatus executionStatus, String businessMessage) {

		
		return new DeductInfo(deductId,deductApplication,successTotalAmount,executionStatus, businessMessage);
	}


	private int calcDeudctApiQueryStrategy(
			OverdueDeductResultQueryModel queryModel) {

		if (!StringUtils.isEmpty(queryModel.getDeductId())) {
			return SINGLE_DEDUCT;
		}
		return WHOLE_CONTRACT_QUERY;
	} 
	    
	@Override
	public List<BatchDeductStatusQueryResult> apiBatchDeductStatusQuery(
			BatchDeductStatusQueryModel queryModel, HttpServletRequest request) {
		//判断查询策略
		int strategy = batchDeductStatusApiQueryStrategy(queryModel);
		
		List<BatchDeductStatusQueryResult> results = null; 
		switch (strategy) {
			case DEDUCT_ID_QUERY:
				results = deductStatusQueryByDeductIdList(queryModel);
				break;
			case REPAYMENT_CODE_QUERY:
				results = deductStatusQueryByRepaymentPlanCodeList(queryModel);
				break;
			default:
				results = Collections.emptyList();
				break;
			}
		fillInfoIntoBatchDeductStatusQueryResultList(results);
		return results;
	}
	
	
	private void fillInfoIntoBatchDeductStatusQueryResultList(List<BatchDeductStatusQueryResult> results) {
		for (BatchDeductStatusQueryResult result : results) {
			List<String> repayScheduleNoList=getRepayScheduleNoListByRepaymentPlanNoList(result.getRepaymentPlanCodeList());
			result.setRepayScheduleNoList(repayScheduleNoList);
		}
	}

	private List<BatchDeductStatusQueryResult> deductStatusQueryByDeductIdList(BatchDeductStatusQueryModel queryModel){
		List<DeductApplication> deductApplicationList = deductApplicationService.getDeductApplicationListByDeductIdList(queryModel.getDeductIdListJson());
		//key：deductApplicationUuid,value:扣款计划列表
		Map<String,List<DeductPlan>> deductPlanMap= deductPlanCoreOperationHandler.getDeductPlanByDeductApplicaiton(deductApplicationList);
		String financialProductCode = queryModel.getFinancialProductCode();
		List<BatchDeductStatusQueryResult> batchDeductStatusQueryResultList = getBatchDeductStatusQueryResultList(
				deductApplicationList, deductPlanMap, financialProductCode);
		return batchDeductStatusQueryResultList;
	}

	private List<BatchDeductStatusQueryResult> getBatchDeductStatusQueryResultList(
			List<DeductApplication> deductApplicationList, Map<String, List<DeductPlan>> deductPlanMap, String financialProductCode) {
		 List<BatchDeductStatusQueryResult>  showResult = new  ArrayList<BatchDeductStatusQueryResult>();
		 for(DeductApplication deductApplication:deductApplicationList){
			 if(!StringUtils.isEmpty(financialProductCode) && !financialProductCode.equals(deductApplication.getFinancialProductCode())){
				 throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
			 }
			BatchDeductStatusQueryResult  batchDeductStatusQueryResult = create_response_datch_deduct_status_model(deductPlanMap, deductApplication);
			showResult.add(batchDeductStatusQueryResult);
		 }
		 return showResult;
	}

	private BatchDeductStatusQueryResult create_response_datch_deduct_status_model(Map<String, List<DeductPlan>> deductPlanMap,
			DeductApplication deductApplication) {
		//取得最新的扣款计划，并获取流水号
		String  paymentFlowNo = getLatestPaymentFlowNo(deductApplication,deductPlanMap);
		return new BatchDeductStatusQueryResult(deductApplication,paymentFlowNo);
	}
	
	private String getLatestPaymentFlowNo(DeductApplication deductApplication, Map<String, List<DeductPlan>> deductPlanMap) {
		
		List<DeductPlan>  deductPlanList = deductPlanMap.get(deductApplication.getDeductApplicationUuid()); 
		if(CollectionUtils.isNotEmpty(deductPlanList)){
		    return deductPlanList.stream().sorted(Comparator.comparing(DeductPlan::getCreateTime)).collect(Collectors.toList()).get(0).getTradeUuid();
		}
		return "";
	}
	
	
	private List<BatchDeductStatusQueryResult> deductStatusQueryByRepaymentPlanCodeList(BatchDeductStatusQueryModel queryModel){
		//通过repaymentPlanCode 获取对应的DeductApplication
		Map<String,List<DeductApplication>> deductApplicationMap = deductApplicationCoreOperationHandler.getDeductApplicationDetailInRepaymentPlanCodeList(queryModel.getRepaymentPlanCodeListJson());
		
		if(deductApplicationMap == null || deductApplicationMap.isEmpty()){
			return Collections.emptyList();
		}
		
		//根据Map<String,List<DeductApplication>> 获取所有 DeductApplication
		List<DeductApplication> deductApplicationAll = getDeductApplication(deductApplicationMap);
		
		//key：deductApplicationUuid,value:扣款计划列表
		Map<String,List<DeductPlan>> deductPlanMap = deductPlanCoreOperationHandler.getDeductPlanByDeductApplicaiton(deductApplicationAll);
		String financialProductCode = queryModel.getFinancialProductCode();
		List<BatchDeductStatusQueryResult> results = getBatchDeductStatusQueryResult(deductApplicationMap, deductPlanMap, financialProductCode);
		return results;
	}

	private List<BatchDeductStatusQueryResult> getBatchDeductStatusQueryResult(Map<String, List<DeductApplication>> deductApplicationMap, Map<String, List<DeductPlan>> deductPlanMap, String financialProductCode) {
		if(deductApplicationMap == null){
			return Collections.emptyList();
		}
		
		List<BatchDeductStatusQueryResult> results = new ArrayList<BatchDeductStatusQueryResult>();
		for(String repaymentPlanCode:deductApplicationMap.keySet()){
			List<DeductApplication> applications = deductApplicationMap.get(repaymentPlanCode);
			//把DeductApplication转成BatchDeductStatusQueryResult
			for(DeductApplication deuctApp:applications)
			{   
				if(deuctApp == null){
					continue;
				}

				if(!StringUtils.isEmpty(financialProductCode) && !financialProductCode.equals(deuctApp.getFinancialProductCode())){
					throw new ApiException(ApiResponseCode.FINANCIAL_PRODUCT_CODE_ERROR);
				}
			    String  paymentFlowNo = getLatestPaymentFlowNo(deuctApp,deductPlanMap);
			    results.add(new BatchDeductStatusQueryResult(deuctApp,paymentFlowNo,repaymentPlanCode));
			}
		}
		return results;
	}

	private List<DeductApplication> getDeductApplication(Map<String, List<DeductApplication>> deductApplicationMap) {
		List<DeductApplication> deductApplicationAll = new ArrayList<DeductApplication>();
		for(String repaymentCode :deductApplicationMap.keySet()){
			deductApplicationAll.addAll(deductApplicationMap.get(repaymentCode));
		}
		return deductApplicationAll;
	}
	
	public int batchDeductStatusApiQueryStrategy(BatchDeductStatusQueryModel queryModel){
		
        if(!StringUtils.isEmpty(queryModel.getDeductIdList())){
        	return DEDUCT_ID_QUERY;
        }
        return REPAYMENT_CODE_QUERY;
	}

}
