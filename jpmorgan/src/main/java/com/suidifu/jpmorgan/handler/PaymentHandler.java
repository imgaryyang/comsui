package com.suidifu.jpmorgan.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.JsonUtils;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.service.BankService;
import com.suidifu.jpmorgan.spec.PaymentHandlerSpec;
import com.suidifu.jpmorgan.util.UUIDUtil;
import org.apache.commons.lang.StringUtils;

public interface PaymentHandler {

	CreditResult executeSinglePay(PaymentOrder paymentOrder,WorkingContext context);
	
	Result executeBatchPay(List<PaymentOrder> paymentOrderList,WorkingContext context);
	
	QueryCreditResult executeQueryPaymentStatus(PaymentOrder paymentOrder,WorkingContext context);
	
	QueryCreditResult handleCallback(Map<String, String> callbackParms);

	Result isTerminated(int remains) throws Exception;

	default CreditModel generateCreditModel(PaymentOrder paymentOrder,
											Map<String, String> workParms,
											BankService bankService) {
		CreditModel creditModel = new CreditModel(
				workParms.getOrDefault("channelAccountName", StringUtils.EMPTY),
				workParms.getOrDefault("channelAccountNo", StringUtils.EMPTY),
				workParms.getOrDefault("channelAccountAppendix", StringUtils.EMPTY),
				workParms.getOrDefault("channelBankInfo", StringUtils.EMPTY),
				paymentOrder.getDestinationAccountName(),
				paymentOrder.getDestinationAccountNo(),
				paymentOrder.getDestinationAccountAppendix(),
				paymentOrder.getDestinationBankInfo(),
				paymentOrder.getTransactionAmount().toPlainString(),
				paymentOrder.getCurrencyCode(),
				String.format(
						PaymentHandlerSpec.TRANSACTION_POSTSCRIPT_TEMPLATE,
						paymentOrder.getUuid(),//TODO 2016.11.21改，在只用fst_slot的情况下，备注中用uuid
						null == paymentOrder.getPostscript() ? "": paymentOrder.getPostscript()
				),
				paymentOrder.getUuid(),
				paymentOrder.getGatewayRouterInfo(),
				new Date()
		);

		String sysFlag = workParms.getOrDefault("sysFlag", GlobalSpec.BankCorpEps.PAB_DEFAULT_MODE);//转账加急标志 S特急 Y加急 S小于5万超级网银 Y都是走大小额

		if(GlobalSpec.BankCorpEps.PAB_DEFAULT_MODE.equals(sysFlag)) { //加急
			return creditModel;
		}

		String bankName = creditModel.getDestinationBankInfo().getOrDefault("bankName", StringUtils.EMPTY);
		long startTime = System.currentTimeMillis();
		Map<String, String> cachedBanks = bankService.getCachedBanks();
//		logger.info("get cached banks use time:" + (System.currentTimeMillis() - startTime));
		String stdBankCode = StringUtils.EMPTY;
		if(cachedBanks.containsKey(bankName)) {
			stdBankCode = cachedBanks.getOrDefault(bankName, StringUtils.EMPTY);
		}
		Map<String, String> destinationBankInfo = creditModel.getDestinationBankInfo();
		destinationBankInfo.put("bankCode", stdBankCode);
		creditModel.setDestinationBankInfo(JsonUtils.toJsonString(destinationBankInfo));

		return creditModel;
	}
}
