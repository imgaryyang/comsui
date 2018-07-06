package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.dao.CashFingerPrinterDAO;
import com.suidifu.dowjones.utils.Constants;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import com.suidifu.dowjones.vo.request.FingerPrinterParameter;
import com.suidifu.dowjones.vo.request.QueryParameter;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.spark.sql.functions.sum;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2018/1/5 <br>
 * @time: 20:10 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Repository
@Slf4j
public class CashFingerPrinterDAOImpl extends BaseDAO implements CashFingerPrinterDAO, Serializable {
    private static final List<String> THIRD_ACCOUNT_UUID = new ArrayList<>();

    @Autowired
    private GenericJdbcSupport genericJdbcSupport;


    static {
        THIRD_ACCOUNT_UUID.add("60000.1000.01");
        THIRD_ACCOUNT_UUID.add("50000.06.01");
        THIRD_ACCOUNT_UUID.add("60000.1000.02");
        THIRD_ACCOUNT_UUID.add("50000.06.02");
        THIRD_ACCOUNT_UUID.add("60000.1000.03");
        THIRD_ACCOUNT_UUID.add("50000.06.03");
        THIRD_ACCOUNT_UUID.add("60000.1000.04");
        THIRD_ACCOUNT_UUID.add("50000.06.04");
        THIRD_ACCOUNT_UUID.add("60000.1000.05");
        THIRD_ACCOUNT_UUID.add("50000.06.05");
        THIRD_ACCOUNT_UUID.add("60000.1000.06");
        THIRD_ACCOUNT_UUID.add("50000.06.06");
        THIRD_ACCOUNT_UUID.add("60000.1000.07");
        THIRD_ACCOUNT_UUID.add("50000.06.07");
        THIRD_ACCOUNT_UUID.add("60000.1000.08");
        THIRD_ACCOUNT_UUID.add("50000.06.08");
        THIRD_ACCOUNT_UUID.add("60000.1000.09");
        THIRD_ACCOUNT_UUID.add("50000.06.09");
    }

    @Resource
    private SparkSession sparkSession;

    @Override
    public void save(FingerPrinterParameter fingerPrinterParameter) {
        List<FingerPrinterParameter> fingerPrinterParameters = new ArrayList<>();
        fingerPrinterParameters.add(fingerPrinterParameter);

        Dataset<Row> df = sparkSession.sqlContext().createDataFrame(fingerPrinterParameters, FingerPrinterParameter.class);
        df.printSchema();


        saveData2Table(df, "spark_schedule_job", getSaveMode(fingerPrinterParameter));
    }

    @Override
    public FingerPrinterParameter[] loadScheduleJob() {
        Dataset<Row> df = loadDataFromTable("spark_schedule_job");
        df.printSchema();
        df.show(false);

        return ((FingerPrinterParameter[]) df.as(Encoders.bean(FingerPrinterParameter.class)).collect());
    }

    @Override
    public FingerPrinterParameter[] loadScheduleJob(String financialContractUuid) {
        Dataset<Row> df = loadDataFromTable("(select * from spark_schedule_job where financialContractUuid='" + financialContractUuid + "') as temp");
        df.printSchema();
        df.show(false);

        return ((FingerPrinterParameter[]) df.as(Encoders.bean(FingerPrinterParameter.class)).collect());
    }

    @Override
    public QueryParameter getQueryParameters(String financialContractUuid) {
        String sql = "(SELECT" +
                "  fc.id as id," +
                "  fc.ledger_book_no as ledgerBookNO," +
                "  fc.financial_contract_uuid as financialContractUuid," +
                "  CONCAT('60000.',a.account_no) as accountNO,"+
                "  a.account_no as hostAccountNo" +
                "  from financial_contract fc inner join account a on fc.capital_account_id=a.id " +
                "  WHERE 1 = 1" +
                "  AND fc.financial_contract_uuid='" + financialContractUuid + "'" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n", financialContractUuid, sql);

        Dataset<Row> df = loadDataFromTable(sql).drop(new Column("id"));
        df.printSchema();


        if (df.count() == 0) {
            return null;
        }
        return ((QueryParameter[]) df.as(Encoders.bean(QueryParameter.class)).collect())[0];
    }

