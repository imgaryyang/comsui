package com.suidifu.pricewaterhouse.yunxin.task;

import com.zufangbao.wellsfargo.silverpool.cashauditing.handler.AutoRecoverAssesNoSession;
import com.zufangbao.wellsfargo.yunxin.handler.RepaymentOrderHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component("assetRefundTask")
public class AssetRefundTask{


	private static Log logger = LogFactory.getLog(AssetRefundTask.class);
	
	@Autowired
	private AutoRecoverAssesNoSession autoRecoverAssesNoSession;
	
	/**
	 *  系统自动   资产退款  
	 */
	public void asset_refund_auto_system(){
		try {
			logger.info("asset_refund_auto_system start");
			autoRecoverAssesNoSession.system_auto_asset_refund();
		} catch(Exception e){
			logger.error("asset_refund_auto_system error");
			e.printStackTrace();
		}
		logger.info("asset_refund_auto_system end");
	}
}
