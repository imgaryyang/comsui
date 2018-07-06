package com.suidifu.dowjones.service.impl;

import com.suidifu.dowjones.dao.OverdueAnalyzeDAO;
import com.suidifu.dowjones.exception.DowjonesException;
import com.suidifu.dowjones.exception.ResponseStatus;
import com.suidifu.dowjones.model.StatisticsReport;
import com.suidifu.dowjones.service.OverdueAnalyzeService;
import com.suidifu.dowjones.utils.ArrayUtils;
import com.suidifu.dowjones.utils.Constants;
import com.suidifu.dowjones.utils.MathUtils;
import com.suidifu.dowjones.vo.enumeration.RepurchaseMode;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/21 <br>
 * @time: 下午1:55 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Service
@Slf4j
public class OverdueAnalyzeServiceImpl implements OverdueAnalyzeService {
    @Resource
    private OverdueAnalyzeDAO overdueAnalyzeDAO;
    @Setter
    @Getter
    private int count;

    @Override
    public List<StatisticsReport> getDynamicOverdueRate(InputParameter inputParameter) throws IOException, DowjonesException {
        StatisticsReport statisticsReport = new StatisticsReport();
        statisticsReport.setStatisticsDate(DateFormatUtils.format(new Date(), Constants.DATE_PATTERN));
        if (inputParameter.getComputeTypeFlag() == 0) {//计算类型：0剩余本金
            BigDecimal overdueUnearnedPrincipal = getOverdueUnearnedPrincipal(inputParameter);
            BigDecimal remainingLoanPrincipal = getRemainingLoanPrincipal(inputParameter);

            log.info("\n\n\n\n\n逾期未偿本金:{},剩余贷款本金:{}\n\n\n\n\n",
                    overdueUnearnedPrincipal, remainingLoanPrincipal);

            if (remainingLoanPrincipal.toString().equals("0.00")) {
                throw new DowjonesException(ResponseStatus.EXCEPTION_INFO.getCode(), "剩余贷款本金无");
            }

            //动态池逾期率
            BigDecimal remainingPrincipalRate = MathUtils.divide(overdueUnearnedPrincipal,
                    remainingLoanPrincipal).multiply(new BigDecimal(100));

            log.info("\n\n\n\n\n逾期未偿本金:{},剩余贷款本金:{},动态池逾期率-剩余本金:{}\n\n\n\n\n",
                    overdueUnearnedPrincipal, remainingLoanPrincipal, remainingPrincipalRate);

            statisticsReport.setComputeType(inputParameter.getComputeTypeFlag());
            statisticsReport.setDenominator(overdueUnearnedPrincipal.toPlainString());
            statisticsReport.setDenominatorDesc("逾期未偿本金");
            statisticsReport.setNumerator(remainingLoanPrincipal.toPlainString());
            statisticsReport.setNumeratorDesc("剩余贷款本金");
            statisticsReport.setQuotient(remainingPrincipalRate + "%");
            statisticsReport.setQuotientDesc("动态池逾期率(剩余本金)");
            statisticsReport.setStatisticsNumber(getCount());
        }

        if (inputParameter.getComputeTypeFlag() == 1) {//计算类型：1剩余本息
            BigDecimal overdueUnearnedPrincipalWithInterest = getOverdueUnearnedPrincipalWithInterest(inputParameter);
            BigDecimal remainingLoanPrincipalWithInterest = getRemainingLoanPrincipalWithInterest(inputParameter);

            log.info("\n\n\n\n\n逾期未偿本息:{},剩余贷款本息:{}\n\n\n\n\n",
                    overdueUnearnedPrincipalWithInterest, remainingLoanPrincipalWithInterest);

            if (remainingLoanPrincipalWithInterest.toString().equals("0.00")) {
                throw new DowjonesException(ResponseStatus.EXCEPTION_INFO.getCode(), "剩余贷款本息无");
            }
            //动态池逾期率
            BigDecimal remainingPrincipalWithInterestRate = MathUtils.divide(overdueUnearnedPrincipalWithInterest,
                    remainingLoanPrincipalWithInterest).multiply(new BigDecimal(100));

            log.info("\n\n\n\n\n逾期未偿本息:{},剩余贷款本息:{},动态池逾期率-剩余本息:{}\n\n\n\n\n",
                    overdueUnearnedPrincipalWithInterest,
                    remainingLoanPrincipalWithInterest,
                    remainingPrincipalWithInterestRate);

            statisticsReport.setComputeType(inputParameter.getComputeTypeFlag());
            statisticsReport.setDenominator(overdueUnearnedPrincipalWithInterest.toPlainString());
            statisticsReport.setDenominatorDesc("逾期未偿本息");
            statisticsReport.setNumerator(remainingLoanPrincipalWithInterest.toPlainString());
            statisticsReport.setNumeratorDesc("剩余贷款本息");
            statisticsReport.setQuotient(remainingPrincipalWithInterestRate + "%");
            statisticsReport.setQuotientDesc("动态池逾期率(剩余本息)");
            statisticsReport.setStatisticsNumber(getCount());
        }

        statisticsReport.setFormulaId(Constants.FORMULA_DYNAMIC);
        overdueAnalyzeDAO.saveData(statisticsReport);
        return overdueAnalyzeDAO.loadAllStatisticsReport(statisticsReport);
    }

