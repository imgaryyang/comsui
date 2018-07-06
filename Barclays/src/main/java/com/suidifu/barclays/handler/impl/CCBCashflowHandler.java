package com.suidifu.barclays.handler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullCashflowException;
import com.suidifu.barclays.handler.CashFlowGenericHandler;
import com.suidifu.barclays.handler.CashflowHandler;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.GlobalSpec.BankCorpEps;
import com.suidifu.coffer.entity.CashFlowResult;
import com.suidifu.coffer.entity.CashFlowResultModel;
import com.suidifu.coffer.entity.QueryCashFlowModel;
import com.suidifu.coffer.handler.ccb.CCBDirectBankHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;

@Component("ccbCashflowHandler")
public class CCBCashflowHandler extends CashFlowGenericHandler implements CashflowHandler {

	private static Log logger = LogFactory.getLog(CCBCashflowHandler.class);

	@Autowired
	CCBDirectBankHandler ccbDirectBankHandler;
	@Autowired
	ChannelWorkerConfigService channelWorkerConfigService;
	
	@Override
	public List<CashFlow> execPullCashflow(ChannelWorkerConfig channelWorkerConfig)
			throws PullCashflowException {

		Map<String, String> workingParms = channelWorkerConfig.getLocalWorkingConfig();
		
		if(null == workingParms) {
			throw new PullCashflowException();
		}
		
		String queryAccountNo = workingParms.getOrDefault("channelAccountNo", StringUtils.EMPTY);
		if(StringUtils.isEmpty(queryAccountNo)) {
			logger.warn("channelAccountNo is not configed,pull cashflow failed!...");
			throw new PullCashflowException();
		}
		
		String pageSize = workingParms.getOrDefault("pageSize", BankCorpEps.CCB_DEFAULT_PAGESIZE);
		String pageNo = workingParms.getOrDefault("nextPageNo", "1");
		String requestSn = DateUtils.format(new Date(), "yyyyMMddHHmmss");
		
		QueryCashFlowModel queryCashFlowModel = new QueryCashFlowModel(queryAccountNo, DateUtils.format(new Date(),"yyyyMMdd"), DateUtils.format(new Date(),"yyyyMMdd"), pageNo, pageSize, requestSn);
		
		CashFlowResultModel cashFlowResultModel = ccbDirectBankHandler.queryIntradayCashFlow(queryCashFlowModel, workingParms);
		
		if(GlobalSpec.DEFAULT_FAIL_CODE.equals(cashFlowResultModel.getCommCode())) {
			logger.warn("failed to pull cashflow accountNo[" + queryAccountNo + "],message:" + cashFlowResultModel.getErrMsg());
		}
		
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		List<CashFlowResult> cashFlowResultList = cashFlowResultModel.getCashFlowResult();
		if(CollectionUtils.isEmpty(cashFlowResultList)) {
			logger.info("pull cashflow for[" + queryAccountNo + "],cash flow is empty...");
			return cashFlowList;
		}
		
		cashFlowList = super.transferToCashflowEntity(cashFlowResultList);

		if(cashFlowResultModel.isHasNextPage()) {
			Integer nextPageNo = Integer.parseInt(pageNo) + 1;
			workingParms.put("nextPageNo", nextPageNo + "");
			channelWorkerConfigService.updateLocalWorkingConfig(channelWorkerConfig.getWorkerUuid(), workingParms);
		}
		
		return cashFlowList;
	}

}
