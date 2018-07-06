package com.suidifu.dowjones.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suidifu.dowjones.dao.AssetSetDAO;
import com.suidifu.dowjones.dao.RelatedContractDAO;
import com.suidifu.dowjones.model.AssetSet;
import com.suidifu.dowjones.model.RelatedContract;
import com.suidifu.dowjones.service.CustomerService;
import com.suidifu.dowjones.utils.ExtraChargeSpec;
import com.suidifu.dowjones.utils.Money;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import static com.suidifu.dowjones.utils.ExtraChargeSpec.SND_UNEARNED_LOAN_ASSET_INTEREST;
import static com.suidifu.dowjones.utils.ExtraChargeSpec.SND_UNEARNED_LOAN_ASSET_PRINCIPLE;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/6 <br>
 * @time: 下午7:21 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService, Serializable {
    private static final Boolean NORMAL = false;//已还
    private static final Boolean OVER_DUE = true;//未还
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Resource
    private transient AssetSetDAO assetSetDAO;
    @Resource
    private transient RelatedContractDAO relatedContractDAO;

    @Override
    public String extractContractInfoByIDNumber(String idNumber) throws IOException, ParseException {
        Dataset<Row> relatedContract = relatedContractDAO.getRelatedContract(idNumber);
        log.info("relatedContract count is:{}", relatedContract.count());

        return OBJECT_MAPPER.writeValueAsString(relatedContract.as(Encoders.bean(RelatedContract.class)).collectAsList());
    }

    @Override
    public String getStatisticByIDNumber(String idNumber) throws IOException {
        Map<Boolean, Money> moneyStatisticsMap = new HashMap<>();

        // 二元操作符
        // 将两个map中的里面的 本金 和 利息 分别进行相加
        // 返回一个map 这个 map 里面有相加后的 本金和利息的值
        BinaryOperator<Map<String, BigDecimal>> sumUpThePrincipalAndInterest =
                (Map<String, BigDecimal> a, Map<String, BigDecimal> b) -> {
                    BigDecimal principleSum = a.getOrDefault(SND_UNEARNED_LOAN_ASSET_PRINCIPLE,
                            BigDecimal.ZERO).add(b.getOrDefault(SND_UNEARNED_LOAN_ASSET_PRINCIPLE,
                            BigDecimal.ZERO));
                    BigDecimal interestSum = a.getOrDefault(SND_UNEARNED_LOAN_ASSET_INTEREST,
                            BigDecimal.ZERO).add(b.getOrDefault(SND_UNEARNED_LOAN_ASSET_INTEREST,
                            BigDecimal.ZERO));
                    Map<String, BigDecimal> result = new HashMap<>(2);
                    result.put(SND_UNEARNED_LOAN_ASSET_PRINCIPLE, principleSum);
                    result.put(SND_UNEARNED_LOAN_ASSET_INTEREST, interestSum);
                    return result;
                };

        // 默认值
        Map<String, BigDecimal> defaultValue = new HashMap<>(2);
        defaultValue.put(SND_UNEARNED_LOAN_ASSET_PRINCIPLE, BigDecimal.ZERO);
        defaultValue.put(SND_UNEARNED_LOAN_ASSET_INTEREST, BigDecimal.ZERO);

        Dataset<Row> assetSets = assetSetDAO.getAllOpenRepaymentPlanList(idNumber);

        List<AssetSet> paidMoneyData = assetSets.filter("thirdAccountUuid IN ('60000.1000.01'," +
                "'60000.1000.02','50000.06.01','50000.06.02')").
                as(Encoders.bean(AssetSet.class)).collectAsList();
        log.info("paidMoneyData count is:{}", paidMoneyData.size());

        List<AssetSet> noPaidMoneyData = assetSets.filter("(thirdAccountUuid IN ('20000.01.01', " +
                "'20000.01.02','20000.05.01','20000.05.02')" +
                " OR secondAccountUuid IN ('10000.01', '10000.02'))").
                as(Encoders.bean(AssetSet.class)).collectAsList();
        log.info("noPaidMoneyData count is:{}", noPaidMoneyData.size());

        paidMoneyData.parallelStream().collect(groupingBy(AssetSet::overDueStatus)).forEach(
                (assertOverDueStatus, action) -> {// 已还
                    Map<String, BigDecimal> paidMoney = action.stream().map(assetSet ->
                            getExtraChargeKeyAndAmountMap(assetSet, ExtraChargeSpec.BANKSAVING_INDEPEND_ACCOUNT_AND_EXTRA_CHARGE_MAP))
                            .reduce(sumUpThePrincipalAndInterest).orElse(defaultValue);

                    Money money = moneyStatisticsMap.get(assertOverDueStatus);
                    money.setPaidInterest(money.getPaidInterest().add(paidMoney.getOrDefault(
                            SND_UNEARNED_LOAN_ASSET_INTEREST, BigDecimal.ZERO)));

                    money.setPaidPrinciple(money.getPaidPrinciple().add(paidMoney.getOrDefault(
                            SND_UNEARNED_LOAN_ASSET_INTEREST, BigDecimal.ZERO)));
                });

        noPaidMoneyData.parallelStream().collect(groupingBy(AssetSet::overDueStatus)).forEach(
                (assertOverDueStatus, action) -> {// 未还
                    Map<String, BigDecimal> notPaidMoney = action.stream().map(assetSet ->
                            getExtraChargeKeyAndAmountMap(assetSet, ExtraChargeSpec.RECEIVABLE_ASSET_AND_EXTRA_CHARGE_MAP))
                            .reduce(sumUpThePrincipalAndInterest).orElse(defaultValue);

                    Money money = moneyStatisticsMap.get(assertOverDueStatus);
                    money.setNotPaidInterest(money.getNotPaidInterest().add(notPaidMoney.getOrDefault(
                            SND_UNEARNED_LOAN_ASSET_INTEREST, BigDecimal.ZERO)));

                    money.setNotPaidPrinciple(money.getNotPaidPrinciple().add(notPaidMoney.getOrDefault(
                            SND_UNEARNED_LOAN_ASSET_INTEREST, BigDecimal.ZERO)));
                });

        return OBJECT_MAPPER.writeValueAsString(generateDataMap(moneyStatisticsMap));
    }

    private Map<String, Object> generateDataMap(Map<Boolean, Money> moneyStatisticsMap) {
        Map<String, Object> data = new HashMap<>();
        if (moneyStatisticsMap.size() == 0) {
            return data;
        }

        Money normalMoney = moneyStatisticsMap.get(NORMAL);
        Money overDueMoney = moneyStatisticsMap.get(OVER_DUE);

        BigDecimal totalPrincipal = normalMoney.getPaidPrinciple().
                add(normalMoney.getNotPaidPrinciple()).
                add(overDueMoney.getPaidPrinciple()).
                add(overDueMoney.getNotPaidPrinciple());
        data.put("totalPrincipal", totalPrincipal);

        BigDecimal totalInterest = normalMoney.getPaidInterest().
                add(normalMoney.getNotPaidInterest()).
                add(overDueMoney.getPaidInterest()).
                add(overDueMoney.getNotPaidInterest());
        data.put("totalInterest", totalInterest);

        BigDecimal paidPrincipal = normalMoney.getPaidPrinciple().
                add(overDueMoney.getPaidPrinciple());
        data.put("paidPrincipal", paidPrincipal);

        BigDecimal paidInterest = normalMoney.getPaidInterest().
                add(overDueMoney.getPaidInterest());
        data.put("paidInterest", paidInterest);

        BigDecimal notPaidPrincipal = normalMoney.getNotPaidPrinciple().
                add(overDueMoney.getNotPaidPrinciple());
        data.put("notPaidPrincipal", notPaidPrincipal);

        data.put("normalPrincipal", normalMoney.getPaidPrinciple());
        data.put("normalInterest", normalMoney.getNotPaidInterest());
        data.put("overDuePrincipal", overDueMoney.getNotPaidPrinciple());
        data.put("overDueInterest", overDueMoney.getNotPaidInterest());
        return data;
    }

    private Map<String, BigDecimal> getExtraChargeKeyAndAmountMap(AssetSet assetSet, Map<String, String> extraChargeMap) {
        Map<String, BigDecimal> extraChargeKeyAndAmountMap = new HashMap<>();

        String extraChargeKey = extraChargeMap.get(lastAccountName(assetSet));
        BigDecimal amount = extraChargeKeyAndAmountMap.getOrDefault(extraChargeKey, BigDecimal.ZERO);
        extraChargeKeyAndAmountMap.put(extraChargeKey, amount.add(getBalance(assetSet)));

        return extraChargeKeyAndAmountMap;
    }

    private BigDecimal getBalance(AssetSet assetSet) {
        return assetSet.getDebitBalance().subtract(assetSet.getCreditBalance());
    }

    private String lastAccountName(AssetSet assetSet) {
        String thirdAccountName = assetSet.getThirdAccountName();
        String secondAccountName = assetSet.getSecondAccountName();
        String firstAccountName = assetSet.getFirstAccountName();
        if (!StringUtils.isEmpty(thirdAccountName)) {
            return thirdAccountName;
        }
        if (!StringUtils.isEmpty(secondAccountName)) {
            return secondAccountName;
        }
        if (!StringUtils.isEmpty(firstAccountName)) {
            return firstAccountName;
        }
        return "";
    }
}