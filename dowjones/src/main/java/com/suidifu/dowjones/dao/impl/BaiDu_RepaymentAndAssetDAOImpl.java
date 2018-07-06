package com.suidifu.dowjones.dao.impl;

import static org.apache.spark.sql.functions.countDistinct;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.sum;

import com.suidifu.dowjones.config.PathConfig;
import com.suidifu.dowjones.dao.BaiDu_RepaymentAndAssetDAO;
import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.model.AssetExcel;
import com.suidifu.dowjones.model.RepaymentExcel;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.ExcelUtil;
import com.suidifu.dowjones.utils.FTPUtils;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import com.suidifu.dowjones.utils.SQLUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.ForeachPartitionFunction;
import org.apache.spark.api.java.function.MapPartitionsFunction;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.net.ftp.FtpProtocolException;

/**
 * Created by zxj on 2018/1/22.
 */
@Repository
@Slf4j
public class BaiDu_RepaymentAndAssetDAOImpl extends BaseDAO implements BaiDu_RepaymentAndAssetDAO {
    @Autowired
    private GenericJdbcSupport genericJdbcSupport;
    @Resource
    private PathConfig pathConfig;
    @Autowired
    private FTPUtils ftpUtils;

    @Override
    public void saveData(Dataset<Row> rows, String sql) {
        saveData2Table(rows, sql, SaveMode.Append);
    }

    /**
     * 导出新增还款文件
     * @param dataList
     * @return
     */
    private String saveIncrementalRepaymentResult2File(List<Map<String, Object>> dataList, Date time) {
        String path = pathConfig.getBaiduQianlongReportTask();
        String fileName = "QL-XJ-1_NEWINSTALLMENT_" + DateUtils.getDateFormatYYMMDD(time).replace("-","") + "_1.xlsx";
        String fullPath = path + fileName;
        File resultFile = new File(fullPath);
        FileOutputStream outResult = null;
        ExcelUtil<RepaymentExcel> excelTool = new ExcelUtil<>(RepaymentExcel.class);
        try {
            List<RepaymentExcel> excels = new ArrayList<>();
            outResult = new FileOutputStream(resultFile);
            if (dataList == null || dataList.size() <= 0) {
                excels.add(new RepaymentExcel());
            } else {
                for (Map<String, Object> data : dataList) {
                    excels.add(getIncrementalRepaymentExcelFrom(data));
                }
            }
            excelTool.exportExcelXlsx(excels, "Sheet1", outResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outResult);
        }
        return fullPath;
    }


    private String generateFileFromDb(String fuid, Date time) {
        String path = pathConfig.getBaiduQianlongReportTask();
        String fileName = "QL-XJ-1_NEWINSTALLMENT_" + DateUtils.getDateFormatYYMMDD(time).replace("-","") + "_1.xlsx";
        String fullPath = path + fileName;
        File resultFile = new File(fullPath);
        FileOutputStream outResult = null;
        ExcelUtil<RepaymentExcel> excelTool = new ExcelUtil<>(RepaymentExcel.class);
        try {
            String sql = "select * from bd_incremental_repayment_file where financial_contract_uuid = :fuid and create_date = :createDate";
            Map<String, Object> params = new HashMap<>();
            params.put("fuid", fuid);
            params.put("createDate", DateUtils.getDateFormatYYMMDD(time));
//            Dataset<Row> results = loadDataFromTable(sql);
           List<Map<String, Object>> results =  this.genericJdbcSupport.queryForList(sql, params);
            List<RepaymentExcel> excels = new ArrayList<>();
            outResult = new FileOutputStream(resultFile);
            if (results == null) {
                excels.add(new RepaymentExcel());
            } else {
                   for (Map<String, Object> o : results){
                       excels.add(getIncrementalRepaymentExcelFromTable(o));
                   }
            }

            excelTool.exportExcelXlsx(excels, "Sheet1", outResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outResult);
        }
        return fullPath;
    }

    private String saveIncrementalRepaymentResult2FileUsingDataSet(Dataset<Row> results, Date time) {
        String path = pathConfig.getBaiduQianlongReportTask();
        String fileName = "QL-XJ-1_NEWINSTALLMENT_" + DateUtils.getDateFormatYYMMDD(time).replace("-","") + "_1.xlsx";
        String fullPath = path + fileName;
        File resultFile = new File(fullPath);
        FileOutputStream outResult = null;
        ExcelUtil<RepaymentExcel> excelTool = new ExcelUtil<>(RepaymentExcel.class);
        try {
            List<RepaymentExcel> excels = new ArrayList<>();
            outResult = new FileOutputStream(resultFile);
            if (results == null) {
                excels.add(new RepaymentExcel());
            } else {
                Dataset<RepaymentExcel> mappedData = results.mapPartitions(
                    (MapPartitionsFunction<Row, RepaymentExcel>) it -> {
                        List<RepaymentExcel> filteredResult = new ArrayList<RepaymentExcel>();
                        while (it.hasNext()) {
                            Row row = it.next();
                            filteredResult.add(getIncrementalRepaymentExcelFromDataSet(row));
                        }
                        return filteredResult.iterator();
                    }, Encoders.javaSerialization(RepaymentExcel.class));
//                excels = new ArrayList<>(mappedData.javaRDD().collect());

                Iterator<RepaymentExcel> iter = mappedData.toJavaRDD().toLocalIterator();
                while (iter.hasNext()) {
                    excels.add(iter.next());
                }

//                Iterator<Row> iter = results.toJavaRDD().toLocalIterator();
//                while (iter.hasNext()){
//                    Row row = iter.next();
//                    excels.add(getIncrementalRepaymentExcelFromDataSet(row));
//                }

//                results.foreachPartition((ForeachPartitionFunction<Row>) t -> {
//                    while (t.hasNext()) {
//                        excels.add(getIncrementalRepaymentExcelFromDataSet(t.next()));
//                    }
//                });
                // excel 为空

//                results.foreach((ForeachFunction<Row>) row -> excels.add(getIncrementalRepaymentExcelFromDataSet(row)));
                // excel 为空

//                Iterator<Row> itRows = results.toLocalIterator();
//                while (itRows.hasNext()){
//                    Row x = itRows.next();
//                    excels.add(getIncrementalRepaymentExcelFromDataSet(x));
//                }

                //OOM

//                for (Map<String, Object> data : dataList) {
//                    excels.add(getIncrementalRepaymentExcelFrom(data));
//                }

            }

            excelTool.exportExcelXlsx(excels, "Sheet1", outResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outResult);
        }
        return fullPath;
    }


    private RepaymentExcel getIncrementalRepaymentExcelFromDataSet(Row data) {
        RepaymentExcel excel = new RepaymentExcel();

        excel.setContractUniqueId(fetchFieldValue(data, "unique_id"));
        excel.setCurrentPeriod(fetchFieldValue(data, "current_period"));
        excel.setAssetRecycleDate(fetchFieldValue(data, "asset_recycle_date"));
        excel.setAssetPrincipalValue(fetchFieldValue(data, "asset_principal_value"));
        excel.setAssetInterestValue(fetchFieldValue(data, "asset_interest_value"));
        String chargeAmount = fetchFieldValue(data, "charge_amount");
        excel.setChargeAmount(chargeAmount == null ? "0.00" : chargeAmount);
        excel.setLoanAssetPrincipal("0");
        excel.setLoanAssetInterest("0");
        excel.setLoanAssetAmount("0");
        excel.setActualRecycleDate(null);
        excel.setTradeTime(null);
        excel.setCapitalType(null);

        return excel;
    }

