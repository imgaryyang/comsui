package com.suidifu.bridgewater.handler.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.handler.RemittanceNotifyJobSender;
import com.suidifu.bridgewater.handler.RemittanceTaskNoSession;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittanceDetail;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationCheckStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplicationDetail;
import com.zufangbao.sun.yunxin.entity.remittance.TransactionRecipient;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationDetailService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;

@Component("remittanceTaskNoSession")
public class RemittanceTaskNoSessionImpl implements RemittanceTaskNoSession {
	
	private static Log logger = LogFactory.getLog(RemittanceTaskNoSessionImpl.class);

	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;

	@Autowired
	private IRemittanceApplicationDetailService iRemittanceApplicationDetailService;

	@Autowired
	private RemittanceNotifyJobSender remittanceNotifyJobSender;

	@Value("#{config['business.verify.notify.url']}")
	private String business_verify_notify_url;
	
	@Value("#{config['notifyserver.notifyType']}")
	private String projectName;
	
	@Autowired
	private JpmorganApiHelper jpmorganApiHelper;
	
	@Autowired
	private IRemittancePlanService iRemittancePlanService;
	
	@Override
	public void sendToCitigroupForQuotaValidation(RemittanceApplication remittanceApplication) {
		
	}
	
	@Override
	public void sendToCitigroupForBussinessValidation(RemittanceApplication remittanceApplication) {
		
		validate(remittanceApplication);
		
		int retryNumber = remittanceApplication.getCheckRetryNumber();
		String remittanceApplicationUuid = remittanceApplication.getRemittanceApplicationUuid();
		if (retryNumber <= 0) {
			String updateSql = "update t_remittance_application set version_lock=:newVersion,check_status = :checkStatus,last_modified_time=:lastModifiedTime where " +
					"remittance_application_uuid=:remittanceApplicationUuid and version_lock=:oldVersion";
			Map<String, Object> params = new HashMap<String, Object>() {
				{
					put("checkStatus", RemittanceApplicationCheckStatus.RESPONSE_FAILURE.ordinal());
					put("remittanceApplicationUuid", remittanceApplicationUuid);
					put("lastModifiedTime", new Date());
				}
			};
			iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, updateSql, params);

			return;
		}
		
		List<RemittanceApplicationDetail> RemittanceApplicationDetailList = iRemittanceApplicationDetailService.getRemittanceApplicationDetailByApplicationUuid
				(remittanceApplicationUuid);
		
		
		RemittanceCommandModel commandModel = getRemittanceConmandModel(remittanceApplication, RemittanceApplicationDetailList);
		String updateSql = "update t_remittance_application set version_lock=:newVersion, check_request_no=:checkRequestNo,check_retry_number " +
				"=:checkRetryNumber,check_send_time =:checkSendTime,last_modified_time=:lastModifiedTime where " +
				"remittance_application_uuid=:remittanceApplicationUuid and version_lock=:oldVersion";
		Map<String, Object> params = new HashMap<String, Object>() {
			{
				put("checkRequestNo", commandModel.getCheckRequestNo());
				put("checkRetryNumber", retryNumber - 1);
				put("checkSendTime", new Date());
				put("lastModifiedTime", new Date());
				put("remittanceApplicationUuid", remittanceApplicationUuid);
			}
		};
		iRemittanceApplicationService.updateRemittanceApplicationWithVersionLock(remittanceApplicationUuid, updateSql, params);

		remittanceNotifyJobSender.pushJobToCitigroupForBusinessValidation(commandModel);
		
