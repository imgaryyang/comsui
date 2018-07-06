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
 * Created by Chen dafu on 17-8-15.
 */
@Component("spdbCashflowHandler")
public class SpdbCashflowHandler extends CashFlowGenericHandler implements CashflowHandler {
    @Autowired
    private DirectBankHandler spdbDirectBankHandler;
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
        String pageSize = workingParams.getOrDefault("pageSize", GlobalSpec.BankCorpEps.SPDB_DEFAULT_PAGE_SIZE);

        int daysBefore = Integer.parseInt(workingParams.getOrDefault("daysBefore", "1"));
        String beginDate = DateUtils.format(DateUtils.addDays(new Date(), -daysBefore), "yyyyMMdd");

        QueryCashFlowModel queryCashFlowModel = new QueryCashFlowModel(queryAccountNo, beginDate,
                DateUtils.format(new Date(), "yyyyMMdd"), new Date(), pageNo, pageSize);

        CashFlowResultModel cashFlowResultModel = spdbDirectBankHandler.queryIntradayCashFlow(queryCashFlowModel,
                workingParams);
        if (Objects.equals(GlobalSpec.DEFAULT_FAIL_CODE, cashFlowResultModel.getCommCode())) {
            return new ArrayList<CashFlow>();
        }

        if (cashFlowResultModel.isHasNextPage()) {
            int pageNoInt = Integer.valueOf(pageNo) + 1;
            workingParams.put("nextPageNo", Integer.toString(pageNoInt));
            channelWorkerConfigService.updateLocalWorkingConfig(channelWorkerConfig.getWorkerUuid(), workingParams);
        }
        return cashFlowResultModel.getCashFlowResult().stream().map(cashFlowResult -> {
            AccountSide accountSide = com.suidifu.coffer.entity.AccountSide.CREDIT.equals(
                    cashFlowResult.getAccountSide()) ? AccountSide.CREDIT : AccountSide.DEBIT;
            StrikeBalanceStatus strikeBalanceStatus = null == cashFlowResult.getStrikeBalanceStatus() ? null :
                    com.suidifu.coffer.entity.StrikeBalanceStatus.NORMAL.equals(cashFlowResult.getStrikeBalanceStatus()) ?
                            StrikeBalanceStatus.NORMAL : StrikeBalanceStatus.STRIKE;

			String tradeUuid = StringUtils.EMPTY;
			String remark = cashFlowResult.getRemark();
			if(StringUtils.isNotEmpty(remark)) {
				String regex = "\\((.*?)\\)";//TODO 抽spec
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