    @Override
    public Dataset<Row> getCashFingerPrinterOfZeroCashFlow(QueryParameter queryParameter) {
        Object[] idArray = THIRD_ACCOUNT_UUID.toArray();

        String sql = "(SELECT" +
                "  id as id," +
                "  second_account_name as bankCard," +
                "  CASE WHEN THIRD_ACCOUNT_UUID IN (" + getAllUuidsString(idArray) + ") THEN" +
                "  debit_balance-credit_balance else 0 END as result," + getElementUuidString(idArray) +
                "  related_lv_3_asset_uuid as fingerPrinter," +
                "  THIRD_ACCOUNT_UUID as thirdAccountUuid," +
                "  business_voucher_uuid as orderNo" +
                "  from ledger_book_shelf lbs " +
                "  WHERE 1 = 1" +
                "      AND lbs.ledger_book_no='" + queryParameter.getLedgerBookNO() + "'" +
                "      AND lbs.second_account_uuid='" + queryParameter.getAccountNO() + "'" +
                "      AND lbs.book_in_date >='" + DateUtils.convertBeforeDate2String(new Date()) + "'" +
                "      AND lbs.book_in_date <'" + DateUtils.convertDate2String(new Date()) + "'" +
                "      AND related_lv_3_asset_uuid is not null and related_lv_3_asset_uuid!=''" +
                "      AND (business_voucher_uuid is null or business_voucher_uuid='')" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                queryParameter.getFinancialContractUuid(), sql);

