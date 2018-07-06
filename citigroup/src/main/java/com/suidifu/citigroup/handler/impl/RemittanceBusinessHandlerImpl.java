package com.suidifu.citigroup.handler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.JsonUtils;
import com.suidifu.citigroup.handler.RemittanceBusinessHandler;
import com.suidifu.citigroup.service.TableCacheService;
import com.suidifu.matryoshka.prePosition.PrePositionHandler;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.NotifyJobServer;
import com.suidifu.swift.notifyserver.notifyserver.configurations.NotifyServerConfig;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.gluon.exception.ApiException;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.gluon.util.ApiMessageUtil;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittanceResponsePacket;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.yunxin.entity.model.SystemTraceLog;
import com.zufangbao.sun.yunxin.entity.remittance.RemittanceStrategy;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.EVENT_NAME;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SEND_RECEIVE_CODE_FOR_REMITTANCE;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.SYSTEM_NAME;
import com.zufangbao.wellsfargo.deduct.handler.RemittancePlanAndScheduleHandler;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.Transfer;

@Component("remittanceBusinessHandler")
public class RemittanceBusinessHandlerImpl implements RemittanceBusinessHandler {
	
	private static Log LOG = LogFactory.getLog(RemittanceBusinessHandlerImpl.class);

	@Autowired
	private PrePositionHandler prePositionHandler;
	
	@Autowired
	private NotifyJobServer fileJobServer;
	
	@Autowired
	private NotifyServerConfig notifyServerConfig;

	@Autowired
	private RemittancePlanAndScheduleHandler remittancePlanAndScheduleHandler;
	
	
	@Autowired
	private TableCacheService tableCacheService;
	
	
	@Override
	public void handleRemittanceBusiness(String preProcessUrl, RemittanceCommandModel remittanceRequestModel, String ip, String callBackUrl) {
		
		SystemTraceLog systemTraceLog = new SystemTraceLog();
		
		String failMsg = "";
		
		List<TradeSchedule> tradeSchedules = new ArrayList<>();
		
		FinancialContract financialContract = null;
		
		boolean isSuccess = true;
		
		boolean isNeedDoBalance = true;

		boolean inNeedToPushJob = true;
		try {
			String eventKey = "checkRequestNo:"+remittanceRequestModel.getCheckRequestNo() + "&remittanceApplicationUuid:" + remittanceRequestModel.getRemittanceApplicationUuid();

			systemTraceLog = new SystemTraceLog(EVENT_NAME.MORGANSTANLEY_DEALING_REMITTANCE_REQUEST_START,
					eventKey, null, null,
					SystemTraceLog.INFO, ip, SYSTEM_NAME.REMITTANCE_SYSTEM, SYSTEM_NAME.CITIGROUP);
			
			LOG.info(systemTraceLog.toString());
			
			HashMap<String,String> preRequestParams = preparePreRequestParams(remittanceRequestModel);
			
			HashMap<String, String> DelayPostRequestParams = new HashMap<>();
			
			// 业务校验
			int errorCode = prePositionHandler.prePositionDefaultTaskHandler(
					preProcessUrl, preRequestParams, DelayPostRequestParams, LOG);

			if (errorCode != ApiResponseCode.SUCCESS) {

				failMsg  = DelayPostRequestParams.get("errMsg");
				
				isSuccess=false;
				
				LOG.info(buildSystemTraceLogInError(systemTraceLog, "系统异常："+failMsg));
				

				return;
			}
			
			financialContract = JsonUtils.parse(DelayPostRequestParams.get(ZhonghangResponseMapSpec.FINANCIAL_CONTRACT), FinancialContract.class);

			if(Transfer.isTransfer(remittanceRequestModel.getTransactionType())) {
				isNeedDoBalance = false;
			}else {
				isNeedDoBalance = tableCacheService.validFinancialContractIsInBalance(financialContract.getFinancialContractUuid());
			}
			
			//根据策略拆分出交易日程列表
			tradeSchedules = splitRemittancePlanByStrategy(remittanceRequestModel, financialContract.getFinancialContractUuid());
		
		} catch (ApiException e) {
			e.printStackTrace();

			failMsg = ApiMessageUtil.getMessage(e.getCode());

			isSuccess = false;

			LOG.error(buildSystemTraceLogInError(systemTraceLog, "校验失败：" + failMsg));

		} catch (Exception e) {

			e.printStackTrace();

			LOG.error(buildSystemTraceLogInError(systemTraceLog, "系统异常：" + e.getMessage()));

			isSuccess = false;

			inNeedToPushJob = false;
		} finally {
			if (inNeedToPushJob) {
				sendTradeScheduleToRemittance(fileJobServer, tradeSchedules, failMsg,
						isSuccess, remittanceRequestModel, financialContract,callBackUrl,isNeedDoBalance);
			}
		}

	}
	
	
	private String buildSystemTraceLogInError(SystemTraceLog systemTraceLog,String errorMsg){
		
		systemTraceLog.setEventLevel(SystemTraceLog.ERROR);
		
		systemTraceLog.setEventName(EVENT_NAME.MORGANSTANLEY_DEALING_REMITTANCE_REQUEST);
		
		systemTraceLog.setErrorMsg(errorMsg);
		
		return systemTraceLog.toString();
		
	}
	
	
	private  NotifyApplication sendTradeScheduleToRemittance(NotifyJobServer fileJobServer,List<TradeSchedule> tradeSchedules,String failMsg,Boolean isSuccess,RemittanceCommandModel remittanceRequestModel,FinancialContract financialContract,String callBackUrl,boolean isNeedDoBalance){

		NotifyApplication notifyJobToRemittance = buildNotifyApplication(tradeSchedules, failMsg, isSuccess, remittanceRequestModel,financialContract,callBackUrl,isNeedDoBalance);
		
		pushJob(notifyJobToRemittance,financialContract);
		
		return notifyJobToRemittance;
	}
	
	
	private NotifyApplication buildNotifyApplication(List<TradeSchedule> tradeSchedules,String failMsg,Boolean isSuccess,RemittanceCommandModel remittanceRequestModel,FinancialContract financialContract,String callBackUrl,boolean isNeedDoBalance){
		NotifyApplication notifyJobToRemittance = new NotifyApplication();
		RemittanceResponsePacket packet  = new RemittanceResponsePacket();
		packet.setIsSuccess(isSuccess);
		packet.setRemittanceCommandModel(remittanceRequestModel);
		packet.setFailMessage(failMsg);
		packet.setTradeSchedules(tradeSchedules);
		packet.setFinancialContract(financialContract);
		packet.setIsNeedDoBalance(isNeedDoBalance);
		HashMap<String, String> responsePacket = new HashMap<>();
		responsePacket.put(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL, JsonUtils.toJsonString(packet));
		
		notifyJobToRemittance.setRequestParameters(responsePacket);
		notifyJobToRemittance.setRequestUrl(callBackUrl);
		
		return notifyJobToRemittance;
	}
	
