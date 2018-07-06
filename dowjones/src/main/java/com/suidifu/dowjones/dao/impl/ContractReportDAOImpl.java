package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.ContractReportDAO;
import com.suidifu.dowjones.exception.ContractReportRuntimeException;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import com.suidifu.dowjones.utils.MathUtils;
import com.suidifu.dowjones.vo.enumeration.AssetClearStatus;
import com.suidifu.dowjones.vo.enumeration.RepurchaseMode;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component("contractReportDAO")
public class ContractReportDAOImpl implements ContractReportDAO {
    @Resource
    private GenericJdbcSupport genericDaoSupport;

    /**
     * 1.逾期未偿本金 (统计范围内，所有逾期还款计划涉及贷款合同的未偿本金) #只统计当日或当日前逾期的还款计划
     *
     * @param inputParameter 输入的计算参数
     * @return 逾期未偿本金
     */
    // @Override
    public BigDecimal getOverdueUnearnedPrincipal(InputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            return getUnEarnedPrincipalFromLedgerBookBy(inputParameter,
                    getContractUuidsBy(inputParameter, RepurchaseMode.M1));
        } else {
            return getUnEarnedPrincipalFromLedgerBookBy(inputParameter,
                    getContractUuidsBy(inputParameter, RepurchaseMode.M2))
                    .add(getUnEarnedPrincipalFromRepurchaseBy(
                            getContractUuidsBy(inputParameter, RepurchaseMode.M3)));
        }
    }

    /**
     * 2.逾期未偿本息 (统计范围内，所有逾期还款计划涉及贷款合同的未偿本息)
     *
     * @param inputParameter 输入的计算参数
     * @return 逾期未偿本息
     */
    // @Override
    public BigDecimal getOverdueUnearnedPrincipalWithInterest(InputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            List<String> ledgerContractUuidList = getContractUuidsBy(inputParameter, RepurchaseMode.M1);
            return getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter, ledgerContractUuidList);
        } else {
            return getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    getContractUuidsBy(inputParameter, RepurchaseMode.M2))
                    .add(getUnEarnedPrincipalInterestFromRepurchaseBy(
                            getContractUuidsBy(inputParameter, RepurchaseMode.M3)));
        }
    }

    /**
     * 3.剩余贷款本金 (统计范围内，所有未结清【已生效,回购中,违约】还款计划涉及贷款合同的未偿本金)
     *
     * @param inputParameter 输入的计算参数
     * @return 剩余贷款本金
     */
    // @Override
    public BigDecimal getRemainingLoanPrincipal(InputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            return getUnEarnedPrincipalFromLedgerBookBy(inputParameter,
                    getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M1));
        } else {
            return getUnEarnedPrincipalFromLedgerBookBy(inputParameter,
                    getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M2))
                    .add(getUnEarnedPrincipalFromRepurchaseBy(
                            getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M3)));
        }
    }

    /**
     * 4.剩余贷款本息 (统计范围内，所有未结清【已生效,回购中,违约】还款计划涉及贷款合同的未偿本息)
     *
     * @param inputParameter 输入的计算参数
     * @return 剩余贷款本息
     */
    // @Override
    public BigDecimal getRemainingLoanPrincipalWithInterest(InputParameter inputParameter) {
        if (inputParameter.isIncludeRepurchase()) {
            return getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M1));
        } else {
            return getUnEarnedPrincipalInterestFromLedgerBookBy(inputParameter,
                    getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M2))
                    .add(getUnEarnedPrincipalInterestFromRepurchaseBy(
                            getUnclearContractUuidsBy(inputParameter, RepurchaseMode.M3)));
        }
    }

    /**
     * 5.X月放款本金 (X月份放款成功的全部金额)
     *
     * @param staticOverdueRateInputParameter 输入的计算参数
     * @return X月放款本金
     */
    @Override
    public BigDecimal getRemittanceTotalAmountByMonth(StaticOverdueRateInputParameter staticOverdueRateInputParameter) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", staticOverdueRateInputParameter.getFinancialContractUuid());
        parameters.put("year", staticOverdueRateInputParameter.getYear());
        parameters.put("month", staticOverdueRateInputParameter.getMonth());

        List<BigDecimal> result = genericDaoSupport.queryForSingleColumnList(
                " select sum(c.total_amount) from t_remittance_plan rp join contract c on rp.contract_unique_id = c.unique_id where rp.execution_status = 2 and year(rp.last_modified_time) = :year and month(rp.last_modified_time) = :month and rp.financial_contract_uuid =:financialContractUuid ",
                parameters, BigDecimal.class);

        if (CollectionUtils.isEmpty(result)) {
            return BigDecimal.ZERO;
        } else {
            return result.get(0) != null ? result.get(0) : BigDecimal.ZERO;
        }
    }

    /**
     * 6.X月发放贷款逾期本金 (X月份放款成功的贷款合同下，逾期状态为“已逾期”或“已逾期”+“待确认”的还款计划所涉及贷款合同的未偿本金)
     *
     * @param staticOverdueRateInputParameter 输入的计算参数
     * @return X月发放贷款逾期本金
     */
    // @Override
    public BigDecimal getMonthlyOverdueLoanPrincipal(StaticOverdueRateInputParameter staticOverdueRateInputParameter) {
        if (staticOverdueRateInputParameter.isIncludeRepurchase()) {
            return getUnEarnedPrincipalFromLedgerBookBy(staticOverdueRateInputParameter,
                    getOverdueLoanPrincipalBy(staticOverdueRateInputParameter, RepurchaseMode.M1));
        } else {
            return getUnEarnedPrincipalFromLedgerBookBy(staticOverdueRateInputParameter,
                    getOverdueLoanPrincipalBy(staticOverdueRateInputParameter, RepurchaseMode.M2))
                    .add(getUnEarnedPrincipalFromRepurchaseBy(
                            getOverdueLoanPrincipalBy(staticOverdueRateInputParameter, RepurchaseMode.M3)));
        }
    }

    /**
     * 7.X月发放贷款逾期本息合计 (X月份放款成功的贷款合同下，逾期状态为“已逾期”或“已逾期”+“待确认”的还款计划所涉及贷款合同的未偿本息)
     *
     * @param staticOverdueRateInputParameter 输入的计算参数
     * @return X月发放贷款逾期本息合计
     */
    // @Override
    public BigDecimal getMonthlyOverdueLoanPrincipalWithInterest(
            StaticOverdueRateInputParameter staticOverdueRateInputParameter) {
        if (staticOverdueRateInputParameter.isIncludeRepurchase()) {
            return getUnEarnedPrincipalInterestFromLedgerBookBy(staticOverdueRateInputParameter,
                    getOverdueLoanPrincipalBy(staticOverdueRateInputParameter, RepurchaseMode.M1));
        } else {
            return getUnEarnedPrincipalInterestFromLedgerBookBy(staticOverdueRateInputParameter,
                    getOverdueLoanPrincipalBy(staticOverdueRateInputParameter, RepurchaseMode.M2))
                    .add(getUnEarnedPrincipalInterestFromRepurchaseBy(
                            getOverdueLoanPrincipalBy(staticOverdueRateInputParameter, RepurchaseMode.M3)));
        }
    }

    /**
     * 8.X月发放贷款的剩余贷款本息合计 (X月份放款成功的贷款合同下，所有【已生效、回购中、违约】的贷款合同未还本息不含罚息)
     *
     * @param staticOverdueRateInputParameter 输入的计算参数
     * @return X月发放贷款的剩余贷款本息合计
     */
    // @Override
    public BigDecimal getMonthlyRemittanceRemainingLoanPrincipalWithInterest(
            StaticOverdueRateInputParameter staticOverdueRateInputParameter) {
        if (staticOverdueRateInputParameter.isIncludeRepurchase()) {
            return getUnEarnedPrincipalInterestFromLedgerBookBy(staticOverdueRateInputParameter,
                    getRemittanceUnclearLoansBy(staticOverdueRateInputParameter, RepurchaseMode.M1));
        } else {
            return getUnEarnedPrincipalInterestFromLedgerBookBy(staticOverdueRateInputParameter,
                    getRemittanceUnclearLoansBy(staticOverdueRateInputParameter, RepurchaseMode.M2))
                    .add(getUnEarnedPrincipalInterestFromRepurchaseBy(
                            getRemittanceUnclearLoansBy(staticOverdueRateInputParameter, RepurchaseMode.M3)));
        }
    }

    private Map<String, Object> initQueryConditionValue(String financialContractUuid) {
        List<Map<String, Object>> dbResult = genericDaoSupport.queryForList(
                " select co.uuid as companyUuid,f.ledger_book_no as ledgerBookNo  from company co join FINANCIAL_CONTRACT f on co.id = f.company_id where f.financial_contract_uuid =:financialContractUuid ",
                "financialContractUuid", financialContractUuid, 0, 1);

        if (CollectionUtils.isEmpty(dbResult)) {
            throw new ContractReportRuntimeException("financial select error#1");
        }
        Map<String, Object> queryCondition = dbResult.get(0);

        if (!queryCondition.containsKey("companyUuid") || !queryCondition.containsKey("ledgerBookNo")) {
            throw new ContractReportRuntimeException("financial select error#2");
        }

        return queryCondition;
    }

    // @Override
    public List<String> getContractUuidsBy(InputParameter inputParameter, RepurchaseMode repurchaseMode) {
        List<String> contractUuidList = getContractUuidsByOverdueAssetSet(inputParameter, repurchaseMode);
        if (inputParameter.isIncludeUnconfirmed()) {
            contractUuidList.addAll(getContractUuidsByUnConfirmedAssetSet(inputParameter, repurchaseMode));
        }
        return contractUuidList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getContractUuidsByOverdueAssetSet(InputParameter inputParameter,
                                                          RepurchaseMode repurchaseMode) {
        // 已逾期
        StringBuilder sentence = new StringBuilder(
                " select c.uuid from asset_set a join contract c on a.contract_uuid = c.uuid where c.financial_contract_uuid = :financialContractUuid and a.overdue_date < date_format(now(), '%Y-%m-%d') and a.overdue_status=2 AND a.active_status = 0 ");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", inputParameter.getFinancialContractUuid());

        overdueOr(sentence, parameters, inputParameter);

        sentence.append(includeStaticRepurchaseSql(repurchaseMode));

        List<String> contractUuids = genericDaoSupport.queryForSingleColumnList(sentence.toString(), parameters,
                String.class);
        return CollectionUtils.isEmpty(contractUuids) ? new ArrayList<>() : contractUuids;
    }

    @Override
    public List<String> getContractUuidsByUnConfirmedAssetSet(InputParameter inputParameter,
                                                              RepurchaseMode repurchaseMode) {
        // 待确认
        int graceDay = getGraceDay(inputParameter.getFinancialContractUuid());
        StringBuilder sentence = new StringBuilder(
                " select c.uuid from asset_set a join contract c on a.contract_uuid = c.uuid where c.financial_contract_uuid = :financialContractUuid and DATEDIFF( date_format( now( ), '%Y-%m-%d' ), a.asset_recycle_date ) > :graceDay and a.overdue_status = 1 AND a.active_status = 0 ");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", inputParameter.getFinancialContractUuid());
        parameters.put("graceDay", graceDay);

        unConfirmedOr(sentence, parameters, inputParameter, graceDay);

        sentence.append(includeStaticRepurchaseSql(repurchaseMode));

        List<String> contractUuids = genericDaoSupport.queryForSingleColumnList(sentence.toString(), parameters,
                String.class);
        return CollectionUtils.isEmpty(contractUuids) ? new ArrayList<>() : contractUuids;
    }

    private String includeStaticRepurchaseSql(RepurchaseMode repurchaseMode) {
        switch (repurchaseMode) {
            case M1:
                return " AND ( c.state = 2 OR 4 OR 6 ) ";
            case M2:
                return " AND c.state = 2 ";
            default:// M3
                return " AND ( c.state = 4 OR 5 OR 6) ";
        }
    }

    private void overdueOr(StringBuilder sentence, Map<String, Object> parameters, InputParameter inputParameter) {
        sentence.append(" and (( ");
        appendOverdueStageSqlForOverdueClear(sentence, parameters, inputParameter);
        sentence.append(" ) or ( ");
        appendOverdueStageSqlForOverdueUnClear(sentence, parameters, inputParameter);
        sentence.append(" )) ");
    }

    private void unConfirmedOr(StringBuilder sentence, Map<String, Object> parameters, InputParameter inputParameter,
                               int graceDay) {
        sentence.append(" and (( ");
        appendOverdueStageSqlForUnconfirmedClear(sentence, parameters, inputParameter, graceDay);
        sentence.append(" ) or ( ");
        appendOverdueStageSqlForUnconfirmedUnClear(sentence, parameters, inputParameter, graceDay);
        sentence.append(" )) ");
    }

    // 已逾期 已结清
    private void appendOverdueStageSqlForOverdueClear(StringBuilder sentence, Map<String, Object> parameters,
                                                      InputParameter inputParameter) {
        int[] days = MathUtils.initOverDueDays(inputParameter.getPeriodDays(), -1);

        String[] overdueStagesArray = inputParameter.getOverdueStage().split(",");
        sentence.append(" a.asset_status =:clearStatus and ( (1 <> 1) ");
        parameters.put("clearStatus", AssetClearStatus.CLEAR.ordinal());

        for (String overdueStage : overdueStagesArray) {
            sentence.append(" or ");
            switch (overdueStage) {
                case "0":
                    sentence.append(" (  TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.overdue_date) <= :day1  ) ");
                    parameters.put("day1", days[0]);

                    break;
                case "1":
                    sentence.append(
                            " ( TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.overdue_date) > :day1 and TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.overdue_date) <= :day2 ) ");
                    parameters.put("day1", days[0]);
                    parameters.put("day2", days[1]);
                    break;
                case "2":
                    sentence.append(
                            " ( TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.overdue_date) > :day2 and TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.overdue_date) <= :day3 ) ");
                    parameters.put("day2", days[1]);
                    parameters.put("day3", days[2]);
                    break;
                default: // M3PLUS:
                    sentence.append(" ( TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.overdue_date) > :day3 ) ");
                    parameters.put("day3", days[2]);
                    break;
            }
        }
        sentence.append(" ) ");
    }

    // 已逾期 未结清
    private void appendOverdueStageSqlForOverdueUnClear(StringBuilder sentence, Map<String, Object> parameters,
                                                        InputParameter inputParameter) {
        int[] days = MathUtils.initOverDueDays(inputParameter.getPeriodDays(), -1);

        String[] overdueStagesArray = inputParameter.getOverdueStage().split(",");
        sentence.append(" a.asset_status =0 and ( (1 <> 1) ");

        for (String overdueStage : overdueStagesArray) {
            sentence.append(" or ");
            switch (overdueStage) {
                case "0":
                    sentence.append(" ( to_days(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.overdue_date) <= :day1  ) ");
                    parameters.put("day1", days[0]);

                    break;
                case "1":
                    sentence.append(
                            " ( TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.overdue_date) > :day1 and TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.overdue_date) <= :day2 ) ");
                    parameters.put("day1", days[0]);
                    parameters.put("day2", days[1]);
                    break;
                case "2":
                    sentence.append(
                            " ( TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.overdue_date) > :day2 and TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.overdue_date) <= :day3 ) ");
                    parameters.put("day2", days[1]);
                    parameters.put("day3", days[2]);
                    break;
                default: // M3PLUS:
                    sentence.append(" ( TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.overdue_date) > :day3 ) ");
                    parameters.put("day3", days[2]);
                    break;
            }
        }
        sentence.append(" ) ");
    }

    // 待确认 已结清
    private void appendOverdueStageSqlForUnconfirmedClear(StringBuilder sentence, Map<String, Object> parameters,
                                                          InputParameter inputParameter, int graceDay) {
        int[] days = MathUtils.initOverDueDays(inputParameter.getPeriodDays(), graceDay);

        String[] overdueStagesArray = inputParameter.getOverdueStage().split(",");

        sentence.append(" a.asset_status =1 and ( (1 <> 1) ");

        for (String overdueStage : overdueStagesArray) {
            sentence.append(" or ");
            switch (overdueStage) {
                case "0":
                    sentence.append(" (  TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.asset_recycle_date) <= :day1  ) ");
                    parameters.put("day1", days[0]);

                    break;
                case "1":
                    sentence.append(
                            " ( TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.asset_recycle_date) > :day1 and TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.asset_recycle_date) <= :day2) ");
                    parameters.put("day1", days[0]);
                    parameters.put("day2", days[1]);
                    break;
                case "2":
                    sentence.append(
                            " ( TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.asset_recycle_date) > :day2 and TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.asset_recycle_date) <= :day3 ) ");
                    parameters.put("day2", days[1]);
                    parameters.put("day3", days[2]);
                    break;
                default: // M3PLUS:
                    sentence.append(" ( TO_DAYS(a.actual_recycle_date) - TO_DAYS(a.asset_recycle_date) > :day3 ) ");
                    parameters.put("day3", days[2]);
                    break;
            }
        }
        sentence.append(" ) ");
    }

    // 待确认 未结清
    private void appendOverdueStageSqlForUnconfirmedUnClear(StringBuilder sentence, Map<String, Object> parameters,
                                                            InputParameter inputParameter, int graceDay) {
        int[] days = MathUtils.initOverDueDays(inputParameter.getPeriodDays(), graceDay);

        String[] overdueStagesArray = inputParameter.getOverdueStage().split(",");
        sentence.append(" a.asset_status =0 and ( (1 <> 1) ");

        for (String overdueStage : overdueStagesArray) {
            sentence.append(" or ");
            switch (overdueStage) {
                case "0":
                    sentence.append(
                            " (  TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.asset_recycle_date) <= :day1  ) ");
                    parameters.put("day1", days[0]);

                    break;
                case "1":
                    sentence.append(
                            " ( TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.asset_recycle_date) > :day1 and TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.asset_recycle_date) <= :day2 ) ");
                    parameters.put("day1", days[0]);
                    parameters.put("day2", days[1]);
                    break;
                case "2":
                    sentence.append(
                            " ( TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.asset_recycle_date) > :day2 and TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.asset_recycle_date) <= :day3 ) ");
                    parameters.put("day2", days[1]);
                    parameters.put("day3", days[2]);
                    break;
                default: // M3PLUS:
                    sentence.append(
                            " ( TO_DAYS(date_format(now(), '%Y-%m-%d')) - TO_DAYS(a.asset_recycle_date) > :day3 ) ");
                    parameters.put("day3", days[2]);
                    break;
            }
        }
        sentence.append(" ) ");
    }

    /* 宽限日 */
    // @Override
    public int getGraceDay(String financialContractUuid) {
        return genericDaoSupport.queryForInt(
                " select adva_repayment_term from FINANCIAL_CONTRACT where financial_contract_uuid =:financialContractUuid ",
                "financialContractUuid", financialContractUuid);
    }

    @Override
    public List<String> getUnclearContractUuidsBy(InputParameter inputParameter, RepurchaseMode repurchaseMode) {
        StringBuilder sentence = new StringBuilder(
                " select uuid from contract c where c.financial_contract_uuid = :financialContractUuid ");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", inputParameter.getFinancialContractUuid());
        sentence.append(includeStaticRepurchaseSql(repurchaseMode));
        List<String> contractUuids = genericDaoSupport.queryForSingleColumnList(sentence.toString(), parameters,
                String.class);
        return CollectionUtils.isEmpty(contractUuids) ? new ArrayList<>() : contractUuids;
    }

    @Override
    public BigDecimal getUnEarnedPrincipalFromLedgerBookBy(InputParameter inputParameter, List<String> contractUuids) {
        if (CollectionUtils.isEmpty(contractUuids)) {
            return BigDecimal.ZERO;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.putAll(initQueryConditionValue(inputParameter.getFinancialContractUuid()));
        parameters.put("contractUuids", contractUuids);
        StringBuilder setence = new StringBuilder(
                " select sum(debit_balance - credit_balance) from ledger_book_shelf where first_party_id = :companyUuid and ledger_book_no = :ledgerBookNo and contract_uuid in (:contractUuids) ");

        // 本金  未到期本金 应收本金 逾期本金
        String principal = "and ((first_account_uuid = '10000'  and second_account_uuid = '10000.02' ) or  (first_account_uuid = '20000'  and second_account_uuid = '20000.01' and third_account_uuid = '20000.01.01') or (first_account_uuid = '20000'  and second_account_uuid = '20000.05' and third_account_uuid = '20000.05.01')) ";

        // 本金加回购   未到期本金 应收本金 逾期本金 回购本金
        String principalWithRepurchase = "and ((first_account_uuid = '10000'  and second_account_uuid = '10000.02' ) or  (first_account_uuid = '20000'  and second_account_uuid = '20000.01' and third_account_uuid = '20000.01.01') or (first_account_uuid = '20000'  and second_account_uuid = '20000.05' and third_account_uuid = '20000.05.01') or (first_account_uuid = '120000'  and second_account_uuid = '120000.01')) ";

        if (inputParameter.isIncludeRepurchase()) {// includeRepurchase == true（回购计入已还）时，即已回购不为未偿，应用账本流水计算未偿回购本息
            setence.append(principalWithRepurchase);
        } else { // includeRepurchase == false(回购不计入已还)时,即已回购为逾期未还，不用账本计算未偿回购本息，应用回购单计算
            setence.append(principal);
        }

        List<BigDecimal> unearned = genericDaoSupport.queryForSingleColumnList(setence.toString(), parameters,
                BigDecimal.class);
        if (CollectionUtils.isEmpty(unearned)) {
            return BigDecimal.ZERO;
        } else {
            return unearned.get(0) != null ? unearned.get(0) : BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal getUnEarnedPrincipalInterestFromLedgerBookBy(InputParameter inputParameter,
                                                                   List<String> contractUuids) {

        if (CollectionUtils.isEmpty(contractUuids)) {
            return BigDecimal.ZERO;
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.putAll(initQueryConditionValue(inputParameter.getFinancialContractUuid()));
        parameters.put("contractUuids", contractUuids);
        StringBuilder setence = new StringBuilder(
                " select sum(debit_balance - credit_balance) from ledger_book_shelf where first_party_id = :companyUuid and ledger_book_no = :ledgerBookNo and contract_uuid in (:contractUuids) ");

        // 本息   未到期本金 应收本金 逾期本金 未到期利息 应收利息 逾期利息
        String principalInterest = "and ((first_account_uuid = '10000'  and second_account_uuid = '10000.02' ) or  (first_account_uuid = '20000'  and second_account_uuid = '20000.01' and third_account_uuid = '20000.01.01') or (first_account_uuid = '20000'  and second_account_uuid = '20000.05' and third_account_uuid = '20000.05.01') or (first_account_uuid = '10000'  and second_account_uuid = '10000.01' ) or  (first_account_uuid = '20000'  and second_account_uuid = '20000.01' and third_account_uuid = '20000.01.02') or (first_account_uuid = '20000'  and second_account_uuid = '20000.05' and third_account_uuid = '20000.05.02')) ";
        // 本息加回购   未到期本金 应收本金 逾期本金 回购本金 未到期利息 应收利息 逾期利息 回购利息
        String principalInterestWithRepurchase = "and ((first_account_uuid = '10000'  and second_account_uuid = '10000.02' ) or  (first_account_uuid = '20000'  and second_account_uuid = '20000.01' and third_account_uuid = '20000.01.01') or (first_account_uuid = '20000'  and second_account_uuid = '20000.05' and third_account_uuid = '20000.05.01') or (first_account_uuid = '120000'  and second_account_uuid = '120000.01') or (first_account_uuid = '10000'  and second_account_uuid = '10000.01' ) or  (first_account_uuid = '20000'  and second_account_uuid = '20000.01' and third_account_uuid = '20000.01.02') or (first_account_uuid = '20000'  and second_account_uuid = '20000.05' and third_account_uuid = '20000.05.02')  or (first_account_uuid = '120000'  and second_account_uuid = '120000.02')) ";

        if (inputParameter.isIncludeRepurchase()) {// includeRepurchase == true（回购计入已还）时，即已回购不为未偿，应用账本流水计算未偿回购本息
            setence.append(principalInterestWithRepurchase);
        } else { // includeRepurchase == false(回购不计入已还)时,即已回购为逾期未还，不用账本计算未偿回购本息，应用回购单计算
            setence.append(principalInterest);
        }

        List<BigDecimal> unearned = genericDaoSupport.queryForSingleColumnList(setence.toString(), parameters,
                BigDecimal.class);
        if (CollectionUtils.isEmpty(unearned)) {
            return BigDecimal.ZERO;
        } else {
            return unearned.get(0) != null ? unearned.get(0) : BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal getUnEarnedPrincipalFromRepurchaseBy(List<String> contractUuids) {
        if (!CollectionUtils.isEmpty(contractUuids)) {
            List<BigDecimal> amount = genericDaoSupport.queryForSingleColumnList(
                    " select sum(re.repurchase_principal) from repurchase_doc re join contract c on re.contract_id = c.id where c.uuid in (:contractUuids) ",
                    "contractUuids", contractUuids, BigDecimal.class);

            if (!CollectionUtils.isEmpty(amount)) {
                return amount.get(0) != null ? amount.get(0) : BigDecimal.ZERO;
            }
        }

        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getUnEarnedPrincipalInterestFromRepurchaseBy(List<String> contractUuids) {
        if (!CollectionUtils.isEmpty(contractUuids)) {
            List<BigDecimal> amount = genericDaoSupport.queryForSingleColumnList(
                    " select sum(re.repurchase_principal + re.repurchase_interest) from repurchase_doc re join contract c on re.contract_id = c.id where c.uuid in (:contractUuids) ",
                    "contractUuids", contractUuids, BigDecimal.class);

            if (!CollectionUtils.isEmpty(amount)) {
                return amount.get(0) != null ? amount.get(0) : BigDecimal.ZERO;
            }
        }

        return BigDecimal.ZERO;
    }

    @Override
    public List<String> getRemittanceUnclearLoansBy(StaticOverdueRateInputParameter staticOverdueRateInputParameter,
                                                    RepurchaseMode repurchaseMode) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("financialContractUuid", staticOverdueRateInputParameter.getFinancialContractUuid());
        parameters.put("year", staticOverdueRateInputParameter.getYear());
        parameters.put("month", staticOverdueRateInputParameter.getMonth());
        StringBuilder sentence = new StringBuilder(
                "select c.uuid from contract c where year(c.create_time) = :year and month(c.create_time) =:month and c.financial_contract_uuid =:financialContractUuid ");

        sentence.append(includeStaticRepurchaseSql(repurchaseMode));

        return genericDaoSupport.queryForSingleColumnList(sentence.toString(), parameters, String.class);
    }

    private List<String> getOverdueLoanPrincipalBy(StaticOverdueRateInputParameter staticOverdueRateInputParameter,
                                                   RepurchaseMode repurchaseMode) {
        List<String> contractUuidList;
        if (staticOverdueRateInputParameter.isIncludeUnconfirmed()) {
            contractUuidList = getOverdueLoanPrincipalByMonthAndOverdueAssetSet(staticOverdueRateInputParameter,
                    repurchaseMode);
            contractUuidList.addAll(getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(
                    staticOverdueRateInputParameter, repurchaseMode));
        } else {
            contractUuidList = getOverdueLoanPrincipalByMonthAndOverdueAssetSet(staticOverdueRateInputParameter,
                    repurchaseMode);
        }
        return contractUuidList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getOverdueLoanPrincipalByMonthAndOverdueAssetSet(
            StaticOverdueRateInputParameter staticOverdueRateInputParameter, RepurchaseMode repurchaseMode) {
        // 已逾期
        StringBuilder sentence = new StringBuilder(
                " select c.uuid from contract c join asset_set a on c.uuid = a.contract_uuid where year(c.create_time) = :year and month(c.create_time) = :month and c.financial_contract_uuid =:financialContractUuid and a.overdue_status = 2 AND a.active_status = 0 ");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("year", staticOverdueRateInputParameter.getYear());
        parameters.put("month", staticOverdueRateInputParameter.getMonth());
        parameters.put("financialContractUuid", staticOverdueRateInputParameter.getFinancialContractUuid());

        overdueOr(sentence, parameters, staticOverdueRateInputParameter);

        sentence.append(includeStaticRepurchaseSql(repurchaseMode));

        List<String> contractUuids = genericDaoSupport.queryForSingleColumnList(sentence.toString(), parameters,
                String.class);
        return CollectionUtils.isEmpty(contractUuids) ? new ArrayList<>() : contractUuids;
    }

    @Override
    public List<String> getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(
            StaticOverdueRateInputParameter staticOverdueRateInputParameter, RepurchaseMode repurchaseMode) {
        // 待确认
        StringBuilder sentence = new StringBuilder(
                " select c.uuid from contract c join asset_set a on c.uuid = a.contract_uuid where year(c.create_time) = :year and month(c.create_time) = :month and c.financial_contract_uuid =:financialContractUuid and a.overdue_status =1 AND a.active_status = 0 ");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("year", staticOverdueRateInputParameter.getYear());
        parameters.put("month", staticOverdueRateInputParameter.getMonth());
        parameters.put("financialContractUuid", staticOverdueRateInputParameter.getFinancialContractUuid());

        int graceDay = getGraceDay(staticOverdueRateInputParameter.getFinancialContractUuid());
        unConfirmedOr(sentence, parameters, staticOverdueRateInputParameter, graceDay);

        sentence.append(includeStaticRepurchaseSql(repurchaseMode));

        List<String> contractUuids = genericDaoSupport.queryForSingleColumnList(sentence.toString(), parameters,
                String.class);
        return CollectionUtils.isEmpty(contractUuids) ? new ArrayList<>() : contractUuids;
    }
}