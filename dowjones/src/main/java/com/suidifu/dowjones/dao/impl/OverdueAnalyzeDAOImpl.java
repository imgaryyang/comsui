package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.dao.OverdueAnalyzeDAO;
import com.suidifu.dowjones.exception.ContractReportRuntimeException;
import com.suidifu.dowjones.model.Contract;
import com.suidifu.dowjones.model.QueryConditionValue;
import com.suidifu.dowjones.model.StatisticsReport;
import com.suidifu.dowjones.utils.Constants;
import com.suidifu.dowjones.utils.MathUtils;
import com.suidifu.dowjones.utils.SQLUtils;
import com.suidifu.dowjones.vo.enumeration.RepurchaseMode;
import com.suidifu.dowjones.vo.request.InputParameter;
import com.suidifu.dowjones.vo.request.StaticOverdueRateInputParameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.spark.sql.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.apache.spark.sql.functions.sum;


/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/22 <br>
 * @time: 上午11:14 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Repository
@Slf4j
public class OverdueAnalyzeDAOImpl extends BaseDAO implements OverdueAnalyzeDAO, Serializable {
    @Resource
    private transient SparkSession sparkSession;

    /**
     * 5.X月放款本金 (X月份放款成功的全部金额)
     *
     * @param inputParameter 输入的计算参数
     * @return X月放款本金
     */
    @Override
    public BigDecimal getRemittanceTotalAmountByMonth(StaticOverdueRateInputParameter inputParameter) {
        String sql = "select trp.actual_total_amount as totalAmount," +
                " trp.execution_status AS status," +
                " trp.last_modified_time AS lastModifiedTime," +
                " trp.financial_contract_uuid AS financialContractUuid" +
                " from t_remittance_plan trp";

        log.info("getRemittanceTotalAmountByMonth sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql)).
                filter("status=2").
                filter("financialContractUuid='" + inputParameter.getFinancialContractUuid() + "'").
                filter("year(lastModifiedTime)=" + inputParameter.getYear()).
                filter("month(lastModifiedTime)=" + inputParameter.getMonth());

        if (df.count() == 0) {
            return BigDecimal.ZERO;
        }

        List<Row> result = df.agg(sum("totalAmount")).
                drop(new Column("status")).
                drop(new Column("lastModifiedTime")).
                drop(new Column("financialContractUuid")).collectAsList();

        if (!CollectionUtils.isEmpty(result)) {
            return result.get(0).getDecimal(0);
        }
        return BigDecimal.ZERO;
    }

    public String[] getContractUuidsByOverdueAssetSet(InputParameter inputParameter, RepurchaseMode repurchaseMode) {
        String[] result = {};
        String sql = "select con.uuid as contractUuid," +
                " con.financial_contract_uuid AS financialContractUuid," +
                " asset.overdue_status AS overdueStatus," +
                " asset.asset_status AS assetStatus," +
                " asset.actual_recycle_date AS actualRecycleDate," +
                " asset.overdue_date AS overdueDate," +
                " asset.active_status AS activeStatus," +
                " con.state AS state" +
                " from asset_set asset " +
                " inner join contract con on asset.contract_uuid = con.uuid" +
                " where " +
                "((" + appendOverdueStageSqlForOverdueClear(inputParameter) +
                ") or (" + appendOverdueStageSqlForOverdueUnClear(inputParameter) + "))" +
                " AND con.financial_contract_uuid='" + inputParameter.getFinancialContractUuid() + "'" +
                " AND asset.overdue_status=2 AND asset.active_status=0";

        log.info("getContractUuidsByOverdueAssetSet sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql));
        if (df.count() == 0) {
            return result;
        }

        df = df.filter(new Column("overdueDate").$less("date_format(now(), '%Y-%m-%d')")).
                filter("overdueDate is not null").
                drop(new Column("actualRecycleDate")).
                drop(new Column("overdueDate")).
                drop(new Column("financialContractUuid")).
                drop(new Column("overdueStatus")).
                drop(new Column("assetStatus"));
        if (df.count() == 0) {
            return result;
        }
        Contract[] rows = (Contract[]) filterRepurchaseMode(repurchaseMode, df).
                repartition(20).as(Encoders.bean(Contract.class)).collect();
        result = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i].getContractUuid();
        }
        return result;
    }

