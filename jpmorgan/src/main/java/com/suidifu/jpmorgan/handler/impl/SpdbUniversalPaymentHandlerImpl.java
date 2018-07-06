package com.suidifu.jpmorgan.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.entity.Result;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.coffer.GlobalSpec.ErrorMsg;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by dafuchen on 2017/9/4.
 */
@Component("spdbUniversalPaymentHandler")
public class SpdbUniversalPaymentHandlerImpl implements PaymentHandler {
    @Autowired
    private PaymentHandler spdbPaymentHandler;
    @Autowired
    private PaymentHandler spdbElectronicBankMutualConnectionHandler;

    private static final String SPDB_BANK_CODE = "C10310";

    private static final int MAXIMUM_FOR_ELECTRONIC_BANK_MUTUAL_CONNECTION = 50000;

    private String SINGLE_PAY = "spdbUniversalPaymentHandler#executeSinglePay";
    private String QUERY_STATE = "SpdbUniversalPaymentHandlerImpl#executeQueryPaymentStatus";

    Logger logger = LoggerFactory.getLogger(SpdbUniversalPaymentHandlerImpl.class);

    @Override
    public CreditResult executeSinglePay(PaymentOrder paymentOrder, WorkingContext context) {
    	try {
            String requestId = paymentOrder.getUuid();
            String destinationBankInfo = paymentOrder.getDestinationBankInfo();
            String bankCode = JSONObject.parseObject(destinationBankInfo).getString("bankCode");
            if (StringUtils.isEmpty(bankCode)) {
                logger.error("{} {} the bankCode is null", SINGLE_PAY, requestId);
                return null;
            }
            if (SPDB_BANK_CODE.equals(bankCode)) {
                return spdbPaymentHandler.executeSinglePay(paymentOrder, context);
            } else {
                BigDecimal amount = paymentOrder.getTransactionAmount();
                if (amount.compareTo(new BigDecimal(MAXIMUM_FOR_ELECTRONIC_BANK_MUTUAL_CONNECTION)) > 0) {
                    return spdbPaymentHandler.executeSinglePay(paymentOrder, context);
                } else {
                    return spdbElectronicBankMutualConnectionHandler.executeSinglePay(paymentOrder, context);
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
			return new CreditResult(ErrorMsg.ERR_SYSTEM_EXCEPTION);
		}
    	
    }

    @Override
    public Result executeBatchPay(List<PaymentOrder> paymentOrderList, WorkingContext context) {
        return null;
    }

    @Override
    public QueryCreditResult executeQueryPaymentStatus(PaymentOrder paymentOrder, WorkingContext context) {
        String requestId = paymentOrder.getUuid();
        String destinationBankInfo = paymentOrder.getDestinationBankInfo();
        String bankCode = JSONObject.parseObject(destinationBankInfo).getString("bankCode");
        if (StringUtils.isEmpty(bankCode)) {
            logger.error("{} {} the bankCode is null", QUERY_STATE, requestId);
            return null;
        }
        if (SPDB_BANK_CODE.equals(bankCode)) {
            return spdbPaymentHandler.executeQueryPaymentStatus(paymentOrder, context);
        } else {
            BigDecimal amount = paymentOrder.getTransactionAmount();
            if (amount.compareTo(new BigDecimal(MAXIMUM_FOR_ELECTRONIC_BANK_MUTUAL_CONNECTION)) > 0) {
                return spdbPaymentHandler.executeQueryPaymentStatus(paymentOrder, context);
            } else {
                return spdbElectronicBankMutualConnectionHandler.executeQueryPaymentStatus(paymentOrder, context);
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
