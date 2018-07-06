package com.suidifu.bridgewater.handler.impl;

import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.bridgewater.api.entity.BusinessErrorReason;
import com.suidifu.bridgewater.api.model.DeductErrorCodeMapSpec;
import com.suidifu.bridgewater.api.model.ReDeductDataPackage;
import com.suidifu.bridgewater.handler.DeductPlanBusinessHandler;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusDetail;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.sun.api.model.deduct.DeductApplicationExecutionStatus;
import com.zufangbao.sun.entity.financial.BusinessType;
import com.zufangbao.sun.entity.financial.PaymentChannelSummaryInfo;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductApplicationService;
import com.zufangbao.sun.yunxin.api.deduct.service.DeductPlanService;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductApplication;
import com.zufangbao.sun.yunxin.entity.api.deduct.DeductPlan;
import com.zufangbao.sun.yunxin.handler.PaymentChannelInformationHandler;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("DeductPlanBusinessHandler")
public class DeductPlanBusinessHandlerImpl implements DeductPlanBusinessHandler {

	@Autowired
	private DeductPlanService deductPlanService;

	@Autowired
	private GenericDaoSupport genericDaoSupport;
	
	@Autowired
	private PaymentChannelInformationHandler paymentChannelInformationHandler;
	
	@Autowired
	private DeductApplicationService deductApplicationService;

	@Override
	public void updateDeductPlanStatusByQueryResult(String deductApplicationUuid,List<QueryStatusResult> queryStatusResults) {

		Map<String, QueryStatusResult> queryStatusResultsMap = generateQueryStatusRsultMap(queryStatusResults);
		
		List<DeductPlan> deductPlans = deductPlanService.getDeductPlanByDeductApplicationUuid(deductApplicationUuid);
		
		//更新deductPlan状态
		updateDeductPlanStatusInProcessing(queryStatusResultsMap, deductPlans);
		
		
	}


	private Map<String, QueryStatusResult> generateQueryStatusRsultMap(List<QueryStatusResult> queryStatusResults) {
		Map<String, QueryStatusResult> queryStatusResultsMap = new HashMap<String, QueryStatusResult>();
		for (QueryStatusResult queryStatusResult : queryStatusResults) {
			if (StringUtils.isEmpty(queryStatusResult.getTransactionUuid())) {
				continue;
			}
			queryStatusResultsMap.put(queryStatusResult.getTransactionUuid(), queryStatusResult);
		}
		return queryStatusResultsMap;
	}


	private void updateDeductPlanStatusInProcessing(Map<String, QueryStatusResult> queryStatusResultsMap,
			List<DeductPlan> deductPlans) {
		for (DeductPlan deductPlan : deductPlans) {
			if(deductPlan.getExecutionStatus() == DeductApplicationExecutionStatus.PROCESSING){
				if (queryStatusResultsMap.containsKey(deductPlan.getDeductPlanUuid())) {
					updateDeductInfoWhenDeductPlanFound(deductPlan.getDeductPlanUuid(),
							queryStatusResultsMap.get(deductPlan.getDeductPlanUuid()));
				} else {
					updateDeductInfoWhenDeductPlanNotFound(deductPlan.getDeductPlanUuid());
				}
			}
		}
	}

	/**
	 * 
	 * @param deductApplicationUuid
	 * @param queryStatusResultsMap
	 * @param deductPlans
	 * @return
	 * 组装重发数据包：当指定通道，对端有未接受的扣款请求都不重发
	 */
	private ReDeductDataPackage castReDeductDataPackage(String deductApplicationUuid, Map<String, QueryStatusResult> queryStatusResultsMap, List<DeductPlan> deductPlans) {
		
		
		ReDeductDataPackage failedDeductDataPackage = new ReDeductDataPackage(false,null); 
		DeductApplication deductApplication = deductApplicationService.getDeductApplicationByDeductApplicationUuid(deductApplicationUuid);
		//若指定通道补充法 || 支付单未全为失败（还有处理中，对端通信失败）
		if (hasPointGateway(deductApplication) || !isAllFailAndSameSize(queryStatusResultsMap,deductPlans)) return failedDeductDataPackage;
		//余额不足||银行系统维护中||扣款银行卡卡号错误
        if(!isChangePaymentGateway(queryStatusResultsMap,deductPlans)){
			return failedDeductDataPackage;
		}
		//生成重发单
		List<String> usedPaymentChannelUuids = getUsedPaymentChannelUuids(deductPlans);
		List<PaymentChannelSummaryInfo> paymentChannelInformations = getAvailablePaymentChannel(deductPlans.get(0));

		ReDeductDataPackage reDecutDataPackage= castNeedReDedcutPlan(deductPlans, usedPaymentChannelUuids, paymentChannelInformations);
		if(reDecutDataPackage==null) return failedDeductDataPackage;
		return reDecutDataPackage;
		
		
	}

