package com.suidifu.bridgewater.handler.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.StringUtils;
import com.suidifu.bridgewater.handler.IRemittanceApplicationHandler;
import com.suidifu.bridgewater.notify.server.RemittanceAsyncNotifyHandler;
import com.zufangbao.gluon.util.SecurityUtil;
import com.zufangbao.sun.api.model.remittance.RemittanceSqlModel;
import com.zufangbao.sun.yunxin.entity.api.TMerConfig;
import com.zufangbao.sun.yunxin.service.TMerConfigService;

@Component("hRRemittanceAsyncNotifyHandler")
public class HRRemittanceAsyncNotifyHandlerImpl extends RemittanceAsyncNotifyHandler{

	@Autowired
	private IRemittanceApplicationHandler iRemittanceApplicationHandler;
	
	@Autowired
	private TMerConfigService tMerConfigService;
	
	@Override
	public List<String> getWaitingNoticeRemittanceApplicationBy(int pageSize, Date queryStartDate, Date notifyOutlierDelay) {
		return iRemittanceApplicationHandler.getWaitingNoticeRemittanceApplicationV3(pageSize,queryStartDate, notifyOutlierDelay);
	}

	@Override
	public String buildContent(RemittanceSqlModel remittanceSqlMode, String remittanceApplicationUuid) {
		String content = iRemittanceApplicationHandler.getRemittanceResultsForBatchNotice(remittanceSqlMode,Arrays.asList(remittanceApplicationUuid));
		String merId = remittanceSqlMode.getCreatorName();
		TMerConfig merConfig = tMerConfigService.getTMerConfigByMerId(merId);
		String contentWithEncryption = StringUtils.EMPTY;
		try {
			contentWithEncryption = SecurityUtil.threeDesEncrypt(content, merConfig.getEncryptionKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contentWithEncryption;
	}

	@Override
	public HashMap<String, String> buildHeaderParamsForNotifyRemittanceResult(String content,
			String financialContractUuid) {
		Map<String, String> buildHeaderParamsForNotifyRemittanceResult = iRemittanceApplicationHandler.buildHeaderParamsForNotifyRemittanceResult(content);
		return (HashMap<String, String>)buildHeaderParamsForNotifyRemittanceResult;
	}

}
