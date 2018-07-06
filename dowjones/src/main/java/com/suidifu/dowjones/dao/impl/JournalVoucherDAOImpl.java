package com.suidifu.dowjones.dao.impl;

import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.dao.JournalVoucherDAO;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

import static org.apache.spark.sql.functions.countDistinct;
import static org.apache.spark.sql.functions.sum;


/**
 * @author: xwq
 */
@Repository
@Slf4j
public class JournalVoucherDAOImpl extends BaseDAO implements JournalVoucherDAO, Serializable {

    private static final String REPAYMENT_BASE = "SELECT" +
            "  curdate() AS createDate," +
            "  jv.related_bill_contract_info_lv_1 AS financialContractUuid," +
            "  jv.journal_voucher_type AS journalVoucherType," +
            "  jv.cash_flow_channel_type AS cashFlowChannelType," +
            "  COUNT(DISTINCT jv.related_bill_contract_info_lv_3) AS count," +
            "  SUM(" +
            "		CASE third_account_name" +
            "		WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSEL_PRINCIPAL' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		ELSE" +
            "			0" +
            "		END" +
            "	) loanAssetPrincipal," +
            "	SUM(" +
            "		CASE third_account_name" +
            "		WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		ELSE" +
            "			0" +
            "		END" +
            "	) loanAssetInterest," +
            "	SUM(" +
            "		CASE third_account_name" +
            "		WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		ELSE" +
            "			0" +
            "		END" +
            "	) loanServiceFee," +
            "	SUM(" +
            "		CASE third_account_name" +
            "		WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		ELSE" +
            "			0" +
            "		END" +
            "	) loanTechFee," +
            "	SUM(" +
            "		CASE third_account_name" +
            "		WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		ELSE" +
            "			0" +
            "		END" +
            "	) loanOtherFee," +
            "	SUM(" +
            "		CASE third_account_name" +
            "		WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		ELSE" +
            "			0" +
            "		END" +
            "	) overdueFeePenalty," +
            "	SUM(" +
            "		CASE third_account_name" +
            "		WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		ELSE" +
            "			0" +
            "		END" +
            "	) overdueFeeObligation," +
            "	SUM(" +
            "		CASE third_account_name" +
            "		WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		ELSE" +
            "			0" +
            "		END" +
            "	) overdueFeeService," +
            "	SUM(" +
            "		CASE third_account_name" +
            "		WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN" +
            "			lbs.debit_balance - lbs.credit_balance" +
            "		ELSE" +
            "			0" +
            "		END" +
            "	) overdueFeeOther" +
            "   FROM journal_voucher jv " +
            "	LEFT JOIN ledger_book_shelf lbs ON lbs.journal_voucher_uuid = jv.journal_voucher_uuid";

