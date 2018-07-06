package com.suidifu.barclays.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullCashflowException;
import com.suidifu.barclays.handler.CashFlowGenericHandler;
import com.suidifu.barclays.handler.CashflowHandler;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.CashFlowResult;
import com.suidifu.coffer.entity.CashFlowResultModel;
import com.suidifu.coffer.entity.QueryCashFlowModel;
import com.suidifu.coffer.entity.bosc.BoscGlobalSpace;
import com.suidifu.coffer.exception.RequestDataException;
import com.suidifu.coffer.handler.bosc.BoscDirectBankHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author dafuchen
 *         2017/11/14
 */
@Component("boscCashflowHandler")
public class BoscCashflowHandler extends CashFlowGenericHandler implements CashflowHandler {
    private Logger logger = LoggerFactory.getLogger(BoscCashflowHandler.class);

    /**
     * redis session key userid + BOSC_SESSION_VALUE session
     **/
    private static final String BOSC_SESSION_VALUE = "BOSC_SESSION_VALUE";

    private static final String BOSC_CASH_FLOW = "BoscCashflowHandler#execPullCashflow";
    /**
     * 过期时间
     **/
    private static final int EXPIRE_TIME_LIMIT = 9;
    /**
     * 编码格式
     **/
    private static final String CHAR_SET = "GBK";

    @Autowired
    private BoscDirectBankHandler boscDirectBankHandler;

    @Autowired
    ChannelWorkerConfigService channelWorkerConfigService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<CashFlow> execPullCashflow(ChannelWorkerConfig channelWorkerConfig) throws PullCashflowException {

        List<CashFlow> cashFlowList = new ArrayList<>();
        Map<String, String> workingParms = channelWorkerConfig.getLocalWorkingConfig();
        if(null == workingParms) {
            throw new PullCashflowException();
        }
        String queryAccountNo = workingParms.getOrDefault("channelAccountNo", StringUtils.EMPTY);
        if(StringUtils.isEmpty(queryAccountNo)) {
            logger.warn("channelAccountNo is not configed,pull cashflow failed!...");
            throw new PullCashflowException();
        }

        try {
            String pageSize = workingParms.getOrDefault("pageSize", GlobalSpec.BankCorpEps.CCB_DEFAULT_PAGESIZE);
            String pageNo = workingParms.getOrDefault("nextPageNo", "1");
            String requestSn = DateUtils.format(new Date(), "yyyyMMddHHmmss");
            int daysBefore = Integer.parseInt(workingParms.getOrDefault("daysBefore", "1"));
            String beginDate = DateUtils.format(DateUtils.addDays(new Date(), -daysBefore), "yyyyMMdd");
            QueryCashFlowModel queryCashFlowModel = new QueryCashFlowModel(queryAccountNo, beginDate, DateUtils.format(new Date(), "yyyyMMdd"), pageNo, pageSize, requestSn);

            //获取sessionid
            String session = getSession(requestSn, BOSC_CASH_FLOW, CHAR_SET, workingParms);
            workingParms.put(BoscGlobalSpace.BOSC_SESSION_KEY_IN_WORKPARAMS, session);
            CashFlowResultModel cashFlowResultModel = boscDirectBankHandler.queryIntradayCashFlow(queryCashFlowModel, workingParms);

            //查看是否session失效 是的话获取服务器端session
            if (StringUtils.equals(cashFlowResultModel.getCommCode(), "ebank00131")) {
                getSessionFlush(requestSn, BOSC_CASH_FLOW, CHAR_SET, workingParms);
            }
            if (GlobalSpec.DEFAULT_FAIL_CODE.equals(cashFlowResultModel.getCommCode())) {
                logger.warn("failed to pull cashflow accountNo[" + queryAccountNo + "],message:" + cashFlowResultModel.getErrMsg());
            }

            List<CashFlowResult> cashFlowResultList = cashFlowResultModel.getCashFlowResult();
            if (CollectionUtils.isEmpty(cashFlowResultList)) {
                logger.info("pull cashflow for[" + queryAccountNo + "],cash flow is empty...");
                return cashFlowList;
            }

            cashFlowList = super.transferToCashflowEntity(cashFlowResultList);

            if (cashFlowResultModel.isHasNextPage()) {
                Integer nextPageNo = Integer.parseInt(pageNo) + 1;
                workingParms.put("nextPageNo", nextPageNo + "");
                channelWorkerConfigService.updateLocalWorkingConfig(channelWorkerConfig.getWorkerUuid(), workingParms);
            }
        } catch (Exception e) {
            logger.error("invoke method : " + BOSC_CASH_FLOW + " pull cashflow for[" + queryAccountNo + "],get sessionid cause exception! :" + e.getMessage());
            throw new PullCashflowException();
        }
        return cashFlowList;
    }

    /**
     * 获得session的值
     * 从redis中尝试获取session的值 如果session存在且没有过期 返回session的值
     * 否则 从银行端拿session的值
     *
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
            sessionValue = boscDirectBankHandler.getDesSessionId(serialNo, invokeMethod, charsetName, workParams);
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
