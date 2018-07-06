package com.suidifu.bridgewater.handler.impl;

import com.suidifu.bridgewater.handler.remittance.v2.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.handler.v2.ZhongHangRemittanceApplicationHandler;
import com.suidifu.bridgewater.notify.server.RemittanceAsyncNotifyHandler;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * zh实现构造请求头和请求体
 *
 * @author wsh
 */
@Component("zHRemittanceAsyncNotifyHandler")
public class ZHRemittanceAsyncNotifyHandlerImpl extends RemittanceAsyncNotifyHandler {


	@Autowired
	private IRemittanceApplicationHandler iRemittanceApplicationHandler;

	@Autowired
	private ZhongHangRemittanceApplicationHandler zhongHangRemittanceApplicationHandler;


	@Override
	public String buildContent(RemittanceSqlModel remittanceSqlMode, String remittanceApplicationUuid) {

		return zhongHangRemittanceApplicationHandler.getRemittanceResultsForBatchNotice(remittanceSqlMode, remittanceApplicationUuid);
	}

	@Override
	public HashMap<String, String> buildHeaderParamsForNotifyRemittanceResult(String content, String financialContractUuid) {
		return (HashMap) zhongHangRemittanceApplicationHandler.buildHeaderParamsForNotifyRemittanceResult(content, financialContractUuid);
	}

	@Override
	public List<String> getWaitingNoticeRemittanceApplicationBy(int limit, Date queryStartDate, Date notifyOutlierDelay) {

		return iRemittanceApplicationHandler.getWaitingNoticeRemittanceApplicationV2(limit, queryStartDate, notifyOutlierDelay);
	}

}
















