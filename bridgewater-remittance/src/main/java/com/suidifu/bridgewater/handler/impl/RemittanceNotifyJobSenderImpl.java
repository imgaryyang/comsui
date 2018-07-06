package com.suidifu.bridgewater.handler.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.bridgewater.handler.RemittanceNotifyJobSender;
import com.suidifu.bridgewater.notify.server.RemittanceNotifyJobServer;
import com.suidifu.swift.notifyserver.notifyserver.NotifyApplication;
import com.suidifu.swift.notifyserver.notifyserver.notifyenum.RoutingKeyType;
import com.zufangbao.gluon.api.jpmorgan.model.TradeSchedule;
import com.zufangbao.sun.api.model.remittance.RemittanceCommandModel;
import com.zufangbao.sun.api.model.remittance.RemittancePlanInfo;
import com.zufangbao.sun.api.model.remittance.RemittanceResponsePacket;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec;
import com.zufangbao.sun.zhonghang.v2.ZhonghangResponseMapSpec.Transfer;

/**
 * 通过notifyServer发送message
 *
 * @author wsh
 */
@Component("remittanceNotifyJobSender")
public class RemittanceNotifyJobSenderImpl implements RemittanceNotifyJobSender {

	@Autowired
	private RemittanceNotifyJobServer remittanceNotifyJobServer;

	/**
	 * 样例："remittance/business-validation/yunxin"
	 */
	@Value("#{config['business.verify.business.type']}")
	private String businessVerifyBusinessType;
	
	/**
	 * 样例："remittance/transfer-validation/yunxin"
	 */
	@Value("#{config['transfer.verify.business.type']}")
	private String transferVerifyBusinessType;
	
	@Value("#{config['citigroup.notify.mq.business.validation.url']}")
	private String citigroupBusinessValidationUrl;

	@Value("#{config['remittance.notify.mq.after.business.validation.url']}")
	private String afterBusinessValidationUrl;
	
	@Value("#{config['citigroup.notify.mq.quota.validation.url']}")
	private String citigroupQuotaValidationUrl;
	
	@Value("#{config['remittance.notify.mq.after.quota.validation.url']}")
	private String afterQuotaValidationUrl;
	
	@Value("#{config['remittance.notify.mq.after.resend.quota.validation.url']}")
	private String afterResendQuotaValidationUrl;
	
	@Value("#{config['remittance.notify.mq.after.second.quota.validation.url']}")
	private String afterSecondQuotaValidationUrl;
	
	@Value("#{config['citigroup.notify.mq.freeze.quota.url']}")
	private String citigroupFreezeQuotaUrl;
	
	@Value("#{config['notifyserver.groupCacheJobQueueMapForMq']}")
	private String groupCacheJobQueueMapForMqStr;
			
	private static Log logger = LogFactory.getLog(RemittanceNotifyJobSenderImpl.class);
	
	private boolean isExclusive(String financialContractUuid) {
		
		Map<String, Integer> groupCacheJobQueueMapForMq = JSON.parseObject(groupCacheJobQueueMapForMqStr,new TypeReference<Map<String, Integer>>(){});
    	
		if(StringUtils.isEmpty(financialContractUuid) || MapUtils.isEmpty(groupCacheJobQueueMapForMq) || !groupCacheJobQueueMapForMq.containsKey(financialContractUuid)) {
			return false;
		}
		return true;
	}

	private void pushJob(NotifyApplication notifyApplication, String financialContractUuid) {
		if (isExclusive(financialContractUuid)) {
			notifyApplication.setGroupName(financialContractUuid);
		}
		remittanceNotifyJobServer.pushJob(notifyApplication);
	}
	
	@Override
	public void pushJobToCitigroupForBusinessValidation(RemittanceCommandModel model) {
		
		RemittanceResponsePacket packet = new RemittanceResponsePacket();
		packet.setRemittanceCommandModel(model);
		packet.setBusinessType(Transfer.isTransfer(model.getTransactionType()) ? transferVerifyBusinessType.trim() : businessVerifyBusinessType.trim());
		packet.setCallBackUrl(afterBusinessValidationUrl);
		HashMap<String, String> responsePacket = new HashMap<String,String>(){
			{
				put(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL, JsonUtils.toJsonString(packet));
			}			
		};
		
		NotifyApplication notifyApplication = new NotifyApplication();
		
		notifyApplication.setRequestUrl(citigroupBusinessValidationUrl);
		notifyApplication.setRequestParameters(responsePacket);
		
		pushJob(notifyApplication, null);
		
	}

	@Override
	public void pushJobToCitigroupForQuotaValidation(String remittanceApplicationUuid,String financialContractUuid, List<RemittancePlanInfo> remittancePlanInfoList,RemittanceCommandModel model,String checkRequestNo, List<TradeSchedule> tradeSchedules) {
		pushJobToCitigroupForQuotaValidation(remittanceApplicationUuid, financialContractUuid, remittancePlanInfoList, tradeSchedules, model, checkRequestNo, afterQuotaValidationUrl);
	}

