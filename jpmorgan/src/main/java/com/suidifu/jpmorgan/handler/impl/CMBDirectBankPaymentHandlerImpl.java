package com.suidifu.jpmorgan.handler.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.DateUtils;
import com.suidifu.coffer.GlobalSpec.ErrorMsg;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.coffer.handler.cmb.CMBDirectBankHandler;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.spec.PaymentHandlerSpec;

@Component("cmbDirectBankPaymentHandler")
public class CMBDirectBankPaymentHandlerImpl implements PaymentHandler {

	@Autowired
	private CMBDirectBankHandler cmbDirectBankHandler;

	@Override
	public CreditResult executeSinglePay(PaymentOrder paymentOrder,
			WorkingContext context) {
		try {
			CreditModel creditModel = generateCreditModel(paymentOrder,
					context.getWorkingParameters());
			CreditResult creditResult = cmbDirectBankHandler.singleCredit(
					creditModel, context.getWorkingParameters());
			return creditResult;
		} catch (Exception e) {
			e.printStackTrace();
			return new CreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
	}

	private CreditModel generateCreditModel(PaymentOrder paymentOrder,
			Map<String, String> workParms) {
		return new CreditModel(workParms.getOrDefault("channelAccountName",
				StringUtils.EMPTY), workParms.getOrDefault("channelAccountNo",
				StringUtils.EMPTY), workParms.getOrDefault(
				"channelAccountAppendix", StringUtils.EMPTY),
				workParms.getOrDefault("channelBankInfo", StringUtils.EMPTY),
				paymentOrder.getDestinationAccountName(),
				paymentOrder.getDestinationAccountNo(),
				paymentOrder.getDestinationAccountAppendix(),
				paymentOrder.getDestinationBankInfo(), paymentOrder
						.getTransactionAmount().toPlainString(),
				paymentOrder.getCurrencyCode(), String.format(
						PaymentHandlerSpec.TRANSACTION_POSTSCRIPT_TEMPLATE,
						paymentOrder.getUuid(),//TODO 2016.11.21改，在只用fst_slot的情况下，备注中用uuid
						paymentOrder.getPostscript()), paymentOrder.getUuid(), 
				paymentOrder.getGatewayRouterInfo(), new Date());

	}

	@Override
	public Result executeBatchPay(List<PaymentOrder> paymentOrderList,
			WorkingContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryCreditResult executeQueryPaymentStatus(
			PaymentOrder paymentOrder, WorkingContext context) {
		try {
			QueryCreditModel queryCreditModel = generateQueryCreditModel(paymentOrder);
			QueryCreditResult queryCreditResult = cmbDirectBankHandler
					.queryCredit(queryCreditModel,
							context.getWorkingParameters());
			return queryCreditResult;
		} catch (Exception e) {
			e.printStackTrace();
			return new QueryCreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
	}

	private QueryCreditModel generateQueryCreditModel(PaymentOrder paymentOrder) {
		String endDate = DateUtils.format(new Date(), "yyyyMMdd");
		String startDate = DateUtils.format(DateUtils.addDays(new Date(),
				-PaymentHandlerSpec.CMB_QUERY_DATE_RANGE), "yyyyMMdd");

		return new QueryCreditModel(startDate, endDate, paymentOrder.getUuid());
	}

	@Override
	public Result isTerminated(int remains) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryCreditResult handleCallback(Map<String, String> callbackParms) {
		// TODO Auto-generated method stub
		return null;
	}

}
