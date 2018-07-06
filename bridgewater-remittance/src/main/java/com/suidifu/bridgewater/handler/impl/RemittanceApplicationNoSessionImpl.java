package com.suidifu.bridgewater.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.RemittanceContext;
import com.suidifu.bridgewater.fast.FastRemittanceApplication;
import com.suidifu.bridgewater.fast.FastRemittancePlan;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.handler.RemittanceApplicationNoSession;
import com.suidifu.bridgewater.handler.RemittanceNotifyHandler;
import com.suidifu.bridgewater.handler.RemittanceNotifyJobSender;
import com.suidifu.bridgewater.handler.RemittancetPlanHandler;
import com.zufangbao.gluon.api.jpmorgan.JpmorganApiHelper;
import com.zufangbao.gluon.api.jpmorgan.model.QueryStatusResult;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.exception.CommonException;
import com.zufangbao.gluon.logspec.GloableLogSpec;
import com.zufangbao.gluon.logspec.GloableLogSpec.bridgewater_remittance_function_point;
import com.zufangbao.gluon.opensdk.HttpClientUtils;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.spec.global.GlobalCodeSpec;
import com.zufangbao.gluon.spec.morganstanley.CommonSpec;
import com.zufangbao.gluon.util.ApiMessageUtil;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittancePlanInfo;
import com.zufangbao.sun.api.model.remittance.RemittanceResponsePacket;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.yunxin.entity.remittance.ExecutionStatus;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceApplication;
import com.zufangbao.sun.yunxin.entity.remittance.RemittancePlan;
import com.zufangbao.sun.yunxin.service.remittance.IRemittanceApplicationService;
import com.zufangbao.sun.yunxin.service.remittance.IRemittancePlanService;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.Transfer;

@Component("remittanceApplicationNoSession")
public class RemittanceApplicationNoSessionImpl implements RemittanceApplicationNoSession {

	private static final Log logger = LogFactory.getLog(RemittanceApplicationNoSessionImpl.class);

	@Autowired
	private IRemittanceApplicationHandler iRemittanceApplicationHandler;

	@Autowired
	private RemittanceNotifyHandler remittanceNotifyHandler;

	@Autowired
	private RemittancetPlanHandler remittancetPlanHandler;

	@Autowired
	private RemittanceNotifyJobSender remittanceNotifyJobSender;

	@Autowired
	private JpmorganApiHelper jpmorganApiHelper;

	@Autowired
	private IRemittanceApplicationService iRemittanceApplicationService;

	@Value("#{config['remittance_notify_url']}")
	private String remittance_notify_url;
	
	@Autowired
	private IRemittancePlanService iRemittancePlanService;

	@Value("#{config['remittance.notify.mq.jpmorgan.callback.url']}")
	private String jpmorganCallbackUrl;

	private static final Log st_logger = LogFactory.getLog("stLogger");

	@Override
	public void receiveCitiGroupCallbackAfterBusinessValidation(Map<String, String> receiveParams) {

		RemittanceResponsePacket packet = JsonUtils.parse(receiveParams.get(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL), RemittanceResponsePacket.class);
		SystemTraceLog systemTraceLog;
		RemittanceCommandModel commandModel = packet.getRemittanceCommandModel();
		List<TradeSchedule> tradeSchedules = packet.getTradeSchedules();
		FinancialContract financialContract = packet.getFinancialContract();
		Boolean isSuccess = packet.getIsSuccess();
		Boolean isNeedDoBalance = packet.getIsNeedDoBalance();
		String failMessage = packet.getFailMessage();
		String checkRequestNo = commandModel.getCheckRequestNo();
		String remittanceApplicationUuid = commandModel.getRemittanceApplicationUuid();
		String eventKey = "checkRequestNo:" + checkRequestNo + "&" + "remittanceApplicationUuid"+ remittanceApplicationUuid;

		if (isSuccess == null || isNeedDoBalance == null) {
			throw new RuntimeException("isSuccess is null or isNeedDoBalance is null");
		}

		try {
			systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_ACCEPT_CITIGROUP_REQUEST_START, eventKey, null,null, SystemTraceLog.INFO, null, SYSTEM_NAME.CITIGROUP, SYSTEM_NAME.REMITTANCE_SYSTEM);
			logger.info(systemTraceLog);
			st_logger.debug(systemTraceLog.getSql(EVENT_NAME.REMITTANCE_ACCEPT_CITIGROUP_REQUEST_START,remittanceApplicationUuid, new Date()));
			systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ACCEPT_CITIGROUP_REQUEST_END);

