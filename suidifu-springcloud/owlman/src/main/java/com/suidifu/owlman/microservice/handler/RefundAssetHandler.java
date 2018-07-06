package com.suidifu.owlman.microservice.handler;

import java.util.List;
import java.util.Map;
import com.zufangbao.sun.utils.ClearingVoucherParameters;
import com.zufangbao.sun.yunxin.entity.api.AssetRefundMode;


public interface RefundAssetHandler {
	
	public void handleAssetRefund(String contractUuid, String refundOrderUuid, AssetRefundMode assetRefundMode,
			int priority);
}