        Dataset<Row> df;
        String[] predicates = initPredicates(Constants.LEDGER_BOOK_SHELF, queryParameter);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }
        df = df.filter(new Column("thirdAccountUuid").isin((Object[]) idArray));

        Column[] groupByColumns = new Column[]{
                new Column("fingerPrinter"),
                new Column("thirdAccountUuid"),
                new Column("bankCard"),
                new Column("orderNo")};

        df = df.groupBy(groupByColumns).
                agg(sum("result").as("totalAmount"),
                        sum("total0").as("principal"),
                        sum("total1").as("interest"),
                        sum("total2").as("loanServiceFee"),
                        sum("total3").as("loanTechFee"),
                        sum("total4").as("loanOtherFee"),
                        sum("total5").as("punishment"),
                        sum("total6").as("overdueFee"),
                        sum("total7").as("overdueServiceFee"),
                        sum("total8").as("overdueOtherFee")).
                drop(new Column("thirdAccountUuid"));
        df.printSchema();

        return df;
    }

    @Override
    public Dataset<Row> getCashFingerPrinterOfZeroCashFlow(QueryParameter queryParameter, String date) {
        Object[] idArray = THIRD_ACCOUNT_UUID.toArray();

        String sql = "(SELECT" +
                "  id as id," +
                "  second_account_name as bankCard," +
                "  CASE WHEN THIRD_ACCOUNT_UUID IN (" + getAllUuidsString(idArray) + ") THEN" +
                "  debit_balance-credit_balance else 0 END as result," + getElementUuidString(idArray) +
                "  related_lv_3_asset_uuid as fingerPrinter," +
                "  THIRD_ACCOUNT_UUID as thirdAccountUuid," +
                "  business_voucher_uuid as orderNo" +
                "  from ledger_book_shelf lbs " +
                "  WHERE 1 = 1" +
                "      AND lbs.ledger_book_no='" + queryParameter.getLedgerBookNO() + "'" +
                "      AND lbs.second_account_uuid='" + queryParameter.getAccountNO() + "'" +
                "      AND lbs.book_in_date >='" + date + "'" +
                "      AND lbs.book_in_date <'" + DateUtils.convertNextDate2String(date) + "'" +
                "      AND related_lv_3_asset_uuid is not null and related_lv_3_asset_uuid!=''" +
                "      AND (business_voucher_uuid is null or business_voucher_uuid='')" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                queryParameter.getFinancialContractUuid(), sql);

        Dataset<Row> df;
        String[] predicates = initPredicates(Constants.LEDGER_BOOK_SHELF, queryParameter);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }
        df = df.filter(new Column("thirdAccountUuid").isin((Object[]) idArray));

        Column[] groupByColumns = new Column[]{
                new Column("fingerPrinter"),
                new Column("thirdAccountUuid"),
                new Column("bankCard"),
                new Column("orderNo")};

        df = df.groupBy(groupByColumns).
                agg(sum("result").as("totalAmount"),
                        sum("total0").as("principal"),
                        sum("total1").as("interest"),
                        sum("total2").as("loanServiceFee"),
                        sum("total3").as("loanTechFee"),
                        sum("total4").as("loanOtherFee"),
                        sum("total5").as("punishment"),
                        sum("total6").as("overdueFee"),
                        sum("total7").as("overdueServiceFee"),
                        sum("total8").as("overdueOtherFee")).
                drop(new Column("thirdAccountUuid"));
        df.printSchema();

        return df;
    }

    @Override
    public Dataset<Row> getCashFingerPrinterOfOneCashFlow(QueryParameter queryParameter) {
        Object[] idArray = THIRD_ACCOUNT_UUID.toArray();
        Object[] orderNOs = getOrderNOsOfCountEqualsOne(queryParameter.getFinancialContractUuid(), DateUtils.getYesterdayFormatYYMMDD());
        String sql = "(SELECT" +
                "  id as id," +
                "  second_account_name as bankCard," +
                "  CASE WHEN THIRD_ACCOUNT_UUID IN (" + getAllUuidsString(idArray) + ") THEN" +
                "  debit_balance-credit_balance else 0 END as result," + getElementUuidString(idArray) +
                "  related_lv_3_asset_uuid as fingerPrinter," +
                "  THIRD_ACCOUNT_UUID as thirdAccountUuid," +
                "  business_voucher_uuid as orderNo" +
                "  from ledger_book_shelf lbs " +
                "  WHERE 1 = 1" +
                "      AND lbs.ledger_book_no='" + queryParameter.getLedgerBookNO() + "'" +
                "      AND lbs.second_account_uuid='" + queryParameter.getAccountNO() + "'" +
                "      AND lbs.book_in_date >='" + DateUtils.convertBeforeDate2String(new Date()) + "'" +
                "      AND lbs.book_in_date <'" + DateUtils.convertDate2String(new Date()) + "'" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                queryParameter.getFinancialContractUuid(), sql);

        Dataset<Row> df;
        String[] predicates = initPredicates(Constants.LEDGER_BOOK_SHELF, queryParameter);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.filter(new Column("thirdAccountUuid").isin((Object[]) idArray));
        if (orderNOs.length != 0) {
            df = df.filter(new Column("orderNo").isin((Object[]) orderNOs));
        }

        Column[] groupByColumns = new Column[]{
                new Column("fingerPrinter"),
                new Column("thirdAccountUuid"),
                new Column("bankCard"),
                new Column("orderNo")};

        return df.groupBy(groupByColumns).
                agg(sum("result").as("totalAmount"),
                        sum("total0").as("principal"),
                        sum("total1").as("interest"),
                        sum("total2").as("loanServiceFee"),
                        sum("total3").as("loanTechFee"),
                        sum("total4").as("loanOtherFee"),
                        sum("total5").as("punishment"),
                        sum("total6").as("overdueFee"),
                        sum("total7").as("overdueServiceFee"),
                        sum("total8").as("overdueOtherFee")).
                drop(new Column("thirdAccountUuid"));
    }


    @Override
    public Dataset<Row> getAccountInfoFromCashFlow(QueryParameter queryParameter, Object[] fingerPrinters){
        String sql = "(select"
            + " id,"
            + " string_field_two as join_key,"
            + " bank_sequence_no as cashFlowNo," // 流水号
            + " '借' as accountSide,"//借贷标志
            + " transaction_amount as amount,"//发生金额
            + " counter_account_no as counterAccountNo,"//对方账户
            + " counter_account_name as counterAccountName,"// 对方户名
            + " counter_bank_info as counterBankInfo," // 对方开户行
            + " transaction_time as transactionTime,"// 入账时间
            + " remark,"// 摘要
            + " other_remark as otherRemark" // 附言
            + " from cash_flow where"
            + " account_side = 1"
            + " and host_account_no='" + queryParameter.getHostAccountNo() + "'";

        if (fingerPrinters != null && fingerPrinters.length == 0){
            sql = sql + " and string_field_two in ('')";
        }
        sql = sql + " ) temp";
//            -- 注意related_lv_3_asset_uuid没有索引

        log.info("getAccountInfoFromCashFlow, {}, {}", queryParameter.getFinancialContractUuid());

        String[] predicates = initPredicates("cash_flow");
        Dataset<Row> result;
        if (predicates == null) {
            result = loadDataFromTable(sql).drop("id");
        } else {
            result = loadDataFromTable(sql, predicates).drop("id");
        }

        if (fingerPrinters != null && fingerPrinters.length != 0) {
            result = result.filter(new Column("join_key").isin((Object[])fingerPrinters));
        }
        return result;
    }

    @Override
    public Dataset<Row> getBankCorporateCashFlow(QueryParameter queryParameter, String beginTime, String endTime) {

        String sql = "select"
            + " id,"
            + " bank_sequence_no,"
            + " (case account_side when 0 then '贷' when 1 then '借' end ) as 'account_side',"
            + " transaction_amount"
            + " from cash_flow"
            + " where account_side=1 and host_account_no='" + queryParameter.getHostAccountNo() + "'"
            + " and transaction_time>='" + beginTime + "'"
            + " and transaction_time<'" + endTime + "'";

        String[] predicates = initPredicates("cash_flow");
        Dataset<Row> result;
        if (predicates == null) {
            result = loadDataFromTable(sql).drop("id");
        } else {
            result = loadDataFromTable(sql, predicates).drop("id");
        }
        return result;
    }

    @Override
    public Dataset<Row> getCashFingerPrinterOfOneCashFlow(QueryParameter queryParameter, String date) {
        Object[] idArray = THIRD_ACCOUNT_UUID.toArray();
        Object[] orderNOs = getOrderNOsOfCountEqualsOne(queryParameter.getFinancialContractUuid(), date);
        String sql = "(SELECT" +
                "  id as id," +
                "  second_account_name as bankCard," +
                "  CASE WHEN THIRD_ACCOUNT_UUID IN (" + getAllUuidsString(idArray) + ") THEN" +
                "  debit_balance-credit_balance else 0 END as result," + getElementUuidString(idArray) +
                "  related_lv_3_asset_uuid as fingerPrinter," +
                "  THIRD_ACCOUNT_UUID as thirdAccountUuid," +
                "  business_voucher_uuid as orderNo" +
                "  from ledger_book_shelf lbs " +
                "  WHERE 1 = 1" +
                "      AND lbs.ledger_book_no='" + queryParameter.getLedgerBookNO() + "'" +
                "      AND lbs.second_account_uuid='" + queryParameter.getAccountNO() + "'" +
                "      AND lbs.book_in_date >='" + date + "'" +
                "      AND lbs.book_in_date <'" + DateUtils.convertNextDate2String(date) + "'" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                queryParameter.getFinancialContractUuid(), sql);

        Dataset<Row> df;
        String[] predicates = initPredicates(Constants.LEDGER_BOOK_SHELF, queryParameter);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.repartition(20).
                filter(new Column("thirdAccountUuid").isin((Object[]) idArray));

        if (orderNOs.length != 0) {
            df = df.filter(new Column("orderNo").isin((Object[]) orderNOs));
        }

        Column[] groupByColumns = new Column[]{
                new Column("fingerPrinter"),
                new Column("thirdAccountUuid"),
                new Column("bankCard"),
                new Column("orderNo")};

        df = df.groupBy(groupByColumns).
                agg(sum("result").as("totalAmount"),
                        sum("total0").as("principal"),
                        sum("total1").as("interest"),
                        sum("total2").as("loanServiceFee"),
                        sum("total3").as("loanTechFee"),
                        sum("total4").as("loanOtherFee"),
                        sum("total5").as("punishment"),
                        sum("total6").as("overdueFee"),
                        sum("total7").as("overdueServiceFee"),
                        sum("total8").as("overdueOtherFee")).
                drop(new Column("thirdAccountUuid"));
        df.printSchema();
        df.show(false);
        return df;
    }

    @Override
    public Dataset<Row> getCashFingerPrinterFromOutlierChannelCode(QueryParameter queryParameter) {
        Object[] idArray = THIRD_ACCOUNT_UUID.toArray();
        Object[] outlierChannelCodes = getOutlierChannelCode(queryParameter.getFinancialContractUuid());
        String sql = "(SELECT" +
                "  id as id," +
                "  second_account_name as bankCard," +
                "  CASE WHEN THIRD_ACCOUNT_UUID IN (" + getAllUuidsString(idArray) + ") THEN" +
                "  debit_balance-credit_balance else 0 END as result," + getElementUuidString(idArray) +
                "  related_lv_3_asset_uuid as fingerPrinter," +
                "  THIRD_ACCOUNT_UUID as thirdAccountUuid," +
                "  lbs.second_account_uuid as secondAccountUuid," +
                "  business_voucher_uuid as orderNo" +
                "  from ledger_book_shelf lbs " +
                "  WHERE 1 = 1" +
                "      AND lbs.ledger_book_no='" + queryParameter.getLedgerBookNO() + "'" +
                "      AND lbs.book_in_date >='" + DateUtils.convertBeforeDate2String(new Date()) + "'" +
                "      AND lbs.book_in_date <'" + DateUtils.convertDate2String(new Date()) + "'" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                queryParameter.getFinancialContractUuid(), sql);

        Dataset<Row> df;
        String[] predicates = initPredicates(Constants.LEDGER_BOOK_SHELF, queryParameter);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.filter(new Column("thirdAccountUuid").isin((Object[]) idArray));

        if (outlierChannelCodes.length != 0) {
            df = df.filter(new Column("secondAccountUuid").isin((Object[]) outlierChannelCodes));
        }
        Column[] groupByColumns = new Column[]{
                new Column("fingerPrinter"),
                new Column("thirdAccountUuid"),
                new Column("bankCard"),
                new Column("orderNo")};

        df = df.groupBy(groupByColumns).
                agg(sum("result").as("totalAmount"),
                        sum("total0").as("principal"),
                        sum("total1").as("interest"),
                        sum("total2").as("loanServiceFee"),
                        sum("total3").as("loanTechFee"),
                        sum("total4").as("loanOtherFee"),
                        sum("total5").as("punishment"),
                        sum("total6").as("overdueFee"),
                        sum("total7").as("overdueServiceFee"),
                        sum("total8").as("overdueOtherFee")).
                drop(new Column("thirdAccountUuid"));
        df.printSchema();
        df.show(false);

        return df;
    }

    @Override
    public Dataset<Row> getCashFingerPrinterFromOutlierChannelCode(QueryParameter queryParameter, String date) {
        Object[] idArray = THIRD_ACCOUNT_UUID.toArray();
        Object[] outlierChannelCodes = getOutlierChannelCode(queryParameter.getFinancialContractUuid());
        String sql = "(SELECT" +
                "  id as id," +
                "  second_account_name as bankCard," +
                "  CASE WHEN THIRD_ACCOUNT_UUID IN (" + getAllUuidsString(idArray) + ") THEN" +
                "  debit_balance-credit_balance else 0 END as result," + getElementUuidString(idArray) +
                "  related_lv_3_asset_uuid as fingerPrinter," +
                "  THIRD_ACCOUNT_UUID as thirdAccountUuid," +
                "  lbs.second_account_uuid as secondAccountUuid," +
                "  business_voucher_uuid as orderNo" +
                "  from ledger_book_shelf lbs " +
                "  WHERE 1 = 1" +
                "      AND lbs.ledger_book_no='" + queryParameter.getLedgerBookNO() + "'" +
                "      AND lbs.book_in_date >='" + date + "'" +
                "      AND lbs.book_in_date <'" + DateUtils.convertNextDate2String(date) + "'" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                queryParameter.getFinancialContractUuid(), sql);

        Dataset<Row> df;
        String[] predicates = initPredicates(Constants.LEDGER_BOOK_SHELF, queryParameter);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.filter(new Column("thirdAccountUuid").isin((Object[]) idArray));

        if (outlierChannelCodes.length != 0) {
            df = df.filter(new Column("secondAccountUuid").isin((Object[]) outlierChannelCodes));
        }
        Column[] groupByColumns = new Column[]{
                new Column("fingerPrinter"),
                new Column("thirdAccountUuid"),
                new Column("bankCard"),
                new Column("orderNo")};

        df = df.groupBy(groupByColumns).
                agg(sum("result").as("totalAmount"),
                        sum("total0").as("principal"),
                        sum("total1").as("interest"),
                        sum("total2").as("loanServiceFee"),
                        sum("total3").as("loanTechFee"),
                        sum("total4").as("loanOtherFee"),
                        sum("total5").as("punishment"),
                        sum("total6").as("overdueFee"),
                        sum("total7").as("overdueServiceFee"),
                        sum("total8").as("overdueOtherFee")).
                drop(new Column("thirdAccountUuid"));
        df.printSchema();
        df.show(false);
        return df;
    }

    @Override
    public void saveData2TableWithAppendMode(Dataset<Row> result, String table, String createDate, String financialContractUuid) {
        String sql = "DELETE FROM " + table
            + " WHERE create_date = '" + createDate + "' and financial_contract_uuid = '" + financialContractUuid + "'";
        log.info("sql is {}", sql);
        genericJdbcSupport.executeSQL(sql, new HashMap<>(0));
        saveData2Table(result, table, SaveMode.Append);
    }

    private String getElementUuidString(Object[] idArray) {
        StringBuilder elementUuid = new StringBuilder();
        for (int i = 0; i < idArray.length/2; i++) {
            elementUuid.append("CASE WHEN THIRD_ACCOUNT_UUID IN ('").append(idArray[2 * i]).append("','")
                .append(idArray[2 * i + 1]).append("') THEN debit_balance-credit_balance else 0 END as total").append(i)
                .append(",");
        }
        return elementUuid.toString();
    }



    @Override
    public Dataset<Row> getCashFingerPrinterOfCashFlow(QueryParameter queryParameter, String date) {
        Object[] roUuids = getRoUuids(queryParameter.getFinancialContractUuid(), date);
        String sql = "(SELECT" +
                "  po.id as id," +
                "  po.host_account_no as bankCard," +
                "  cf.string_field_two  as fingerPrinter," +
                "  po.amount as totalAmount," +
                "  po.order_uuid as orderNo," +
                "  po.outlier_document_uuid as outlierDocumentUuid," +
                "  po.uuid as poUuid" +
                "  from payment_order po inner join cash_flow cf on cf.cash_flow_uuid=po.outlier_document_uuid";

        if (roUuids.length == 0) {
            sql = sql + "  where po.order_uuid IN ('')";
        }
        sql = sql + ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                queryParameter.getFinancialContractUuid(), sql);

        Dataset<Row> df;
        String[] predicates = initPredicates("payment_order");
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        if (roUuids.length != 0) {
            df = df.filter(new Column("orderNo").isin(roUuids));
        }

        df = df.drop(new Column("id")).
                drop(new Column("poUuid")).
                drop(new Column("outlierDocumentUuid"));
        df.printSchema();
        df.show(false);
        return df;
    }

    @Override
    public Dataset<Row> getCashFingerPrinterFromOrderNO(QueryParameter queryParameter) {
        Object[] orderNos = getOrderNOs(queryParameter.getFinancialContractUuid(), DateUtils.getYesterdayFormatYYMMDD());
        Object[] idArray = THIRD_ACCOUNT_UUID.toArray();

        String sql = "(SELECT" +
                "  id as id," +
                "  business_voucher_uuid as orderNo," +
                "  CASE WHEN THIRD_ACCOUNT_UUID IN (" + getAllUuidsString(idArray) + ") THEN" +
                "  debit_balance-credit_balance else 0 END as result," + getElementUuidString(idArray) +
                "  third_account_uuid as thirdAccountUuid" +
                "  from ledger_book_shelf lbs " +
                "  WHERE 1 = 1" +
                "      AND lbs.ledger_book_no='" + queryParameter.getLedgerBookNO() + "'" +
                "      AND lbs.book_in_date >='" + DateUtils.convertBeforeDate2String(new Date()) + "'" +
                "      AND lbs.book_in_date <'" + DateUtils.convertDate2String(new Date()) + "'" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                queryParameter.getFinancialContractUuid(), sql);

        Dataset<Row> df;
        String[] predicates = initPredicates(Constants.LEDGER_BOOK_SHELF, queryParameter);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.filter(new Column("thirdAccountUuid").isin(idArray));

        if (orderNos.length != 0) {
            df = df.filter(new Column("orderNo").isin(orderNos));
        }

        Column[] groupByColumns = new Column[]{
                new Column("orderNo"),
                new Column("thirdAccountUuid")};

        df = df.groupBy(groupByColumns).
                agg(sum("result").as("totalAmount"),
                        sum("total0").as("principal"),
                        sum("total1").as("interest"),
                        sum("total2").as("loanServiceFee"),
                        sum("total3").as("loanTechFee"),
                        sum("total4").as("loanOtherFee"),
                        sum("total5").as("punishment"),
                        sum("total6").as("overdueFee"),
                        sum("total7").as("overdueServiceFee"),
                        sum("total8").as("overdueOtherFee")).
                drop(new Column("thirdAccountUuid"));
        df.printSchema();

        return df;
    }

    @Override
    public Dataset<Row> getCashFingerPrinterFromOrderNO(QueryParameter queryParameter, String date) {
        Object[] orderNos = getOrderNOs(queryParameter.getFinancialContractUuid(), date);
        Object[] idArray = THIRD_ACCOUNT_UUID.toArray();

        String sql = "(SELECT" +
                "  id as id," +
                "  business_voucher_uuid as orderNo," +
                "  CASE WHEN THIRD_ACCOUNT_UUID IN (" + getAllUuidsString(idArray) + ") THEN" +
                "  debit_balance-credit_balance else 0 END as result," + getElementUuidString(idArray) +
                "  third_account_uuid as thirdAccountUuid" +
                "  from ledger_book_shelf lbs " +
                "  WHERE 1 = 1" +
                "      AND lbs.ledger_book_no='" + queryParameter.getLedgerBookNO() + "'" +
                "      AND lbs.book_in_date >='" + date + "'" +
                "      AND lbs.book_in_date <'" + DateUtils.convertNextDate2String(date) + "'" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                queryParameter.getFinancialContractUuid(), sql);

        Dataset<Row> df;
        String[] predicates = initPredicates(Constants.LEDGER_BOOK_SHELF, queryParameter);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.filter(new Column("thirdAccountUuid").isin(idArray));

        if (orderNos.length != 0) {
            df = df.filter(new Column("orderNo").isin(orderNos));
        }

        Column[] groupByColumns = new Column[]{
                new Column("orderNo"),
                new Column("thirdAccountUuid")};

        df = df.groupBy(groupByColumns).
                agg(sum("result").as("totalAmount"),
                        sum("total0").as("principal"),
                        sum("total1").as("interest"),
                        sum("total2").as("loanServiceFee"),
                        sum("total3").as("loanTechFee"),
                        sum("total4").as("loanOtherFee"),
                        sum("total5").as("punishment"),
                        sum("total6").as("overdueFee"),
                        sum("total7").as("overdueServiceFee"),
                        sum("total8").as("overdueOtherFee")).
                drop(new Column("thirdAccountUuid"));
        df.printSchema();
        df.show(false);
        return df;
    }

    private String getAllUuidsString(Object[] idArray) {
        StringBuilder allUuids = new StringBuilder();
        for (Object id : idArray) {
            allUuids.append("'").append(id).append("'").append(",");
        }
        return allUuids.toString().substring(0, allUuids.length() - 1);
    }

    private String[] getOrderNOs(String financialContractUuid, String date) {
        String sql = "(SELECT" +
                "  min(ro.id) as id," +
                "  ro.order_uuid as orderUuid" +
                "  from repayment_order ro inner join payment_order po on ro.order_uuid=po.order_uuid" +
                "  WHERE 1 = 1" +
                "      AND ro.first_repayment_way_group IN (2, 3)" +
                "      AND po.pay_status=1 and po.pay_way=0" +
                "      AND ro.order_create_time>='" + date + "'" +
                "      AND ro.order_last_modified_time<'" + DateUtils.convertNextDate2String(date) + "'" +
                "      AND ro.financial_contract_uuid='" + financialContractUuid + "'" +
                "      group by orderUuid having count(orderUuid)>=1" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                financialContractUuid, sql);

        Dataset<Row> df;
        String[] predicates = initPredicates("repayment_order", financialContractUuid);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.drop(new Column("id"));
        df.printSchema();


        Object[] rows = (Object[]) df.collect();
        String[] resultArray = new String[rows.length];
        if (rows.length == 0) {
            return new String[0];
        }

        for (int i = 0; i < rows.length; i++) {
            log.info("element is:{}", ((Row) rows[i]).getString(0));
            resultArray[i] = ((Row) rows[i]).getString(0);
        }

        return resultArray;
    }

    private String[] getOrderNOsOfCountEqualsOne(String financialContractUuid, String date) {
        String sql = "(SELECT" +
                "  min(ro.id) as id," +
                "  min(ro.first_repayment_way_group) as firstRepaymentWayGroup," +
                "  ro.order_uuid as orderUuid" +
                "  from repayment_order ro inner join payment_order po on ro.order_uuid=po.order_uuid" +
                "  WHERE 1 = 1" +
                "      AND po.pay_status=1 and po.pay_way=0" +
                "      AND ro.order_create_time>='" + date + "'" +
                "      AND ro.order_last_modified_time<'" + DateUtils.convertNextDate2String(date) + "'" +
                "      AND ro.financial_contract_uuid='" + financialContractUuid + "'" +
                "      group by orderUuid having count(orderUuid)=1" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                financialContractUuid, sql);

        Dataset<Row> df;
        String[] predicates = initPredicates("repayment_order", financialContractUuid);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.filter(new Column("firstRepaymentWayGroup").isin(2, 3)).
                drop(new Column("id")).
                drop(new Column("firstRepaymentWayGroup"));
        df.printSchema();
        df.show(false);

        Object[] rows = (Object[]) df.collect();
        String[] resultArray = new String[rows.length];
        if (rows.length == 0) {
            return new String[0];
        }

        for (int i = 0; i < rows.length; i++) {
            log.info("element is:{}", ((Row) rows[i]).getString(0));
            resultArray[i] = ((Row) rows[i]).getString(0);
        }

        return resultArray;
    }

    private String[] getRoUuids(String financialContractUuid, String date) {
        String sql = "(SELECT" +
                "  min(ro.id) as id," +
                "  min(ro.first_repayment_way_group) as firstRepaymentWayGroup," +
                "  ro.order_uuid as orderUuid" +
                "  from repayment_order ro inner join payment_order po on ro.order_uuid=po.order_uuid" +
                "  WHERE 1 = 1" +
                "      AND po.pay_status=1 and po.pay_way=0" +
                "      AND ro.order_create_time>='" + date + "'" +
                "      AND ro.order_last_modified_time<'" + DateUtils.convertNextDate2String(date) + "'" +
                "      AND ro.financial_contract_uuid='" + financialContractUuid + "'" +
                "      group by orderUuid having count(orderUuid)>1" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                financialContractUuid, sql);

        Dataset<Row> df;
        String[] predicates = initPredicates("repayment_order", financialContractUuid);
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.filter(new Column("firstRepaymentWayGroup").isin(2, 3)).
                drop(new Column("id")).
                drop(new Column("firstRepaymentWayGroup"));
        df.printSchema();
        df.show(false);

        Object[] rows = (Object[]) df.collect();
        String[] resultArray = new String[rows.length];
        if (rows.length == 0) {
            return new String[0];
        }

        for (int i = 0; i < rows.length; i++) {
            log.info("element is:{}", ((Row) rows[i]).getString(0));
            resultArray[i] = ((Row) rows[i]).getString(0);
        }

        return resultArray;
    }

    public String[] getOutlierChannelCode(String financialContractUuid) {
        String sql = "(SELECT" +
                "  id as id," +
                "  clearing_no as clearingNo," +
                "  outlier_channel_name as outlierChannelCode" +
                "  from payment_channel_information " +
                "  WHERE 1 = 1" +
                "      AND related_financial_contract_uuid='" + financialContractUuid + "'" +
                ") temp";

        log.info("\n\n\n\n\nfinancialContractUuid is:{},sql is:{}\n\n\n\n\n",
                financialContractUuid, sql);

        Dataset<Row> df;
        String[] predicates = initPredicates("payment_channel_information",
                financialContractUuid, "related_financial_contract_uuid");
        if (predicates == null) {
            df = loadDataFromTable(sql);
        } else {
            df = loadDataFromTable(sql, predicates);
        }

        df = df.drop(new Column("id"));

        df.printSchema();
        df.show(false);

        Object[] rows = (Object[]) df.collect();
        if (rows.length == 0) {
            return new String[0];
        }

        List<String> resultList = new ArrayList<>();
        for (Object row : rows) {
            String clearingNo = ((Row) row).getString(0);
            String outlierChannelCode = ((Row) row).getString(1);

            log.info("outlierChannelCode is:{}", outlierChannelCode);
            log.info("clearingNo is:{}", clearingNo);

            String element = "";
            if (StringUtils.isNotEmpty(outlierChannelCode)) {
                element = "60000." + outlierChannelCode;
                resultList.add(element);
            }

            if (StringUtils.isNotEmpty(clearingNo)) {
                element = "60000." + outlierChannelCode + "_" + clearingNo;
                resultList.add(element);
            }
        }

        String[] resultArray = new String[resultList.size()];
        resultList.toArray(resultArray);
        return resultArray;
    }

    private SaveMode getSaveMode(FingerPrinterParameter fingerPrinterParameter) {
        long existed = loadDataFromTable("spark_schedule_job").
                filter("dataStreamUuid='" + fingerPrinterParameter.getDataStreamUuid() + "'").
                count();

        return existed != 0 ? SaveMode.Ignore : SaveMode.Append;
    }
}