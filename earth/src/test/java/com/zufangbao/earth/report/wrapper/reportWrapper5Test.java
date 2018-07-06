package com.zufangbao.earth.report.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

		"classpath:/local/applicationContext-*.xml",
		"classpath:/DispatcherServlet.xml"})
@Transactional
@WebAppConfiguration(value="webapp")
public class reportWrapper5Test {

	
	@Autowired
	private SqlCacheManager sqlCacheManager;
	
	
	@Test
	public void testSql1(){
		String sqlTest="SELECT cu.name AS customerName, c.contract_no AS contractNo, c.unique_id AS uniqueId, c.periods AS allPeriods, assetSet.single_loan_contract_no AS singleLoanContractNo, assetSet.current_period AS currentPeriod, assetSet.asset_principal_value AS assetPrincipalValue, assetSet.asset_interest_value AS assetInterestValue, assetSet.asset_fair_value AS amount, assetSet.asset_Recycle_date AS assetRecycleDate, assetSet.actual_recycle_date AS actualRecycleDate, assetSet.asset_initial_value AS assetInitialValue, assetSet.refund_amount AS refundAmount, assetSet.executing_status AS executingStatus, assetSet.time_interval AS timeInterval, assetSet.overdue_status AS overdueStatus, assetSet.guarantee_status AS guaranteeStatus, assetSet.comment AS comment, assetSet.asset_status AS assetStatus, assetSet.on_account_status AS onAccountStatus, assetSet.overdue_date AS overdueDate, assetSet.deduction_status AS deductionStatus, assetSet.financial_contract_uuid AS financialContractUuid, ( SELECT CONCAT_WS( \",\", SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END ) ) FROM asset_set_extra_charge asstec WHERE assetSet.asset_uuid = asstec.asset_set_uuid ) AS chargeArray, (CASE assetSet.on_account_status WHEN 3 THEN ( SELECT CONCAT_WS( \",\", SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_PRINCIPAL' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ) ) FROM ledger_book_shelf lbs WHERE lbs.related_lv_1_asset_uuid = assetSet.asset_uuid ) ELSE '' END ) AS actualChargeArray FROM asset_set assetSet INNER JOIN contract c ON c.id = assetSet.contract_id LEFT JOIN customer cu ON c.customer_id = cu.id WHERE assetSet.financial_contract_uuid IN (:financialContractUuids)  AND(     (assetSet.executing_status = 1 AND assetSet.deduction_status NOT IN (1,2))            OR(assetSet.executing_status = 1 AND assetSet.deduction_status IN (1,2))            OR(assetSet.executing_status = 2 )      )  AND assetSet.overdue_status IN (:overdueStatusList) AND assetSet.active_status = 0  AND assetSet.single_loan_contract_no =:singleLoanContractNo   AND assetSet.asset_recycle_date >=:startDate   AND assetSet.asset_recycle_date <=:endDate   AND date(assetSet.actual_recycle_date) >=:actualStartDate   AND date(assetSet.actual_recycle_date) <=:actualEndDate   AND c.contract_no =:contractNo   AND cu.name =:customerName  ORDER BY assetSet.id DESC";
		Map<String, Object> params=new HashMap<>();
		List<Integer> repaymentStatusList=new ArrayList<Integer>(){
			{
				add(2);
				add(3);
				add(4);
			}
		};
		
		
		List<Integer> overdueStatusList=new ArrayList<Integer>(){
			{
				add(0);
				add(1);
				add(2);
			}
		};
		
		List<String> financialContractUuids=new ArrayList<String>(){
			{
				add("asdfasfasfasfadf");
				add("543213531");
				add("adfadfasfadfadfafasf");
			}
		};
		
		params.put("financialContractUuids",financialContractUuids);
		params.put("repaymentStatusList", repaymentStatusList);
		params.put("overdueStatusList", overdueStatusList);
		params.put("singleLoanContractNo", "1234567899897");
		params.put("startDate", "2017-12-11");
		params.put("endDate", "2017-12-11");
		params.put("actualStartDate", "2017-12-11");
		params.put("actualEndDate", "456231656");

		params.put("activeStatus", 1);
		params.put("contractNo", "asdfasdfasdad4564123123");
		params.put("customerName", "zhanglongfei");
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper5"), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
	
	
	@Test
	public void testSql2(){
		String sqlTest="SELECT cu.name AS customerName, c.contract_no AS contractNo, c.unique_id AS uniqueId, c.periods AS allPeriods, assetSet.single_loan_contract_no AS singleLoanContractNo, assetSet.current_period AS currentPeriod, assetSet.asset_principal_value AS assetPrincipalValue, assetSet.asset_interest_value AS assetInterestValue, assetSet.asset_fair_value AS amount, assetSet.asset_Recycle_date AS assetRecycleDate, assetSet.actual_recycle_date AS actualRecycleDate, assetSet.asset_initial_value AS assetInitialValue, assetSet.refund_amount AS refundAmount, assetSet.executing_status AS executingStatus, assetSet.time_interval AS timeInterval, assetSet.overdue_status AS overdueStatus, assetSet.guarantee_status AS guaranteeStatus, assetSet.comment AS comment, assetSet.asset_status AS assetStatus, assetSet.on_account_status AS onAccountStatus, assetSet.overdue_date AS overdueDate, assetSet.deduction_status AS deductionStatus, assetSet.financial_contract_uuid AS financialContractUuid, ( SELECT CONCAT_WS( \",\", SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_LOAN_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_OTHER_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_UNEARNED_LOAN_ASSET_TECH_FEE' THEN account_amount ELSE 0 END ), SUM( CASE second_account_name WHEN 'SND_RECIEVABLE_LOAN_PENALTY' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OBLIGATION' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_SERVICE_FEE' THEN account_amount ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_RECIEVABLE_OVERDUE_FEE_OTHER_FEE' THEN account_amount ELSE 0 END ) ) FROM asset_set_extra_charge asstec WHERE assetSet.asset_uuid = asstec.asset_set_uuid ) AS chargeArray, (CASE assetSet.on_account_status WHEN 3 THEN ( SELECT CONCAT_WS( \",\", SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_PRINCIPAL' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_PRINCIPAL' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_INTEREST' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_INTEREST' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_LOAN_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_TECH_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_PENALTY' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OBLIGATION' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_SERVICE_FEE' THEN debit_balance - credit_balance ELSE 0 END ), SUM( CASE third_account_name WHEN 'TRD_BANK_SAVING_GENERAL_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance WHEN 'TRD_LIABILITIES_INDEPENDENT_REIMBURSE_LOAN_ASSET_OVERDUE_FEE_OTHER_FEE' THEN debit_balance - credit_balance ELSE 0 END ) ) FROM ledger_book_shelf lbs WHERE lbs.related_lv_1_asset_uuid = assetSet.asset_uuid ) ELSE '' END ) AS actualChargeArray FROM asset_set assetSet INNER JOIN contract c ON c.id = assetSet.contract_id LEFT JOIN customer cu ON c.customer_id = cu.id WHERE assetSet.financial_contract_uuid IN (:financialContractUuids)  AND(      (assetSet.executing_status = 1 AND assetSet.deduction_status IN (1,2))            OR(assetSet.executing_status = 2 )            OR(assetSet.executing_status = 3 )            OR(assetSet.executing_status = 4 )    )  AND assetSet.overdue_status IN (:overdueStatusList) AND assetSet.active_status = 0  AND assetSet.single_loan_contract_no =:singleLoanContractNo   AND assetSet.asset_recycle_date >=:startDate   AND assetSet.asset_recycle_date <=:endDate   AND date(assetSet.actual_recycle_date) >=:actualStartDate   AND date(assetSet.actual_recycle_date) <=:actualEndDate   AND c.contract_no =:contractNo   AND cu.name =:customerName  ORDER BY assetSet.id DESC";
		Map<String, Object> params=new HashMap<>();
		List<Integer> repaymentStatusList=new ArrayList<Integer>(){
			{
				add(3);
				add(4);
				add(6);
				add(7);
				
			}
		};
		
		
		List<Integer> overdueStatusList=new ArrayList<Integer>(){
			{
				add(0);
				add(2);
			}
		};
		
		List<String> financialContractUuids=new ArrayList<String>(){
			{
				add("asdfasfasfasfadf");
				add("543213531");
				add("adfadfasfadfadfafasf");
			}
		};
		
		params.put("financialContractUuids",financialContractUuids);
		params.put("repaymentStatusList", repaymentStatusList);
		params.put("overdueStatusList", overdueStatusList);
		params.put("singleLoanContractNo", "1234567899897");
		params.put("startDate", "2017-12-11");
		params.put("endDate", "2017-12-11");
		params.put("actualStartDate", "2017-12-11");
		params.put("actualEndDate", "456231656");

		params.put("activeStatus", 1);
		params.put("contractNo", "asdfasdfasdad4564123123");
		params.put("customerName", "zhanglongfei");
		String sql=null;
		try {
			 sql=FreemarkerUtil.process(sqlCacheManager.getCachedSqlMap().get("reportWrapper5"), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.assertEquals(sqlTest, sql);
	}
	
}
