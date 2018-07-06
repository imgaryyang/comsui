package com.suidifu.barclays.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullCashflowException;
import com.suidifu.barclays.handler.CashFlowGenericHandler;
import com.suidifu.barclays.handler.CashflowHandler;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.CashFlowResult;
import com.suidifu.coffer.entity.CashFlowResultModel;
import com.suidifu.coffer.entity.QueryCashFlowModel;
import com.suidifu.coffer.handler.DirectBankHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dafuchen on 2017/9/7.
 */
@Component("cmbcCashflowHandler")
public class CmbcCashflowHandler extends CashFlowGenericHandler implements CashflowHandler {
    Logger logger = LoggerFactory.getLogger(CmbcCashflowHandler.class);

    @Autowired
    private DirectBankHandler cmbcDirectBankHandler;
    @Autowired
    private ChannelWorkerConfigService channelWorkerConfigService;

    @Override
    public List<CashFlow> execPullCashflow(ChannelWorkerConfig channelWorkerConfig) throws PullCashflowException {

        Map<String, String> workingParms = channelWorkerConfig.getLocalWorkingConfig();

        if (null == workingParms) {
            throw new PullCashflowException();
        }
        String queryAccountNo = workingParms.getOrDefault("channelAccountNo", StringUtils.EMPTY);
        if(StringUtils.isEmpty(queryAccountNo)) {
            logger.warn("channelAccountNo is not configed,pull cashflow failed!...");
            throw new PullCashflowException();
        }

        String pageSize = workingParms.getOrDefault("pageSize", GlobalSpec.BankCorpEps.CCB_DEFAULT_PAGESIZE);
        String pageNo = workingParms.getOrDefault("nextPageNo", "1");
        String requestSn = DateUtils.format(new Date(), "yyyyMMddHHmmss");

        QueryCashFlowModel queryCashFlowModel = new QueryCashFlowModel(queryAccountNo,
                                                                        DateUtils.format(new Date(),"yyyy-MM-dd"),
                                                                        DateUtils.format(new Date(),"yyyy-MM-dd"),
                                                                        pageNo,
                                                                        pageSize,
                                                                        requestSn);
        queryCashFlowModel.setRequestDate(new Date());
        CashFlowResultModel cashFlowResultModel = cmbcDirectBankHandler.queryIntradayCashFlow(queryCashFlowModel, workingParms);

        if(GlobalSpec.DEFAULT_FAIL_CODE.equals(cashFlowResultModel.getCommCode())) {
            logger.warn("failed to pull cashflow accountNo[{}],message: {}", queryAccountNo, cashFlowResultModel.getErrMsg());
        }

        List<CashFlow> cashFlowList = new ArrayList<>();
        List<CashFlowResult> cashFlowResultList = cashFlowResultModel.getCashFlowResult();
        if(CollectionUtils.isEmpty(cashFlowResultList)) {
            logger.info("pull cashflow for[{}],cash flow is empty...", queryAccountNo);
            return cashFlowList;
        }

        cashFlowList = super.transferToCashflowEntity(cashFlowResultList);

        if(cashFlowResultModel.isHasNextPage()) {
            Integer nextPageNo = Integer.parseInt(pageNo) + 1;
            workingParms.put("nextPageNo", nextPageNo + "");
            channelWorkerConfigService.updateLocalWorkingConfig(channelWorkerConfig.getWorkerUuid(), workingParms);
        }

        return cashFlowList;
    }
}