    private RepaymentExcel getIncrementalRepaymentExcelFrom(Map<String, Object> data) {
        RepaymentExcel excel = new RepaymentExcel();

        excel.setContractUniqueId(fetchFieldValue(data, "uniqueId"));
        excel.setCurrentPeriod(fetchFieldValue(data, "currentPeriod"));
        excel.setAssetRecycleDate(fetchFieldValue(data, "assetRecycleDate"));
        excel.setAssetPrincipalValue(fetchFieldValue(data, "assetPrincipalValue"));
        excel.setAssetInterestValue(fetchFieldValue(data, "assetInterestValue"));
        String chargeAmount = fetchFieldValue(data, "chargeAmount");
        excel.setChargeAmount(chargeAmount == null ? "0.00" : chargeAmount);
        excel.setLoanAssetPrincipal("0");
        excel.setLoanAssetInterest("0");
        excel.setLoanAssetAmount("0");
        excel.setActualRecycleDate(null);
        excel.setTradeTime(null);
        excel.setCapitalType(null);

        return excel;
    }


    private RepaymentExcel getIncrementalRepaymentExcelFromTable(Map<String, Object> data) {
        RepaymentExcel excel = new RepaymentExcel();

        excel.setContractUniqueId(fetchFieldValue(data, "contract_unique_id"));
        excel.setCurrentPeriod(fetchFieldValue(data, "current_period"));
        excel.setAssetRecycleDate(fetchFieldValue(data, "asset_recycle_date"));
        excel.setAssetPrincipalValue(fetchFieldValue(data, "asset_principal_value"));
        excel.setAssetInterestValue(fetchFieldValue(data, "asset_interest_value"));
        String chargeAmount = fetchFieldValue(data, "charge_amount");
        excel.setChargeAmount(chargeAmount == null ? "0.00" : chargeAmount);
        excel.setLoanAssetPrincipal("0");
        excel.setLoanAssetInterest("0");
        excel.setLoanAssetAmount("0");
        excel.setActualRecycleDate(null);
        excel.setTradeTime(null);
        excel.setCapitalType(null);

        return excel;
    }

    /**
     * 导出还款文件
     * @param time
     * @param results
     * @return
     */
    private final String saveRepaymentResult2File(Date time, Dataset<Row> results) {
        String path = pathConfig.getBaiduQianlongReportTask();
        String fileName = "QL-XJ-1_INSTALLMENT_" + DateUtils.getDateFormatYYMMDD(time).replace("-", "") + "_1.xlsx";
        String fullPath = path + fileName;
        File resultFile = new File(fullPath);
        FileOutputStream outResult = null;
        ExcelUtil<RepaymentExcel> excelTool = new ExcelUtil<>(RepaymentExcel.class);
        try {
            List<RepaymentExcel> excels = new ArrayList<>();
            outResult = new FileOutputStream(resultFile);
            if (results == null) {
                //返回空文件
                excels.add(new RepaymentExcel());
            } else {
                Iterator<Row> resultIt = results.toLocalIterator();
                while (resultIt.hasNext()) {
                    Row row = resultIt.next();
                    excels.add(getRepaymentExcelFrom(row));
                }
            }
            excelTool.exportExcelXlsx(excels, "Sheet1", outResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outResult);
        }
        return fullPath;
    }

    private RepaymentExcel getRepaymentExcelFrom(Row row) {

        RepaymentExcel excel = new RepaymentExcel();

        excel.setContractUniqueId(fetchFieldValue(row, "contract_unique_id"));
        excel.setCurrentPeriod(fetchFieldValue(row, "current_period"));
        excel.setAssetRecycleDate(fetchFieldValue(row, "asset_recycle_date"));
        excel.setAssetPrincipalValue(fetchFieldValue(row, "asset_principal_value"));
        excel.setAssetInterestValue(fetchFieldValue(row, "asset_interest_value"));
        String chargeAmount = fetchFieldValue(row, "charge_amount");
        excel.setChargeAmount(chargeAmount == null ? "0.00" : chargeAmount);
        excel.setLoanAssetPrincipal(fetchFieldValue(row, "loan_asset_principal"));
        excel.setLoanAssetInterest(fetchFieldValue(row, "loan_asset_interest"));
        excel.setLoanAssetAmount(fetchFieldValue(row, "loan_charge_amount"));
        excel.setActualRecycleDate(
            DateUtils.transferYYYYMMDDHHmmss2YYYYMMDD(fetchFieldValue(row, "actual_recycle_date")));
        excel.setTradeTime(DateUtils.transferYYYYMMDDHHmmss2YYYYMMDD(fetchFieldValue(row, "trade_time")));

        String journalVoucherType = fetchFieldValue(row, "journal_voucher_type");
        String secondJournalVoucherType = fetchFieldValue(row, "second_journal_voucher_type");

        String tmp = "";
        if (StringUtils.isNotBlank(journalVoucherType) && StringUtils.isNotBlank(secondJournalVoucherType) && Objects
            .equals(journalVoucherType, "7")) {
            tmp = "线上代扣 - " + secondJournalVoucherTypeMap.get(secondJournalVoucherType);
        } else if (StringUtils.isNotBlank(journalVoucherType)){
            tmp = "线下转账 - " + journalVoucherTypeMap.get(journalVoucherType);
        }
        excel.setCapitalType(tmp);

        return excel;
    }

    private String fetchFieldValue(Row row, String fieldName) {

        Object x;
        try {
            x = row.getAs(fieldName);
        } catch (Exception ignored) {
            x = null;
        }
        return x == null ? null : x.toString();
    }

    private Map<String, String> secondJournalVoucherTypeMap = new HashMap<String, String>() {
        {
            put("0", "系统代扣");
            put("1", "系统代扣");
            put("2", "商户代扣");
        }
    };

    private Map<String, String> journalVoucherTypeMap = new HashMap<String, String>() {
        {
            put("0", "线上扣款");
            put("1", "线下支付");
            put("2", "银行充值");
            put("3", "余额冲销还款计划");
            put("4", "余额支付退款");
            put("5", "委托转付");
            put("6", "余额提现");
            put("7", "第三方扣款");
            put("8", "余额冲销担保");
            put("9", "主动付款");
            put("10", "回购");
            put("11", "商户代偿");
            put("12", "商户付款担保");
            put("13", "清算流水关联");
            put("14", "差额划拨");
            put("15", "专户放款凭证");

        }
    };

    @Override
    public Set<Object> getContractUuidListFromJv(String financialContractUuid, String yesterday) {
        Set<Object> contractUuidList = new HashSet<>();
        //第一步取出当日生成的还款记录的贷款合同编号
        String jvSql = "SELECT"
            + " jv.id AS id,"
            + " jv.related_bill_contract_info_lv_2 AS contractUuid"
            + " FROM"
            + " journal_voucher jv"
            + " WHERE DATE(jv.issued_time) = '" + yesterday + "'"
            + " AND jv.status = 1"
            + " AND jv.journal_voucher_type IN (0, 1, 3, 5, 7, 9, 10, 11, 14)"
            + " AND jv.related_bill_contract_info_lv_1 = '" + financialContractUuid + "'";

        String[] jvPredicates = initPredicates("journal_voucher");

        Dataset<Row> jvDataSet = loadDataFromTable(SQLUtils.wrapperSQL(jvSql), jvPredicates);

        if (jvDataSet != null && jvDataSet.count() > 0) {
            jvDataSet = jvDataSet.drop("id").distinct();
            for (Row row: jvDataSet.collectAsList()) {
                contractUuidList.add(row.getAs("contractUuid"));
            }
        }

        //第二步取出变更(提前还款)的还款计划的贷款合同编号
        String assetSql = "SELECT"
            + " asset.id AS id,"
            + " asset.contract_uuid AS contractUuid"
            + " FROM asset_set asset"
            + " WHERE DATE(asset.create_time) = '" + yesterday + "'"
            + " AND asset.financial_contract_uuid = '" + financialContractUuid + "'"
            + " AND asset.can_be_rollbacked = 1"
            + " AND asset.plan_type = 1"
            + " AND asset.active_status = 0";

        String[] assetPredicates = initPredicates("asset_set");

        Dataset<Row> assetDataSet = loadDataFromTable(SQLUtils.wrapperSQL(assetSql), assetPredicates);

        if (assetDataSet != null && assetDataSet.count() > 0) {
            assetDataSet = assetDataSet.drop("id").distinct();
            for (Row row: assetDataSet.collectAsList()) {
                contractUuidList.add(row.getAs("contractUuid"));
            }
        }
        return contractUuidList;
    }

