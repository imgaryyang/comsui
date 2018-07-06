package com.suidifu.bridgewater.handler.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.bridgewater.handler.RemittancetPlanHandler;
import com.zufangbao.gluon.api.jpmorgan.enums.AccountSide;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.sun.utils.EnumUtil;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationDetail;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlanExecLog;
import com.zufangbao.sun.yunxin.entity.remittance.ReverseStatus;
import com.zufangbao.sun.yunxin.service.UnionpayBankConfigService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanExecLogService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;

@Component("remittancetPlanHandler")
public class RemittancePlanHandlerImpl implements RemittancetPlanHandler{

	
	@Autowired
	private IRemittancePlanService iRemittancePlanService;
	
	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;
	
	@Autowired
	private IRemittanceApplicationDetailService iRemittanceApplicationDetailService; 
	
	@Autowired
	private IRemittancePlanExecLogService iRemittancePlanExecLogService;
	
	@Autowired
	private UnionpayBankConfigService unionpayBankConfigService;
	
	@Autowired
	private GenericDaoSupport genericDaoSupport; 
	
	@Value("#{config['remittance.notify.mq.jpmorgan.callback.url']}")
	private String jpmorganCallbackUrl;
	
	@Override
	public TradeSchedule saveRemittanceInfoBeforeResendForFailedPlan(String remittancePlanUuid) throws CommonException {
		RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
		if(remittancePlan == null || remittancePlan.getExecutionStatus() != ExecutionStatus.FAIL) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "仅失败的放款单允许人工处理！");
		}
		String remittanceApplicationUuid = remittancePlan.getRemittanceApplicationUuid();
		RemittanceApplication remittanceApplication = iRemittanceApplicationService.getUniqueRemittanceApplicationByUuid(remittanceApplicationUuid);
		if (remittanceApplication == null || remittanceApplication.getExecutionStatus() == ExecutionStatus.ABANDON
				|| remittanceApplication.getExecutionStatus() == ExecutionStatus.CREATE
				|| remittanceApplication.getExecutionStatus() == ExecutionStatus.PROCESSING
				|| remittanceApplication.getExecutionStatus() == ExecutionStatus.SUCCESS) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "仅异常或失败的放款计划订单允许人工处理！");
		}
		
		List<RemittancePlanExecLog> planExecLogs = iRemittancePlanExecLogService.getRemittancePlanExecLogListBy(remittancePlanUuid);
		if(CollectionUtils.isEmpty(planExecLogs)) {
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "人工处理失败，放款单下不存在相关线上代付单！");
		}
		
		for (RemittancePlanExecLog log : planExecLogs) {
			if (log.getExecutionStatus() != ExecutionStatus.FAIL) {
				if (log.getExecutionStatus() == ExecutionStatus.SUCCESS && log.getReverseStatus() == ReverseStatus.REFUND) {
					continue;
				}
				throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "仅相关线上代付单为失败和已退票时，允许人工处理！");
			}
		}
		
		remittancePlan = reRemittanceForRefund(remittancePlan);
		
		TradeSchedule tradeSchedule = convertToTradeSchedule(remittanceApplicationUuid, remittancePlan);
		
		tradeSchedule.setNotifyUrl(jpmorganCallbackUrl);
		
		updateApplicationAndDetailTotalCount(remittanceApplication, remittancePlan);
		
		RemittancePlanExecLog remittancePlanExecLog = new RemittancePlanExecLog(remittancePlan, tradeSchedule.getSourceMessageUuid());
		iRemittancePlanExecLogService.save(remittancePlanExecLog);
		return tradeSchedule;
	}

	private void updateApplicationAndDetailTotalCount(RemittanceApplication remittanceApplication, RemittancePlan remittancePlan) {
		int actualCount = remittanceApplication.getActualCount() - 1;
		remittanceApplication.setActualCount(actualCount);
		iRemittanceApplicationService.update(remittanceApplication);
		
		
		RemittanceApplicationDetail detail = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByUuid(remittancePlan.getRemittanceApplicationDetailUuid());
		int totalCount = detail.getTotalCount() + 1;
		detail.setTotalCount(totalCount);
		iRemittanceApplicationDetailService.update(detail);
	}

	/**
	 * 更新线上代付单状态（交易受理失败）
	 * @param commandModel
	 * @param tradeSchedules
	 */
	@Override
	public void updateRemittancePlanExecLogAndDetailAfterSendFailBy(String remittancePlanUuid, String remittanceExecReqNo, String remittanceApplicationDetailUuid, String remittanceApplicationUuid, String executionRemark) {
		Map<String, Object> paramsForFail = new HashMap<String, Object>();
		
		paramsForFail.put("lastModifiedTime", new Date());
		paramsForFail.put("remittanceApplicationDetailUuid", remittanceApplicationDetailUuid);
		paramsForFail.put("remittanceApplicationUuid", remittanceApplicationUuid);

		genericDaoSupport.executeSQL(" update t_remittance_application_detail set actual_count = total_count where remittance_application_detail_uuid = :remittanceApplicationDetailUuid ", paramsForFail);
		genericDaoSupport.executeSQL(" update t_remittance_application set actual_count = total_count where remittance_application_uuid = :remittanceApplicationUuid ", paramsForFail);

		paramsForFail.put("execReqNo", remittanceExecReqNo);
		paramsForFail.put("remittancePlanUuid", remittancePlanUuid);
		paramsForFail.put("executionStatus", ExecutionStatus.FAIL.ordinal());
		paramsForFail.put("executionRemark", executionRemark);
		
		genericDaoSupport.executeSQL(
				"UPDATE t_remittance_plan "
					+ " SET execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime "
					+ " WHERE remittance_plan_uuid =:remittancePlanUuid", paramsForFail);
		
		genericDaoSupport.executeSQL(
				"UPDATE t_remittance_plan_exec_log "
					+ " SET execution_status =:executionStatus, execution_remark =:executionRemark, last_modified_time =:lastModifiedTime "
					+ " WHERE exec_req_no =:execReqNo", paramsForFail);
		

	}
	
	/**
	 * 更新放款相关单据状态，为处理中（以便进行重新同步）
	 * @param remittanceApplicationUuid
	 */
	@Override
	public void updateRemittanceStatusAfterResendSuccessBy(
			String remittanceApplicationUuid, String remittanceDetailUuid, String remittancePlanUuid) {
		if(StringUtils.isEmpty(remittanceApplicationUuid) || StringUtils.isEmpty(remittanceDetailUuid) ||StringUtils.isEmpty(remittancePlanUuid)) {
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("executionStatus", ExecutionStatus.PROCESSING.ordinal());
		params.put("remittanceApplicationUuid", remittanceApplicationUuid);
		params.put("remittanceDetailUuid", remittanceDetailUuid);
		params.put("remittancePlanUuid", remittancePlanUuid);
		genericDaoSupport.executeSQL(
				"UPDATE t_remittance_application "
					+ "SET execution_status =:executionStatus "
					+ "WHERE remittance_application_uuid =:remittanceApplicationUuid", params);
		genericDaoSupport.executeSQL(
				"UPDATE t_remittance_application_detail "
						+ "SET execution_status =:executionStatus "
						+ "WHERE remittance_application_detail_uuid =:remittanceDetailUuid", params);
		genericDaoSupport.executeSQL(
				"UPDATE t_remittance_plan "
						+ "SET execution_status =:executionStatus "
						+ "WHERE remittance_plan_uuid =:remittancePlanUuid", params);
	}
	
	/**
 	 * 退票时，重新放款，需要重新生成放款单，原先放款单置为撤销
 	 */
 	private RemittancePlan reRemittanceForRefund(RemittancePlan remittancePlan){
 			RemittancePlan newRemittancePlan = new RemittancePlan(UUID.randomUUID().toString(),
 					remittancePlan.getRemittanceApplicationUuid(), remittancePlan.getRemittanceApplicationDetailUuid(),
 					remittancePlan.getFinancialContractUuid(), remittancePlan.getFinancialContractId(),
 					remittancePlan.getBusinessRecordNo(), remittancePlan.getContractUniqueId(),
 					remittancePlan.getContractNo(), remittancePlan.getPaymentGateway(),
 					remittancePlan.getPaymentChannelUuid(), remittancePlan.getPaymentChannelName(),
 					remittancePlan.getPgAccountName(), remittancePlan.getPgClearingAccount(),
 					remittancePlan.getTransactionType().ordinal(), remittancePlan.getTransactionRemark(),
 					remittancePlan.getPriorityLevel(), remittancePlan.getCpBankCode(), remittancePlan.getCpBankCardNo(),
 					remittancePlan.getCpBankAccountHolder(), remittancePlan.getCpIdType(),
 					remittancePlan.getCpIdNumber(), remittancePlan.getCpBankProvince(), remittancePlan.getCpBankCity(),
 					remittancePlan.getCpBankName(), remittancePlan.getPlannedPaymentDate(),
 					remittancePlan.getPlannedTotalAmount(), remittancePlan.getExecutionPrecond(),
 					remittancePlan.getCreatorName());
 			newRemittancePlan.setPgAccountNo(remittancePlan.getPgAccountNo());
 			iRemittancePlanService.save(newRemittancePlan);
 			remittancePlan.setExecutionStatus(ExecutionStatus.ABANDON);
 			iRemittancePlanService.update(remittancePlan);
 			return newRemittancePlan;
 	}
 	
 	private TradeSchedule convertToTradeSchedule(String remittanceApplicationUuid, RemittancePlan remittancePlan) {
		String fstPaymentChannelUuid = remittancePlan.getPaymentChannelUuid();
		Integer paymentGateway = remittancePlan.getPaymentGateway().ordinal();
		String channelName = remittancePlan.getPaymentChannelName();
		
		String remittanceDetailUuid = remittancePlan.getRemittanceApplicationDetailUuid();
		String relatedBusinessRecordNo = remittancePlan.getBusinessRecordNo();
				
		Map<String, String> destinationAccountAppendixMap = new HashMap<String, String>();
		destinationAccountAppendixMap.put("idNumber", remittancePlan.getCpIdNumber());
		String destinationAccountAppendix = JSON.toJSONString(destinationAccountAppendixMap, SerializerFeature.DisableCircularReferenceDetect);
		
		Map<String, String> destinationBankInfoMap = new HashMap<String, String>();
		destinationBankInfoMap.put("bankCode", remittancePlan.getCpBankCode());
		destinationBankInfoMap.put("bankProvince", remittancePlan.getCpBankProvince());
		destinationBankInfoMap.put("bankCity", remittancePlan.getCpBankCity());
		destinationBankInfoMap.put("bankName", remittancePlan.getCpBankName());
		String destinationBankInfo = JSON.toJSONString(destinationBankInfoMap, SerializerFeature.DisableCircularReferenceDetect);
		
		int transactionType = remittancePlan.getTransactionType().ordinal();
		String bankAccountHolder = remittancePlan.getCpBankAccountHolder();
		String bankCardNo = remittancePlan.getCpBankCardNo();
		
		BigDecimal fstTransactionAmount = remittancePlan.getPlannedTotalAmount();
		int priorityLevel = remittancePlan.getPriorityLevel();
		String fstGateWayRouterInfo = null;
		if(transactionType == AccountSide.DEBIT.ordinal()) {
			String debitBankCode = remittancePlan.getCpBankCode();
			String reckonAccount = remittancePlan.getPgClearingAccount();
			fstGateWayRouterInfo = getFstGateWayRouterInfoForDeduct(debitBankCode, reckonAccount);
		}
		String outlierTransactionUuid = remittancePlan.getRemittancePlanUuid();
		
		AccountSide accountSide = EnumUtil.fromOrdinal(AccountSide.class, transactionType);
		return new TradeSchedule(accountSide, bankAccountHolder, bankCardNo, destinationAccountAppendix, destinationBankInfo, null, outlierTransactionUuid, UUID.randomUUID().toString(), fstPaymentChannelUuid, null, fstTransactionAmount, remittanceApplicationUuid, null, remittanceDetailUuid, relatedBusinessRecordNo, priorityLevel, paymentGateway, channelName, fstGateWayRouterInfo);
	}
 	
 	private String getFstGateWayRouterInfoForDeduct(String standardBankCode, String reckonAccount) {
		String debitMode = unionpayBankConfigService.isUseBatchDeduct(null, standardBankCode) ? "batchMode" : "realTimeMode";
		
		Map<String,String> fstGateWayRouterInfoMap = new HashMap<String,String>();
		fstGateWayRouterInfoMap.put("debitMode",debitMode);
		
		if(StringUtils.isNotEmpty(reckonAccount)) {
			fstGateWayRouterInfoMap.put("reckonAccount", reckonAccount);
		}
		
		return JSON.toJSONString(fstGateWayRouterInfoMap);
	}
}
