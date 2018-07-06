package com.suidifu.dowjones.dao.impl;

import static org.apache.spark.sql.functions.countDistinct;
import static org.apache.spark.sql.functions.cume_dist;
import static org.apache.spark.sql.functions.sum;

import com.suidifu.dowjones.dao.AssetSetDAO;
import com.suidifu.dowjones.dao.BaseDAO;
import com.suidifu.dowjones.utils.DateUtils;
import com.suidifu.dowjones.utils.GenericJdbcSupport;
import com.suidifu.dowjones.utils.SQLUtils;
import java.io.Serializable;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author: frankwoo(吴峻申) <br>
 * @date: 2017/11/6 <br>
 * @time: 下午9:19 <br>
 * @mail: frank_wjs@hotmail.com <br>
 */
@Repository
@Slf4j
public class AssetSetDAOImpl extends BaseDAO implements AssetSetDAO, Serializable {

    @Override
    public Dataset<Row> getAllOpenRepaymentPlanList(String idNumber) {
        if (StringUtils.isBlank(idNumber)) {
            return null;
        }

        String sql = "(SELECT" +
                "  ass.overdue_status      AS overDueStatus," +
                "  lbs.debit_balance       AS debitBalance," +
                "  lbs.credit_balance      AS creditBalance," +
                "  lbs.third_account_name  AS thirdAccountName," +
                "  lbs.second_account_name AS secondAccountName," +
                "  lbs.first_account_name  AS firstAccountName," +
                "  lbs.third_account_uuid  AS thirdAccountUuid," +
                "  lbs.second_account_uuid AS secondAccountUuid" +
                "  FROM customer cu" +
                "  INNER JOIN asset_set ass ON cu.customer_uuid = ass.customer_uuid" +
                "  INNER JOIN contract con ON cu.id = con.customer_id" +
                "  INNER JOIN FINANCIAL_CONTRACT fc ON con.financial_contract_uuid = fc.financial_contract_uuid" +
                "  INNER JOIN ledger_book lb ON lb.ledger_book_no = fc.ledger_book_no" +
                "  INNER JOIN ledger_book_shelf lbs ON lbs.related_lv_1_asset_uuid = lb.ledger_book_no AND" +
                "                                      lbs.ledger_book_no = ass.asset_uuid" +
                "  WHERE 1 = 1" +
                "      AND cu.account='" + idNumber + "'" +
                "      AND cu.customer_type = 0" +
                "      AND ass.active_status = 0" +
                "      AND lbs.life_cycle = 1" +
                "  ORDER BY ass.current_period ASC" +
                ") temp";

        return loadDataFromTable(sql);
    }

