package com.suidifu.jpmorgan.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.*;
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
 * @author dafuchen
 *         2017/12/4
 */
@Component("njbcDirectBankPaymentHandler")
public class NjbcDirectBankPaymentHandler implements PaymentHandler  {
    @Autowired
    private DirectBankHandler njbcDirectBankHandler;

    @Autowired
    private BankService bankService;

    @Override
    public CreditResult executeSinglePay(PaymentOrder paymentOrder, WorkingContext context) {
        CreditModel creditModel = generateCreditModel(paymentOrder, context.getWorkingParameters(), bankService);
        String fileId = ((JSONObject)JSONObject.parse(paymentOrder.getGatewayRouterInfo())).getString(GlobalSpec.BankCorpEps.CONTRACT_UNIQUE_ID);
        CaseAttch caseAttch = new CaseAttch("", fileId, "");
        creditModel.setCaseAttch(caseAttch);
        creditModel.setPostscript(paymentOrder.getUuid());
        String requestNo = UUIDUtil.snowFlakeIdString();
        creditModel.setRequestSequenceNo(requestNo);
        return njbcDirectBankHandler.singleCredit(creditModel, context.getWorkingParameters());
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
        queryCreditModel.setRequestSequenceNo(UUIDUtil.snowFlakeIdString());
        return njbcDirectBankHandler.queryCredit(queryCreditModel, context.getWorkingParameters());
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
