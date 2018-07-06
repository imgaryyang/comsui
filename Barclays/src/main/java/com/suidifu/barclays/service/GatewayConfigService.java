package com.suidifu.barclays.service;

import java.util.Map;

public interface GatewayConfigService {
	Map<String, String> getByChannelIdentityAndMerchantNo(String gatewayName,String merchantno);
}
