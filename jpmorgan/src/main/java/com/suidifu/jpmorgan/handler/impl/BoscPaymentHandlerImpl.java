package com.suidifu.jpmorgan.handler.impl;

import com.demo2do.core.entity.Result;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.CreditModel;
import com.suidifu.coffer.entity.CreditResult;
import com.suidifu.coffer.entity.QueryCreditModel;
import com.suidifu.coffer.entity.QueryCreditResult;
import com.suidifu.coffer.entity.bosc.BoscGlobalSpace;
import com.suidifu.coffer.exception.RequestDataException;
import com.suidifu.coffer.handler.bosc.BoscDirectBankHandler;
import com.suidifu.jpmorgan.entity.PaymentOrder;
import com.suidifu.jpmorgan.entity.WorkingContext;
import com.suidifu.jpmorgan.handler.PaymentHandler;
import com.suidifu.jpmorgan.service.BankService;
import com.suidifu.jpmorgan.util.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import com.demo2do.core.utils.StringUtils;

/**
 * @author dafuchen
 *         2017/11/10
 */
@Component("boscPaymentHandler")
public class BoscPaymentHandlerImpl implements PaymentHandler {
    private static Logger logger = LoggerFactory.getLogger(BoscPaymentHandlerImpl.class);

    @Autowired
    private BoscDirectBankHandler boscDirectBankHandler;

    @Autowired
    private BankService bankService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**redis session key userid + BOSC_SESSION_VALUE session**/
    private static final String BOSC_SESSION_VALUE = "BOSC_SESSION_VALUE";
    /**过期时间**/
    private static final int EXPIRE_TIME_LIMIT = 9;
    /**编码格式**/
    private static final String CHAR_SET = "GBK";

    private static final String EXECUTE_SINGLE_PAY = "BoscPaymentHandlerImpl#executeSinglePay";
    private static final String EXECUTE_QUERYPAYMENT_STATUS = "BoscPaymentHandlerImpl#executeQueryPaymentStatus";


    @Override
    public CreditResult executeSinglePay(PaymentOrder paymentOrder, WorkingContext context) {
        try {
            Map<String, String> workParams = context.getWorkingParameters();
            CreditModel creditModel = generateCreditModel(paymentOrder, context.getWorkingParameters(), bankService);

            String requestId = creditModel.getTransactionVoucherNo();
            String session = getSession(requestId, EXECUTE_SINGLE_PAY, CHAR_SET, workParams);

            workParams.put(BoscGlobalSpace.BOSC_SESSION_KEY_IN_WORKPARAMS, session);
            workParams.put(BoscGlobalSpace.BOSC_SERIAL_NUMBER_KEY_IN_WORKPARAMS, requestId);

            CreditResult creditResult = boscDirectBankHandler.singleCredit(creditModel, workParams);
            //查看是否session失效 是的话获取服务器端session
            if (StringUtils.equals(creditResult.getCommCode(), "ebank00131")) {
                getSessionFlush(requestId, EXECUTE_SINGLE_PAY, CHAR_SET, workParams);
            }
            return creditResult;
        } catch (Exception e) {
            String requestId = paymentOrder.getUuid();
            logger.error("{}, the serial no is {}, the cause is {}", EXECUTE_SINGLE_PAY, requestId, e.getMessage());
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
            Map<String, String> workParams = context.getWorkingParameters();
            QueryCreditModel queryCreditModel = new QueryCreditModel(paymentOrder.getUuid(),
                    new Date(System.currentTimeMillis()));

            queryCreditModel.setAccountNo(context.getWorkingParameters().getOrDefault("channelAccountNo", ""));
            queryCreditModel.setRequestSequenceNo(UUIDUtil.snowFlakeIdString());

            String requestId = queryCreditModel.getTransactionVoucherNo();
            String session = getSession(requestId, EXECUTE_QUERYPAYMENT_STATUS, CHAR_SET, workParams);

            workParams.put(BoscGlobalSpace.BOSC_SESSION_KEY_IN_WORKPARAMS, session);
            workParams.put(BoscGlobalSpace.BOSC_SERIAL_NUMBER_KEY_IN_WORKPARAMS, requestId);

            QueryCreditResult creditResult = boscDirectBankHandler.queryCredit(queryCreditModel, workParams);
            //查看是否session失效 是的话获取服务器端session
            if (StringUtils.equals(creditResult.getCommCode(), "ebank00131")) {
                getSessionFlush(requestId, EXECUTE_SINGLE_PAY, CHAR_SET, workParams);
            }
            return creditResult;
        } catch (Exception e) {
            String requestId = paymentOrder.getUuid();
            logger.error("{}, the serial no is {}, the cause is {}", EXECUTE_QUERYPAYMENT_STATUS, requestId,
                    e.getMessage());
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

    /**
     * 获得session的值
     * 从redis中尝试获取session的值 如果session存在且没有过期 返回session的值
     * 否则 从银行端拿session的值
     * @param serialNo
     * @param invokeMethod
     * @param charsetName
     * @param workParams
     * @return
     * @throws RequestDataException
     */
    private String getSession(String serialNo,
                              String invokeMethod,
                              String charsetName,
                              Map<String, String> workParams) throws RequestDataException {
        String userID = workParams.get(BoscGlobalSpace.BOSC_USER_ID_KEY_IN_WORKPARAMS);
        String sessionKey = userID + BOSC_SESSION_VALUE;
        String sessionValue = stringRedisTemplate.opsForValue().get(sessionKey);

        if (StringUtils.isEmpty(sessionValue)) {
            sessionValue = boscDirectBankHandler.getDesSessionId(serialNo,invokeMethod, charsetName, workParams);
        }
        stringRedisTemplate.opsForValue().set(sessionKey, sessionValue, EXPIRE_TIME_LIMIT, TimeUnit.MINUTES);
        return sessionValue;
    }

    /**
     * 前提 ： 本地session已经失效
     * 从银行端获取最新的session并更行本地session缓存
     *
     * @param serialNo
     * @param invokeMethod
     * @param charsetName
     * @param workParams
     * @return
     */
    private String getSessionFlush(String serialNo,
                                   String invokeMethod,
                                   String charsetName,
                                   Map<String, String> workParams) throws RequestDataException {
        String userID = workParams.get(BoscGlobalSpace.BOSC_USER_ID_KEY_IN_WORKPARAMS);
        String sessionKey = userID + BOSC_SESSION_VALUE;
        String sessionValue = boscDirectBankHandler.getDesSessionId(serialNo, invokeMethod, charsetName, workParams);
        stringRedisTemplate.opsForValue().set(sessionKey, sessionValue, EXPIRE_TIME_LIMIT, TimeUnit.MINUTES);
        return sessionValue;
    }
}