	private boolean hasPointGateway(DeductApplication deductApplication) {
		if(!StringUtils.isEmpty(deductApplication.getGateway())){
			return true;
		}
		return false;
	}

	private ReDeductDataPackage castNeedReDedcutPlan(List<DeductPlan> deductPlans, List<String> usedPaymentChannelUuids,
			List<PaymentChannelSummaryInfo> paymentChannelInformations) {
		for(PaymentChannelSummaryInfo paymentChannelInformation:paymentChannelInformations){
			if(usedPaymentChannelUuids.contains(paymentChannelInformation.getChannelServiceUuid())){
				continue;
			}
			else{
				return new ReDeductDataPackage(true, newReDeductPlan(deductPlans.get(0), paymentChannelInformation));
			}
		}
		return null;
	}
	
	private DeductPlan newReDeductPlan(DeductPlan reDeductPlan, PaymentChannelSummaryInfo paymentChannelInformation) {
		DeductPlan  deductPlan = new DeductPlan(reDeductPlan);
		deductPlan.setPaymentChannelUuid(paymentChannelInformation.getChannelServiceUuid());
		deductPlan.setPgClearingAccount(paymentChannelInformation.getClearingNo());
		deductPlan.setPaymentGateway(PaymentInstitutionName.fromValue(paymentChannelInformation.getPaymentGateway()));
		deductPlanService.save(deductPlan);
		return deductPlan;
	}
	
	
	private List<PaymentChannelSummaryInfo> getAvailablePaymentChannel(DeductPlan deductPlan) {
		
		return paymentChannelInformationHandler.getPaymentChannelServiceUuidsBy(deductPlan.getFinancialContractUuid(),  BusinessType.SELF, com.zufangbao.sun.yunxin.entity.remittance.AccountSide.DEBIT,deductPlan.getCpBankCode(), null);
	}

	private List<String> getUsedPaymentChannelUuids(List<DeductPlan> allFaildeductPlans) {
		List<String> usedPaymentChannelUuids = allFaildeductPlans.stream().map(pc -> pc.getPaymentChannelUuid()).collect(Collectors.toList());
		return usedPaymentChannelUuids;
	}


	
	boolean isAllFailAndSameSize(Map<String, QueryStatusResult> queryStatusResultsMap, List<DeductPlan> deductPlans){
		boolean isAllFail = true;
		if(queryStatusResultsMap.size() != deductPlans.size() ){
			return false;
		}
		for(String key:queryStatusResultsMap.keySet()){
			QueryStatusResult result = queryStatusResultsMap.get(key);
			if(result.convertToDeductAppliationExecutionStatus() != 3){
				isAllFail = false;
			}
		}
		return isAllFail;
	}


	private void updateDeductInfoWhenDeductPlanFound(String deductPlanUuid, QueryStatusResult queryStatusResult) {
		if (StringUtils.isEmpty(deductPlanUuid) || queryStatusResult == null) {
			return;
		}

		Integer executionStatus = queryStatusResult.convertToDeductAppliationExecutionStatus();
		if (queryStatusResult.isFinish() && executionStatus != null) {
			DeductPlan  deductPlan = deductPlanService.getDeductPlanByUUid(deductPlanUuid);
			deductPlan.setCompletePaymentDate(queryStatusResult.getBusinessSuccessTime());
			if(executionStatus == 2){
			deductPlan.setActualTotalAmount(queryStatusResult.getTransactionAmount());
			}
			deductPlan.setExecutionStatus(DeductApplicationExecutionStatus.fromOrdinal(executionStatus));
			//deductPlan.setPgAccount(queryStatusResult.getChannelAccountNo());
			deductPlan.setExecutionRemark(queryStatusResult.getBusinessResultMsg());
			deductPlan.setTransactionSerialNo(queryStatusResult.getChannelSequenceNo());
			deductPlan.setLastModifiedTime(new Date());
			if(executionStatus != 1 ){
				deductPlan.setCompleteTime(new Date());
			}
			deductPlan.setDeductPlanUuid(deductPlanUuid);
			deductPlan.setTradeUuid(queryStatusResult.getTradeUuid());
			deductPlan.fillDeductCashIdentity();
			this.genericDaoSupport.update(deductPlan);
			
		}
	}

