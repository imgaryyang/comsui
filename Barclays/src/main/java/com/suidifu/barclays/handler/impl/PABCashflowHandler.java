package com.suidifu.barclays.handler.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.demo2do.core.utils.SignatureUtils;
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
import com.suidifu.coffer.GlobalSpec.BankCorpEps;
import com.suidifu.coffer.entity.CashFlowResult;
import com.suidifu.coffer.entity.CashFlowResultModel;
import com.suidifu.coffer.entity.QueryCashFlowModel;
import com.suidifu.coffer.handler.pab.PABDirectBankHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.directbank.business.StrikeBalanceStatus;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;

@Component("pabCashflowHandler")
public class PABCashflowHandler extends CashFlowGenericHandler implements CashflowHandler {

	private static Log logger = LogFactory.getLog(PABCashflowHandler.class);
	@Autowired
	PABDirectBankHandler pabDirectBankHandler;
	
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
		
		//pageNo逻辑
		String pageNo = workingParms.getOrDefault("pageNo", "1");
		String pageSize = workingParms.getOrDefault("pageSize", BankCorpEps.PAB_DEFAULT_PAGESIZE);
		
		QueryCashFlowModel queryCashFlowModel = new QueryCashFlowModel(queryAccountNo, DateUtils.format(new Date(),"yyyyMMdd"), DateUtils.format(new Date(),"yyyyMMdd"), new Date(), pageNo, pageSize);
		CashFlowResultModel cashFlowResultModel = pabDirectBankHandler.queryIntradayCashFlow(queryCashFlowModel, workingParms);
		
		if(GlobalSpec.DEFAULT_FAIL_CODE.equals(cashFlowResultModel.getCommCode())) {
			logger.warn(queryAccountNo + "communication error," +cashFlowResultModel.getErrMsg());
		}
		List<CashFlowResult> cashFlowResultList = cashFlowResultModel.getCashFlowResult();
		if(CollectionUtils.isEmpty(cashFlowResultList)) {
			logger.info(queryAccountNo + "cash flow is empty...");
		}
		
		List<CashFlow> auditBillList = transferToCashflowEntity(cashFlowResultList);
		
		//update pageNo
		if(cashFlowResultModel.isHasNextPage()) {
			
		}
		
		return auditBillList;
	}
	
	public List<CashFlow> transferToCashflowEntity(List<CashFlowResult> cashFlowResultList) {
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		for(CashFlowResult cashFlowResult : cashFlowResultList) {
			
			
			AccountSide accountSide = com.suidifu.coffer.entity.AccountSide.CREDIT.equals(cashFlowResult.getAccountSide()) ? AccountSide.CREDIT : AccountSide.DEBIT;
			
			StrikeBalanceStatus strikeBalanceStatus = null == cashFlowResult.getStrikeBalanceStatus() ? null : com.suidifu.coffer.entity.StrikeBalanceStatus.NORMAL.equals(cashFlowResult.getStrikeBalanceStatus()) ? StrikeBalanceStatus.NORMAL : StrikeBalanceStatus.STRIKE;
			
			String tradeUuid = StringUtils.EMPTY;
			String remark = cashFlowResult.getRemark();
			if(StringUtils.isNotEmpty(remark)) {
				String regex = "\\((.*?)\\)";//TODO 抽spec
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(remark);
				if(null != matcher && matcher.find(0)) {
					tradeUuid = matcher.group(1);
				}
			}
			
			CashFlow cashFlow = new CashFlow(UUID.randomUUID().toString(), CashFlowChannelType.DirectBank, null, null, cashFlowResult.getHostAccountNo(), cashFlowResult.getHostAccountName(), cashFlowResult.getCounterAccountNo(), cashFlowResult.getCounterAccountName(), cashFlowResult.getCounterAccountAppendix(), cashFlowResult.getCounterBankInfo(), accountSide, cashFlowResult.getTransactionTime(), cashFlowResult.getTransactionAmount(), cashFlowResult.getBalance(), cashFlowResult.getTransactionVoucherNo(), cashFlowResult.getBankSequenceNo(), cashFlowResult.getRemark(), cashFlowResult.getOtherRemark(), strikeBalanceStatus, tradeUuid);

			String flag = super.getFlag(cashFlowResult);
			cashFlow.setStringFieldTwo(flag);

			cashFlowList.add(cashFlow);
		}
		return cashFlowList;
	}

}