    @Override
    public List<StatisticsReport> getStaticOverdueRate(StaticOverdueRateInputParameter inputParameter) throws IOException, DowjonesException {
        if (inputParameter.getYear() != Calendar.getInstance().get(Calendar.YEAR)) {
            throw new DowjonesException(ResponseStatus.EXCEPTION_INFO.getCode(), "请输入当前年份，而不是" + inputParameter.getYear());
        }

        StatisticsReport statisticsReport = new StatisticsReport();
        statisticsReport.setStatisticsDate(DateFormatUtils.format(new Date(), Constants.DATE_PATTERN));
        if (inputParameter.getComputeTypeFlag() == 0) {//计算类型：0剩余本金
            BigDecimal overdueLoanPrincipalByMonth = getMonthlyOverdueLoanPrincipal(inputParameter);
            BigDecimal remittanceTotalAmountByMonth = overdueAnalyzeDAO.getRemittanceTotalAmountByMonth(inputParameter);

            if (remittanceTotalAmountByMonth.toString().equals("0")) {
                throw new DowjonesException(ResponseStatus.EXCEPTION_INFO.getCode(), inputParameter.getMonth() + "月放款本金无");
            }

            log.info("\n\n\n\n\nX月发放贷款剩余逾期本金:{},X月放款本金:{}\n\n\n\n\n",
                    overdueLoanPrincipalByMonth, remittanceTotalAmountByMonth);

            //月度静态池逾期率
            BigDecimal remainingPrincipalRate = MathUtils.divide(overdueLoanPrincipalByMonth,
                    remittanceTotalAmountByMonth).multiply(new BigDecimal(100));

            log.info("\n\n\n\n\nX月发放贷款剩余逾期本金:{},X月放款本金:{},月度静态池逾期率-剩余本金:{}\n\n\n\n\n",
                    overdueLoanPrincipalByMonth, remittanceTotalAmountByMonth, remainingPrincipalRate);

            statisticsReport.setComputeType(inputParameter.getComputeTypeFlag());
            statisticsReport.setDenominator(overdueLoanPrincipalByMonth.toPlainString());
            statisticsReport.setDenominatorDesc(inputParameter.getMonth() +"月发放贷款剩余逾期本金");
            statisticsReport.setNumerator(remittanceTotalAmountByMonth.toPlainString());
            statisticsReport.setNumeratorDesc(inputParameter.getMonth() +"月放款本金");
            statisticsReport.setQuotient(remainingPrincipalRate.toPlainString() + "%");
            statisticsReport.setQuotientDesc("月度静态池逾期率(剩余本金)");
            statisticsReport.setStatisticsNumber(getCount());
        }

        if (inputParameter.getComputeTypeFlag() == 1) {//计算类型：1剩余本息
            BigDecimal overdueLoanPrincipalWithInterestByMonth = getOverdueLoanPrincipalWithInterestByMonth(inputParameter);
            BigDecimal remittanceRemainingLoanPrincipalWithInterestByMonth = getRemittanceRemainingLoanPrincipalWithInterestByMonth(inputParameter);

            if (remittanceRemainingLoanPrincipalWithInterestByMonth.toString().equals("0")) {
                throw new DowjonesException(ResponseStatus.EXCEPTION_INFO.getCode(), inputParameter.getMonth() + "月放款本息无");
            }

            log.info("\n\n\n\n\nX月发放贷款剩余逾期本息:{},X月发放贷款的贷款本息合计:{}\n\n\n\n\n",
                    overdueLoanPrincipalWithInterestByMonth, remittanceRemainingLoanPrincipalWithInterestByMonth);

            //月度静态池逾期率
            BigDecimal remainingPrincipalWithInterestRate = MathUtils.divide(overdueLoanPrincipalWithInterestByMonth,
                    remittanceRemainingLoanPrincipalWithInterestByMonth).multiply(new BigDecimal(100));

            log.info("\n\n\n\n\nX月发放贷款剩余逾期本息:{},X月发放贷款的贷款本息合计:{},月度静态池逾期率-剩余本息:{}\n\n\n\n\n", overdueLoanPrincipalWithInterestByMonth,
                    remittanceRemainingLoanPrincipalWithInterestByMonth,
                    remainingPrincipalWithInterestRate);

            statisticsReport.setComputeType(inputParameter.getComputeTypeFlag());
            statisticsReport.setDenominator(overdueLoanPrincipalWithInterestByMonth.toPlainString());
            statisticsReport.setDenominatorDesc(inputParameter.getMonth() +"月发放贷款剩余逾期本息");
            statisticsReport.setNumerator(remittanceRemainingLoanPrincipalWithInterestByMonth.toPlainString());
            statisticsReport.setNumeratorDesc(inputParameter.getMonth() +"月发放贷款的贷款本息合计");
            statisticsReport.setQuotient(remainingPrincipalWithInterestRate.toPlainString() + "%");
            statisticsReport.setQuotientDesc("月度静态池逾期率(剩余本息)");
            statisticsReport.setStatisticsNumber(getCount());
        }

        statisticsReport.setFormulaId(Constants.FORMULA_STATIC);
        overdueAnalyzeDAO.saveData(statisticsReport);
        return overdueAnalyzeDAO.loadAllStatisticsReport(statisticsReport);
    }