	private void updateDeductInfoWhenDeductPlanNotFound(String deductPlanUuid) {
		if (StringUtils.isEmpty(deductPlanUuid)) {
			return;
		}
	
		DeductPlan  deductPlan = deductPlanService.getDeductPlanByUUid(deductPlanUuid);
		deductPlan.setExecutionStatus(DeductApplicationExecutionStatus.FAIL);
		deductPlan.setExecutionRemark("无法查询到该交易！");
		deductPlan.setLastModifiedTime(new Date());
		deductPlan.setCompleteTime(new Date());
		deductPlan.setDeductPlanUuid(deductPlanUuid);
		deductPlan.fillDeductCashIdentity();
		this.genericDaoSupport.update(deductPlan);
	}


	@Override
	public ReDeductDataPackage castRedeductPackage(String deductApplicationUuid,
			List<QueryStatusResult> queryStatusResults) {
		
		Map<String, QueryStatusResult> queryStatusResultsMap = generateQueryStatusRsultMap(queryStatusResults);
		List<DeductPlan> deductPlans = deductPlanService.getDeductPlanByDeductApplicationUuid(deductApplicationUuid);
		//组装重发数据包
		return castReDeductDataPackage(deductApplicationUuid,queryStatusResultsMap,deductPlans);
	}

	private boolean isChangePaymentGateway(Map<String, QueryStatusResult> queryStatusResultsMap,List<DeductPlan> deductPlans) {
		for (DeductPlan deductPlan : deductPlans) {
			QueryStatusResult statusResult = queryStatusResultsMap.get(deductPlan.getDeductPlanUuid());
			if(null == statusResult){
				continue;
			}
			BusinessErrorReason businessErrorReason = DeductErrorCodeMapSpec.getBusinessErrorReason(deductPlan.getPaymentGateway(),statusResult.getBusinessResultCode());
			if(BusinessErrorReason.CARDNOERROR.equals(businessErrorReason) || BusinessErrorReason.INSUFFICIENTBALANCE.equals(businessErrorReason) || BusinessErrorReason.SYSTEMMAINTAINING.equals(businessErrorReason)){
				return false;
			}
		}
		return true;
	}


	@Override
	public void updateDeductPlanStatusByQueryResultV2(
			String deductApplicationUuid,
			QueryStatusResult queryStatusResult) {

		Map<String, QueryStatusDetail> queryStatusDetailsMap = queryStatusResult.getQueryStatusDetailMap();

		List<DeductPlan> deductPlans = deductPlanService.getDeductPlanByDeductApplicationUuid(deductApplicationUuid);
		
		for (DeductPlan deductPlan : deductPlans) {
			if(deductPlan.getExecutionStatus() == DeductApplicationExecutionStatus.PROCESSING){
				if (queryStatusDetailsMap.containsKey(deductPlan.getTradeScheduleSlotUuid())) {
					QueryStatusResult qsr = new QueryStatusResult(queryStatusDetailsMap.get(deductPlan.getTradeScheduleSlotUuid()));
					if (!qsr.isFinish() ){
						continue;
					}
					updateDeductInfoWhenDeductPlanFound(deductPlan.getDeductPlanUuid(),qsr);
				}
//				else {
//					updateDeductInfoWhenDeductPlanNotFound(deductPlan.getDeductPlanUuid());
//				}
			}
		}
		
	}

	@Override
	public void updateDeductPlanByRepaymentOrder(DeductApplication deductApplication) {
		if (deductApplication == null){
			return;
		}
		List<DeductPlan> deductPlans = deductPlanService.getDeductPlanByDeductApplicationUuid(deductApplication.getDeductApplicationUuid());
		List<DeductPlan> successdeductPlans = deductPlans.stream().filter(d -> d.getExecutionStatus() == DeductApplicationExecutionStatus.SUCCESS).collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(successdeductPlans) && successdeductPlans.size() == 1 &&
				deductApplication.getActualDeductTotalAmount().compareTo(successdeductPlans.get(0).getPlannedTotalAmount())==0){
			String queryString  = " UPDATE DeductPlan set executionStatus =:abandon, lastModifiedTime =:lastModifiedTime,completeTime =:completeTime where deductApplicationUuid =:deductApplicationUuid and executionStatus =:processing";
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("abandon", DeductApplicationExecutionStatus.ABANDON);
			params.put("processing", DeductApplicationExecutionStatus.PROCESSING);
			params.put("deductApplicationUuid", deductApplication.getDeductApplicationUuid());
			params.put("lastModifiedTime", new Date());
			params.put("completeTime", new Date());
			this.genericDaoSupport.executeHQL(queryString, params);
		}
	}


}
