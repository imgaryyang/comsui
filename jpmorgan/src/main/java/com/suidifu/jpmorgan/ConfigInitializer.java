package com.suidifu.jpmorgan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.suidifu.jpmorgan.entity.PaymentGatewayConfig;
import com.suidifu.jpmorgan.hash.ConsistentHash;
import com.suidifu.jpmorgan.service.PaymentGatewayConfigService;
import com.suidifu.jpmorgan.service.PaymentOrderService;
import com.suidifu.jpmorgan.service.ScheduleJobService;
import com.suidifu.jpmorgan.util.SpringContextUtil;

@Component
public class ConfigInitializer {

	private static final Log logger = LogFactory.getLog(ConfigInitializer.class);
	
	@Autowired
	PaymentGatewayConfigService paymentGatewayConfigService;
	@Autowired
	ScheduleJobService scheduleJobService;
	@Autowired
	SpringContextUtil SpringContextUtil;
	
	@Value("#{config['persistentMode']}")
	private Integer persistentMode;
	
	private PaymentOrderService paymentOrderService;

	private Map<String, ConsistentHash<Integer>> gatewayChannelAndConsistentHashMapper = new HashMap<String, ConsistentHash<Integer>>();

	private Map<String, Map<String, String>> gatewayChannelConfigKeyAndValueMapper = new HashMap<String, Map<String,String>>();
	
	private ConsistentHash<Integer> generalConsistentHashSource;
	
	@PostConstruct
	public void configInitialization() throws Exception {
		
		paymentOrderServiceInitialization();

		gatewayChannelAndConsistentHashInitialize();
		
		gatewayChannelConfigKeyAndValueInitialize();
		
		generalConsistentHashSourceInitialize();

	}
	
	private void paymentOrderServiceInitialization() throws Exception {
		
		if(null == persistentMode) {
			logger.warn("persistentMode is not configed, paymentOrderServiceInitialization error!");
			throw new Exception();
		}
		
		PersistentMode mode = PersistentMode.valueOf(persistentMode);
		
		if(PersistentMode.DB.equals(mode)) {
			this.paymentOrderService = SpringContextUtil.getBean("paymentOrderService");
		}else if (PersistentMode.CACHE_DB.equals(mode)) {
			this.paymentOrderService = SpringContextUtil.getBean("paymentOrderCacheService");
		}else {
			logger.warn("not supported persistentMode, paymentOrderServiceInitialization error!");
			throw new Exception();
		}
	}

	private void gatewayChannelAndConsistentHashInitialize() throws Exception {
		List<Map<String, Object>> gatewayQueueSizeConfigList = paymentGatewayConfigService.loadGatewayQueueSizeConfig();

		if (CollectionUtils.isEmpty(gatewayQueueSizeConfigList)) {
			logger.info("gatewayQueueSizeConfig is empty!");
			throw new Exception();
		}

		for (Map<String, Object> queueSizeConfig : gatewayQueueSizeConfigList) {
			
			String paymentChannelUuid = queueSizeConfig.getOrDefault("payment_channel_uuid", StringUtils.EMPTY).toString();
			
			String queueSizeStr = queueSizeConfig.getOrDefault("config_value", StringUtils.EMPTY).toString();
			
			if(StringUtils.isEmpty(paymentChannelUuid) || StringUtils.isEmpty(queueSizeStr)) {
				logger.warn("paymentChannelUuid or queueSize is not configed, consistentHashInitialization failed!...");
				throw new Exception();
			}
			
			Integer queueSize = Integer.parseInt(queueSizeStr);

			Set<Integer> queueIndexSet = new HashSet<>(queueSize);
		    for (int i = 1; i <= queueSize; i++) {
		    	queueIndexSet.add(i);
		    }
		    ConsistentHash<Integer> consistentHashSource = new ConsistentHash<>(queueIndexSet);
			
		    appendMapper(paymentChannelUuid, consistentHashSource);
		}
	}
	
	private void gatewayChannelConfigKeyAndValueInitialize() throws Exception {
		List<PaymentGatewayConfig> paymentGatewayConfigList = paymentGatewayConfigService.loadAll(PaymentGatewayConfig.class);
		
		if(CollectionUtils.isEmpty(paymentGatewayConfigList)) {
			logger.info("no paymentGatewayConfig!...");
			return;
		}
		
		for(PaymentGatewayConfig config : paymentGatewayConfigList) {
			String paymentChannelUuid = config.getPaymentChannelUuid();
			
			if(!this.gatewayChannelConfigKeyAndValueMapper.containsKey(paymentChannelUuid)) {
				this.gatewayChannelConfigKeyAndValueMapper.put(paymentChannelUuid, new HashMap<String,String>());
			}
			
			Map<String, String> map = this.gatewayChannelConfigKeyAndValueMapper.get(paymentChannelUuid);
			map.put(config.getConfigKey(), config.getConfigValue());
			
			this.gatewayChannelConfigKeyAndValueMapper.put(paymentChannelUuid, map);
		}
		
	}
	
	private void generalConsistentHashSourceInitialize() throws Exception {
		
		int queueSize = scheduleJobService.getEnabledJobCountByJobGroup("distributeJob");

		Set<Integer> queueIndexSet = new HashSet<>(queueSize);
	    for (int i = 1; i <= queueSize; i++) {
	    	queueIndexSet.add(i);
	    }
	    generalConsistentHashSource = new ConsistentHash<>(queueIndexSet);
		
	}

	public void appendMapper(String paymentChannelUuid, ConsistentHash<Integer> consistentHashSource) {
		this.gatewayChannelAndConsistentHashMapper.put(paymentChannelUuid, consistentHashSource);
	}

	public Map<String, ConsistentHash<Integer>> getGatewayChannelAndConsistentHashMapper() {
		return gatewayChannelAndConsistentHashMapper;
	}

	public Map<String, Map<String, String>> getGatewayChannelConfigKeyAndValueMapper() {
		return gatewayChannelConfigKeyAndValueMapper;
	}

	public ConsistentHash<Integer> getGeneralConsistentHashSource() {
		return generalConsistentHashSource;
	}

	public PaymentOrderService getPaymentOrderService() {
		return paymentOrderService;
	}

}