    @Override
    public String[] getContractUuidsByUnConfirmedAssetSet(InputParameter inputParameter, RepurchaseMode repurchaseMode) {
        String[] result = {};
        //待确认
        String sql = "select con.uuid as contractUuid," +
                " con.financial_contract_uuid AS financialContractUuid," +
                " asset.overdue_status AS overdueStatus," +
                " asset.asset_status AS assetStatus," +
                " asset.actual_recycle_date AS actualRecycleDate," +
                " asset.overdue_date AS overdueDate," +
                " asset.asset_recycle_date AS assetRecycleDate," +
                " asset.active_status AS activeStatus," +
                " con.id AS id," +
                " con.state AS state" +
                " from asset_set asset " +
                " inner join contract con on asset.contract_uuid = con.uuid" +
                " where 1=1" +
                " and datediff(date_format(now( ), '%Y-%m-%d' ), asset.asset_recycle_date)>" +
                getGraceDay(inputParameter.getFinancialContractUuid()) +
                " and ((" + appendOverdueStageSqlForUnconfirmedClear(inputParameter) +
                ") or (" + appendOverdueStageSqlForUnconfirmedUnClear(inputParameter) + "))";

        log.info("getContractUuidsByUnConfirmedAssetSet sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql), initPredicates(Constants.CONTRACT,
                inputParameter.getFinancialContractUuid())).
                filter("overdueStatus=1").
                filter("activeStatus=0").
                filter("financialContractUuid='" +
                        inputParameter.getFinancialContractUuid() + "'").
                drop(new Column("financialContractUuid")).
                drop(new Column("overdueStatus")).
                drop(new Column("assetStatus")).
                drop(new Column("actualRecycleDate")).
                drop(new Column("overdueDate")).
                drop(new Column("id")).
                drop(new Column("assetRecycleDate"));

        if (df.count() == 0) {
            return result;
        }
        df = filterRepurchaseMode(repurchaseMode, df);

        if (df.count() == 0) {
            return result;
        }