	@Override
	public void pushJobToCitigroupForResendQuotaValidation(String remittanceApplicationUuid ,String financialContractUuid, RemittancePlanInfo remittancePlanInfo, TradeSchedule tradeSchedule) {
		pushJobToCitigroupForQuotaValidation(remittanceApplicationUuid, financialContractUuid,Arrays.asList(remittancePlanInfo),Arrays.asList(tradeSchedule), null , null,afterResendQuotaValidationUrl);
	}
	
	@Override
	public void pushJobToCitigroupForSecondRemittanceQuotaValidation(String remittanceApplicationUuid ,String financialContractUuid, List<RemittancePlanInfo> remittancePlanInfos, List<TradeSchedule> tradeSchedules) {
		pushJobToCitigroupForQuotaValidation(remittanceApplicationUuid, financialContractUuid,remittancePlanInfos,tradeSchedules, null , null,afterSecondQuotaValidationUrl);
	}
	
	private void pushJobToCitigroupForQuotaValidation(String remittanceApplicationUuid,String financialContractUuid, List<RemittancePlanInfo> remittancePlanInfoList,List<TradeSchedule> tradeSchedules,RemittanceCommandModel model, String checkRequestNo,String callBackUrl) {
		RemittanceResponsePacket packet = new RemittanceResponsePacket();
		packet.setRemittanceCommandModel(model);
		packet.setCheckRequestNo(checkRequestNo);
		packet.setRemittancePlanInfoList(remittancePlanInfoList);
		packet.setFinancialContractUuid(financialContractUuid);
		packet.setRemittanceApplicationUuid(remittanceApplicationUuid);
		packet.setTradeSchedules(tradeSchedules);
		packet.setCallBackUrl(callBackUrl);

		HashMap<String, String> responsePacket = new HashMap<String,String>(){
			{
				put(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL, JsonUtils.toJsonString(packet));
			}			
		};
		
		NotifyApplication notifyApplication = new NotifyApplication();
		
		notifyApplication.setRequestUrl(citigroupQuotaValidationUrl);
		notifyApplication.setRequestParameters(responsePacket);
		notifyApplication.setConsistenceHashPolicy(financialContractUuid);
		notifyApplication.setMqRoutingKey(RoutingKeyType.CONSISTENT_HASH);
		
		pushJob(notifyApplication, financialContractUuid);
	}
	
	@Override
	public void pushJobToCitigroupForUnFreezeQuota(String remittanceApplicationUuid ,String financialContractUuid, List<RemittancePlanInfo> remittancePlanInfoList) {
		pushJobToCitigroupForUnFreezeQuota(remittanceApplicationUuid, financialContractUuid, remittancePlanInfoList, citigroupFreezeQuotaUrl);
	}
	
	@Override
	public void pushJobToCitigroupForResendUnFreezeQuota(String remittanceApplicationUuid ,String financialContractUuid, RemittancePlanInfo remittancePlanInfo) {
		pushJobToCitigroupForUnFreezeQuota(remittanceApplicationUuid, financialContractUuid, Arrays.asList(remittancePlanInfo), citigroupFreezeQuotaUrl);
	}
	
	@Override
	public void pushJobToCitigroupForSecondUnFreezeQuota(String remittanceApplicationUuid ,String financialContractUuid, List<RemittancePlanInfo> remittancePlanInfoList) {
		pushJobToCitigroupForUnFreezeQuota(remittanceApplicationUuid, financialContractUuid, remittancePlanInfoList, citigroupFreezeQuotaUrl);
	}

	private void pushJobToCitigroupForUnFreezeQuota(String remittanceApplicationUuid ,String financialContractUuid, List<RemittancePlanInfo> remittancePlanInfoList, String callBackUrl) {
		RemittanceResponsePacket packet = new RemittanceResponsePacket();
		packet.setRemittancePlanInfoList(remittancePlanInfoList);
		packet.setFinancialContractUuid(financialContractUuid);
		packet.setRemittanceApplicationUuid(remittanceApplicationUuid);

		HashMap<String, String> responsePacket = new HashMap<String,String>(){
			{
				put(ZhonghangResponseMapSpec.REMITTANCEREQUESTMODEL, JsonUtils.toJsonString(packet));
			}			
		};
		
		NotifyApplication notifyApplication = new NotifyApplication();
		
		notifyApplication.setRequestUrl(callBackUrl);
		notifyApplication.setRequestParameters(responsePacket);
		notifyApplication.setConsistenceHashPolicy(financialContractUuid);
		notifyApplication.setMqRoutingKey(RoutingKeyType.CONSISTENT_HASH);
		
		pushJob(notifyApplication, financialContractUuid);
	}
}







