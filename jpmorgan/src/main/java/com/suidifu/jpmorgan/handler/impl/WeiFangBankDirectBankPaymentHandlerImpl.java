package com.suidifu.jpmorgan.handler.impl;

import com.demo2do.core.entity.Result;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.coffer.handler.wfb.WeiFangBankDirectBankHandler;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.service.BankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen dafu on 17-7-27.
 */
@Component("weiFangBankDirectBankPaymentHandler")
public class WeiFangBankDirectBankPaymentHandlerImpl implements PaymentHandler {
    Logger logger = LoggerFactory.getLogger(WeiFangBankDirectBankPaymentHandlerImpl.class);

    @Autowired
    private WeiFangBankDirectBankHandler weiFangBankDirectBankHandler;
    @Autowired
    private BankService bankService;
    @Override
    public CreditResult executeSinglePay(PaymentOrder paymentOrder, WorkingContext context) {
        try{
            CreditModel creditResult = generateCreditModel(paymentOrder, context.getWorkingParameters(),
                    bankService);
            return weiFangBankDirectBankHandler.singleCredit(creditResult, context.getWorkingParameters());
        }catch (Exception e){
            logger.error("error occurred while execute WeiFangBankDirectBankPaymentHandlerImpl.executeSinglePay the message is {}",
                    e.getMessage());
            return null;
        }
    }

    @Override
    public Result executeBatchPay(List<PaymentOrder> paymentOrderList, WorkingContext context) {
        return null;
    }

    @Override
    public QueryCreditResult executeQueryPaymentStatus(PaymentOrder paymentOrder, WorkingContext context) {
        try{
            QueryCreditModel queryCreditModel = new QueryCreditModel(paymentOrder.getUuid(), new Date());
            return weiFangBankDirectBankHandler.queryCredit(queryCreditModel, context.getWorkingParameters());
        }catch (Exception e){
            logger.error("error occurred while execute WeiFangBankDirectBankPaymentHandlerImpl.executeQueryPaymentStatus the " +
                    "message is {}", e.getMessage());
        }
        return null;
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
