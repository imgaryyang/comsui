package com.suidifu.bridgewater.handler.impl;

import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.notify.server.RemittanceAsyncNotifyHandler;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component("yXRemittanceAsyncNotifyHandler")
public class YXRemittanceAsyncNotifyHandlerImpl extends RemittanceAsyncNotifyHandler {

	@Autowired
	private IRemittanceApplicationHandler iRemittanceApplicationHandler;

	@Override
	public String buildContent(RemittanceSqlModel remittanceSqlMode, String remittanceApplicationUuid) {
		return iRemittanceApplicationHandler.getRemittanceResultsForBatchNotice(remittanceSqlMode, Arrays.asList(remittanceApplicationUuid));
	}

	@Override
	public HashMap<String, String> buildHeaderParamsForNotifyRemittanceResult(String content, String financialContractUuid) {
		return (HashMap) iRemittanceApplicationHandler.buildHeaderParamsForNotifyRemittanceResult(content);
	}

	@Override
	public List<String> getWaitingNoticeRemittanceApplicationBy(int limit, Date queryStartDate, Date notifyOutlierDelay) {

		return iRemittanceApplicationHandler.getWaitingNoticeRemittanceApplicationV2(limit, queryStartDate, notifyOutlierDelay);
	}
}






