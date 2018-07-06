package com.suidifu.berkshire.mq.rpc.proxy.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.suidifu.hathaway.mq.annotations.v2.MicroService;
import com.suidifu.owlman.microservice.handler.RefundAssetHandler;
import com.zufangbao.sun.yunxin.entity.api.AssetRefundMode;
import com.zufangbao.wellsfargo.yunxin.handler.RefundOrderHandler;

@Component("assetRefundNewSyncProxy")
public class AssetRefundNewSyncProxy  implements RefundAssetHandler{

	@Autowired
	private RefundOrderHandler RefundOrderHandler;

	@Override
	@MicroService(beanName="refundAssetHandler",
			methodName="handleAssetRefund",
			sync=true,
			vhostName="/business",
			exchangeName="exchange-business",
			routingKey = "refund-asset")
	public void handleAssetRefund(String contractUuid, String refundOrderUuid, AssetRefundMode assetRefundMode,
			int priority) {
		
		RefundOrderHandler.handleAssetRefund(contractUuid, refundOrderUuid, assetRefundMode, priority);
		
	}
	
}
