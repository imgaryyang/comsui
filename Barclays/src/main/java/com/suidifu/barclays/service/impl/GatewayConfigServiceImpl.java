package com.suidifu.barclays.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.persistence.GenericDaoSupport;
import com.suidifu.barclays.entity.ChannelWorkerConfig;
import com.suidifu.barclays.service.GatewayConfigService;

@Component(value = "gatewayConfigService")
public class GatewayConfigServiceImpl implements GatewayConfigService{
	@Autowired private GenericDaoSupport genericDaoSupport;
	
	@Override
	public Map<String, String> getByChannelIdentityAndMerchantNo(String channelIdentity,String merchantno) {
		Map<String, String> config = Collections.EMPTY_MAP;
		if (StringUtils.isBlank(channelIdentity)||StringUtils.isBlank(merchantno)) {
			return config;
		}
		String getSql = "select local_working_config localWorkingConfig"
				+ " from channel_worker_config where channel_identity = :channelIdentity and merchant_no = :merchantno";
		
		Map<String, Object> paramMap = new HashMap<>(2);
		paramMap.put("channelIdentity", channelIdentity);
		paramMap.put("merchantno", merchantno);
		List<ChannelWorkerConfig> configList = 
				genericDaoSupport.queryForList(getSql, paramMap, ChannelWorkerConfig.class);
		
		if (CollectionUtils.isNotEmpty(configList)) {
			return toMap(configList.get(0).getLocalWorkingConfig());
		}else {
			return config;
		}
	}
	
	private Map<String, String> toMap(String localWorkingConfig) {
		try {
			if (StringUtils.isEmpty(localWorkingConfig)) {
				new HashMap<String, String>();
			}
			return JSON.parseObject(localWorkingConfig, new TypeReference<Map<String,String> >(){});
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, String>();
		}
	}

}
