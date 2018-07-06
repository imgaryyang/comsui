package com.suidifu.jpmorgan.handler.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.GlobalSpec.ErrorMsg;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.coffer.handler.ccb.CCBDirectBankHandler;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.service.BankService;
import com.suidifu.jpmorgan.spec.PaymentHandlerSpec;
import com.suidifu.jpmorgan.util.UUIDUtil;

@Component("ccbDirectBankPaymentHandler")
public class CCBDirectBankPaymentHandlerImpl implements PaymentHandler {
	
	private static final Log logger = LogFactory.getLog(CCBDirectBankPaymentHandlerImpl.class);

	@Autowired
	private CCBDirectBankHandler ccbDirectBankHandler;
	@Autowired
	private BankService bankService;
	
	
	@Override
	public CreditResult executeSinglePay(PaymentOrder paymentOrder,
			WorkingContext context) {
		try {
			CreditModel creditModel = generateCreditModel(paymentOrder,
					context.getWorkingParameters());
			CreditResult creditResult = ccbDirectBankHandler.singleCredit(
					creditModel, context.getWorkingParameters());
			return creditResult;
		} catch (Exception e) {
			e.printStackTrace();
			return new CreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
	}
	
	private CreditModel generateCreditModel(PaymentOrder paymentOrder,
			Map<String, String> workParms) {
		
		CreditModel creditModel = new CreditModel(workParms.getOrDefault("channelAccountName",
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
		
//		BigDecimal amount = paymentOrder.getTransactionAmount();
//		if(amount.compareTo(GlobalSpec.CRITICAL_AMOUNT) < 1) {
//			return creditModel;
//		}
		String stdBankCode = creditModel.getDestinationBankInfo().getOrDefault("stdBankCode", StringUtils.EMPTY);
		if (StringUtils.isNotEmpty(stdBankCode)) {
			return creditModel;
		}
		String bankCacheSwitch = workParms.getOrDefault("bankCacheSwitch", "true");
		
		if(Boolean.parseBoolean(bankCacheSwitch)) {
			String bankName = creditModel.getDestinationBankInfo().getOrDefault("bankName", StringUtils.EMPTY);
			long startTime = System.currentTimeMillis();
			Map<String, String> cachedBanks = bankService.getCachedBanks();
			logger.info("get cached banks use time:" + (System.currentTimeMillis() - startTime));
			if(cachedBanks.containsKey(bankName)) {
				stdBankCode = cachedBanks.getOrDefault(bankName, StringUtils.EMPTY);
			}
			Map<String, String> destinationBankInfo = creditModel.getDestinationBankInfo();
			destinationBankInfo.put("stdBankCode", stdBankCode);
			creditModel.setDestinationBankInfo(JsonUtils.toJsonString(destinationBankInfo));
		}
		
		return creditModel;
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
			QueryCreditResult queryCreditResult = ccbDirectBankHandler
					.queryCredit(queryCreditModel,
							context.getWorkingParameters());
			return queryCreditResult;
		} catch (Exception e) {
			e.printStackTrace();
			return new QueryCreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
	}
	
	private QueryCreditModel generateQueryCreditModel(PaymentOrder paymentOrder) {

		return new QueryCreditModel(paymentOrder.getUuid(), new Date(), UUIDUtil.snowFlakeIdString());
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
