package com.suidifu.jpmorgan.handler.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.demo2do.core.entity.Result;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.coffer.exception.UnSupportedException;
import com.suidifu.coffer.handler.ThirdPartyPayHandler;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.util.UUIDUtil;

@Component("cpcnPaymentHandler")
public class CpcnPaymentHandlerImpl implements PaymentHandler {

	@Autowired
	@Qualifier(value = "cpcnHandler")
	private ThirdPartyPayHandler cpcnHandler;

	@Override
	public CreditResult executeSinglePay(PaymentOrder paymentOrder, WorkingContext context) {

		CreditModel creditModel = generateCreditModel(paymentOrder, context.getWorkingParameters());

		try {
			return cpcnHandler.singleDebit(creditModel, context.getWorkingParameters());
		} catch (UnSupportedException e) {
			e.printStackTrace();
			return new CreditResult(GlobalSpec.ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
	}

	@Override
	public Result executeBatchPay(List<PaymentOrder> paymentOrderList, WorkingContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryCreditResult executeQueryPaymentStatus(PaymentOrder paymentOrder, WorkingContext context) {
		QueryCreditModel queryCreditModel = new QueryCreditModel(paymentOrder.getUuid(), new Date(), null);
		
		try {
			return cpcnHandler.queryDebit(queryCreditModel, context.getWorkingParameters());
		} catch (UnSupportedException e) {
			e.printStackTrace();
			return new QueryCreditResult(GlobalSpec.ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
	}

	@Override
	public Result isTerminated(int remains) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	private CreditModel generateCreditModel(PaymentOrder paymentOrder, Map<String, String> workParms) {

		return new CreditModel(workParms.getOrDefault("channelAccountName", StringUtils.EMPTY),
				workParms.getOrDefault("channelAccountNo", StringUtils.EMPTY),
				workParms.getOrDefault("channelAccountAppendix", StringUtils.EMPTY),
				workParms.getOrDefault("channelBankInfo", StringUtils.EMPTY), paymentOrder.getDestinationAccountName(),
				paymentOrder.getDestinationAccountNo(), paymentOrder.getDestinationAccountAppendix(),
				paymentOrder.getDestinationBankInfo(), paymentOrder.getTransactionAmount().toPlainString(),
				paymentOrder.getCurrencyCode(), null, paymentOrder.getUuid(), paymentOrder.getGatewayRouterInfo(),
				new Date(), UUIDUtil.uuid());
	}

	@Override
	public QueryCreditResult handleCallback(Map<String, String> callbackParms) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
