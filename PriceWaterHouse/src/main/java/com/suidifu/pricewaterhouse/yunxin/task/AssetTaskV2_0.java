package com.suidifu.pricewaterhouse.yunxin.task;

import com.suidifu.pricewaterhouse.yunxin.handler.AssetTaskHandler;
import com.zufangbao.sun.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 资产评估
 * 
 * @author louguanyang
 *
 */
@Component()
public class AssetTaskV2_0{

	@Autowired
	private AssetTaskHandler assetTaskHandler;

	private static Log logger = LogFactory.getLog(AssetTaskV2_0.class);
	
	/**
	 * 结束昨日的工作，开始今日的工作
	 * 评估2
	 */
	public void endYesterdayWorkAndStartTodayWork() {
//		logger.info("#endYesterdayWorkAndStartTodayWork start!");
		assetTaskHandler.handleAssetValuationAndCreateNormalOrder(DateUtils.asDay(new Date()));
//		logger.info("#endYesterdayWorkAndStartTodayWork end!");
	}
	
}