		SystemTraceLog systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_SEND_TO_MORGANSTANLEY_BY_TASK, commandModel.getCheckRequestNo(),
				"allParameters:" + JsonUtils.toJsonString(commandModel), null, SystemTraceLog.INFO, null,
				SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
		logger.info(systemTraceLog);
	}

	private void validate(RemittanceApplication remittanceApplication) {
		if(remittanceApplication == null) {
			throw new RuntimeException("remittanceApplication is empty");
		}
		if(remittanceApplication.getCheckStatus() != RemittanceApplicationCheckStatus.TO_VERIFY && remittanceApplication.getCheckStatus() != RemittanceApplicationCheckStatus.BUSINESS_VERIFICATION_SUCCESS) {
			throw new RuntimeException("check status is not to_verify or business_verification_success");
		}	
		if(remittanceApplication.getExecutionStatus() != ExecutionStatus.PROCESSING) {
			throw new RuntimeException("execution status is not processing");
		}		
		if(remittanceApplication.getTransactionRecipient() != TransactionRecipient.LOCAL) {
			throw new RuntimeException("transaction recipient is not local");
		}		
	}

	private RemittanceCommandModel getRemittanceConmandModel(RemittanceApplication remittanceApplication,
			List<RemittanceApplicationDetail> RemittanceApplicationDetailList) {
		RemittanceCommandModel commandModel = new RemittanceCommandModel();
		
		commandModel.setRequestNo(remittanceApplication.getRequestNo());
		commandModel.setUniqueId(remittanceApplication.getContractUniqueId());
		commandModel.setContractNo(remittanceApplication.getContractNo());
		commandModel.setProductCode(remittanceApplication.getFinancialProductCode());
		commandModel.setRemittanceDetails(getRemittanceDatailsString(RemittanceApplicationDetailList));
		commandModel.setRemittanceId(remittanceApplication.getRemittanceId());
		commandModel.setRemittanceStrategy(String.valueOf(remittanceApplication.getRemittanceStrategy().ordinal()));
		commandModel.setRemittanceApplicationUuid(remittanceApplication.getRemittanceApplicationUuid());
		commandModel.setRemark(remittanceApplication.getRemark());
		commandModel.setProjectName(projectName);
		commandModel.setTransactionType(remittanceApplication.getStringField1());
		
		return commandModel;
	}

	private String getRemittanceDatailsString(List<RemittanceApplicationDetail> RemittanceApplicationDetailList) {
		List<RemittanceDetail> remittanceDetailList = new ArrayList<RemittanceDetail>();
		for (RemittanceApplicationDetail remittanceApplicationDetail : RemittanceApplicationDetailList) {
			RemittanceDetail remittanceDetail = convertToRemittanceDetail(remittanceApplicationDetail);
			remittanceDetailList.add(remittanceDetail);
		}
		return JsonUtils.toJsonString(remittanceDetailList);
	}
	
	private RemittanceDetail convertToRemittanceDetail(RemittanceApplicationDetail remittanceApplicationDetail) {
		RemittanceDetail remittanceDetail = new RemittanceDetail();
		remittanceDetail.setRemittanceApplicationDetailUuid(remittanceApplicationDetail.getRemittanceApplicationDetailUuid());
		remittanceDetail.setDetailNo(remittanceApplicationDetail.getBusinessRecordNo());
		remittanceDetail.setRecordSn("" + remittanceApplicationDetail.getPriorityLevel());
		remittanceDetail.setBankCode(remittanceApplicationDetail.getCpBankCode());
		remittanceDetail.setBankCardNo(remittanceApplicationDetail.getCpBankCardNo());
		remittanceDetail.setBankAccountHolder(remittanceApplicationDetail.getCpBankAccountHolder());
		remittanceDetail.setIdNumber(remittanceApplicationDetail.getCpIdNumber());
		remittanceDetail.setBankProvince(remittanceApplicationDetail.getCpBankProvince());
		remittanceDetail.setBankCity(remittanceApplicationDetail.getCpBankCity());
		remittanceDetail.setBankName(remittanceApplicationDetail.getCpBankName());
		remittanceDetail.setPlannedDate(DateUtils.format(remittanceApplicationDetail.getPlannedPaymentDate(),"yyyy-MM-dd HH:mm:ss"));
		remittanceDetail.setAmount(remittanceApplicationDetail.getPlannedTotalAmount().toString());
		return remittanceDetail;
	}

	@Override
	public Map<String, Object> callBackQueryStatusForRemittance(List<String> remittanceApplicationUuids) {
		Map<String, Object> messages = new HashMap<>();

		if (CollectionUtils.isEmpty(remittanceApplicationUuids)) {
			logger.info("参数为空");
			return messages;
		}
		
		for (String remittanceApplicationUuid : remittanceApplicationUuids) {
			RemittanceSqlModel remittanceSqlModel = iRemittanceApplicationService.getRemittanceSqlModelBy(remittanceApplicationUuid);
			if(null == remittanceSqlModel) {
				messages.put(remittanceApplicationUuid, "放款订单uuid有误，无法查询到放款订单");
				continue;
			}
			
			if(remittanceSqlModel.getExecutionStatus() != ExecutionStatus.PROCESSING.ordinal() || remittanceSqlModel.getTransactionRecipient() != TransactionRecipient.OPPOSITE.ordinal()) {
				messages.put(remittanceApplicationUuid, "放款订单应为对端处理中");
				continue;
			}
			
			List<String> remittancePlanUuids = iRemittancePlanService.getRemittancePlanUuidByApplicationUuid(remittanceApplicationUuid);
			if(CollectionUtils.isEmpty(remittancePlanUuids)){
				messages.put(remittanceApplicationUuid, "无法查询到对应的处理中的放款单");
				continue;
			}
			
			logger.info("放款结果回调补漏，处理中的放款计划订单总计［"+remittancePlanUuids.size()+"］");

			
			Result result = jpmorganApiHelper.callBackQueryStatusForRemittance(remittancePlanUuids);
			
			String resultStr = JsonUtils.toJsonString(result);
			
			logger.info("放款订单号:["+remittanceApplicationUuid+"],放款单号:["+JsonUtils.toJsonString(remittancePlanUuids)+"],result:["+resultStr+"].");

			if(!result.isValid()) {
				messages.put(remittanceApplicationUuid, resultStr);
			}else {
				messages.put(remittanceApplicationUuid, "成功");
			}
		}
		
		return messages;
	}
}









