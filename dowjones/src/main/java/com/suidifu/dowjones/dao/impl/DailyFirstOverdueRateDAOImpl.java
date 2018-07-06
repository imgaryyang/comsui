package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.dao.DailyFirstOverdueRateDAO;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math.stat.descriptive.summary.Sum;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.apache.spark.sql.functions.sum;

/**
 * Created by zxj on 2018/3/15.
 */
@Repository
@Slf4j
public class DailyFirstOverdueRateDAOImpl extends BaseDAO implements DailyFirstOverdueRateDAO {


    @Override
    public Dataset<Row> getAllFinancialContract() {
        String sql = "SELECT" +
            "   id," +
            "   contract_no," +
            "   contract_name," +
            "   financial_contract_uuid," +
            "   adva_repayment_term" +
            "   FROM" +
            "   financial_contract";

        String[] predicates = initPredicates("financial_contract");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("financial_contract dataset size is :{}", dataSet.count());

            dataSet = dataSet.drop("id").distinct();
        }
        return dataSet;
    }

    @Override
    public Row getFinancialContract(String financialContractUuid) {
        String sql = "SELECT" +
            "   id," +
            "   contract_no," +
            "   contract_name," +
            "   financial_contract_uuid," +
            "   adva_repayment_term" +
            "   FROM" +
            "   financial_contract fc" +
            "   WHERE fc.financial_contract_uuid = '"+ financialContractUuid +"'";

        String[] predicates = initPredicates("financial_contract");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("financial_contract dataset size is :{}", dataSet.count());
            dataSet = dataSet.drop("id").distinct();
        }
        return dataSet == null ? null : dataSet.first();
    }

    @Override
    public Dataset<Row> getAssetSet(String financialContractUuid, Date overdueDate) {
        String date = DateUtils.getDateFormatYYMMDD(overdueDate);
        if (StringUtils.isEmpty(financialContractUuid) || StringUtils.isEmpty(date)) return null;
        String sql = "SELECT"
            + " id,"
            + " asset_status,"
            + " asset_uuid,"
            + " asset_recycle_date,"
            + " financial_contract_uuid,"
            + " active_status,"
            + " asset_principal_value,"
            + " asset_interest_value"
            + " FROM asset_set"
            + " WHERE financial_contract_uuid = '"+ financialContractUuid +"'"
            + " AND active_status = 0"
            + " AND DATE(asset_recycle_date) = '" + date + "'";

        String[] predicates = initPredicates("asset_set");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("asset dataset size is :{}", dataSet.count());
            dataSet = dataSet.drop("id").distinct();
        }
        return dataSet;
    }

    @Override
    public Dataset<Row> getAllRepayment(Collection<Object> assetsetUuid, Date doingDate) {
        String date = DateUtils.getDateFormatYYMMDD(doingDate);
        if (assetsetUuid.size() <= 0) return null;
        String uuids = getUuidLists(assetsetUuid);
        if (StringUtils.isEmpty(uuids) || StringUtils.isEmpty(date)) return null;
        String sql = "SELECT"
            + " id,"
            + " journal_voucher_uuid"
            + " FROM journal_voucher jv"
            + " WHERE"
            + " jv.journal_voucher_type IN (0, 1, 3, 5, 7, 9, 11, 14)"
            + " AND jv.status != 2"
            + " AND DATE(jv.issued_time) <= '" + date + "'"
            + " AND jv.related_bill_contract_info_lv_3 IN ("+uuids+")";
        String[] predicates = initPredicates("journal_voucher");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("all repayment dataset size is :{}", dataSet.count());
            dataSet = dataSet.drop("id").distinct();
        }
        return dataSet;
    }

    @Override
    public Dataset<Row> getOfflineRepayment(Collection<Object> assetsetUuid, Date doingDate) {
        String date = DateUtils.getDateFormatYYMMDD(doingDate);
        if (assetsetUuid.size() <= 0) return null;
        String uuids = getUuidLists(assetsetUuid);
        if (StringUtils.isEmpty(uuids) || StringUtils.isEmpty(date)) return null;
        String sql = "SELECT"
            + " id,"
            + " journal_voucher_uuid"
            + " FROM journal_voucher jv"
            + " WHERE"
            + " jv.journal_voucher_type IN (5,9,11,14)"
            + " AND jv.status != 2"
            + " AND DATE(jv.issued_time) = '" + date + "'"
            + " AND jv.related_bill_contract_info_lv_3 IN ("+uuids+")";
        String[] predicates = initPredicates("journal_voucher");

        Dataset<Row> dataSet = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        if (dataSet != null && dataSet.count() > 0) {
            log.info("offline repayment dataset size is :{}", dataSet.count());
            dataSet = dataSet.drop("id").distinct();

        }
        return dataSet;
    }

    @Override
    public Dataset<Row> getPaidData(Collection<Object> journalVoucherUuid) {
        if (journalVoucherUuid.size() <= 0) return null;
        String uuids = getUuidLists(journalVoucherUuid);
        if (StringUtils.isEmpty(uuids)) return null;
        String lbsSql = "SELECT"
            + " id,"
            + " ledger_book_no,"
            + " journal_voucher_uuid,"
            + " CASE WHEN third_account_name IN ('TRD_BANK_SAVING_GENERAL_PRINCIPAL', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSEL_PRINCIPAL') THEN debit_balance - credit_balance ELSE 0 END AS loanAssetPrincipal,"
            + " CASE WHEN third_account_name IN ('TRD_BANK_SAVING_GENERAL_INTEREST', 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST') THEN debit_balance - credit_balance ELSE 0 END AS loanAssetInterest"
            + " FROM ledger_book_shelf"
            + " where journal_voucher_uuid in (" + uuids + ")";

        String[] lbsPredicates = initPredicates("ledger_book_shelf");

        Dataset<Row> paidOffDs = loadDataFromTable(SQLUtils.wrapperSQL(lbsSql), lbsPredicates);

        if (paidOffDs != null && paidOffDs.count() > 0) {
            log.info("paid dataset size is :{}", paidOffDs.count());
            paidOffDs = paidOffDs.drop("id").distinct();

            // 合并计算实收
            // 本金 利息
            paidOffDs = paidOffDs.groupBy("journal_voucher_uuid", "ledger_book_no").agg(
                sum("loanAssetPrincipal").as("loan_asset_principal"),
                sum("loanAssetInterest").as("loan_asset_interest")
            );
        }
        return paidOffDs;
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
