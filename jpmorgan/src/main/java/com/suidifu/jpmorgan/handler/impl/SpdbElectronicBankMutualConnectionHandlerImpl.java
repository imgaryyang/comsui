package com.suidifu.jpmorgan.handler.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

/**
 * Created by Chen dafu on 17-8-17.
 */
@Component("spdbElectronicBankMutualConnectionHandler")
public class SpdbElectronicBankMutualConnectionHandlerImpl implements PaymentHandler {

    @Autowired
    private DirectBankHandler spdbElectronicBankMutualConnectionHandlerImpl;

    @Autowired
    private BankService bankService;

    @Override
    public CreditResult executeSinglePay(PaymentOrder paymentOrder, WorkingContext context) {
    	
        Map<String, String> workParams = context.getWorkingParameters();
        threadSleep(workParams);
    	
        CreditModel creditModel = generateCreditModel(paymentOrder, context.getWorkingParameters(), bankService);
        workParams.put("requestId", creditModel.getTransactionVoucherNo());
        return spdbElectronicBankMutualConnectionHandlerImpl.singleCredit(creditModel, workParams);
    }

    @Override
    public Result executeBatchPay(List<PaymentOrder> paymentOrderList, WorkingContext context) {
        return null;
    }

    @Override
    public QueryCreditResult executeQueryPaymentStatus(PaymentOrder paymentOrder, WorkingContext context) {
    	
        Map<String, String> workParams = context.getWorkingParameters();
        threadSleep(workParams);
    	
        QueryCreditModel queryCreditModel = new QueryCreditModel(paymentOrder.getUuid(),
                new Date(System.currentTimeMillis()));
        queryCreditModel.setAccountNo(context.getWorkingParameters().getOrDefault("channelAccountNo", ""));
        queryCreditModel.setTransactionVoucherNo(queryCreditModel.getTransactionVoucherNo());

        workParams.put("requestId", queryCreditModel.getTransactionVoucherNo());
        return spdbElectronicBankMutualConnectionHandlerImpl.queryCredit(queryCreditModel, workParams);
    }

	private void threadSleep(Map<String, String> workParams) {
		int paymentSleep = Integer.parseInt(workParams.getOrDefault("paymentSleep", "0"));
        if(paymentSleep > 0) {
        	try {
				Thread.sleep(paymentSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
}
