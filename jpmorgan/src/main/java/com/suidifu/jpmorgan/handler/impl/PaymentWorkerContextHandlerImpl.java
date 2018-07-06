package com.suidifu.jpmorgan.handler.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.jpmorgan.entity.PaymentOrderWorkerConfig;
import com.suidifu.jpmorgan.entity.PaymentWorkerContext;
import com.suidifu.jpmorgan.exception.PaymentWorkerException;
import com.suidifu.jpmorgan.factory.PaymentHandlerFactory;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.handler.PaymentWorkerContextHanlder;
import com.suidifu.jpmorgan.service.PaymentGatewayConfigService;
import com.suidifu.jpmorgan.service.PaymentOrderWorkerConfigService;
import com.suidifu.jpmorgan.spec.TradeScheduleHandlerSpec;

@Component
public class PaymentWorkerContextHandlerImpl implements PaymentWorkerContextHanlder {

	
	@Autowired 
	PaymentOrderWorkerConfigService wokerConfigure;
	@Autowired 
	PaymentGatewayConfigService gateWayConfigure;
	
	private static Log logger = LogFactory.getLog(PaymentWorkerContextHandlerImpl.class);


	@Override
	public PaymentWorkerContext buildWorkerContext(String workerUuid)throws PaymentWorkerException {
		PaymentOrderWorkerConfig workerConfig = wokerConfigure.getPaymentOrderWorkerConfigBy(workerUuid);
		
		if(workerConfig == null) {
			logger.info("initialization failed cannot get workerConfigure for "+workerUuid);
			throw new PaymentWorkerException();
		}
		
		Map<String, Object> gatewayConfig = gateWayConfigure.getGatewayConfigByGatewayUuid(workerConfig.getPaymentGatewayUuid());
		String queueTableName = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.PaymentOrderQueueTableNameKey, StringUtils.EMPTY).toString();
		String logTableName = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.PaymentOrderLogTableNameKey, StringUtils.EMPTY).toString();
		String gatewayName = gatewayConfig.getOrDefault(TradeScheduleHandlerSpec.GatewayNameKey, StringUtils.EMPTY).toString();
		
		if(StringUtils.isEmpty(queueTableName) || StringUtils.isEmpty(logTableName)) {
			logger.info("initialization failed cannot located table name");
			throw new PaymentWorkerException();
		}
		
		PaymentHandler hanlder = PaymentHandlerFactory.newInstance(gatewayName);
		if(hanlder == null) {
			logger.info("the gatewayInfo is {}" + gatewayConfig.toString());
			logger.info("initialization failed cannot create PaymentHanlder for "+workerUuid);
			throw new PaymentWorkerException();
		}
		
		PaymentWorkerContext context = new PaymentWorkerContext(workerConfig, queueTableName, logTableName, hanlder, gatewayName);
		return context;
	}
}
