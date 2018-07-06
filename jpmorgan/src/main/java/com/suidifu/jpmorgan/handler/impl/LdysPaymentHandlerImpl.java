package com.suidifu.jpmorgan.handler.impl;

import com.demo2do.core.entity.Result;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.coffer.entity.thirdparty.ldys.LdysGlobalSpace;
import com.suidifu.coffer.handler.ThirdPartyPayHandler;
import com.suidifu.coffer.util.DateUtils;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.spec.PaymentHandlerSpec;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component("ldysPaymentHandler")
public class LdysPaymentHandlerImpl implements PaymentHandler {
    Logger logger = LoggerFactory.getLogger(LdysPaymentHandlerImpl.class);

    private static final String EXECUTE_SIGLE = "LdysPaymentHandlerImpl#executeSinglePay";
    private static final String EXECUTE_PAYMENT_STATUS = "LdysPaymentHandlerImpl#executeQueryPaymentStatus";

    @Autowired
    @Qualifier(value = "ldysHandler")
    private ThirdPartyPayHandler ldysHandler;


    @Override
    public CreditResult executeSinglePay(PaymentOrder paymentOrder, WorkingContext context) {
        try {
            Map<String, String> workParams = context.getWorkingParameters();
            CreditModel creditModel = generateCreditModel(paymentOrder, workParams);
            workParams.put(LdysGlobalSpace.MER_ID, creditModel.getSourceAccountNo());
            Date merDate = paymentOrder.getCommunicationLastSentTime();
            workParams.put(LdysGlobalSpace.MER_DATE, DateUtils.format(merDate, DateUtils.DATE_FORMAT_YYYYMMDD));


            CreditResult creditResult = ldysHandler.singleDebit(creditModel, workParams);
            return creditResult;
        } catch (Exception e) {
            logger.error("{}, 异常信息为 ： {}", EXECUTE_SIGLE, e.getMessage());
            return new CreditResult(GlobalSpec.ErrorMsg.ERR_SYSTEM_EXCEPTION);
        }
    }

    private CreditModel generateCreditModel(PaymentOrder paymentOrder, Map<String, String> workParms) {

        return new CreditModel(
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
                        paymentOrder.getUuid(),
                        paymentOrder.getPostscript()),
                paymentOrder.getUuid(),
                paymentOrder.getGatewayRouterInfo(),
                new Date()
        );
    }

    @Override
    public Result executeBatchPay(List<PaymentOrder> paymentOrderList, WorkingContext context) {
        return null;
    }

    @Override
    public QueryCreditResult executeQueryPaymentStatus(PaymentOrder paymentOrder, WorkingContext context) {
        try {
            Map<String, String> workParams = context.getWorkingParameters();
            QueryCreditModel creditModel = generateQueryCreditModel(paymentOrder, workParams);
            workParams.put(LdysGlobalSpace.MER_ID, creditModel.getAccountNo());
            Date merDate = paymentOrder.getCommunicationLastSentTime();
            workParams.put(LdysGlobalSpace.MER_DATE, DateUtils.format(merDate, DateUtils.DATE_FORMAT_YYYYMMDD));
            QueryCreditResult queryCreditResult = ldysHandler.queryDebit(creditModel, workParams);
            return queryCreditResult;
        } catch (Exception e) {
            logger.error("{}, 异常信息为 ： {}", EXECUTE_PAYMENT_STATUS, e.getMessage());
            return new QueryCreditResult(GlobalSpec.ErrorMsg.ERR_SYSTEM_EXCEPTION);
        }
    }

    private QueryCreditModel generateQueryCreditModel(PaymentOrder paymentOrder, Map<String, String> workParms) {
        return new QueryCreditModel(workParms.getOrDefault("channelAccountNo", StringUtils.EMPTY),
                paymentOrder.getUuid(),
                new Date()
        );
    }

    @Override
    public QueryCreditResult handleCallback(Map<String, String> callbackParms) {
        return null;
    }

    @Override
    public Result isTerminated(int remains) throws Exception {
        return null;
    }

}
