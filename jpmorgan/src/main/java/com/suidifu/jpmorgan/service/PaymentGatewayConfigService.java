package com.suidifu.jpmorgan.service;

import java.util.List;
import java.util.Map;

import com.demo2do.core.service.GenericService;
import com.suidifu.jpmorgan.entity.PaymentGatewayConfig;
import com.suidifu.jpmorgan.entity.RouteConfigQueryModel;

public interface PaymentGatewayConfigService extends GenericService<PaymentGatewayConfig>{
	
	//List<String> listUrl(GatewayType gatewayType);
	
	String listUrl(String routeConfigUuid);
	
	List<PaymentGatewayConfig> find(RouteConfigQueryModel queryModel);
	
	String getPaymentOrderQueueTableNameBy(String configUuid);
		
	PaymentGatewayConfig getPaymentGatewayConfigBy(String channelUuid);
	
	Map<String, Object> getGatewayConfigByChannelUuid(String paymentChannelUuid);
	
	Map<String, Object> getGatewayConfigByGatewayUuid(String paymentGatewayConfigUuid);
	
	List<Map<String, Object>> loadGatewayQueueSizeConfig();
}