    @Override
    public Dataset<Row> getDailyOverStatusContractNumber() {
        String sql = "SELECT id AS id," +
                " contract_uuid AS contractUuid," +
                " overdue_status AS overdueStatus" +
                " FROM asset_set";

        String[] predicates = initPredicates("asset_set");

        return loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).
                filter("overdueStatus<>0 and contractUuid IS NOT NULL").
                groupBy("contractUuid").
                count().
                drop(new Column("overdueStatus")).
                drop(new Column("id"));
    }

    private static final String ACTUAL_PAYMENT_BASE = "SELECT" +
    		"  curdate() AS createDate," +
    		"  asst.financial_contract_uuid AS financialContractUuid," + 
    		"  COUNT(DISTINCT asst.asset_uuid) AS count," + 
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
    		"	FROM asset_set asst" + 
    		"	LEFT JOIN ledger_book_shelf lbs ON lbs.related_lv_1_asset_uuid = asst.asset_uuid";
    
    //实际还款-提前还款
	@Override
	public Dataset<Row> getPrePaymentData(String financialContractUuid, Date time) {
        String yesterday = DateUtils.getDateFormatYYMMDD(time);
        if (StringUtils.isBlank(yesterday) || StringUtils.isEmpty(financialContractUuid)) {
            return null;
        }

        String sql = "SELECT"
            + " asst.id AS id,"
            + " '" + yesterday + "' AS create_date,"
            + " asst.financial_contract_uuid AS financial_contract_uuid,"
            + " asst.asset_uuid AS will_distinct_count,"
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
            + " FROM asset_set asst"
            + " LEFT JOIN ledger_book_shelf lbs ON lbs.related_lv_1_asset_uuid = asst.asset_uuid"
            + " WHERE asst.financial_contract_uuid = '" + financialContractUuid + "'"
            + " AND date(asst.actual_recycle_date) ='" + yesterday + "'"
            + " AND asst.asset_recycle_date > date(asst.actual_recycle_date)"
            + " AND asst.asset_status = 1"
            + " AND asst.on_account_status = 2"
            + " AND lbs.life_cycle IN (0,1)";

        String[] predicates = initPredicates("asset_set");

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql), predicates).drop("id");

        log.info("Raw pre payment data size {}", ds.count());

        ds = ds.groupBy("financial_contract_uuid", "create_date").agg(
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

	//实际还款-部分还款
	@Override
	public Dataset<Row> getPartPaymentData(String financialContractUuid, Date time) {
        String yesterday = DateUtils.getDateFormatYYMMDD(time);
        if (StringUtils.isBlank(yesterday)) {
            return null;
        }
		
		String sql = "SELECT"
            + " asst.id AS id,"
            + " '" + yesterday + "' AS create_date,"
            + " asst.financial_contract_uuid AS financial_contract_uuid,"
            + " asst.asset_uuid AS will_distinct_count,"
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
            + " FROM asset_set asst"
            + " LEFT JOIN ledger_book_shelf lbs ON lbs.related_lv_1_asset_uuid = asst.asset_uuid"
            + " WHERE asst.financial_contract_uuid = '" + financialContractUuid + "'"
            + " AND date(asst.actual_recycle_date) ='" + yesterday + "'"
            + " AND asst.asset_status = 0"
            + " AND asst.on_account_status = 3"
            + " AND lbs.life_cycle IN (0,1)";

        String[] predicates = initPredicates("asset_set");

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql),predicates).drop("id");

        log.info("Raw part payment data size {}", ds.count());

        ds = ds.groupBy("financial_contract_uuid", "create_date").agg(
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
	
	 private static final String PLAN_PAYMENT_BASE = 
				"  asst.asset_uuid assetUuid," + 
				"  asst.financial_contract_uuid financialContractUuid," + 
				"  asst.asset_principal_value assetPrincipalValue," + 
				"  asst.asset_interest_value assetInterestValue," + 
				"  SUM(" + 
				"  	   CASE second_account_name" + 
				"  	   WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN" + 
				"  	   	 account_amount" + 
				"  	   ELSE" + 
				"  	     0" + 
				"  	   END" + 
				"  ) loanServiceFee," + 
				"  SUM(" + 
				"  	   CASE second_account_name" + 
				"  	   WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN" + 
				"  	   	 account_amount" + 
				"  	   ELSE" + 
				"  	   	 0" + 
				"  	   END" + 
				"  ) loanTechFee," + 
				"  SUM(" + 
				"  	   CASE second_account_name" + 
				"  	   WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN" + 
				"  	   	 account_amount" + 
				"  	   ELSE" + 
				"  	     0" + 
				"  	   END" + 
				"  ) loanOtherFee," + 
				"  SUM(" + 
				"  	   CASE second_account_name" + 
				"  	   WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN" + 
				"  	     account_amount" + 
				"  	   ELSE" + 
				"  	   	 0" + 
				"  	   END" + 
				"  ) overdueFeePenalty," + 
				"  SUM(" + 
				"  	   CASE third_account_name" + 
				"  	   WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN" + 
				"  	   	 account_amount" + 
				"  	   ELSE" + 
				"  	   	 0" + 
				"  	   END" + 
				"  ) overdueFeeObligation," + 
				"  SUM(" + 
				"  	   CASE third_account_name" + 
				"  	   WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN" + 
				"  	   	 account_amount" + 
				"  	   ELSE" + 
				"  	   	 0" + 
				"  	   END" + 
				"  ) overdueFeeService," + 
				"  SUM(" + 
				"  	   CASE third_account_name" + 
				"  	   WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN" + 
				"  	   	 account_amount" + 
				"  	   ELSE" + 
				"  	   	 0" + 
				"	   END" + 
				"  ) overdueFeeOther" + 
				"  FROM asset_set asst" + 
				"  LEFT JOIN asset_set_extra_charge asstec ON asst.asset_uuid = asstec.asset_set_uuid";


    //应收款-应还日计划还款
    @Override
    public Dataset<Row> getPlanRepaymentData(String financialContractUuid, Date time) {

        String yesterday = DateUtils.getDateFormatYYMMDD(time);

        if (StringUtils.isBlank(yesterday) || StringUtils.isEmpty(financialContractUuid)) {
            return null;
        }

        String sql = "SELECT"
            + " '" + yesterday + "' AS create_date,"
            + " asst.id AS id,"
            + " 0 AS plan_style,"
            + " asst.asset_uuid AS assetUuid,"
            + " asst.financial_contract_uuid AS financial_contract_uuid,"
            + " asst.asset_principal_value AS assetPrincipalValue,"
            + " asst.asset_interest_value AS assetInterestValue,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END AS loanServiceFee,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END AS loanTechFee,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END AS loanOtherFee,"
            + " CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END AS overdueFeePenalty,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END AS overdueFeeObligation,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END AS overdueFeeService,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END AS overdueFeeOther"
            + " FROM asset_set asst"
            + " LEFT JOIN asset_set_extra_charge asstec"
            + " ON asst.asset_uuid = asstec.asset_set_uuid"
            + " WHERE asst.financial_contract_uuid = '" + financialContractUuid + "'"
            + " AND asst.asset_recycle_date = '" + yesterday + "'"
            + " AND asst.active_status != '1'";

        String[] predicates = initPredicates("asset_set");

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql),predicates).drop("id");

        log.info("Raw plan repayment data size: {}", ds.count());

        ds = ds.groupBy("financial_contract_uuid", "create_date","plan_style", "assetUuid", "assetPrincipalValue", "assetInterestValue").agg(
            sum("loanServiceFee").as("loanServiceFee"),
            sum("loanTechFee").as("loanTechFee"),
            sum("loanOtherFee").as("loanOtherFee"),
            sum("overdueFeePenalty").as("overdueFeePenalty"),
            sum("overdueFeeObligation").as("overdueFeeObligation"),
            sum("overdueFeeService").as("overdueFeeService"),
            sum("overdueFeeOther").as("overdueFeeOther")
        );

        ds = ds.groupBy("financial_contract_uuid", "create_date", "plan_style").agg(
            countDistinct("assetUuid").as("count"),
            sum("assetPrincipalValue").as("asset_principal_value"),
            sum("assetInterestValue").as("asset_interest_value"),
            sum("loanServiceFee").as("loan_service_fee"),
            sum("loanTechFee").as("loan_tech_fee"),
            sum("loanOtherFee").as("loan_other_fee"),
            sum("overdueFeePenalty").as("overdue_fee_penalty"),
            sum("overdueFeeObligation").as("overdue_fee_obligation"),
            sum("overdueFeeService").as("overdue_fee_service"),
            sum("overdueFeeOther").as("overdue_fee_other")
        );

        return ds;
    }

	//应收款-宽限期内计划还款
	@Override
	public Dataset<Row> getNotOverduePlanRepaymentData(String financialContractUuid, Date time) {
        String yesterday = DateUtils.getDateFormatYYMMDD(time);
        if (StringUtils.isBlank(yesterday)) {
            return null;
        }
		
		String sql = "SELECT"
            + " '" + yesterday + "' AS create_date,"
            + " asst.id AS id,"
            + " 1 AS plan_style,"
            + " asst.asset_uuid assetUuid,"
            + " asst.financial_contract_uuid financial_contract_uuid,"
            + " asst.asset_principal_value assetPrincipalValue,"
            + " asst.asset_interest_value assetInterestValue,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END AS loanServiceFee,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END AS loanTechFee,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END AS loanOtherFee,"
            + " CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END AS overdueFeePenalty,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END AS overdueFeeObligation,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END AS overdueFeeService,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END AS overdueFeeOther"
            + " FROM asset_set asst"
            + " LEFT JOIN asset_set_extra_charge asstec"
            + " ON asst.asset_uuid = asstec.asset_set_uuid"
            + " LEFT JOIN financial_contract fc"
            + " ON asst.financial_contract_uuid = fc.financial_contract_uuid"
            + " WHERE asst.financial_contract_uuid = '" + financialContractUuid + "'"
            + " AND date_add(asst.asset_recycle_date, interval fc.adva_repayment_term day) >= '" + yesterday + "'"
            + " AND asst.asset_recycle_date < '" + yesterday + "'"
            + " AND asst.active_status != '1'";

        String[] predicates = initPredicates("asset_set");

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql),predicates).drop("id");

        log.info("Raw not overdue plan repayment data {}", ds.count());

        ds = ds.groupBy("financial_contract_uuid", "create_date","plan_style", "assetUuid","assetPrincipalValue","assetInterestValue").agg(
            sum("loanServiceFee").as("loanServiceFee"),
            sum("loanTechFee").as("loanTechFee"),
            sum("loanOtherFee").as("loanOtherFee"),
            sum("overdueFeePenalty").as("overdueFeePenalty"),
            sum("overdueFeeObligation").as("overdueFeeObligation"),
            sum("overdueFeeService").as("overdueFeeService"),
            sum("overdueFeeOther").as("overdueFeeOther")
        );

        ds = ds.groupBy("financial_contract_uuid", "create_date", "plan_style").agg(
            countDistinct("assetUuid").as("count"),
            sum("assetPrincipalValue").as("asset_principal_value"),
            sum("assetInterestValue").as("asset_interest_value"),
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

	//应收款-待确认计划还款
	@Override
	public Dataset<Row> getUnconfirmedPlanRepaymentData(String financialContractUuid, Date doDate) {

        String yesterday = DateUtils.getDateFormatYYMMDD(doDate);

        String sql = "SELECT"
            + " '" + yesterday + "' AS create_date,"
            + " 2 AS plan_style,"
            + " asst.id AS id,"
            + " asst.asset_uuid assetUuid,"
            + " asst.financial_contract_uuid financial_contract_uuid,"
            + " asst.asset_principal_value assetPrincipalValue,"
            + " asst.asset_interest_value assetInterestValue,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END AS loanServiceFee,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END AS loanTechFee,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END AS loanOtherFee,"
            + " CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END AS overdueFeePenalty,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END AS overdueFeeObligation,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END AS overdueFeeService,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END AS overdueFeeOther"
            + " FROM asset_set asst"
            + " LEFT JOIN asset_set_extra_charge asstec"
            + " ON asst.asset_uuid = asstec.asset_set_uuid"
            + " WHERE asst.financial_contract_uuid = '" + financialContractUuid + "'"
            + " AND asst.overdue_status = 1"
            + " AND asst.active_status != 1";
//
//        String sql = "SELECT 2 " + " AS planStyle," + PLAN_PAYMENT_BASE +
//				"  WHERE asst.financial_contract_uuid = '" + financialContractUuid + "'" +
//				"    AND asst.overdue_tatus = '1'" +
//				"	 AND asst.active_status != '1'";
//
        String[] predicates = initPredicates("asset_set");

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql),predicates).drop("id");

        log.info("Raw get unconfirmed plan repayment data size {}", ds.count());

        ds = ds.groupBy("financial_contract_uuid", "create_date","plan_style", "assetUuid","assetPrincipalValue","assetInterestValue").agg(
            sum("loanServiceFee").as("loanServiceFee"),
            sum("loanTechFee").as("loanTechFee"),
            sum("loanOtherFee").as("loanOtherFee"),
            sum("overdueFeePenalty").as("overdueFeePenalty"),
            sum("overdueFeeObligation").as("overdueFeeObligation"),
            sum("overdueFeeService").as("overdueFeeService"),
            sum("overdueFeeOther").as("overdueFeeOther")
        );

        ds =ds.groupBy("financial_contract_uuid", "create_date", "plan_style").agg(
            countDistinct("assetUuid").as("count"),
            sum("assetPrincipalValue").as("asset_principal_value"),
            sum("assetInterestValue").as("asset_interest_value"),
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

    //应收款-已逾期计划还款
    @Override
    public Dataset<Row> getOverduePlanRepaymentData(String financialContractUuid, Date doDate) {

//        String sql = "SELECT 3 " + " AS planStyle," + PLAN_PAYMENT_BASE +
//            "  WHERE asst.financial_contract_uuid = '" + financialContractUuid + "'" +
//            "    AND asst.overdue_tatus = '2'" +
//            "	 AND asst.active_status != '1'";

        String yesterday = DateUtils.getDateFormatYYMMDD(doDate);

        String sql = "SELECT"
            + " '" + yesterday + "' AS create_date,"
            + " 3 AS plan_style,"
            + " asst.id AS id,"
            + " asst.asset_uuid assetUuid,"
            + " asst.financial_contract_uuid financial_contract_uuid,"
            + " asst.asset_principal_value assetPrincipalValue,"
            + " asst.asset_interest_value assetInterestValue,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END AS loanServiceFee,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END AS loanTechFee,"
            + " CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END AS loanOtherFee,"
            + " CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END AS overdueFeePenalty,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END AS overdueFeeObligation,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END AS overdueFeeService,"
            + " CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END AS overdueFeeOther"
            + " FROM asset_set asst"
            + " LEFT JOIN asset_set_extra_charge asstec ON asst.asset_uuid = asstec.asset_set_uuid"
            + " WHERE asst.financial_contract_uuid = '" + financialContractUuid + "'"
            + " AND asst.overdue_status = 2"
            + " AND asst.active_status != 1";

        String[] predicates = initPredicates("asset_set");

        Dataset<Row> ds = loadDataFromTable(SQLUtils.wrapperSQL(sql),predicates).drop("id");
        log.info("Raw overdue plan repayment data size:{}", ds.count());

        ds = ds.groupBy("financial_contract_uuid", "create_date","plan_style", "assetUuid","assetPrincipalValue","assetInterestValue").agg(
            sum("loanServiceFee").as("loanServiceFee"),
            sum("loanTechFee").as("loanTechFee"),
            sum("loanOtherFee").as("loanOtherFee"),
            sum("overdueFeePenalty").as("overdueFeePenalty"),
            sum("overdueFeeObligation").as("overdueFeeObligation"),
            sum("overdueFeeService").as("overdueFeeService"),
            sum("overdueFeeOther").as("overdueFeeOther")
        );

        ds = ds.groupBy("financial_contract_uuid", "create_date", "plan_style").agg(
            countDistinct("assetUuid").as("count"),
            sum("assetPrincipalValue").as("asset_principal_value"),
            sum("assetInterestValue").as("asset_interest_value"),
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