    @Override
    public Dataset<Row> getRepurchaseData(String financialContractUuid, Date time) {

        String yesterday = DateUtils.getDateFormatYYMMDD(time);

        if (StringUtils.isBlank(yesterday) || StringUtils.isBlank(financialContractUuid)) {
            return null;
        }

        /**
         * raw sql:
         * params: financialContractUuid = "d2812bc5-5057-4a91-b3fd-9019506f0499";
         * yesterday = "2017-05-16"
         SELECT
         curdate()                                          AS createDate,
         jv.related_bill_contract_info_lv_1                 AS financialContractUuid,
         COUNT(DISTINCT jv.related_bill_contract_info_lv_3) AS count,
         SUM(
         CASE lbs.third_account_name
         WHEN 'TRD_BANK_SAVING_GENERAL_REPURCHASE_ASSET_PRINCIPLE'
         THEN
         lbs.debit_balance - lbs.credit_balance
         ELSE
         0
         END
         )                                                  AS repurchasePrincipal,
         SUM(
         CASE lbs.third_account_name
         WHEN 'TRD_BANK_SAVING_GENERAL_REPURCHASE_ASSET_INTEREST'
         THEN
         lbs.debit_balance - lbs.credit_balance
         ELSE
         0
         END
         )                                                  AS repurchaseInterest,
         SUM(
         CASE lbs.third_account_name
         WHEN 'TRD_BANK_SAVING_GENERAL_REPURCHASE_ASSET_PENALTY'
         THEN
         lbs.debit_balance - lbs.credit_balance
         ELSE
         0
         END
         )                                                  AS repurchasePenalty,
         SUM(
         CASE lbs.third_account_name
         WHEN 'TRD_BANK_SAVING_GENERAL_REPURCHASE_ASSET_OTHER_FEE'
         THEN
         lbs.debit_balance - lbs.credit_balance
         ELSE
         0
         END
         )                                                  AS repurchaseOtherCharges
         FROM journal_voucher jv
         LEFT JOIN ledger_book_shelf lbs ON lbs.journal_voucher_uuid = jv.journal_voucher_uuid
         WHERE jv.related_bill_contract_info_lv_1 = :financial_contract_uuid
         AND jv.status = '1'
         AND jv.journal_voucher_type = '10'
         AND lbs.life_cycle IN (0, 1)
         AND DATE(jv.trade_time) = :create_time;
         *
         */


        String sql = "SELECT"
                + " jv.id AS id,"
                + " jv.status AS x,"
                + " jv.journal_voucher_type AS xx,"
                + " lbs.life_cycle AS xxx,"
                + " jv.trade_time AS xxxx,"
                + " '" + yesterday + "' AS create_date,"
                + " jv.related_bill_contract_info_lv_1 AS financial_contract_uuid,"
                + " jv.related_bill_contract_info_lv_3 AS will_count,"
                + " CASE lbs.third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_REPURCHASE_ASSET_PRINCIPLE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS repurchase_principal,"
                + " CASE lbs.third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_REPURCHASE_ASSET_INTEREST' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS repurchase_interest,"
                + " CASE lbs.third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_REPURCHASE_ASSET_PENALTY' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS repurchase_penalty,"
                + " CASE lbs.third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_REPURCHASE_ASSET_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS repurchase_other_charges"
                + " FROM journal_voucher jv"
                + " LEFT JOIN ledger_book_shelf lbs"
                + " ON lbs.journal_voucher_uuid = jv.journal_voucher_uuid"
                + " WHERE jv.related_bill_contract_info_lv_1 = '" + financialContractUuid + "'"
                + " AND jv.status = '1'"
                + " AND jv.journal_voucher_type = '10'"
                + " AND lbs.life_cycle IN (0, 1)"
                + " AND DATE(jv.trade_time) = '" + yesterday + "'";

        String[] predicates = initPredicates("journal_voucher");
        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);

        log.info("raw repurchase data size:{}", ds.count());

        ds = ds.drop("id", "x", "xx", "xxx", "xxxx");
        ds = ds.groupBy("financial_contract_uuid", "create_date").agg(
                sum("repurchase_principal").as("repurchase_principal"),
                sum("repurchase_interest").as("repurchase_interest"),
                sum("repurchase_penalty").as("repurchase_penalty"),
                sum("repurchase_other_charges").as("repurchase_other_charges"),
                countDistinct("will_count").as("count")
        );

//      ds = ds.groupBy("financial_contract_uuid").sum("repurchase_principal","repurchase_interest","repurchase_penalty","repurchase_other_charges");
//      ds.withColumnRenamed("sum(repurchase_principal)","repurchase_principal").withColumnRenamed().withColumnRenamed().withColumnRenamed();

        log.info("result repurchase row size:{}", ds.count());

