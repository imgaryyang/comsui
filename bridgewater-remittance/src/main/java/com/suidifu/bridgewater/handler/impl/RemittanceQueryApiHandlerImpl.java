package com.suidifu.bridgewater.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.api.model.RemittanceQueryResult;
import com.suidifu.bridgewater.api.model.RemittanceResultBatchPackModel;
import com.suidifu.bridgewater.api.model.RemittanceResultBatchQueryModel;
import com.suidifu.bridgewater.api.model.RemittanceResultDetail;
import com.suidifu.bridgewater.fast.FastRemittanceApplication;
import com.suidifu.bridgewater.fast.FastRemittanceApplicationEnum;
import com.suidifu.bridgewater.fast.FastRemittancePlan;
import com.suidifu.bridgewater.fast.FastRemittancePlanEnum;
import com.suidifu.bridgewater.handler.IRemittanceQueryApiHandler;
import com.suidifu.giotto.handler.FastDataWithTemperatureHandler;
import com.suidifu.giotto.service.relation.RemittanceRelation;
import com.suidifu.giotto.service.relation.RemittanceRelationCacheService;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("RemittanceQueryApiHandler")
public class RemittanceQueryApiHandlerImpl implements
		IRemittanceQueryApiHandler {

	@Autowired
	FastDataWithTemperatureHandler fastDataWithTemperatureHandler;
	
	@Autowired
	private RemittanceRelationCacheService remittanceRelationCacheService;
	

	@Override
	public List<RemittanceQueryResult> apiRemittanceResultBatchQuery(RemittanceResultBatchPackModel batchPackModel) {
		//查询放款申请表列表
		//根据放款申请单的uuId查询到相应的还款计划拼接成RemittanceQueryResult的列表
		List<RemittanceQueryResult> remittanceQueryResultList = new ArrayList<RemittanceQueryResult>();
		List<RemittanceResultBatchQueryModel> remittanceResultBatchQueryModelList = batchPackModel.getRemittanceResultBatchQueryModelList();
		List<String> oriRequestNoArray = new ArrayList<String>();
		List<String> remittanceIdArray = new ArrayList<String>();
		List<String> remittanceContractUniqueIds = new ArrayList<>();
		List<String> batchQueryModelUniqueIds = new ArrayList<>();
		Map<String, String> remittanceIdAndOriRequestNo = new HashMap<>();
		for (RemittanceResultBatchQueryModel remittanceResultBatchQueryModel:
				remittanceResultBatchQueryModelList) {

			if (StringUtils.isNotEmpty(remittanceResultBatchQueryModel.getOriRequestNo())) {
				oriRequestNoArray.add(remittanceResultBatchQueryModel.getOriRequestNo());
			}
			if (StringUtils.isNotEmpty(remittanceResultBatchQueryModel.getRemittanceId())) {
				remittanceIdArray.add(remittanceResultBatchQueryModel.getRemittanceId());
				remittanceIdAndOriRequestNo.put(remittanceResultBatchQueryModel.getRemittanceId(),remittanceResultBatchQueryModel.getOriRequestNo());
			}
			batchQueryModelUniqueIds.add(remittanceResultBatchQueryModel.getUniqueId());
		}

//		List<RemittanceApplication> remittanceApplications = new ArrayList<>();
//		if (CollectionUtils.isNotEmpty(remittanceIdArray)){
//			remittanceApplications = iRemittanceApplicationService.getRemittancePlanListByRemittanceIdArray(remittanceIdArray);
//		}else {
//			remittanceApplications = iRemittanceApplicationService.getRemittanceApplicationListByRequestNoArray(oriRequestNoArray);
//		}
		List<FastRemittanceApplication> remittanceApplications = new ArrayList<FastRemittanceApplication>();
		if (CollectionUtils.isNotEmpty(remittanceIdArray)){
			for (String remittanceId : remittanceIdArray) {
				FastRemittanceApplication application = fastDataWithTemperatureHandler.getByKey(FastRemittanceApplicationEnum.REMITTANCE_ID, remittanceId,
						FastRemittanceApplication.class);
				remittanceApplications.add(application);
			}
		}else {
			for (String oriRequestNo : oriRequestNoArray) {
				FastRemittanceApplication application = fastDataWithTemperatureHandler.getByKey(FastRemittanceApplicationEnum.REQUEST_NO, oriRequestNo,
						FastRemittanceApplication.class);
				remittanceApplications.add(application);
			}
		}
		
		
		




		if(CollectionUtils.isEmpty(remittanceApplications)) {
			throw  new ApiException(ApiResponseCode.NOT_EXIST_REQUEST_NO);
		}
		
		for (FastRemittanceApplication remittanceApplication :
			 		remittanceApplications) {
			if(null == remittanceApplication){
				throw  new ApiException(ApiResponseCode.NOT_EXIST_REQUEST_NO);
			}
			if (remittanceIdAndOriRequestNo.containsKey(remittanceApplication.getRemittanceId())){
				String requestNo = remittanceIdAndOriRequestNo.get(remittanceApplication.getRemittanceId());
				if (StringUtils.isNotEmpty(requestNo) && !requestNo.equals(remittanceApplication.getRequestNo())){
					throw  new ApiException(ApiResponseCode.NOT_CORRESPONDING_REMTTANCEID_REQUESTNO);
				}
			}
			remittanceContractUniqueIds.add(remittanceApplication.getContractUniqueId());

		}
		
		//信托计划与贷款合同校验
		if(StringUtils.isNotEmpty(batchPackModel.getProductCode())){
		List<String> uniqueIdNotMatch = batchQueryModelUniqueIds.stream().filter(p-> !remittanceContractUniqueIds.contains(p)).collect(Collectors.toList());
		if(!remittanceApplications.stream().allMatch(p->p.getFinancialProductCode().equals(batchPackModel.getProductCode()))) {
			
			throw  new ApiException(ApiResponseCode.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT,"不匹配的unique_id:["+JsonUtils.toJsonString(batchQueryModelUniqueIds)+"]");
		}else if (CollectionUtils.isNotEmpty(uniqueIdNotMatch)) {
					
			throw  new ApiException(ApiResponseCode.CONTRACT_NOT_EQUAL_FINANCIAL_CONTRACT,"不匹配的unique_id:["+JsonUtils.toJsonString(uniqueIdNotMatch)+"]");
		}
		}
		
		//不包含的情况
		if(!remittanceContractUniqueIds.containsAll(batchQueryModelUniqueIds)) {
			throw  new ApiException(ApiResponseCode.NOT_CORRESPONDING_CONTRACT_UNIQUEID);
		}
		for (FastRemittanceApplication remittanceApplication :
				remittanceApplications) {

			//List<RemittancePlan> remittancePlanList = iRemittancePlanService.getRemittancePlanListBy(remittanceApplication.getRemittanceApplicationUuid());

			List<FastRemittancePlan> remittancePlanList = getAllFastRemittancePlans(remittanceApplication);
			
			RemittanceQueryResult remittanceQueryResult = assembleTheQueryResultModel(remittanceApplication, remittancePlanList);
			if(null != remittanceQueryResult) {
				remittanceQueryResultList.add(remittanceQueryResult);
			}
		}

		return remittanceQueryResultList;
	}

	private List<FastRemittancePlan> getAllFastRemittancePlans(FastRemittanceApplication remittanceApplication) {
		List<String> remittanPlanUuidList = remittanceRelationCacheService.get(RemittanceRelation.RA_RP, remittanceApplication.getRemittanceApplicationUuid());
		List<FastRemittancePlan> fastRemittancePlans = new ArrayList<>();
		for (String uuid : remittanPlanUuidList) {
			FastRemittancePlan fastRemittancePlan = fastDataWithTemperatureHandler.getByKey(FastRemittancePlanEnum.REMITTANCE_PLAN_UUID, uuid, FastRemittancePlan
					.class);
			if (null == fastRemittancePlan) {
				throw new RuntimeException("找不到 fastRemittancePlan");
			}
			fastRemittancePlans.add(fastRemittancePlan);
		}
		return fastRemittancePlans;
	}

	private RemittanceQueryResult assembleTheQueryResultModel(FastRemittanceApplication remittanceApplication, List<FastRemittancePlan> remittancePlanList) {
		
		RemittanceQueryResult remittanceQueryResult = new RemittanceQueryResult();
		
		remittanceQueryResult.setUniqueId(remittanceApplication.getContractUniqueId());
		remittanceQueryResult.setOriRequestNo(remittanceApplication.getRequestNo());
		remittanceQueryResult.setRemittanceId(remittanceApplication.getRemittanceId());
		remittanceQueryResult.setContractNo(remittanceApplication.getContractNo());
		//remittanceQueryResult.setExecutionStatus(remittanceApplication.getExecutionStatus().ordinal());
		remittanceQueryResult.setExecutionStatus(remittanceApplication.getExecutionStatus());
		
		List<RemittanceResultDetail> remittanceResultDetails = new ArrayList<RemittanceResultDetail>();

		for (FastRemittancePlan plan : remittancePlanList) {
			
			String actExcutedTime = StringUtils.EMPTY;
			try {
				actExcutedTime = DateUtils.format(plan.getCompletePaymentDate(), "yyyy-MM-dd HH:mm:ss");
			} catch (Exception e) {
			}
			
			RemittanceResultDetail result = new RemittanceResultDetail(plan.getBusinessRecordNo(), plan.getCpBankCardNo(), plan.getCpBankAccountHolder(), plan.getActualTotalAmount(), plan.getExecutionStatus(), plan.getTransactionSerialNo(), actExcutedTime);

			remittanceResultDetails.add(result);
		}
		remittanceQueryResult.setRemittanceResultDetails(remittanceResultDetails);
		return remittanceQueryResult;
		
	}

}
