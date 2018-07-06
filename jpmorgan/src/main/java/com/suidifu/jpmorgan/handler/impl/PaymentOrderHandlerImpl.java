package com.suidifu.jpmorgan.handler.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.jpmorgan.entity.DistributeWorkingContext;
import com.suidifu.jpmorgan.entity.GatewaySlot;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.exception.paymentorder.PaymentOrderListOfNullException;
import com.suidifu.jpmorgan.exception.paymentorder.PaymentOrderOfNullException;
import com.suidifu.jpmorgan.handler.PaymentOrderHandler;
import com.suidifu.jpmorgan.service.PaymentOrderService;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec;
import com.suidifu.jpmorgan.spec.logspec.GloableLogSpec.JPMORGAN_FUNCTION_POINT;
import com.suidifu.jpmorgan.util.JsoupUtils;

@Component("paymentOrderHandler")
public class PaymentOrderHandlerImpl implements PaymentOrderHandler {

	@Autowired
	private PaymentOrderService paymentOrderService;

	private static final Log logger = LogFactory.getLog(PaymentOrderHandlerImpl.class);
	
	@Override
	public void paymentOrderListInitAndSave(PaymentOrder paymentOrder, DistributeWorkingContext workingContext) {
		
		if (null == paymentOrder) {
			throw new PaymentOrderListOfNullException("paymentOrder is null");
		}
		
		//TODO change to controller validate
		if (StringUtils.isEmpty(paymentOrder.getAccountSide()) || StringUtils.isEmpty(paymentOrder.getTransactionAmount()) || StringUtils.isEmpty(paymentOrder.getOutlierTransactionUuid())) {
			throw new PaymentOrderOfNullException();
		}

		paymentOrder.init(workingContext);
		
		paymentOrderService.taskInQueue(paymentOrder, workingContext.getPaymentOrderQueueTableName(), workingContext.getQueueIndex());
		
	}

	@Override
	public GatewaySlot getRealtimeInfo(String paymentOrderUuid, String tableName, String logTableName) {

		PaymentOrder paymentOrder = paymentOrderService.getPaymentOrderBy(paymentOrderUuid, tableName);
		
		if (null == paymentOrder) {
			paymentOrder = paymentOrderService.getPaymentOrderBy(paymentOrderUuid, logTableName);
		}
		if (null == paymentOrder) {
			return null;
		}
		return paymentOrder.transferToGatewaySlot();
	}

	@Override
	public void handleInnerCallback(PaymentOrder readyTask,
			String lobTableName) throws Exception {
		
		PaymentOrder paymentOrder = paymentOrderService.getPaymentOrderBy(readyTask.getUuid(), lobTableName);
		GatewaySlot gatewaySlot = paymentOrder.transferToGatewaySlot();
		
		Map<String, Object> innerCallbackInfo = JSON.parseObject(readyTask.getStringFieldOne(), Map.class);
		String innerCallbackUrl = innerCallbackInfo.getOrDefault("innerCallbackUrl", "").toString();
		
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("tradeUuid", innerCallbackInfo.getOrDefault("tradeUuid", "").toString());
		data.put("gatewaySlot", JsonUtils.toJsonString(gatewaySlot));
		data.put("nthSlot", (int)innerCallbackInfo.get("nthSlot"));
		data.put("gatewayName", innerCallbackInfo.getOrDefault("gatewayName", "").toString());
		
		logger.info(GloableLogSpec.AuditLogHeaderSpec() + JPMORGAN_FUNCTION_POINT.INNER_CALLBACK_BIGIN_SEND + readyTask.getUuid());
		
		JsoupUtils.post(innerCallbackUrl, data);
		
	}

}