    @Override
    public List<Object> getContractUuidListFromRapp(String financialContractUuid, String yesterday) {
        String sql = "SELECT" +
            "   rp.id AS id," +
            "   c.uuid AS uuid" +
            "   FROM" +
            "   t_remittance_application rp" +
            "   INNER JOIN contract c ON c.contract_no = rp.contract_no" +
            "   AND rp.financial_contract_uuid = '"+ financialContractUuid +"'" +
            "   AND DATE(rp.last_modified_time) = '" + yesterday + "'" +
            "   AND rp.execution_status = 2";

        String[] predicates = initPredicates("t_remittance_application");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        List<Object> contractUuidList = new  ArrayList<>();

        if (dataSet != null && dataSet.count() > 0) {
            log.info("t_remittance_application dataset size is :{}", dataSet.count());

            dataSet = dataSet.drop("id").distinct();
            for (Row row: dataSet.collectAsList()) {
                contractUuidList.add(row.getAs("uuid"));
            }
        }
        return contractUuidList;
    }

    @Override
    public Dataset<Row> getContractDataset(Collection<Object> contractUuids) {
        if (contractUuids.size() <= 0) {
            return null;
        }
        String uuids = getUuidLists(contractUuids);
        if (StringUtils.isEmpty(uuids)) {
            return null;
        }
        String sql = "SELECT * FROM contract WHERE uuid IN (" + uuids + ")";
        String[] predicates = initPredicates("contract");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("contract dataset size is :{}", dataSet.count());
            dataSet = dataSet.distinct();
        }

        return dataSet;
    }

    @Override
    public Dataset<Row> getAssetDataset(Collection<Object> contractUuids) {
        if (contractUuids.size() <= 0) {
            return null;
        }

        String uuids = getUuidLists(contractUuids);
        if (StringUtils.isEmpty(uuids)) {
            return null;
        }
        String sql = "SELECT"
            + " id,"
            + " contract_uuid,"
            + " current_period,"
            + " create_time AS asset_create_time,"
            + " version_no,"
            + " asset_status,"
            + " asset_uuid,"
            + " asset_recycle_date,"
            + " CASE asset_status when 1 then actual_recycle_date else null end AS actual_recycle_date,"
            + " active_status,"
            + " asset_principal_value,"
            + " asset_interest_value"
            + " FROM asset_set"
            + " WHERE"
            + " contract_uuid IN ("+ uuids +")";

        String[] predicates = initPredicates("asset_set");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).drop("id");

        if (dataSet != null && dataSet.count() > 0) {
            log.info("asset dataset size is :{}", dataSet.count());
            dataSet = dataSet.distinct();
        }

        // 作废的还款计划位于 asset_set_history
        sql =  sql.replace("FROM asset_set ","FROM asset_set_history ");
        predicates = initPredicates("asset_set_history");
        Dataset<Row> historyDataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).drop("id");

        if (historyDataSet != null && historyDataSet.count() > 0) {
            log.info("assetSetHistory dataset size is :{}", historyDataSet.count());
            historyDataSet = historyDataSet.distinct();
        }

        if (dataSet != null && historyDataSet != null){
            return dataSet.union(historyDataSet);
        }

        if (dataSet == null){
            return historyDataSet;
        }

        return dataSet;
    }

    @Override
    public Dataset<Row> getJournalVoucherDataset(Collection<Object> assetUuids) {
        if (assetUuids.size() <= 0) {
            return null;
        }
        String uuids = getUuidLists(assetUuids);
        if (StringUtils.isEmpty(uuids)) {
            return null;
        }
        String sql = "SELECT *"
            + " FROM journal_voucher jv"
            + " WHERE"
            + " jv.journal_voucher_type IN (0, 1, 3, 5, 7, 9, 11, 12, 14)"
            + " AND jv.status = 1"
            + " AND jv.related_bill_contract_info_lv_3 IN ("+uuids+")";
        String[] predicates = initPredicates("journal_voucher");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("journal voucher dataset size is :{}", dataSet.count());
            dataSet = dataSet.drop("id");
        }

        return dataSet;
    }

    @Override
    public void doRepaymentFile(Dataset<Row> contractDataSet, Dataset<Row> assetDataSet, Dataset<Row> jvDataSet,Collection<Object> assetUuids, Date time) {
        if (contractDataSet == null || contractDataSet.count() <= 0 || assetDataSet == null || assetDataSet.count() <= 0) {
            String emptyFullPath = saveRepaymentResult2File(time , contractDataSet);
            ftpUtils.uploadFileToBaidu(new File(emptyFullPath), time);
            return;
        }
        String assetUuidLists = getUuidLists(assetUuids);
        //asset_set_extra_charge
        String sql = "SELECT"
            + " id,"
            + " asset_set_uuid,"
            + " CASE WHEN second_account_name IN ('SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE','SND_UNEARNED_LOAN_ASSET_OTHER_FEE','SND_UNEARNED_LOAN_ASSET_TECH_FEE','SND_RECIEVABLE_LOAN_PENALTY','TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION','TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE','TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE') THEN account_amount ELSE 0 END AS charge_amount"
            + " FROM asset_set_extra_charge"
            + " WHERE"
            + " asset_set_uuid IN ("+ assetUuidLists +")";
        String[] predicates = initPredicates("asset_set_extra_charge");

        Dataset<Row> assetcDataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).drop("id");

        Dataset<Row> resultData = assetDataSet.join(contractDataSet, assetDataSet.col("contract_uuid").equalTo(contractDataSet.col("uuid")));

        resultData = resultData.join(jvDataSet, resultData.col("asset_uuid").equalTo(jvDataSet.col("related_bill_contract_info_lv_3")), "left_outer");

        resultData = resultData.join(assetcDataSet, resultData.col("asset_uuid").equalTo(assetcDataSet.col("asset_set_uuid")), "left_outer").drop("asset_set_uuid");

        log.info("resultData size is :{}", resultData.count());

        if (resultData.count() <= 0) {
            String emptyFullPath = saveRepaymentResult2File(time , resultData);
            ftpUtils.uploadFileToBaidu(new File(emptyFullPath), time);
            return;
        }

        //取出所有有效的还款计划
        resultData = resultData.filter(resultData.col("active_status").equalTo("0"));
        //对每个还款记录的应还费用求和
        resultData = resultData.groupBy("asset_uuid", "journal_voucher_uuid","financial_contract_uuid", "actual_recycle_date",
            "trade_time", "journal_voucher_type", "second_journal_voucher_type", "unique_id", "current_period",
            "asset_recycle_date", "asset_principal_value", "asset_interest_value").agg(
            sum("charge_amount").as("charge_amount")
        );
        //unique_id 重命名为 contract_unique_id
        resultData = resultData.withColumnRenamed("unique_id", "contract_unique_id");

        //获取所有的jv uuid
        List<Row> jvUuidList = resultData.select("journal_voucher_uuid").distinct().collectAsList();

        List<String> jvUuidLists = new ArrayList<>();

        for (Row jvUuid : jvUuidList) {
            jvUuidLists.add("'" + jvUuid.getAs("journal_voucher_uuid")+ "'");
        }
        String jvUuids = StringUtils.join(jvUuidLists, ",");

        log.info("journal_voucher_uuid is :{}", jvUuids);

        //取出实收明细
        //通过还款记录uui查询实收本金，实收利息，实收费用（七项）
        String lbsSql = "SELECT"
            + " id,"
            + " journal_voucher_uuid AS journalVoucherUuid2,"
            + " CASE WHEN third_account_name IN ('TRD_BANK_SAVING_GENERAL_PRINCIPAL', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSEL_PRINCIPAL') THEN debit_balance - credit_balance ELSE 0 END AS loanAssetPrincipal,"
            + " CASE WHEN third_account_name IN ('TRD_BANK_SAVING_GENERAL_INTEREST', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST') THEN debit_balance - credit_balance ELSE 0 END AS loanAssetInterest,"
            + " CASE WHEN third_account_name IN ('TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE') THEN debit_balance - credit_balance ELSE 0 END AS loanAssetAmount"
            + " FROM ledger_book_shelf"
            + " where journal_voucher_uuid in (" + jvUuids + ")";

        String[] lbsPredicates = initPredicates("ledger_book_shelf");

        Dataset<Row> paidOffDs = loadDataFromTable(SQLUtils.wrapperSQL(lbsSql), lbsPredicates).drop("id");

        // 合并计算实收
        // 本金 利息 费用（7项之和）
        paidOffDs = paidOffDs.groupBy("journalVoucherUuid2").agg(
            sum("loanAssetPrincipal").as("loan_asset_principal"),
            sum("loanAssetInterest").as("loan_asset_interest"),
            sum("loanAssetAmount").as("loan_charge_amount")
        );

        Dataset<Row> resultDataset = resultData.join(paidOffDs,resultData.col("journal_voucher_uuid").equalTo(paidOffDs.col("journalVoucherUuid2")), "left_outer");

        resultDataset = resultDataset.drop("journalVoucherUuid2","asset_uuid");

        resultDataset = resultDataset.sort("contract_unique_id", "current_period");
        //新增 create_date列
        resultDataset = resultDataset.withColumn("create_date", lit(DateUtils.getDateFormatYYMMDD(time)));

        log.info("result data set row size :{}", resultDataset.count());

        resultDataset.printSchema();
        resultDataset.show();

        //保存还款数据 以及导出到文件
        long saveDataStartTime = System.currentTimeMillis();

        log.info("Start save repayment data file for :{} time is :{}", DateUtils.getDateFormatYYMMDD(time), saveDataStartTime);