			if(!Transfer.isTransfer(commandModel.getTransactionType())) {
				if (!remittanceNotifyHandler.validateRemittanceBlackList(isSuccess, commandModel.getUniqueId())) {
					isSuccess = false;
					failMessage = ApiMessageUtil
							.getMessage(ApiResponseCode.CONTRACT_UNIQUED_ID_EXIST_IN_REMITTANCE_BLACK_LIST);
				}
			}

			String newCheckRequestNo = iRemittanceApplicationHandler.checkAndUpdateApplication(remittanceApplicationUuid, isSuccess, checkRequestNo, true, isNeedDoBalance);

			fillFinancialInfoIntoRemittance(financialContract, remittanceApplicationUuid);

			operationByBusinessVeriationResult(systemTraceLog, commandModel, tradeSchedules, financialContract,
					isSuccess, isNeedDoBalance, failMessage, remittanceApplicationUuid, newCheckRequestNo);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					new SystemTraceLog(EVENT_NAME.REMITTANCE_ACCEPT_CITIGROUP_REQUEST_END, eventKey, null,
							"#receiveCitygroupCallback# occur exception with stack trace["
									+ ExceptionUtils.getFullStackTrace(e) + "]",
							SystemTraceLog.ERROR, null, SYSTEM_NAME.CITIGROUP, SYSTEM_NAME.REMITTANCE_SYSTEM));
		}
	}

	private void fillFinancialInfoIntoRemittance(FinancialContract financialContract,
			String remittanceApplicationUuid) {
		if (financialContract != null) {

			iRemittanceApplicationHandler.fillFinancialInfoIntoRemittance(remittanceApplicationUuid,
					financialContract);

		}
	}

	private void operationByBusinessVeriationResult(SystemTraceLog systemTraceLog, RemittanceCommandModel commandModel,
			List<TradeSchedule> tradeSchedules, FinancialContract financialContract, Boolean isSuccess,
			Boolean isNeedDoBalance, String failMessage, String remittanceApplicationUuid, String newCheckRequestNo) {
		if (isSuccess) {

			operationWhenBusinessVeriationSuccess(systemTraceLog, commandModel, tradeSchedules, financialContract,
					isNeedDoBalance, remittanceApplicationUuid, newCheckRequestNo);

		} else {

			failMessage = "业务校验失败，" + failMessage;

			failOperationsForFirstTimeRemittance(remittanceApplicationUuid, failMessage, false);

			logger.info(systemTraceLog.error(failMessage));

		}
	}

	private void operationWhenBusinessVeriationSuccess(SystemTraceLog systemTraceLog,
			RemittanceCommandModel commandModel, List<TradeSchedule> tradeSchedules,
			FinancialContract financialContract, Boolean isNeedDoBalance, String remittanceApplicationUuid,
			String newCheckRequestNo) {
		businessSuccessValidate(tradeSchedules, financialContract);

		if (iRemittancePlanService.isExistsRemittancePlan(remittanceApplicationUuid)) {
			logger.info(systemTraceLog.info("放款单已存在"));
			iRemittancePlanService.deleteRedundancyRemittancePlanAndExecLog(remittanceApplicationUuid);
		}

		List<RemittancePlan> remittancePlans = iRemittanceApplicationHandler.saveRemittanceInfo(commandModel,
				tradeSchedules, financialContract);

		if (isNeedDoBalance) {
			remittanceNotifyJobSender.pushJobToCitigroupForQuotaValidation(remittanceApplicationUuid,
					financialContract.getFinancialContractUuid(), getRemittancePlanInfoList(remittancePlans),
					commandModel, newCheckRequestNo, tradeSchedules);
			logger.info(systemTraceLog.info("向citigroup发起限额申请"));
			st_logger.debug(systemTraceLog.getSql(remittanceApplicationUuid, new Date()));
		} else {
			pushTradeSchedulesToJpmorgan(commandModel, tradeSchedules, remittanceApplicationUuid, financialContract.getFinancialContractUuid());
			logger.info(systemTraceLog.info("该信托未配置限额，直接向jpmorgan发起放款"));
			st_logger.debug(systemTraceLog.getSql(remittanceApplicationUuid, new Date()));
		}
	}

	private void pushTradeSchedulesToJpmorgan(RemittanceCommandModel commandModel, List<TradeSchedule> tradeSchedules,
			String remittanceApplicationUuid, String financialContractUuid) {
		setNotifyUrlForTradeScheduleList(tradeSchedules, financialContractUuid);

		this.processingAndUpdateRemittanceInfo(tradeSchedules, remittanceApplicationUuid, commandModel.getRequestNo(),
				false, Transfer.isTransfer(commandModel.getTransactionType()));
	}

	private List<RemittancePlanInfo> getRemittancePlanInfoList(List<RemittancePlan> remittancePlans) {
		List<RemittancePlanInfo> remittancePlanInfoList = new ArrayList<>();
		for (RemittancePlan remittancePlan : remittancePlans) {
			remittancePlanInfoList.add(new RemittancePlanInfo(remittancePlan.getRemittancePlanUuid(),
					remittancePlan.getPlannedTotalAmount(), null));
		}
		return remittancePlanInfoList;
	}

	@Override
	public void receiveCitiGroupCallbackAfterQuotaValidation(Map<String, String> receiveParams) {

		RemittanceResponsePacket packet = JsonUtils.parse(
				receiveParams.get(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL), RemittanceResponsePacket.class);

		SystemTraceLog systemTraceLog;
		String ip = null;
		RemittanceCommandModel commandModel = packet.getRemittanceCommandModel();
		List<TradeSchedule> tradeSchedules = packet.getTradeSchedules();
		Boolean isSuccess = packet.getIsSuccess();
		String failMessage = packet.getFailMessage();
		String afterBusinessValidationCheckRequestNo = packet.getCheckRequestNo();
		String financialContractUuid = packet.getFinancialContractUuid();
		String remittanceApplicationUuid = commandModel.getRemittanceApplicationUuid();
		String eventKey = "checkRequestNo:" + afterBusinessValidationCheckRequestNo + "&" + "remittanceApplicationUuid"
				+ remittanceApplicationUuid;

		if (isSuccess == null || CollectionUtils.isEmpty(tradeSchedules) || StringUtils.isEmpty(financialContractUuid)) {
			throw new RuntimeException("isSuccess is null or tradeSchedules is empty");
		}

		try {
			systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_ACCEPT_CITIGROUP_REQUEST_QUOTA_VALIDATION_START,
					eventKey, null, null, SystemTraceLog.INFO, ip, SYSTEM_NAME.CITIGROUP,
					SYSTEM_NAME.REMITTANCE_SYSTEM);
			logger.info(systemTraceLog);
			st_logger.debug(systemTraceLog.getSql(remittanceApplicationUuid, new Date()));

			systemTraceLog.setEventName(EVENT_NAME.REMITTANCE_ACCEPT_CITIGROUP_REQUEST_QUOTA_VALIDATION_END);

			iRemittanceApplicationHandler.checkAndUpdateApplication(remittanceApplicationUuid, isSuccess,
					afterBusinessValidationCheckRequestNo, false, null);
			if (isSuccess) {

				validaterRemittancePlanSize(systemTraceLog, tradeSchedules, remittanceApplicationUuid);

				pushTradeSchedulesToJpmorgan(commandModel, tradeSchedules, remittanceApplicationUuid, financialContractUuid);

				logger.info(systemTraceLog);
				st_logger.debug(systemTraceLog.getSql(remittanceApplicationUuid, new Date()));

			} else {

				failMessage = "限额校验失败，" + failMessage;

				failOperationsForFirstTimeRemittance(remittanceApplicationUuid, failMessage, false);

				logger.info(systemTraceLog.error(failMessage));

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					new SystemTraceLog(EVENT_NAME.REMITTANCE_ACCEPT_CITIGROUP_REQUEST_END, eventKey, null,
							"#receiveCitygroupCallback# occur exception with stack trace["
									+ ExceptionUtils.getFullStackTrace(e) + "]",
							SystemTraceLog.ERROR, ip, SYSTEM_NAME.CITIGROUP, SYSTEM_NAME.REMITTANCE_SYSTEM));
		}
	}

	private void validaterRemittancePlanSize(SystemTraceLog systemTraceLog, List<TradeSchedule> tradeSchedules,
			String remittanceApplicationUuid) {
		List<RemittancePlan> remittancePlans = iRemittancePlanService
				.getRemittancePlanListBy(remittanceApplicationUuid);
		if (remittancePlans.size() != tradeSchedules.size()) {
			String errorMessage = "系统异常，重复生成放款单";
			logger.error(systemTraceLog.error(errorMessage));
			throw new RuntimeException(errorMessage);
		}
	}

	private void setNotifyUrlForTradeScheduleList(List<TradeSchedule> tradeSchedules, String financialContractUuid) {
		for (TradeSchedule tradeSchedule : tradeSchedules) {
			tradeSchedule.setNotifyUrl(jpmorganCallbackUrl);
			tradeSchedule.setFinancialContractUuid(financialContractUuid);
		}
	}

	private void businessSuccessValidate(List<TradeSchedule> tradeSchedules, FinancialContract financialContract) {

		if (CollectionUtils.isEmpty(tradeSchedules)) {
			throw new ApiException("tradeSchedules_is_empty");
		}

		if (financialContract == null) {
			throw new ApiException("financialContract_is_null");
		}

	}

	@Override
	public void processingAndUpdateRemittanceInfo(List<TradeSchedule> tradeSchedules, String remittanceApplicationUuid,
			String requestNo, boolean isSecondRemittance, boolean isTransfer) {

		String localKeyWord = "timeStamp:[" + System.currentTimeMillis() + "].BRIDGEWATER[requestNo=" + requestNo
				+ ";remittanceApplicationUuid=" + remittanceApplicationUuid + "]";
		String remoteKeyWord = "==SENTTO==>>JPMORGAN[";

		List<String> remittancePlanUuids = new ArrayList<>();
		for (TradeSchedule trade : tradeSchedules) {
			remoteKeyWord += "SourceMessageUuid=" + trade.getSourceMessageUuid() + ";";
			remittancePlanUuids.add(trade.getOutlierTransactionUuid());
		}
		remoteKeyWord += "]}";
		String requestBody = JSON.toJSONString(tradeSchedules, SerializerFeature.DisableCircularReferenceDetect);

		logger.info(GloableLogSpec.AuditLogHeaderSpec()
				+ bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN + localKeyWord + remoteKeyWord
				+ GloableLogSpec.RawData(requestBody));
		logger.info("sendBatchTradeSchedules start:" + localKeyWord);
		Result result = jpmorganApiHelper.sendBatchTradeSchedules(tradeSchedules, requestBody);
		logger.info("sendBatchTradeSchedules end:" + localKeyWord);

		if (!result.isValid()) {
			String httpStatus = (String) result.getData().getOrDefault(HttpClientUtils.DATA_RESPONSE_HTTP_STATUS, "");
			logger.error("放款请求受理失败，递交对端失败(" + httpStatus + ")!");
			logger.info(GloableLogSpec.AuditLogHeaderSpec()
					+ bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN + localKeyWord
					+ "ERR:[放款请求受理失败，递交对端失败!(" + httpStatus + ")]" + GloableLogSpec
							.RawData(JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect)));

			operationsForSendJpmorganFailure(isSecondRemittance, remittanceApplicationUuid,
					httpStatus + ApiMessageUtil.getMessage(ApiResponseCode.SYSTEM_BUSY), remittancePlanUuids, isTransfer);
			throw new ApiException(ApiResponseCode.SYSTEM_BUSY);
		}

		String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
		Result responseResult = JsonUtils.parse(responseStr, Result.class);
		if (responseResult != null && !responseResult.isValid()) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec()
					+ bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN + localKeyWord
					+ "ERR:[放款请求受理失败，对端响应" + responseResult.getMessage() + "]" + GloableLogSpec
							.RawData(JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect)));

			logger.error("放款请求受理失败，对端响应（" + responseResult.getMessage() + "）!");

			operationsForSendJpmorganFailure(isSecondRemittance, remittanceApplicationUuid, "放款请求受理失败!",
					remittancePlanUuids, isTransfer);

			throw new ApiException(ApiResponseCode.SYSTEM_ERROR, "放款请求受理失败!");
		}

		// 捕捉异常，防止DB异常，返回系统错误
		try {
			logger.info("updateRemittanceInfoAfterSendSuccessBy start:" + localKeyWord);
			iRemittanceApplicationHandler.updateRemittanceInfoAfterSendSuccessBy(remittanceApplicationUuid);
			logger.info("updateRemittanceInfoAfterSendSuccessBy end:" + localKeyWord);
		} catch (Exception e) {
			logger.info(GloableLogSpec.AuditLogHeaderSpec()
					+ bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN + localKeyWord
					+ "ERR:[落盘对端处理中失败]");
			e.printStackTrace();
		}
		logger.info(GloableLogSpec.AuditLogHeaderSpec()
				+ bridgewater_remittance_function_point.SEND_TRADE_SCHEDULES_TO_JPMORGAN + localKeyWord
				+ "[SUCC:放款请求受理成功]");
	}

	private void operationsForSendJpmorganFailure(boolean isSecondRemittance, String remittanceApplicationUuid,
			String failMessage, List<String> remittancePlanUuids, boolean isTransfer) {
		if (isSecondRemittance) {
			failOperationsForSecondRemittance(remittanceApplicationUuid, remittancePlanUuids, failMessage, true);
		} else {
			failOperationsForFirstTimeRemittance(remittanceApplicationUuid, failMessage, isTransfer ? false : true);
		}

	}

	private void failOperationsForFirstTimeRemittance(String remittanceApplicationUuid, String executionRemark,
			boolean unfreezeQuota) {
		iRemittanceApplicationHandler.updateRemittanceInfoAfterSendFailBy(remittanceApplicationUuid, executionRemark);
		remittanceNotifyHandler.processingRemittanceCallback(remittanceApplicationUuid);
		if (unfreezeQuota) {
			unfreezeQuota(remittanceApplicationUuid);
		}
	}

	private void unfreezeQuota(String remittanceApplicationUuid) {
		RemittanceSqlModel remittanceSqlModel = iRemittanceApplicationService
				.getRemittanceSqlModelBy(remittanceApplicationUuid);
		List<RemittancePlan> remittancePlans = iRemittancePlanService
				.getRemittancePlanListBy(remittanceApplicationUuid);
		List<RemittancePlanInfo> remittancePlanInfoList = new ArrayList<>();
		for (RemittancePlan remittancePlan : remittancePlans) {
			remittancePlanInfoList.add(new RemittancePlanInfo(remittancePlan.getRemittancePlanUuid(),
					remittancePlan.getPlannedTotalAmount(), false));
		}
		remittanceNotifyJobSender.pushJobToCitigroupForUnFreezeQuota(remittanceApplicationUuid,
				remittanceSqlModel.getFinancialContractUuid(), remittancePlanInfoList);
	}

	@Override
	public void processingAndUpdateRemittanceInfoForResend(TradeSchedule tradeSchedule) throws CommonException {

		Result result = jpmorganApiHelper.sendBatchTradeSchedules(Arrays.asList(tradeSchedule));

		String remittanceApplicationUuid = tradeSchedule.getBatchUuid();
		String remittancePlanUuid = tradeSchedule.getOutlierTransactionUuid();
		String remittanceExecReqNo = tradeSchedule.getSourceMessageUuid();
		String remittanceDetailUuid = tradeSchedule.getRelatedDetailUuid();

		if (!result.isValid()) {
			logger.error("放款请求受理失败，递交对端失败!");
			failOpeationsWhenResend(remittanceApplicationUuid, remittancePlanUuid, remittanceExecReqNo, "系统繁忙，请稍后再试", true);
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "系统繁忙，请稍后再试");
		}

		String responseStr = String.valueOf(result.get(HttpClientUtils.DATA_RESPONSE_PACKET));
		Result responseResult = JsonUtils.parse(responseStr, Result.class);
		if (responseResult != null && !responseResult.isValid()) {
			logger.error("放款请求受理失败，对端响应（" + responseResult.getMessage() + "）!");
			failOpeationsWhenResend(remittanceApplicationUuid, remittancePlanUuid, remittanceExecReqNo, "放款请求受理失败!", true);
			throw new CommonException(GlobalCodeSpec.CODE_FAILURE, "放款请求受理失败!");
		}

		remittancetPlanHandler.updateRemittanceStatusAfterResendSuccessBy(remittanceApplicationUuid,
				remittanceDetailUuid, remittancePlanUuid);
		logger.info("放款请求受理成功，响应结果（" + JsonUtils.toJsonString(result) + "）");

		// TODO 冻结限额

	}

	private void failOpeationsWhenResend(String remittanceApplicationUuid, String remittancePlanUuid,
			String remittanceExecReqNo, String failMessage, boolean unFreezeQuota) {
	
		RemittancePlan plan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
		
		String remittanceApplicationDetailUuid = plan.getRemittanceApplicationDetailUuid();
		remittancetPlanHandler.updateRemittancePlanExecLogAndDetailAfterSendFailBy(remittancePlanUuid, remittanceExecReqNo,
				remittanceApplicationDetailUuid, remittanceApplicationUuid, failMessage);
		
		if (unFreezeQuota) {
			RemittanceApplication application = iRemittanceApplicationService
					.getRemittanceApplicationBy(remittanceApplicationUuid);
			RemittancePlanInfo remittancePlanInfo = new RemittancePlanInfo(plan.getRemittancePlanUuid(),
					plan.getPlannedTotalAmount(), false);
			remittanceNotifyJobSender.pushJobToCitigroupForResendUnFreezeQuota(remittanceApplicationUuid,
					application.getFinancialContractUuid(), remittancePlanInfo);
		}
	}

	@Override
	public void receiveJpmorganCallback(Map<String, String> receiveParams) {
		RemittanceContext remittanceContext = null;
		try {

			String queryStatusResultJson = receiveParams.get(CommonSpec.TRANSACTION_RESULT);

			QueryStatusResult queryStatusResult = JsonUtils.parse(queryStatusResultJson, QueryStatusResult.class);

			remittanceContext = iRemittanceApplicationHandler.buildContext(queryStatusResult);

			iRemittanceApplicationHandler.singleAnalysisRemittanceResult(remittanceContext);

			if (remittanceContext.getFastRemittanceApplication().getExecutionStatus() != ExecutionStatus.PROCESSING
					.ordinal()) {
				remittanceNotifyHandler.processingRemittanceCallback(
						remittanceContext.getFastRemittanceApplication().getRemittanceApplicationUuid());
			}

			if(!Transfer.isTransfer(remittanceContext.getFastRemittanceApplication().getStringField1())) {
				unfreezeQuota(remittanceContext);
			}

			String remittanceApplicationUuid = remittanceContext.getFastRemittanceApplication()
					.getRemittanceApplicationUuid();
			String remittancePlanUuid = remittanceContext.getFastRemittancePlan().getUuid();
			SystemTraceLog systemTraceLog = new SystemTraceLog(EVENT_NAME.REMITTANCE_ACCEPT_JPMORGAN_QUERY_RESULT_END,
					"planUuid:" + remittancePlanUuid + "&applicationUuid:" + remittanceApplicationUuid, "exec-success",
					null, SystemTraceLog.INFO, null, SYSTEM_NAME.JPMORGAN, SYSTEM_NAME.REMITTANCE_SYSTEM);
			logger.info(systemTraceLog);
			st_logger.debug(systemTraceLog.getSql(remittanceApplicationUuid, new Date()));

		} catch (Exception e) {
			e.printStackTrace();

			String planUuid = remittanceContext != null && remittanceContext.getFastRemittancePlan() != null
					? remittanceContext.getFastRemittancePlan().getUuid()
					: null;
			String applicationUuid = remittanceContext != null
					&& remittanceContext.getFastRemittanceApplication() != null
							? remittanceContext.getFastRemittanceApplication().getRemittanceApplicationUuid()
							: null;
			String eventKey = "planUuid:" + planUuid + "&applicationUuid:" + applicationUuid;
			SystemTraceLog systemTraceLog = new SystemTraceLog(
					EVENT_NAME.REMITTANCE_ACCEPT_JPMORGAN_QUERY_RESULT_END_ERROR, eventKey, null,
					"#receiveRemittanceCallback# occur exception with stack trace["
							+ ExceptionUtils.getFullStackTrace(e) + "]",
					SystemTraceLog.ERROR, null, SYSTEM_NAME.JPMORGAN, SYSTEM_NAME.REMITTANCE_SYSTEM);

			logger.info(systemTraceLog);

		}

	}

	private void unfreezeQuota(RemittanceContext remittanceContext) {
		FastRemittanceApplication application = remittanceContext.getFastRemittanceApplication();
		FastRemittancePlan plan = remittanceContext.getFastRemittancePlan();
		boolean isSuccess = plan.getExecutionStatus() == ExecutionStatus.SUCCESS.ordinal() ? true : false;
		RemittancePlanInfo remittancePlanInfo = new RemittancePlanInfo(plan.getRemittancePlanUuid(),
				plan.getPlannedTotalAmount(), isSuccess);
		remittanceNotifyJobSender.pushJobToCitigroupForUnFreezeQuota(application.getRemittanceApplicationUuid(),
				application.getFinancialContractUuid(), Arrays.asList(remittancePlanInfo));
	}

	@Override
	public void receiveCitiGroupAfterResendQuotaValidation(Map<String, String> receiveParams) {

		RemittanceResponsePacket packet = JsonUtils.parse(
				receiveParams.get(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL), RemittanceResponsePacket.class);
		List<TradeSchedule> tradeSchedules = packet.getTradeSchedules();
		Boolean isSuccess = packet.getIsSuccess();
		String failMessage = packet.getFailMessage();

		if (isSuccess == null || CollectionUtils.isEmpty(tradeSchedules) || tradeSchedules.size() != 1) {
			throw new RuntimeException("isSuccess is null or tradeSchedules size != 1");
		}
		TradeSchedule tradeSchedule = tradeSchedules.get(0);
		String remittanceApplicationUuid = tradeSchedule.getBatchUuid();
		String remittancePlanUuid = tradeSchedule.getOutlierTransactionUuid();
		String remittanceExecReqNo = tradeSchedule.getSourceMessageUuid();
		try {
			if (isSuccess) {

				processingAndUpdateRemittanceInfoForResend(tradeSchedule);

			} else {
				failOpeationsWhenResend(remittanceApplicationUuid, remittancePlanUuid, remittanceExecReqNo,
						failMessage, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			iRemittanceApplicationHandler.deleteAllObjectAndRelationCache(remittancePlanUuid);
		}
	}

	private void failOperationsForSecondRemittance(String remittanceApplicationUuid, List<String> remittancePlanUuids,
			String executionRemark, boolean unfreezeQuota) {

		iRemittanceApplicationHandler.updateRemittanceInfoAfterSendFailForSecondRemittance(remittanceApplicationUuid,
				remittancePlanUuids, executionRemark);

		remittanceNotifyHandler.processingRemittanceCallback(remittanceApplicationUuid);

		if (unfreezeQuota) {
			unfreezeQuotaForSecondRemittance(remittanceApplicationUuid, remittancePlanUuids);
		}
	}

	private void unfreezeQuotaForSecondRemittance(String remittanceApplicationUuid, List<String> remittancePlanUuids) {
		RemittanceSqlModel remittanceSqlModel = iRemittanceApplicationService
				.getRemittanceSqlModelBy(remittanceApplicationUuid);
		List<RemittancePlanInfo> remittancePlanInfoList = new ArrayList<>();
		for (String remittancePlanUuid : remittancePlanUuids) {
			RemittancePlan remittancePlan = iRemittancePlanService.getUniqueRemittancePlanByUuid(remittancePlanUuid);
			remittancePlanInfoList.add(new RemittancePlanInfo(remittancePlan.getRemittancePlanUuid(),
					remittancePlan.getPlannedTotalAmount(), false));
		}
		remittanceNotifyJobSender.pushJobToCitigroupForUnFreezeQuota(remittanceApplicationUuid,
				remittanceSqlModel.getFinancialContractUuid(), remittancePlanInfoList);
	}

	@Override
	public void receiveCitiGroupAfterSecondQuotaValidation(Map<String, String> receiveParams) {
		RemittanceResponsePacket packet = JsonUtils.parse(
				receiveParams.get(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL), RemittanceResponsePacket.class);
		List<TradeSchedule> tradeSchedules = packet.getTradeSchedules();
		Boolean isSuccess = packet.getIsSuccess();
		String failMessage = packet.getFailMessage();
		String remittanceApplicationUuid = packet.getRemittanceApplicationUuid();

		if (isSuccess == null || CollectionUtils.isEmpty(tradeSchedules)) {
			throw new RuntimeException("isSuccess is null or tradeSchedules is empty");
		}

		RemittanceApplication application = iRemittanceApplicationService
				.getRemittanceApplicationBy(remittanceApplicationUuid);

		try {
			if (isSuccess) {
				processingAndUpdateRemittanceInfo(tradeSchedules, remittanceApplicationUuid, application.getRequestNo(),
						true, false);

			} else {
				List<String> remittancePlanUuids = new ArrayList<>();
				for (TradeSchedule tradeSchedule : tradeSchedules) {
					remittancePlanUuids.add(tradeSchedule.getOutlierTransactionUuid());
				}
				failOperationsForSecondRemittance(remittanceApplicationUuid, remittancePlanUuids, failMessage, false);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			iRemittanceApplicationHandler.deleteAllObjectAndRelationCacheByApplicationUuid(remittanceApplicationUuid);
		}
	}
}
