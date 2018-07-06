package com.suidifu.barclays.handler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.suidifu.barclays.handler.CashFlowGenericHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullCashflowException;
import com.suidifu.barclays.handler.CashflowHandler;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.CashFlowResult;
import com.suidifu.coffer.entity.CashFlowResultModel;
import com.suidifu.coffer.entity.QueryCashFlowModel;
import com.suidifu.coffer.handler.cmb.CMBDirectBankHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.directbank.business.StrikeBalanceStatus;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
@Component("cmbCashflowHandler")
public class CMBCashflowHandler extends CashFlowGenericHandler implements CashflowHandler {
	
	private static Log logger = LogFactory.getLog(CMBCashflowHandler.class);

	@Autowired
	CMBDirectBankHandler cmbDirectBankHandler;
	
	@Override
	public List<CashFlow> execPullCashflow(ChannelWorkerConfig channelWorkerConfig)
			throws PullCashflowException {
		Map<String, String> workingParms = channelWorkerConfig.getLocalWorkingConfig();
		
		if(null == workingParms) {
			throw new PullCashflowException();
		}
		
		String queryAccountNo = workingParms.getOrDefault("channelAccountNo", StringUtils.EMPTY);
		if(StringUtils.isEmpty(queryAccountNo)) {
			throw new PullCashflowException();
		}
		
		QueryCashFlowModel queryCashFlowModel = new QueryCashFlowModel(queryAccountNo, DateUtils.format(new Date(),"yyyyMMdd"), DateUtils.format(new Date(),"yyyyMMdd"));
		CashFlowResultModel cashFlowResultModel = cmbDirectBankHandler.queryIntradayCashFlow(queryCashFlowModel, workingParms);
		
		List<CashFlow> cashflowList = new ArrayList<CashFlow>();
		if(GlobalSpec.DEFAULT_FAIL_CODE.equals(cashFlowResultModel.getCommCode())) {
			logger.warn(queryAccountNo + "communication error," +cashFlowResultModel.getErrMsg());
			return cashflowList;
		}
		List<CashFlowResult> cashFlowResultList = cashFlowResultModel.getCashFlowResult();
		
		if(CollectionUtils.isEmpty(cashFlowResultList)) {
			logger.info("[" + queryAccountNo + "]" + "cash flow is empty...");
			return cashflowList;
		}
		
		cashflowList = transferToCashflowEntity(cashFlowResultList);
		
		return cashflowList;
	}
	
	
	public List<CashFlow> transferToCashflowEntity(List<CashFlowResult> cashFlowResultList) {
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		for(CashFlowResult cashFlowResult : cashFlowResultList) {
			
			AccountSide accountSide = com.suidifu.coffer.entity.AccountSide.CREDIT.equals(cashFlowResult.getAccountSide()) ? AccountSide.CREDIT : AccountSide.DEBIT;
			
			StrikeBalanceStatus strikeBalanceStatus = null == cashFlowResult.getStrikeBalanceStatus() ? null : com.suidifu.coffer.entity.StrikeBalanceStatus.NORMAL.equals(cashFlowResult.getStrikeBalanceStatus()) ? StrikeBalanceStatus.NORMAL : StrikeBalanceStatus.STRIKE;
			
			//String tradeUuid = cashFlowResult.getTransactionVoucherNo();
			
			CashFlow cashFlow = new CashFlow(UUID.randomUUID().toString(), CashFlowChannelType.DirectBank, null, null, cashFlowResult.getHostAccountNo(), cashFlowResult.getHostAccountName(), cashFlowResult.getCounterAccountNo(), cashFlowResult.getCounterAccountName(), cashFlowResult.getCounterAccountAppendix(), cashFlowResult.getCounterBankInfo(), accountSide, cashFlowResult.getTransactionTime(), cashFlowResult.getTransactionAmount(), cashFlowResult.getBalance(), cashFlowResult.getTransactionVoucherNo(), cashFlowResult.getBankSequenceNo(), cashFlowResult.getRemark(), cashFlowResult.getOtherRemark(), strikeBalanceStatus, null);

			String flag = super.getFlag(cashFlowResult);
			cashFlow.setStringFieldTwo(flag);

			cashFlowList.add(cashFlow);
		}
		return cashFlowList;
	}

}
