package com.suidifu.jpmorgan.handler.impl;

import com.demo2do.core.entity.Result;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.coffer.handler.ThirdPartyPayHandler;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.spec.PaymentHandlerSpec;
import com.suidifu.jpmorgan.util.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author dafuchen
 *         2017/12/7
 */
@Component("epayHandler")
public class EpayHandlerImpl implements PaymentHandler {
    @Autowired
    private ThirdPartyPayHandler ePayHandler;

    @Override
    public CreditResult executeSinglePay(PaymentOrder paymentOrder, WorkingContext context) {
        try {
            CreditModel creditModel = generateCreditModel(paymentOrder, context.getWorkingParameters());
            Map<String, String> workParams = context.getWorkingParameters();
            CreditResult creditResult = ePayHandler.singleDebit(creditModel, workParams);
            return creditResult;
        } catch (Exception e) {
            return new CreditResult(GlobalSpec.ErrorMsg.ERR_SYSTEM_EXCEPTION);
        }
    }

    @Override
    public Result executeBatchPay(List<PaymentOrder> paymentOrderList, WorkingContext context) {
        return null;
    }

    @Override
    public QueryCreditResult executeQueryPaymentStatus(PaymentOrder paymentOrder, WorkingContext context) {
        try {
            QueryCreditModel queryCreditModel = generateQueryCreditModel(paymentOrder);
            Map<String, String> workParams = context.getWorkingParameters();
            QueryCreditResult queryCreditResult = ePayHandler.queryDebit(queryCreditModel, workParams);
            return queryCreditResult;
        } catch (Exception e) {
            return new QueryCreditResult(GlobalSpec.ErrorMsg.ERR_SYSTEM_EXCEPTION);
        }
    }

    @Override
    public QueryCreditResult handleCallback(Map<String, String> callbackParms) {
        return null;
    }

    @Override
    public Result isTerminated(int remains) throws Exception {
        return null;
    }

    private CreditModel generateCreditModel(PaymentOrder paymentOrder,
                                            Map<String, String> workParms) {

        return new CreditModel(
                workParms.getOrDefault("channelAccountName", StringUtils.EMPTY),
                workParms.getOrDefault("channelAccountNo", StringUtils.EMPTY),
                workParms.getOrDefault("channelAccountAppendix", StringUtils.EMPTY),
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

    private QueryCreditModel generateQueryCreditModel(PaymentOrder paymentOrder) {

        return new QueryCreditModel(paymentOrder.getUuid(), new Date(),
                UUIDUtil.uuid());
    }
}
