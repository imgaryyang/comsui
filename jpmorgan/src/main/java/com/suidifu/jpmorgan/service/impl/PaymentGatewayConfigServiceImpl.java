package com.suidifu.jpmorgan.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.demo2do.core.service.impl.GenericServiceImpl;
import com.suidifu.jpmorgan.entity.PaymentGatewayConfig;
import com.suidifu.jpmorgan.entity.RouteConfigQueryModel;
import com.suidifu.jpmorgan.service.PaymentGatewayConfigService;

@Service("paymentGatewayConfigService")
public class PaymentGatewayConfigServiceImpl extends GenericServiceImpl<PaymentGatewayConfig> implements PaymentGatewayConfigService{

	private static final String CONFIG_KEY_QUEUESIZE = "queue_size";
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<String> listUrl(GatewayType gatewayType) {
//		
//		List<PaymentGatewayConfig> routeConfigList = genericDaoSupport.searchForList(
//				"from RouteConfig where gatewayType =:gatewayType",
//				"gatewayType", gatewayType);
//		return routeConfigList.stream().map(c->c.getUrl()).collect(Collectors.toList());
//	}


	@Override
	public List<PaymentGatewayConfig> find(RouteConfigQueryModel queryModel) {
		
		Map<String, Object> para = new HashMap<String, Object>();
		StringBuffer sqlString = new StringBuffer("FROM RouteConfig WHERE effectiveDate <=: effectiveDate ");
		para.put("effectiveDate", new Date());
		
		sqlString.append("and gatewayOnlineStatus=:gatewayOnlineStatus ");
		para.put("gatewayOnlineStatus", queryModel.getGatewayOnlineStatus());
		
		sqlString.append("and gatewayWorkingStatus=:gatewayWorkingStatus ");
		para.put("gatewayWorkingStatus", queryModel.getGatewayWorkingStatus());
		
		sqlString.append("and gatewayType=:gatewayType ");
		para.put("gatewayType", queryModel.getGatewayType());
		
		@SuppressWarnings("unchecked")
		List<PaymentGatewayConfig> result = genericDaoSupport.searchForList(sqlString.toString(),para);
		return result;
	}

	@Override
	public String listUrl(String routeConfigUuid) {
		if(StringUtils.isEmpty(routeConfigUuid)) {
			return StringUtils.EMPTY;
		}
		List<String> uuidList = genericDaoSupport.queryForSingleColumnList("SELECT url FROM route_config WHERE uuid =:uuid", "uuid", routeConfigUuid, String.class);
		
		if(CollectionUtils.isEmpty(uuidList)) {
			return StringUtils.EMPTY;
		}
		return uuidList.get(0);
	}

	@Override
	public String getPaymentOrderQueueTableNameBy(String configUuid) {
		if(StringUtils.isEmpty(configUuid)) {
			return StringUtils.EMPTY;
		}
		List<String> tableNameList = genericDaoSupport.queryForSingleColumnList("SELECT payment_order_queue_table_name FROM payment_gateway_config WHERE payment_gateway_uuid =:configUuid", "configUuid", configUuid, String.class);
		if(CollectionUtils.isEmpty(tableNameList)) {
			return StringUtils.EMPTY;
		}
		return tableNameList.get(0);
	}

	@Override
	public PaymentGatewayConfig getPaymentGatewayConfigBy(String channelUuid) {
		if(StringUtils.isEmpty(channelUuid)) {
			return null;
		}
		
		List<PaymentGatewayConfig> configList = genericDaoSupport.searchForList("FROM PaymentGatewayConfig WHERE paymentChannelUuid =:channelUuid", "channelUuid", channelUuid);
		
		if(CollectionUtils.isEmpty(configList)) {
			return null;
		}
		return configList.get(0);
	}


	@Override
	public Map<String, Object> getGatewayConfigByGatewayUuid(String paymentGatewayConfigUuid) {
		Map<String, Object> config = new HashMap<String, Object>();
		if(StringUtils.isEmpty(paymentGatewayConfigUuid)) {
			return config;
		}
		List<PaymentGatewayConfig> configList = genericDaoSupport.queryForList("select * from payment_gateway_config where payment_gateway_uuid =:paymentGatewayConfigUuid", "paymentGatewayConfigUuid", paymentGatewayConfigUuid, PaymentGatewayConfig.class);
		extractConfigInfo(config, configList);
		return config;
	}


	@Override
	public Map<String, Object> getGatewayConfigByChannelUuid(String paymentChannelUuid) {
		Map<String, Object> config = new HashMap<String, Object>();
		if(StringUtils.isEmpty(paymentChannelUuid)) {
			return config;
		}
		List<PaymentGatewayConfig> configList = genericDaoSupport.queryForList("select * from payment_gateway_config where payment_channel_uuid =:paymentChannelUuid", "paymentChannelUuid", paymentChannelUuid, PaymentGatewayConfig.class);
		extractConfigInfo(config, configList);
		return config;
	}
	
	private void extractConfigInfo(Map<String, Object> config, List<PaymentGatewayConfig> configList) {
		if (CollectionUtils.isNotEmpty(configList)) {
			config.put("paymentGatewayUuid", configList.get(0).getPaymentGatewayUuid());
		}
		for(PaymentGatewayConfig gatewayConfig : configList) {
			String configKey = gatewayConfig.getConfigKey();
			String configValue = gatewayConfig.getConfigValue();
			
			if(StringUtils.isEmpty(configKey) || StringUtils.isEmpty(configValue)) {
				continue;
			}
			config.put(configKey, configValue);
		}
	}

	@Override
	public List<Map<String, Object>> loadGatewayQueueSizeConfig() {
		
		return genericDaoSupport.queryForList("select config_value,payment_channel_uuid from payment_gateway_config where config_key =:queueSize", "queueSize", CONFIG_KEY_QUEUESIZE);
		
	}

}