//        saveData(resultDataset, "bd_repayment_file");

        String fullPath = saveRepaymentResult2File(time , resultDataset);

        ftpUtils.uploadFileToBaidu(new File(fullPath), time);

        long saveDataEndTime = System.currentTimeMillis();
        log.info("End save repayment file data end, time used :{}ms", saveDataEndTime - saveDataStartTime);
    }

    @Override
    public void doIncrementalRepaymentFile(Dataset<Row> contractDataSet, Dataset<Row> assetDataSet, Collection<Object> assetUuids,Collection<Object> contractUuidList, Date time) {
        if (contractDataSet == null || assetDataSet == null ) {
            String emptyFullPath = saveIncrementalRepaymentResult2File(new ArrayList<>(), time);
            ftpUtils.uploadFileToBaidu(new File(emptyFullPath), time);
            return;
        }
        String yesterday = DateUtils.getDateFormatYYMMDD(time);
        String assetUuidLists = getUuidLists(assetUuids);
        //获取应收费用信息 asset_set_extra_charge
        String sql = "SELECT"
            + " id,"
            + " asset_set_uuid,"
            + " CASE WHEN second_account_name IN ('SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE','SND_UNEARNED_LOAN_ASSET_OTHER_FEE','SND_UNEARNED_LOAN_ASSET_TECH_FEE') THEN account_amount ELSE 0 END AS charge_amount"
            + " FROM asset_set_extra_charge"
            + " WHERE"
            + " asset_set_uuid IN ("+ assetUuidLists +")";
        String[] predicates = initPredicates("asset_set_extra_charge");

        Dataset<Row> assetcDataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).drop("id");

        //还款文件信息
        Dataset<Row> dataSet = assetDataSet.join(contractDataSet, assetDataSet.col("contract_uuid").equalTo(contractDataSet.col("uuid")));

        dataSet = dataSet.join(assetcDataSet, dataSet.col("asset_uuid").equalTo(assetcDataSet.col("asset_set_uuid")), "left_outer").drop("asset_set_uuid");

//        log.info("raw row size:{}", dataSet.count());

        if (dataSet.count() <= 0) {
            saveIncrementalRepaymentResult2File(new ArrayList<>(), time);
            return;
        }

        Dataset<Row> allData = dataSet
            .groupBy("asset_uuid", "contract_uuid","version_no", "unique_id", "current_period", "asset_recycle_date",
                "asset_principal_value", "asset_interest_value", "asset_create_time", "financial_contract_uuid").agg(
                sum("charge_amount").as("charge_amount")
            );

        Dataset<Row> allResult = null;

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Object contractUuid:contractUuidList) {
            Dataset<Row> currentAssetSets = allData.filter(allData.col("contract_uuid").equalTo(contractUuid));

            //第一版本 版本号
            Object firstVersion  = currentAssetSets.sort("asset_create_time").first().getAs("version_no");
            //按应还日排序后的第一版本还款计划
            Dataset<Row> allFirstVersions = currentAssetSets.filter(currentAssetSets.col("version_no").equalTo(firstVersion)).sort("current_period");
            //insertSql
            String insertSql = "INSERT INTO" +
                "   bd_incremental_repayment_file" +
                "   (contract_unique_id, current_period, asset_recycle_date, asset_principal_value, asset_interest_value, charge_amount, loan_asset_principal," +
                "    loan_asset_interest, loan_charge_amount, actual_recycle_date, trade_time, journal_voucher_type,second_journal_voucher_type, financial_contract_uuid, create_date)" +
                "   VALUES (" +
                "   :uniqueId, :currentPeriod, :assetRecycleDate, :assetPrincipalValue, :assetInterestValue, :chargeAmount, 0," +
                "    0, 0, null, null, null, null, :financialContractUuid, :createDate" +
                "   )";

            allFirstVersions = allFirstVersions.select("unique_id","financial_contract_uuid","current_period","asset_recycle_date","asset_principal_value","asset_interest_value","charge_amount");

            if (allResult == null) {
                allResult = allFirstVersions;
            } else {
                allResult = allResult.union(allFirstVersions);
            }

//            for (Row row : allFirstVersions.collectAsList()) {
//                HashMap<String, Object> params = new HashMap<>();
//                params.put("uniqueId", row.getAs("unique_id"));
//                params.put("currentPeriod", row.getAs("current_period"));
//                params.put("assetRecycleDate", row.getAs("asset_recycle_date"));
//                params.put("assetPrincipalValue", row.getAs("asset_principal_value"));
//                params.put("assetInterestValue", row.getAs("asset_interest_value"));
//                params.put("chargeAmount", row.getAs("charge_amount"));
//                params.put("financialContractUuid", row.getAs("financial_contract_uuid"));
//                params.put("createDate", yesterday);
//
////                genericJdbcSupport.executeSQL(insertSql, params);
//
//                resultList.add(params);
//            }
        }

