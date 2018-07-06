package com.suidifu.jpmorgan.handler.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.entity.Result;
import com.suidifu.coffer.GlobalSpec.ErrorMsg;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.coffer.handler.sxccb.SxCcbHandler;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.util.UUIDUtil;

@Component("sxCcbPaymentHandler")
public class SxCcbPaymentHandlerImpl implements PaymentHandler {

	@Autowired
	SxCcbHandler sxCcbHandler;

	@Override
	public CreditResult executeSinglePay(PaymentOrder paymentOrder,
			WorkingContext context) {
		try {

			// TODO 只支持扣款
			CreditModel creditModel = generateCreditModel(paymentOrder,
					context.getWorkingParameters());
			CreditResult creditResult = sxCcbHandler.singleDebit(creditModel,
					context.getWorkingParameters());
			return creditResult;
		} catch (Exception e) {
			e.printStackTrace();
			return new CreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
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
			QueryCreditResult queryCreditResult = sxCcbHandler.queryDebit(
					queryCreditModel, context.getWorkingParameters());
			return queryCreditResult;
		} catch (Exception e) {
			e.printStackTrace();
			return new QueryCreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
	}

	@Override
	public Result isTerminated(int remains) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	private CreditModel generateCreditModel(PaymentOrder paymentOrder,
			Map<String, String> workParms) {

		CreditModel creditModel = new CreditModel();
		creditModel.setDestinationAccountName(paymentOrder.getDestinationAccountName());
		creditModel.setDestinationAccountNo(paymentOrder.getDestinationAccountNo());
		creditModel.setDestinationBankInfo(paymentOrder.getDestinationBankInfo());
		//creditModel.setCurrencyCode(paymentOrder.getCurrencyCode());
		creditModel.setTransactionVoucherNo(paymentOrder.getUuid());
		creditModel.setRequestDate(new Date());
		creditModel.setTransactionAmount(paymentOrder.getTransactionAmount().toString());
		creditModel.setDestinationAccountAppendix(paymentOrder.getDestinationAccountAppendix());
		
		creditModel.setTransactionAppendix(paymentOrder.getGatewayRouterInfo());
		return creditModel;
	}

	private QueryCreditModel generateQueryCreditModel(PaymentOrder paymentOrder) {

		return new QueryCreditModel(paymentOrder.getUuid(), new Date(),
				UUIDUtil.uuid());
	}

	@Override
	public QueryCreditResult handleCallback(Map<String, String> callbackParms) {
		
		return sxCcbHandler.handleCallback(callbackParms);
	}

}
