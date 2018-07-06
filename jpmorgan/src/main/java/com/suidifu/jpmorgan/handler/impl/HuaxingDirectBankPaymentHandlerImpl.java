package com.suidifu.jpmorgan.handler.impl;

import com.demo2do.core.entity.Result;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.coffer.handler.DirectBankHandler;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.service.BankService;
import com.suidifu.jpmorgan.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dafuchen on 2017/8/29.
 */
@Component("huaxingDirectBankPaymentHandler")
public class HuaxingDirectBankPaymentHandlerImpl implements PaymentHandler {
    @Autowired
    private DirectBankHandler huaxingDirectBankHandler;

    @Autowired
    private BankService bankService;

    @Override
    public CreditResult executeSinglePay(PaymentOrder paymentOrder, WorkingContext context) {
        CreditModel creditModel = generateCreditModel(paymentOrder, context.getWorkingParameters(), bankService);
        creditModel.setPostscript(paymentOrder.getUuid());
        String requestNo = UUIDUtil.snowFlakeIdString();
        creditModel.setRequestSequenceNo(requestNo);
        return huaxingDirectBankHandler.singleCredit(creditModel, context.getWorkingParameters());
    }

    @Override
    public Result executeBatchPay(List<PaymentOrder> paymentOrderList, WorkingContext context) {
        return null;
    }

    @Override
    public QueryCreditResult executeQueryPaymentStatus(PaymentOrder paymentOrder, WorkingContext context) {
        QueryCreditModel queryCreditModel = new QueryCreditModel(paymentOrder.getUuid(),
                new Date(System.currentTimeMillis()));
        queryCreditModel.setAccountNo(context.getWorkingParameters().getOrDefault("channelAccountNo", ""));
        queryCreditModel.setTransactionVoucherNo(queryCreditModel.getTransactionVoucherNo());
        queryCreditModel.setRequestDate(paymentOrder.getCommunicationLastSentTime());
        String requestNo = UUIDUtil.snowFlakeIdString();
        queryCreditModel.setRequestSequenceNo(requestNo);
        return huaxingDirectBankHandler.queryCredit(queryCreditModel, context.getWorkingParameters());
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