//        log.info("incremental repayment result List size:{}", resultList.size());

        // 导出到文件
        long saveDataStartTime = System.currentTimeMillis();

        log.info("Start save incremental repayment data file for [ :{}] time is :{}",yesterday, saveDataStartTime);

        boolean flag = false;

        try {
            saveData2Db(allResult, yesterday);
            String fullPath = generateFileFromDb(allResult.first().getAs("financial_contract_uuid"), time);
            ftpUtils.uploadFileToBaidu(new File(fullPath), time);
            flag = true;
        } catch (Exception e){
            e.printStackTrace();
            flag = false;
        }

        if (!flag) {
            try {
                // String fullPath = saveIncrementalRepaymentResult2File(resultList, time);
                String fullPath = saveIncrementalRepaymentResult2FileUsingDataSet(allResult, time);
                ftpUtils.uploadFileToBaidu(new File(fullPath), time);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        long saveDataEndTime = System.currentTimeMillis();
        log.info("End save incremental repayment file data end, time used :{}ms", saveDataEndTime - saveDataStartTime);

    }

    private void saveData2Db(Dataset<Row> results, String yesterday){
        results = results.withColumnRenamed("unique_id", "contract_unique_id")
            .withColumn("create_date", functions.lit(yesterday))
            .withColumn("loan_asset_principal", functions.lit("0"))
            .withColumn("loan_asset_interest", functions.lit("0"))
            .withColumn("loan_charge_amount", functions.lit("0"));
        log.info("starting save to db");
        saveData(results, "bd_incremental_repayment_file");
    }

    @Override
    public void doAssetFile(Dataset<Row> contractDataSet, Dataset<Row> assetDataSet, Collection<Object> assetUuids, Collection<Object> contractUuidList, Date time) {
        String yesterday = DateUtils.getDateFormatYYMMDD(time);
        String fileName = "QL-XJ-1_ASSET_" + yesterday.replace("-", "") + "_1.xlsx";
        if (contractDataSet == null || contractDataSet.count() <= 0 || assetDataSet == null || assetDataSet.count() <= 0) {
            String emptyFullPath = saveAssetData2File(new ArrayList<>(), fileName);
            ftpUtils.uploadFileToBaidu(new File(emptyFullPath),time);
            return;
        }
        //获取contract uniqueId customerUuid id assetUuid
        List<Object> uniqueIdList = new ArrayList<>();
        List<Object> customerUuidList = new ArrayList<>();
        List<Object> contractIdList = new ArrayList<>();
        for (Row row: contractDataSet.collectAsList()){
            uniqueIdList.add(row.getAs("unique_id"));
            customerUuidList.add(row.getAs("customer_uuid"));
            contractIdList.add(row.getAs("id"));
        }
        String uniqueIdLists = getUuidLists(uniqueIdList);
        String customerUuidLists = getUuidLists(customerUuidList);
        String contractIdLists = getUuidLists(contractIdList);
        String assetUuidLists = getUuidLists(assetUuids);
        String contractUuidLists = getUuidLists(contractUuidList);

        // 计划订单 t_remittance_application
        String raSql = "SELECT"
            + " id,"
            + " contract_unique_id,"
            + " last_modified_time AS loan_date"
            + " FROM t_remittance_application"
            + " WHERE"
            + " contract_unique_id IN ("+ uniqueIdLists +")";
        String[] raPredicates = initPredicates("t_remittance_application");

        Dataset<Row> raDataSet = loadDataFromTable(SQLUtils.wrapperSQL(raSql), raPredicates).drop("id");
        //客户信息 customer customer_person
        String customerSql = "SELECT"
            + " customer.id AS id,"
            + " customer.customer_uuid AS customer_uuid,"
            + " customer.name AS customer_name,"
            + " customer.account AS customer_account,"
            + " customer.mobile AS customer_mobile,"
            + " cp.marital_status AS customer_marriage"
            + " FROM customer customer"
            + " LEFT JOIN customer_person cp ON cp.customer_uuid = customer.customer_uuid"
            + " WHERE"
            + " customer.customer_uuid IN ("+ customerUuidLists +")";
        String[] customerPredicates = initPredicates("customer");

        Dataset<Row> customerDataSet = loadDataFromTable(SQLUtils.wrapperSQL(customerSql), customerPredicates).drop("id");
        //contract_account
        String caSql = "SELECT"
            + " id,"
            + " contract_id,"
            + " province AS province,"
            + " city AS city"
            + " FROM contract_account"
            + " WHERE contract_id IN ("+ contractIdLists +")"
            + " AND contract_account_type = 0"
            + " AND thru_date ='2900-01-01'";
        String[] caPredicates = initPredicates("contract_account");

        Dataset<Row> caDataSet = loadDataFromTable(SQLUtils.wrapperSQL(caSql), caPredicates).drop("id");

        // 第一步 取贷款合同信息
        // 合同号 客户名称 身份证号 手机号码 客户婚姻状态  客户所在省 客户所在市 贷款年利率（%） 还款方式 放款日期
        // 合同开始日 合同结束日
        Dataset<Row> allContractDataSet = contractDataSet.join(raDataSet, contractDataSet.col("unique_id").equalTo(raDataSet.col("contract_unique_id")));

        allContractDataSet = allContractDataSet.join(caDataSet, allContractDataSet.col("id").equalTo(caDataSet.col("contract_id")));

        allContractDataSet = allContractDataSet.join(customerDataSet, allContractDataSet.col("customer_uuid").equalTo(customerDataSet.col("customer_uuid")));

        log.info("allContractDataSet size is :{}", allContractDataSet.count());

        if (allContractDataSet.count() <= 0) {
            String emptyFullPath = saveAssetData2File(new ArrayList<>(), fileName);
            ftpUtils.uploadFileToBaidu(new File(emptyFullPath),time);
            return;
        }

        //应收费用信息 asset_set_extra_charge
        String assetcSql = "SELECT"
            + " id,"
            + " asset_set_uuid,"
            + " CASE WHEN second_account_name IN ('SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE','SND_UNEARNED_LOAN_ASSET_OTHER_FEE','SND_UNEARNED_LOAN_ASSET_TECH_FEE') THEN account_amount ELSE 0 END AS chargeAmount"
            + " FROM asset_set_extra_charge"
            + " WHERE"
            + " asset_set_uuid IN ("+ assetUuidLists +")";
        String[] assetcPredicates = initPredicates("asset_set_extra_charge");

        Dataset<Row> assetcDataSet = loadDataFromTable(SQLUtils.wrapperSQL(assetcSql), assetcPredicates).drop("id");

        // 合并计算出每个还款计划的借据应收费用 贷款服务费+技术维护费+其他费用
        assetcDataSet = assetcDataSet.groupBy("asset_set_uuid").agg(sum("chargeAmount").as("charge_amount"));

        // 第二步 还款计划信息
        //还款日
        //首期应还款日
        // 下一期应还款日
        //已还期数
        //剩余期数
        // 总期数
        //借据应收本金（元）
        //借据应收利息（元）
        //借据应收费用（元）
        Dataset<Row> allAssetecDataSet = assetDataSet.join(assetcDataSet, assetDataSet.col("asset_uuid").equalTo(assetcDataSet.col("asset_set_uuid")), "left_outer").drop("asset_set_uuid");

        log.info("allAssetecDataSet size is :{}", allAssetecDataSet.count());

        Dataset<Row> allAssetecDs = allAssetecDataSet.select("asset_uuid", "contract_uuid", "version_no", "active_status","asset_create_time", "current_period", "asset_status",
                "asset_principal_value", "asset_interest_value", "asset_recycle_date","charge_amount");

        // 第三步 应收未收
        String lbsSql = "select"
            + " id,"
            + " contract_uuid,"
            + " CASE when third_account_name in ('TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE') then debit_balance - credit_balance"
            + " when second_account_name ='SND_UNEARNED_LOAN_ASSET_PRINCIPLE' then debit_balance - credit_balance ELSE 0 END as remaining_principal,"
            + " CASE when third_account_name in ('TRD_RECIEVABLE_LOAN_ASSET_INTEREST','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST') then debit_balance - credit_balance"
            + " when second_account_name = 'SND_UNEARNED_LOAN_ASSET_INTEREST' THEN debit_balance - credit_balance ELSE 0 END as remaining_interest,"
            + " CASE when third_account_name in ('TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_LOAN_SERVICE_FEE','TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_TECH_FEE','TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_OTHER_FEE', 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' ,'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' ) then debit_balance - credit_balance"
            + " when second_account_name in( 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE','SND_UNEARNED_LOAN_ASSET_OTHER_FEE', 'SND_RECIEVABLE_LOAN_PENALTY')THEN debit_balance - credit_balance ELSE 0 END as remaining_fee"
            + " FROM ledger_book_shelf WHERE contract_uuid in(" + contractUuidLists + ")";

        String[] lbsPredicates = initPredicates("ledger_book_shelf");

        Dataset<Row> allLbsDataSet = loadDataFromTable(SQLUtils.wrapperSQL(lbsSql), lbsPredicates).drop("id");

        log.info("allLbsDataSet size :{}", allLbsDataSet.count());

        // 合并计算应收未收
        // 本金 利息 7项之和
        Dataset<Row> allLbsDs = allLbsDataSet.groupBy("contract_uuid").agg(
            sum("remaining_principal").as("remaining_principal"),
            sum("remaining_interest").as("remaining_interest"),
            sum("remaining_fee").as("remaining_fee")
        );

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Object curContractUuid: contractUuidList) {
            Dataset<Row> curContractData = allContractDataSet.filter(allContractDataSet.col("uuid").equalTo(curContractUuid));
            if (curContractData.count() <= 0) {
                continue;
            }
            Dataset<Row> curAssetecData = allAssetecDs.filter(allAssetecDs.col("contract_uuid").equalTo(curContractUuid));
            Row curLbsData = allLbsDs.filter(allLbsDs.col("contract_uuid").equalTo(curContractUuid)).first();

            // 第一版本版本号
            Object firstVersion  = curAssetecData.sort("asset_create_time").first().getAs("version_no");
            Dataset<Row> allFirstVersions = curAssetecData.filter(curAssetecData.col("version_no").equalTo(firstVersion));

            //总期数
            //第一版还款计划本金总额
            //第一版还款计划利息总额
            //第一版还款计划费用
            Row firstVersionsData = allFirstVersions.groupBy("contract_uuid").agg(
                countDistinct("asset_uuid").as("first_version_periods"),
                sum("asset_principal_value").as("first_version_asset_principal_value"),
                sum("asset_interest_value").as("first_version_asset_interest_value"),
                sum("charge_amount").as("first_version_charge_amount")
            ).first();

            //首期应还款日
            Object firstVersionCreatePaymentDate = allFirstVersions.sort("current_period").first().getAs("asset_recycle_date");
            // 有效的还款计划
            Dataset<Row> validAssetSets = curAssetecData.filter(curAssetecData.col("active_status").equalTo(0));
            // 未结清的还款计划
            Dataset<Row> unclearAssetSets = validAssetSets.filter(validAssetSets.col("asset_status").equalTo(0));

            // 未结清的第一期还款计划的应还日
            Object lastAssetRecycleDate = null;
            // 未结清的期数
            Long unclearPeriod = unclearAssetSets.count();
            // 有未结清
            if (unclearAssetSets.count() != 0){
                // 未结清的第一期还款计划的应还日
                lastAssetRecycleDate = unclearAssetSets.sort("current_period").first().getAs("asset_recycle_date");
            }
            // 已结清的期数
            long clearPeriod = validAssetSets.filter(validAssetSets.col("asset_status").equalTo(1)).count();

            //insert into
            String  insertSql = "INSERT INTO" +
                "   bd_asset_file" +
                "   (contract_unique_id, customer_name, customer_account, customer_mobile, customer_marriage, province, city," +
                "   iou_status, total_periods, principal_value, interest_value, charge_amount, interest_rate, repayment_way, formality_rate, formality, formality_type, loan_date," +
                "   begin_date, end_date, payment_day, first_version_payment_date, last_principal_value, last_interest_value, last_charge_amount, next_asset_recycle_date," +
                "   clear_period, unclear_period, total_overdue, current_overdue, financial_contract_uuid, create_date)" +
                "   VALUES (" +
                "   :contractUniqueId, :customerName, :customerAccount, :customerMobile, :customerMarriage, :province, :city, null, :totalPeriods," +
                "   :principalValue, :interestValue, :chargeAmount, :interestRate, :repaymentWay, null, null, null, :loanDate, :beginDate, :endDate, :paymentDay," +
                "   :firstVersionPaymentDate, :lastPrincipalValue, :lastInterestValue, :lastChargeAmount, :nextAssetRecycleDate,:clearPeriod, :unclearPeriod, null, null, :financialContractUuid, :createDate" +
                "   )" ;

            Row firstRow = curContractData.first();
            Map<String, Object> params = new HashMap<>();
            params.put("contractUniqueId",firstRow.getAs("unique_id"));
            params.put("customerName",firstRow.getAs("customer_name"));
            params.put("customerAccount",firstRow.getAs("customer_account"));
            params.put("customerMobile",firstRow.getAs("customer_mobile"));
            params.put("customerMarriage",firstRow.getAs("customer_marriage"));
            params.put("province",firstRow.getAs("province"));
            params.put("city",firstRow.getAs("city"));
            params.put("totalPeriods",firstVersionsData.getAs("first_version_periods"));
            params.put("principalValue",firstVersionsData.getAs("first_version_asset_principal_value"));
            params.put("interestValue",firstVersionsData.getAs("first_version_asset_interest_value"));
            params.put("chargeAmount",
                firstVersionsData.getAs("first_version_charge_amount") == null ? new BigDecimal("0.00")
                    : firstVersionsData.getAs("first_version_charge_amount"));
            params.put("interestRate",firstRow.getAs("interest_rate"));
            params.put("repaymentWay",firstRow.getAs("repayment_way"));
            params.put("loanDate",firstRow.getAs("loan_date"));
            params.put("beginDate",firstRow.getAs("begin_date"));
            params.put("endDate",firstRow.getAs("end_date"));
            params.put("paymentDay", lastAssetRecycleDate);
            params.put("firstVersionPaymentDate",firstVersionCreatePaymentDate);
            params.put("lastPrincipalValue",curLbsData.getAs("remaining_principal"));
            params.put("lastInterestValue",curLbsData.getAs("remaining_interest"));
            params.put("lastChargeAmount",curLbsData.getAs("remaining_fee"));
            params.put("nextAssetRecycleDate",lastAssetRecycleDate);
            params.put("clearPeriod",clearPeriod);
            params.put("unclearPeriod",unclearPeriod);
            params.put("financialContractUuid", firstRow.getAs("financial_contract_uuid"));
            params.put("createDate", yesterday);

//            genericJdbcSupport.executeSQL(insertSql, params);

            resultList.add(params);
        }

        log.info("asset data result List size is", resultList.size());

        //第四步 导出数据文件
        long saveTime = System.currentTimeMillis();

        log.info("Start save asset data file for [ :{}] time is :{}",yesterday, saveTime);


        String fullPath = saveAssetData2File(resultList, fileName);

        ftpUtils.uploadFileToBaidu(new File(fullPath),time);

        long saveDataEndTime = System.currentTimeMillis();

        log.info("End save asset data File Used :{}", saveDataEndTime - saveTime);
    }

    @Override
    public void doIncrementalAssetFile(Dataset<Row> contractDataSet, Dataset<Row> assetDataSet, Collection<Object> assetUuids, Collection<Object> contractUuidList,Date time) {
        String yesterday = DateUtils.getDateFormatYYMMDD(time);
        String fileName = "QL-XJ-1_NEWASSET_" + yesterday.replace("-","") + "_1.xlsx";
        if (contractDataSet == null || contractDataSet.count() <= 0 || assetDataSet == null || assetDataSet.count() <= 0) {
            String emptyFullPath = saveAssetData2File(new ArrayList<>(), fileName);
            ftpUtils.uploadFileToBaidu(new File(emptyFullPath),time);
            return;
        }
        //获取contract uniqueId customerUuid id assetUuid
        List<Object> uniqueIdList = new ArrayList<>();
        List<Object> customerUuidList = new ArrayList<>();
        List<Object> contractIdList = new ArrayList<>();
        for (Row row: contractDataSet.collectAsList()){
            uniqueIdList.add(row.getAs("unique_id"));
            customerUuidList.add(row.getAs("customer_uuid"));
            contractIdList.add(row.getAs("id"));
        }
        String uniqueIdLists = getUuidLists(uniqueIdList);
        String customerUuidLists = getUuidLists(customerUuidList);
        String contractIdLists = getUuidLists(contractIdList);
        String assetUuidLists = getUuidLists(assetUuids);

        // 计划订单 t_remittance_application
        String raSql = "SELECT"
            + " id,"
            + " contract_unique_id,"
            + " last_modified_time AS loan_date"
            + " FROM t_remittance_application"
            + " WHERE"
            + " contract_unique_id IN ("+ uniqueIdLists +")";
        String[] raPredicates = initPredicates("t_remittance_application");

        Dataset<Row> raDataSet = loadDataFromTable(SQLUtils.wrapperSQL(raSql), raPredicates).drop("id");
        //客户信息 customer customer_person
        String customerSql = "SELECT"
            + " customer.id AS id,"
            + " customer.customer_uuid AS customer_uuid,"
            + " customer.name AS customer_name,"
            + " customer.account AS customer_account,"
            + " customer.mobile AS customer_mobile,"
            + " cp.marital_status AS customer_marriage"
            + " FROM customer customer"
            + " LEFT JOIN customer_person cp ON cp.customer_uuid = customer.customer_uuid"
            + " WHERE"
            + " customer.customer_uuid IN ("+ customerUuidLists +")";
        String[] customerPredicates = initPredicates("customer");

        Dataset<Row> customerDataSet = loadDataFromTable(SQLUtils.wrapperSQL(customerSql), customerPredicates).drop("id");
        //contract_account
        String caSql = "SELECT"
            + " id,"
            + " contract_id,"
            + " province AS province,"
            + " city AS city"
            + " FROM contract_account"
            + " WHERE contract_id IN ("+ contractIdLists +")"
            + " AND contract_account_type = 0"
            + " AND thru_date ='2900-01-01'";
        String[] caPredicates = initPredicates("contract_account");

        Dataset<Row> caDataSet = loadDataFromTable(SQLUtils.wrapperSQL(caSql), caPredicates).drop("id");

        // 第一步 取贷款合同信息
        // 合同号 客户名称 身份证号 手机号码 客户婚姻状态  客户所在省 客户所在市 贷款年利率（%） 还款方式 放款日期
        // 合同开始日 合同结束日
        Dataset<Row> allContractDataSet = contractDataSet.join(raDataSet, contractDataSet.col("unique_id").equalTo(raDataSet.col("contract_unique_id")));

        allContractDataSet = allContractDataSet.join(caDataSet, allContractDataSet.col("id").equalTo(caDataSet.col("contract_id")));

        allContractDataSet = allContractDataSet.join(customerDataSet, allContractDataSet.col("customer_uuid").equalTo(customerDataSet.col("customer_uuid")));

//        log.info("allContractDataSet size is :{}", allContractDataSet.count());

        if (allContractDataSet.count() <= 0) {
            String emptyFullPath = saveAssetData2File(new ArrayList<>(), fileName);
            ftpUtils.uploadFileToBaidu(new File(emptyFullPath),time);
            return;
        }

        //应收费用信息 asset_set_extra_charge
        String assetcSql = "SELECT"
            + " id,"
            + " asset_set_uuid,"
            + " CASE WHEN second_account_name IN ('SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE','SND_UNEARNED_LOAN_ASSET_OTHER_FEE','SND_UNEARNED_LOAN_ASSET_TECH_FEE') THEN account_amount ELSE 0 END AS charge_amount"
            + " FROM asset_set_extra_charge"
            + " WHERE"
            + " asset_set_uuid IN ("+ assetUuidLists +")";
        String[] assetcPredicates = initPredicates("asset_set_extra_charge");

        Dataset<Row> assetcDataSet = loadDataFromTable(SQLUtils.wrapperSQL(assetcSql), assetcPredicates).drop("id");

        // 第二步 还款计划信息
        //还款日
        //首期应还款日
        // 下一期应还款日
        //已还期数
        //剩余期数
        // 总期数
        //借据应收本金（元）
        //借据应收利息（元）
        //借据应收费用（元）
        Dataset<Row> allAssetecDataSet = assetDataSet.join(assetcDataSet, assetDataSet.col("asset_uuid").equalTo(assetcDataSet.col("asset_set_uuid")), "left_outer").drop("asset_set_uuid");

//        log.info("allAssetecDataSet size is :{}", allAssetecDataSet.count());

        // 合并计算出每个还款计划的借据应收费用 贷款服务费+技术维护费+其他费用
        Dataset<Row> allAssetecDs = allAssetecDataSet
            .groupBy("asset_uuid", "contract_uuid", "version_no", "active_status","asset_create_time", "current_period", "asset_status",
                "asset_principal_value", "asset_interest_value", "asset_recycle_date").agg(
                sum("charge_amount").as("charge_amount")
            );
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Object contractUuid: contractUuidList) {
            Dataset<Row> curContractData = allContractDataSet.filter(allContractDataSet.col("uuid").equalTo(contractUuid));
            if (curContractData.count() <= 0) {
                continue;
            }
            Dataset<Row> curAssetecData = allAssetecDs.filter(allAssetecDs.col("contract_uuid").equalTo(contractUuid));

            //第一版本版本号
            Object firstVersionNo = curAssetecData.sort("asset_create_time").first().getAs("version_no");
            Dataset<Row> allFirstVersions = curAssetecData.filter(curAssetecData.col("version_no").equalTo(firstVersionNo));

            Row firstVersionsData = allFirstVersions.groupBy("contract_uuid").agg(
                countDistinct("asset_uuid").as("firstVersionPeriods"),//总期数
                sum("asset_principal_value").as("firstVersionAssetPrincipalValue"),//第一版还款计划本金总额
                sum("asset_interest_value").as("firstVersionAssetInterestValue"),//第一版还款计划利息总额
                sum("charge_amount").as("firstVersionChargeAmount")//第一版还款计划费用
            ).first();

            //首期应还款日
            Object firstVersionCreatePaymentDate = allFirstVersions.sort("current_period").first().getAs("asset_recycle_date");

            //insert into
            String  insertSql = "INSERT INTO" +
                "   bd_incremental_asset_file" +
                "   (contract_unique_id, customer_name, customer_account, customer_mobile, customer_marriage, province, city, iou_status, total_periods," +
                "   principal_value, interest_value, charge_amount, interest_rate, repayment_way, formality_rate, formality, formality_type, loan_date, begin_date, end_date, payment_day, " +
                "   first_version_payment_date, last_principal_value, last_interest_value, last_charge_amount, next_asset_recycle_date," +
                "   clear_period, unclear_period, total_overdue, current_overdue, financial_contract_uuid, create_date)" +
                "   VALUES (" +
                "   :contractUniqueId, :customerName, :customerAccount, :customerMobile, :customerMarriage, :province, :city, null, :totalPeriods," +
                "   :principalValue, :interestValue, :chargeAmount, :interestRate, :repaymentWay, null, null, null, :loanDate, :beginDate, :endDate, :paymentDay," +
                "   :firstVersionPaymentDate, :lastPrincipalValue, :lastInterestValue, :lastChargeAmount, :nextAssetRecycleDate,:clearPeriod, :unclearPeriod, null, null, :financialContractUuid, :createDate" +
                "   )";
            Row firstRow = curContractData.first();
            Map<String, Object> params = new HashMap<>();
            params.put("contractUniqueId",firstRow.getAs("unique_id"));
            params.put("customerName",firstRow.getAs("customer_name"));
            params.put("customerAccount",firstRow.getAs("customer_account"));
            params.put("customerMobile",firstRow.getAs("customer_mobile"));
            params.put("customerMarriage",firstRow.getAs("customer_marriage"));
            params.put("province",firstRow.getAs("province"));
            params.put("city",firstRow.getAs("city"));
            params.put("totalPeriods",firstVersionsData.getAs("firstVersionPeriods"));
            params.put("principalValue",firstVersionsData.getAs("firstVersionAssetPrincipalValue"));
            params.put("interestValue",firstVersionsData.getAs("firstVersionAssetInterestValue"));
            params.put("chargeAmount",firstVersionsData.getAs("firstVersionChargeAmount"));
            params.put("interestRate",firstRow.getAs("interest_rate"));
            params.put("repaymentWay",firstRow.getAs("repayment_way"));
            params.put("loanDate",firstRow.getAs("loan_date"));
            params.put("beginDate",firstRow.getAs("begin_date"));
            params.put("endDate",firstRow.getAs("end_date"));
            params.put("paymentDay", firstVersionCreatePaymentDate);
            params.put("firstVersionPaymentDate",firstVersionCreatePaymentDate);
            params.put("lastPrincipalValue",firstVersionsData.getAs("firstVersionAssetPrincipalValue"));
            params.put("lastInterestValue",firstVersionsData.getAs("firstVersionAssetInterestValue"));
            params.put("lastChargeAmount",firstVersionsData.getAs("firstVersionChargeAmount"));
            params.put("nextAssetRecycleDate",firstVersionCreatePaymentDate);
            params.put("clearPeriod",0);
            params.put("unclearPeriod",firstVersionsData.getAs("firstVersionPeriods"));
            params.put("financialContractUuid", firstRow.getAs("financial_contract_uuid"));
            params.put("createDate", yesterday);

//            genericJdbcSupport.executeSQL(insertSql, params);

            resultList.add(params);
        }
        log.info("incremental asset data result List size is", resultList.size());

        //第四步 导出到文件
        long saveFileTime = System.currentTimeMillis();

        String fullPath = saveAssetData2File(resultList, fileName);

        ftpUtils.uploadFileToBaidu(new File(fullPath),time);

        long saveFileEndTime = System.currentTimeMillis();

        log.info("Save File Used :{}", saveFileEndTime - saveFileTime);
        log.info("End save incremental asset data file for :{} time is :{}", DateUtils.getDateFormatYYMMDD(time), saveFileEndTime);

    }

    /**
     * 拼接uuid
     * @param uuids
     * @return
     */
    private String getUuidLists(Collection<Object> uuids) {

        if (CollectionUtils.isEmpty(uuids)){
            return null;
        }

        List<String> uuidList = new ArrayList<String>();
        for (Object uuid: uuids) {
            if (uuid == null){
                continue;
            }
            String tmp;
            if(uuid instanceof Long || uuid instanceof Integer){
                tmp = String.valueOf(uuid);
            } else {
                tmp = "'" + String.valueOf(uuid) + "'";
            }
            uuidList.add(tmp);
        }
        return StringUtils.join(uuidList, ',');
    }

    /**
     * 导出资产文件(新增资产文件)
     * @param dataList
     * @param fileName
     * @return
     */
    private String saveAssetData2File(List<Map<String, Object>> dataList, String fileName) {
        String path = pathConfig.getBaiduQianlongReportTask();
        String fullPath = path + fileName;
        File resultFile = new File(fullPath);
        FileOutputStream outResult = null;
        ExcelUtil<AssetExcel> excelTool = new ExcelUtil<>(AssetExcel.class);
        try {
            List<AssetExcel> excels = new ArrayList<>();
            outResult = new FileOutputStream(resultFile);
            if (dataList == null || dataList.size() <= 0) {
                excels.add(new AssetExcel());
            } else {
                for (Map<String, Object> data : dataList) {
                    excels.add(getAssetExcelFrom(data));
                }
            }
            excelTool.exportExcelXlsx(excels, "Sheet1", outResult);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(outResult);
        }
        return fullPath;
    }

    private AssetExcel getAssetExcelFrom(Map<String, Object> data) {
        AssetExcel assetExcel = new AssetExcel();
        assetExcel.setContractUniqueId(fetchFieldValue(data, "contractUniqueId"));
        assetExcel.setCustomerName(fetchFieldValue(data, "customerName"));
        assetExcel.setCustomerAccount(fetchFieldValue(data, "customerAccount"));
        assetExcel.setCustomerMobile(fetchFieldValue(data, "customerMobile"));
        assetExcel.setCustomerAge(getAgeFrom(fetchFieldValue(data, "customerAccount")));
        assetExcel.setCustomerSex(getSexFrom(fetchFieldValue(data, "customerAccount")));
        assetExcel.setCustomerMarriage(customerMarriageMap.get(fetchFieldValue(data, "customerMarriage")));
        assetExcel.setProvince(fetchFieldValue(data, "province"));
        assetExcel.setCity(fetchFieldValue(data, "city"));
        assetExcel.setIouStatus(null);
        assetExcel.setTotalPeriods(fetchFieldValue(data, "totalPeriods"));
        assetExcel.setPrincipalValue(fetchFieldValue(data, "principalValue"));
        assetExcel.setInterestValue(fetchFieldValue(data, "interestValue"));
        String chargeAmount = fetchFieldValue(data, "chargeAmount");
        assetExcel.setChargeAmount(chargeAmount == null ? "0.00" : chargeAmount);
        String interestRate = fetchFieldValue(data, "interestRate");
        assetExcel.setInterestRate(interestRate == null ? null : String.valueOf(new BigDecimal(interestRate).multiply(new BigDecimal(100)).stripTrailingZeros()));
        String repaymentWay = fetchFieldValue(data, "repaymentWay");
        assetExcel.setRepaymentWay(repaymentWay == null ? null : repaymentWayMap.get(repaymentWay));
        assetExcel.setFormalityRate(null);
        assetExcel.setFormality(null);
        assetExcel.setFormalityType(null);
        assetExcel.setLoanDate(DateUtils.transferYYYYMMDDHHmmss2YYYYMMDD(fetchFieldValue(data, "loanDate")));
        assetExcel.setBeginDate(fetchFieldValue(data, "beginDate"));
        assetExcel.setEndDate(fetchFieldValue(data, "endDate"));
        String paymentDay = fetchFieldValue(data, "paymentDay");
        assetExcel.setPaymentDay(paymentDay == null ? null : DateUtils.getDayFromDate(paymentDay) + "");
        assetExcel.setFirstVersionPaymentDate(fetchFieldValue(data, "firstVersionPaymentDate"));
        assetExcel.setLastPrincipalValue(fetchFieldValue(data, "lastPrincipalValue"));
        assetExcel.setLastInterestValue(fetchFieldValue(data, "lastInterestValue"));
        String lastChargeAmount = fetchFieldValue(data, "lastChargeAmount");
        assetExcel.setLastChargeAmount(lastChargeAmount == null ? "0.00" : lastChargeAmount);
        assetExcel.setNextAssetRecycleDate(fetchFieldValue(data, "nextAssetRecycleDate"));
        assetExcel.setClearPeriod(fetchFieldValue(data, "clearPeriod"));
        assetExcel.setUnclearPeriod(fetchFieldValue(data, "unclearPeriod"));
        assetExcel.setTotalOverdue(null);
        assetExcel.setCurrentOverdue(null);
        return assetExcel;
    }

    private String fetchFieldValue(Map<String, Object> row, String fieldName) {
        Object x;
        try {
            x = row.get(fieldName);
        } catch (Exception ignored) {
            x = null;
        }
        return x == null ? null : x.toString();
    }

    private Map<String, String> repaymentWayMap = new HashMap<String, String>() {
        {
            put("0", "等额本息");
            put("1", "等额本金");
            put("2", "自定义");
            put("3", "随借随还");
        }
    };

    private Map<String, String> customerMarriageMap = new HashMap<String, String>() {
        {
            put("0", "未婚");
            put("1", "已婚有子女");
            put("2", "已婚无子女");
            put("3", "离异");
            put("4", "复婚");
            put("5", "未说明");
            put("6", "已婚");
            put("7", "初婚");
            put("8", "再婚");
            put("9", "丧偶");
        }
    };

    /**
     * 获取性别
     * @param customerAccount
     * @return
     */
    private String getSexFrom(String customerAccount) {
        if (customerAccount == null || customerAccount.length() != 18) {
            return "";
        }

        String id17 = customerAccount.substring(16, 17);

        if (Integer.parseInt(id17) % 2 != 0) {
            return "男";
        } else {
            return "女";
        }
    }

    /**
     * 获取年龄
     * @param customerAccount
     * @return
     */
    private String getAgeFrom(String customerAccount) {
        if (customerAccount == null || customerAccount.length() < 15) {
            return "";
        }
        String birthday = customerAccount.substring(6, 14);
        try {
            Date birthDate = new SimpleDateFormat("yyyyMMdd")
                .parse(birthday);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birthDate);
            int birthYear = calendar.get(Calendar.YEAR);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(new Date());
            int nowYear = calendar1.get(Calendar.YEAR);

            return nowYear - birthYear + "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
