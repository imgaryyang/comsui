package com.zufangbao.earth.yunxin.api.handler.impl;

import com.demo2do.core.utils.StringUtils;
import com.zufangbao.earth.yunxin.api.model.PagedTradeList;
import com.zufangbao.earth.yunxin.api.model.query.AccountTradeDetailModel;
import com.zufangbao.earth.yunxin.api.model.query.AccountTradeList;
import com.zufangbao.gluon.spec.barclays.BillRedisKey;
import com.zufangbao.gluon.spec.earth.v3.ApiResponseCode;
import com.zufangbao.sun.entity.account.Account;
import com.zufangbao.sun.entity.directbank.business.CashFlow;
import com.zufangbao.sun.entity.directbank.business.CashFlowChannelType;
import com.zufangbao.sun.entity.financial.FinancialContract;
import com.zufangbao.sun.entity.financial.PaymentInstitutionName;
import com.zufangbao.sun.ledgerbook.AccountSide;
import com.zufangbao.sun.service.CashFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by whb on 17-7-5.
 */
@Component("directBankTradeListApiHandler")
public class DirectBankTradeListApiHandlerImpl extends TradeListApiHandlerImpl {
    private static final Logger logger = LoggerFactory.getLogger(DirectBankTradeListApiHandlerImpl.class);
    @Autowired
    private CashFlowService cashFlowService;

    @Autowired
    private ZSetOperations<String, CashFlow> cashFlowZSetOperations;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("#{config['queryCashFlowFromCacheSwitch']}")
    private String queryCashFlowFromCacheSwitch;

    private final String TURN_ON_QUERY_CASH_FLOW_FROM_CACHE = "on";

    @Override
    public PagedTradeList findAccountTradeListBy(String productNo,
                                                 String capitalAccountNo,
                                                 PaymentInstitutionName paymentInstitutionName,
                                                 Date startDate,
                                                 Date endDate,
                                                 Integer pageNumber,
                                                 AccountSide accountSide) {
        String cleanUpTimeKey = BillRedisKey.CASH_FLOW_CLEAN_UP_NAMESPACE + capitalAccountNo;
        String cleanUpTimeValue = stringRedisTemplate.opsForValue().get(cleanUpTimeKey);
        Date cleanUpDate = null;
        if (! StringUtils.isEmpty(cleanUpTimeValue)) {
            cleanUpDate = new Date(Long.parseLong(cleanUpTimeValue));
        }

        // 如果查询的时间是在上次清理的时间之后的
        // 说明cache 里面是有保存的数据的
        Collection<CashFlow> cashFlowCollection = null;
        boolean nextPageFlag;
        int beginIndex = (pageNumber - 1) * getSizeOfPerPage();
        int offset = getSizeOfPerPage() * 2;

        if (! Objects.isNull(cleanUpDate) &&
                startDate.compareTo(cleanUpDate) >= 0 &&
                TURN_ON_QUERY_CASH_FLOW_FROM_CACHE.equals(queryCashFlowFromCacheSwitch)) {
            String storeKey = BillRedisKey.CASH_FLOW_SAVE_NAMESPACE + capitalAccountNo;
            long maxScore = endDate.getTime();
            long minScore = startDate.getTime();
            long start = System.currentTimeMillis();
            cashFlowCollection = cashFlowZSetOperations.rangeByScore(storeKey, minScore, maxScore, beginIndex, offset);
            long end = System.currentTimeMillis();
            logger.info("DirectBankTradeListApiHandlerImpl# query data from redis the size is {} the time cost is {}",
                    cashFlowCollection.size(), end - start);
        }
        // 到db 里面找
        // 1 时间间隔 不在db的范围内
        // 2 没有命中缓存
        if (Objects.isNull(cashFlowCollection) ||
                cashFlowCollection.isEmpty()){
            // 从数据库里面拿
            long start = System.currentTimeMillis();
            cashFlowCollection = cashFlowService.getCashFlowsBy(capitalAccountNo,
                    CashFlowChannelType.DirectBank,
                    startDate,
                    endDate,
                    beginIndex,
                    offset,
                    accountSide
            );
            long end = System.currentTimeMillis();
            logger.info("DirectBankTradeListApiHandlerImpl# query data from mysql the size is {} the time cost is {}",
                    cashFlowCollection.size(), end - start);
        }
        nextPageFlag = ifHasNextPage(cashFlowCollection);
        List<AccountTradeList> tradeList =  cashFlowCollection.parallelStream().limit(getSizeOfPerPage()).map(
                cashFlow -> {
                    return new AccountTradeList(cashFlow.getBankSequenceNo(),
                            cashFlow.getAccountSide().getOrdinal(),
                            cashFlow.getCounterAccountNo(),
                            cashFlow.getCounterAccountName(),
                            cashFlow.getCounterBankName(),
                            cashFlow.getTransactionAmount(),
                            cashFlow.getTransactionTime(),
                            cashFlow.getRemark(),
                            cashFlow.getOtherRemark(),
                            "",
                            cashFlow.getTradeUuid() == null ? "" : cashFlow.getTradeUuid());
                }
        ).collect(Collectors.toList());
        
        PagedTradeList pagedList=new PagedTradeList();
        
        pagedList.setTradeList(tradeList);
        pagedList.setHasNextPage(nextPageFlag);
        return pagedList;
    }


    @Override
    public Integer verifyAccountTradeDetailModel(AccountTradeDetailModel accountTradeDetailModel){
        Integer verifyResult = super.verifyAccountTradeDetailModel(accountTradeDetailModel);
        if(Objects.equals(0, verifyResult)){
            FinancialContract financialContract = financialContractService.
                    getUniqueFinancialContractBy(accountTradeDetailModel.getProductCode());
            if(null == financialContract){
                return ApiResponseCode.FINANCIAL_CONTRACT_NOT_EXIST;
            }
            Account account = financialContract.getCapitalAccount();
            if( null == account){
                return ApiResponseCode.ACCOUNT_INFO_NOT_EXIST_WITH_THIS_FINANCIAL_NO;
            }
            String accountNo = account.getAccountNo();
            if(StringUtils.isEmpty(accountNo)){
                return ApiResponseCode.ACCOUNT_INFO_NOT_EXIST_WITH_THIS_FINANCIAL_NO;
            }
            if(! Objects.equals(accountNo, accountTradeDetailModel.getCapitalAccountNo())){
                return ApiResponseCode.FINANCIAL_NO_CANNOT_MATCH_WITH_ACCOUNT_NO;
            }
            return 0;
        }else{
            return verifyResult;
        }
    }
}
