package com.suidifu.barclays.factory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.suidifu.barclays.handler.TransactionRecordHandler;
import com.zufangbao.sun.utils.SpringContextUtil;

public class TransactionRecordHandlerFactory {

	public static String CHANNEL_IDENTITY_THIRDPARTY_SINAPAY="CHANNEL_IDENTITY_THIRDPARTY_SINAPAY";
	
	public static String CHANNEL_IDENTITY_THIRDPARTY_GZUION = "CHANNEL_IDENTITY_THIRDPARTY_GZUION";
	
	private final static Map<String, String> channelIdentityTransactionRecordHandlerBeanNameMapper = new HashMap<String, String>() {

		{
			put(CHANNEL_IDENTITY_THIRDPARTY_SINAPAY, "sinapayTransactionRecordHandler");
			put("CHANNEL_IDENTITY_THIRDPARTY_GZUION", "gzUionTransactionRecordHandler");
		}
	};
	
	public static <T extends TransactionRecordHandler> T newInstance(String channelIdentity) {
		String beanName = channelIdentityTransactionRecordHandlerBeanNameMapper.getOrDefault(
				channelIdentity, "");
		if(StringUtils.isEmpty(beanName)) return null;
		return SpringContextUtil.getBean(beanName);
	}
}