        return ds;
    }

    @Override
    public Dataset<Row> getGuaranteeData(String financialContractUuid, Date time) {
        String yesterday = DateUtils.getDateFormatYYMMDD(time);

        if (StringUtils.isBlank(yesterday) || StringUtils.isEmpty(financialContractUuid)) {
            return null;
        }

        String sql = "SELECT"
                + " jv.id as id,"
                + " '" + yesterday + "' as create_date,"
                + " jv.related_bill_contract_info_lv_1 as financial_contract_uuid,"
                + " jv.related_bill_contract_info_lv_3 as count,"
                + " jv.booking_amount as amount"
                + " FROM journal_voucher jv"
                + " WHERE jv.related_bill_contract_info_lv_1 = '" + financialContractUuid + "'"
                + " AND jv.status = '1'"
                + " AND jv.journal_voucher_type = '12'"
                + " AND DATE(jv.trade_time) ='" + yesterday + "'";

        String[] predicates = initPredicates("journal_voucher");
        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates);
        log.info("Raw guarantee date size {}", ds.count());
        ds = ds.drop("id");
        ds = ds.groupBy("financial_contract_uuid", "create_date").agg(
                sum("amount").as("amount"),
                countDistinct("count").as("count")
        );

        ds.printSchema();
        ds.show();

        return ds;
    }

    /**
     * 实际还款-线上还款
     * 不区分 journal_voucher_type
     */
    @Override
    public Dataset<Row> getOnlineRepaymentData(String financialContractUuid, Date time) {

        String yesterday = DateUtils.getDateFormatYYMMDD(time);

        if (StringUtils.isBlank(yesterday) || StringUtils.isEmpty(financialContractUuid)) {
            return null;
        }

        String sql = "SELECT"
                + " jv.id AS id,"
                + " 0 AS business_type,"
                + " '" + yesterday + "' AS create_date,"
                + " jv.related_bill_contract_info_lv_1 AS financial_contract_uuid,"
                + " jv.cash_flow_channel_type AS cash_flow_channel_type,"
                + " jv.related_bill_contract_info_lv_3 AS will_count,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSEL_PRINCIPAL' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanAssetPrincipal,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanAssetInterest,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanServiceFee,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanTechFee,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanOtherFee,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeePenalty,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeeObligation,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeeService,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeeOther"
                + " FROM journal_voucher jv"
                + " LEFT JOIN ledger_book_shelf lbs ON lbs.journal_voucher_uuid = jv.journal_voucher_uuid"
                + " WHERE jv.related_bill_contract_info_lv_1 = '" + financialContractUuid + "'"
                + " AND jv.status = 1"
                + " AND jv.journal_voucher_type = 7"
                + " AND lbs.life_cycle IN (0,1)"
                + " AND DATE(jv.trade_time) ='" + yesterday + "'";

        String[] predicates = initPredicates("journal_voucher");
        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).drop("id");

        log.info("Raw online repayment data size {}", ds.count());

        ds = ds.groupBy("financial_contract_uuid", "business_type", "create_date", "cash_flow_channel_type").agg(
                countDistinct("will_count").as("count"),
                sum("loanAssetPrincipal").as("loan_asset_principal"),
                sum("loanAssetInterest").as("loan_asset_interest"),
                sum("loanServiceFee").as("loan_service_fee"),
                sum("loanTechFee").as("loan_tech_fee"),
                sum("loanOtherFee").as("loan_other_fee"),
                sum("overdueFeePenalty").as("overdue_fee_penalty"),
                sum("overdueFeeObligation").as("overdue_fee_obligation"),
                sum("overdueFeeService").as("overdue_fee_service"),
                sum("overdueFeeOther").as("overdue_fee_other")
        );

        ds.printSchema();
        ds.show();

        return ds;
    }

    /*
     * 实际还款-线下还款
     * 按类型
     */
    @Override
    public Dataset<Row> getOfflineRepaymentData(String financialContractUuid, Date time) {
        String yesterday = DateUtils.getDateFormatYYMMDD(time);
        if (StringUtils.isBlank(yesterday) || StringUtils.isEmpty(financialContractUuid)) {
            return null;
        }

        String sql = "SELECT"
                + " jv.id AS id,"
                + " 1 AS business_type,"
                + " '" + yesterday + "' AS create_date,"
                + " jv.related_bill_contract_info_lv_1 AS financial_contract_uuid,"
                + " jv.journal_voucher_type AS journal_voucher_type,"
//            + " jv.cash_flow_channel_type AS cashFlowChannelType,"
                + " jv.related_bill_contract_info_lv_3 AS will_distinct_count,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN   lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSEL_PRINCIPAL' THEN   lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanAssetPrincipal,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN   lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanAssetInterest,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanServiceFee,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanTechFee,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanOtherFee,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeePenalty,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeeObligation,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeeService,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeeOther"
                + " FROM journal_voucher jv"
                + " LEFT JOIN ledger_book_shelf lbs ON lbs.journal_voucher_uuid = jv.journal_voucher_uuid"
                + " WHERE jv.related_bill_contract_info_lv_1 = '" + financialContractUuid + "'"
                + " AND jv.status = 1"
                + " AND jv.journal_voucher_type IN (5,9,11,14)"
                + " AND lbs.life_cycle IN (0,1)"
                + " AND DATE(jv.trade_time) ='" + yesterday + "'";

        String[] predicates = initPredicates("journal_voucher");
        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).drop("id");

        log.info("Raw offline repayment data size {}", ds.count());

        ds = ds.groupBy("financial_contract_uuid", "business_type", "create_date", "journal_voucher_type").agg(
                countDistinct("will_distinct_count").as("count"),
                sum("loanAssetPrincipal").as("loan_asset_principal"),
                sum("loanAssetInterest").as("loan_asset_interest"),
                sum("loanServiceFee").as("loan_service_fee"),
                sum("loanTechFee").as("loan_tech_fee"),
                sum("loanOtherFee").as("loan_other_fee"),
                sum("overdueFeePenalty").as("overdue_fee_penalty"),
                sum("overdueFeeObligation").as("overdue_fee_obligation"),
                sum("overdueFeeService").as("overdue_fee_service"),
                sum("overdueFeeOther").as("overdue_fee_other")
        );

        ds.printSchema();
        ds.show();

        return ds;
    }

    /*
     * 实际还款-线下支付单
     */
    @Override
    public Dataset<Row> getOfflinePaymentData(String financialContractUuid, Date time) {
        String yesterday = DateUtils.getDateFormatYYMMDD(time);
        if (StringUtils.isBlank(yesterday)) {
            return null;
        }

        String sql = "SELECT"
                + " jv.id AS id,"
                + " 2 AS business_type,"
                + " '" + yesterday + "' AS create_date,"
                + " jv.related_bill_contract_info_lv_1 AS financial_contract_uuid,"
//            + " jv.journal_voucher_type AS journalVoucherType,"
//            + " jv.cash_flow_channel_type AS cashFlowChannelType,"
                + " jv.related_bill_contract_info_lv_3 AS will_distinct_count,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSEL_PRINCIPAL' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanAssetPrincipal,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanAssetInterest,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanServiceFee,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanTechFee,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS loanOtherFee,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN   lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeePenalty,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeeObligation,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeeService,"
                + " CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN   lbs.debit_balance - lbs.credit_balance"
                + " WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN lbs.debit_balance - lbs.credit_balance ELSE 0 END AS overdueFeeOther"
                + " FROM journal_voucher jv"
                + " LEFT JOIN ledger_book_shelf lbs ON lbs.journal_voucher_uuid = jv.journal_voucher_uuid"
                + " WHERE jv.related_bill_contract_info_lv_1 = '" + financialContractUuid + "'"
                + " AND jv.status = 1"
                + " AND jv.journal_voucher_type = 1"
                + " AND lbs.life_cycle IN (0,1)"
                + " AND DATE(jv.trade_time) ='" + yesterday + "'";

        String[] predicates = initPredicates("journal_voucher");
        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).drop("id");

        log.info("Raw offline payment data size {}", ds.count());

        ds = ds.groupBy("financial_contract_uuid", "business_type", "create_date").agg(
                countDistinct("will_distinct_count").as("count"),
                sum("loanAssetPrincipal").as("loan_asset_principal"),
                sum("loanAssetInterest").as("loan_asset_interest"),
                sum("loanServiceFee").as("loan_service_fee"),
                sum("loanTechFee").as("loan_tech_fee"),
                sum("loanOtherFee").as("loan_other_fee"),
                sum("overdueFeePenalty").as("overdue_fee_penalty"),
                sum("overdueFeeObligation").as("overdue_fee_obligation"),
                sum("overdueFeeService").as("overdue_fee_service"),
                sum("overdueFeeOther").as("overdue_fee_other")
        );

        ds.printSchema();
        ds.show();

        return ds;
    }
}
