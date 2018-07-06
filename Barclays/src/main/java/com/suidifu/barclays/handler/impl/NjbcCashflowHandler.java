package com.suidifu.barclays.handler.impl;

import com.demo2do.core.utils.DateUtils;
import com.suidifu.barclays.exception.PullCashflowException;
import com.suidifu.barclays.handler.CashFlowGenericHandler;
import com.suidifu.barclays.handler.CashflowHandler;
import com.suidifu.coffer.GlobalSpec;
import com.suidifu.coffer.entity.CashFlowResultModel;
import com.suidifu.coffer.entity.QueryCashFlowModel;
import com.suidifu.coffer.handler.DirectBankHandler;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.directbank.business.StrikeBalanceStatus;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.yunxin.entity.barclays.ChannelWorkerConfig;
import com.zufangbao.sun.yunxin.service.barclays.ChannelWorkerConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author dafuchen
 *         2017/12/4
 */
@Component("njbcCashflowHandler")
public class NjbcCashflowHandler extends CashFlowGenericHandler implements CashflowHandler {
    @Autowired
    private DirectBankHandler njbcDirectBankHandler;
    @Autowired
    ChannelWorkerConfigService channelWorkerConfigService;

    @Override
    public List<CashFlow> execPullCashflow(ChannelWorkerConfig channelWorkerConfig) throws PullCashflowException {
        Map<String, String> workingParams = channelWorkerConfig.getLocalWorkingConfig();

        if (Objects.isNull(workingParams)) {
            throw new PullCashflowException();
        }

        String queryAccountNo = workingParams.getOrDefault("channelAccountNo", StringUtils.EMPTY);
        if (Objects.isNull(queryAccountNo)) {
            throw new PullCashflowException();
        }

        String pageNo = workingParams.getOrDefault("nextPageNo", "1");
        String pageSize = workingParams.getOrDefault("pageSize", "20");

        int daysBefore = Integer.parseInt(workingParams.getOrDefault("daysBefore", "1"));
        String beginDate = DateUtils.format(DateUtils.addDays(new Date(), -daysBefore), "yyyy-MM-dd");

        QueryCashFlowModel queryCashFlowModel = new QueryCashFlowModel(queryAccountNo, beginDate,
                DateUtils.format(new Date(), "yyyy-MM-dd"),  new Date(), pageNo, pageSize);

        CashFlowResultModel cashFlowResultModel = njbcDirectBankHandler.queryIntradayCashFlow(queryCashFlowModel,
                workingParams);
        //查询失败 返回空结果集合
        if (Objects.equals(GlobalSpec.DEFAULT_FAIL_CODE, cashFlowResultModel.getCommCode())) {
            return new ArrayList<>();
        }

        if (cashFlowResultModel.isHasNextPage()) {
            int pageNoInt = Integer.valueOf(pageNo) + 1;
            workingParams.put("nextPageNo", Integer.toString(pageNoInt));
            channelWorkerConfigService.updateLocalWorkingConfig(channelWorkerConfig.getWorkerUuid(), workingParams);
        }


        return cashFlowResultModel.getCashFlowResult().parallelStream().map(cashFlowResult -> {
            AccountSide accountSide = com.suidifu.coffer.entity.AccountSide.CREDIT.equals(
                    cashFlowResult.getAccountSide()) ? AccountSide.CREDIT : AccountSide.DEBIT;
            StrikeBalanceStatus strikeBalanceStatus = null == cashFlowResult.getStrikeBalanceStatus() ? null :
                    com.suidifu.coffer.entity.StrikeBalanceStatus.NORMAL.equals(cashFlowResult.getStrikeBalanceStatus()) ?
                            StrikeBalanceStatus.NORMAL : StrikeBalanceStatus.STRIKE;

            String remark = cashFlowResult.getRemark();
            String tradeUuid = StringUtils.EMPTY;
            if(StringUtils.isNotEmpty(remark)) {
                String regex = "\\((.*?)\\)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(remark);
                if(null != matcher && matcher.find(0)) {
                    tradeUuid = matcher.group(1);
                }
            }

            CashFlow cashFlow = new CashFlow(UUID.randomUUID().toString(),
                    CashFlowChannelType.DirectBank,
                    null,
                    null,
                    cashFlowResult.getHostAccountNo(),
                    cashFlowResult.getHostAccountName(),
                    cashFlowResult.getCounterAccountNo(),
                    cashFlowResult.getCounterAccountName(),
                    cashFlowResult.getCounterAccountAppendix(),
                    cashFlowResult.getCounterBankInfo(),
                    accountSide,
                    cashFlowResult.getTransactionTime(),
                    cashFlowResult.getTransactionAmount(),
                    cashFlowResult.getBalance(),
                    cashFlowResult.getTransactionVoucherNo(),
                    cashFlowResult.getBankSequenceNo(),
                    cashFlowResult.getRemark(),
                    cashFlowResult.getOtherRemark(),
                    strikeBalanceStatus,
                    tradeUuid);

            String flag = super.getFlag(cashFlowResult);
            cashFlow.setStringFieldTwo(flag);

            return cashFlow;

        }).collect(Collectors.toList());
    }
}