    /**
     * 1.逾期未偿本金 (统计范围内，所有逾期还款计划涉及贷款合同的未偿本金)
     * #只统计当日或当日前逾期的还款计划
     *
     * @param inputParameter 输入的计算参数
     * @return 逾期未偿本金
     */
    private BigDecimal getOverdueUnearnedPrincipal(InputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            String[] contractUuidsOfM1 = getContractUuidsBy(inputParameter, RepurchaseMode.M1);
            setCount(contractUuidsOfM1.length);

            return overdueAnalyzeDAO.getUnEarnedPrincipalFromLedgerBookBy(inputParameter,
                    contractUuidsOfM1);
        } else {
            String[] contractUuidsOfM2 = getContractUuidsBy(inputParameter, RepurchaseMode.M2);
            String[] contractUuidsOfM3 = getContractUuidsBy(inputParameter, RepurchaseMode.M3);
            setCount(contractUuidsOfM2.length + contractUuidsOfM3.length);

            return overdueAnalyzeDAO.getUnEarnedPrincipalFromLedgerBookBy(inputParameter,
                    contractUuidsOfM2).add(overdueAnalyzeDAO.
                    getUnEarnedPrincipalFromRepurchaseBy(contractUuidsOfM3, false));
        }
    }

    /**
     * 2.逾期未偿本息 (统计范围内，所有逾期还款计划涉及贷款合同的未偿本息)
     *
     * @param inputParameter 输入的计算参数
     * @return 逾期未偿本息
     */
    private BigDecimal getOverdueUnearnedPrincipalWithInterest(InputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            String[] contractUuidsOfM1 = getContractUuidsBy(inputParameter, RepurchaseMode.M1);
            setCount(contractUuidsOfM1.length);

            return overdueAnalyzeDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    contractUuidsOfM1);
        } else {
            String[] contractUuidsOfM2 = getContractUuidsBy(inputParameter, RepurchaseMode.M2);
            String[] contractUuidsOfM3 = getContractUuidsBy(inputParameter, RepurchaseMode.M3);
            setCount(contractUuidsOfM2.length + contractUuidsOfM3.length);

            return overdueAnalyzeDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    contractUuidsOfM2).add(overdueAnalyzeDAO.
                    getUnEarnedPrincipalFromRepurchaseBy(contractUuidsOfM3, true));
        }
    }

    /**
     * 3.剩余贷款本金 (统计范围内，所有未结清【已生效,回购中,违约】还款计划涉及贷款合同的未偿本金)
     *
     * @param inputParameter 输入的计算参数
     * @return 剩余贷款本金
     */
    private BigDecimal getRemainingLoanPrincipal(InputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            return overdueAnalyzeDAO.getUnEarnedPrincipalFromLedgerBookBy(inputParameter,
                    overdueAnalyzeDAO.getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M1));
        } else {
            return overdueAnalyzeDAO.getUnEarnedPrincipalFromLedgerBookBy(inputParameter,
                    overdueAnalyzeDAO.getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M2)).
                    add(overdueAnalyzeDAO.getUnEarnedPrincipalFromRepurchaseBy(
                            overdueAnalyzeDAO.getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M3), false));
        }
    }

    /**
     * 4.剩余贷款本息 (统计范围内，所有未结清【已生效,回购中,违约】还款计划涉及贷款合同的未偿本息)
     *
     * @param inputParameter 输入的计算参数
     * @return 剩余贷款本息
     */
    private BigDecimal getRemainingLoanPrincipalWithInterest(InputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            return overdueAnalyzeDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    overdueAnalyzeDAO.getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M1));
        } else {
            return overdueAnalyzeDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    overdueAnalyzeDAO.getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M2)).
                    add(overdueAnalyzeDAO.getUnEarnedPrincipalFromRepurchaseBy(
                            overdueAnalyzeDAO.getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M3), true));
        }
    }

    /**
     * 6.X月发放贷款逾期本金 (X月份放款成功的贷款合同下，逾期状态为“已逾期”或“已逾期”+“待确认”的还款计划所涉及贷款合同的未偿本金)
     *
     * @param inputParameter 输入的计算参数
     * @return X月发放贷款逾期本金
     */
    private BigDecimal getMonthlyOverdueLoanPrincipal(StaticOverdueRateInputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            String[] contractUuidsOfM1 = getOverdueLoanPrincipalBy(inputParameter, RepurchaseMode.M1);
            setCount(contractUuidsOfM1.length);

            return overdueAnalyzeDAO.getUnEarnedPrincipalFromLedgerBookBy(inputParameter, contractUuidsOfM1);
        } else {
            String[] contractUuidsOfM2 = getOverdueLoanPrincipalBy(inputParameter, RepurchaseMode.M2);
            String[] contractUuidsOfM3 = getOverdueLoanPrincipalBy(inputParameter, RepurchaseMode.M3);
            setCount(contractUuidsOfM2.length + contractUuidsOfM3.length);

            return overdueAnalyzeDAO.getUnEarnedPrincipalFromLedgerBookBy(inputParameter,
                    contractUuidsOfM2).add(overdueAnalyzeDAO.
                    getUnEarnedPrincipalFromRepurchaseBy(contractUuidsOfM3, false));
        }
    }

    /**
     * 7.X月发放贷款逾期本息合计 (X月份放款成功的贷款合同下，逾期状态为“已逾期”或“已逾期”+“待确认”的还款计划所涉及贷款合同的未偿本息)
     *
     * @param inputParameter 输入的计算参数
     * @return X月发放贷款逾期本息合计
     */
    private BigDecimal getOverdueLoanPrincipalWithInterestByMonth(StaticOverdueRateInputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            String[] contractUuidsOfM1 = getOverdueLoanPrincipalBy(inputParameter, RepurchaseMode.M1);
            setCount(contractUuidsOfM1.length);

            return overdueAnalyzeDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter, contractUuidsOfM1);
        } else {
            String[] contractUuidsOfM2 = getOverdueLoanPrincipalBy(inputParameter, RepurchaseMode.M2);
            String[] contractUuidsOfM3 = getOverdueLoanPrincipalBy(inputParameter, RepurchaseMode.M3);
            setCount(contractUuidsOfM2.length + contractUuidsOfM3.length);

            return overdueAnalyzeDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    contractUuidsOfM2).add(overdueAnalyzeDAO.
                    getUnEarnedPrincipalFromRepurchaseBy(contractUuidsOfM3, true));
        }
    }

    /**
     * 8.X月发放贷款的剩余贷款本息合计 (X月份放款成功的贷款合同下，所有【已生效、回购中、违约】的贷款合同未还本息不含罚息)
     *
     * @param inputParameter 输入的计算参数
     * @return X月发放贷款的剩余贷款本息合计
     */
    private BigDecimal getRemittanceRemainingLoanPrincipalWithInterestByMonth(StaticOverdueRateInputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            return overdueAnalyzeDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    overdueAnalyzeDAO.getRemittanceUnclearLoansBy(inputParameter, RepurchaseMode.M1));
        } else {
            return overdueAnalyzeDAO.getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    overdueAnalyzeDAO.getRemittanceUnclearLoansBy(inputParameter, RepurchaseMode.M2)).
                    add(overdueAnalyzeDAO.getUnEarnedPrincipalFromRepurchaseBy(
                            overdueAnalyzeDAO.getRemittanceUnclearLoansBy(inputParameter, RepurchaseMode.M3), true));
        }
    }

    private String[] getContractUuidsBy(InputParameter inputParameter, RepurchaseMode repurchaseMode) {
        String[] contractUuids = ArrayUtils.removeNullAndRepeatedElement(
                overdueAnalyzeDAO.getContractUuidsByOverdueAssetSet(inputParameter, repurchaseMode));
        if (inputParameter.isIncludeUnconfirmed()) {
            contractUuids = ArrayUtils.mergeStringArrays(contractUuids,
                    overdueAnalyzeDAO.getContractUuidsByUnConfirmedAssetSet(inputParameter, repurchaseMode));
        }

        return contractUuids;
    }

    private String[] getOverdueLoanPrincipalBy(StaticOverdueRateInputParameter inputParameter,
                                               RepurchaseMode repurchaseMode) {
        String[] contractUuids = ArrayUtils.removeNullAndRepeatedElement(
                overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndOverdueAssetSet(inputParameter, repurchaseMode));
        if (inputParameter.isIncludeUnconfirmed()) {
            contractUuids = ArrayUtils.mergeStringArrays(contractUuids,
                    overdueAnalyzeDAO.getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(inputParameter, repurchaseMode));
        }

        return contractUuids;
    }
}