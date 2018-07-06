package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.ABS_RepaymentAndContractDAO;
import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.apache.spark.sql.functions.sum;

/**
 * Created by zxj on 2018/2/7.
 */
@Repository
@Slf4j
public class ABS_RepaymentAndContractDAOImpl extends BaseDAO implements ABS_RepaymentAndContractDAO {

    @Override
    public Dataset<Row> getTencentAbsTrailData(Date start, Date end, int fileType) {
        String sql = "SELECT *"
            + " FROM t_tencent_abs_trail"
            + " WHERE file_type = " + fileType
            + " AND '" + DateUtils.getDateFormatYYMMDDHHMMSS(start) + "'"
            + " <= Date(last_modified_time)"
            + " AND Date(last_modified_time) < "
            + " '" + DateUtils.getDateFormatYYMMDDHHMMSS(end) + "'";
        log.info(sql);
        String[] predicates = initPredicates("t_tencent_abs_trail");
        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("tencent abs trail data size is :{}", dataSet.count());
            dataSet = dataSet.distinct();
        }
        return dataSet;
    }

    @Override
    public Dataset<Row> getFinancialContractDataset(Collection<Object> financialUuids) {
        if (financialUuids.size() <= 0) {
            return null;
        }
        String uuids = getUuidLists(financialUuids);
        if (StringUtils.isEmpty(uuids)) {
            return null;
        }
        String sql = "SELECT *"
            + " FROM financial_contract"
            + " WHERE"
            + " financial_contract_uuid IN ("+uuids+")";
        String[] predicates = initPredicates("financial_contract");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("financial contract dataset size is :{}", dataSet.count());
            dataSet = dataSet.drop("id");
        }
        return dataSet;
    }
    @Override
    public Dataset<Row> getAssetDatasetByAssetUuid(Collection<Object> assetUuids) {
        if (assetUuids.size() <= 0) {
            return null;
        }
        String uuids = getUuidLists(assetUuids);
        if (StringUtils.isEmpty(uuids)) {
            return null;
        }
        String sql = "SELECT"
            + " id,"
            + " contract_uuid,"
            + " current_period,"
            + " asset_uuid,"
            + " asset_recycle_date,"
            + " actual_recycle_date,"
            + " asset_principal_value,"
            + " create_time AS asset_create_time,"
            + " plan_type,"
            + " active_status,"
            + " asset_status,"
            + " on_account_status,"
            + " can_be_rollbacked,"
            + " asset_interest_value"
            + " FROM asset_set"
            + " WHERE"
            + " asset_uuid IN ("+ uuids +")";

        String[] predicates = initPredicates("asset_set");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("asset dataset size is :{}", dataSet.count());
            dataSet = dataSet.drop("id").distinct();
        }
        return dataSet;
    }

    @Override
    public Dataset<Row> getAssetDatasetByContractUuid(Collection<Object> contractUuids) {
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
            + " asset_uuid,"
            + " overdue_date,"
            + " version_no,"
            + " asset_recycle_date,"
            + " actual_recycle_date,"
            + " asset_principal_value,"
            + " create_time AS asset_create_time,"
            + " plan_type,"
            + " active_status,"
            + " overdue_status,"
            + " asset_status,"
            + " asset_interest_value"
            + " FROM asset_set"
            + " WHERE"
            + " contract_uuid IN ("+ uuids +")";


        String[] predicates = initPredicates("asset_set");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("asset dataset size is :{}", dataSet.count());
            dataSet = dataSet.drop("id").distinct();
        }
        // 作废的还款计划位于 asset_set_history
        sql = sql.replace("FROM asset_set ","FROM asset_set_history ");

        predicates = initPredicates("asset_set_history");

        Dataset<Row> historyDataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (historyDataSet != null && historyDataSet.count() > 0) {
            log.info("history dataSet size is :{}", historyDataSet.count());
            historyDataSet = historyDataSet.drop("id").distinct();
        }

        if (dataSet != null && historyDataSet != null && historyDataSet.count() > 0) {
            return dataSet.union(historyDataSet);
        }

        if (dataSet == null) {
            return historyDataSet;
        }

        return dataSet;
    }

    @Override
    public Dataset<Row> getContractDataset(Collection<Object> contractUuids) {
        if (contractUuids.size() <= 0) {
            return null;
        }
        String contractUuidStr = getUuidLists(contractUuids);
        if (StringUtils.isEmpty(contractUuidStr)) {
            return null;
        }
        String sql = "SELECT * FROM contract WHERE uuid IN (" + contractUuidStr + ")";
        String[] predicates = initPredicates("contract");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("contract dataset size is :{}", dataSet.count());
            dataSet = dataSet.distinct().drop("id");
        }

        return dataSet;
    }

    @Override
    public Dataset<Row> getAssetSetExtraCharge(Collection<Object> assetUuids) {
        if (assetUuids.size() <= 0) {
            return null;
        }
        String assetUuidStr = getUuidLists(assetUuids);
        if (StringUtils.isEmpty(assetUuidStr)) {
            return null;
        }
        String sql = "SELECT"
            + " id,"
            + " asset_set_uuid,"
            + " CASE WHEN second_account_name IN ('SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE','SND_UNEARNED_LOAN_ASSET_OTHER_FEE','SND_UNEARNED_LOAN_ASSET_TECH_FEE','SND_RECIEVABLE_LOAN_PENALTY','TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION','TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE','TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE') THEN account_amount ELSE 0 END AS charge_amount"
            + " FROM asset_set_extra_charge"
            + " WHERE"
            + " asset_set_uuid IN ("+ assetUuidStr +")";

        String[] predicates = initPredicates("asset_set_extra_charge");

        Dataset<Row> assetcDataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (assetcDataSet != null && assetcDataSet.count() > 0) {
            log.info("assetc dataset size is :{}", assetcDataSet.count());
            assetcDataSet = assetcDataSet.distinct().drop("id");
            //应收 费用 （七项之和）
            assetcDataSet = assetcDataSet.groupBy("asset_set_uuid").agg(
                sum("charge_amount").as("charge_amount")
            );
        }

        return assetcDataSet;
    }

    @Override
    public Dataset<Row> getPaidOffDs(Collection<Object> assetUuids) {
        if (assetUuids.size() <= 0) {
            return null;
        }
        String assetUuidStr = getUuidLists(assetUuids);
        if (StringUtils.isEmpty(assetUuidStr)) {
            return null;
        }
        String lbsSql = "SELECT"
            + " id,"
            + " related_lv_1_asset_uuid,"
            + " CASE WHEN third_account_name IN ('TRD_BANK_SAVING_GENERAL_PRINCIPAL', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSEL_PRINCIPAL') THEN debit_balance - credit_balance ELSE 0 END AS loanAssetPrincipal,"
            + " CASE WHEN third_account_name IN ('TRD_BANK_SAVING_GENERAL_INTEREST', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST') THEN debit_balance - credit_balance ELSE 0 END AS loanAssetInterest,"
            + " CASE WHEN third_account_name IN ('TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE', 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE') THEN debit_balance - credit_balance ELSE 0 END AS loanAssetAmount"
            + " FROM ledger_book_shelf"
            + " where related_lv_1_asset_uuid in (" + assetUuidStr + ")";

        String[] lbsPredicates = initPredicates("ledger_book_shelf");

        Dataset<Row> paidOffDs = loadDataFromTable(SQLUtils.wrapperSQL(lbsSql), lbsPredicates);

        if (paidOffDs != null && paidOffDs.count() > 0) {
            paidOffDs.drop("id");
            // 合并计算实收
            // 本金 利息 费用（7项之和）
            paidOffDs = paidOffDs.groupBy("related_lv_1_asset_uuid").agg(
                sum("loanAssetPrincipal").as("loan_asset_principal"),
                sum("loanAssetInterest").as("loan_asset_interest"),
                sum("loanAssetAmount").as("loan_charge_amount")
            );
        }
        return paidOffDs;
    }

    @Override
    public Dataset<Row> getCustomerDataset(Collection<Object> customerUuids) {
        if (customerUuids.size() <= 0) {
            return null;
        }
        String uuidStr = getUuidLists(customerUuids);
        if (StringUtils.isEmpty(uuidStr)) {
            return null;
        }
        //客户信息 customer customer_person
        String customerSql = "SELECT"
            + " customer.id AS id,"
            + " customer.customer_uuid AS customer_uuid,"
            + " customer.name AS customer_name,"
            + " customer.account AS customer_account,"
            + " customer.mobile AS customer_mobile"
            + " FROM customer customer"
            + " WHERE"
            + " customer.customer_uuid IN ("+ uuidStr +")";
        String[] customerPredicates = initPredicates("customer");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(customerSql), customerPredicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("customer dataset size is :{}", dataSet.count());
            dataSet = dataSet.distinct().drop("id");
        }
        return dataSet;
    }

    @Override
    public Dataset<Row> getRemainingAmountDataset(Collection<Object> contractUuids) {
        if (contractUuids.size() <= 0) {
            return null;
        }
        String uuidStr = getUuidLists(contractUuids);
        if (StringUtils.isEmpty(uuidStr)) {
            return null;
        }
        String lbsSql = "select"
            + " id,"
            + " contract_uuid,"
            + " CASE when third_account_name in ('TRD_RECIEVABLE_LOAN_ASSET_PRINCIPLE','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_PRINCIPLE') then debit_balance - credit_balance"
            + " when second_account_name ='SND_UNEARNED_LOAN_ASSET_PRINCIPLE' then debit_balance - credit_balance ELSE 0 END as remaining_principal,"
            + " CASE when third_account_name in ('TRD_RECIEVABLE_LOAN_ASSET_INTEREST','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_INTEREST') then debit_balance - credit_balance"
            + " when second_account_name = 'SND_UNEARNED_LOAN_ASSET_INTEREST' THEN debit_balance - credit_balance ELSE 0 END as remaining_interest,"
            + " CASE when third_account_name in ('TRD_RECIEVABLE_LOAN_ASSET_LOAN_SERVICE_FEE','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_LOAN_SERVICE_FEE','TRD_RECIEVABLE_LOAN_ASSET_TECH_FEE','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_TECH_FEE','TRD_RECIEVABLE_LOAN_ASSET_OTHER_FEE','TRD_RECIEVABLE_OVERDUE_LOAN_ASSET_OTHER_FEE', 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' ,'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE', 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' ) then debit_balance - credit_balance"
            + " when second_account_name in( 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE', 'SND_UNEARNED_LOAN_ASSET_TECH_FEE','SND_UNEARNED_LOAN_ASSET_OTHER_FEE', 'SND_RECIEVABLE_LOAN_PENALTY')THEN debit_balance - credit_balance ELSE 0 END as remaining_fee"
            + " FROM ledger_book_shelf WHERE contract_uuid in(" + uuidStr + ")";

        String[] lbsPredicates = initPredicates("ledger_book_shelf");

        Dataset<Row> allLbsDataSet = loadDataFromTable(SQLUtils.wrapperSQL(lbsSql), lbsPredicates);

        if (allLbsDataSet != null && allLbsDataSet.count() > 0) {
            log.info("allLbsDataSet size is :{}", allLbsDataSet.count());
            allLbsDataSet = allLbsDataSet.distinct().drop("id");

            // 合并计算应收未收
            // 本金 利息 7项之和
            allLbsDataSet = allLbsDataSet.groupBy("contract_uuid").agg(
                sum("remaining_principal").as("remaining_principal"),
                sum("remaining_interest").as("remaining_interest"),
                sum("remaining_fee").as("remaining_fee")
            );
        }
        return allLbsDataSet;
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
}
