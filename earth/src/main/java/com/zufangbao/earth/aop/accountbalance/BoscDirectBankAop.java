package com.zufangbao.earth.aop.accountbalance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo2do.core.utils.StringUtils;
import com.suidifu.coffer.entity.QueryBalanceModel;
import com.suidifu.coffer.entity.bosc.BoscGlobalSpace;
import com.suidifu.coffer.exception.RequestDataException;
import com.suidifu.coffer.handler.bosc.BoscDirectBankHandler;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;


import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Aspect
public class BoscDirectBankAop {
    private static final Logger logger = LoggerFactory.getLogger(BoscDirectBankAop.class);

    private static String CHARSET = "GBK";

    private static String INVOKER = "BoscDirectBankAop#getSession";

    private static String BOSC_USER_ID_KEY_IN_WORKPARAMS = "userId";

    private static final int EXPIRE_TIME_LIMIT = 9;

    private static final String BOSC_SESSION_VALUE = "BOSC_SESSION_VALUE";

    private static final String BOSC_SESSION_KEY_IN_WORKPARAMS = "session";

    @Autowired
    private  StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BoscDirectBankHandler boscDirectBankHandler;

    @Pointcut("execution(* com.suidifu.coffer.handler.DirectBankHandler.queryAccountBalance " +
            "(com.suidifu.coffer.entity.QueryBalanceModel, java.util.Map<String, String>)) " +
            "&& args(queryBalanceModel, workParams)")
    public void queryAccountBalance(QueryBalanceModel queryBalanceModel, Map<String, String> workParams) {
    }

    @Before("queryAccountBalance(queryBalanceModel, workParams)")
    public void getSession(QueryBalanceModel queryBalanceModel, Map<String, String> workParams) throws RequestDataException {
        String bankInfo = workParams.getOrDefault("channelBankInfo","");
        if (! StringUtils.isEmpty(bankInfo)) {
            JSONObject object = (JSONObject) JSON.parse(bankInfo);
            String bankCode = object.getString("bankCode");
            if (Objects.equals(BoscGlobalSpace.BOSC_BANK_CODE,bankCode)) {
                //上海银行的余额查询
                String serialNo = queryBalanceModel.getRequestSn();
                String userId = workParams.get(BOSC_USER_ID_KEY_IN_WORKPARAMS);
                String sessionKey = userId + BOSC_SESSION_VALUE;
                String sessionValue = stringRedisTemplate.opsForValue().get(sessionKey);
                if (StringUtils.isEmpty(sessionValue)) {
                    sessionValue = boscDirectBankHandler.getDesSessionId(serialNo,INVOKER, CHARSET, workParams);
                }
                stringRedisTemplate.opsForValue().set(sessionKey, sessionValue, EXPIRE_TIME_LIMIT, TimeUnit.MINUTES);
                workParams.put(BOSC_SESSION_KEY_IN_WORKPARAMS, sessionValue);
            }
        }
    }
}
