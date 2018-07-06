package com.suidifu.barclays.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.demo2do.core.utils.DateUtils;
import com.demo2do.core.utils.SignatureUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.suidifu.coffer.entity.CashFlowResult;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.directbank.business.StrikeBalanceStatus;
import com.zufangbao.sun.ledgerbook.AccountSide;

public class CashFlowGenericHandler {

	public List<CashFlow> transferToCashflowEntity(List<CashFlowResult> cashFlowResultList) {
		List<CashFlow> cashFlowList = new ArrayList<CashFlow>();
		
		if(CollectionUtils.isEmpty(cashFlowResultList)) {
			return cashFlowList;
		}
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

			String flag = getFlag(cashFlowResult);
			cashFlow.setStringFieldTwo(flag);

			cashFlowList.add(cashFlow);
		}
		return cashFlowList;
	}
	/**
	 * 获取流水标识：付款方户号+付款方人名+收款方户号+收款方人名+金额+时间+摘要，做md5值
	 */
	public String getFlag(CashFlowResult cashFlowResult){
		String hostAccountNo = cashFlowResult.getHostAccountNo()==null?"":cashFlowResult.getHostAccountNo();
		String hostAccountName = cashFlowResult.getHostAccountName()==null?"":cashFlowResult.getHostAccountName();
		String counterAccountNo = cashFlowResult.getCounterAccountNo()==null?"":cashFlowResult.getCounterAccountNo();
		String counterAccountName = cashFlowResult.getCounterAccountName()==null?"":cashFlowResult.getCounterAccountName();
		String transactionAmount = cashFlowResult.getTransactionAmount()==null? BigDecimal.ZERO.toString():cashFlowResult.getTransactionAmount()+"";
		String transactionTime = cashFlowResult.getTransactionTime()==null?"": DateUtils.format(cashFlowResult.getTransactionTime(), "yyyy-MM-dd HH:mm:ss");
		String remark = cashFlowResult.getRemark()==null?"":cashFlowResult.getRemark();

		return SignatureUtils.makeMD5Signature(hostAccountNo, hostAccountName, counterAccountNo, counterAccountName, transactionAmount, transactionTime, remark);
	}

}
