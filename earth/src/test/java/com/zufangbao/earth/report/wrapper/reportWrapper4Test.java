package com.zufangbao.earth.report.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zufangbao.earth.report.factory.SqlCacheManager;
import com.zufangbao.earth.report.util.FreemarkerUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/local/applicationContext-*.xml", "classpath:/DispatcherServlet.xml" })
@Transactional
@WebAppConfiguration(value="webapp")
public class reportWrapper4Test {

	@Autowired
	private SqlCacheManager sqlCacheManager;

	@Test
	public void testSql1() {
		String sqlTest = "SELECT c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, c.periods AS allPeriods, c.app_id AS appId, cu.`name` AS customerName, assetSet.single_loan_contract_no AS singleLoanContractNo, assetSet.asset_Recycle_date AS assetRecycleDate, assetSet.actual_recycle_date AS actualRecycleDate, assetSet.current_period AS currentPeriod, assetSet.overdue_date AS overdueDate, assetSet.asset_fair_value AS amount, assetSet.asset_principal_value AS assetPrincipalValue, assetSet.asset_interest_value AS assetInterestValue, assetSet.refund_amount AS refundAmount, assetSet.asset_status AS assetStatus, assetSet.overdue_status AS overdueStatus, assetSet.guarantee_status AS guaranteeStatus, assetSet.`comment` AS `comment`, assetSet.on_account_status AS onAccountStatus, assetSet.executing_status AS executingStatus, assetSet.time_interval AS timeInterval, assetSet.deduction_status AS deductionStatus, ( SELECT CONCAT_WS( \",\", SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END ) ) FROM asset_set_extra_charge asstec WHERE assetSet.asset_uuid = asstec.asset_set_uuid ) AS chargeArray, (CASE assetSet.on_account_status WHEN 3 THEN ( SELECT CONCAT_WS( \",\", SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_PRINCIPAL' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ) ) FROM ledger_book_shelf lbs WHERE lbs.related_lv_1_asset_uuid = assetSet.asset_uuid ) ELSE '' END ) AS actualChargeArray FROM asset_set assetSet INNER JOIN contract c ON c.id = assetSet.contract_id LEFT JOIN customer cu ON c.customer_id = cu.id WHERE assetSet.financial_contract_uuid IN (:financialContractUuids) AND assetSet.overdue_status IN (:overdueStatusList) AND (   )        ORDER BY assetSet.id DESC";
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractUuids", Arrays.asList(new String[] { "d2812bc5-5057-4a91-b3fd-9019506f0499",
				"eec7e6d8-6123-4a61-bcee-18085334c8ff", "d8d36e0d-06c6-4b17-b3aa-a1724ffb398e" }));
		params.put("overdueStatusList", Arrays.asList(new int[] { 1, 2 }));
		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper4"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}

	@Test
	public void testSql2() {
		String sqlTest = "SELECT c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, c.periods AS allPeriods, c.app_id AS appId, cu.`name` AS customerName, assetSet.single_loan_contract_no AS singleLoanContractNo, assetSet.asset_Recycle_date AS assetRecycleDate, assetSet.actual_recycle_date AS actualRecycleDate, assetSet.current_period AS currentPeriod, assetSet.overdue_date AS overdueDate, assetSet.asset_fair_value AS amount, assetSet.asset_principal_value AS assetPrincipalValue, assetSet.asset_interest_value AS assetInterestValue, assetSet.refund_amount AS refundAmount, assetSet.asset_status AS assetStatus, assetSet.overdue_status AS overdueStatus, assetSet.guarantee_status AS guaranteeStatus, assetSet.`comment` AS `comment`, assetSet.on_account_status AS onAccountStatus, assetSet.executing_status AS executingStatus, assetSet.time_interval AS timeInterval, assetSet.deduction_status AS deductionStatus, ( SELECT CONCAT_WS( \",\", SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END ) ) FROM asset_set_extra_charge asstec WHERE assetSet.asset_uuid = asstec.asset_set_uuid ) AS chargeArray, (CASE assetSet.on_account_status WHEN 3 THEN ( SELECT CONCAT_WS( \",\", SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_PRINCIPAL' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ) ) FROM ledger_book_shelf lbs WHERE lbs.related_lv_1_asset_uuid = assetSet.asset_uuid ) ELSE '' END ) AS actualChargeArray FROM asset_set assetSet INNER JOIN contract c ON c.id = assetSet.contract_id LEFT JOIN customer cu ON c.customer_id = cu.id WHERE assetSet.financial_contract_uuid IN (:financialContractUuids) AND assetSet.overdue_status IN (:overdueStatusList) AND (   ) AND assetSet.single_loan_contract_no =:singleLoanContractNo AND assetSet.asset_recycle_date >=:startDate AND assetSet.asset_recycle_date <=:endDate AND date(assetSet.actual_recycle_date) >=:actualStartDate AND date(assetSet.actual_recycle_date) <=:actualEndDate AND c.contract_no =:contractNo AND cu.name =:customerName ORDER BY assetSet.id DESC";
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractUuids", Arrays.asList(new String[] { "d2812bc5-5057-4a91-b3fd-9019506f0499",
				"eec7e6d8-6123-4a61-bcee-18085334c8ff", "d8d36e0d-06c6-4b17-b3aa-a1724ffb398e" }));
		params.put("overdueStatusList", Arrays.asList(new int[] { 1, 2 }));
		params.put("singleLoanContractNo", "ZC925109584054910976");
		params.put("startDate", "2015-01-01");
		params.put("endDate", "2017-02-15");
		params.put("actualStartDate", "2015-01-01");
		params.put("actualEndDate", "2017-02-15");
		params.put("contractNo", "wwtest--contract-2034-2034");
		params.put("customerName", "测试员2034");
		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper4"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}

	@Test
	public void testSql3() {
		String sqlTest = "SELECT c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, c.periods AS allPeriods, c.app_id AS appId, cu.`name` AS customerName, assetSet.single_loan_contract_no AS singleLoanContractNo, assetSet.asset_Recycle_date AS assetRecycleDate, assetSet.actual_recycle_date AS actualRecycleDate, assetSet.current_period AS currentPeriod, assetSet.overdue_date AS overdueDate, assetSet.asset_fair_value AS amount, assetSet.asset_principal_value AS assetPrincipalValue, assetSet.asset_interest_value AS assetInterestValue, assetSet.refund_amount AS refundAmount, assetSet.asset_status AS assetStatus, assetSet.overdue_status AS overdueStatus, assetSet.guarantee_status AS guaranteeStatus, assetSet.`comment` AS `comment`, assetSet.on_account_status AS onAccountStatus, assetSet.executing_status AS executingStatus, assetSet.time_interval AS timeInterval, assetSet.deduction_status AS deductionStatus, ( SELECT CONCAT_WS( \",\", SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END ) ) FROM asset_set_extra_charge asstec WHERE assetSet.asset_uuid = asstec.asset_set_uuid ) AS chargeArray, (CASE assetSet.on_account_status WHEN 3 THEN ( SELECT CONCAT_WS( \",\", SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_PRINCIPAL' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ) ) FROM ledger_book_shelf lbs WHERE lbs.related_lv_1_asset_uuid = assetSet.asset_uuid ) ELSE '' END ) AS actualChargeArray FROM asset_set assetSet INNER JOIN contract c ON c.id = assetSet.contract_id LEFT JOIN customer cu ON c.customer_id = cu.id WHERE assetSet.financial_contract_uuid IN (:financialContractUuids) AND assetSet.overdue_status IN (:overdueStatusList) AND (  ( assetSet.active_status IN (0,2) AND (   (assetSet.executing_status = 0 AND assetSet.time_interval <> 0)  OR    (assetSet.executing_status = 1 AND assetSet.deduction_status NOT IN (1,2))   ) )   )        ORDER BY assetSet.id DESC";
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractUuids", Arrays.asList(new String[] { "d2812bc5-5057-4a91-b3fd-9019506f0499",
				"eec7e6d8-6123-4a61-bcee-18085334c8ff", "d8d36e0d-06c6-4b17-b3aa-a1724ffb398e" }));
		params.put("overdueStatusList", Arrays.asList(new int[] { 1, 2 }));
		params.put("repaymentStatusList", new ArrayList<Integer>() {
			private static final long serialVersionUID = 1L;
			{
				add(1);
				add(2);
			}
		});
		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper4"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}

	@Test
	public void testSql4() {
		String sqlTest = "SELECT c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, c.periods AS allPeriods, c.app_id AS appId, cu.`name` AS customerName, assetSet.single_loan_contract_no AS singleLoanContractNo, assetSet.asset_Recycle_date AS assetRecycleDate, assetSet.actual_recycle_date AS actualRecycleDate, assetSet.current_period AS currentPeriod, assetSet.overdue_date AS overdueDate, assetSet.asset_fair_value AS amount, assetSet.asset_principal_value AS assetPrincipalValue, assetSet.asset_interest_value AS assetInterestValue, assetSet.refund_amount AS refundAmount, assetSet.asset_status AS assetStatus, assetSet.overdue_status AS overdueStatus, assetSet.guarantee_status AS guaranteeStatus, assetSet.`comment` AS `comment`, assetSet.on_account_status AS onAccountStatus, assetSet.executing_status AS executingStatus, assetSet.time_interval AS timeInterval, assetSet.deduction_status AS deductionStatus, ( SELECT CONCAT_WS( \",\", SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END ) ) FROM asset_set_extra_charge asstec WHERE assetSet.asset_uuid = asstec.asset_set_uuid ) AS chargeArray, (CASE assetSet.on_account_status WHEN 3 THEN ( SELECT CONCAT_WS( \",\", SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_PRINCIPAL' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ) ) FROM ledger_book_shelf lbs WHERE lbs.related_lv_1_asset_uuid = assetSet.asset_uuid ) ELSE '' END ) AS actualChargeArray FROM asset_set assetSet INNER JOIN contract c ON c.id = assetSet.contract_id LEFT JOIN customer cu ON c.customer_id = cu.id WHERE assetSet.financial_contract_uuid IN (:financialContractUuids) AND assetSet.overdue_status IN (:overdueStatusList) AND (  ( assetSet.active_status IN (0,2) AND (   (assetSet.executing_status = 0 AND assetSet.time_interval <> 0)  OR    (assetSet.executing_status = 1 AND assetSet.deduction_status NOT IN (1,2))  OR    (assetSet.executing_status = 1 AND assetSet.deduction_status IN (1,2))  OR    assetSet.executing_status = 2  OR    assetSet.executing_status = 3   ) )   )        ORDER BY assetSet.id DESC";
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractUuids", Arrays.asList(new String[] { "d2812bc5-5057-4a91-b3fd-9019506f0499",
				"eec7e6d8-6123-4a61-bcee-18085334c8ff", "d8d36e0d-06c6-4b17-b3aa-a1724ffb398e" }));
		params.put("overdueStatusList", Arrays.asList(new int[] { 1, 2 }));
		params.put("repaymentStatusList", new ArrayList<Integer>() {
			private static final long serialVersionUID = 1L;
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(6);
			}
		});
		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper4"), params);
			System.out.println(sql);
			System.out.println(sqlTest);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}

	@Test
	public void testSql5() {
		String sqlTest = "SELECT c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, c.periods AS allPeriods, c.app_id AS appId, cu.`name` AS customerName, assetSet.single_loan_contract_no AS singleLoanContractNo, assetSet.asset_Recycle_date AS assetRecycleDate, assetSet.actual_recycle_date AS actualRecycleDate, assetSet.current_period AS currentPeriod, assetSet.overdue_date AS overdueDate, assetSet.asset_fair_value AS amount, assetSet.asset_principal_value AS assetPrincipalValue, assetSet.asset_interest_value AS assetInterestValue, assetSet.refund_amount AS refundAmount, assetSet.asset_status AS assetStatus, assetSet.overdue_status AS overdueStatus, assetSet.guarantee_status AS guaranteeStatus, assetSet.`comment` AS `comment`, assetSet.on_account_status AS onAccountStatus, assetSet.executing_status AS executingStatus, assetSet.time_interval AS timeInterval, assetSet.deduction_status AS deductionStatus, ( SELECT CONCAT_WS( \",\", SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END ) ) FROM asset_set_extra_charge asstec WHERE assetSet.asset_uuid = asstec.asset_set_uuid ) AS chargeArray, (CASE assetSet.on_account_status WHEN 3 THEN ( SELECT CONCAT_WS( \",\", SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_PRINCIPAL' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ) ) FROM ledger_book_shelf lbs WHERE lbs.related_lv_1_asset_uuid = assetSet.asset_uuid ) ELSE '' END ) AS actualChargeArray FROM asset_set assetSet INNER JOIN contract c ON c.id = assetSet.contract_id LEFT JOIN customer cu ON c.customer_id = cu.id WHERE assetSet.financial_contract_uuid IN (:financialContractUuids) AND assetSet.overdue_status IN (:overdueStatusList) AND (  ( assetSet.active_status IN (0,2) AND (   (assetSet.executing_status = 0 AND assetSet.time_interval <> 0)  OR    (assetSet.executing_status = 1 AND assetSet.deduction_status NOT IN (1,2))  OR    (assetSet.executing_status = 1 AND assetSet.deduction_status IN (1,2))  OR    assetSet.executing_status = 2  OR    assetSet.executing_status = 3   ) )    OR  assetSet.executing_status = 4  )        ORDER BY assetSet.id DESC";
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractUuids", Arrays.asList(new String[] { "d2812bc5-5057-4a91-b3fd-9019506f0499",
				"eec7e6d8-6123-4a61-bcee-18085334c8ff", "d8d36e0d-06c6-4b17-b3aa-a1724ffb398e" }));
		params.put("overdueStatusList", Arrays.asList(new int[] { 1, 2 }));
		params.put("repaymentStatusList", new ArrayList<Integer>() {
			private static final long serialVersionUID = 1L;
			{
				add(1);
				add(2);
				add(3);
				add(4);
				add(6);
			}
		});
		params.put("haveRepurchased", true);
		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper4"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}
	@Test
	public void testSql6() {
		String sqlTest = "SELECT c.unique_id AS uniqueId, c.contract_no AS contractNo, c.begin_date AS beginDate, c.periods AS allPeriods, c.app_id AS appId, cu.`name` AS customerName, assetSet.single_loan_contract_no AS singleLoanContractNo, assetSet.asset_Recycle_date AS assetRecycleDate, assetSet.actual_recycle_date AS actualRecycleDate, assetSet.current_period AS currentPeriod, assetSet.overdue_date AS overdueDate, assetSet.asset_fair_value AS amount, assetSet.asset_principal_value AS assetPrincipalValue, assetSet.asset_interest_value AS assetInterestValue, assetSet.refund_amount AS refundAmount, assetSet.asset_status AS assetStatus, assetSet.overdue_status AS overdueStatus, assetSet.guarantee_status AS guaranteeStatus, assetSet.`comment` AS `comment`, assetSet.on_account_status AS onAccountStatus, assetSet.executing_status AS executingStatus, assetSet.time_interval AS timeInterval, assetSet.deduction_status AS deductionStatus, ( SELECT CONCAT_WS( \",\", SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END ) ) FROM asset_set_extra_charge asstec WHERE assetSet.asset_uuid = asstec.asset_set_uuid ) AS chargeArray, (CASE assetSet.on_account_status WHEN 3 THEN ( SELECT CONCAT_WS( \",\", SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_PRINCIPAL' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ) ) FROM ledger_book_shelf lbs WHERE lbs.related_lv_1_asset_uuid = assetSet.asset_uuid ) ELSE '' END ) AS actualChargeArray FROM asset_set assetSet INNER JOIN contract c ON c.id = assetSet.contract_id LEFT JOIN customer cu ON c.customer_id = cu.id WHERE assetSet.financial_contract_uuid IN (:financialContractUuids) AND assetSet.overdue_status IN (:overdueStatusList) AND (    assetSet.executing_status = 4  )        ORDER BY assetSet.id DESC";
		Map<String, Object> params = new HashMap<>();
		params.put("financialContractUuids", Arrays.asList(new String[] { "d2812bc5-5057-4a91-b3fd-9019506f0499",
				"eec7e6d8-6123-4a61-bcee-18085334c8ff", "d8d36e0d-06c6-4b17-b3aa-a1724ffb398e" }));
		params.put("overdueStatusList", Arrays.asList(new int[] { 1, 2 }));
		params.put("repaymentStatusList", new ArrayList<Integer>());
		params.put("haveRepurchased", true);
		String sql = null;
		try {
			sql = FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper4"), params);
			System.out.println(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest.trim(), sql.trim());
	}
}