	private HashMap<String, String> preparePreRequestParams(
			RemittanceCommandModel remittanceRequestModel) {
		
		HashMap<String,String> parameters = new HashMap<String,String>();
		
		parameters.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.REQUEST_NO, remittanceRequestModel.getRequestNo());
		
		parameters.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.UNIQUE_ID, remittanceRequestModel.getUniqueId());
		
		parameters.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.PRODUCT_CODE, remittanceRequestModel.getProductCode());
		
		parameters.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.REMITTANCE_DETAILS, JsonUtils.toJsonString(remittanceRequestModel.getRemittanceDetailListFromJsonString()));
		
		parameters.put(SEND_RECEIVE_CODE_FOR_REMITTANCE.REMITTANCE_ID, remittanceRequestModel.getRemittanceId());
		
		return parameters;
	}
	
	/**
	 * 通过策略拆分放款单
	 */
	private List<TradeSchedule> splitRemittancePlanByStrategy(RemittanceCommandModel commandModel, String financialContractUuid) {
		RemittanceStrategy remittanceStrategy = commandModel.getEnumRemittanceStrategy();
		switch (remittanceStrategy) {
		case MULTIOBJECT:
			return remittancePlanAndScheduleHandler.buildTradeScheduleForStrategyMultiobject(commandModel, financialContractUuid);
		case DEDUCT_AFTER_REMITTANCE:
			return remittancePlanAndScheduleHandler.buildTradeScheduleForStrategyDeductAfterRemittance(commandModel, financialContractUuid);
		default:
			throw new ApiException(ApiResponseCode.INVALID_PARAMS, "放款策略［remittanceStrategy］，不存在！");
		}
	}
	
	private boolean isExclusive(String financialContractUuid) {
		
		Map<String, Integer> groupCacheJobQueueMap = notifyServerConfig.getGroupCacheJobQueueMapForMq();
		if(StringUtils.isEmpty(financialContractUuid) || MapUtils.isEmpty(groupCacheJobQueueMap) || !groupCacheJobQueueMap.containsKey(financialContractUuid)) {
			return false;
		}
		return true;
	}

	private void pushJob(NotifyApplication notifyApplication, FinancialContract financialContract) {
		
		String financialContractUuid = null==financialContract?StringUtils.EMPTY:financialContract.getFinancialContractUuid();
		if (isExclusive(financialContractUuid)) {
			notifyApplication.setGroupName(financialContractUuid);
		}
		fileJobServer.pushJob(notifyApplication);
	}
}