        Contract[] rows = (Contract[]) df.repartition(20).as(Encoders.bean(Contract.class)).collect();
        result = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i].getContractUuid();
        }
        return result;
    }

    public Dataset<Row> getContractUuidsByUnConfirmedAssetSet(InputParameter inputParameter) {
        //待确认
        String sql = "select con.uuid as contractUuid," +
                " con.financial_contract_uuid AS financialContractUuid," +
                " asset.overdue_status AS overdueStatus," +
                " asset.asset_status AS assetStatus," +
                " asset.actual_recycle_date AS actualRecycleDate," +
                " asset.overdue_date AS overdueDate," +
                " asset.asset_recycle_date AS assetRecycleDate," +
                " asset.active_status AS activeStatus," +
                " con.id AS id," +
                " con.state AS state" +
                " from asset_set asset " +
                " inner join contract con on asset.contract_uuid = con.uuid";

        log.info("getContractUuidsByUnConfirmedAssetSet sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql), initPredicates(Constants.CONTRACT,
                inputParameter.getFinancialContractUuid())).
                filter("overdueStatus=1").
                filter("activeStatus=0").
                drop(new Column("financialContractUuid")).
                drop(new Column("overdueStatus")).
                drop(new Column("assetStatus")).
                drop(new Column("actualRecycleDate")).
                drop(new Column("overdueDate")).
                drop(new Column("id")).
                drop(new Column("assetRecycleDate"));

        return filterRepurchaseMode(RepurchaseMode.M1, df);
    }

    public String[] getUnclearContractUuidsBy(InputParameter inputParameter, RepurchaseMode repurchaseMode) {
        String[] result;
        //待确认
        String sql = "select con.id as id,con.uuid as contractUuid," +
                " con.financial_contract_uuid AS financialContractUuid," +
                " con.state AS state" +
                " from contract con ";

        log.info("getUnclearContractUuidsBy sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql), initPredicates(Constants.CONTRACT,
                inputParameter.getFinancialContractUuid())).
                filter("financialContractUuid='" + inputParameter.getFinancialContractUuid() + "'").
                drop(new Column("financialContractUuid")).
                drop(new Column("id"));

        Contract[] rows = (Contract[]) filterRepurchaseMode(repurchaseMode, df).
                as(Encoders.bean(Contract.class)).collect();
        result = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i].getContractUuid();
        }
        return result;
    }

    @Override
    public BigDecimal getUnEarnedPrincipalFromLedgerBookBy(InputParameter inputParameter, String[] contractUuids) {
        if (ArrayUtils.isEmpty(contractUuids)) {
            return BigDecimal.ZERO;
        }
        for (String contractUuid : contractUuids) {
            log.info("contractUuid is:{}", contractUuid);
        }

        QueryConditionValue queryConditionValue = initQueryConditionValue(inputParameter.getFinancialContractUuid());
        log.info("\ncompanyUuid is:{}\nfinancialContractUuid is:{}\nledgerBookNo is:{}\n",
                queryConditionValue.getCompanyUuid(),
                queryConditionValue.getFinancialContractUuid(),
                queryConditionValue.getLedgerBookNo());

        String sql = "select lbs.debit_balance-lbs.credit_balance AS balance," +
                " lbs.first_party_id AS firstPartyId," +
                " lbs.ledger_book_no AS ledgerBookNo," +
                " lbs.first_account_uuid AS firstAccountUuid," +
                " lbs.second_account_uuid AS secondAccountUuid," +
                " lbs.third_account_uuid AS thirdAccountUuid," +
                " lbs.id AS id," +
                " lbs.contract_uuid AS contractUuid" +
                " from ledger_book_shelf lbs " +
                " where (" +
                " (lbs.first_account_uuid='10000' and lbs.second_account_uuid='10000.02')" +
                " or (lbs.first_account_uuid='20000' and lbs.second_account_uuid='20000.01'" +
                " and lbs.third_account_uuid ='20000.01.01') " +
                " or (lbs.first_account_uuid='20000' and lbs.second_account_uuid='20000.05'" +
                " and lbs.third_account_uuid ='20000.05.01')";

        if (inputParameter.isIncludeRepurchase()) {// includeRepurchase == true（回购计入已还）时，即已回购不为未偿，应用账本流水计算未偿回购本息
            sql = sql + " or (lbs.first_account_uuid='120000'" +
                    " and lbs.second_account_uuid='120000.01')";
        }

        sql = sql + ")" +
                " AND lbs.first_party_id='" + queryConditionValue.getCompanyUuid() + "'" +
                " AND lbs.ledger_book_no='" + queryConditionValue.getLedgerBookNo() + "'";

        log.info("getUnEarnedPrincipalFromLedgerBookBy sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql));

        df = df.filter(new Column("contractUuid").isin((Object[]) contractUuids)).
                drop(new Column("firstPartyId")).
                drop(new Column("ledgerBookNo")).
                drop(new Column("contractUuid")).
                drop(new Column("firstAccountUuid")).
                drop(new Column("secondAccountUuid")).
                drop(new Column("thirdAccountUuid")).
                drop(new Column("id"));

        Dataset<Row> result = df.agg(sum("balance"));

        List<Row> rows = result.collectAsList();
        log.info("rows size is:{}", rows.size());
        if (!CollectionUtils.isEmpty(rows) && rows.get(0) != null) {
            return rows.get(0).getDecimal(0) != null ?
                    rows.get(0).getDecimal(0) : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public Dataset<Row> getUnEarnedPrincipalFromLedgerBookBy(InputParameter inputParameter) {
        String sql = "select lbs.debit_balance-lbs.credit_balance AS balance," +
                " lbs.first_party_id AS firstPartyId," +
                " lbs.ledger_book_no AS ledgerBookNo," +
                " lbs.first_account_uuid AS firstAccountUuid," +
                " lbs.second_account_uuid AS secondAccountUuid," +
                " lbs.third_account_uuid AS thirdAccountUuid," +
                " lbs.id AS id," +
                " lbs.contract_uuid AS contractUuid" +
                " from ledger_book_shelf lbs " +
                " where (" +
                " (lbs.first_account_uuid='10000' and lbs.second_account_uuid='10000.02')" +
                " or (lbs.first_account_uuid='20000' and lbs.second_account_uuid='20000.01'" +
                " and lbs.third_account_uuid ='20000.01.01') " +
                " or (lbs.first_account_uuid='20000' and lbs.second_account_uuid='20000.05'" +
                " and lbs.third_account_uuid ='20000.05.01')";

        if (inputParameter.isIncludeRepurchase()) {// includeRepurchase == true（回购计入已还）时，即已回购不为未偿，应用账本流水计算未偿回购本息
            sql = sql + " or (lbs.first_account_uuid='120000'" +
                    " and lbs.second_account_uuid='120000.01')";
        }
        sql = sql + ")";

        log.info("getUnEarnedPrincipalFromLedgerBookBy sql is:{}", sql);

        QueryConditionValue queryConditionValue = initQueryConditionValue(inputParameter.getFinancialContractUuid());
        log.info("\ncompanyUuid is:{}\nfinancialContractUuid is:{}\nledgerBookNo is:{}\n",
                queryConditionValue.getCompanyUuid(),
                queryConditionValue.getFinancialContractUuid(),
                queryConditionValue.getLedgerBookNo());

        return loadDataFromTable(SQLUtils.wrapperSQL(sql), initPredicates(Constants.LEDGER_BOOK_SHELF)).
                drop(new Column("firstPartyId")).
                drop(new Column("ledgerBookNo")).
                drop(new Column("firstAccountUuid")).
                drop(new Column("secondAccountUuid")).
                drop(new Column("thirdAccountUuid")).
                drop(new Column("id"));
    }

    @Override
    public BigDecimal getUnEarnedPrincipalInterestFromLedgerBookBy(InputParameter inputParameter, String[] contractUuids) {
        if (ArrayUtils.isEmpty(contractUuids)) {
            return BigDecimal.ZERO;
        }

        QueryConditionValue queryConditionValue = initQueryConditionValue(inputParameter.getFinancialContractUuid());
        String firstPartyId = queryConditionValue.getCompanyUuid();
        String ledgerBookNo = queryConditionValue.getLedgerBookNo();

        String sql = "select lbs.debit_balance-lbs.credit_balance AS balance," +
                " lbs.first_party_id AS firstPartyId," +
                " lbs.ledger_book_no AS ledgerBookNo," +
                " lbs.first_account_uuid AS firstAccountUuid," +
                " lbs.second_account_uuid AS secondAccountUuid," +
                " lbs.third_account_uuid AS thirdAccountUuid," +
                " lbs.id AS id," +
                " lbs.contract_uuid AS contractUuid" +
                " from ledger_book_shelf lbs " +
                " where 1=1 " +
                " and lbs.first_party_id='" + firstPartyId + "'" +
                " and lbs.ledger_book_no='" + ledgerBookNo + "'" +
                " and ((lbs.first_account_uuid='10000' and lbs.second_account_uuid='10000.02')" +
                " or (lbs.first_account_uuid='20000' and lbs.second_account_uuid='20000.01'" +
                " and lbs.third_account_uuid='20000.01.01')" +
                " or (lbs.first_account_uuid = '20000' and lbs.second_account_uuid='20000.05'" +
                " and lbs.third_account_uuid='20000.05.01')" +
                " or (lbs.first_account_uuid='10000' and lbs.second_account_uuid='10000.01')" +
                " or (lbs.first_account_uuid='20000' and lbs.second_account_uuid='20000.01'" +
                " and lbs.third_account_uuid='20000.01.02')" +
                " or (lbs.first_account_uuid='20000' and lbs.second_account_uuid='20000.05'" +
                " and lbs.third_account_uuid='20000.05.02')";

        if (inputParameter.isIncludeRepurchase()) {// includeRepurchase == true（回购计入已还）时，即已回购不为未偿，应用账本流水计算未偿回购本息
            sql = sql +
                    " or (lbs.first_account_uuid='120000'" +
                    " and lbs.second_account_uuid='120000.02')" +
                    " or (lbs.first_account_uuid='120000'" +
                    " and lbs.second_account_uuid='120000.01')";
        }
        sql = sql + ")";

        log.info("getUnEarnedPrincipalInterestFromLedgerBookBy sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql), initPredicates(Constants.LEDGER_BOOK_SHELF)).
                filter(new Column("contractUuid").
                        isin((Object[]) contractUuids)).
                drop(new Column("firstPartyId")).
                drop(new Column("ledgerBookNo")).
                drop(new Column("contractUuid")).
                drop(new Column("firstAccountUuid")).
                drop(new Column("secondAccountUuid")).
                drop(new Column("thirdAccountUuid")).
                drop(new Column("id"));

        if (df.count() == 0) {
            return BigDecimal.ZERO;
        }

        Dataset<Row> result = df.agg(sum("balance"));

        if (result.count() == 0) {
            return BigDecimal.ZERO;
        }

        List<Row> rows = result.collectAsList();
        if (!CollectionUtils.isEmpty(rows) && rows.get(0) != null) {
            return rows.get(0).getDecimal(0) != null ?
                    rows.get(0).getDecimal(0) : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getUnEarnedPrincipalFromRepurchaseBy(String[] contractUuids, boolean withInterest) {
        if (ArrayUtils.isEmpty(contractUuids)) {
            return BigDecimal.ZERO;
        }
        String sql = "select re.repurchase_principal" + (withInterest ? "+re.repurchase_interest" : "") +
                " AS repurchasePrincipal," +
                " re.contract_id AS contractId," +
                " re.id AS id," +
                " con.uuid AS contractUuid" +
                " from repurchase_doc re " +
                " inner join contract con on re.contract_id=con.id";

        log.info("getUnEarnedPrincipalFromRepurchaseBy sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql), initPredicates(Constants.REPURCHASE_DOC)).
                filter(new Column("contractUuid").isin((Object[]) contractUuids)).
                drop(new Column("id")).
                agg(sum("repurchasePrincipal"));

        List<Row> rows = df.collectAsList();
        if (!CollectionUtils.isEmpty(rows) && rows.get(0) != null) {
            return rows.get(0).getDecimal(0) != null ?
                    rows.get(0).getDecimal(0) : BigDecimal.ZERO;
        }

        return BigDecimal.ZERO;
    }

    public String[] getRemittanceUnclearLoansBy(StaticOverdueRateInputParameter inputParameter, RepurchaseMode repurchaseMode) {
        String[] result;
        //待确认
        String sql = "select con.uuid as contractUuid," +
                " con.create_time AS createTime," +
                " con.financial_contract_uuid AS financialContractUuid," +
                " asset.overdue_status AS overdueStatus," +
                " asset.asset_status AS assetStatus," +
                " asset.actual_recycle_date AS actualRecycleDate," +
                " asset.overdue_date AS overdueDate," +
                " asset.asset_recycle_date AS assetRecycleDate," +
                " con.id AS id," +
                " con.state AS state" +
                " from contract con " +
                " inner join asset_set asset on asset.contract_uuid=con.uuid ";

        log.info("getRemittanceUnclearLoansBy sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql), initPredicates(Constants.CONTRACT,
                inputParameter.getFinancialContractUuid())).
                filter("year(createTime)=" + inputParameter.getYear()).
                filter("month(createTime)=" + inputParameter.getMonth()).
                filter("financialContractUuid='" + inputParameter.getFinancialContractUuid() + "'").
                drop(new Column("lastModifiedTime")).
                drop(new Column("financialContractUuid")).
                drop(new Column("overdueStatus")).
                drop(new Column("assetStatus")).
                drop(new Column("actualRecycleDate")).
                drop(new Column("overdueDate")).
                drop(new Column("id")).
                drop(new Column("assetRecycleDate"));

        Contract[] rows = (Contract[]) filterRepurchaseMode(repurchaseMode, df).
                repartition(20).as(Encoders.bean(Contract.class)).collect();
        result = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i].getContractUuid();
        }
        return result;
    }

    public String[] getOverdueLoanPrincipalByMonthAndOverdueAssetSet(StaticOverdueRateInputParameter inputParameter, RepurchaseMode repurchaseMode) {
        String[] result;
        //已逾期
        String sql = "select con.uuid as contractUuid," +
                " con.create_time AS createTime," +
                " con.financial_contract_uuid AS financialContractUuid," +
                " asset.overdue_status AS overdueStatus," +
                " asset.asset_status AS assetStatus," +
                " asset.actual_recycle_date AS actualRecycleDate," +
                " asset.overdue_date AS overdueDate," +
                " asset.asset_recycle_date AS assetRecycleDate," +
                " con.id AS id," +
                " con.state AS state" +
                " from contract con " +
                " inner join asset_set asset on asset.contract_uuid=con.uuid " +
                " where ((" + appendOverdueStageSqlForOverdueClear(inputParameter) +
                ") or (" + appendOverdueStageSqlForOverdueUnClear(inputParameter) + "))";

        log.info("getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet sql is:{}", sql);

        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql), initPredicates(Constants.CONTRACT,
                inputParameter.getFinancialContractUuid())).
                filter("year(createTime)=" + inputParameter.getYear()).
                filter("month(createTime)=" + inputParameter.getMonth()).
                filter("financialContractUuid ='" + inputParameter.getFinancialContractUuid() + "'").
                filter("overdueStatus=2").
                drop(new Column("createTime")).
                drop(new Column("financialContractUuid")).
                drop(new Column("overdueStatus")).
                drop(new Column("assetStatus")).
                drop(new Column("actualRecycleDate")).
                drop(new Column("overdueDate")).
                drop(new Column("id")).
                drop(new Column("assetRecycleDate"));

        Contract[] rows = (Contract[]) filterRepurchaseMode(repurchaseMode, df).
                repartition(20).as(Encoders.bean(Contract.class)).collect();
        result = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i].getContractUuid();
        }
        return result;
    }

    public String[] getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet(StaticOverdueRateInputParameter inputParameter, RepurchaseMode repurchaseMode) {
        String[] result = {};
        //待确认
        String sql = "select con.uuid as contractUuid," +
                " con.create_time AS createTime," +
                " con.financial_contract_uuid AS financialContractUuid," +
                " asset.overdue_status AS overdueStatus," +
                " asset.asset_status AS assetStatus," +
                " asset.actual_recycle_date AS actualRecycleDate," +
                " asset.overdue_date AS overdueDate," +
                " asset.asset_recycle_date AS assetRecycleDate," +
                " con.id AS id," +
                " con.state AS state" +
                " from contract con " +
                " inner join asset_set asset on asset.contract_uuid=con.uuid " +
                " where ((" + appendOverdueStageSqlForUnconfirmedClear(inputParameter) +
                ") or (" + appendOverdueStageSqlForUnconfirmedUnClear(inputParameter) + "))";

        log.info("getOverdueLoanPrincipalByMonthAndUnconfirmedAssetSet sql is:{}", sql);
        Dataset<Row> df = loadDataFromTable(SQLUtils.wrapperSQL(sql), initPredicates(Constants.CONTRACT,
                inputParameter.getFinancialContractUuid())).
                filter("year(createTime)=" + inputParameter.getYear()).
                filter("month(createTime)=" + inputParameter.getMonth()).
                filter("financialContractUuid ='" + inputParameter.getFinancialContractUuid() + "'").
                filter("overdueStatus=1").
                drop(new Column("createTime")).
                drop(new Column("financialContractUuid")).
                drop(new Column("overdueStatus")).
                drop(new Column("assetStatus")).
                drop(new Column("actualRecycleDate")).
                drop(new Column("overdueDate")).
                drop(new Column("id")).
                drop(new Column("assetRecycleDate"));

        if (df.count() == 0) {
            return result;
        }
        Contract[] rows = (Contract[]) filterRepurchaseMode(repurchaseMode, df).
                repartition(20).as(Encoders.bean(Contract.class)).collect();
        result = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            result[i] = rows[i].getContractUuid();
        }
        return result;
    }

    public QueryConditionValue initQueryConditionValue(String financialContractUuid) {
        String sql = "SELECT fc.financial_contract_uuid as financialContractUuid," +
                " fc.ledger_book_no AS ledgerBookNo," +
                " com.uuid AS companyUuid" +
                " FROM company com" +
                " INNER JOIN FINANCIAL_CONTRACT fc ON com.id=fc.company_id";

        List<QueryConditionValue> queryConditionValues =
                loadDataFromTable(SQLUtils.wrapperSQL(sql)).
                        filter("financialContractUuid= '" + financialContractUuid + "'").
                        as(Encoders.bean(QueryConditionValue.class)).
                        collectAsList();

        if (!CollectionUtils.isEmpty(queryConditionValues) && ObjectUtils.notEqual(queryConditionValues.get(0), null)) {
            return queryConditionValues.get(0);
        }

        throw new ContractReportRuntimeException("financialContractUuid and ledgerBookNo not found");
    }

    //已逾期 已结清
    private String appendOverdueStageSqlForOverdueClear(InputParameter inputParameter) {
        int[] days = MathUtils.initOverDueDays(inputParameter.getPeriodDays(), -1);

        String[] overdueStagesArray = inputParameter.getOverdueStage().split(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("asset.asset_status=1 and ((1 <> 1) ");

        for (String overdueStage : overdueStagesArray) {
            stringBuilder.append(" or ");
            switch (overdueStage) {
                case "0":
                    stringBuilder.
                            append("(datediff(asset.actual_recycle_date, asset.overdue_date)<=").
                            append(days[0]).append(")");
                    break;
                case "1":
                    stringBuilder.
                            append("(datediff(asset.actual_recycle_date,asset.overdue_date)>").
                            append(days[0]).
                            append(" and datediff(asset.actual_recycle_date,asset.overdue_date)<=").append(days[1]).append(")");
                    break;
                case "2":
                    stringBuilder.
                            append("(datediff(asset.actual_recycle_date,asset.overdue_date)>").
                            append(days[1]).
                            append(" and datediff(asset.actual_recycle_date,asset.overdue_date)<=").append(days[2]).append(")");
                    break;
                default: // M3PLUS:
                    stringBuilder.append("(datediff(asset.actual_recycle_date,asset.overdue_date)>").append(days[2]).append(")");
                    break;
            }
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }

    //已逾期 未结清
    private String appendOverdueStageSqlForOverdueUnClear(InputParameter inputParameter) {
        int[] days = MathUtils.initOverDueDays(inputParameter.getPeriodDays(), -1);

        String[] overdueStagesArray = inputParameter.getOverdueStage().split(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("asset.asset_status=0 and ((1 <> 1) ");

        for (String overdueStage : overdueStagesArray) {
            stringBuilder.append(" or ");
            switch (overdueStage) {
                case "0":
                    stringBuilder.
                            append("(datediff(date_format(now(), '%Y-%m-%d'),asset.overdue_date)<=").
                            append(days[0]).append(")");

                    break;
                case "1":
                    stringBuilder.
                            append("(datediff(date_format(now(), '%Y-%m-%d'),asset.overdue_date)>").
                            append(days[0]).
                            append(" and datediff(date_format(now(), '%Y-%m-%d'),asset.overdue_date)<=").append(days[1]).append(")");
                    break;
                case "2":
                    stringBuilder.
                            append("(datediff(date_format(now(), '%Y-%m-%d'),asset.overdue_date)>").
                            append(days[1]).
                            append(" and datediff(date_format(now(), '%Y-%m-%d'),asset.overdue_date)<=").append(days[2]).append(")");
                    break;
                default: // M3PLUS:
                    stringBuilder.append("(datediff(date_format(now(), '%Y-%m-%d'),asset.overdue_date)>").append(days[2]).append(")");
                    break;
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    //待确认 已结清
    private String appendOverdueStageSqlForUnconfirmedClear(InputParameter inputParameter) {
        int[] days = MathUtils.initOverDueDays(inputParameter.getPeriodDays(),
                getGraceDay(inputParameter.getFinancialContractUuid()));

        String[] overdueStagesArray = inputParameter.getOverdueStage().split(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("asset.asset_status=1 and ((1 <> 1) ");

        for (String overdueStage : overdueStagesArray) {
            stringBuilder.append(" or ");
            switch (overdueStage) {
                case "0":
                    stringBuilder.
                            append("(datediff(asset.actual_recycle_date,asset.asset_recycle_date)<=").
                            append(days[0]).append(")");
                    break;
                case "1":
                    stringBuilder.
                            append("(datediff(asset.actual_recycle_date,asset.asset_recycle_date)>").
                            append(days[0]).
                            append(" and datediff(asset.actual_recycle_date,asset.asset_recycle_date)<=").append(days[1]).append(")");
                    break;
                case "2":
                    stringBuilder.
                            append("(datediff(asset.actual_recycle_date,asset.asset_recycle_date)>").
                            append(days[1]).
                            append(" and datediff(asset.actual_recycle_date,asset.asset_recycle_date)<=").append(days[2]).append(")");
                    break;
                default: // M3PLUS:
                    stringBuilder.append("(datediff(asset.actual_recycle_date,asset.asset_recycle_date)>").append(days[2]).append(")");
                    break;
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    //待确认 未结清
    private String appendOverdueStageSqlForUnconfirmedUnClear(InputParameter inputParameter) {
        int[] days = MathUtils.initOverDueDays(inputParameter.getPeriodDays(),
                getGraceDay(inputParameter.getFinancialContractUuid()));

        String[] overdueStagesArray = inputParameter.getOverdueStage().split(",");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("asset.asset_status=0 and ((1 <> 1) ");

        for (String overdueStage : overdueStagesArray) {
            stringBuilder.append(" or ");
            switch (overdueStage) {
                case "0":
                    stringBuilder.
                            append("(datediff(date_format(now(), '%Y-%m-%d'),asset.asset_recycle_date)<=").
                            append(days[0]).append(")");

                    break;
                case "1":
                    stringBuilder.
                            append("(datediff(date_format(now(), '%Y-%m-%d'),asset.asset_recycle_date)>").
                            append(days[0]).
                            append(" and datediff(date_format(now(), '%Y-%m-%d'),asset.asset_recycle_date)<=").append(days[1]).append(")");
                    break;
                case "2":
                    stringBuilder.
                            append("(datediff(date_format(now(), '%Y-%m-%d'),asset.asset_recycle_date)>").
                            append(days[1]).
                            append(" and datediff(date_format(now(), '%Y-%m-%d'),asset.asset_recycle_date)<=").append(days[2]).append(")");
                    break;
                default: // M3PLUS:
                    stringBuilder.append("(datediff(date_format(now(), '%Y-%m-%d'),asset.asset_recycle_date)>").append(days[2]).append(")");
                    break;
            }
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public void saveData(StatisticsReport statisticsReport) {
        List<StatisticsReport> reports = new ArrayList<>();
        reports.add(statisticsReport);

        Dataset<StatisticsReport> df = sparkSession.createDataset(reports, Encoders.bean(StatisticsReport.class));

        saveData2Table(df.toDF(), "statistics_report", getSaveMode(statisticsReport));
    }

    @Override
    public List<StatisticsReport> loadAllStatisticsReport(StatisticsReport statisticsReport) {
        return loadDataFromTable("statistics_report").
                filter("computeType=" + statisticsReport.getComputeType()).
                filter("formulaId=" + statisticsReport.getFormulaId()).
                as(Encoders.bean(StatisticsReport.class)).collectAsList();
    }

    private SaveMode getSaveMode(StatisticsReport statisticsReport) {
        long existed = loadDataFromTable("statistics_report").
                filter("computeType=" + statisticsReport.getComputeType()).
                filter("statisticsDate='" + statisticsReport.getStatisticsDate() + "'").
                filter("formulaId=" + statisticsReport.getFormulaId()).
                count();

        return existed != 0 ? SaveMode.Overwrite : SaveMode.Append;
    }
}