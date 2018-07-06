package com.suidifu.barclays.entity;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo2do.core.utils.JsonUtils;
import com.demo2do.core.utils.StringUtils;

public class ChannelWorkerConfig {

	private Long id;

	private String workerUuid;

	private String channelIdentity;

	private String localWorkingConfig;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWorkerUuid() {
		return workerUuid;
	}

	public void setWorkerUuid(String workerUuid) {
		this.workerUuid = workerUuid;
	}

	public String getChannelIdentity() {
		return channelIdentity;
	}

	public void setChannelIdentity(String channelIdentity) {
		this.channelIdentity = channelIdentity;
	}

	public String getLocalWorkingConfig() {
		return localWorkingConfig;
	}

	public void setLocalWorkingConfig(String localWorkingConfig) {
		this.localWorkingConfig = localWorkingConfig;
	}
	
}
